package com.cli.knowledgebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.QuizDataAdapter;
import Constants.Const;
import DataBase.DataBaseHelper;
import Interfaces.AsyncTaskCompleteListener;
import Interfaces.RecyclerViewItemClickListener;
import Model.Login;
import Model.QuizData;
import Model.Specialization;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import Utils.ViewUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class Quiz extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener, AsyncTaskCompleteListener {


    TextView tv_menu_name;
    ImageView iv_back;

    Specialization specialization;

    ArrayList<QuizData> quizData = new ArrayList<>();
    RecyclerView recycler_view_quiz_data;

    PreferenceHelper preferenceHelper;

    QuizDataAdapter adapter;
    ApiInterface apiInterface;

    Login loginDetails;
    ImageView iv_nodata;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);
        dataBaseHelper= new DataBaseHelper(this);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);


        specialization = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).
                getParcelable(Const.IntentParams.SPECIALIZATION_DATA);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        widgets();

        getQuiz();

        // dummyData();
    }

    private void widgets() {

        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(specialization.getName() + " " + getString(R.string.quiz));

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        recycler_view_quiz_data = (RecyclerView) findViewById(R.id.recycler_view_quiz_data);
        recycler_view_quiz_data.setLayoutManager(new LinearLayoutManager(this));

        iv_nodata = (ImageView) findViewById(R.id.iv_nodata);

    }


    private void getQuiz() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.COURSE_ID, "" + specialization.getId());
        param.put(Const.Params.SEMESTER, loginDetails.getUser().getSemester());
        param.put(Const.Params.YEAR, loginDetails.getUser().getYear());
        param.put(Const.Params.SPECIALIZATIONID, "" + loginDetails.getUser().getSpecializationId());
        param.put(Const.Params.ADMISSION_YEAR, loginDetails.getUser().getAdmissionYear());
        param.put(Const.Params.UNIVERSITY_ID, loginDetails.getUser().getUniversityId());
        param.put(Const.Params.MOBILE, "true");


        Call<ResponseBody> call = apiInterface.getQuiz(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_QUIZ_DATA);
    }


    private void getQuizResult() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.STUDENT_ID, "" + loginDetails.getUser().getId());

        Call<ResponseBody> call = apiInterface.getQuizResult(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_QUIZ_Result);
    }


    private void loadView() {

        if (quizData != null && quizData.size() > 0) {

            Collections.sort(quizData);

            adapter = new QuizDataAdapter(this, quizData, this);
            recycler_view_quiz_data.setAdapter(adapter);
            recycler_view_quiz_data.setVisibility(View.VISIBLE);
            iv_nodata.setVisibility(View.GONE);

        } else {

            recycler_view_quiz_data.setVisibility(View.GONE);
            iv_nodata.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;

        }
    }

    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.IntentParams.QUIZ_DATA, quizData.get(position));
        ViewUtils.launchActivity(this, bundle, PlayQuiz.class);


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {

            case Const.ServiceCode.GET_QUIZ_DATA:


                Gson gson = new Gson();
                quizData = gson.fromJson(response, new TypeToken<List<QuizData>>() {
                }.getType());

                // if (quizData != null)

                getQuizResult();

                break;

            case Const.ServiceCode.GET_QUIZ_Result:

                AndyUtils.cancelLoading();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        dataBaseHelper.insertQuizDetails(jsonObject.getString(Const.Json_Parms.QUIZ_ID),
                                jsonObject.getString(Const.Json_Parms.SCORE),Const.STATUS.UPDATED);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                loadView();

                break;

        }
    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.notifyDataSetChanged();

        }
    }


}

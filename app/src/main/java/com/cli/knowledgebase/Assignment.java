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
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.AssignmentDataAdapter;
import Constants.Const;
import DataBase.DataBaseHelper;
import Interfaces.AsyncTaskCompleteListener;
import Interfaces.RecyclerViewItemClickListener;
import Model.AssignmentData;
import Model.Login;
import Model.Specialization;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import Utils.ViewUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class Assignment extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener, RecyclerViewItemClickListener {

    PreferenceHelper preferenceHelper;
    ApiInterface apiInterface;
    Login loginDetails;
    TextView tv_menu_name;
    ImageView iv_back;

    Specialization specialization;
    ArrayList<AssignmentData> assignmentData;

    RecyclerView recycler_view_assignment_data;
    AssignmentDataAdapter adapter;
    ImageView iv_nodata;

    String[] items = {"All", "Assignment", "Practice Assignment", "Wiki Assignment", "BlogAssignment"};
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);
        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);

        dataBaseHelper = new DataBaseHelper(this);


        specialization = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).
                getParcelable(Const.IntentParams.SPECIALIZATION_DATA);

        widgets();
        getAssignments();

    }

    private void widgets() {

        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(specialization.getName() + " " + getString(R.string.assignments));

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        recycler_view_assignment_data = (RecyclerView) findViewById(R.id.recycler_view_assignment_data);
        recycler_view_assignment_data.setLayoutManager(new LinearLayoutManager(this));

        iv_nodata = (ImageView) findViewById(R.id.iv_nodata);


        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems(items);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (adapter != null) {


                    switch (position) {

                        case 0:

                            adapter.filterList("");

                            break;

                        case 1:

                            adapter.filterList("assignment");

                            break;

                        case 2:

                            adapter.filterList("practiceAssignment");


                            break;

                        case 3:
                            adapter.filterList("wikiAssignment");


                            break;

                        case 4:

                            adapter.filterList("blogAssignment");

                            break;
                    }

                }

            }
        });
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;

        }
    }


    private void getAssignments() {
        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.COURSE_ID, "" + specialization.getId());
        param.put(Const.Params.SEMESTER, loginDetails.getUser().getSemester());
        param.put(Const.Params.YEAR, loginDetails.getUser().getYear());
        param.put(Const.Params.SPECIALIZATIONID, "" + loginDetails.getUser().getSpecializationId());
        param.put(Const.Params.ADMISSION_YEAR, loginDetails.getUser().getAdmissionYear());
        param.put(Const.Params.UNIVERSITY_ID, loginDetails.getUser().getUniversityId());
        param.put(Const.Params.MOBILE, "true");

        Call<ResponseBody> call = apiInterface.getAssignments(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_ASSIGNMENT_DATA);

    }


    private void getAssignmentsAnswer() {
        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.STUDENT_ID, "" + loginDetails.getUser().getId());
        Call<ResponseBody> call = apiInterface.getAssignmentsAnswer(param);
        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_ASSIGNMENT_ANSWER);

    }


    private void getAssignmentsResult() {

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.STUDENT_ID, "" + loginDetails.getUser().getId());
        Call<ResponseBody> call = apiInterface.getAssignmentsResult(param);
        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_ASSIGNMENT_RESULT);

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {

            case Const.ServiceCode.GET_ASSIGNMENT_DATA:


                Gson gson = new Gson();
                assignmentData = gson.fromJson(response, new TypeToken<List<AssignmentData>>() {
                }.getType());


                getAssignmentsAnswer();

                //loadData();
                break;

            case Const.ServiceCode.GET_ASSIGNMENT_ANSWER:

                AndyUtils.cancelLoading();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        dataBaseHelper.insertAssignmentDetails(new AssignmentData(jsonObject.getString(Const.Json_Parms.QUESTION_ID),
                                        jsonObject.getString(Const.Json_Parms.ANSWER),
                                        jsonObject.getString(Const.Json_Parms.FILE_ID)),
                                Const.STATUS.UPDATED);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getAssignmentsResult();
                loadData();
                break;


            case Const.ServiceCode.GET_ASSIGNMENT_RESULT:


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        dataBaseHelper.updateAssignmentScore(jsonObject.getString(Const.Json_Parms.QUESTION_ID),
                                jsonObject.getString(Const.Json_Parms.MARKS));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (adapter != null)
                    adapter.notifyDataSetChanged();


                break;

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private void loadData() {

        if (assignmentData != null && assignmentData.size() > 0) {

            Collections.sort(assignmentData);

            adapter = new AssignmentDataAdapter(this, assignmentData, this);
            recycler_view_assignment_data.setAdapter(adapter);
            recycler_view_assignment_data.setVisibility(View.VISIBLE);
            iv_nodata.setVisibility(View.GONE);

        } else {

            recycler_view_assignment_data.setVisibility(View.GONE);
            iv_nodata.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }

    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.IntentParams.ASSIGNMENT_DATA, adapter.getList().get(position));
        ViewUtils.launchActivity(this, bundle, SubmitAssignment.class);

    }
}

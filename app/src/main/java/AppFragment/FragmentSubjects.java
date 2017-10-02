package AppFragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cli.knowledgebase.Assignment;
import com.cli.knowledgebase.NotesActivity;
import com.cli.knowledgebase.Quiz;
import com.cli.knowledgebase.R;
import com.cli.knowledgebase.ScormActivity;
import com.cli.knowledgebase.SessionPlan;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Adapter.SpecializationAdapter;
import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Interfaces.RecyclerViewItemClickListener;
import Model.Login;
import Model.Specialization;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.AppLog;
import Utils.PreferenceHelper;
import Utils.ViewUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class FragmentSubjects extends Fragment implements AsyncTaskCompleteListener, RecyclerViewItemClickListener {


    ApiInterface apiInterface;
    Login loginDetails;
    PreferenceHelper preferenceHelper;
    ArrayList<Specialization> specialization = new ArrayList<>();
    RecyclerView recycler_view_specialization;
    SpecializationAdapter adapter;

    Dialog listDialog;
    ImageView iv_emptyview;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.fragment_subjects, container, false);

        preferenceHelper = new PreferenceHelper(mActivity);
        apiInterface = ApiClient.getBaseClient(mActivity).create(ApiInterface.class);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);

        widgets(rootView);

        getCourseBySpecialization();


        return rootView;
    }

    private void widgets(View rootView) {

        recycler_view_specialization = (RecyclerView) rootView.findViewById(R.id.recycler_view_specialization);
        recycler_view_specialization.setLayoutManager(new GridLayoutManager(mActivity, 3));

        iv_emptyview = (ImageView) rootView.findViewById(R.id.iv_emptyview);
    }


    Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }


    private void getCourseBySpecialization() {

        AndyUtils.showLoading(mActivity, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.SPECIALIZATION_ID, loginDetails.getUser().getSpecializationId());


        Call<ResponseBody> call = apiInterface.getCourseBySpecialization(param);

        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_COURSE_SPECIALIZATION);
    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        AndyUtils.cancelLoading();

        switch (serviceCode) {

            case Const.ServiceCode.GET_COURSE_SPECIALIZATION:

                AppLog.Log("pavan", response);

                try {
                    JSONObject jsonObject = new JSONObject(response).getJSONObject(Const.Json_Parms.COURSE);

                    Iterator<?> keys = jsonObject.keys();

                    while (keys.hasNext()) {

                        String key = (String) keys.next();

                        JSONObject object = jsonObject.getJSONObject(key);

                        int position = 0;

                        if (specialization.size() > 0) {
                            position = (specialization.size()) % Const.menu_bg.length;

                        }

                        specialization.add(new Specialization(object.getInt(Const.Json_Parms.ID),
                                object.getString(Const.Json_Parms.CATEGORY),
                                object.getString(Const.Json_Parms.FULL_NAME), "" +
                                Const.menu_bg[position]));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loadView();


                break;

        }


    }

    private void loadView() {


        if (specialization != null && specialization.size() > 0) {

            iv_emptyview.setVisibility(View.GONE);
            recycler_view_specialization.setVisibility(View.VISIBLE);

            if (adapter == null) {

                adapter = new SpecializationAdapter(mActivity, specialization, this);
                recycler_view_specialization.setAdapter(adapter);

            } else {

                adapter.notifyDataSetChanged();
            }
        } else {

            iv_emptyview.setVisibility(View.VISIBLE);
            recycler_view_specialization.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }

    @Override
    public void onItemClick(int position) {

        showDialog(specialization.get(position));

    }


    private void showDialog(final Specialization specialization) {

        String[] val = {mActivity.getString(R.string.read_course_content),
                mActivity.getString(R.string.quiz),
                mActivity.getString(R.string.assignments),
                mActivity.getString(R.string.session_plan),
                mActivity.getString(R.string.notes)};


        listDialog = new Dialog(mActivity);
        listDialog.setTitle(specialization.getName());
        LayoutInflater li = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.list_dialog, null, false);
        listDialog.setContentView(v);
        listDialog.setCancelable(true);
        //there are a lot of settings, for dialog, check them all out!

        ListView list1 = (ListView) listDialog.findViewById(R.id.listview);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listDialog.dismiss();

                switch (position) {

                    case 0:

                        Bundle bundle_scorm = new Bundle();
                        bundle_scorm.putParcelable(Const.IntentParams.SPECIALIZATION_DATA, specialization);
                        ViewUtils.launchActivity(mActivity, bundle_scorm, ScormActivity.class);

                        break;

                    case 1:

                        Bundle bundle = new Bundle();
                        bundle.putParcelable(Const.IntentParams.SPECIALIZATION_DATA, specialization);
                        ViewUtils.launchActivity(mActivity, bundle, Quiz.class);

                        break;

                    case 2:

                        Bundle bundle1 = new Bundle();
                        bundle1.putParcelable(Const.IntentParams.SPECIALIZATION_DATA, specialization);
                        ViewUtils.launchActivity(mActivity, bundle1, Assignment.class);

                        break;

                    case 3:

                        Bundle bundle_session_plan = new Bundle();
                        bundle_session_plan.putParcelable(Const.IntentParams.SPECIALIZATION_DATA, specialization);
                        ViewUtils.launchActivity(mActivity, bundle_session_plan, SessionPlan.class);

                        break;

                    case 4:

                        Bundle bundle_notes = new Bundle();
                        bundle_notes.putParcelable(Const.IntentParams.SPECIALIZATION_DATA, specialization);
                        ViewUtils.launchActivity(mActivity, bundle_notes, NotesActivity.class);

                        break;

                    default:

                        Toast.makeText(mActivity, "Coming soon", Toast.LENGTH_LONG).show();

                        break;

                }

            }
        });
        list1.setAdapter(new ArrayAdapter<String>(mActivity, R.layout.item_list_dialog, val));
        //now that the dialog is set up, it's time to show it
        listDialog.show();
    }
}
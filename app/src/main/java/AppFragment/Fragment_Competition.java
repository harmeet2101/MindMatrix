package AppFragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cli.knowledgebase.CompetitionDetailsActivity;
import com.cli.knowledgebase.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.CompetitionAdapter;
import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Interfaces.RecyclerViewItemClickListener;
import Model.AssignmentDetails;
import Model.CompetitionData;
import Model.Login;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import Utils.ViewUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static Constants.Const.Params.ENTITY_TYPE_ID;


public class Fragment_Competition extends Fragment implements AsyncTaskCompleteListener, RecyclerViewItemClickListener {


    ApiInterface apiInterface;
    Login loginDetails;
    PreferenceHelper preferenceHelper;

    ArrayList<CompetitionData> competitionData = new ArrayList<>();
    ImageView iv_emptyview;
    RecyclerView recycler_view_competition;

    CompetitionAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.fragment_competition, container, false);

        preferenceHelper = new PreferenceHelper(mActivity);
        apiInterface = ApiClient.getBaseClient(mActivity).create(ApiInterface.class);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);

        widgets(rootView);

        getCompetition();


        return rootView;
    }

    private void widgets(View rootView) {

        iv_emptyview = (ImageView) rootView.findViewById(R.id.iv_emptyview);

        recycler_view_competition = (RecyclerView) rootView.findViewById(R.id.recycler_view_competition);
        recycler_view_competition.setLayoutManager(new LinearLayoutManager(mActivity));


    }


    public void getCompetition() {

        AndyUtils.showLoading(mActivity, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.SPECIALIZATION_ID, loginDetails.getUser().getSpecializationId());
        param.put(Const.Params.SEM_ID, "" + loginDetails.getUser().getSemester());
        param.put(Const.Params.YEAR, loginDetails.getUser().getYear());
        param.put(Const.Params.STATUS, "2");

        Call<ResponseBody> call = apiInterface.getListOfScenarioByOptions(param);

        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_COMPETITION);
    }

    Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {

            case Const.ServiceCode.GET_COMPETITION:

                AndyUtils.cancelLoading();

                if (response != null && !response.equals("") && !response.equals("null")) {


                    competitionData = new Gson().fromJson(response, new TypeToken<List<CompetitionData>>() {
                    }.getType());


                    loadData();


                }


                break;


            case Const.ServiceCode.GET_ALL_FILES:


                AndyUtils.cancelLoading();

                Gson gson = new Gson();
                List<AssignmentDetails> details = gson.fromJson(response, new TypeToken<List<AssignmentDetails>>() {
                }.getType());

                if (details != null && details.size() > 0) {

                    if (competitionData != null && competitionData.size() > 0)
                        competitionData.get(0).setFilepath(details.get(0).getFullPathToFile());

                    if (adapter != null)
                        adapter.notifyDataSetChanged();


                }

                //enableUploadBtn();


                break;

        }

    }

    private void loadData() {

        if (competitionData != null && competitionData.size() > 0) {

            iv_emptyview.setVisibility(View.GONE);
            recycler_view_competition.setVisibility(View.VISIBLE);

            Collections.sort(competitionData);

            if (adapter == null) {

                adapter = new CompetitionAdapter(mActivity, competitionData, this);
                recycler_view_competition.setAdapter(adapter);

            } else {

                adapter.notifyDataSetChanged();
            }

            getFileDetails(competitionData.get(0).getId());


        } else {

            iv_emptyview.setVisibility(View.VISIBLE);
            recycler_view_competition.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }

    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.IntentParams.COMPETITION_DATA, competitionData.get(position));
        ViewUtils.launchActivity(mActivity, bundle, CompetitionDetailsActivity.class);

    }


    private void getFileDetails(String id) {

        AndyUtils.showLoading(mActivity, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(ENTITY_TYPE_ID, id);
        param.put(Const.Params.ENTITY_TYPE_NAME, "FCompetition");

        Call<ResponseBody> call = apiInterface.getAllFiles(param);

        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_ALL_FILES);

    }
}
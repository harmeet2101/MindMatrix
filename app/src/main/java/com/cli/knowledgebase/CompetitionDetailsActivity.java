package com.cli.knowledgebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.TeamMemberAdapter;
import Adapter.TeammateAutoCompleteAdapter;
import AppFragment.AllFilesFragment;
import AppFragment.WebViewFragment;
import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Interfaces.RecyclerViewItemClickListener;
import Model.AssignmentDetails;
import Model.CompetitionData;
import Model.FileDetails;
import Model.Login;
import Model.TeamDetails;
import Model.TeamMember;
import Model.TeammatesData;
import Model.UserDetails;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import Utils.TimeUtils;
import Utils.ViewUtils;
import app.AppController;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static Constants.Const.Params.ENTITY_TYPE_ID;
import static Constants.Const.ServiceCode.CREATE_MILESTONE;

public class CompetitionDetailsActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener, RecyclerViewItemClickListener {

    PreferenceHelper preferenceHelper;
    ApiInterface apiInterface;
    Login loginDetails;
    TextView tv_menu_name;
    ImageView iv_back;
    EditText et_group_name;
    TextView tv_save;

    CompetitionData competitionData;
    String team_id = "";

    TeamDetails teamDetails;

    LinearLayout lay_team_leader, lay_team_member;
    ImageView iv_leader;
    TextView tv_leader_name, tv_created_date;
    ArrayList<TeammatesData> teammatesData = new ArrayList<>();

    AutoCompleteTextView autoCompleteTextView_team_member;
    RecyclerView recycler_view_team_members;

    TeamMemberAdapter teamMemberAdapter;
    ArrayList<TeamMember> teamMember = new ArrayList<>();

    TextView tv_upload_file, tv_view_file;

    String file_path = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);
        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);

        competitionData = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).
                getParcelable(Const.IntentParams.COMPETITION_DATA);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Const.IntentFilter.SELECT_PATH));


        widgets();

        AndyUtils.showLoading(this, false, "");
        getTeamDetails(loginDetails.getUser().getId());

    }

    private void widgets() {

        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(getString(R.string.competition));

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        tv_upload_file = (TextView) findViewById(R.id.tv_upload_file);
        tv_upload_file.setOnClickListener(this);

        tv_view_file = (TextView) findViewById(R.id.tv_view_file);
        tv_view_file.setOnClickListener(this);


        recycler_view_team_members = (RecyclerView) findViewById(R.id.recycler_view_team_members);
        recycler_view_team_members.setLayoutManager(new LinearLayoutManager(this));

        et_group_name = (EditText) findViewById(R.id.et_group_name);
        et_group_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals("")) {

                    tv_save.setEnabled(true);

                } else {

                    tv_save.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);

        lay_team_member = (LinearLayout) findViewById(R.id.lay_team_member);


        lay_team_leader = (LinearLayout) findViewById(R.id.lay_team_leader);
        tv_leader_name = (TextView) findViewById(R.id.tv_leader_name);
        tv_created_date = (TextView) findViewById(R.id.tv_created_date);
        iv_leader = (ImageView) findViewById(R.id.iv_leader);

        autoCompleteTextView_team_member = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_team_member);


        autoCompleteTextView_team_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ViewUtils.closeKeyboard(CompetitionDetailsActivity.this);


                autoCompleteTextView_team_member.setText("");

                if (checkForDuplicate(teammatesData.get(position))) {

                    sendInviteToJoinGroup(teammatesData.get(position));
                    ArrayList<UserDetails> teamMemberDetails = new ArrayList<UserDetails>();
                    teamMemberDetails.add(new UserDetails(teammatesData.get(position).getId(),
                            teammatesData.get(position).getFirstname(), teammatesData.get(position).getLastname(),
                            teammatesData.get(position).getEmail(), ""));

                    ArrayList<UserDetails> admin = new ArrayList<UserDetails>();

                    admin.add(new UserDetails(loginDetails.getUser().getId(),
                            loginDetails.getUser().getFirstname(), loginDetails.getUser().getLastname(),
                            loginDetails.getUser().getEmail(), loginDetails.getUser().getProfileUrl()));

                    TeamMember member = new TeamMember("0", admin
                            , teamMemberDetails, "0", TimeUtils.getGmtTime(), competitionData.getId(), "");

                    if (teamMember == null)
                        teamMember = new ArrayList<TeamMember>();

                    teamMember.add(member);
                    loadTeamMember();
                }

            }
        });


    }

    private boolean checkForDuplicate(TeammatesData teammatesData) {

        for (int i = 0; i < teamMember.size(); i++) {

            if (teamMember.get(i).getTeamMemberUserId().get(0).getId().equals(teammatesData.getId())) {

                Toast.makeText(CompetitionDetailsActivity.this, getString(R.string.member_exist), Toast.LENGTH_LONG).show();
                return false;
            }

        }

        return true;

    }

    private void sendInviteToJoinGroup(TeammatesData teammatesData) {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.TEAM_LEAD_ID, teamDetails.getTeamLeadId().get(0).getId());
        param.put(Const.Params.SCENARIO_ID, competitionData.getId());
        param.put(Const.Params.TEAM_MEMBER_USER_ID, teammatesData.getId());
        param.put(Const.Params.MODULE, "competition");


        Call<ResponseBody> call = apiInterface.sendRequestToTeamMembersForTeamCreation(param);

        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                Const.ServiceCode.SEND_INVITE);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;

            case R.id.tv_save:

                if (et_group_name.isEnabled()) {

                    if (team_id.equals(""))
                        createTeam();
                    else
                        editTeamName();

                } else {


                    tv_save.setText(getString(R.string.save));
                    et_group_name.setEnabled(true);

                }

                break;

            case R.id.tv_view_file:

               // ViewUtils.openWebActivity(CompetitionDetailsActivity.this, file_path);

                WebViewFragment newFragment_web = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Const.IntentParams.URL, file_path);
                newFragment_web.setArguments(bundle);
                newFragment_web.show(getFragmentManager(), "dialog");



                break;

            case R.id.tv_upload_file:

                AllFilesFragment newFragment = new AllFilesFragment();
                newFragment.show(getFragmentManager(), "dialog");

                break;


        }

    }


    private void getTeamDetails(String id) {


        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.TEAM_LEAD_ID, id);
        param.put(Const.Params.DEPT_ID, loginDetails.getUser().getDepartmentId());
        param.put(Const.Params.TEAM_SPECILIZATION_ID, loginDetails.getUser().getSpecializationId());
        param.put(Const.Params.SEM_ID, loginDetails.getUser().getSemester());
        param.put(Const.Params.YEAR, loginDetails.getUser().getYear());
        param.put(Const.Params.SCENARIO_STATUS, "2");
        param.put(Const.Params.SCENARIO_ID, competitionData.getId());


        Call<ResponseBody> call = apiInterface.getListOfTeamByOptions(param);


        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                Const.ServiceCode.GET_TEAM_DETAILS);
    }


    private void editTeamName() {


        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.TEAM_NAME, et_group_name.getText().toString());
        param.put(Const.Params.TEAM_ID, team_id);

        Call<ResponseBody> call = apiInterface.editTeamByStep(param);
        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                Const.ServiceCode.UPDATE_TEAM);
    }


    private void getAllTeammate() {


        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.TEAM_LEAD_ID, teamDetails.getTeamLeadId().get(0).getId());
        param.put(Const.Params.SCENARIO_ID, competitionData.getId());

        Call<ResponseBody> call = apiInterface.getListOfRequestForTeamByOptions(param);

        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                Const.ServiceCode.GET_ALL_TEAMMATE);


    }


    private void getAllStudents() {


        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.UNIVERSITY_ID, loginDetails.getUser().getUniversityId());
        param.put(Const.Params.SPECIALIZATIONID, loginDetails.getUser().getSpecializationId());
        param.put(Const.Params.SEMESTER, loginDetails.getUser().getSemester());
        param.put(Const.Params.YEAR, loginDetails.getUser().getYear());
        param.put(Const.Params.SCENARIO_STATUS, "2");
        param.put(Const.Params.MODULE, "competition");


        Call<ResponseBody> call = apiInterface.getListOfStudentForInvite(param);

        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                Const.ServiceCode.GET_ALL_STUDENTS);


    }


    public void acceptRequest(int position) {

        AndyUtils.showLoading(this, false, "");
        teamMember.get(position).setStatus("1");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.ID, teamMember.get(position).getId());
        Call<ResponseBody> call = apiInterface.approveRequestByTeamMember(param);

        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                Const.ServiceCode.APPROVE_REQUEST);
    }

    public void rejectRequest(int position) {

        AndyUtils.showLoading(this, false, "");
        teamMember.get(position).setStatus("2");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.ID, teamMember.get(position).getId());
        Call<ResponseBody> call = apiInterface.rejectRequestByTeamMember(param);

        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                Const.ServiceCode.REJECT_REQUEST);
    }


    public void getMyInvites() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.TEAM_MEMBER_USER_ID, loginDetails.getUser().getId());
        param.put(Const.Params.SCENARIO_ID, competitionData.getId());

        Call<ResponseBody> call = apiInterface.getListOfRequestForTeamByOption(param);

        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                Const.ServiceCode.GET_MY_INVITES);
    }


    private void createMilestone(String id) {


        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.TEAM_ID, teamDetails.getId());
        param.put(Const.Params.FILE_ID, id);
        param.put(Const.Params.MILESTONE_NAME, "1");

        Call<ResponseBody> call = apiInterface.createTeamMilestone(param);

        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                CREATE_MILESTONE);


    }


    private void createTeam() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.TEAM_NAME, et_group_name.getText().toString());
        param.put(Const.Params.TEAM_LEAD_ID, loginDetails.getUser().getId());
        param.put(Const.Params.DEPT_ID, loginDetails.getUser().getDepartmentId());
        param.put(Const.Params.TEAM_SPECILIZATION_ID, loginDetails.getUser().getSpecializationId());
        param.put(Const.Params.SEM_ID, loginDetails.getUser().getSemester());
        param.put(Const.Params.YEAR, loginDetails.getUser().getYear());
        param.put(Const.Params.STAGE, "inviteTeamMember");
        param.put(Const.Params.SCENARIO_ID, competitionData.getId());
        param.put(Const.Params.SCENARIO_STATUS, "2");
        param.put(Const.Params.MODULE, "competition");

        Call<ResponseBody> call = apiInterface.createTeamByStep(param);


        new RetroFitRequest().networkRequest(CompetitionDetailsActivity.this, call, this,
                Const.ServiceCode.CREATE_TEAM);


    }


    private void getFileDetails() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(ENTITY_TYPE_ID, "" + competitionData.getId());
        param.put(Const.Params.ENTITY_TYPE_NAME, "competition");

        Call<ResponseBody> call = apiInterface.getAllFiles(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_ALL_FILES);

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {

            case Const.ServiceCode.CREATE_TEAM:

                AndyUtils.cancelLoading();

                if (response != null && !response.equals("") && !response.equals("null")) {

                    // show the invite students screen

                    try {
                        team_id = new JSONObject(response).getString(Const.Json_Parms.LAST_INSERT_ID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    et_group_name.setEnabled(false);
                    tv_save.setText(getString(R.string.edit_post));

                    teamMember.clear();
                    if (teamMemberAdapter != null) {
                        teamMemberAdapter.notifyDataSetChanged();
                        teamMemberAdapter = null;
                    }

                    getTeamDetails(loginDetails.getUser().getId());

                }

                break;


            case Const.ServiceCode.UPDATE_TEAM:

                AndyUtils.cancelLoading();

                et_group_name.setEnabled(false);
                tv_save.setText(getString(R.string.edit_post));


                break;


            case Const.ServiceCode.GET_TEAM_DETAILS:


                if (response != null && !response.equals("") && !response.equals("null")) {

                    ArrayList<TeamDetails> details = new Gson().fromJson(response, new TypeToken<List<TeamDetails>>() {
                    }.getType());

                    updateData(details);
                } else {

                    getMyInvites();

                }


                break;


            case Const.ServiceCode.GET_ALL_TEAMMATE:

                AndyUtils.cancelLoading();

                if (response != null && !response.equals("") && !response.equals("null")) {


                    teamMember.clear();
                    if (teamMemberAdapter != null) {
                        teamMemberAdapter.notifyDataSetChanged();
                        teamMemberAdapter = null;
                    }

                    teamMember = new Gson().fromJson(response, new TypeToken<List<TeamMember>>() {
                    }.getType());

                    loadTeamMember();


                }

                break;


            case Const.ServiceCode.GET_ALL_STUDENTS:


                if (response != null && !response.equals("") && !response.equals("null")) {

                    ArrayList<TeammatesData> details = new Gson().fromJson(response, new TypeToken<List<TeammatesData>>() {
                    }.getType());

                    teammatesData.addAll(details);
                    autoCompleteTextView_team_member.setVisibility(View.VISIBLE);
                    autoCompleteTextView_team_member.setAdapter(
                            new TeammateAutoCompleteAdapter(CompetitionDetailsActivity.this,
                                    R.layout.item_tag_autocomplete, details));

                }


                break;

            case Const.ServiceCode.SEND_INVITE:


                AndyUtils.cancelLoading();

                break;


            case Const.ServiceCode.APPROVE_REQUEST:


                if (response != null && !response.equals("") && !response.equals("null")) {

                    showViewAsAMember();


                }

                break;

            case Const.ServiceCode.REJECT_REQUEST:

                AndyUtils.cancelLoading();

                break;

            case Const.ServiceCode.UPLOAD_FILES:


                Gson gson_1 = new Gson();
                ArrayList<FileDetails> fileData = gson_1.fromJson(response,
                        new TypeToken<List<FileDetails>>() {
                        }.getType());

                if (fileData != null && fileData.size() > 0) {

                    file_path = fileData.get(0).getFullPathToFile();
                    createMilestone(fileData.get(0).getId());
                    enableUploadBtn();
                }

                break;


            case Const.ServiceCode.CREATE_MILESTONE:

                AndyUtils.cancelLoading();

                Toast.makeText(CompetitionDetailsActivity.this,
                        getString(R.string.upload_file_success), Toast.LENGTH_LONG).show();


                break;


            case Const.ServiceCode.GET_MY_INVITES:


                if (response != null && !response.equals("") && !response.equals("null")) {

                    teamMember = new Gson().fromJson(response, new TypeToken<List<TeamMember>>() {
                    }.getType());


                    showViewAsAMember();


                } else {

                    AndyUtils.cancelLoading();
                }

                break;


            case Const.ServiceCode.GET_ALL_FILES:


                AndyUtils.cancelLoading();

                Gson gson = new Gson();
                List<AssignmentDetails> details = gson.fromJson(response, new TypeToken<List<AssignmentDetails>>() {
                }.getType());

                if (details != null && details.size() > 0) {

                    file_path = details.get(0).getFullPathToFile();
                }

                enableUploadBtn();


                break;

        }

    }


    private void showViewAsAMember() {

        TeamMember member = null;


        for (int i = 0; i < teamMember.size(); i++) {

            if (teamMember.get(i).getStatus().equals("1")) {

                member = teamMember.get(i);
                break;
            }


        }


        if (member != null) {
            tv_save.setVisibility(View.GONE);
            tv_upload_file.setVisibility(View.GONE);
            getTeamDetails(member.getTeamLeadId().get(0).getId());
        } else {

            AndyUtils.cancelLoading();
            loadTeamMember();
        }

    }

    private void loadTeamMember() {

        if (teamMember != null && teamMember.size() > 0) {

            // enableUploadBtn();

            getFileDetails();

            if (teamMemberAdapter == null) {

                teamMemberAdapter = new TeamMemberAdapter(CompetitionDetailsActivity.this, teamMember,
                        CompetitionDetailsActivity.this);
                recycler_view_team_members.setAdapter(teamMemberAdapter);

            } else {
                teamMemberAdapter.notifyDataSetChanged();
            }
        }
    }


    ImageLoader imageLoader = AppController.getImageLoader();

    private void updateData(ArrayList<TeamDetails> details) {

        if (details != null && details.size() > 0) {

            teamDetails = details.get(0);
            et_group_name.setText(teamDetails.getTeamName());
            team_id = teamDetails.getId();

            et_group_name.setEnabled(false);
            tv_save.setText(getString(R.string.edit_post));


            lay_team_leader.setVisibility(View.VISIBLE);
            tv_leader_name.setText(teamDetails.getTeamLeadId().get(0).getFirstname() + " " +
                    teamDetails.getTeamLeadId().get(0).getLastname());
            tv_created_date.setText(Utils.ViewUtils.getSpanableText(CompetitionDetailsActivity.this,
                    getString(R.string.team_created) + " " + TimeUtils.getFormattedTime(teamDetails.getCreatedAt(),
                            TimeUtils.BLOG_TIME_FORMAT),
                    0, 14, new ForegroundColorSpan(Color.BLACK)));


            if (!teamDetails.getTeamLeadId().get(0).getProfileUrl().equals(""))
                imageLoader.displayImage(teamDetails.getTeamLeadId().get(0).getProfileUrl(), iv_leader);

            lay_team_member.setVisibility(View.VISIBLE);

            if (teamDetails.getTeamLeadId().get(0).getId().equals(loginDetails.getUser().getId()))
                getAllStudents();

            getAllTeammate();

        }

    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }

    @Override
    public void onItemClick(int position) {

    }


    public void enableUploadBtn() {

        for (int i = 0; i < teamMember.size(); i++) {

            if (teamMember.get(i).getStatus().equals("1")) {

                if (file_path.equals("")) {
                    tv_upload_file.setEnabled(true);
                    tv_view_file.setVisibility(View.GONE);
                } else {
                    tv_upload_file.setEnabled(false);
                    tv_view_file.setVisibility(View.VISIBLE);

                }

                break;
            }


        }

    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String path = intent.getBundleExtra(Const.IntentParams.BROADCAST_BUNDLE).
                    getString(Const.IntentParams.PATH);

            final File file = new File(path);


            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CompetitionDetailsActivity.this, SweetAlertDialog.NORMAL_TYPE)
                    .setContentText(CompetitionDetailsActivity.this.getString(R.string.upload_warning, file.getName()))
                    .setTitleText(CompetitionDetailsActivity.this.getString(R.string.sure))
                    .setConfirmText(CompetitionDetailsActivity.this.getString(R.string.text_ok))
                    .setCancelText(CompetitionDetailsActivity.this.getString(R.string.text_cancel))
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.dismiss();
                            // call upload Api

                            uploadImage(file);

                        }
                    });

            try {

                sweetAlertDialog.show();

            } catch (Exception e) {

                e.printStackTrace();

            }

            //   Toast.makeText(SubmitAssignment.this, path, Toast.LENGTH_LONG).show();
        }
    };

    private void uploadImage(File file) {


        AndyUtils.showLoading(this, false, "");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(Const.Params.USER_FILE, file.getName(), requestFile);


        String type = "Competition";

        Call<ResponseBody> call = apiInterface.uploadFile(Integer.parseInt(loginDetails.getUser().getId()),
                Integer.parseInt(teamDetails.getId()), type.trim().toString(), body);
        new RetroFitRequest().networkRequest(this, call, CompetitionDetailsActivity.this, Const.ServiceCode.UPLOAD_FILES);

    }

}

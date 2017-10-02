package com.cli.knowledgebase;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cli.knowledgebase.AppService.SendDataService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AppFragment.AllFilesFragment;
import Constants.Const;
import DataBase.DataBaseHelper;
import Interfaces.AsyncTaskCompleteListener;
import Model.AssignmentData;
import Model.AssignmentDetails;
import Model.FileDetails;
import Model.Login;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.AppLog;
import Utils.FileUtils;
import Utils.PermissionUtils;
import Utils.PreferenceHelper;
import Utils.ViewUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static Constants.Const.Params.ENTITY_TYPE_ID;
import static com.cli.knowledgebase.R.string.score;

public class SubmitAssignment extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {


    PreferenceHelper preferenceHelper;
    ApiInterface apiInterface;
    Login loginDetails;
    TextView tv_menu_name;
    ImageView iv_back;
    AssignmentData assignmentData;
    ArrayList<AssignmentDetails> assignmentDetails = new ArrayList<>();
    WebView browser;

    EditText et_assignment;
    TextView tv_submit_assignment;
    TextView tv_upload_assignment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);
        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);

        assignmentData = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).getParcelable(Const.IntentParams.ASSIGNMENT_DATA);
        widgets();

        getAssignmentDetails();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Const.IntentFilter.SELECT_PATH));


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

    }

    private void widgets() {


        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(assignmentData.getAssignmentName());

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        browser = (WebView) findViewById(R.id.webview_assignment);

        et_assignment = (EditText) findViewById(R.id.et_assignment);

        tv_submit_assignment = (TextView) findViewById(R.id.tv_submit_assignment);
        tv_submit_assignment.setOnClickListener(this);

        tv_upload_assignment = (TextView) findViewById(R.id.tv_upload_assignment);
        tv_upload_assignment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;

            case R.id.tv_submit_assignment:

                if (et_assignment.getText().toString().equals("")) {

                    Toast.makeText(this, getString(R.string.error_empty_assignment), Toast.LENGTH_LONG).show();

                    return;
                }


                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SubmitAssignment.this, SweetAlertDialog.NORMAL_TYPE)

                        .setTitleText(SubmitAssignment.this.getString(R.string.sure))
                        .setConfirmText(SubmitAssignment.this.getString(R.string.text_ok))
                        .setCancelText(SubmitAssignment.this.getString(R.string.text_cancel))
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
                                submitAssignment("");

                            }
                        });

                sweetAlertDialog.show();


                break;

            case R.id.tv_upload_assignment:

//                if (PermissionUtils.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                    ViewUtils.openFiles(this);
//                } else {
//
//                    PermissionUtils.getStoragePermission(this);
//                }

                // getFilesList();

                AllFilesFragment newFragment = new AllFilesFragment();
                newFragment.show(getFragmentManager(), "dialog");

                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {


            case Const.RequestCode.GET_STORAGE_PERMISSION:

                if (PermissionUtils.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                    ViewUtils.openGallery(this);

                break;

        }
    }

    private void loadAssignment(String url) {

        WebSettings webSettings = browser.getSettings();
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        browser.setScrollbarFadingEnabled(true);

        browser.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // callback.invoke(String origin, boolean allow, boolean remember);
                callback.invoke(origin, true, false);
            }
        });

        if (url.toLowerCase().endsWith("png") || url.toLowerCase().endsWith("jpeg") || url.toLowerCase().endsWith("jpg")) {
            browser.loadUrl(url);
        } else {

            browser.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        }
    }


    private void getAssignmentDetails() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(ENTITY_TYPE_ID, "" + assignmentData.getId());
        param.put(Const.Params.ENTITY_TYPE_NAME, "Assignment");

        Call<ResponseBody> call = apiInterface.getAllFiles(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_ALL_FILES);

    }


    private void submitAssignment(String file_id) {

        // AndyUtils.showLoading(this, false, "");


        assignmentData.setAssignmentFile(file_id);
        assignmentData.setAssignmentText(et_assignment.getText().toString());
        new DataBaseHelper(this).insertAssignmentDetails(assignmentData, Const.STATUS.SUBMITTED);

        SendDataService.sendAssignmentData(this);
        AndyUtils.cancelLoading();
        finish();

       /* Map<String, String> param = new HashMap<>();
        param.put(Const.Params.QUESTION_ID, "" + assignmentData.getId());
        param.put(Const.Params.STUDENT_ID, loginDetails.getUser().getId());
        param.put(Const.Params.FILE_ID, file_id);
        param.put(Const.Params.ANSWER, et_assignment.getText().toString());

        Call<ResponseBody> call = apiInterface.createAssignmentAnswer(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.SUBMIT_ASSIGNMENT_ANSWER);*/


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {

            case Const.ServiceCode.GET_ALL_FILES:


                AndyUtils.cancelLoading();

                Gson gson = new Gson();
                assignmentDetails = gson.fromJson(response, new TypeToken<List<AssignmentDetails>>() {
                }.getType());


                loadData();

                break;

            case Const.ServiceCode.UPLOAD_FILES:


                Gson gson_1 = new Gson();
                ArrayList<FileDetails> assignmentFileData = gson_1.fromJson(response,
                        new TypeToken<List<FileDetails>>() {
                        }.getType());

                Toast.makeText(SubmitAssignment.this,
                        getString(R.string.upload_file_success), Toast.LENGTH_LONG).show();


                if (assignmentFileData != null && assignmentFileData.size() > 0)
                    submitAssignment(assignmentFileData.get(0).getId());

                break;

            case Const.ServiceCode.SUBMIT_ASSIGNMENT_ANSWER:

                AndyUtils.cancelLoading();

                FileUtils.writeConfiguration(SubmitAssignment.this, Const.FileNames.ASSIGNMENT +
                        loginDetails.getUser().getId() + assignmentData.getId() +
                        Const.FileNames.FILE_EXTENSION, "" + score);

                finish();


                break;

        }

    }

    private void loadData() {

        if (assignmentDetails != null && assignmentDetails.size() > 0) {
            loadAssignment(assignmentDetails.get(0).getFullPathToFile());
        } else {

            Toast.makeText(SubmitAssignment.this, getString(R.string.file_not_uploaded_error), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }

    private void getFilesList() {

        File mfile = new File("/sdcard/Download");
        File[] list = mfile.listFiles();

        System.out.println("list" + mfile.listFiles().length);
        for (int i = 0; i < mfile.listFiles().length; i++) {
            // if (list[i].isHidden())
            AppLog.Log("FilePath", "hidden path files.." + list[i].getAbsolutePath());
            AppLog.Log("FilePath", "hidden path files.." + list[i].getName());
            AppLog.Log("FilePath", "hidden path files.." + list[i].isDirectory());
        }
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String path = intent.getBundleExtra(Const.IntentParams.BROADCAST_BUNDLE).
                    getString(Const.IntentParams.PATH);

            final File file = new File(path);


            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SubmitAssignment.this, SweetAlertDialog.NORMAL_TYPE)
                    .setContentText(SubmitAssignment.this.getString(R.string.upload_warning, file.getName()))
                    .setTitleText(SubmitAssignment.this.getString(R.string.sure))
                    .setConfirmText(SubmitAssignment.this.getString(R.string.text_ok))
                    .setCancelText(SubmitAssignment.this.getString(R.string.text_cancel))
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

            sweetAlertDialog.show();

            //   Toast.makeText(SubmitAssignment.this, path, Toast.LENGTH_LONG).show();
        }
    };

    private void uploadImage(File file) {


        AndyUtils.showLoading(this, false, "");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(Const.Params.USER_FILE, file.getName(), requestFile);


        Call<ResponseBody> call = apiInterface.uploadFile(Integer.parseInt(loginDetails.getUser().getId()),
                Integer.parseInt(assignmentData.getId()), "Assignment", body);
        new RetroFitRequest().networkRequest(this, call, SubmitAssignment.this, Const.ServiceCode.UPLOAD_FILES);

    }
}

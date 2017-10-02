package com.cli.knowledgebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cli.knowledgebase.AppService.DownloadService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Model.FileDetails;
import Model.Login;
import Model.Specialization;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SessionPlan extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {

    TextView tv_menu_name;
    ImageView iv_back;
    Specialization specialization;
    ApiInterface apiInterface;
    PreferenceHelper preferenceHelper;
    Login loginDetails;

    WebView webview_session_plan;
    ImageView iv_nodata;
    TextView tv_download;
    FileDetails fileDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_plan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);
        specialization = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).
                getParcelable(Const.IntentParams.SPECIALIZATION_DATA);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);


        widgets();

        getSessionPlan();


    }

    private void widgets() {


        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(specialization.getName() + " " + getString(R.string.session_plan));

        webview_session_plan = (WebView) findViewById(R.id.webview_session_plan);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        iv_nodata = (ImageView) findViewById(R.id.iv_nodata);

        tv_download = (TextView) findViewById(R.id.tv_download);
        tv_download.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;

            case R.id.tv_download:

                //onBackPressed();
                if (fileDetails != null) {
                    Intent intent = new Intent(this, DownloadService.class);
                    intent.putExtra(Const.IntentParams.FILE_DETAILS_DATA, fileDetails);
                    startService(intent);
                }

                break;

        }

    }


    private void loadSessionPlan(String url) {


        webview_session_plan.setVisibility(View.VISIBLE);
        iv_nodata.setVisibility(View.GONE);

        WebSettings webSettings = webview_session_plan.getSettings();
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        webview_session_plan.setScrollbarFadingEnabled(true);

        webview_session_plan.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // callback.invoke(String origin, boolean allow, boolean remember);
                callback.invoke(origin, true, false);
            }
        });

        if (url.toLowerCase().endsWith("png") || url.toLowerCase().endsWith("jpeg") || url.toLowerCase().endsWith("jpg")) {
            webview_session_plan.loadUrl(url);
        } else {

            webview_session_plan.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        }
    }


    private void getSessionPlan() {


        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.COURSE_ID, "" + specialization.getId());
        param.put(Const.Params.TYPE, "sessionPlan");

        Call<ResponseBody> call = apiInterface.getAnnouncements(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_SESSION_PLAN);

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {

            case Const.ServiceCode.GET_SESSION_PLAN:

                AndyUtils.cancelLoading();

                if (response != null) {

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        JSONObject jsonObject = jsonArray.getJSONObject(0).getJSONArray(Const.Json_Parms.FILE_ID).
                                getJSONObject(0);

                        //  String url =.getString(Const.Json_Parms.FULL_PATH_TO_FILE);

                        fileDetails = new Gson().fromJson(jsonObject.toString(), FileDetails.class);


                        if (fileDetails != null)
                            loadSessionPlan(fileDetails.getFullPathToFile());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                break;

        }

    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }
}

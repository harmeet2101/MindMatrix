package com.cli.knowledgebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.TopicExpandableListAdapter;
import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Model.Login;
import Model.ScormData;
import Model.Specialization;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ScormActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {


    PreferenceHelper preferenceHelper;
    ApiInterface apiInterface;
    Login loginDetails;
    TextView tv_menu_name;
    ImageView iv_back;
    Specialization specialization;
    ArrayList<ScormData> scormData;
    ArrayList<String> chapter_title = new ArrayList<>();
    //MaterialSpinner spinner_chapters;
    //RecyclerView recycler_view_topic_list;
    // TopicsListAdapter adapter;
    WebView webview_scorm;
    RelativeLayout rel_progress_bar;

    ExpandableListView expandable_list_topics;
    TopicExpandableListAdapter expandableListAdapter;
    HashMap<String, List<ScormData>> listDataChild = new HashMap<>();
    LinearLayout lay_scorm_data;
    ImageView iv_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);
        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);
        specialization = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).
                getParcelable(Const.IntentParams.SPECIALIZATION_DATA);

        widgets();

        getScorm();

    }

    private void widgets() {


        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(specialization.getName());

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        rel_progress_bar = (RelativeLayout) findViewById(R.id.rel_progress_bar);

        lay_scorm_data = (LinearLayout) findViewById(R.id.lay_scorm_data);
        iv_nodata = (ImageView) findViewById(R.id.iv_nodata);

       /* spinner_chapters = (MaterialSpinner) findViewById(R.id.spinner_chapters);
        spinner_chapters.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                loadTopics(chapter_title.get(position));

            }
        });

        recycler_view_topic_list = (RecyclerView) findViewById(recycler_view_topic_list);
        recycler_view_topic_list.setLayoutManager(new LinearLayoutManager(this));*/


        expandable_list_topics = (ExpandableListView) findViewById(R.id.expandable_list_topics);
        expandable_list_topics.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


                loadTopic(Const.ServiceType.TOPICS_URL + specialization.getId() + "/" + listDataChild.get(
                        chapter_title.get(groupPosition)).get(
                        childPosition).getLessionHref());


                return false;
            }
        });

        webview_scorm = (WebView) findViewById(R.id.webview_scorm);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;
        }
    }


    private void getScorm() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.COURSE_ID, "" + specialization.getId());

        Call<ResponseBody> call = apiInterface.getScorm(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_SCORM);


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {

            case Const.ServiceCode.GET_SCORM:

                AndyUtils.cancelLoading();

                Gson gson = new Gson();
                scormData = gson.fromJson(response, new TypeToken<List<ScormData>>() {
                }.getType());

                loadData();

                break;

        }

    }

    private void loadData() {

        chapter_title.clear();

        if (scormData != null && scormData.size() > 0) {

            lay_scorm_data.setVisibility(View.VISIBLE);
            iv_nodata.setVisibility(View.GONE);

            for (int i = 0; i < scormData.size(); i++) {

                if (!chapter_title.contains(scormData.get(i).getChapterTitle()))
                    chapter_title.add(scormData.get(i).getChapterTitle());

            }
        } else {

            lay_scorm_data.setVisibility(View.GONE);
            iv_nodata.setVisibility(View.VISIBLE);

        }


        prepareChildData();

        if (expandableListAdapter == null) {

            expandableListAdapter = new TopicExpandableListAdapter(ScormActivity.this, chapter_title, listDataChild);
            expandable_list_topics.setAdapter(expandableListAdapter);

        }


    }

    private void prepareChildData() {


        for (int i = 0; i < chapter_title.size(); i++) {

            ArrayList<ScormData> datas = new ArrayList<>();

            for (int j = 0; j < scormData.size(); j++) {


                if (scormData.get(j).getChapterTitle().equals(chapter_title.get(i)))
                    datas.add(scormData.get(j));

            }

            listDataChild.put(chapter_title.get(i), datas);


        }


    }

    /* private void loadTopics(String chapter_name) {

       *//*  if (adapter == null) {

            adapter = new TopicsListAdapter(ScormActivity.this, scormData, this);
            adapter.filterList(chapter_title.get(0));
            recycler_view_topic_list.setAdapter(adapter);


        } else {

            adapter.filterList(chapter_name);
            adapter.notifyDataSetChanged();
        }
*//*

        loadTopic(Const.ServiceType.TOPICS_URL + specialization.getId() + "/" + adapter.getList().get(0).getLessionHref());

    }
*/
    @Override
    public void onTaskFailure(int serviceCode) {

    }


    private void loadTopic(String url) {


        WebSettings webSettings = webview_scorm.getSettings();
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        webview_scorm.setScrollbarFadingEnabled(true);
        rel_progress_bar.setVisibility(View.VISIBLE);
        webview_scorm.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String request) {


                return true;
            }

            public void onPageFinished(WebView view, String url) {


                rel_progress_bar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {

                rel_progress_bar.setVisibility(View.GONE);
            }
        });

    /*    webview_scorm.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // callback.invoke(String origin, boolean allow, boolean remember);
                callback.invoke(origin, true, false);
            }
        });*/

        //  if (url.toLowerCase().endsWith("png") || url.toLowerCase().endsWith("jpeg") || url.toLowerCase().endsWith("jpg")) {
        webview_scorm.loadUrl(url);
      /*  } else {

            browser.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        }*/
    }
}

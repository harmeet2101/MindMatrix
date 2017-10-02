package com.cli.knowledgebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Model.BlogData;
import Model.Login;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import Utils.ViewUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class CreateBlogActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {

    PreferenceHelper preferenceHelper;
    ApiInterface apiInterface;
    Login loginDetails;
    TextView tv_menu_name;
    ImageView iv_back;

    TextView tv_draft_blog, tv_publish_blog;
    EditText et_description, et_title;

    BlogData blogData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);
        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);

        try {
            blogData = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).
                    getParcelable(Const.IntentParams.BLOG_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }


        widgets();

    }


    private void widgets() {


        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(getString(R.string.create_blog));

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        tv_publish_blog = (TextView) findViewById(R.id.tv_publish_blog);
        tv_publish_blog.setOnClickListener(this);

        tv_draft_blog = (TextView) findViewById(R.id.tv_draft_blog);
        tv_draft_blog.setOnClickListener(this);

        et_description = (EditText) findViewById(R.id.et_description);
        et_description.setTextIsSelectable(true);

        et_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                enablePublishButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_title = (EditText) findViewById(R.id.et_title);
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                enablePublishButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loadData();


    }

    private void loadData() {

        if (blogData != null) {

            et_description.setText(blogData.getAnnouncementMsg());
            et_title.setText(blogData.getTitle());

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;

            case R.id.tv_publish_blog:

                createBlog(1, Const.ServiceCode.CREATE_BLOG);

                break;

            case R.id.tv_draft_blog:

                createBlog(0, Const.ServiceCode.UPDATE_BLOG);

                break;
        }

    }


    private void enablePublishButton() {

        if (!et_title.getText().toString().equals("") && !et_description.getText().toString().equals("")) {

            tv_publish_blog.setEnabled(true);
            tv_draft_blog.setEnabled(true);

        } else {

            tv_publish_blog.setEnabled(false);
            tv_draft_blog.setEnabled(false);

        }

    }


    private void createBlog(int status, int serviceCode) {

        Call<ResponseBody> call;

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.TITLE, et_title.getText().toString());
        param.put(Const.Params.COURSE_ID, "0");
        param.put(Const.Params.USER_ID, loginDetails.getUser().getId());
        param.put(Const.Params.ANNOUNCEMENT_MSG, et_description.getText().toString());
        param.put(Const.Params.STATUS, "" + status);
        param.put(Const.Params.TYPE, "blog");
        param.put(Const.Params.MOBILE, "true");
        param.put(Const.Params.UNIVERSITYID, loginDetails.getUser().getUniversityId());

        if (blogData == null) {
            call = apiInterface.createDiscussion(param);
        } else {
            param.put(Const.Params.ID, blogData.getId());
            call = apiInterface.updateDiscussion(param);
        }


        new RetroFitRequest().networkRequest(CreateBlogActivity.this, call, this,
                serviceCode);


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {

            case Const.ServiceCode.CREATE_BLOG:

                if (response == null && response.equals("") && response.equals("null"))
                    return;


                AndyUtils.cancelLoading();

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(this.getString(R.string.success_publish))
                        .setConfirmText(this.getString(R.string.text_ok))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        });
                sweetAlertDialog.show();

                ViewUtils.sendLocalBroadCast(this, Const.IntentFilter.UPDATE_BLOG, null);

                break;


            case Const.ServiceCode.UPDATE_BLOG:

                if (response == null && response.equals("") && response.equals("null"))
                    return;


                AndyUtils.cancelLoading();

                SweetAlertDialog sweetAlertDialog_update = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(this.getString(R.string.blog_draft))
                        .setConfirmText(this.getString(R.string.text_ok))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        });
                sweetAlertDialog_update.show();
                ViewUtils.sendLocalBroadCast(this, Const.IntentFilter.UPDATE_BLOG, null);


                break;

        }

    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }
}

package com.cli.knowledgebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.TagAutoCompleteAdapter;
import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Model.DiscussionTags;
import Model.Login;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class CreateDiscussionActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {


    PreferenceHelper preferenceHelper;
    ApiInterface apiInterface;
    Login loginDetails;
    TextView tv_menu_name;
    ImageView iv_back;

    TextView tv_publish_discussion;
    EditText et_description, et_title;
    AutoCompleteTextView autoCompleteTextView_tags;

    ArrayList<DiscussionTags> discussionTag = new ArrayList<>();
    TagAutoCompleteAdapter autoCompleteAdapter;

    ArrayList<DiscussionTags> selectedTagList = new ArrayList<>();

    ChipCloud chip_cloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discussion);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);
        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);

        discussionTag = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).getParcelableArrayList(Const.IntentParams.DISCUSSION_TAGS);

        widgets();

    }

    private void widgets() {

        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(getString(R.string.create_discussion));

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        tv_publish_discussion = (TextView) findViewById(R.id.tv_publish_discussion);
        tv_publish_discussion.setOnClickListener(this);

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

        chip_cloud = (ChipCloud) findViewById(R.id.chip_cloud);
        chip_cloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int i) {

                selectedTagList.remove(i);
                clearChipCloud();
                addTags();
                enablePublishButton();

            }

            @Override
            public void chipDeselected(int i) {

            }
        });

        autoCompleteTextView_tags = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView_tags);

        ArrayList<DiscussionTags> autoCompleteTags = new ArrayList<>();
        autoCompleteTags.addAll(discussionTag);

        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new TagAutoCompleteAdapter(CreateDiscussionActivity.this,
                    R.layout.item_tag_autocomplete, autoCompleteTags);
            autoCompleteTextView_tags.setAdapter(autoCompleteAdapter);
        }

        autoCompleteTextView_tags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (!selectedTagList.contains(autoCompleteAdapter.getItem(position))) {

                    String selectedTag = autoCompleteAdapter.getItem(position).getTitle();
                    chip_cloud.addChip(selectedTag);
                    autoCompleteTextView_tags.setText("");


                    selectedTagList.add(autoCompleteAdapter.getItem(position));
                    enablePublishButton();
                } else {

                    Toast.makeText(CreateDiscussionActivity.this,
                            getString(R.string.error_tag_exist), Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    private void addTags() {


        if (selectedTagList != null && chip_cloud != null) {

            for (int i = 0; i < selectedTagList.size(); i++)
                chip_cloud.addChip(selectedTagList.get(i).getTitle());


        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;

            case R.id.tv_publish_discussion:

                createDiscussion();

                break;

        }

    }


    private void clearChipCloud() {

        chip_cloud.removeAllViews();

    }


    private void enablePublishButton() {

        if (!et_title.getText().toString().equals("") &&
                selectedTagList.size() > 0 && !et_description.getText().toString().equals("")) {

            tv_publish_discussion.setEnabled(true);

        } else {

            tv_publish_discussion.setEnabled(false);


        }

    }


    private void createDiscussion() {


        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.TITLE, et_title.getText().toString());
        param.put(Const.Params.COURSE_ID, "0");
        param.put(Const.Params.USER_ID, loginDetails.getUser().getId());
        param.put(Const.Params.ANNOUNCEMENT_MSG, et_description.getText().toString());
        param.put(Const.Params.STATUS, "1");
        param.put(Const.Params.TYPE, "discuss");
        param.put(Const.Params.TAG, getSelectedTag());
        param.put(Const.Params.MOBILE, "true");
        param.put(Const.Params.UNIVERSITYID, loginDetails.getUser().getUniversityId());

        Call<ResponseBody> call = apiInterface.createDiscussion(param);
        new RetroFitRequest().networkRequest(CreateDiscussionActivity.this, call, this,
                Const.ServiceCode.CREATE_DISCUSSION);


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {

            case Const.ServiceCode.CREATE_DISCUSSION:


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

                break;
        }

    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }

    public String getSelectedTag() {

        String tag = "";

        for (int i = 0; i < selectedTagList.size(); i++) {

            if (!tag.equals("")) {

                tag = tag + "," + selectedTagList.get(i).getId();

            } else {

                tag = selectedTagList.get(i).getId();

            }

        }


        return tag;
    }
}

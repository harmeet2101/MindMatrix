package com.cli.knowledgebase;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Model.FileDetails;
import Model.Login;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.FileUtils;
import Utils.PermissionUtils;
import Utils.PreferenceHelper;
import Utils.ViewUtils;
import app.AppController;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {


    PreferenceHelper preferenceHelper;
    ApiInterface apiInterface;
    Login loginDetails;
    TextView tv_menu_name;
    ImageView iv_back;

    ImageView iv_profile_pic;
    TextView tv_name, tv_email_id, tv_update_profile_pic, tv_change_password;
    EditText et_current_password, et_new_password, et_retype_password;

    ImageLoader imageLoader;
    String file_path;
    String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);


        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Const.IntentFilter.SELECT_PATH));


        imageLoader = AppController.getImageLoader();

        widgets();


    }

    private void widgets() {

        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(getString(R.string.update_profile));

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);

        if (!loginDetails.getUser().getProfileUrl().equals("")) {
            imageLoader.displayImage(loginDetails.getUser().getProfileUrl(), iv_profile_pic);
        }


        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(loginDetails.getUser().getFirstname() + " " + loginDetails.getUser().getLastname());

        tv_email_id = (TextView) findViewById(R.id.tv_email_id);
        tv_email_id.setText(loginDetails.getUser().getEmail());

        tv_update_profile_pic = (TextView) findViewById(R.id.tv_update_profile_pic);
        tv_update_profile_pic.setOnClickListener(this);

        tv_change_password = (TextView) findViewById(R.id.tv_change_password);
        tv_change_password.setOnClickListener(this);

        et_current_password = (EditText) findViewById(R.id.et_current_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_retype_password = (EditText) findViewById(R.id.et_retype_password);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();
                break;

            case R.id.tv_change_password:

                if (vallidatePasswordField()) {

                    changePassword();

                }

                break;

            case R.id.tv_update_profile_pic:
/*
                AllFilesFragment newFragment = new AllFilesFragment();
                newFragment.show(getFragmentManager(), "dialog");*/


                if (PermissionUtils.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ViewUtils.openGallery(this);
                } else {

                    PermissionUtils.getStoragePermission(this);
                }

                //  ViewUtils.showMediaChoiceDialog(this);

                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case Const.RequestCode.GET_CAMERA_PERMISSION:

                ViewUtils.openCamera(this);

                break;

            case Const.RequestCode.GET_STORAGE_PERMISSION:

                ViewUtils.openGallery(this);

                break;


        }


    }

    private void changePassword() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.EMAIL, loginDetails.getUser().getId());
        param.put(Const.Params.UNIVERSITY_ID, loginDetails.getUser().getUniversityId());
        param.put(Const.Params.CURRENT_PASSWORD, et_current_password.getText().toString());
        param.put(Const.Params.NEW_PASSWORD, et_new_password.getText().toString());


        Call<ResponseBody> call = apiInterface.changeUserPassword(param);


        new RetroFitRequest().networkRequest(ProfileActivity.this, call, this,
                Const.ServiceCode.CHANGE_PASSWORD);

    }

    private boolean vallidatePasswordField() {

        if (et_current_password.getText().toString().equals("")) {

            Toast.makeText(this, getString(R.string.error_enter_current_password), Toast.LENGTH_SHORT).show();

            return false;

        }

        if (et_new_password.getText().toString().equals("")) {

            Toast.makeText(this, getString(R.string.error_enter_new_password), Toast.LENGTH_SHORT).show();

            return false;

        }

        if (et_retype_password.getText().toString().equals("")) {

            Toast.makeText(this, getString(R.string.error_enter_retype_password), Toast.LENGTH_SHORT).show();

            return false;

        }


        if (!et_retype_password.getText().toString().equals(et_new_password.getText().toString())) {

            Toast.makeText(this, getString(R.string.error_password_mismatch), Toast.LENGTH_SHORT).show();

            return false;

        }


        return true;
    }

    private void uploadImage(File file) {


        AndyUtils.showLoading(this, false, "");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(Const.Params.USER_FILE, file.getName(), requestFile);


        String type = "ProfilePhoto";

        Call<ResponseBody> call = apiInterface.uploadFile(Integer.parseInt(loginDetails.getUser().getId()),
                Integer.parseInt(loginDetails.getUser().getId()), type.trim().toString(), body);
        new RetroFitRequest().networkRequest(this, call, ProfileActivity.this, Const.ServiceCode.UPLOAD_FILES);

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {


            case Const.ServiceCode.UPLOAD_FILES:

                Gson gson_1 = new Gson();
                ArrayList<FileDetails> fileData = gson_1.fromJson(response,
                        new TypeToken<List<FileDetails>>() {
                        }.getType());

                if (fileData != null && fileData.size() > 0) {

                    file_path = fileData.get(0).getFullPathToFile();
                    editProfile(file_path);

                }

                break;

            case Const.ServiceCode.UPDATE_PROFILE_PIC:

                AndyUtils.cancelLoading();

                if (response != null) {

                    loginDetails.getUser().setProfileUrl(file_path);

                    preferenceHelper.putUserDetails(new Gson().toJson(loginDetails));


                    iv_profile_pic.setImageResource(R.mipmap.ic_placeholderimage);

                    if (!loginDetails.getUser().getProfileUrl().equals("")) {
                        imageLoader.displayImage(loginDetails.getUser().getProfileUrl(), iv_profile_pic);
                    }


                }

                break;

            case Const.ServiceCode.CHANGE_PASSWORD:

                AndyUtils.cancelLoading();

                if (response != null) {

                    try {
                        JSONObject jsonObject= new JSONObject(response);
                        Toast.makeText(this,"Password changed successfully",Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();


                        Toast.makeText(this,response,Toast.LENGTH_LONG).show();
                    }

                }

                break;


        }

    }

    private void editProfile(String file_path) {


        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.ID, loginDetails.getUser().getId());
        param.put(Const.Params.PROFILE_URL, file_path);

        Call<ResponseBody> call = apiInterface.editUser(param);


        new RetroFitRequest().networkRequest(ProfileActivity.this, call, this,
                Const.ServiceCode.UPDATE_PROFILE_PIC);

    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String path = intent.getBundleExtra(Const.IntentParams.BROADCAST_BUNDLE).
                    getString(Const.IntentParams.PATH);

            final File file = new File(path);
            uploadImage(file);


            //   Toast.makeText(SubmitAssignment.this, path, Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == Const.RequestCode.OPEN_CAMERA) {


                ViewUtils.runCropImage(Uri.parse(Const.imagePAth), this, null);
            } else if (requestCode == Const.RequestCode.OPEN_GALLERY) {


                ViewUtils.runCropImage(data.getData(), this, null);
            } else if (requestCode == Const.RequestCode.CROPPED_IMAGE) {
                if (resultCode == RESULT_OK) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri path_uri = result.getUri();

                    // if nothing received
                    if (path_uri == null) {
                        return;
                    }

                    filePath = FileUtils.getImageUrlWithAuthority(this, path_uri);

                    //  filePath = FileUtils.getCropImageImage(path_uri, this);
                    // compress image
                    filePath = FileUtils.compressImage(Uri.parse(filePath), this);

                    final File file = new File(filePath);
                    uploadImage(file);

                    return;
                }
            }
        }


    }
}

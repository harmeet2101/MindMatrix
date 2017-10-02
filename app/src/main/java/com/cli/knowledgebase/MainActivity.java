package com.cli.knowledgebase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cli.knowledgebase.AppService.SendDataService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Model.Login;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import Utils.ViewUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {

    //    ImageView iv_bg;
    EditText et_email, et_password;
    TextView tv_btn_login_or_siginup, tv_forgot_password, checkbox_text;
    CheckBox checkbox_terms;
    PreferenceHelper preferenceHelper;

    LinearLayout layout_email, layout_pass;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        getSupportActionBar().hide();
        preferenceHelper = new PreferenceHelper(this);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);


        if (!preferenceHelper.getApiToken().equals("")) {
            launchHomeScreen();

            SendDataService.sendQuizData(this);
            SendDataService.sendAssignmentData(this);

            return;
        }

        widgets();

/*
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_login);
        Bitmap blurredBitmap = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurredBitmap = ViewUtils.getBlurImage(bitmap, MainActivity.this,5f);
            iv_bg.setImageBitmap(blurredBitmap);
        } else {

            iv_bg.setImageResource(R.mipmap.bg_login);

        }*/


    }


    private void widgets() {

//        iv_bg = (ImageView) findViewById(R.id.iv_bg);

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        tv_btn_login_or_siginup = (TextView) findViewById(R.id.tv_btn_login_or_siginup);
        tv_btn_login_or_siginup.setOnClickListener(this);

        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(this);

        checkbox_terms = (CheckBox) findViewById(R.id.checkbox_terms);
        checkbox_text = (TextView) findViewById(R.id.checkbox_text);
        checkbox_text.setOnClickListener(this);

        layout_email = (LinearLayout) findViewById(R.id.layout_email);
        layout_pass = (LinearLayout) findViewById(R.id.layout_pass);


//        et_email.setOnFocusChangeListener(focusListener);
//        et_password.setOnFocusChangeListener(focusListener);
    }

    //    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
//        public void onFocusChange(View v, boolean hasFocus) {
//            if (v.getId()==R.id.tv_btn_login_or_siginup){
//                layout_email.setBackgroundResource(R.drawable.bg_login_text);
//                layout_pass.setBackground(null);
//            } else {
//                layout_pass.setBackgroundResource(R.drawable.bg_login_text);
//                layout_email.setBackground(null);
//            }
//        }
//    };
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_btn_login_or_siginup:

                if (vallidate()) {
                    login();
                }
                break;

            case R.id.tv_forgot_password:
                forgotPasswordDialog();

                break;

            case R.id.checkbox_text:

                termAndConditionDialog();

                break;
        }

    }

    void termAndConditionDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Terms and Conditions");
        dialogBuilder.setMessage("" + getString(R.string.terms_and_condition_details));
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
//                Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    void forgotPasswordDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.forgot_password_custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Enter your registered emailId.");


        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (!AndyUtils.eMailValidation(edt.getText().toString())) {

                    String error_message = getString(R.string.error_email_id_not_vallid);
                    Toast.makeText(MainActivity.this, error_message, Toast.LENGTH_LONG).show();

                } else {

                    forgetPassword(edt.getText().toString());
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private boolean vallidate() {

        String error_message = "";

        if (et_email.getText().toString().equals("")) {

            error_message = getString(R.string.error_enter_email_id);


        } else if (!AndyUtils.eMailValidation(et_email.getText().toString())) {

            error_message = getString(R.string.error_email_id_not_vallid);

        } else if (et_password.getText().toString().equals("")) {

            error_message = getString(R.string.error_enter_password);

        } else if (!checkbox_terms.isChecked()) {

            error_message = getString(R.string.error_accept_terms);
        }


        if (!error_message.equals("")) {

            Toast.makeText(this, error_message, Toast.LENGTH_LONG).show();

            return false;

        }

        return true;


    }


    private void launchHomeScreen() {

        ViewUtils.launchActivity(this, null, HomeScreenNew.class);
        finish();

    }

    private void login() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.EMAIL, et_email.getText().toString());
        param.put(Const.Params.PASSWORD, et_password.getText().toString());
        param.put("universityId", "");

        Call<ResponseBody> call = apiInterface.login(param);

        new RetroFitRequest().networkRequest(this, call, this, Const.ServiceCode.LOGIN);
    }

    private void forgetPassword(String emailId) {
        AndyUtils.showLoading(this, false, "");
        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.EMAIL, emailId);
        Call<ResponseBody> call = apiInterface.forgotPassword(param);
        new RetroFitRequest().networkRequest(this, call, this, Const.ServiceCode.FORGET_PASSWORD);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {

            case Const.ServiceCode.LOGIN:

                AndyUtils.cancelLoading();

                Gson gson = new Gson();
                Login login = gson.fromJson(response, Login.class);

                if (login.getCode().equals("" + Const.ResponseCode.SUCCESS)) {

                    if (login != null) {

                        preferenceHelper.putApiToken(login.getToken());
                        preferenceHelper.putUserDetails(response);
                        SendDataService.createLoginLog(this);
                        launchHomeScreen();
                        return;
                    }
                }
                Toast.makeText(this, getString(R.string.error_login_auth_failure), Toast.LENGTH_LONG).show();
                break;
            case Const.ServiceCode.FORGET_PASSWORD:
                AndyUtils.cancelLoading();
                Toast.makeText(this, "" + response.toString(), Toast.LENGTH_LONG).show();
                break;

        }

    }

    @Override
    public void onTaskFailure(int serviceCode) {

        AndyUtils.cancelLoading();


    }
}

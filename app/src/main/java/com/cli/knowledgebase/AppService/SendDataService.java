package com.cli.knowledgebase.AppService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Constants.Const;
import DataBase.DataBaseHelper;
import Interfaces.AsyncTaskCompleteListener;
import Model.AssignmentData;
import Model.CommentsData;
import Model.Login;
import Model.QuizData;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.PreferenceHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by ganesha on 27/3/17.
 */

public class SendDataService extends Service implements Handler.Callback, AsyncTaskCompleteListener {


    private static Handler mHandler;
    ApiInterface apiInterface;

    private static final String KEY_METHOD = "key_method";
    private static final String KEY_DATA = "data";


    private static final int METHOD_SEND_QUIZ_DATA = 1;
    private static final int METHOD_SEND_ASSIGNMENT_DATA = 2;
    private static final int METHOD_CREATE_LOGIN_LOG = 3;
    private static final int METHOD_CREATE_COMMENT = 4;
    private static final int METHOD_CREATE_LIKE = 5;

    DataBaseHelper dataBaseHelper;
    Login loginDetails;


    // STATIC PUBLIC METHODS

    public static void sendQuizData(final Context context) {
        Intent xmppServiceIntent = getSendDataServiceIntent(context);
        xmppServiceIntent.putExtra(KEY_METHOD, METHOD_SEND_QUIZ_DATA);
        context.startService(xmppServiceIntent);
    }

    public static void sendAssignmentData(final Context context) {
        Intent xmppServiceIntent = getSendDataServiceIntent(context);
        xmppServiceIntent.putExtra(KEY_METHOD, METHOD_SEND_ASSIGNMENT_DATA);
        context.startService(xmppServiceIntent);
    }

    public static void createLoginLog(final Context context) {
        Intent xmppServiceIntent = getSendDataServiceIntent(context);
        xmppServiceIntent.putExtra(KEY_METHOD, METHOD_CREATE_LOGIN_LOG);
        context.startService(xmppServiceIntent);
    }

    public static void createComment(final Context context, CommentsData data) {
        Intent xmppServiceIntent = getSendDataServiceIntent(context);
        xmppServiceIntent.putExtra(KEY_METHOD, METHOD_CREATE_COMMENT);
        xmppServiceIntent.putExtra(KEY_DATA, data);
        context.startService(xmppServiceIntent);
    }


    public static void createLike(final Context context, Bundle data) {
        Intent xmppServiceIntent = getSendDataServiceIntent(context);
        xmppServiceIntent.putExtra(KEY_METHOD, METHOD_CREATE_LIKE);
        xmppServiceIntent.putExtra(KEY_DATA, data);
        context.startService(xmppServiceIntent);
    }

    public static void stopService(final Context context) {
        Intent xmppServiceIntent = getSendDataServiceIntent(context);
        context.stopService(xmppServiceIntent);
    }


    private static Intent getSendDataServiceIntent(final Context context) {
        return new Intent(context, SendDataService.class);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent != null) {
            int method = intent.getIntExtra(KEY_METHOD, -1);
            if (method != -1) {
                mHandler.sendMessage(mHandler.obtainMessage(method, intent));
            }
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);
        dataBaseHelper = new DataBaseHelper(this);
        loginDetails = new Gson().fromJson(new PreferenceHelper(this).getUser_detials(), Login.class);

        HandlerThread mThread = new HandlerThread("sendDataThread",
                android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        mThread.start();
        mHandler = new Handler(mThread.getLooper(), this);
    }

    @Override
    public boolean handleMessage(Message msg) {

        int method = msg.what;
        //AppLog.Log("User_xmpp", "in switch case  " + method);

        switch (method) {
            case METHOD_SEND_QUIZ_DATA:

                callSendQuizResultApi();

                break;

            case METHOD_SEND_ASSIGNMENT_DATA:

                callSendAssignmentDataApi();

                break;


            case METHOD_CREATE_LOGIN_LOG:

                callCreateLoginLogApi();

                break;


            case METHOD_CREATE_COMMENT:

                CommentsData data = ((Intent) msg.obj).getParcelableExtra(KEY_DATA);
                callCreateCommentApi(data);

                break;


            case METHOD_CREATE_LIKE:


                Bundle bundle = ((Intent) msg.obj).getBundleExtra(KEY_DATA);

                Map<String, String> param = new HashMap<>();
                param.put(Const.Params.ENTITY_TYPE_ID, bundle.getString(Const.IntentParams.ENTITY_TYPE_ID));
                param.put(Const.Params.STATUS, bundle.getString(Const.IntentParams.STATUS));
                param.put(Const.Params.USER_ID, bundle.getString(Const.IntentParams.USER_ID));


                callCreateLike(param);

                break;

        }


        return false;
    }

    private void callCreateLike(Map<String, String> param) {


        Call<ResponseBody> call = apiInterface.createLike(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.CREATE_LIKE);
    }

    private void callCreateCommentApi(CommentsData data) {

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.ANNOUNCEMENT_ID, "" + data.getAnnouncementId());
        param.put(Const.Params.USER_ID, "" + data.getUserId().get(0).getId());
        param.put(Const.Params.CONTENT, "" + data.getContent());

        Call<ResponseBody> call = apiInterface.createComments(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.CREATE_COMMENTS);
    }

    private void callCreateLoginLogApi() {


        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.USER_ID, "" + loginDetails.getUser().getId());
        Call<ResponseBody> call = apiInterface.createLoginLog(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.CREATE_LOGIN_LOG);

    }

    private void callSendAssignmentDataApi() {

        ArrayList<AssignmentData> assignmentData = dataBaseHelper.getSubmittedAssignmentDetails();

        for (int i = 0; i < assignmentData.size(); i++) {

            Map<String, String> param = new HashMap<>();
            param.put(Const.Params.QUESTION_ID, "" + assignmentData.get(i).getId());
            param.put(Const.Params.STUDENT_ID, loginDetails.getUser().getId());
            param.put(Const.Params.FILE_ID, assignmentData.get(i).getAssignmentFile());
            param.put(Const.Params.ANSWER, assignmentData.get(i).getAssignmentText());

            Call<ResponseBody> call = apiInterface.createAssignmentAnswer(param);

            new RetroFitRequest().networkRequest(this, call, this,
                    Const.ServiceCode.SUBMIT_ASSIGNMENT_ANSWER);
        }

    }

    private void callSendQuizResultApi() {

        ArrayList<QuizData> quizDatas = dataBaseHelper.getSubmittedQuizDetails();


        for (int i = 0; i < quizDatas.size(); i++) {
            Map<String, String> param = new HashMap<>();
            param.put(Const.Params.QUIZ_ID, quizDatas.get(i).getId());
            param.put(Const.Params.SCORE, "" + quizDatas.get(i).getScore());
            param.put(Const.Params.STUDENT_ID, loginDetails.getUser().getId());

            Call<ResponseBody> call = apiInterface.sendQuizResult(param);

            new RetroFitRequest().networkRequest(this, call, this,
                    Const.ServiceCode.SEND_QUIZ_SCORE);
        }


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {

            case Const.ServiceCode.SEND_QUIZ_SCORE:

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    dataBaseHelper.updateQuizStatus(jsonObject.getString(Const.Json_Parms.QUIZ_ID), Const.STATUS.UPDATED);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;


            case Const.ServiceCode.SUBMIT_ASSIGNMENT_ANSWER:

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    dataBaseHelper.updateAssignmentStatus(jsonObject.getString(Const.Json_Parms.QUESTION_ID), Const.STATUS.UPDATED);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;


            case Const.ServiceCode.CREATE_COMMENTS:

                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;


        }
    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }
}

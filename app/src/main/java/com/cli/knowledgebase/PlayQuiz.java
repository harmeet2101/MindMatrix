package com.cli.knowledgebase;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cli.knowledgebase.AppService.SendDataService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Constants.Const;
import DataBase.DataBaseHelper;
import Interfaces.AsyncTaskCompleteListener;
import Model.Login;
import Model.QuizData;
import Model.QuizQuestionData;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.FileUtils;
import Utils.PreferenceHelper;
import Utils.TimeUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class PlayQuiz extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {

    TextView tv_menu_name;
    ImageView iv_back;

    QuizData quizData;
    ArrayList<QuizQuestionData> quizQuestionData = new ArrayList<>();
    ApiInterface apiInterface;
    TextView tv_timer;
    TextView tv_question_count, tv_question;
    RadioButton button_option_1, button_option_2, button_option_3, button_option_4;
    TextView tv_prev, tv_submit, tv_next;

    int question_count = 1;

    int[] answered_question;
    RadioGroup radio_group_options;

    boolean isReset = false;
    PreferenceHelper preferenceHelper;
    Login loginDetails;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);


        quizData = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).
                getParcelable(Const.IntentParams.QUIZ_DATA);
        preferenceHelper = new PreferenceHelper(this);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);


        widgets();

        getQuizQuestions();


    }

    private void widgets() {

        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(quizData.getName());

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_question_count = (TextView) findViewById(R.id.tv_question_count);
        tv_question = (TextView) findViewById(R.id.tv_question);

        button_option_1 = (RadioButton) findViewById(R.id.button_option_1);
        button_option_2 = (RadioButton) findViewById(R.id.button_option_2);
        button_option_3 = (RadioButton) findViewById(R.id.button_option_3);
        button_option_4 = (RadioButton) findViewById(R.id.button_option_4);

        tv_prev = (TextView) findViewById(R.id.tv_prev);
        tv_prev.setOnClickListener(this);

        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);

        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);

        radio_group_options = (RadioGroup) findViewById(R.id.radio_group_options);
        radio_group_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (isReset)
                    return;


                if (question_count == quizQuestionData.size()) {
                    tv_next.setEnabled(false);
                } else {
                    tv_next.setEnabled(true);

                }

                switch (checkedId) {

                    case R.id.button_option_1:

                        answered_question[question_count - 1] = 1;

                        break;

                    case R.id.button_option_2:

                        answered_question[question_count - 1] = 2;

                        break;

                    case R.id.button_option_3:

                        answered_question[question_count - 1] = 3;

                        break;

                    case R.id.button_option_4:

                        answered_question[question_count - 1] = 4;

                        break;

                }

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;

            case R.id.tv_prev:


                question_count = question_count - 1;
                loadQuestion(question_count);


                break;

            case R.id.tv_submit:

                // onBackPressed();
                score = calculateScore();
                showScore(score);


                break;

            case R.id.tv_next:


                question_count = question_count + 1;
                loadQuestion(question_count);


                break;
        }

    }


    private void getQuizQuestions() {

        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();

        param.put(Const.Params.QUIZ_ID, quizData.getId());


        Call<ResponseBody> call = apiInterface.getQuizQuestion(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_QUIZ_QUESTIONS);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        AndyUtils.cancelLoading();

        switch (serviceCode) {

            case Const.ServiceCode.GET_QUIZ_QUESTIONS:


                Gson gson = new Gson();
                quizQuestionData = gson.fromJson(response, new TypeToken<List<QuizQuestionData>>() {
                }.getType());

                if (quizQuestionData != null && quizQuestionData.size()>0) {
                    answered_question = new int[quizQuestionData.size()];
                    startTimer();
                    loadQuestion(question_count);
                }else{

                    Toast.makeText(PlayQuiz.this,getString(R.string.file_not_uploaded_error),Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case Const.ServiceCode.SEND_QUIZ_SCORE:


                FileUtils.writeConfiguration(PlayQuiz.this, Const.FileNames.QUIZ +
                        loginDetails.getUser().getId() + quizData.getId() +
                        Const.FileNames.FILE_EXTENSION, "" + score);

                AndyUtils.cancelLoading();
                showScore(score);


                break;
        }

    }

    private void loadQuestion(int question_count) {


        if (question_count == 1) {
            tv_prev.setEnabled(false);

        } else {
            tv_prev.setEnabled(true);
        }

        if (question_count == quizQuestionData.size()) {
            tv_next.setEnabled(false);
        } else {
            tv_next.setEnabled(true);

        }


        if (question_count == 0 || question_count <= quizQuestionData.size()) {

            resetOptions();
            QuizQuestionData question = quizQuestionData.get(question_count - 1);

            tv_question_count.setText(question_count + ".");
            tv_question.setText(question.getQuestion());

            if (question.getQuesOption1() != null && !question.getQuesOption1().equals("")) {
                button_option_1.setVisibility(View.VISIBLE);
                button_option_1.setText(question.getQuesOption1());
            } else {
                button_option_1.setVisibility(View.GONE);
            }
            if (question.getQuesOption2() != null && !question.getQuesOption2().equals("")) {
                button_option_2.setVisibility(View.VISIBLE);
                button_option_2.setText(question.getQuesOption2());
            } else {
                button_option_2.setVisibility(View.GONE);
            }

            if (question.getQuesOption3() != null && !question.getQuesOption3().equals("")) {
                button_option_3.setVisibility(View.VISIBLE);
                button_option_3.setText(question.getQuesOption3());
            } else {
                button_option_3.setVisibility(View.GONE);
            }

            if (question.getQuesOption4() != null && !question.getQuesOption4().equals("")) {
                button_option_4.setVisibility(View.VISIBLE);
                button_option_4.setText(question.getQuesOption4());
            } else {
                button_option_4.setVisibility(View.GONE);
            }

            setAnswer();


        }
    }

    private void setAnswer() {

        //tv_next.setEnabled(true);

        if (question_count == quizQuestionData.size()) {
            tv_next.setEnabled(false);
        } else {
            tv_next.setEnabled(true);

        }

        switch (answered_question[question_count - 1]) {

            case 1:

                button_option_1.setChecked(true);
                break;

            case 2:

                button_option_2.setChecked(true);
                break;

            case 3:

                button_option_3.setChecked(true);

                break;

            case 4:

                button_option_4.setChecked(true);
                break;

            case 0:

                tv_next.setEnabled(false);

                break;

        }


    }

    private void resetOptions() {

        isReset = true;
/*
        button_option_1.setChecked(false);
        button_option_2.setChecked(false);
        button_option_3.setChecked(false);
        button_option_4.setChecked(false);*/

        radio_group_options.clearCheck();

        isReset = false;
    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }


    CountDownTimer timer;

    private void startTimer() {

        if (timer != null)
            return;

        timer = new CountDownTimer(Integer.parseInt(
                quizData.getDuration()) * 60 * 1000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                tv_timer.setText("" + String.format(TimeUtils.TIMER_FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {


                int score = calculateScore();

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PlayQuiz.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(PlayQuiz.this.getString(R.string.quiz_timer_alert))
                        .setContentText(getString(R.string.quiz_score, "" + score))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        });

                sweetAlertDialog.show();


            }
        }.start();

    }


    private int calculateScore() {

        int score = 0;

        if (quizQuestionData != null) {


            for (int i = 0; i < quizQuestionData.size(); i++) {
                if (quizQuestionData.get(i).getCorrect().equals("" + answered_question[i])) {

                    score = score + Integer.parseInt(quizQuestionData.get(i).getMarks());
                }
            }
        }
        sendScore(score);


        return score;


    }

    private void sendScore(int score) {


        new DataBaseHelper(this).insertQuizDetails(quizData.getId(), "" + score, Const.STATUS.SUBMITTED);
        SendDataService.sendQuizData(this);



       /* AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.QUIZ_ID, quizData.getId());
        param.put(Const.Params.SCORE, "" + score);
        param.put(Const.Params.STUDENT_ID, loginDetails.getUser().getId());

        Call<ResponseBody> call = apiInterface.sendQuizResult(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.SEND_QUIZ_SCORE);
*/
    }


    private void showScore(int score) {

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PlayQuiz.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(PlayQuiz.this.getString(R.string.quiz_score, "" + score))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                });


        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PlayQuiz.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(PlayQuiz.this.getString(R.string.quiz_quit_warning))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                    }
                });

        sweetAlertDialog.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}


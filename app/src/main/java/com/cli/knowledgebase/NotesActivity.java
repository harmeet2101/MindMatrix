package com.cli.knowledgebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.AnnouncementAdapter;
import Adapter.NotesAdapter;
import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Interfaces.RecyclerViewItemClickListener;
import Model.FileDetails;
import Model.Login;
import Model.Notes;
import Model.Specialization;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener, RecyclerViewItemClickListener {


    PreferenceHelper preferenceHelper;
    ApiInterface apiInterface;
    Login loginDetails;
    TextView tv_menu_name;
    ImageView iv_back;
    Specialization specialization;

    ArrayList<Notes> notes;
    ArrayList<FileDetails> fileDetails= new ArrayList<>();

    RecyclerView recycler_view_announcement, recycler_view_notes;

    AnnouncementAdapter announcementAdapter;
    NotesAdapter notesAdapter;

    LinearLayout lay_notes;
    ImageView iv_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferenceHelper = new PreferenceHelper(this);
        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);
        apiInterface = ApiClient.getBaseClient(this).create(ApiInterface.class);
        specialization = getIntent().getBundleExtra(Const.IntentParams.INTENT_BUNDLE).
                getParcelable(Const.IntentParams.SPECIALIZATION_DATA);

        widgets();

        getNotes();

    }

    private void widgets() {


        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(specialization.getName());

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        recycler_view_announcement = (RecyclerView) findViewById(R.id.recycler_view_announcement);
        recycler_view_announcement.setLayoutManager(new LinearLayoutManager(NotesActivity.this));

        recycler_view_notes = (RecyclerView) findViewById(R.id.recycler_view_notes);
        recycler_view_notes.setLayoutManager(new GridLayoutManager(NotesActivity.this, 3));

        lay_notes = (LinearLayout) findViewById(R.id.lay_notes);
        iv_nodata = (ImageView) findViewById(R.id.iv_nodata);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.iv_back:

                onBackPressed();

                break;
        }
    }


    private void getNotes() {


        AndyUtils.showLoading(this, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.COURSE_ID, "" + specialization.getId());
        param.put(Const.Params.TYPE, "Announcement");

        Call<ResponseBody> call = apiInterface.getAnnouncements(param);

        new RetroFitRequest().networkRequest(this, call, this,
                Const.ServiceCode.GET_NOTES);

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {

            case Const.ServiceCode.GET_NOTES:

                AndyUtils.cancelLoading();

                Gson gson = new Gson();
                notes = gson.fromJson(response, new TypeToken<List<Notes>>() {
                }.getType());


                loadData();


                break;

        }

    }

    private void loadData() {

        if (notes != null) {

            lay_notes.setVisibility(View.VISIBLE);
            iv_nodata.setVisibility(View.GONE);

            if (announcementAdapter == null) {
                announcementAdapter = new AnnouncementAdapter(NotesActivity.this, notes);
                recycler_view_announcement.setAdapter(announcementAdapter);
            } else {

                announcementAdapter.notifyDataSetChanged();
            }

            loadNotes();


        } else {

            lay_notes.setVisibility(View.GONE);
            iv_nodata.setVisibility(View.VISIBLE);
        }


    }

    private void loadNotes() {


        for (int i = 0; i < notes.size(); i++) {


            if (notes.get(i).getFileId() != null && notes.get(i).getFileId().size() > 0)
                fileDetails.add(notes.get(i).getFileId().get(0));


        }

        if (notesAdapter == null) {

            notesAdapter = new NotesAdapter(NotesActivity.this, fileDetails, this);
            recycler_view_notes.setAdapter(notesAdapter);

        } else {

            notesAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }

    @Override
    public void onItemClick(int position) {

    }
}

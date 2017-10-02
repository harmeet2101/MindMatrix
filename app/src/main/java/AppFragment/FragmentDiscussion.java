package AppFragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.cli.knowledgebase.AppService.SendDataService;
import com.cli.knowledgebase.CreateDiscussionActivity;
import com.cli.knowledgebase.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.CommentsAdapter;
import Adapter.DiscussionAdapter;
import Adapter.TagAutoCompleteAdapter;
import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Interfaces.RecyclerViewItemClickListener;
import Model.CommentsData;
import Model.Discussion;
import Model.DiscussionData;
import Model.DiscussionTags;
import Model.UserDetails;
import Model.Like;
import Model.Login;
import Model.TrendingTags;
import Retrofit_Network_Utils.ApiClient;
import Retrofit_Network_Utils.ApiInterface;
import Retrofit_Network_Utils.RetroFitRequest;
import Utils.AndyUtils;
import Utils.PreferenceHelper;
import Utils.TimeUtils;
import Utils.ViewUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;


public class FragmentDiscussion extends Fragment implements AsyncTaskCompleteListener, RecyclerViewItemClickListener, View.OnClickListener {


    ApiInterface apiInterface;
    Login loginDetails;
    PreferenceHelper preferenceHelper;
    ArrayList<DiscussionTags> discussionTags = new ArrayList<>();
    ArrayList<String> tag_id = new ArrayList<>();

    ArrayList<DiscussionData> discussionData = new ArrayList<>();

    private int max_post_count = 0;

    RecyclerView recycler_view_discussion, recycler_view_comments;
    ImageView iv_emptyview;
    DiscussionAdapter adapter;
    AutoCompleteTextView autoCompleteTextView_tags;
    LinearLayout lay_selected_tag;
    TextView tv_selected_tag;
    TagAutoCompleteAdapter autoCompleteAdapter;

    ChipCloud chip_cloud;


    Boolean isLoading = false;
    String selectedTagId = "";

    ArrayList<TrendingTags> trendingTags = new ArrayList<>();
    String[] trendingTagsNames;

    RelativeLayout rel_all_data;
    RelativeLayout lay_comments_data;

    TextView tv_topic_description, tv_topic_heading;

    ArrayList<CommentsData> commentsData = new ArrayList<>();
    CommentsAdapter commentsAdapter;

    TextView tv_submit_comment, tv_collapse, tv_create_discussion, tv_clearTags;
    EditText et_comment;

    TextView tv_vote_count;

    ScrollView scrollView;


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.fragment_discussion, container, false);


        preferenceHelper = new PreferenceHelper(mActivity);
        apiInterface = ApiClient.getBaseClient(mActivity).create(ApiInterface.class);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);

        widgets(rootView);

        getAllTags();

        return rootView;
    }


    private void widgets(View rootView) {


        recycler_view_discussion = (RecyclerView) rootView.findViewById(R.id.recycler_view_discussion);
        recycler_view_discussion.setLayoutManager(new LinearLayoutManager(mActivity));

        recycler_view_comments = (RecyclerView) rootView.findViewById(R.id.recycler_view_comments);
        recycler_view_comments.setLayoutManager(new LinearLayoutManager(mActivity));

        // Pagination
        recycler_view_discussion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();


                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && max_post_count - 1 > discussionData.size()) {

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        isLoading = true;
                        adapter.addLoading();
                        if (selectedTagId.equals(""))
                            getDiscussionPosts();
                        else
                            getDiscussionByTagsPosts();
                    }
                }

            }
        });


        iv_emptyview = (ImageView) rootView.findViewById(R.id.iv_emptyview);
        autoCompleteTextView_tags = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView_tags);
        autoCompleteTextView_tags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedTag = autoCompleteAdapter.getItem(position).getTitle();

                lay_selected_tag.setVisibility(View.VISIBLE);
                tv_selected_tag.setText(selectedTag);
                ViewUtils.closeKeyboard(mActivity);

                selectedTagId = autoCompleteAdapter.getItem(position).getId();
                getTagData();

                clearChipCloud();
                tv_clearTags.setEnabled(true);


            }
        });

        lay_selected_tag = (LinearLayout) rootView.findViewById(R.id.lay_selected_tag);
        lay_selected_tag.setOnClickListener(this);

        tv_selected_tag = (TextView) rootView.findViewById(R.id.tv_selected_tag);

        chip_cloud = (ChipCloud) rootView.findViewById(R.id.chip_cloud);
        chip_cloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int i) {

                tv_clearTags.setEnabled(true);

                if (trendingTags != null && trendingTags.size() > i) {

                    removeSelectedTag();

                    selectedTagId = trendingTags.get(i).getStatus();
                    getTagData();

                }
            }

            @Override
            public void chipDeselected(int i) {

            }
        });


        rel_all_data = (RelativeLayout) rootView.findViewById(R.id.rel_all_data);
        lay_comments_data = (RelativeLayout) rootView.findViewById(R.id.lay_comments_data);

        tv_topic_description = (TextView) rootView.findViewById(R.id.tv_topic_description);
        tv_topic_description.setTextIsSelectable(true);

        tv_submit_comment = (TextView) rootView.findViewById(R.id.tv_submit_comment);
        tv_submit_comment.setOnClickListener(this);

        et_comment = (EditText) rootView.findViewById(R.id.et_comment);
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().equals("")) {

                    tv_submit_comment.setEnabled(true);

                } else {

                    tv_submit_comment.setEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);

        tv_collapse = (TextView) rootView.findViewById(R.id.tv_collapse);
        tv_collapse.setOnClickListener(this);

        tv_create_discussion = (TextView) rootView.findViewById(R.id.tv_create_discussion);
        tv_create_discussion.setOnClickListener(this);


        tv_clearTags = (TextView) rootView.findViewById(R.id.tv_clearTags);
        tv_clearTags.setOnClickListener(this);

        tv_topic_heading = (TextView) rootView.findViewById(R.id.tv_topic_heading);
        tv_vote_count = (TextView) rootView.findViewById(R.id.tv_count);



    }

    private void clearChipCloud() {

        chip_cloud.removeAllViews();
        addTrendingTags();

    }


    Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }


    private void getAllTags() {

        AndyUtils.showLoading(mActivity, false, "");

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.TYPE, "tags");


        Call<ResponseBody> call = apiInterface.getAnnouncementsWithoutUniversity(param);

        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_TAGS);
    }


    private void getDiscussionPosts() {

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.TYPE, "discuss");
        param.put(Const.Params.START, "" + discussionData.size());
        param.put(Const.Params.LIMIT, "10");
        param.put(Const.Params.STATUS, "2");

        Call<ResponseBody> call = apiInterface.getAnnouncements(param);

        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_DISCUSSION);
    }


    private void getDiscussionByTagsPosts() {

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.TYPE, "discuss");
        param.put(Const.Params.START, "" + discussionData.size());
        param.put(Const.Params.LIMIT, "10");
        param.put(Const.Params.TAG, selectedTagId);

        Call<ResponseBody> call = apiInterface.getAnnouncementsOfTAG(param);

        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_DISCUSSION);
    }


    private void getTopTrendingTags() {


        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.UNIVERSITY_ID, loginDetails.getUser().getUniversityId());
        param.put(Const.Params.MOBILE, "true");

        Call<ResponseBody> call = apiInterface.getTopTrendingTags(param);
        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_TOP_TREADING_TAGS);
    }


    private void getDiscussionComments(String id) {


        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.ANNOUNCEMENT_ID, id);

        Call<ResponseBody> call = apiInterface.getComments(param);
        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_COMMENTS);


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {

            case Const.ServiceCode.GET_TAGS:

                Gson gson = new Gson();
                discussionTags = gson.fromJson(response, new TypeToken<List<DiscussionTags>>() {
                }.getType());


                if (discussionTags != null) {

                    for (int i = 0; i < discussionTags.size(); i++)
                        tag_id.add(discussionTags.get(i).getId());

                }

                if (discussionTags != null) {
                    ArrayList<DiscussionTags> autoCompleteTags = new ArrayList<>();
                    autoCompleteTags.addAll(discussionTags);

                    if (autoCompleteAdapter == null) {
                        autoCompleteAdapter = new TagAutoCompleteAdapter(mActivity,
                                R.layout.item_tag_autocomplete, autoCompleteTags);
                        autoCompleteTextView_tags.setAdapter(autoCompleteAdapter);
                    }

                    getTopTrendingTags();
                    getDiscussionPosts();
                } else {

                    AndyUtils.cancelLoading();
                    tv_create_discussion.setEnabled(false);
                }


                break;


            case Const.ServiceCode.GET_DISCUSSION:


                isLoading = false;
                if (adapter != null && discussionData.size() > 0)
                    adapter.removeLoading();

                try {
                    Discussion discussion = new Gson().fromJson(response, new TypeToken<Discussion>() {
                    }.getType());

                    max_post_count = discussion.getNumFound();


                    List<DiscussionData> sorted_data = sortData(discussion.getData());

                    discussionData.addAll(sorted_data);


                } catch (Exception e) {

                    e.printStackTrace();

                }
                AndyUtils.cancelLoading();
                loadData();


                break;


            case Const.ServiceCode.GET_TOP_TREADING_TAGS:

                // AndyUtils.cancelLoading();

                if (response != null && !response.equals("")) {

                    trendingTags = new Gson().fromJson(response, new TypeToken<List<TrendingTags>>() {
                    }.getType());

                    addTrendingTags();

                    //    trendingTags.get(0);


                }


                break;


            case Const.ServiceCode.GET_COMMENTS:


                if (response != null && !response.equals("")) {

                    commentsData = new Gson().fromJson(response, new TypeToken<List<CommentsData>>() {
                    }.getType());

                    loadComments();


                }


                break;

        }

    }

    private void loadComments() {

        if (commentsAdapter == null) {

            Collections.sort(commentsData);
            commentsAdapter = new CommentsAdapter(mActivity, commentsData);
            recycler_view_comments.setAdapter(commentsAdapter);

        } else {

            commentsAdapter.notifyDataSetChanged();
        }

    }

    private void addTrendingTags() {

        for (int i = 0; i < trendingTags.size(); i++) {


            int index = tag_id.indexOf(trendingTags.get(i).getStatus());


            if (index != -1) {
                trendingTags.get(i).setDiscussionTag(
                        discussionTags.get(index));

                chip_cloud.addChip(discussionTags.get
                        (tag_id.indexOf(trendingTags.get(i).getStatus())).getTitle());
            }
        }

    }

    private List<DiscussionData> sortData(List<DiscussionData> data) {


        for (int i = 0; i < data.size(); i++) {

            DiscussionData discussionData = data.get(i);
            int like_count = 0;
            String tags = "";

            List<Like> like = discussionData.getLike();

            if (like != null) {
                for (int j = 0; j < like.size(); j++) {

                    if (like.get(j).getStatus().equals("1")) {

                        like_count = like_count + 1;

                        if (like.get(j).getUserId().equals(loginDetails.getUser().getId())) {

                            data.get(i).setIVoted(true);


                        }

                    } else if (like.get(j).getStatus().equals("0")) {


                        if (like.get(j).getUserId().equals(loginDetails.getUser().getId())) {

                            data.get(i).setIFlagged(true);

                        }

                    } else {

                        // get tags

                        int index = tag_id.indexOf(like.get(j).getStatus());

                        if (index != -1) {

                            try {
                                if (!tags.equals("")) {

                                    tags = tags + " , " + discussionTags.get(index).getTitle().trim();

                                } else {

                                    tags = discussionTags.get(index).getTitle().trim();

                                }
                            } catch (Exception e) {

                                e.printStackTrace();

                            }
                        }
                    }

                }
            }

            data.get(i).setLikeCount(like_count);
            data.get(i).setTags(tags);


        }


        return data;

    }

    private void loadData() {


        rel_all_data.setVisibility(View.VISIBLE);
        lay_comments_data.setVisibility(View.GONE);

        if (discussionData != null && discussionData.size() > 0) {

            iv_emptyview.setVisibility(View.GONE);
            recycler_view_discussion.setVisibility(View.VISIBLE);

            if (adapter == null) {

                adapter = new DiscussionAdapter(mActivity, discussionData, this, loginDetails);
                recycler_view_discussion.setAdapter(adapter);

            } else {

                adapter.notifyDataSetChanged();

            }
        } else {

            iv_emptyview.setVisibility(View.VISIBLE);
            recycler_view_discussion.setVisibility(View.GONE);
        }

    }

    @Override
    public void onTaskFailure(int serviceCode) {

    }

    @Override
    public void onItemClick(int position) {


        rel_all_data.setVisibility(View.GONE);
        lay_comments_data.setVisibility(View.VISIBLE);

        DiscussionData bean = adapter.getList().get(position);

        tv_topic_description.setText(Html.fromHtml(bean.getAnnouncementMsg()));
        tv_topic_heading.setText(Html.fromHtml(mActivity.getString(R.string.discussion_topic, bean.getTitle())));
        tv_vote_count.setText(""+bean.getLikeCount());


        commentsData.clear();
        if (commentsAdapter != null)
            commentsAdapter.notifyDataSetChanged();

        tv_submit_comment.setTag(bean.getId());

        getDiscussionComments(bean.getId());

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lay_selected_tag:

                removeSelectedTag();

                AndyUtils.showLoading(mActivity, false, "");
                discussionData.clear();
                adapter.notifyDataSetChanged();
                getDiscussionPosts();

                break;

            case R.id.tv_submit_comment:


                ArrayList<UserDetails> users = new ArrayList<>();
                users.add(new UserDetails(loginDetails.getUser().getId(), loginDetails.getUser().getFirstname(), loginDetails.getUser().getLastname(),
                        loginDetails.getUser().getEmail(), loginDetails.getUser().getProfileUrl()));

                CommentsData data = new CommentsData("0", (String) tv_submit_comment.getTag(),
                        users, et_comment.getText().toString().trim(), TimeUtils.getGmtTime());

                commentsData.add(data);
                loadComments();
                SendDataService.createComment(mActivity, data);

                et_comment.setText("");
                ViewUtils.closeKeyboard(mActivity);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                }, 500);


                break;


            case R.id.tv_collapse:

                rel_all_data.setVisibility(View.VISIBLE);
                lay_comments_data.setVisibility(View.GONE);


                break;

            case R.id.tv_create_discussion:

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Const.IntentParams.DISCUSSION_TAGS, discussionTags);

                ViewUtils.launchActivity(mActivity, bundle, CreateDiscussionActivity.class);

                break;


            case R.id.tv_clearTags:

                removeSelectedTag();
                clearChipCloud();

                discussionData.clear();
                adapter.notifyDataSetChanged();
                AndyUtils.showLoading(mActivity, false, "");
                getDiscussionPosts();

                tv_clearTags.setEnabled(false);


                break;

        }


    }

    private void removeSelectedTag() {


        autoCompleteTextView_tags.setText("");
        lay_selected_tag.setVisibility(View.INVISIBLE);
        tv_selected_tag.setText("");
        selectedTagId = "";


    }

    public void getTagData() {
        discussionData.clear();
        adapter.notifyDataSetChanged();
        AndyUtils.showLoading(mActivity, false, "");
        getDiscussionByTagsPosts();
    }
}
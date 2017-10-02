package AppFragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cli.knowledgebase.AppService.SendDataService;
import com.cli.knowledgebase.CreateBlogActivity;
import com.cli.knowledgebase.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.BlogAdapter;
import Adapter.CommentsAdapter;
import Constants.Const;
import Interfaces.AsyncTaskCompleteListener;
import Interfaces.RecyclerViewItemClickListener;
import Model.Blog;
import Model.BlogData;
import Model.CommentsData;
import Model.UserDetails;
import Model.Like;
import Model.Login;
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


public class Fragment_BLOG extends Fragment implements AsyncTaskCompleteListener, RecyclerViewItemClickListener, View.OnClickListener {


    ApiInterface apiInterface;
    Login loginDetails;
    PreferenceHelper preferenceHelper;
    ArrayList<BlogData> blogData = new ArrayList<>();

    RecyclerView recycler_view_blog, recycler_view_comments;
    ImageView iv_emptyview;
    BlogAdapter adapter;

    Boolean isLoading = false;

    int max_post_count = 0;

    RelativeLayout rel_all_data;
    RelativeLayout lay_comments_data;

    ArrayList<CommentsData> commentsData = new ArrayList<>();
    CommentsAdapter commentsAdapter;

    TextView tv_topic_description, tv_topic_heading;

    TextView tv_submit_comment, tv_collapse, tv_create_blog;
    EditText et_comment;

    ScrollView scrollView;
    MaterialSpinner spinner;
    String[] items = {"All Blogs", "My Blogs"};

    boolean isMyBlogCalled = false;

    Boolean isMyBlog = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.fragment_blog, container, false);

        preferenceHelper = new PreferenceHelper(mActivity);
        apiInterface = ApiClient.getBaseClient(mActivity).create(ApiInterface.class);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(preferenceHelper.getUser_detials(), Login.class);

        widgets(rootView);

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(broadcastReceiver,
                new IntentFilter(Const.IntentFilter.UPDATE_BLOG));

        AndyUtils.showLoading(mActivity, false, "");
        getBlogData();

        return rootView;
    }

    private void widgets(View rootView) {


        recycler_view_blog = (RecyclerView) rootView.findViewById(R.id.recycler_view_blog);
        recycler_view_blog.setLayoutManager(new GridLayoutManager(mActivity, 3));

        recycler_view_comments = (RecyclerView) rootView.findViewById(R.id.recycler_view_comments);
        recycler_view_comments.setLayoutManager(new LinearLayoutManager(mActivity));

        rel_all_data = (RelativeLayout) rootView.findViewById(R.id.rel_all_data);
        lay_comments_data = (RelativeLayout) rootView.findViewById(R.id.lay_comments_data);


        recycler_view_blog.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                if (!isLoading && max_post_count - 1 > blogData.size()) {

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        isLoading = true;
                        adapter.addLoading();

                        getBlogData();
                    }

                }
            }
        });


        iv_emptyview = (ImageView) rootView.findViewById(R.id.iv_emptyview);

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

        tv_topic_heading = (TextView) rootView.findViewById(R.id.tv_topic_heading);

        tv_create_blog = (TextView) rootView.findViewById(R.id.tv_create_blog);
        tv_create_blog.setOnClickListener(this);

        spinner = (MaterialSpinner) rootView.findViewById(R.id.spinner);
        spinner.setItems(items);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {


                if (blogData != null)
                    blogData.clear();
                else
                    blogData = new ArrayList<BlogData>();

                if (adapter != null)
                    adapter.notifyDataSetChanged();

                AndyUtils.showLoading(mActivity, false, "");

                if (position == 0) {

                    isMyBlogCalled = false;
                    getBlogData();

                } else {

                    isMyBlogCalled = true;
                    getMyBlogData();

                }

            }
        });

    }


    Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }


    public void getBlogData() {

        isMyBlog = false;

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.TYPE, "blog");
        param.put(Const.Params.START, "" + blogData.size());
        param.put(Const.Params.LIMIT, "15");
        param.put(Const.Params.STATUS, "2");

        Call<ResponseBody> call = apiInterface.getAnnouncements(param);

        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_BLOG);
    }


    public void getMyBlogData() {

        isMyBlog = true;

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.TYPE, "blog");
        param.put(Const.Params.USER_ID, loginDetails.getUser().getId());

        Call<ResponseBody> call = apiInterface.getAnnouncements(param);
        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_MY_BLOG);
    }


    private void getBlogComments(String id) {

        Map<String, String> param = new HashMap<>();
        param.put(Const.Params.ANNOUNCEMENT_ID, id);

        Call<ResponseBody> call = apiInterface.getComments(param);
        new RetroFitRequest().networkRequest(mActivity, call, this,
                Const.ServiceCode.GET_COMMENTS);


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {


        switch (serviceCode) {

            case Const.ServiceCode.GET_BLOG:


                isLoading = false;
                if (adapter != null && blogData.size() > 0)
                    adapter.removeLoading();

                try {
                    Blog blog = new Gson().fromJson(response, new TypeToken<Blog>() {
                    }.getType());

                    max_post_count = blog.getNumFound();


                    List<BlogData> sorted_data = sortData(blog.getData());

                    blogData.addAll(sorted_data);


                } catch (Exception e) {

                    e.printStackTrace();

                }
                AndyUtils.cancelLoading();
                recycler_view_blog.setLayoutManager(new GridLayoutManager(mActivity, 3));

                loadData();

                break;


            case Const.ServiceCode.GET_COMMENTS:

                if (response != null && !response.equals("")) {

                    commentsData = new Gson().fromJson(response, new TypeToken<List<CommentsData>>() {
                    }.getType());

                    loadComments();
                }

                break;

            case Const.ServiceCode.GET_MY_BLOG:

                isLoading = false;
                if (adapter != null && blogData.size() > 0)
                    adapter.removeLoading();

                try {


                    blogData = new Gson().fromJson(response, new TypeToken<List<BlogData>>() {
                    }.getType());

                    max_post_count = blogData.size();


                } catch (Exception e) {

                    e.printStackTrace();

                }

                recycler_view_blog.setLayoutManager(new LinearLayoutManager(mActivity));

                AndyUtils.cancelLoading();
                loadData();

                break;

        }

    }

    @Override
    public void onTaskFailure(int serviceCode) {

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


    private List<BlogData> sortData(List<BlogData> data) {


        for (int i = 0; i < data.size(); i++) {

            BlogData blogData = data.get(i);
            int like_count = 0;
            String tags = "";

            List<Like> like = blogData.getLike();

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

        if (blogData != null && blogData.size() > 0) {

            if (isMyBlog)
                Collections.sort(blogData);

            iv_emptyview.setVisibility(View.GONE);
            recycler_view_blog.setVisibility(View.VISIBLE);

            //  if (adapter == null) {

            adapter = new BlogAdapter(mActivity, blogData, this, loginDetails, isMyBlog);
            recycler_view_blog.setAdapter(adapter);

           /* } else {

                adapter.notifyDataSetChanged();

            }*/
        } else {

            iv_emptyview.setVisibility(View.VISIBLE);
            recycler_view_blog.setVisibility(View.GONE);
        }

    }


    @Override
    public void onItemClick(int position) {

        BlogData bean = blogData.get(position);

        if (!bean.getStatus().equals("0")) {

            rel_all_data.setVisibility(View.GONE);
            lay_comments_data.setVisibility(View.VISIBLE);

            tv_topic_description.setText(Html.fromHtml(bean.getAnnouncementMsg()));
            tv_topic_heading.setText(Html.fromHtml(bean.getTitle()));


            commentsData.clear();
            if (commentsAdapter != null)
                commentsAdapter.notifyDataSetChanged();

            tv_submit_comment.setTag(bean.getId());

            getBlogComments(bean.getId());

        } else {


            Bundle bundle = new Bundle();
            bundle.putParcelable(Const.IntentParams.BLOG_DATA, blogData.get(position));
            ViewUtils.launchActivity(mActivity, bundle, CreateBlogActivity.class);


        }


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.tv_collapse:

                rel_all_data.setVisibility(View.VISIBLE);
                lay_comments_data.setVisibility(View.GONE);


                break;

            case R.id.tv_create_blog:

                ViewUtils.launchActivity(mActivity, null, CreateBlogActivity.class);

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

        }

    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isMyBlogCalled && blogData != null) {
                blogData.clear();
                loadData();

                getMyBlogData();

            }

        }
    };
}
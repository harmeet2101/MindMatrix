package Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cli.knowledgebase.AppService.SendDataService;
import com.cli.knowledgebase.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Constants.Const;
import Interfaces.RecyclerViewItemClickListener;
import Model.BlogData;
import Model.Login;
import Utils.TimeUtils;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<BlogData> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;
    Login login;
    Boolean isMyBlog;

    public BlogAdapter(Context con, ArrayList<BlogData> beanList,
                       RecyclerViewItemClickListener recyclerViewItemClickListener,
                       Login login, Boolean isMyBlog) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
        this.login = login;
        this.isMyBlog = isMyBlog;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_blog, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        final BlogData bean = beanList.get(position);

        if (bean.getTitle() != null) {

            holder.lay_loading.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);


            holder.tv_topic.setText(bean.getTitle());
            if (bean.getUserId() != null && bean.getUserId().size() > 0)
                holder.tv_posted_by.setText(Html.fromHtml(context.getString(R.string.discussion_posted_by, bean.getUserId().get(0).getFirstname() +
                        " " + bean.getUserId().get(0).getLastname())));
            else
                holder.tv_posted_by.setText("");

            holder.tv_posted_date.setText(Html.fromHtml(TimeUtils.getFormattedTime(bean.getCreatedAt(),
                    TimeUtils.BLOG_TIME_FORMAT)));

            holder.tv_vote_count.setText("" + bean.getLikeCount());

            holder.tv_description.setText(Html.fromHtml(bean.getAnnouncementMsg()));

            if (!bean.getStatus().equals("0")) {


                if (bean.getStatus().equals("1")) {

                    holder.tv_post_status.setVisibility(View.VISIBLE);

                    holder.tv_post_status.setText(context.getString(R.string.blog_subbmited));
                    holder.tv_post_status.setTextColor(ContextCompat.getColor(context, R.color.green));

                } else if (bean.getStatus().equals("3")) {
                    holder.tv_post_status.setVisibility(View.VISIBLE);

                    holder.tv_post_status.setText(context.getString(R.string.blog_rejected));
                    holder.tv_post_status.setTextColor(ContextCompat.getColor(context, R.color.red));


                } else {

                    holder.tv_post_status.setText(context.getString(R.string.blog_approved));
                    holder.tv_post_status.setTextColor(ContextCompat.getColor(context, R.color.green));
                   // holder.tv_post_status.setVisibility(View.INVISIBLE);


                }

                holder.lay_options.setVisibility(View.VISIBLE);
                holder.tv_edit_post.setVisibility(View.GONE);

            } else {
                holder.tv_post_status.setVisibility(View.VISIBLE);

                holder.tv_post_status.setText(context.getString(R.string.blog_drafted));
                holder.tv_post_status.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

                holder.lay_options.setVisibility(View.GONE);
                holder.tv_edit_post.setVisibility(View.VISIBLE);

            }

            if (!bean.getIVoted()) {

                holder.iv_like.setColorFilter(ContextCompat.getColor(context, R.color.black));

            } else {

                holder.iv_like.setColorFilter(ContextCompat.getColor(context, R.color.app_colour));

            }


            if (!bean.getIFlagged()) {

                holder.iv_flag.setColorFilter(ContextCompat.getColor(context, R.color.black));

            } else {

                holder.iv_flag.setColorFilter(ContextCompat.getColor(context, R.color.app_colour));

            }

            holder.lay_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!bean.getIVoted() && !bean.getIFlagged()) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Const.IntentParams.ENTITY_TYPE_ID, bean.getId());
                        bundle.putString(Const.IntentParams.STATUS, "1");
                        bundle.putString(Const.IntentParams.USER_ID, login.getUser().getId());

                        SendDataService.createLike(context, bundle);
                        beanList.get(position).setIVoted(true);
                        beanList.get(position).setLikeCount(beanList.get(position).getLikeCount() + 1);

                        notifyDataSetChanged();
                    }

                }
            });

            holder.lay_flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!bean.getIVoted() && !bean.getIFlagged()) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Const.IntentParams.ENTITY_TYPE_ID, bean.getId());
                        bundle.putString(Const.IntentParams.STATUS, "0");
                        bundle.putString(Const.IntentParams.USER_ID, login.getUser().getId());

                        SendDataService.createLike(context, bundle);
                        beanList.get(position).setIFlagged(true);

                        notifyDataSetChanged();
                    }


                }
            });

            if (isMyBlog) {

                holder.lay_options.setVisibility(View.GONE);

            } else {
                holder.tv_post_status.setVisibility(View.GONE);
                holder.lay_options.setVisibility(View.VISIBLE);
            }


        } else {

            holder.lay_loading.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);


        }
    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_vote_count, tv_topic, tv_posted_date, tv_posted_by, tv_tags, tv_description;
        ImageView iv_like, iv_reply, iv_flag;
        LinearLayout lay_loading;
        CardView cardView;

        LinearLayout lay_flag, lay_like;

        TextView tv_edit_post;
        LinearLayout lay_options;
        TextView tv_post_status;


        public MyViewHolder(View itemView) {
            super(itemView);


            tv_vote_count = (TextView) itemView.findViewById(R.id.tv_vote_count);
            tv_topic = (TextView) itemView.findViewById(R.id.tv_topic);
            tv_posted_date = (TextView) itemView.findViewById(R.id.tv_posted_date);
            tv_posted_by = (TextView) itemView.findViewById(R.id.tv_posted_by);
            tv_tags = (TextView) itemView.findViewById(R.id.tv_tags);

            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
            iv_reply = (ImageView) itemView.findViewById(R.id.iv_reply);
            iv_flag = (ImageView) itemView.findViewById(R.id.iv_flag);

            lay_loading = (LinearLayout) itemView.findViewById(R.id.lay_loading);
            cardView = (CardView) itemView.findViewById(R.id.cardview);

            tv_description = (TextView) itemView.findViewById(R.id.tv_description);

            lay_flag = (LinearLayout) itemView.findViewById(R.id.lay_flag);
            lay_like = (LinearLayout) itemView.findViewById(R.id.lay_like);

            tv_edit_post = (TextView) itemView.findViewById(R.id.tv_edit_post);
            lay_options = (LinearLayout) itemView.findViewById(R.id.lay_options);

            tv_post_status = (TextView) itemView.findViewById(R.id.tv_post_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    recyclerViewItemClickListener.onItemClick(getAdapterPosition());

                }
            });

        }


    }

    public void addLoading() {

        beanList.add(new BlogData());
        notifyDataSetChanged();
    }


    public void removeLoading() {

        beanList.remove(beanList.size() - 1);
    }

}

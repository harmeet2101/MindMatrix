package Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import Model.DiscussionData;
import Model.Login;
import Utils.TimeUtils;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<DiscussionData> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;
    Login login;

    public DiscussionAdapter(Context con, ArrayList<DiscussionData> beanList,
                             RecyclerViewItemClickListener recyclerViewItemClickListener, Login login) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
        this.login = login;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_discussion, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        final DiscussionData bean = beanList.get(position);

        if (bean.getTitle() != null) {

            holder.lay_loading.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);

            holder.tv_topic.setText(context.getString(R.string.discussion_topic, bean.getTitle()));
            if (bean.getUserId() != null && bean.getUserId().size() > 0)
                holder.tv_posted_by.setText(context.getString(R.string.discussion_posted_by, bean.getUserId().get(0).getFirstname() +
                        " " + bean.getUserId().get(0).getLastname()));
            else
                holder.tv_posted_by.setText("");

            holder.tv_posted_date.setText(context.getString(R.string.discussion_posted_date, TimeUtils.getFormattedTime(bean.getCreatedAt(),
                    TimeUtils.QUIZ_TIME_FORMAT)));
            holder.tv_tags.setText(context.getString(R.string.discussion_tags) + " " + bean.getTags().trim());

            holder.tv_vote_count.setText("" + bean.getLikeCount());

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

            holder.iv_like.setOnClickListener(new View.OnClickListener() {
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

            holder.iv_flag.setOnClickListener(new View.OnClickListener() {
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


        } else {

            holder.lay_loading.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);


        }
    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public ArrayList<DiscussionData> getList() {
        return beanList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_vote_count, tv_topic, tv_posted_date, tv_posted_by, tv_tags;
        ImageView iv_like, iv_reply, iv_flag;
        LinearLayout lay_loading;
        CardView cardView;


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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    recyclerViewItemClickListener.onItemClick(getAdapterPosition());

                }
            });

        }


    }

    public void addLoading() {

        beanList.add(new DiscussionData());
        notifyDataSetChanged();
    }


    public void removeLoading() {

        beanList.remove(beanList.size() - 1);
    }

}

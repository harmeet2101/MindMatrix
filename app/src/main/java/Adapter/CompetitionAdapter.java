package Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cli.knowledgebase.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import AppFragment.WebViewFragment;
import Constants.Const;
import Interfaces.RecyclerViewItemClickListener;
import Model.CompetitionData;
import Utils.TimeUtils;
import Utils.ViewUtils;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<CompetitionData> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public CompetitionAdapter(Activity con, ArrayList<CompetitionData> beanList,
                              RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_competition, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        CompetitionData bean = beanList.get(position);

        holder.tv_tittle.setText(bean.getTitle());


        holder.tv_type.setText(ViewUtils.getSpanableText(context, context.getString(R.string.competition_type, bean.getType()),
                0, 6, new ForegroundColorSpan(Color.BLACK)));


        holder.tv_description.setText(bean.getComment());

        if (position == 0) {

            holder.tv_due_date.setVisibility(View.VISIBLE);
            holder.tv_date_announced.setVisibility(View.VISIBLE);
            holder.tv_for_class.setVisibility(View.VISIBLE);
            holder.lay_start_competition.setVisibility(View.VISIBLE);

            holder.tv_due_date.setText(context.getString(R.string.due_date,
                    TimeUtils.getFormattedTime(bean.getDueDate(), TimeUtils.BLOG_TIME_FORMAT)));

            holder.tv_date_announced.setText(ViewUtils.getSpanableText(context, context.getString(R.string.date_anounced,
                    TimeUtils.getFormattedTime(bean.getCreatedAt(), TimeUtils.BLOG_TIME_FORMAT)),
                    0, 15, new ForegroundColorSpan(Color.BLACK)));

            holder.tv_for_class.setText(ViewUtils.getSpanableText(context, context.getString(R.string.for_class, bean.getYear()),
                    0, 7, new ForegroundColorSpan(Color.BLACK)));

            if (!bean.getFilepath().equals("0"))
                holder.tv_view_details.setVisibility(View.VISIBLE);
            else
                holder.tv_view_details.setVisibility(View.GONE);

            if (TimeUtils.getTimeDiff(bean.getDueDate(),
                    TimeUtils.addHoursToDate(TimeUtils.getGmtTime(), -24)) < 0) {

                holder.tv_due_date.setText(context.getString(R.string.expired));
                holder.lay_start_competition.setVisibility(View.GONE);

            }


        } else {

            holder.tv_due_date.setVisibility(View.GONE);
            holder.tv_for_class.setVisibility(View.GONE);
            holder.tv_date_announced.setVisibility(View.GONE);
            holder.lay_start_competition.setVisibility(View.GONE);

        }


    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_tittle, tv_type, tv_description, tv_due_date,
                tv_date_announced, tv_for_class, tv_start;

        LinearLayout lay_start_competition;
        TextView tv_view_details;

        public MyViewHolder(View itemView) {
            super(itemView);


            tv_tittle = (TextView) itemView.findViewById(R.id.tv_tittle);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_date_announced = (TextView) itemView.findViewById(R.id.tv_date_announced);
            tv_for_class = (TextView) itemView.findViewById(R.id.tv_for_class);

            tv_due_date = (TextView) itemView.findViewById(R.id.tv_due_date);
            lay_start_competition = (LinearLayout) itemView.findViewById(R.id.lay_start_competition);

            tv_start = (TextView) itemView.findViewById(R.id.tv_start);

            tv_view_details = (TextView) itemView.findViewById(R.id.tv_view_details);
            tv_view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!beanList.get(getAdapterPosition()).getFilepath().equals("0")) {


                        WebViewFragment newFragment = new WebViewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Const.IntentParams.URL, beanList.get(getAdapterPosition()).getFilepath());
                        newFragment.setArguments(bundle);
                        newFragment.show(context.getFragmentManager(), "dialog");


                    }
                    //ViewUtils.openWebActivity(context, beanList.get(getAdapterPosition()).getFilepath());

                }
            });


            tv_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.onItemClick(getAdapterPosition());
                }
            });


        }


    }


}

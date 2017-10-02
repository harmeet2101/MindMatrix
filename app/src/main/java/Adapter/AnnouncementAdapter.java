package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cli.knowledgebase.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Interfaces.RecyclerViewItemClickListener;
import Model.Notes;
import Utils.TimeUtils;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Notes> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public AnnouncementAdapter(Context con, ArrayList<Notes> beanList) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_faculty_announcement, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        Notes bean = beanList.get(position);
        if (bean.getUserId() != null && bean.getUserId().size() > 0) {

            holder.tv_faculty_name.setText(bean.getUserId().get(0).getFirstname() + " " + bean.getUserId().get(0).getLastname());
        }

        holder.tv_announcement_time.setText(TimeUtils.getFormattedTime(bean.getCreatedAt(), TimeUtils.QUIZ_TIME_FORMAT));

        if (!bean.getAnnouncementMsg().equals("")) {
            holder.tv_announcement.setVisibility(View.VISIBLE);
            holder.tv_announcement.setText(bean.getAnnouncementMsg());
        } else {

            holder.tv_announcement.setVisibility(View.GONE);
        }

        if (bean.getFileId() != null && bean.getFileId().size() > 0) {

            holder.lay_attachment.setVisibility(View.VISIBLE);
            holder.tv_attachment.setText(bean.getFileId().get(0).getOriginalFileName());

        } else {
            holder.lay_attachment.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_faculty_name, tv_announcement_time, tv_announcement, tv_attachment;
        LinearLayout lay_attachment;

        public MyViewHolder(View itemView) {
            super(itemView);


            tv_faculty_name = (TextView) itemView.findViewById(R.id.tv_faculty_name);
            tv_announcement_time = (TextView) itemView.findViewById(R.id.tv_announcement_time);
            tv_announcement = (TextView) itemView.findViewById(R.id.tv_announcement);
            tv_attachment = (TextView) itemView.findViewById(R.id.tv_attachment);

            lay_attachment = (LinearLayout) itemView.findViewById(R.id.lay_attachment);

        }


    }


}

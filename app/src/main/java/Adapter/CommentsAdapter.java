package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cli.knowledgebase.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Model.CommentsData;
import Utils.TimeUtils;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<CommentsData> beanList;
    public static ImageLoader imageLoader;

    public CommentsAdapter(Context con, ArrayList<CommentsData> beanList) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        CommentsData bean = beanList.get(position);

        holder.tv_comment.setText(bean.getContent());

        if (bean.getUserId() != null && bean.getUserId().size() > 0)
            holder.tv_comment_heading.setText(bean.getUserId().get(0).getFirstname() + " " +
                    bean.getUserId().get(0).getLastname() + " , "
                    + TimeUtils.getFormattedTime(bean.getCreatedAt(), TimeUtils.QUIZ_TIME_FORMAT));
        else
            holder.tv_comment_heading.setText("");


    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tv_comment_heading, tv_comment;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_comment_heading = (TextView) itemView.findViewById(R.id.tv_comment_heading);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_comment.setTextIsSelectable(true);

        }


    }


}

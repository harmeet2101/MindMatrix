package Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cli.knowledgebase.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import DataBase.DataBaseHelper;
import Filter.AssignmentSearchFilter;
import Interfaces.RecyclerViewItemClickListener;
import Model.AssignmentData;
import Model.Login;
import Utils.PreferenceHelper;
import Utils.TimeUtils;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class AssignmentDataAdapter extends RecyclerView.Adapter<AssignmentDataAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<AssignmentData> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;
    AssignmentSearchFilter filter;
    Login loginDetails;
    DataBaseHelper dataBaseHelper;

    public AssignmentDataAdapter(Context con, ArrayList<AssignmentData> beanList,
                                 RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;

        filter = new AssignmentSearchFilter(beanList, this);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(new PreferenceHelper(con).getUser_detials(), Login.class);

        dataBaseHelper = new DataBaseHelper(con);

        //  filterList("");

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_assignment_list, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        AssignmentData bean = beanList.get(position);

        if (!bean.getType().equals("assignment")) {

            holder.cardview.setVisibility(View.GONE);
            holder.lay_master_layout.setVisibility(View.GONE);

        } else {

            holder.cardview.setVisibility(View.VISIBLE);
            holder.lay_master_layout.setVisibility(View.VISIBLE);

            holder.tv_assignment_title.setText(bean.getAssignmentName());
            holder.tv_assignment_duration.setText(context.getString(R.string.total_marks, bean.getTotalMarks()));

            holder.tv_assignment_created.setText(context.getString(R.string.created, TimeUtils.getFormattedTime(bean.getCreatedAt(), TimeUtils.QUIZ_TIME_FORMAT)));
            holder.tv_assignment_expries.setText(context.getString(R.string.assignment_expires, TimeUtils.getFormattedTime(bean.getCompletionDate(), TimeUtils.QUIZ_TIME_FORMAT)));

            String score = dataBaseHelper.getAssignmentScore(bean.getId());

            if (score != null && !score.equals("")) {

                holder.tv_start_assignment.setVisibility(View.GONE);
                holder.tv_assignment_status.setVisibility(View.VISIBLE);
                if (score.equals("-1"))
                    holder.tv_assignment_status.setText(context.getString(R.string.submitted));
                else
                    holder.tv_assignment_status.setText(context.getString(R.string.marks, score));

                holder.tv_assignment_status.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_shape_score));
            } else if (TimeUtils.getTimeDiff(bean.getCompletionDate(), TimeUtils.addHoursToDate(TimeUtils.getGmtTime(), -24)) < 0) {

                holder.tv_start_assignment.setVisibility(View.GONE);
                holder.tv_assignment_status.setVisibility(View.VISIBLE);
                holder.tv_assignment_status.setText(context.getString(R.string.expired));
                holder.tv_assignment_status.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_shape_expried));


            } else {
                holder.tv_start_assignment.setVisibility(View.VISIBLE);
                holder.tv_assignment_status.setVisibility(View.GONE);
            }

        }

    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public void setList(ArrayList<AssignmentData> list) {
        this.beanList = list;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_assignment_title, tv_assignment_duration, tv_assignment_expries, tv_assignment_created, tv_start_assignment, tv_assignment_status;
        View itemView;
        LinearLayout lay_master_layout;
        CardView cardview;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            lay_master_layout = (LinearLayout) itemView.findViewById(R.id.lay_master_layout);
            tv_assignment_title = (TextView) itemView.findViewById(R.id.tv_assignment_title);
            tv_assignment_duration = (TextView) itemView.findViewById(R.id.tv_assignment_duration);
            tv_assignment_expries = (TextView) itemView.findViewById(R.id.tv_assignment_expries);
            tv_assignment_created = (TextView) itemView.findViewById(R.id.tv_assignment_created);
            tv_start_assignment = (TextView) itemView.findViewById(R.id.tv_start_assignment);
            tv_assignment_status = (TextView) itemView.findViewById(R.id.tv_assignment_status);

            tv_start_assignment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.onItemClick(getAdapterPosition());
                }
            });


            cardview = (CardView) itemView.findViewById(R.id.cardview);


        }


    }

    public void filterList(String text) {

        filter.filter(text);

    }

    public ArrayList<AssignmentData> getList() {

        if (filter.getlist() != null && filter.getlist().size() > 0)
            return filter.getlist();
        else
            return beanList;
    }


}

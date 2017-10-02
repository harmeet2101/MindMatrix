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
import Filter.QuizSearchFilter;
import Interfaces.RecyclerViewItemClickListener;
import Model.Login;
import Model.QuizData;
import Utils.PreferenceHelper;
import Utils.TimeUtils;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class QuizDataAdapter extends RecyclerView.Adapter<QuizDataAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<QuizData> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;
    QuizSearchFilter filter;
    Login loginDetails;

    DataBaseHelper dataBaseHelper;

    public QuizDataAdapter(Context con, ArrayList<QuizData> beanList,
                           RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;

        filter = new QuizSearchFilter(beanList, this);

        Gson gson = new Gson();
        loginDetails = gson.fromJson(new PreferenceHelper(con).getUser_detials(), Login.class);
        dataBaseHelper = new DataBaseHelper(con);

        //  filterList("");

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_quiz_list, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        QuizData bean = beanList.get(position);

        if (bean.getType().equals("practiceQuiz")) {

            holder.cardview.setVisibility(View.GONE);
            holder.lay_master_layout.setVisibility(View.GONE);

        } else {

            holder.cardview.setVisibility(View.VISIBLE);
            holder.lay_master_layout.setVisibility(View.VISIBLE);

            holder.tv_quiz_title.setText(bean.getName());
            holder.tv_quiz_duration.setText(context.getString(R.string.duration, bean.getDuration()));

            holder.tv_quiz_created.setText(context.getString(R.string.created, TimeUtils.getFormattedTime(bean.getCreatedAt(), TimeUtils.QUIZ_TIME_FORMAT)));
            holder.tv_quiz_expries.setText(context.getString(R.string.quiz_expires, TimeUtils.getFormattedTime(bean.getDueDate(), TimeUtils.QUIZ_TIME_FORMAT)));

            String score = dataBaseHelper.getQuizScore(bean.getId());

            if (!score.equals("")) {

                holder.tv_start_quiz.setVisibility(View.GONE);
                holder.tv_quiz_status.setVisibility(View.VISIBLE);
                holder.tv_quiz_status.setText(context.getString(R.string.score, score, bean.getTotalMarks()));
                holder.tv_quiz_status.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_shape_score));
            } else if (TimeUtils.getTimeDiff(bean.getDueDate(), TimeUtils.addHoursToDate(TimeUtils.getGmtTime(), -24)) < 0) {

                holder.tv_start_quiz.setVisibility(View.GONE);
                holder.tv_quiz_status.setVisibility(View.VISIBLE);
                holder.tv_quiz_status.setText(context.getString(R.string.expired));
                holder.tv_quiz_status.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_shape_expried));


            } else {
                holder.tv_start_quiz.setVisibility(View.VISIBLE);
                holder.tv_quiz_status.setVisibility(View.GONE);
            }

        }

    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public void setList(ArrayList<QuizData> list) {
        this.beanList = list;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_quiz_title, tv_quiz_duration, tv_quiz_expries, tv_quiz_created, tv_start_quiz, tv_quiz_status;
        View itemView;
        LinearLayout lay_master_layout;
        CardView cardview;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            lay_master_layout = (LinearLayout) itemView.findViewById(R.id.lay_master_layout);
            tv_quiz_title = (TextView) itemView.findViewById(R.id.tv_quiz_title);
            tv_quiz_duration = (TextView) itemView.findViewById(R.id.tv_quiz_duration);
            tv_quiz_expries = (TextView) itemView.findViewById(R.id.tv_quiz_expries);
            tv_quiz_created = (TextView) itemView.findViewById(R.id.tv_quiz_created);
            tv_start_quiz = (TextView) itemView.findViewById(R.id.tv_start_quiz);
            tv_quiz_status = (TextView) itemView.findViewById(R.id.tv_quiz_status);

            tv_start_quiz.setOnClickListener(new View.OnClickListener() {
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

    public ArrayList<QuizData> getList() {
        return filter.getlist();
    }


}

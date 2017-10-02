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

import Filter.TopicsSearchFilter;
import Interfaces.RecyclerViewItemClickListener;
import Model.ScormData;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class TopicsListAdapter extends RecyclerView.Adapter<TopicsListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ScormData> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    TopicsSearchFilter filter;

    public TopicsListAdapter(Context con, ArrayList<ScormData> beanList,
                             RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
        filter = new TopicsSearchFilter(beanList, this);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_topics_list, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        ScormData bean = beanList.get(position);
        holder.tv_topics.setText(bean.getLessonTitle());


    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public void setList(ArrayList<ScormData> list) {
        this.beanList = list;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_topics;

        public MyViewHolder(View itemView) {
            super(itemView);


            tv_topics = (TextView) itemView.findViewById(R.id.tv_topics);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.onItemClick(getAdapterPosition());
                }
            });


        }


    }

    public void filterList(String text) {

        filter.filter(text);

    }

    public ArrayList<ScormData> getList() {

        if (filter.getlist() != null && filter.getlist().size() > 0)
            return filter.getlist();
        else
            return beanList;
    }



}

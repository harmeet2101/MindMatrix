package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cli.knowledgebase.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Interfaces.RecyclerViewItemClickListener;
import Model.Specialization;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class SpecializationAdapter extends RecyclerView.Adapter<SpecializationAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Specialization> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public SpecializationAdapter(Context con, ArrayList<Specialization> beanList,
                                 RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_specialization, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        Specialization bean = beanList.get(position);

        holder.tv_specialization_name.setText(bean.getName());
        holder.iv_icon.setImageResource(Integer.parseInt(bean.getIcon()));


    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_specialization_name;
        ImageView iv_icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_specialization_name = (TextView) itemView.findViewById(R.id.tv_specialization_name);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.onItemClick(getAdapterPosition());
                }
            });


        }


    }


}

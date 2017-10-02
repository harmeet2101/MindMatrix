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

import java.io.File;

import Interfaces.RecyclerViewItemClickListener;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class AllFilesAdapter extends RecyclerView.Adapter<AllFilesAdapter.MyViewHolder> {
    private Context context;
    private File[] beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public AllFilesAdapter(Context con, File[] beanList,
                           RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_all_files, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        File file = beanList[position];


        holder.tv_file_name.setText(file.getName());

        if (file.isDirectory()) {

            holder.iv_icon.setImageResource(R.mipmap.ic_folder);

        } else {

            if (file.getName().toLowerCase().endsWith(".pdf")) {

                holder.iv_icon.setImageResource(R.mipmap.ic_pdf);
            } else if (file.getName().toLowerCase().endsWith(".doc") || file.getName().toLowerCase().endsWith(".docx")) {

                holder.iv_icon.setImageResource(R.mipmap.ic_doc);

            } else if (file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpeg")
                    || file.getName().toLowerCase().endsWith(".jpg")) {

                holder.iv_icon.setImageResource(R.mipmap.ic_picture);

            } else {

                holder.iv_icon.setImageResource(R.mipmap.ic_file);

            }

        }

    }


    @Override
    public int getItemCount() {
        return beanList.length;
    }

    public void setList(File[] list) {

        beanList = list;

    }

    public File[] getList() {

        return beanList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_file_name;
        ImageView iv_icon;
        View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;


            tv_file_name = (TextView) itemView.findViewById(R.id.tv_file_name);
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

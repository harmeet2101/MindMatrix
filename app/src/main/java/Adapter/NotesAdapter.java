package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cli.knowledgebase.AppService.DownloadService;
import com.cli.knowledgebase.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Constants.Const;
import Interfaces.RecyclerViewItemClickListener;
import Model.FileDetails;
import Utils.AndyUtils;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<FileDetails> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public NotesAdapter(Context con, ArrayList<FileDetails> beanList,
                        RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_notes, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        FileDetails bean = beanList.get(position);

        holder.tv_file_name.setText(bean.getOriginalFileName());

        if (bean.getOriginalFileName().toLowerCase().endsWith(".pdf")) {

            holder.iv_icon.setImageResource(R.mipmap.ic_pdf);
        } else if (bean.getOriginalFileName().toLowerCase().endsWith(".doc") ||
                bean.getOriginalFileName().toLowerCase().endsWith(".docx")) {

            holder.iv_icon.setImageResource(R.mipmap.ic_doc);

        } else if (bean.getOriginalFileName().toLowerCase().endsWith(".png") ||
                bean.getOriginalFileName().toLowerCase().endsWith(".jpeg")
                || bean.getOriginalFileName().toLowerCase().endsWith(".jpg")) {

            holder.iv_icon.setImageResource(R.mipmap.ic_picture);

        } else {

            holder.iv_icon.setImageResource(R.mipmap.ic_file);

        }

    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_file_name;
        ImageView iv_icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_file_name = (TextView) itemView.findViewById(R.id.tv_file_name);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // recyclerViewItemClickListener.onItemClick(getAdapterPosition());


                    AndyUtils.showLoading(context,false,context.getString(R.string.downloading));

                    Intent intent = new Intent(context, DownloadService.class);
                    intent.putExtra(Const.IntentParams.FILE_DETAILS_DATA,beanList.get(getAdapterPosition()));
                    context.startService(intent);


                }
            });


        }


    }


}

package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cli.knowledgebase.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Constants.Const;
import Interfaces.RecyclerViewItemClickListener;
import Model.DrawerMenuItem;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<DrawerMenuItem> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public DrawerAdapter(Context con, ArrayList<DrawerMenuItem> beanList,
                         RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_menu_drawer, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        DrawerMenuItem bean = beanList.get(position);

        switch (bean.getMenuType()) {

            case Const.MenuType.PROFILE:

                holder.layout_profile.setVisibility(View.VISIBLE);
                holder.layout_menu.setVisibility(View.GONE);

                if (!bean.getMenuIcon().equals(""))
                    imageLoader.displayImage(bean.getMenuIcon(), holder.iv_profile_pic);

                holder.tv_profile_name.setText(bean.getMenuName());

                break;

            case Const.MenuType.LIST:

                holder.layout_profile.setVisibility(View.GONE);
                holder.layout_menu.setVisibility(View.VISIBLE);

                holder.iv_menu_icon.setImageResource(Integer.parseInt(bean.getMenuIcon()));
                holder.tv_menu_name.setText(bean.getMenuName());

                break;

        }

    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_profile, layout_menu;
        ImageView iv_profile_pic, iv_menu_icon;
        TextView tv_profile_name, tv_menu_name;

        public MyViewHolder(View itemView) {
            super(itemView);

            layout_profile = (LinearLayout) itemView.findViewById(R.id.layout_profile);
            layout_menu = (LinearLayout) itemView.findViewById(R.id.layout_menu);

            iv_profile_pic = (ImageView) itemView.findViewById(R.id.iv_profile_pic);
            iv_menu_icon = (ImageView) itemView.findViewById(R.id.iv_menu_icon);

            tv_profile_name = (TextView) itemView.findViewById(R.id.tv_profile_name);
            tv_menu_name = (TextView) itemView.findViewById(R.id.tv_menu_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.onItemClick(beanList.get(getAdapterPosition()).getId());
                }
            });


        }


    }


}

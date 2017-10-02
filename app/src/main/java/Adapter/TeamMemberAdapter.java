package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cli.knowledgebase.CompetitionDetailsActivity;
import com.cli.knowledgebase.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Interfaces.RecyclerViewItemClickListener;
import Model.Login;
import Model.TeamMember;
import Model.UserDetails;
import Utils.PreferenceHelper;
import Utils.TimeUtils;
import Utils.ViewUtils;
import app.AppController;

/**
 * Created by Getit on 13-04-2016.
 */
public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TeamMember> beanList;
    public static ImageLoader imageLoader;
    RecyclerViewItemClickListener recyclerViewItemClickListener;
    Login loginDetails;


    public TeamMemberAdapter(Context con, ArrayList<TeamMember> beanList,
                             RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = con;
        this.beanList = beanList;
        this.imageLoader = AppController.getImageLoader();
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;

        loginDetails = new Gson().fromJson(new PreferenceHelper(con).getUser_detials(), Login.class);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_teammates, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        TeamMember bean = beanList.get(position);

        holder.iv_teammates.setImageResource(R.mipmap.ic_placeholderimage);
        if (bean.getTeamMemberUserId() != null && bean.getTeamMemberUserId().size() > 0) {

            UserDetails details = bean.getTeamMemberUserId().get(0);

            holder.tv_name.setText(details.getFirstname() + " " + details.getLastname());

            if (!details.getProfileUrl().equals(""))
                imageLoader.displayImage(details.getProfileUrl(), holder.iv_teammates);

            holder.tv_invited_date.setText(ViewUtils.getSpanableText(context,
                    context.getString(R.string.invited_on, TimeUtils.getFormattedTime(bean.getCreatedAt(),
                            TimeUtils.BLOG_TIME_FORMAT)), 0, 12, new ForegroundColorSpan(Color.BLACK)));

            if (bean.getStatus().equals("0")) {

                holder.tv_status.setText(
                        ViewUtils.getSpanableText(context,
                                context.getString(R.string.status, context.getString(R.string.waiting)),
                                0, 12, new ForegroundColorSpan(Color.BLACK)));

                if (!bean.getTeamLeadId().get(0).getId().equals(loginDetails.getUser().getId())
                        && bean.getTeamMemberUserId().get(0).getId().equals(loginDetails.getUser().getId())) {
                    holder.lay_action.setVisibility(View.VISIBLE);
                    holder.tv_status.setVisibility(View.GONE);
                } else {
                    holder.lay_action.setVisibility(View.GONE);
                    holder.tv_status.setVisibility(View.VISIBLE);
                }


            } else if (bean.getStatus().equals("1")) {

                holder.tv_status.setText(
                        ViewUtils.getSpanableText(context,
                                context.getString(R.string.status, context.getString(R.string.accepted)),
                                0, 12, new ForegroundColorSpan(Color.BLACK)));

                holder.lay_action.setVisibility(View.GONE);
                holder.tv_status.setVisibility(View.VISIBLE);


            } else if (bean.getStatus().equals("2")) {

                holder.tv_status.setText(
                        ViewUtils.getSpanableText(context,
                                context.getString(R.string.status, context.getString(R.string.rejected)),
                                0, 12, new ForegroundColorSpan(Color.BLACK)));

                holder.lay_action.setVisibility(View.GONE);
                holder.tv_status.setVisibility(View.VISIBLE);


            }


        }


    }


    @Override
    public int getItemCount() {
        return beanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_teammates;
        TextView tv_status, tv_invited_date, tv_name, tv_accept, tv_reject;
        LinearLayout lay_action;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);

            iv_teammates = (ImageView) itemView.findViewById(R.id.iv_teammates);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);


            tv_accept = (TextView) itemView.findViewById(R.id.tv_accept);
            tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((CompetitionDetailsActivity) context).acceptRequest(getAdapterPosition());
                    beanList.get(getAdapterPosition()).setStatus("1");
                    notifyDataSetChanged();

                }
            });

            tv_reject = (TextView) itemView.findViewById(R.id.tv_reject);
            tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((CompetitionDetailsActivity) context).rejectRequest(getAdapterPosition());
                    beanList.get(getAdapterPosition()).setStatus("2");
                    notifyDataSetChanged();

                }
            });

            tv_invited_date = (TextView) itemView.findViewById(R.id.tv_invited_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.onItemClick(getAdapterPosition());
                }
            });

            lay_action = (LinearLayout) itemView.findViewById(R.id.lay_action);


        }


    }


}

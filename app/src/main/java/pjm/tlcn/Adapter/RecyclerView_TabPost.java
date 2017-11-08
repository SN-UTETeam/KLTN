package pjm.tlcn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Activity.ViewCmt_tabProfile;
import pjm.tlcn.Model.Image;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user;

/**
 * Created by Pjm on 11/8/2017.
 */

public class RecyclerView_TabPost extends RecyclerView.Adapter<RecyclerView_TabPost.RecyclerViewHolder>{

    private ArrayList<Image> item = new ArrayList<Image>();
    private Context context;
    public static String img_id;

    public RecyclerView_TabPost(ArrayList<Image> item) {
        this.item = item;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context=parent.getContext();
        View view = inflater.inflate(R.layout.item_post_tab_profile, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.tv_username_tabpost.setText(user.getUsername());
        Picasso.with(context).load(user.getAvatarurl()).fit().centerInside().into(holder.img_avatar_tabpost);
        Picasso.with(context).load(item.get(position).getImgurl()).fit().centerCrop().into(holder.img_image_tabpost);
        holder.tv_likes_tabpost.setText(item.get(position).getLikes()+" like");
        if(item.get(position).getStatus().length()>0) holder.tv_status_tabpost.setText(item.get(position).getStatus());
        holder.tv_comments_tabpost.setText("View " +item.get(position).getComment()+" comments");
        holder.tv_time_tabpost.setText(item.get(position).getDatetime());

        holder.tv_comments_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_id = item.get(position).getId();
                Intent intent = new Intent(context,ViewCmt_tabProfile.class);
                context.startActivity(intent);
            }
        });
        holder.img_cmt_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_id = item.get(position).getId();
                Intent intent = new Intent(context,ViewCmt_tabProfile.class);
                intent.putExtra("imgurl",item.get(position).getImgurl()+"");
                intent.putExtra("status",item.get(position).getStatus()+"");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tv_username_tabpost,tv_likes_tabpost,tv_status_tabpost,tv_comments_tabpost,tv_time_tabpost;
        ImageView img_avatar_tabpost,img_image_tabpost,img_cmt_tabpost;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            img_cmt_tabpost = (ImageView) itemView.findViewById(R.id.img_cmt_tabpost);
            tv_username_tabpost = (TextView) itemView.findViewById(R.id.tv_username_tabpost);
            tv_likes_tabpost = (TextView) itemView.findViewById(R.id.tv_likes_tabpost);
            tv_status_tabpost = (TextView) itemView.findViewById(R.id.tv_status_tabpost);
            tv_comments_tabpost = (TextView) itemView.findViewById(R.id.tv_comments_tabpost);
            img_avatar_tabpost = (ImageView) itemView.findViewById(R.id.img_avatar_tabpost);
            img_image_tabpost = (ImageView) itemView.findViewById(R.id.img_image_tabpost);
            tv_time_tabpost = (TextView) itemView.findViewById(R.id.tv_time_tabpost);
        }
    }
}
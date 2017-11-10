package pjm.tlcn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import pjm.tlcn.Activity.ViewCmt_tabProfile;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user;

/**
 * Created by Pjm on 11/8/2017.
 */

public class RecyclerView_TabPost extends RecyclerView.Adapter<RecyclerView_TabPost.RecyclerViewHolder>{
    private ArrayList<Photo> item = new ArrayList<Photo>();
    Bundle bundle;
    private Context context;
    public static String img_id;
    private StringBuilder mUsers;
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    public RecyclerView_TabPost(ArrayList<Photo> item) {
        this.item = item;
    }
    Boolean[] flag_like;
    private Boolean mLikedByCurrentUser;
    private String mLikesString = "";

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context=parent.getContext();
        flag_like = new Boolean[item.size()+1];
        Log.d("size",item.size()+"");
        Arrays.fill(flag_like, Boolean.FALSE);
        View view = inflater.inflate(R.layout.item_post_tab_profile, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        databaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                holder.tv_username_tabpost.setText(user.getUsername());
                Picasso.with(context).load(user.getAvatarurl()).fit().centerInside().into(holder.img_avatar_tabpost);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Picasso.with(context).load(item.get(position).getImage_path()).fit().centerCrop().into(holder.img_image_tabpost);

        //GetLike
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("photos")
                .child(item.get(position).getPhoto_id())
                .child("likes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers = new StringBuilder();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Log.d("RecyclerView_Tabpost", "onDataChange: found like: " +
                            singleSnapshot.getValue(User.class).getUsername());

                    mUsers.append(singleSnapshot.getValue(User.class).getUsername());
                    mUsers.append(",");
                }
                String[] splitUsers = mUsers.toString().split(",");
                if(mUsers.toString().contains(user.getUsername() + ",")){//mitch, mitchell.tabian
                    mLikedByCurrentUser = true;
                }else{
                    mLikedByCurrentUser = false;
                }

                int length = splitUsers.length;
                if(length == 1){
                    mLikesString = "Người thích: " + splitUsers[0];
                }
                else if(length == 2){
                    mLikesString = "Người thích: " + splitUsers[0]
                            + " và " + splitUsers[1];
                }
                else if(length == 3){
                    mLikesString = "Người thích: " + splitUsers[0]
                            + ", " + splitUsers[1]
                            + " và " + splitUsers[2];

                }
                else if(length == 4){
                    mLikesString = "Người thích: " + splitUsers[0]
                            + ", " + splitUsers[1]
                            + ", " + splitUsers[2]
                            + " và " + splitUsers[3];
                }
                else if(length > 4){
                    mLikesString = "Người thích: " + splitUsers[0]
                            + ", " + splitUsers[1]
                            + ", " + splitUsers[2]
                            + " và " + (splitUsers.length - 3) + " người khác";
                }
                if(length>8)
                holder.tv_likes_tabpost.setText(mLikesString+"");
                else holder.tv_likes_tabpost.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(item.get(position).getCaption().length()>0) holder.tv_status_tabpost.setText(item.get(position).getCaption());
        Log.d("Comment",item.get(position).getComments().size()+"");
        if(item.get(position).getComments().size() > 0){
            holder.tv_comments_tabpost.setVisibility(View.VISIBLE);
            holder.tv_comments_tabpost.setText("Xem " + item.get(position).getComments().size() + " bình luận");
        }else{
            holder.tv_comments_tabpost.setVisibility(View.GONE);
        }
        //TimeStamp
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currenttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        try {
            Date date1 = formater.parse(item.get(position).getDate_created());
            Date date2 = formater.parse(currenttime);

            long different=date2.getTime() - date1.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long monthMilli = daysInMilli * 30;

            long elapsedMonths = different / monthMilli;
            different = different % monthMilli;
            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;
            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;
            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;
            String diffTime =" trước";
            if(elapsedMonths>1)
                diffTime= elapsedMonths +" tháng" + diffTime;
            else
                if(elapsedDays>1)
                    diffTime= elapsedDays +" ngày"+ diffTime;
                 else
                    if(elapsedHours>1)
                        diffTime= elapsedHours +" giờ"+ diffTime;
                 else
                    if(elapsedMinutes>1)
                        diffTime= elapsedMinutes +" phút"+ diffTime;
                    else diffTime="Vừa xong";

            holder.tv_time_tabpost.setText(diffTime);
        } catch (ParseException e) {
            holder.tv_time_tabpost.setText(item.get(position).getDate_created());
            e.printStackTrace();
        }

        //Like
        holder.img_like_tabpost.setImageResource(R.drawable.ufi_heart_bold);


        //View Comment
        holder.img_cmt_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewCmt_tabProfile.class);
                intent.putExtra("caption",item.get(position).getCaption());
                intent.putExtra("image_path",item.get(position).getImage_path());
                intent.putExtra("photo_id",item.get(position).getPhoto_id());
                intent.putParcelableArrayListExtra("ArrayComment", (ArrayList<? extends Parcelable>) item.get(position).getComments());
                context.startActivity(intent);
            }
        });
        holder.tv_comments_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewCmt_tabProfile.class);
                intent.putExtra("caption",item.get(position).getCaption());
                intent.putExtra("image_path",item.get(position).getImage_path());
                intent.putExtra("photo_id",item.get(position).getPhoto_id());
                intent.putParcelableArrayListExtra("ArrayComment", (ArrayList<? extends Parcelable>) item.get(position).getComments());
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
        ImageView img_avatar_tabpost,img_image_tabpost,img_cmt_tabpost,img_like_tabpost;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            img_cmt_tabpost = (ImageView) itemView.findViewById(R.id.img_cmt_tabpost);
            img_like_tabpost = (ImageView) itemView.findViewById(R.id.img_like_tabpost);
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
package pjm.tlcn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pjm.tlcn.Activity.ViewCmt_tabProfile;
import pjm.tlcn.Model.Image;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user;
import static pjm.tlcn.Activity.Login.user_id;

/**
 * Created by Pjm on 11/8/2017.
 */

public class RecyclerView_TabPost extends RecyclerView.Adapter<RecyclerView_TabPost.RecyclerViewHolder>{
    private ArrayList<Image> item = new ArrayList<Image>();
    private Context context;
    public static String img_id;
    private DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
    private DatabaseReference iDatabase = FirebaseDatabase.getInstance().getReference().child("Images").child(user_id);
    private DatabaseReference lDatabase = FirebaseDatabase.getInstance().getReference().child("Likes");
    public RecyclerView_TabPost(ArrayList<Image> item) {
        this.item = item;
    }
    Boolean flag_like = false;

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context=parent.getContext();
        View view = inflater.inflate(R.layout.item_post_tab_profile, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        uDatabase.addValueEventListener(new ValueEventListener() {
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
        Picasso.with(context).load(item.get(position).getImgurl()).fit().centerCrop().into(holder.img_image_tabpost);
        holder.tv_likes_tabpost.setText(item.get(position).getLikes()+" like");
        if(item.get(position).getStatus().length()>0) holder.tv_status_tabpost.setText(item.get(position).getStatus());
        holder.tv_comments_tabpost.setText("View " +item.get(position).getComment()+" comments");

        //TimeStamp
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currenttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        try {
            Date date1 = formater.parse(item.get(position).getDatetime());
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
            holder.tv_time_tabpost.setText(item.get(position).getDatetime());
            e.printStackTrace();
        }

        //Onclick Like
        holder.img_like_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_like=false;
                lDatabase.child(item.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            String k = postSnapshot.getKey();
                            if (user_id.equals(k)){
                                flag_like = true;
                            }
                        }
                        final DatabaseReference databaseReference = iDatabase.child(item.get(position).getId()).child("likes");
                        if(flag_like){
                            holder.img_like_tabpost.setImageResource(R.drawable.ufi_heart_bold);
                            flag_like=false;
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Integer i = dataSnapshot.getValue(Integer.class);
                                    i--;
                                    databaseReference.setValue(i);
                                    lDatabase.child(item.get(position).getId()).child(user_id).setValue(null);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            holder.img_like_tabpost.setImageResource(R.drawable.direct_heart);
                            flag_like=true;
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Integer i = dataSnapshot.getValue(Integer.class);
                                    i++;
                                    databaseReference.setValue(i);
                                    lDatabase.child(item.get(position).getId()).child(user_id).setValue(user.getUsername());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        //onclick comment
        holder.tv_comments_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_id = item.get(position).getId();
                Intent intent = new Intent(context,ViewCmt_tabProfile.class);
                intent.putExtra("imgurl",item.get(position).getImgurl()+"");
                intent.putExtra("status",item.get(position).getStatus()+"");
                intent.putExtra("ref_img",iDatabase.toString()+"/"+item.get(position).getId()+"");
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
                intent.putExtra("ref_img",iDatabase.toString()+"/"+item.get(position).getId()+"");
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
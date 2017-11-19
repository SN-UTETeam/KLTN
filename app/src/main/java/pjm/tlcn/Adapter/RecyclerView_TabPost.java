package pjm.tlcn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import pjm.tlcn.Activity.ViewLike;
import pjm.tlcn.Model.Like;
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
    public static String img_id="";
    private StringBuilder[] mUsers ;
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    public RecyclerView_TabPost(ArrayList<Photo> item) {
        this.item = item;
    }
    private Boolean[] mLikedByCurrentUser;
    private Boolean[] mSavedByCurrentUser;
    private String[] mLikesString;

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context=parent.getContext();

        mLikedByCurrentUser = new Boolean[item.size()];
        Arrays.fill(mLikedByCurrentUser, Boolean.FALSE);

        mSavedByCurrentUser = new Boolean[item.size()];
        Arrays.fill(mSavedByCurrentUser, Boolean.FALSE);

        mLikesString = new String[item.size()];
        Arrays.fill(mLikesString,"");

        mUsers = new StringBuilder[item.size()];
        for(int i=0;i<mUsers.length;i++)
            mUsers[i] = new StringBuilder();

        View view = inflater.inflate(R.layout.item_post_tab_profile, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        databaseRef.child("users").child(item.get(position).getUser_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                holder.tv_username_tabpost.setText(user.getUsername());
                holder.tv_username.setText(user.getUsername()+" ");
                Picasso.with(context).load(user.getAvatarurl()).fit().centerInside().into(holder.img_avatar_tabpost);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get key photo
        img_id =item.get(position).getPhoto_id();
        Picasso.with(context).load(item.get(position).getImage_path()).fit().centerCrop().into(holder.img_image_tabpost);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //Get Saved
        Query query1= reference.child("saved").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("size item",String.valueOf(item.size()));
                if(dataSnapshot.getValue()!=null){
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if(singleSnapshot.getValue()!=null) {
                        if (singleSnapshot.child("photo_id").getValue().toString().equals(item.get(position).getPhoto_id())) {
                            mSavedByCurrentUser[position] = true;
                            holder.img_save_tabpost.setImageResource(R.drawable.ufi_save_active);
                        }
                    }
                }
                if(!dataSnapshot.exists()){
                    mSavedByCurrentUser[position] = false;
                    holder.img_save_tabpost.setImageResource(R.drawable.ufi_save);
                }
            }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //GetLike
        Query query = reference
                .child("photos")
                .child(item.get(position).getPhoto_id())
                .child("likes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    mUsers[position] = new StringBuilder("");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference
                            .child("users")
                            .orderByChild("user_id")
                            .equalTo(singleSnapshot.getValue(Like.class).getUser_id());
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//                                                Log.d("RecyclerView_Tabpost", "onDataChange: found like: " +
//                                                        singleSnapshot.getValue(User.class).getUsername());

                                                mUsers[position].append(singleSnapshot.getValue(User.class).getUsername());
                                                mUsers[position].append(",");
                                            }
                                            String[] splitUsers = mUsers[position].toString().split(",");
                                            if(mUsers[position].toString().contains(user.getUsername() + ",")){
                                                mLikedByCurrentUser[position] = true;
                                                holder.img_like_tabpost.setImageResource(R.drawable.direct_heart);
                                            }else{
                                                mLikedByCurrentUser[position] = false;
                                            }

                                            int length = splitUsers.length;
                                            //Log.d("length split:"+position+": ",length+"");
                                            if(length == 1){
                                                mLikesString[position] = "Người thích: " +"<b>" + splitUsers[0] + "</b>";
                                            }
                                            else if(length == 2){
                                                mLikesString[position] = "Người thích: " + "<b>" + splitUsers[0] + "</b>"
                                                        + " và " + "<b>" + splitUsers[1] + "</b>";
                                            }
                                            else if(length == 3){
                                                mLikesString[position] = "Người thích: " + "<b>" + splitUsers[0] + "</b>"
                                                        + ", " + "<b>" + splitUsers[1] + "</b>"
                                                        + " và " + "<b>" + splitUsers[2] + "</b>";

                                            }
                                            else if(length == 4){
                                                mLikesString[position] = "Người thích: " + "<b>" + splitUsers[0] + "</b>"
                                                        + ", " + "<b>" + splitUsers[1] + "</b>"
                                                        + ", " + "<b>" + splitUsers[2] + "</b>"
                                                        + " và " + "<b>" + splitUsers[3] + "</b>";
                                            }
                                            else if(length > 4){
                                                mLikesString[position] = "Người thích: " + "<b>" + splitUsers[0] + "</b>"
                                                        + ", " + "<b>" + splitUsers[1] + "</b>"
                                                        + ", " + "<b>" + splitUsers[2] + "</b>"
                                                        + " và " + "<b>" + (splitUsers.length-3) + "</b>" + " người khác";
                                            }
                                            //Log.d("likes string: " +position+": ",mLikesString[position]);
                                            holder.tv_likes_tabpost.setVisibility(View.VISIBLE);
                                            holder.tv_likes_tabpost.setText(Html.fromHtml(mLikesString[position]+""));
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                }
                if(!dataSnapshot.exists()){
                    mLikesString[position] = "";
                    mLikedByCurrentUser[position] = false;
                    holder.tv_likes_tabpost.setVisibility(View.GONE);
                    holder.tv_likes_tabpost.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if(item.get(position).getCaption().length()>0) holder.tv_status_tabpost.setText(item.get(position).getCaption());
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
        holder.img_like_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click Like",position+"");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference
                        .child("photos")
                        .child(item.get(position).getPhoto_id())
                        .child("likes");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                            String keyID = singleSnapshot.getKey();
                            //case1: Then user already liked the photo
                            if(mLikedByCurrentUser[position] &&
                                    singleSnapshot.getValue(Like.class).getUser_id()
                                            .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                databaseRef.child("photos")
                                        .child(item.get(position).getPhoto_id())
                                        .child("likes")
                                        .child(keyID)
                                        .removeValue();
                                Log.d("Set Remove",1+"");
                                holder.img_like_tabpost.setImageResource(R.drawable.ufi_heart_bold);
                                //holder.tv_likes_tabpost.setText(getLikeString(position)+"");
                                mLikedByCurrentUser[position]=false;
                                break;
                               //Setlike

                                //End Setup
                            }
                            //case2: The user has not liked the photo
                            else if(!mLikedByCurrentUser[position]){
                                //add new like
                                String newLikeID = databaseRef.push().getKey();
                                //Log.d("Like","here");
                                Like like = new Like();
                                like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                databaseRef.child("photos")
                                        .child(item.get(position).getPhoto_id())
                                        .child("likes")
                                        .child(newLikeID)
                                        .setValue(like);
                                Log.d("Set Like",1+"");
                                mLikedByCurrentUser[position]=true;
                                holder.img_like_tabpost.setImageResource(R.drawable.direct_heart);
                               // holder.tv_likes_tabpost.setText(getLikeString(position)+"");
                                break;
                            }
                        }
                        if(!dataSnapshot.exists()){
                            //add new like
//                            mLikedByCurrentUser[position]=false;
//                            mLikesString[position]="";
                            String newLikeID = databaseRef.push().getKey();
                            //Log.d("Like","here");
                            Like like = new Like();
                            like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            databaseRef.child("photos")
                                    .child(item.get(position).getPhoto_id())
                                    .child("likes")
                                    .child(newLikeID)
                                    .setValue(like);
                            mLikedByCurrentUser[position]=true;
                            holder.img_like_tabpost.setImageResource(R.drawable.direct_heart);
                            //holder.tv_likes_tabpost.setText(getLikeString(position)+"");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //View Like
        holder.tv_likes_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewLike.class);
                intent.putExtra("photo_id",item.get(position).getPhoto_id());
                context.startActivity(intent);
            }
        });

        //View Comment
        holder.img_cmt_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewCmt_tabProfile.class);
                intent.putExtra("user_id",item.get(position).getUser_id());
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
                intent.putExtra("user_id",item.get(position).getUser_id());
                intent.putExtra("caption",item.get(position).getCaption());
                intent.putExtra("image_path",item.get(position).getImage_path());
                intent.putExtra("photo_id",item.get(position).getPhoto_id());
                intent.putParcelableArrayListExtra("ArrayComment", (ArrayList<? extends Parcelable>) item.get(position).getComments());
                context.startActivity(intent);
            }
        });

        holder.img_save_tabpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                //Get Saved
                Query query= reference.child("saved").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            //Log.d("Found ",singleSnapshot.child("photo_id").getValue().toString());
                            //Log.d("item",item.get(position).getPhoto_id());
                            //Log.d("Saved",mSavedByCurrentUser[position].toString());
                            if(mSavedByCurrentUser[position] && singleSnapshot.child("photo_id").getValue().toString().equals(item.get(position).getPhoto_id())){
                                Log.d("remove",singleSnapshot.getKey());
                                holder.img_save_tabpost.setImageResource(R.drawable.ufi_save);
                                databaseRef.child("saved")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(singleSnapshot.getKey())
                                        .removeValue();
                                mSavedByCurrentUser[position]=false;
                                break;

                            }
                            else
                                if (!mSavedByCurrentUser[position]){
                                    Log.d("save",item.get(position).getPhoto_id());
                                    String newkey=databaseRef.push().getKey();
                                    databaseRef.child("saved")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(newkey)
                                            .child("photo_id")
                                            .setValue(item.get(position).getPhoto_id());
                                    mSavedByCurrentUser[position]=true;
                                    holder.img_save_tabpost.setImageResource(R.drawable.ufi_save_active);
                                    break;
                                }
                        }
                        if(!dataSnapshot.exists()){
                            Log.d("save",item.get(position).getPhoto_id());
                            String newkey=databaseRef.push().getKey();
                            databaseRef.child("saved")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(newkey)
                                    .child("photo_id")
                                    .setValue(item.get(position).getPhoto_id());
                            mSavedByCurrentUser[position]=true;
                            holder.img_save_tabpost.setImageResource(R.drawable.ufi_save_active);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tv_username_tabpost,tv_likes_tabpost,tv_status_tabpost,tv_comments_tabpost,tv_time_tabpost,tv_username;
        ImageView img_avatar_tabpost,img_image_tabpost,img_cmt_tabpost,img_like_tabpost,img_save_tabpost;
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
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            img_save_tabpost = (ImageView) itemView.findViewById(R.id.img_save_tabpost);

        }
    }

    public String getLikeString(final int position2){
        //GetLike

        Query query = databaseRef
                .child("photos")
                .child(item.get(position2).getPhoto_id())
                .child("likes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               final StringBuilder mUser = new StringBuilder();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference
                            .child("users")
                            .orderByChild("user_id")
                            .equalTo(singleSnapshot.getValue(Like.class).getUser_id());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//                                                Log.d("RecyclerView_Tabpost", "onDataChange: found like: " +
//                                                        singleSnapshot.getValue(User.class).getUsername());

                                mUser.append(singleSnapshot.getValue(User.class).getUsername());
                                mUser.append(",");
                            }
                            String[] splitUsers = mUser.toString().split(",");
//                            if(mUser.toString().contains(user.getUsername() + ",")){
//                                mLikedByCurrentUser[position2] = true;
//                            }else{
//                                mLikedByCurrentUser[position2] = false;
//                            }

                            int length = splitUsers.length;
                            //Log.d("length split:"+position+": ",length+"");
                            if(length == 1){
                                mLikesString[position2] = "Người thích: " +"<b>" + splitUsers[0] + "</b>";
                            }
                            else if(length == 2){
                                mLikesString[position2] = "Người thích: " + "<b>" + splitUsers[0] + "</b>"
                                        + " và " + "<b>" + splitUsers[1] + "</b>";
                            }
                            else if(length == 3){
                                mLikesString[position2] = "Người thích: " + "<b>" + splitUsers[0] + "</b>"
                                        + ", " + "<b>" + splitUsers[1] + "</b>"
                                        + " và " + "<b>" + splitUsers[2] + "</b>";

                            }
                            else if(length == 4){
                                mLikesString[position2] = "Người thích: " + "<b>" + splitUsers[0] + "</b>"
                                        + ", " + "<b>" + splitUsers[1] + "</b>"
                                        + ", " + "<b>" + splitUsers[2] + "</b>"
                                        + " và " + "<b>" + splitUsers[3] + "</b>";
                            }
                            else if(length > 4){
                                mLikesString[position2] = "Người thích: " + "<b>" + splitUsers[0] + "</b>"
                                        + ", " + "<b>" + splitUsers[1] + "</b>"
                                        + ", " + "<b>" + splitUsers[2] + "</b>"
                                        + " và " + "<b>" + (splitUsers.length-3) + "</b>" + " người khác";
                            }
                            //Log.d("likes string: " +position+": ",mLikesString[position]);
                            //holder.tv_likes_tabpost.setVisibility(View.VISIBLE);
                            //holder.tv_likes_tabpost.setText(Html.fromHtml(mLikesString[position2]+""));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if(!dataSnapshot.exists()){
                    mLikesString[position2] = "";
                   // mLikedByCurrentUser[position2] = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mLikesString[position2]+"";
    }
}
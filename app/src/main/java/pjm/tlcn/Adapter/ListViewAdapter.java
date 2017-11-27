package pjm.tlcn.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pjm.tlcn.Activity.Activity_comment;
import pjm.tlcn.Activity.TabActivity_viewall;
import pjm.tlcn.Activity.ViewLike;
import pjm.tlcn.Model.Like;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

import static com.facebook.FacebookSdk.getApplicationContext;
import static pjm.tlcn.Activity.Login.user;
import static pjm.tlcn.Activity.Login.user_id;

/**
 * Created by thienphu on 10/26/2017.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private Activity activity;
    private List<Photo> items;
    public DatabaseReference uDatabase,keyimage;
    private StorageReference sDatabase;
    private TabActivity_viewall mAdapterCallback;
    public static String key_img="";
    private StringBuilder[] mUsers ;
    private Boolean[] mLikedByCurrentUser;
    private Boolean[] mSavedByCurrentUser;
    private String[] mLikesString;
    //


    public ListViewAdapter(Activity activity, List<Photo> items) {

        this.activity = activity;
        this.items = items;
    }



    //call activity
   //


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();

        mLikedByCurrentUser = new Boolean[items.size()];
        Arrays.fill(mLikedByCurrentUser, Boolean.FALSE);

        mSavedByCurrentUser = new Boolean[items.size()];
        Arrays.fill(mSavedByCurrentUser, Boolean.FALSE);

        mLikesString = new String[items.size()];
        Arrays.fill(mLikesString,"");

        mUsers = new StringBuilder[items.size()];
        for(int i=0;i<mUsers.length;i++)
            mUsers[i] = new StringBuilder();



        View view = inflater.inflate(R.layout.item_list, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //Firebase
        uDatabase = FirebaseDatabase.getInstance().getReference();
       sDatabase = FirebaseStorage.getInstance().getReference().child("AvatarUsers").child(user_id);
        //
      //  keyimage = FirebaseDatabase.getInstance().getReference().child("Images").child(user_id);
        //load Image
        uDatabase.child("photos").child(items.get(position).getPhoto_id()).child("image_path").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Uri> uriArrayList = new ArrayList<>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Uri uri = Uri.parse(singleSnapshot.child("path").getValue().toString());
                    uriArrayList.add(uri);
                }
                Photo_Slider image_slider = new Photo_Slider(activity,uriArrayList);
                viewHolder.viewpager_item_main.setAdapter(image_slider);
                image_slider.notifyDataSetChanged();
                viewHolder.tab_layout_item_main.setupWithViewPager(viewHolder.viewpager_item_main,true);
                if(uriArrayList.size()==1){
                    viewHolder.tab_layout_item_main.setVisibility(View.GONE);
                   viewHolder.view_main.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       /* if(items.get(position).getComments().size() > 0){
            viewHolder.tv_comments_tabpost_main.setVisibility(View.VISIBLE);
            viewHolder.tv_comments_tabpost_main.setText("Xem " + items.get(position).getComments().size() + " bình luận");
        }else{
            viewHolder.tv_comments_tabpost_main.setVisibility(View.GONE);
        }*/




        //click comment show activiity comment
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intet activity in adapter
                Intent intent = new Intent(activity, Activity_comment.class);
                v.getContext().startActivity( intent);
               // key_img =items.get(position).getId();
            }
        });



        //GetLike
        Query query = uDatabase
                .child("photos")
                .child(items.get(position).getPhoto_id())
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
                                viewHolder.image_like.setBackgroundResource(R.drawable.direct_heart);
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
                            viewHolder.quatity_likes.setVisibility(View.VISIBLE);
                            viewHolder.quatity_likes.setText(Html.fromHtml(mLikesString[position]+""));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if(!dataSnapshot.exists()){
                    mLikesString[position] = "";
                    mLikedByCurrentUser[position] = false;
                    viewHolder.quatity_likes.setVisibility(View.GONE);
                    viewHolder.quatity_likes.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //TimeStamp
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currenttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        try {
            Date date1 = formater.parse(items.get(position).getDate_created());
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

            viewHolder.realtime.setText(diffTime);
        } catch (ParseException e) {
            viewHolder.realtime.setText(items.get(position).getDate_created());
            e.printStackTrace();
        }


        if(items.get(position).getCaption() != null && !items.get(position).getCaption().isEmpty() && !items.get(position).getCaption().equals("null")){
            //get caption
            viewHolder.textView.setText(items.get(position).getCaption());
            viewHolder.username.setText(user.getUsername());
            //////


        }


        //Like
        viewHolder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("Click Like",position+"");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference
                        .child("photos")
                        .child(items.get(position).getPhoto_id())
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
                                uDatabase.child("photos")
                                        .child(items.get(position).getPhoto_id())
                                        .child("likes")
                                        .child(keyID)
                                        .removeValue();
                                Log.d("Set Remove",1+"");
                                viewHolder.image_like.setBackgroundResource(R.drawable.ufi_heart_bold);
                                //holder.tv_likes_tabpost.setText(getLikeString(position)+"");
                                mLikedByCurrentUser[position]=false;
                                break;
                                //Setlike

                                //End Setup
                            }
                            //case2: The user has not liked the photo
                            else if(!mLikedByCurrentUser[position]){
                                //add new like
                                String newLikeID = uDatabase.push().getKey();
                                //Log.d("Like","here");
                                Like like = new Like();
                                like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                               uDatabase.child("photos")
                                        .child(items.get(position).getPhoto_id())
                                        .child("likes")
                                        .child(newLikeID)
                                        .setValue(like);
                                Log.d("Set Like",1+"");
                                mLikedByCurrentUser[position]=true;
                                viewHolder.image_like.setBackgroundResource(R.drawable.direct_heart);
                                // holder.tv_likes_tabpost.setText(getLikeString(position)+"");
                                break;
                            }
                        }
                        if(!dataSnapshot.exists()){
                            //add new like
//                            mLikedByCurrentUser[position]=false;
//                            mLikesString[position]="";
                            String newLikeID = uDatabase.push().getKey();
                            //Log.d("Like","here");
                            Like like = new Like();
                            like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            uDatabase.child("photos")
                                    .child(items.get(position).getPhoto_id())
                                    .child("likes")
                                    .child(newLikeID)
                                    .setValue(like);
                            mLikedByCurrentUser[position]=true;
                            viewHolder.image_like.setBackgroundResource(R.drawable.direct_heart);
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
        viewHolder.quatity_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewLike.class);
                intent.putExtra("photo_id",items.get(position).getPhoto_id());
                activity.startActivity(intent);
            }
        });





        //Load user profile
        uDatabase.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewHolder.user_name_img.setText(user.getUsername());
                Picasso.with(getApplicationContext())
                        .load(user.getAvatarurl())
                        .fit()
                        .into(viewHolder.imname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView view_main,imname,comment;
        private ImageButton image_like;
        private TextView textView,quatity_likes,username,realtime,user_name_img,tv_comments_tabpost_main;
        private ViewPager viewpager_item_main;
        private TabLayout tab_layout_item_main;

        //biến public sang comment
      //  public ImageView ;


        public ViewHolder(View view) {
            super(view);
            tv_comments_tabpost_main = (TextView) itemView.findViewById(R.id.tv_comments_tabpost);
           view_main = (ImageView) view.findViewById(R.id.view_main);
            viewpager_item_main = (ViewPager)view.findViewById(R.id.viewpager_item_main);
            tab_layout_item_main = (TabLayout) itemView.findViewById(R.id.tab_layout_item_main);
            imname = (ImageView) view.findViewById(R.id.idname);
            comment = (ImageView)view.findViewById(R.id.comment);
            textView = (TextView)view.findViewById(R.id.text);
            quatity_likes =(TextView)view.findViewById(R.id.quatity_like);
            username =(TextView)view.findViewById(R.id.user_name);
            realtime =(TextView)view.findViewById(R.id.date_time);
            user_name_img = (TextView)view.findViewById(R.id.name_user);
            image_like =(ImageButton) view.findViewById(R.id.image_like);
        }

    }

}

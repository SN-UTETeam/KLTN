package pjm.tlcn.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

import pjm.tlcn.Fragment.View_ProfilePost;
import pjm.tlcn.Model.Follow;
import pjm.tlcn.Model.Notification;
import pjm.tlcn.Model.User;
import pjm.tlcn.Model.UserFollow;
import pjm.tlcn.R;

public class Activity_viewprofile extends AppCompatActivity {

    public DatabaseReference uDatabase;
    private TextView tv_ViewUserName,tv_baiviet,tv_following,tv_follower;
    private Button bt_follow_user;
    private Button Img_nhantin;
    private ImageView img_view_avatar_user;
    private ImageButton image_back;
    private ViewPager viewPager;
    private Toolbar toolbar_viewprofile;
    private Boolean mFollowdByCurrentUser = false;
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;
    private UserFollow userFollow = null;
    private Follow follow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);
        uDatabase = FirebaseDatabase.getInstance().getReference();

        //Set back Toolbar
        toolbar_viewprofile = (Toolbar) findViewById(R.id.toolbar_view_profile);
        setSupportActionBar(toolbar_viewprofile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_viewprofile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       // toolbar_viewprofile.setTitle();
        tv_ViewUserName = (TextView) findViewById(R.id.tv_username_viewprofile);
        img_view_avatar_user = (ImageView) findViewById(R.id.id_view_image_user);
        bt_follow_user = (Button) findViewById(R.id.btn_follow_user);
        Img_nhantin = (Button) findViewById(R.id.imge_nhantin);
        tv_baiviet =(TextView)findViewById(R.id.tv_view_item) ;
        tv_follower =(TextView)findViewById(R.id.tv_view_follower);
        tv_following =(TextView)findViewById(R.id.tv_view_following);
       /* image_back =(ImageButton) findViewById(R.id.bt_img_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        //Set UserProfile
        getPostsCount();
        getFollowersCount();
        getFollowingCount();


        viewPager = (ViewPager) findViewById(R.id.materialup_viewpager_view);
        //Create Tabhost
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs_view);
        viewPager.setAdapter(new Activity_viewprofile.TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        //End Create Tabhost
        //get key tu ben adapter
        Intent intent = getIntent();
        final String key = intent.getStringExtra("send");

        uDatabase.child("users").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.with(getApplicationContext()).load(user.getAvatarurl()).fit().centerInside().into(img_view_avatar_user);
                tv_ViewUserName.setText(user.getUsername());
               // toolbar_viewprofile.setTitle(user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Get UserFollow
        Query query = uDatabase.child("followers").child(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(singleSnapshot.getValue(Follow.class).getUser_id())){
                        Img_nhantin.setVisibility(View.VISIBLE);
                        mFollowdByCurrentUser=true;
                        bt_follow_user.setText("Đang theo dõi");
                        bt_follow_user.setTextColor(Color.BLACK);
                        bt_follow_user.setBackgroundResource(R.drawable.button_edit_profile);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Set click button
        bt_follow_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Notification */
                /*String NotificationKey = FirebaseDatabase.getInstance().getReference("users"
                        + "/" + userFollow.getUser_id()
                        + "/notifications").push().getKey();

                Notification n = new Notification();

                n.setNewFollow(true);
                n.setKey(NotificationKey);

                if (userFollow != null)
                    n.setContent(userFollow.getUsername() + " đã theo dõi.");
                else
                    n.setContent(FirebaseAuth.getInstance().getCurrentUser().getEmail() + " đã theo dõi.");
                n.setStatus(Notification.NOTIFY);
                n.setSendTime((new Date()).getTime());
                n.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());*/

                /*Follower and Following*/
                Query query = uDatabase.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {


                          final  String keyID = singleSnapshot.getKey();
//                            Log.d("Boolen",mFollowdByCurrentUser.toString());
//                            Log.d("Found user",singleSnapshot.getValue(Follow.class)
//                                    .getUser_id() + " == " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Toast.makeText(Activity_viewprofile.this,  " == " + FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                            if (mFollowdByCurrentUser &&
                                    singleSnapshot.getValue(Follow.class)
                                            .getUser_id().equals(String.valueOf(key))) {

                                uDatabase.child("following")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(keyID)
                                        .removeValue();
                                Log.d("Clear",keyID);

                              //  Toast.makeText(Activity_viewprofile.this, FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                                uDatabase.child("followers")
                                        .child(key)
                                        .child(keyID)
                                        .removeValue();
                                mFollowdByCurrentUser=false;
                                bt_follow_user.setText("Theo dõi");
                                bt_follow_user.setBackgroundResource(R.drawable.custom_button_viewprofile);
                                bt_follow_user.setTextColor(Color.WHITE);
                                Img_nhantin.setVisibility(View.GONE);
                                //End Setup
                            }
                            //case2: The user has not liked the photo
                            else if (!mFollowdByCurrentUser) {
                                //add new follow
                                String newkey = uDatabase.push().getKey();
                                //Log.d("Like","here");
                                Follow fl = new Follow();
                                fl.setUser_id(key);
                                // following
                                Follow following = new Follow();
                                following.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                uDatabase.child("following")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(newkey)
                                        .setValue(fl);
                                uDatabase.child("followers")
                                        .child(key)
                                        .child(newkey)
                                        .setValue(following);

                                Img_nhantin.setVisibility(View.VISIBLE);
                                bt_follow_user.setText("Đang theo dõi");
                                bt_follow_user.setTextColor(Color.BLACK);
                                bt_follow_user.setBackgroundResource(R.drawable.button_edit_profile);

                                mFollowdByCurrentUser = true;
                            }
                        }

                        if (!dataSnapshot.exists()) {
                            String newkey = uDatabase.push().getKey();
                            Follow fl = new Follow();
                            fl.setUser_id(key);
                            // following
                            Follow following = new Follow();
                            following.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            uDatabase.child("following")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(newkey)
                                    .setValue(fl);
                            uDatabase.child("followers")
                                    .child(key)
                                    .child(newkey)
                                    .setValue(following);


                            Img_nhantin.setVisibility(View.VISIBLE);
                            bt_follow_user.setText("Đang theo dõi");
                            bt_follow_user.setTextColor(Color.BLACK);
                            bt_follow_user.setBackgroundResource(R.drawable.button_edit_profile);
                            //  break;
                            mFollowdByCurrentUser = true;
                            //break;
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        //View Followers
        tv_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_viewprofile.this,ViewFollow.class);
                intent.putExtra("view","followers");
                intent.putExtra("user_id",key);
                startActivity(intent);
            }
        });

        //View Saved
        tv_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_viewprofile.this,ViewFollow.class);
                intent.putExtra("view","following");
                intent.putExtra("user_id",key);
                startActivity(intent);
            }
        });


    }
    //getFollowersCount
    private void getFollowersCount(){

        //get key tu ben adapter
        Intent intent = getIntent();
        final String key = intent.getStringExtra("send");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("followers")
                .child(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFollowersCount = 0;
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    //Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.getValue());
                    mFollowersCount++;
                }
                tv_follower.setText(String.valueOf(mFollowersCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //View Followers

    //getFollowingCount
    private void getFollowingCount(){
        mFollowingCount = 0;
        //get key tu ben adapter
        Intent intent = getIntent();
        final String key = intent.getStringExtra("send");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("following")
                .child(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFollowingCount = 0;
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    //Log.d(TAG, "onDataChange: found following user:" + singleSnapshot.getValue());
                    mFollowingCount++;
                }
                tv_following.setText(String.valueOf(mFollowingCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPostsCount(){
        mPostsCount = 0;
        //get key tu ben adapter
        Intent intent = getIntent();
        final String key = intent.getStringExtra("send");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("photos")
                .orderByChild("user_id").equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPostsCount = 0;
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    //Log.d(TAG, "onDataChange: found post:" + singleSnapshot.getValue());
                    mPostsCount++;
                }
                tv_baiviet.setText(String.valueOf(mPostsCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static class TabsAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 1;

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return new View_ProfilePost();
            }
//            } else if (i == 1) {
//                return new NoPermission();
//            } else if (i == 2) {
//                return new NoPermission();
//            }
            else {
                return new View_ProfilePost();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Bài viết";
//                case 1:
//                    return "Đã thích";
//                case 2:
//                    return "Đã lưu";
                default:
                    return "Bài viết";
            }
        }
    }

}

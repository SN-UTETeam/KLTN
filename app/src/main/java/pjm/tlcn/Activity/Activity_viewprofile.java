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
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import pjm.tlcn.Fragment.Following;
import pjm.tlcn.Fragment.Likes;
import pjm.tlcn.Fragment.Post;
import pjm.tlcn.Model.Follow;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

public class Activity_viewprofile extends AppCompatActivity {

    public DatabaseReference uDatabase;
    private TextView tv_ViewUserName;
    private Button bt_follow_user;
    private Button Img_nhantin;
    private ImageView img_view_avatar_user;
    private ViewPager viewPager;
    private Boolean mFollowdByCurrentUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);
        uDatabase = FirebaseDatabase.getInstance().getReference();


        tv_ViewUserName = (TextView) findViewById(R.id.tv_ViewUserName);
        img_view_avatar_user = (ImageView) findViewById(R.id.id_view_image_user);
        bt_follow_user = (Button) findViewById(R.id.btn_follow_user);
        Img_nhantin = (Button) findViewById(R.id.imge_nhantin);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Get UserFollow
        Query query = uDatabase.child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if(key.equals(singleSnapshot.getValue(Follow.class).getUser_id())){
                        Img_nhantin.setVisibility(View.VISIBLE);
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
                /*uDatabase.child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().child("user_id").setValue(key);
                Img_nhantin.setVisibility(View.VISIBLE);
                bt_follow_user.setText("Đang theo dõi");
                bt_follow_user.setTextColor(Color.BLACK);
                bt_follow_user.setBackgroundResource(R.drawable.button_edit_profile);*/
                Query query = uDatabase.child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            String keyID = singleSnapshot.getKey();

                            if (mFollowdByCurrentUser &&
                                    singleSnapshot.getValue(Follow.class)
                                            .getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                Log.d("cay",singleSnapshot.getKey());
                                uDatabase.child("followers")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(singleSnapshot.getKey())
                                        .removeValue();

                                Toast.makeText(Activity_viewprofile.this, singleSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                uDatabase.child("following")
                                        .child(key)
                                        .child(singleSnapshot.getKey())
                                        .removeValue();
                                bt_follow_user.setBackgroundResource(R.drawable.custom_button_viewprofile);
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
                                uDatabase.child("followers")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(newkey)
                                        .setValue(fl);
                                uDatabase.child("following")
                                        .child(key)
                                        .child(newkey)
                                        .setValue(following);


                                bt_follow_user.setText("Đang theo dõi");
                                bt_follow_user.setTextColor(Color.BLACK);
                                bt_follow_user.setBackgroundResource(R.drawable.button_edit_profile);
                                Img_nhantin.setVisibility(View.VISIBLE);
                                mFollowdByCurrentUser = true;
                                break;
                            }
                        }
                        if (!dataSnapshot.exists()) {
                            String newkey = uDatabase.push().getKey();
                            Follow fl = new Follow();
                            fl.setUser_id(key);
                            // following
                            Follow following = new Follow();
                            following.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            uDatabase.child("followers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(newkey)
                                    .setValue(fl);
                            uDatabase.child("following")
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


                //  bt_follow_user.setBackground(R.drawable.);
            }
        });


    }

    private static class TabsAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 3;

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
                return new Post();
            } else if (i == 1) {
                return new Likes();
            } else if (i == 2) {
                return new Following();
            } else {
                return new Post();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Bài viết";

                case 1:
                    return "Đã thích";

                case 2:
                    return "Đang theo dõi";
                default:
                    return null;
            }
        }
    }

}

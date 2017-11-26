package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import pjm.tlcn.Fragment.NoPermission;
import pjm.tlcn.Fragment.PostGrid;
import pjm.tlcn.Fragment.PostList;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

@SuppressWarnings("deprecation")
public class TabActivity_profile extends FragmentActivity{
    public DatabaseReference databaseRef;
    private TextView tv_UserName,tv_describer,tv_following,tv_follower,tv_post;

    private Button btn_edit_profile;
    private ImageButton ImgBtn_setting,button_saved;
    private ImageView img_avatar;
    public String UserName;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_profile);

        //Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference();

        //Create Variable
        btn_edit_profile = (Button) findViewById(R.id.btn_edit_profile);
        ImgBtn_setting = (ImageButton) findViewById(R.id.ImgBtn_setting);
        button_saved = (ImageButton) findViewById(R.id.button_saved);
        tv_UserName = (TextView) findViewById(R.id.tv_UserName);
        tv_describer = (TextView) findViewById(R.id.tv_describer);
        tv_following = (TextView) findViewById(R.id.tv_following);
        tv_follower = (TextView) findViewById(R.id.tv_follower);
        tv_post = (TextView) findViewById(R.id.tv_post);
        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        viewPager = (ViewPager) findViewById(R.id.materialup_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);


        //Create Tabhost
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setTabsFromPagerAdapter(tabsAdapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.profile_grid);
        tabLayout.getTabAt(1).setIcon(R.drawable.profile_list);
        tabLayout.getTabAt(2).setIcon(R.drawable.profile_save);
        //End Create Tabhost

        //Start Set Onclick btn_edit_profile
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabActivity_profile.this,Edit_profile.class);
                startActivity(intent);

            }
        });
        //End Set Onclick btn_edit_profile

        //Start Set Onclick ImgBtn_setting
        ImgBtn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabActivity_profile.this,Setting.class);
                startActivity(intent);

            }
        });
        //End Set Onclick ImgBtn_setting

        //Start intent Saved
        button_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabActivity_profile.this, pjm.tlcn.Activity.Saved.class);
                startActivity(intent);
            }
        });

        //Set UserProfile
        getPostsCount();
        getFollowersCount();
        getFollowingCount();
        databaseRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                tv_UserName.setText(user.getUsername());
                tv_describer.setText(user.getDescriber());
                Picasso.with(getApplicationContext()).load(user.getAvatarurl()).fit().centerInside().into(img_avatar);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //View Followers
        tv_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabActivity_profile.this,ViewFollow.class);
                intent.putExtra("view","followers");
                intent.putExtra("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
            }
        });

        //View Saved
        tv_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabActivity_profile.this,ViewFollow.class);
                intent.putExtra("view","following");
                intent.putExtra("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
            }
        });

    }
    //getFollowersCount
    private void getFollowersCount(){


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("followers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("following")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("photos")
                .orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPostsCount = 0;
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    //Log.d(TAG, "onDataChange: found post:" + singleSnapshot.getValue());
                    mPostsCount++;
                }
                tv_post.setText(String.valueOf(mPostsCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static class TabsAdapter extends FragmentPagerAdapter {

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0: {Log.d("return tab post",i+"");
                            return new NoPermission();

                        }
                case 1: {Log.d("return tab like",i+"");

                            return new PostList();
                        }
                case 2: {Log.d("return tab saved",i+"");
                    return new pjm.tlcn.Fragment.Saved();
                }
                default: {Log.d("return tab post",i+"");
                    return new PostGrid();}
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
                    return null;
        }
    }

    public void SetCurrentTab(int i){
        viewPager.setCurrentItem(i);
    }

}
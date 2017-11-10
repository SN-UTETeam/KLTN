package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import pjm.tlcn.Fragment.Following;
import pjm.tlcn.Fragment.Likes;
import pjm.tlcn.Fragment.Post;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

@SuppressWarnings("deprecation")
public class TabActivity_profile extends FragmentActivity {
    public DatabaseReference mDatabase,uDatabase;
    private TextView tv_UserName,tv_describer;

    private Button btn_edit_profile;
    private ImageButton ImgBtn_setting;
    private ImageView img_avatar;
    public String UserName;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_profile);

        //Firebase
        uDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //Create Variable
        btn_edit_profile = (Button) findViewById(R.id.btn_edit_profile);
        ImgBtn_setting = (ImageButton) findViewById(R.id.ImgBtn_setting);
        tv_UserName = (TextView) findViewById(R.id.tv_UserName);
        tv_describer = (TextView) findViewById(R.id.tv_describer);
        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        viewPager = (ViewPager) findViewById(R.id.materialup_viewpager);


        //Create Tabhost
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);

        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

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

        //Set UserProfile
        uDatabase.addValueEventListener(new ValueEventListener() {
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
            } else if (i == 1){
                return new Likes();
            } else if (i == 2){
                return new Following();
            } else {
                return new Post();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
           switch (position){
               case 0: return "Bài viết";

               case 1: return "Đã thích";

               case 2: return "Đang theo dõi";
               default:
                   return null;
           }
        }
    }

}

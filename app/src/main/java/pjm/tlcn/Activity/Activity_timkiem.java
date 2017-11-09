package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import pjm.tlcn.Fragment.Following;
import pjm.tlcn.Fragment.Likes;
import pjm.tlcn.Fragment.Post;
import pjm.tlcn.R;

public class Activity_timkiem extends FragmentActivity {

    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timkiem);



        viewPager = (ViewPager) findViewById(R.id.timkiem_viewpager);


        //Create Tabhost
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.timkiem_tabs);

        viewPager.setAdapter(new Activity_timkiem.TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
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

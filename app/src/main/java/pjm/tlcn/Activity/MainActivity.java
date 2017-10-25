package pjm.tlcn.Activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import pjm.tlcn.R;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
    private TabHost Tabhost_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create Tabhost
        Tabhost_main =(TabHost) findViewById(android.R.id.tabhost);
        Tabhost_main.getTabWidget().setDividerDrawable(null);

        TabHost.TabSpec Tabhost1 =  Tabhost_main.newTabSpec("Tab1");
        TabHost.TabSpec Tabhost2 =  Tabhost_main.newTabSpec("Tab2");
        TabHost.TabSpec Tabhost3 =  Tabhost_main.newTabSpec("Tab3");
        TabHost.TabSpec Tabhost4 =  Tabhost_main.newTabSpec("Tab4");
        TabHost.TabSpec Tabhost5 =  Tabhost_main.newTabSpec("Tab5");

        //Create Tab1 - TabActivity_main
        Tabhost1.setIndicator("",getResources().getDrawable(R.drawable.tab1_selector));
        Tabhost1.setContent(new Intent(this,TabActivity_home.class));

        //Create Tab2 - search
        Tabhost2.setIndicator("",getResources().getDrawable(R.drawable.tab2_selector));
        Tabhost2.setContent(new Intent(this,TabActivity_search.class));

        //Create Tab3 - news
        Tabhost3.setIndicator("",getResources().getDrawable(R.drawable.tab3_selector));
        Tabhost3.setContent(new Intent(this,TabActivity_news.class));

        //Create Tab4 - message
        Tabhost4.setIndicator("",getResources().getDrawable(R.drawable.tab4_selector));
        Tabhost4.setContent(new Intent(this,TabActivity_message.class));

        //Create Tab5 - Profile
        Tabhost5.setIndicator("",getResources().getDrawable(R.drawable.tab5_selector));
        Tabhost5.setContent(new Intent(this,TabActivity_profile.class));

        //Add Tab
        Tabhost_main.addTab(Tabhost1);
        Tabhost_main.addTab(Tabhost2);
        Tabhost_main.addTab(Tabhost3);
        Tabhost_main.addTab(Tabhost4);
        Tabhost_main.addTab(Tabhost5);

        Tabhost_main.getTabWidget().setBackgroundResource(R.color.colorHaftWhite);
        Tabhost_main.getTabWidget().setStripEnabled(false);

        //Set Icon
        for (int i = 0; i < Tabhost_main.getTabWidget().getChildCount(); i++){
            Tabhost_main.getTabWidget().getChildAt(i).setPadding(50,50,50,50);
        }
    }
}

package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import pjm.tlcn.R;

public class TabActivity_viewall extends AppCompatActivity {

    public static TabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_viewall);
        mTabHost =(TabHost) findViewById(android.R.id.tabhost);

    }
}

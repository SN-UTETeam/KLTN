package pjm.tlcn.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pjm.tlcn.Adapter.GridViewAdapter;
import pjm.tlcn.Adapter.ListViewAdapter;
import pjm.tlcn.Adapter.RecyclerViewItem;
import pjm.tlcn.R;



public class TabActivity_home extends AppCompatActivity {

    private ArrayList<RecyclerViewItem> corporations;
    private ArrayList<RecyclerViewItem> operatingSystems;
    private RecyclerView listView;
    private RecyclerView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    ////
    ListView lv;
    GridView gv;
    TextView tvfont,tvall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home);
        tvfont = (TextView)findViewById(R.id.font);
        // khai báo và add kiểu font bạn cần
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/SNAP____.TTF");
        //add kiểu font vào textview font
        tvfont.setTypeface(typeface);
        ////
         tvall = (TextView) findViewById(R.id.idviewall);
         tvall.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(), TabActivity_viewall.class);
                 startActivity(intent);
             }
         });

        listView = (RecyclerView) findViewById(R.id.list);
        gridView = (RecyclerView) findViewById(R.id.grid);

        setDummyData();

        listView.setHasFixedSize(true);
        gridView.setHasFixedSize(true);

        //set layout manager and adapter for "ListView"
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(horizontalManager);
        listViewAdapter = new ListViewAdapter(this, corporations);
        listView.setAdapter(listViewAdapter);

        //set layout manager and adapter for "GridView"
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, operatingSystems);
        gridView.setAdapter(gridViewAdapter);


    }
    private void setDummyData() {
        corporations = new ArrayList<>();
        corporations.add(new RecyclerViewItem(R.drawable.ava, "Microsoft"));
        corporations.add(new RecyclerViewItem(R.drawable.ic_message_tab, "Apple"));
        corporations.add(new RecyclerViewItem(R.drawable.ic_photo, "Google"));
        // corporations.add(new RecyclerViewItem(R.drawable.oracle, "Oracle"));
        // corporations.add(new RecyclerViewItem(R.drawable.yahoo, "Yahoo"));
        // corporations.add(new RecyclerViewItem(R.drawable.mozilla, "Mozilla"));

        operatingSystems = new ArrayList<>();
        operatingSystems.add(new RecyclerViewItem(R.drawable.ava, "NgocDoly"));
        operatingSystems.add(new RecyclerViewItem(R.drawable.ic_photo, "Tizen"));
        // operatingSystems.add(new RecyclerViewItem(R.drawable.android, "Android"));
        // operatingSystems.add(new RecyclerViewItem(R.drawable.symbian, "Symbian"));
        // operatingSystems.add(new RecyclerViewItem(R.drawable.firefox_os, "Firefox OS"));
        // operatingSystems.add(new RecyclerViewItem(R.drawable.wp_os, "Windows Phone OS"));
    }
}

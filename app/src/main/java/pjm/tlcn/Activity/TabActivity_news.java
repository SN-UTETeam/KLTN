package pjm.tlcn.Activity;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import pjm.tlcn.Adapter.AsyncTaskLoadFiles;
import pjm.tlcn.Adapter.TabNews_ImageAdapter;
import pjm.tlcn.R;


public class TabActivity_news extends AppCompatActivity {
    AsyncTaskLoadFiles myAsyncTaskLoadFiles;
    TabNews_ImageAdapter tabNews_imageAdapter;
    ImageView tab_news_imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_news);


        tab_news_imageview = (ImageView) findViewById(R.id.tab_news_imageview);
        final GridView gridview = (GridView) findViewById(R.id.tab_news_gridview);


        tabNews_imageAdapter = new TabNews_ImageAdapter(this);
        gridview.setAdapter(tabNews_imageAdapter);
        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(tabNews_imageAdapter);
        myAsyncTaskLoadFiles.execute();
        gridview.setOnItemClickListener(myOnItemClickListener);

    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            tab_news_imageview.setImageURI(Uri.parse((String) parent.getItemAtPosition(position)));
//            tabNews_imageAdapter.remove(position);
//            tabNews_imageAdapter.notifyDataSetChanged();
        }
    };
}
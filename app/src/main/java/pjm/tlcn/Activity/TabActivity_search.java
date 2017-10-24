package pjm.tlcn.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import pjm.tlcn.R;

public class TabActivity_search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_search);
        LinearLayout lnsearch=(LinearLayout) findViewById(R.id.timkiem);
        lnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timkiem= new Intent(TabActivity_search.this,Activity_timkiem.class);
                startActivity(timkiem);
            }
        });
    }
}

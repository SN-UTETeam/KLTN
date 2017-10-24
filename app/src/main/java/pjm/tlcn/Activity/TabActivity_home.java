package pjm.tlcn.Activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import pjm.tlcn.R;



public class TabActivity_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home);

        TextView tvfont = (TextView)findViewById(R.id.font);
        // khai báo và add kiểu font bạn cần
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/SNAP____.TTF");
        //add kiểu font vào textview font

        tvfont.setTypeface(typeface);
    }
}

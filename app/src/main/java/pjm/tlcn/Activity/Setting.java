package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import pjm.tlcn.R;

public class Setting extends AppCompatActivity {
    Toolbar toolbar_setting;
    private TextView tv_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Create Variable
        toolbar_setting = (Toolbar) findViewById(R.id.toolbar_setting);
        tv_logout = (TextView) findViewById(R.id.tv_logout);

        //Set Back ToolBar
        setSupportActionBar(toolbar_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_setting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Logout
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                System.exit(0);
            }
        });
    }
}

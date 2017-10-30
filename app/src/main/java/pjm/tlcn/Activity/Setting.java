package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import pjm.tlcn.R;

public class Setting extends AppCompatActivity {
    Toolbar toolbar_setting;
    private TextView tv_logout;
    private ImageView img_changepassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Create Variable
        toolbar_setting = (Toolbar) findViewById(R.id.toolbar_setting);
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        img_changepassword = (ImageView) findViewById(R.id.img_changepassword);

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
                finishAffinity();
                System.exit(0);
            }
        });

        //Set Click Change password
        img_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Set_password.class);
                startActivity(intent);
            }
        });
    }
}

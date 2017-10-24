package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pjm.tlcn.R;

public class Login extends AppCompatActivity {
    private Button btn_login,btn_loginwithfb;
    private TextView textview_dangky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Create variable
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_loginwithfb = (Button) findViewById(R.id.btn_loginwithfb);
        textview_dangky = (TextView) findViewById(R.id.textview_dangky);



        //SetOnlick btn_login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                
            }
        });
        //End SetOnlick btn_login

        //Set Onlick textview_dangky
        textview_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        //End Set Onlick textview_dangky
    }
}

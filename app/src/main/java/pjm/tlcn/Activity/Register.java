package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import pjm.tlcn.R;

public class Register extends AppCompatActivity {
private TextView textview_registerwithEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Create Variable
        textview_registerwithEmail = (TextView) findViewById(R.id.textview_registerwithEmail);


        //Set Onclick textview_registerwithEmail
        textview_registerwithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,RegisterWithEmail.class);
                startActivity(intent);
            }
        });
        //End Set Onclick textview_registerwithEmail

    }
}

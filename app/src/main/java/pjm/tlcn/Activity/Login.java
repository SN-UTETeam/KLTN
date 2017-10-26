package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pjm.tlcn.R;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btn_login,btn_loginwithfb;
    private TextView textview_dangky;
    private EditText edt_EmailLogin,edt_PassWordLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Create variable
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_loginwithfb = (Button) findViewById(R.id.btn_loginwithfb);
        textview_dangky = (TextView) findViewById(R.id.textview_dangky);
        edt_EmailLogin = (EditText) findViewById(R.id.edt_EmailLogin);
        edt_PassWordLogin = (EditText) findViewById(R.id.edt_PassWordLogin);
        edt_PassWordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());


        //Create User FireBaseAuth
        mAuth = FirebaseAuth.getInstance();

        //SetOnlick btn_login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(edt_EmailLogin.getText().toString(),edt_PassWordLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Login success",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                
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

package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pjm.tlcn.Model.User;
import pjm.tlcn.R;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    public FirebaseAuth mAuth;
    public DatabaseReference mDatabase;
    public TextView tv_dangnhap;
    public Button btn_Register;
    public EditText edt_EmailRegister,edt_PassWordRegister;
    private ProgressBar progress_bar_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Create Variable
        progress_bar_register = (ProgressBar) findViewById(R.id.progress_bar_register);
        btn_Register = (Button) findViewById(R.id.btn_Register);
        tv_dangnhap = (TextView) findViewById(R.id.tv_dangnhap);
        edt_EmailRegister = (EditText) findViewById(R.id.edt_EmailRegister);
        edt_PassWordRegister = (EditText) findViewById(R.id.edt_PassWordRegister);
        edt_PassWordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //Create Firebase Ref
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_EmailRegister.getText().toString().length()!=0 && edt_PassWordRegister.getText().toString().length()>=6){
                    progress_bar_register.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(edt_EmailRegister.getText().toString(),edt_PassWordRegister.getText().toString())
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
                                        User user = new User(fbuser.getUid(),"UserName",edt_EmailRegister.getText().toString(),edt_PassWordRegister.getText().toString(),"","No Describer","NoAvatar");
                                        mDatabase.child("Users").child(fbuser.getUid()).setValue(user);
                                        progress_bar_register.setVisibility(View.GONE);
                                        Toast.makeText(Register.this,"Đăng ký thành công!",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else {
                                        progress_bar_register.setVisibility(View.GONE);
                                        Toast.makeText(Register.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    }
                else {
                    progress_bar_register.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Email or Password requires", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Set Onclick tv_dangnhap
        tv_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //End Set Onclick tv_dangnhap

    }

}
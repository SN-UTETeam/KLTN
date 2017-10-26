package pjm.tlcn.Activity;

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
import com.google.firebase.auth.FirebaseUser;

import pjm.tlcn.R;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    public FirebaseAuth mAuth;
    public TextView tv_dangnhap;
    public Button btn_Register;
    public EditText edt_EmailRegister,edt_PassWordRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Create Variable
        btn_Register = (Button) findViewById(R.id.btn_Register);
        tv_dangnhap = (TextView) findViewById(R.id.tv_dangnhap);
        edt_EmailRegister = (EditText) findViewById(R.id.edt_EmailRegister);
        edt_PassWordRegister = (EditText) findViewById(R.id.edt_PassWordRegister);
        edt_PassWordRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //Create User FireBaseAuth
        mAuth = FirebaseAuth.getInstance();


        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(edt_EmailRegister.getText().toString(),edt_PassWordRegister.getText().toString())
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Register.this,"Đăng ký thành công!",Toast.LENGTH_LONG).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                }
                                else {
                                    Toast.makeText(Register.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
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

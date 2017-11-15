package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.firebaseUser;
import static pjm.tlcn.Activity.Login.user;

public class Set_password extends AppCompatActivity {
    public DatabaseReference mDatabase,uDatabase;
    private FirebaseUser fbUser;
    private android.support.v7.widget.Toolbar toolbar_changepassword;
    private EditText edt_current_password,edt_new_password,edt_confirm_password;
    private TextView tv_done_changepassword;
    private String CurrentPW="";
    public Boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        //Firebase
        uDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //Set mapped
        toolbar_changepassword = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_changepassword);
        tv_done_changepassword = (TextView) findViewById(R.id.tv_done_changepassword);
        edt_current_password   = (EditText) findViewById(R.id.edt_current_password);
        edt_new_password   = (EditText) findViewById(R.id.edt_new_password);
        edt_confirm_password   = (EditText) findViewById(R.id.edt_confirm_password);

        //Set back Toolbar
        setSupportActionBar(toolbar_changepassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_changepassword.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_done_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassWord(edt_current_password.getText().toString(),edt_new_password.getText().toString(),edt_confirm_password.getText().toString());

            }
        });
    }

    private void ChangePassWord(String pold,String pnew,String pconfirm){
        final String newPass = pnew;
        CurrentPW = user.getPassword();
        flag=true;
        if(pnew.length() < 6 || pconfirm.length() < 6)
            Toast.makeText(getApplicationContext(), "Mật khẩu mới quá ngắn!", Toast.LENGTH_SHORT).show();
        else
            if(!pold.equals(CurrentPW))
                Toast.makeText(getApplicationContext(), "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
        else
        {
            if (pnew.length() < 6 || pconfirm.length() < 6 || !pnew.equals(pconfirm))
                flag = false;
            if (pold.equals(CurrentPW) && flag) {
                firebaseUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            uDatabase.child("password").setValue(newPass);
                            Toast.makeText(getApplicationContext(), "Change password Successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
            else
                Toast.makeText(getApplicationContext(), "Sai thông tin!", Toast.LENGTH_SHORT).show();

        }

    }
}

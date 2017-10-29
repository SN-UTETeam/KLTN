package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pjm.tlcn.R;

public class Login extends AppCompatActivity {
    private static final String TAG = "FacebookLogin";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private AccessToken handleFacebookAccessToken;
    private Button btn_login;
    private LoginButton loginButton;
    private TextView textview_dangky;
    private EditText edt_EmailLogin,edt_PassWordLogin;
    public static FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FaceBook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        //Create variable
        btn_login = (Button) findViewById(R.id.btn_login);
       // btn_loginfb = (Button) findViewById(R.id.btn_loginFB);
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
        //Facebook
        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.btn_loginFB);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]

    }
    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            firebaseUser = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]
    // [START on_activity_result]

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK

        Toast.makeText(Login.this,"Login FB Success",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Login.this,MainActivity.class);
        startActivity(intent);
        finish();
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [END on_activity_result]
}

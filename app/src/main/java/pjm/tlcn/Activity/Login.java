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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pjm.tlcn.Model.User;
import pjm.tlcn.R;

public class Login extends AppCompatActivity {
    private static final String TAG = "FacebookLogin";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    public DatabaseReference mDatabase,uDatabase;
    private Button btn_login;
    private LoginButton loginButton;
    private TextView textview_dangky;
    private EditText edt_EmailLogin,edt_PassWordLogin;
    private ProgressBar progress_bar_login;
    public static FirebaseUser firebaseUser;
    public static User user;
    public static String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FaceBook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        //Create variable
        btn_login = (Button) findViewById(R.id.btn_login);
        progress_bar_login = (ProgressBar) findViewById(R.id.progress_bar_login);
        textview_dangky = (TextView) findViewById(R.id.textview_dangky);
        edt_EmailLogin = (EditText) findViewById(R.id.edt_EmailLogin);
        edt_PassWordLogin = (EditText) findViewById(R.id.edt_PassWordLogin);
        edt_PassWordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());


        //Create User FireBaseAuth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        if(isLoggedIn()){
            firebaseUser=mAuth.getCurrentUser();
            if(firebaseUser!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();}
            else {
                LoginManager.getInstance().logOut();
                mAuth.signOut();
                finish();
                startActivity(getIntent());
            }
        }
        //SetOnlick btn_login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_EmailLogin.getText().toString().length()!=0 && edt_PassWordLogin.getText().toString().length()!=0) {
                    progress_bar_login.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(edt_EmailLogin.getText().toString(), edt_PassWordLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user_id = mAuth.getUid();
                                firebaseUser = mAuth.getCurrentUser();
                                progress_bar_login.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progress_bar_login.setVisibility(View.GONE);
                                Toast.makeText(Login.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"You must be have Email or Password",Toast.LENGTH_LONG).show();
                }
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
                progress_bar_login.setVisibility(View.VISIBLE);
                handleFacebookAccessToken(loginResult.getAccessToken().getToken());

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [START auth_with_facebook]
    private void handleFacebookAccessToken(String token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        progress_bar_login.setVisibility(View.VISIBLE);
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            firebaseUser=mAuth.getCurrentUser();
                            user_id=firebaseUser.getUid();
                            user = new User(firebaseUser.getUid()+"",
                                                    firebaseUser.getDisplayName()+"",
                                                    firebaseUser.getEmail()+"",
                                                    firebaseUser.getUid()+"",
                                                    firebaseUser.getPhoneNumber()+"",
                                                    "No Describer",
                                                    firebaseUser.getPhotoUrl().toString()+"");
                            mDatabase.child("Users").child(firebaseUser.getUid()).setValue(user);
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            progress_bar_login.setVisibility(View.VISIBLE);
                            startActivity(intent);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progress_bar_login.setVisibility(View.VISIBLE);

                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    public boolean isLoggedIn() {
//        if(mAuth.getCurrentUser()!=null){
//            firebaseUser=mAuth.getCurrentUser();
//            return true;
//        }
//        else {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            //ser_id=accessToken.getUserId();
             firebaseUser=mAuth.getCurrentUser();
            user_id=firebaseUser.getUid();
            return accessToken != null;
        //}
    }
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }

}

package pjm.tlcn.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pjm.tlcn.Common.SessionUser;
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
    private ProgressBar login_progressbar;
    public static FirebaseUser firebaseUser;
    public static User user;
    public static String user_id;
    public Dialog dialog;
    private TextView tvfont;

    private SessionUser sessionUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FaceBook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        sessionUser=new SessionUser(getBaseContext());
        tvfont = (TextView) findViewById(R.id.tvfont);
        // khai báo và add kiểu font bạn cần
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/SNAP____.TTF");
        //add kiểu font vào textview font
        tvfont.setTypeface(typeface);

        //Create variable
        btn_login = (Button) findViewById(R.id.btn_login);

        textview_dangky = (TextView) findViewById(R.id.textview_dangky);
        edt_EmailLogin = (EditText) findViewById(R.id.edt_EmailLogin);
        edt_PassWordLogin = (EditText) findViewById(R.id.edt_PassWordLogin);
        edt_PassWordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());


        //Create User FireBaseAuth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        //Show progressDialog

        //Created Dialog
        dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


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

                if(edt_EmailLogin.getText().toString().length()==0)
                    Toast.makeText(getApplicationContext(),"You must enter an Email",Toast.LENGTH_LONG).show();
                else
                    if(edt_PassWordLogin.getText().toString().length()==0)
                        Toast.makeText(getApplicationContext(),"You must enter an Password",Toast.LENGTH_LONG).show();
                else
                {
                    dialog.show();
                    mAuth.signInWithEmailAndPassword(edt_EmailLogin.getText().toString(), edt_PassWordLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user_id = mAuth.getUid();
                                uDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
                                uDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        user = dataSnapshot.getValue(User.class);
                                        sessionUser.clearUserStorage();
                                        sessionUser.createSessionUser(user);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                firebaseUser = mAuth.getCurrentUser();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                dialog.dismiss();
                                startActivity(intent);
                                finish();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(Login.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                dialog.show();
                handleFacebookAccessToken(loginResult.getAccessToken().getToken());
            }
            @Override
            public void onCancel() {
               // Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                ///Log.d(TAG, "facebook:onError", error);
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
        //Log.d(TAG, "handleFacebookAccessToken:" + token);
        dialog.show();
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
                            //Check existing user
                            mDatabase.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() !=null){
                                        User getU = dataSnapshot.getValue(User.class);
                                        user = getU;

                                        sessionUser.clearUserStorage();
                                        sessionUser.createSessionUser(user);

                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        dialog.dismiss();
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        user = new User(firebaseUser.getUid()+"",
                                                firebaseUser.getDisplayName()+"",
                                                firebaseUser.getEmail()+"",
                                                firebaseUser.getUid()+"",
                                                firebaseUser.getPhoneNumber()+"",
                                                "No Describer",
                                                firebaseUser.getPhotoUrl().toString()+"");

                                        sessionUser.clearUserStorage();
                                        sessionUser.createSessionUser(user);

                                        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        dialog.dismiss();
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();


                        }

                    }
                });
    }
    // [END auth_with_facebook]

    public boolean isLoggedIn() {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if(accessToken!=null){
            //ser_id=accessToken.getUserId();
            firebaseUser=mAuth.getCurrentUser();
                user = new User(firebaseUser.getUid()+"",
                        firebaseUser.getDisplayName()+"",
                        firebaseUser.getEmail()+"",
                        firebaseUser.getUid()+"",
                        firebaseUser.getPhoneNumber()+"",
                        "No Describer",
                        firebaseUser.getPhotoUrl().toString()+"");
            user_id=firebaseUser.getUid();
            uDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }
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

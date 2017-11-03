package pjm.tlcn.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.firebaseUser;
import static pjm.tlcn.Activity.TabActivity_message.useridchatwith;

public class Chat extends AppCompatActivity {

    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        //
        scrollView.fullScroll(View.FOCUS_DOWN);
        //
        Firebase.setAndroidContext(this);
        reference1 = FirebaseDatabase.getInstance().getReference().child("Messages").child("/" + firebaseUser.getUid() + "_" + useridchatwith);
        reference2 = FirebaseDatabase.getInstance().getReference().child("Messages").child("/" + useridchatwith + "_" + firebaseUser.getUid());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("userid", firebaseUser.getUid());
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userId = map.get("userid").toString();

                if(userId.equals(firebaseUser.getUid())){
                    addMessageBox(message, 1);
                }
                else{
                    addMessageBox(message, 2);
                }

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setTextSize(20);
        textView.setPadding(20,10,20,10);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        lp2.setMargins(20,10,20,10);

        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.sendmessage);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.receivedmessage);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }
}

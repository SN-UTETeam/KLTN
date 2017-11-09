package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pjm.tlcn.Adapter.CustomAdapterListFollow;
import pjm.tlcn.Model.UserFollow;
import pjm.tlcn.R;

public class Activity_timkiem extends AppCompatActivity {

    ListView list_follow;
    ArrayList<UserFollow> userArrayListFollow = new ArrayList();
    private CustomAdapterListFollow customAdapterListFollow;
    private EditText editText_TimKiem;
    DatabaseReference uDatabase= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timkiem);


        list_follow =(ListView)findViewById(R.id.list_follow);
        getData();
        customAdapterListFollow =new CustomAdapterListFollow(this,userArrayListFollow);
        list_follow.setAdapter(customAdapterListFollow);

        editText_TimKiem =(EditText)findViewById(R.id.edittext_timkiem);
        // Add Text Change Listener to EditText
        editText_TimKiem.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                customAdapterListFollow.getFilter().filter(s.toString());
              //  filterText.addTextChangedListener(filterTextWatcher);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    void getData(){
        uDatabase.child("Users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    userArrayListFollow.clear();
                    for (DataSnapshot snop : dataSnapshot.getChildren()) {
                        UserFollow temp = new UserFollow();
                        temp = snop.getValue(UserFollow.class);
                        //  Log.d("AAA", temp.getUsername());
                        userArrayListFollow.add(temp);
                        customAdapterListFollow.notifyDataSetChanged();
                    }
                } else {
                    Log.d("list", "NUL");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

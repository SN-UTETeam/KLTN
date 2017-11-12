package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
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

    public static int iduser=-1;
    ListView list_follow;
    ArrayList<UserFollow> userArrayListFollow = new ArrayList();
    private CustomAdapterListFollow customAdapterListFollow;
    private EditText editText_TimKiem;
    private ImageView back_Follow;
    DatabaseReference uDatabase= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timkiem);


        list_follow =(ListView)findViewById(R.id.list_follow);
        back_Follow =(ImageView)findViewById(R.id.back_follow);
        back_Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
        customAdapterListFollow =new CustomAdapterListFollow(this,userArrayListFollow);
        list_follow.setAdapter(customAdapterListFollow);
      /*  list_follow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Activity_timkiem.this,Activity_viewprofile.class);
                TextView  textView = (TextView) findViewById(R.id.follow_username);

                String text = textView.getText().toString();
                intent.putExtra("send", text );
                Toast.makeText(Activity_timkiem.this, text, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });*/


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
        uDatabase.child("users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                     Log.d("usser",FirebaseAuth.getInstance().getCurrentUser().getUid());
                      userArrayListFollow.clear();
                      for (DataSnapshot snop : dataSnapshot.getChildren()) {


                              UserFollow temp = new UserFollow();
                              temp = snop.getValue(UserFollow.class);
                              Log.d("AAA", FirebaseAuth.getInstance().getCurrentUser().getUid());
                              userArrayListFollow.add(temp);
                              // Toast.makeText(Activity_timkiem.this, snop.getKey(), Toast.LENGTH_SHORT).show();
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

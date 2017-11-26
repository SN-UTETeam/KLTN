package pjm.tlcn.Adapter;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pjm.tlcn.Model.Follow;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/26/2017.
 */

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    private static TextView tv_sendto_sharepost;
    private ImageView img_search_sharepost,img_clear_search;
    private EditText edt_search_sharepost;
    private RecyclerView rv_sharepost;
    private static Button btn_closedialog_sharepost,btn_send_sharepost;
    private RecyclerView_SharePost recyclerView_sharePost;
    private ArrayList<User> arrayUser = new ArrayList<User>();
    private LinearLayout ln_sendto;

    public CustomBottomSheetDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_sharepost, container, false);

        //Create Var
        ln_sendto = (LinearLayout) v.findViewById(R.id.ln_sendto);
        tv_sendto_sharepost = (TextView) v.findViewById(R.id.tv_sendto_sharepost);
        img_search_sharepost = (ImageView) v.findViewById(R.id.img_search_sharepost);
        img_clear_search = (ImageView) v.findViewById(R.id.img_clear_search);
        edt_search_sharepost = (EditText) v.findViewById(R.id.edt_search_sharepost);
        rv_sharepost = (RecyclerView) v.findViewById(R.id.rv_sharepost);
        btn_closedialog_sharepost = (Button) v.findViewById(R.id.btn_closedialog_sharepost);
        btn_send_sharepost = (Button) v.findViewById(R.id.btn_send_sharepost);

        //Set Adapter
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_sharepost.setLayoutManager(layoutManager);
        loadData();
        recyclerView_sharePost = new RecyclerView_SharePost(getContext(),arrayUser);
        rv_sharepost.setAdapter(recyclerView_sharePost);
        recyclerView_sharePost.notifyDataSetChanged();


        //Set click img search
        img_search_sharepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sendto_sharepost.setVisibility(View.GONE);
                edt_search_sharepost.setVisibility(View.VISIBLE);
                img_clear_search.setVisibility(View.VISIBLE);
                edt_search_sharepost.requestFocus();
            }
        });

        ln_sendto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sendto_sharepost.setVisibility(View.GONE);
                edt_search_sharepost.setVisibility(View.VISIBLE);
                img_clear_search.setVisibility(View.VISIBLE);
                edt_search_sharepost.requestFocus();
            }
        });

        img_clear_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sendto_sharepost.setVisibility(View.VISIBLE);
                edt_search_sharepost.setVisibility(View.GONE);
                img_clear_search.setVisibility(View.GONE);
            }
        });

        btn_closedialog_sharepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_send_sharepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Gửi thành công",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        edt_search_sharepost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recyclerView_sharePost.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    private void loadData(){
        databaseRef.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayUser.clear();
                        for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Query query = reference
                                    .child("users")
                                    .orderByChild("user_id")
                                    .equalTo(singleSnapshot.getValue(Follow.class).getUser_id());

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                        User user = singleSnapshot.getValue(User.class);
                                        arrayUser.add(user);
                                        recyclerView_sharePost.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void setTextSendto(String text){
        if(text.length()>1) {
            tv_sendto_sharepost.setVisibility(View.VISIBLE);
            tv_sendto_sharepost.setText(text);
            btn_closedialog_sharepost.setVisibility(View.GONE);
            btn_send_sharepost.setVisibility(View.VISIBLE);
        }
        else {
            tv_sendto_sharepost.setVisibility(View.GONE);
            btn_closedialog_sharepost.setVisibility(View.VISIBLE);
            btn_send_sharepost.setVisibility(View.GONE);
        }
    }
}

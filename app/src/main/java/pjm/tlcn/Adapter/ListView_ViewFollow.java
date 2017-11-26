package pjm.tlcn.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import pjm.tlcn.Model.Follow;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/12/2017.
 */

public class ListView_ViewFollow extends BaseAdapter {
    Context context;
    int resource;
    List<User> items;
    private Boolean mFollowdByCurrentUser[];
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    public ListView_ViewFollow(Context context, int resource, List<User> items) {
        //super(context, resource, items);
        this.context=context;
        this.resource=resource;
        this.items=items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mFollowdByCurrentUser = new Boolean[items.size()];
        Arrays.fill(mFollowdByCurrentUser,false);
        View view = convertView;
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
            viewHolder = new ViewHolder();

            viewHolder.img_avatar_viewfollow = (ImageView) view.findViewById(R.id.img_avatar_viewfollow);
            viewHolder.tv_username_viewfollow = (TextView) view.findViewById(R.id.tv_username_viewfollow);
            viewHolder.btn_follow_viewfollow = (Button) view.findViewById(R.id.btn_follow_viewfollow);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(items.size()>0 && position>=0){
            viewHolder.tv_username_viewfollow.setText(items.get(position).getUsername());
            Picasso.with(context).load(items.get(position).getAvatarurl()).fit().centerInside().into(viewHolder.img_avatar_viewfollow);
            //Set following
            //Get UserFollow
            Query query = databaseRef.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        try {
                        if(items.get(position).getUser_id().equals(singleSnapshot.child("user_id").getValue().toString())){
                            mFollowdByCurrentUser[position]=true;
                            viewHolder.btn_follow_viewfollow.setBackgroundResource(R.drawable.button_following);
                            viewHolder.btn_follow_viewfollow.setText("Đang theo dõi");
                            viewHolder.btn_follow_viewfollow.setTextColor(Color.BLUE);
                            Log.d(items.get(position).getUser_id(),(singleSnapshot.child("user_id").getValue().toString()));
                        }
                        }
                        catch (Exception e){
                            Log.d("Out","Size");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            viewHolder.btn_follow_viewfollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(items.size()>0) {
                        Query query = databaseRef.child("followers").child(items.get(position).getUser_id());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    final String keyID = singleSnapshot.getKey();

                                    if (mFollowdByCurrentUser[position] &&
                                            singleSnapshot.getValue(Follow.class).getUser_id()
                                                    .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                        databaseRef.child("followers")
                                                .child(items.get(position).getUser_id())
                                                .child(keyID)
                                                .removeValue();

                                        databaseRef.child("following")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child(keyID)
                                                .removeValue();

                                        mFollowdByCurrentUser[position] = false;

                                        viewHolder.btn_follow_viewfollow.setBackgroundResource(R.drawable.button_follow);
                                        viewHolder.btn_follow_viewfollow.setText("Theo dõi");
                                        viewHolder.btn_follow_viewfollow.setTextColor(Color.WHITE);
                                        notifyDataSetChanged();

                                        //End Setup
                                    }
                                    //case2: The user has not liked the photo
                                    else if (!mFollowdByCurrentUser[position]) {
                                        //add new follow
                                        String newkey = databaseRef.push().getKey();
                                        //Log.d("Like","here");
                                        Follow fl = new Follow();
                                        fl.setUser_id(items.get(position).getUser_id());
                                        // following
                                        Follow following = new Follow();
                                        following.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        databaseRef.child("followers")
                                                .child(items.get(position).getUser_id())
                                                .child(newkey)
                                                .setValue(following);
                                        databaseRef.child("following")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child(newkey)
                                                .setValue(fl);

                                        viewHolder.btn_follow_viewfollow.setBackgroundResource(R.drawable.button_following);
                                        viewHolder.btn_follow_viewfollow.setText("Đang theo dõi");
                                        viewHolder.btn_follow_viewfollow.setTextColor(Color.BLUE);
                                        mFollowdByCurrentUser[position] = true;
                                        notifyDataSetChanged();
                                    }
                                }
                                if (!dataSnapshot.exists()) {
                                    String newkey = databaseRef.push().getKey();
                                    Follow fl = new Follow();
                                    fl.setUser_id(items.get(position).getUser_id());
                                    // following
                                    Follow following = new Follow();
                                    following.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    databaseRef.child("followers")
                                            .child(items.get(position).getUser_id())
                                            .child(newkey)
                                            .setValue(following);
                                    databaseRef.child("following")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(newkey)
                                            .setValue(fl);

                                    viewHolder.btn_follow_viewfollow.setBackgroundResource(R.drawable.button_following);
                                    viewHolder.btn_follow_viewfollow.setText("Đang theo dõi");
                                    viewHolder.btn_follow_viewfollow.setTextColor(Color.BLUE);
                                    mFollowdByCurrentUser[position] = true;
                                    notifyDataSetChanged();
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else Toast.makeText(context,"Danh sách này không tồn tại! Vui lòng Refresh lại trang",Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
    static class ViewHolder {

        TextView tv_username_viewfollow;
        ImageView img_avatar_viewfollow;
        Button btn_follow_viewfollow;

    }
}
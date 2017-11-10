package pjm.tlcn.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import pjm.tlcn.Activity.Activity_comment;
import pjm.tlcn.Activity.TabActivity_viewall;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

import static com.facebook.FacebookSdk.getApplicationContext;
import static pjm.tlcn.Activity.Login.user;
import static pjm.tlcn.Activity.Login.user_id;
import static pjm.tlcn.Activity.TabActivity_home.id_image;

/**
 * Created by thienphu on 10/26/2017.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private Activity activity;
    private List<Photo> items;
    public DatabaseReference uDatabase,keyimage;
    private StorageReference sDatabase;
    private TabActivity_viewall mAdapterCallback;
    public static String key_img="";
    //


    public ListViewAdapter(Activity activity, List<Photo> items) {

        this.activity = activity;
        this.items = items;
    }



    //call activity
   //


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //Firebase
        uDatabase = FirebaseDatabase.getInstance().getReference();
       sDatabase = FirebaseStorage.getInstance().getReference().child("AvatarUsers").child(user_id);
        //
      //  keyimage = FirebaseDatabase.getInstance().getReference().child("Images").child(user_id);

        //click comment show activity comment
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intet activity in adapter
                Intent intent = new Intent(activity, Activity_comment.class);
                v.getContext().startActivity( intent);
                key_img =items.get(position).getId();
            }
        });
        viewHolder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.image_like.setBackgroundResource(R.drawable.direct_heart);
                uDatabase.child("Likes").child(id_image).child(user_id).setValue(user.getUsername());
                uDatabase.child("Images").child(user_id).child(id_image).child("likes").setValue(items.get(position).getLikes()+1);

            }
        });

        //////
        viewHolder.realtime.setText(items.get(position).getDatetime());

         if(items.get(position).getStatus() != null && !items.get(position).getStatus().isEmpty() && !items.get(position).getStatus().equals("null")){
             viewHolder.username.setText(user.getUsername());
             viewHolder.textView.setText(items.get(position).getStatus());
             viewHolder.quatity.setText(items.get(position).getLikes()+" lượt thích");
             //
             Picasso
                     .with(activity)
                     .load(items.get(position).getImgurl())
                     .fit()
                     .into(viewHolder.imageView);
         }

        //Load user profile
        uDatabase.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewHolder.user_name_img.setText(user.getUsername());
                Picasso.with(getApplicationContext())
                        .load(user.getAvatarurl())
                        .fit()
                        .into(viewHolder.imname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView,imname,comment;
        private ImageButton image_like;
        private TextView textView,quatity,username,realtime,user_name_img;

        //biến public sang comment
      //  public ImageView ;


        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imagemain);
            imname = (ImageView) view.findViewById(R.id.idname);
            comment = (ImageView)view.findViewById(R.id.comment);
            textView = (TextView)view.findViewById(R.id.text);
            quatity =(TextView)view.findViewById(R.id.quatity_like);
            username =(TextView)view.findViewById(R.id.user_name);
            realtime =(TextView)view.findViewById(R.id.date_time);
            user_name_img = (TextView)view.findViewById(R.id.name_user);
            image_like =(ImageButton) view.findViewById(R.id.image_like);

        }

    }

}

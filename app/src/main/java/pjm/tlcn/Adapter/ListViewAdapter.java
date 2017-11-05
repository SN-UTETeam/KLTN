package pjm.tlcn.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import pjm.tlcn.Model.Image;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

import static com.facebook.FacebookSdk.getApplicationContext;
import static pjm.tlcn.Activity.Login.user_id;

/**
 * Created by thienphu on 10/26/2017.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private Activity activity;
    private List<Image> items;
    public DatabaseReference uDatabase;
    private StorageReference sDatabase;
    private TabActivity_viewall mAdapterCallback;
    public static String idimage="";
    //


    public ListViewAdapter(Activity activity, List<Image> items) {

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
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        sDatabase = FirebaseStorage.getInstance().getReference().child("AvatarUsers").child(user_id);
       final String id=uDatabase.getKey();
        //click comment show activity comment
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intet activity in adapter
                v.getContext().startActivity( new Intent(activity, Activity_comment.class));
               // Intent.putExtra(DESCRIPTION,items.get(position).getId().toString());
            }
        });



        // viewHolder.imageView.setImageResource(items.get(position).getDrawableId());
        viewHolder.textView.setText(items.get(position).getStatus());
      //  String tamp = String.valueOf(items.get(position).getLikes());
     //   String tamp = new Integer().toString();
        viewHolder.quatity.setText(items.get(position).getLikes()+" lượt thích");

         Picasso.with(activity).load(items.get(position).getImgurl()).fit().centerInside().into(viewHolder.imageView);

        //Load user profile
        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.with(getApplicationContext())
                        .load(user.getAvatarurl())
                        .fit()
                        .centerInside()
                        .into(viewHolder.imname);
             //   edt_username_editprofile.setText(user.getUsername());
              //  edt_describer_editprofile.setText(user.getDescriber());
             //   edt_email_editprofile.setText(user.getEmail());
             //   if(!user.getPhonenumber().equals(null)) edt_phonenumber_editprofile.setText(user.getPhonenumber());
               // else edt_phonenumber_editprofile.setText("");
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
        private TextView textView,quatity;

        //biến public sang comment
      //  public ImageView ;


        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imagemain);
            imname = (ImageView) view.findViewById(R.id.idname);
            comment = (ImageView)view.findViewById(R.id.comment);
            textView = (TextView)view.findViewById(R.id.text);
            quatity =(TextView)view.findViewById(R.id.quatity_like);

        }

    }

}
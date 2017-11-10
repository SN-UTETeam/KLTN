package pjm.tlcn.Adapter;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pjm.tlcn.Model.Comment;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/8/2017.
 */

public class RecyclerView_TabCmt extends RecyclerView.Adapter<RecyclerView_TabCmt.RecyclerViewHolder>{

    private ArrayList<Comment> item = new ArrayList<Comment>();
    private Context context;
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();



    public RecyclerView_TabCmt(ArrayList<Comment> item) {
        this.item = item;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cmt_tabprofile, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        databaseRef.child("users").child(item.get(position).getUser_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                holder.tv_username_tabcmt.setText(user.getUsername());
                Picasso.with(context).load(user.getAvatarurl()).fit().centerInside().into(holder.img_avatar_tabcmt);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//TimeStamp
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currenttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        try {
            Date date1 = formater.parse(item.get(position).getDate_created());
            Date date2 = formater.parse(currenttime);

            long different=date2.getTime() - date1.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long monthMilli = daysInMilli * 30;

            long elapsedMonths = different / monthMilli;
            different = different % monthMilli;
            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;
            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;
            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;
            String diffTime =" trước";
            if(elapsedMonths>1)
                diffTime= elapsedMonths +" tháng" + diffTime;
            else
            if(elapsedDays>1)
                diffTime= elapsedDays +" ngày"+ diffTime;
            else
            if(elapsedHours>1)
                diffTime= elapsedHours +" giờ"+ diffTime;
            else
            if(elapsedMinutes>1)
                diffTime= elapsedMinutes +" phút"+ diffTime;
            else diffTime="Vừa xong";

            holder.tv_datetime_tabcmt.setText(diffTime);
        } catch (ParseException e) {
            holder.tv_datetime_tabcmt.setText(item.get(position).getDate_created());
            e.printStackTrace();
        }
        holder.tv_cmt_tabcmt.setText(item.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView img_avatar_tabcmt;
        TextView tv_username_tabcmt,tv_datetime_tabcmt,tv_cmt_tabcmt;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            img_avatar_tabcmt = (ImageView) itemView.findViewById(R.id.img_avatar_tabcmt);
            tv_username_tabcmt = (TextView) itemView.findViewById(R.id.tv_username_tabcmt);
            tv_datetime_tabcmt = (TextView) itemView.findViewById(R.id.tv_datetime_tabcmt);
            tv_cmt_tabcmt = (TextView) itemView.findViewById(R.id.tv_cmt_tabcmt);
        }
    }
}
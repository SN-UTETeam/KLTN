package pjm.tlcn.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Model.Message;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/12/2017.
 */

public class MessageAdapter extends BaseAdapter{
    private Activity activity;
    private ArrayList<Message> items = new ArrayList<Message>();

    public MessageAdapter(Activity activity, ArrayList<Message> items) {
        super();
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        if(items.get(position).getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        return 0;
        else return 1;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        int type = getItemViewType(position);
        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = LayoutInflater.from(activity);
            if (type == 0) {
                // Inflate the layout with image
                v = inflater.inflate(R.layout.item_message_sent, parent, false);
            }
            else {
                v = inflater.inflate(R.layout.item_message_received, parent, false);
            }
        }
        //
        ImageView image_message_profile = (ImageView) v.findViewById(R.id.image_message_profile);
        ImageView image_message_body = (ImageView) v.findViewById(R.id.image_message_body);
        TextView text_message_body = (TextView) v.findViewById(R.id.text_message_body);
        TextView text_message_time = (TextView) v.findViewById(R.id.text_message_time);
        TextView text_message_time_image = (TextView) v.findViewById(R.id.text_message_time_image);


        //Load
        Picasso.with(activity).load(items.get(position).getUser_avatar()).fit().centerInside().into(image_message_profile);
        if(items.get(position).getMessage().length()>0) {
            if(items.get(position).getImage_url().length()>10){
               // text_message_time.setText(items.get(position).getDatecreated().toString().substring(11,16));
                text_message_body.setText(items.get(position).getMessage());
                text_message_body.setVisibility(View.VISIBLE);
                text_message_time.setVisibility(View.GONE);
                text_message_time_image.setText(items.get(position).getDatecreated().toString().substring(11,16));
                text_message_time_image.setVisibility(View.VISIBLE);

                Picasso.with(activity).load(items.get(position).getImage_url()).into(image_message_body);
                image_message_body.setVisibility(View.VISIBLE);
            }
            else {
                text_message_time.setText(items.get(position).getDatecreated().toString().substring(11,16));
                text_message_body.setText(items.get(position).getMessage());
                image_message_body.setVisibility(View.GONE);
                text_message_time_image.setVisibility(View.GONE);
            }
        }
        else {
            text_message_body.setVisibility(View.GONE);
            Picasso.with(activity).load(items.get(position).getImage_url()).fit().centerInside().into(image_message_body);
            image_message_body.setVisibility(View.VISIBLE);
            text_message_time_image.setText(items.get(position).getDatecreated().toString().substring(11,16));
            text_message_time_image.setVisibility(View.VISIBLE);
            text_message_time.setVisibility(View.GONE);
        }


        return v;
    }

}
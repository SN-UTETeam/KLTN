package pjm.tlcn.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Model.User;
import pjm.tlcn.R;

/**
 * Created by thienphu on 11/7/2017.
 */

public class CustomAdapterSearch extends BaseAdapter {
    ArrayList<User> users;
    Activity ac;
    ImageView image_search;
    public CustomAdapterSearch(Activity ac, ArrayList<User> users) {
        this.ac = ac;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = ac.getLayoutInflater();
        convertView = l.inflate(R.layout.custom_adapter_search, null);
        image_search =(ImageView)convertView.findViewById(R.id.image_search);

        Picasso
                .with(ac)
                .load(users.get(position).getAvatarurl())
                .resize(200, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into(image_search);

        return convertView;
    }
}

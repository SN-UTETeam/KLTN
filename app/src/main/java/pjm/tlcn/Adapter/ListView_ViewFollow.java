package pjm.tlcn.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import pjm.tlcn.Model.User;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/12/2017.
 */

public class ListView_ViewFollow extends ArrayAdapter<User> {
    Activity context;
    int resource;
    List<User> items;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    public ListView_ViewFollow(Activity context, int resource, List<User> items) {
        super(context, resource, items);
        this.context=context;
        this.resource=resource;
        this.items=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
            viewHolder = new ViewHolder();

            viewHolder.img_avatar_viewlike = (ImageView) view.findViewById(R.id.img_avatar_viewfollow);
            viewHolder.tv_username_viewlike = (TextView) view.findViewById(R.id.tv_username_viewfollow);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(items.size()>0 && position>=0){
            viewHolder.tv_username_viewlike.setText(items.get(position).getUsername());
            Picasso.with(context).load(items.get(position).getAvatarurl()).fit().centerInside().into(viewHolder.img_avatar_viewlike);



        }

        return view;
    }
    static class ViewHolder {
        TextView tv_username_viewlike;
        ImageView img_avatar_viewlike;

    }
}
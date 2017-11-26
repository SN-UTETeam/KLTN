package pjm.tlcn.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Model.User;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/1/2017.
 */

public class ListViewUserMessage extends BaseAdapter implements Filterable{
    Activity context;
    int layoutId;
    ArrayList<User> arrayUser;
    ArrayList<User> items;

    public ListViewUserMessage(Activity context,int layoutId,ArrayList<User> arrayUser){
        //super(context, layoutId ,arrayUser);
        this.context=context;
        this.layoutId=layoutId;
        this.arrayUser=arrayUser;
    }

    @Override
    public int getCount() {
        return arrayUser.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.img_user_message = (ImageView) view.findViewById(R.id.img_user_message);
            viewHolder.tv_user_message = (TextView) view.findViewById(R.id.tv_user_message);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(arrayUser.size()>0 && position>=0)
        viewHolder.tv_user_message.setText(arrayUser.get(position).getUsername());
        try{
            Picasso.with(context).load(arrayUser.get(position).getAvatarurl()).fit().centerInside().into(viewHolder.img_user_message);
        }
        catch (Exception e){}
        viewHolder.img_user_message.setVisibility(View.VISIBLE);
        return view;
    }

    static class ViewHolder {
        TextView tv_user_message;
        ImageView img_user_message;

    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                arrayUser = (ArrayList<User>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<User> FilteredArrayNames = new ArrayList<User>();

                if (items == null) {
                    items = new ArrayList<User>(arrayUser);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = items.size();
                    results.values = items;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < items.size(); i++) {
                        //  String dataNames = items.get(i).getUsername();
                        // String image =items.get(i).getAvatarurl();
                        if (items.get(i).getUsername().toLowerCase()
                                .contains(constraint)) {
                            User u = items.get(i);
                            FilteredArrayNames.add(u);
                        }
                    }

                    results.count = FilteredArrayNames.size();
                    System.out.println(results.count);

                    results.values = FilteredArrayNames;
                    Log.e("VALUES", results.values.toString());
                }

                return results;
            }
        };

        return filter;
    }
}

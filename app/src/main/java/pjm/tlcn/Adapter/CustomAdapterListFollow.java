package pjm.tlcn.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Activity.Activity_viewprofile;
import pjm.tlcn.Model.UserFollow;
import pjm.tlcn.R;

/**
 * Created by thienphu on 11/9/2017.
 */

public class CustomAdapterListFollow extends BaseAdapter implements Filterable {
    ArrayList<UserFollow> items ;// Original Values
    ArrayList<UserFollow> itemsDisplayed;// Values to be displayed
    Activity ac;
    ImageView img_follow;
    TextView usename_follow;
    LayoutInflater inflater = null;
    ImageLoader imageLoader;
    public CustomAdapterListFollow(Activity ac, ArrayList<UserFollow> itemsDisplayed) {
        this.ac = ac;
        this.itemsDisplayed=itemsDisplayed;
        inflater = LayoutInflater.from(ac);
    }

    @Override
    public int getCount() {
        if(itemsDisplayed.size()>=5){
            return 5;
        }
        else
        return itemsDisplayed.size();
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
        LayoutInflater l=ac.getLayoutInflater();
        convertView=l.inflate(R.layout.custom_listview_adapter_follow,null);
        img_follow =(ImageView)convertView.findViewById(R.id.image_follow);
        usename_follow=(TextView)convertView.findViewById(R.id.follow_username);
        usename_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ac,Activity_viewprofile.class);
               // TextView  textView = (TextView) findViewById(R.id.follow_username);

                String test = itemsDisplayed.get(position).getUser_id();
                //String key =itemsDisplayed.get(position).g
                intent.putExtra("send", test );
                Toast.makeText(ac, test, Toast.LENGTH_SHORT).show();
                v.getContext().startActivity( intent);
              //  TextView  textView = (TextView) findViewById(R.id.follow_username);

            }
        });

        ///set text
         usename_follow.setText(itemsDisplayed.get(position).getUsername());
        Picasso
                .with(ac)
                .load(itemsDisplayed.get(position).getAvatarurl())
                .resize(200, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into(img_follow);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                itemsDisplayed = (ArrayList<UserFollow>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<UserFollow> FilteredArrayNames = new ArrayList<UserFollow>();

                if (items == null) {
                    items = new ArrayList<UserFollow>(itemsDisplayed);
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
                            UserFollow u =new UserFollow(items.get(i).getUsername(),items.get(i).getAvatarurl(),items.get(i).getUser_id());
                            FilteredArrayNames.add(new UserFollow(u.getUsername(),u.getAvatarurl(),u.getUser_id()));
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

package pjm.tlcn.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import pjm.tlcn.Activity.Activity_view_photo_search;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

/**
 * Created by thienphu on 11/7/2017.
 */

public class CustomAdapterSearch extends BaseAdapter {
    ArrayList<Photo> photo_shes;
    Activity ac;
    ImageView image_search;
    public static String key_discover ="";
    public CustomAdapterSearch(Activity ac, ArrayList<Photo> photo_shes) {
        this.ac = ac;
        this.photo_shes = photo_shes;
    }

    @Override
    public int getCount() {
        return photo_shes.size();
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

        //intent view photo
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ac,Activity_view_photo_search.class);
                v.getContext().startActivity( intent);
                // get key image
                 key_discover = photo_shes.get(position).getUser_id();
             //   Log.d("discover",key_discover.toString());
               // intent.putExtra("sen", key_discover.toString() );
            }
        });
//        Integer size =photo_shes.get(position).getImage_path().size();
//        for(int i=0;i<size;i++)
//        Picasso
//                .with(ac)
//                .load(photo_shes.get(position).getImage_path().get(i).getPath())
//                .resize(200, 200) // resizes the image to these dimensions (in pixel)
//                .centerCrop()
//                .into(image_search);

        return convertView;
    }

}

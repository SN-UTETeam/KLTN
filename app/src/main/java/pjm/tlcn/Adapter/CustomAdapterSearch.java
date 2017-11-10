package pjm.tlcn.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

/**
 * Created by thienphu on 11/7/2017.
 */

public class CustomAdapterSearch extends BaseAdapter {
    ArrayList<Photo> photo_shes;
    Activity ac;
    ImageView image_search;
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

        Picasso
                .with(ac)
                .load(photo_shes.get(position).getImage_path())
                .resize(200, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into(image_search);

        return convertView;
    }
}

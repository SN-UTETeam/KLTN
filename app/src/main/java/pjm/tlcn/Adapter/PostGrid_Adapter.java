package pjm.tlcn.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/14/2017.
 */

public class PostGrid_Adapter extends BaseAdapter {
    ArrayList<Photo> items;
    Context context;
    ImageView image_search;
    public PostGrid_Adapter(Context context, ArrayList<Photo> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
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
        LayoutInflater l = LayoutInflater.from(context);
        convertView = l.inflate(R.layout.item_postgird, null);
        image_search =(ImageView)convertView.findViewById(R.id.ImageView_postgrid);

        //intent view photo
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth);
        Picasso
                .with(context)
                .load(items.get(position).getImage_path())
                .resize(noOfColumns,noOfColumns)
                .centerCrop()
                .into(image_search);

        return convertView;
    }

}

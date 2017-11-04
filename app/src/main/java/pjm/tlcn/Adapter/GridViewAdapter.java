package pjm.tlcn.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import pjm.tlcn.Model.User;
import pjm.tlcn.R;

/**
 * Created by thienphu on 10/26/2017.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
    private List<User> items;
   //private List<RecyclerViewItem> items;
    private Activity activity;

    //
    private DatabaseReference uDatabase;
    private StorageReference sDatabase;
    public GridViewAdapter(Activity activity, List<User> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
     //  viewHolder.imageView.setImageResource(items.get(position).getDrawableId());
        viewHolder.textView.setText(items.get(position).getUsername());
         Picasso.with(activity).load(items.get(position).getAvatarurl()).fit().centerInside().into(viewHolder.img);
       // viewHolder.textView.setText(items.get(position).getUsername());
        //Firebase
        //Firebase




    }

    @Override
    public int getItemCount() {
        if(items.size()>2)
            return 2;
        else
            return items.size();
    }



    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView)view.findViewById(R.id.name_fl);
            img=(ImageView)view.findViewById(R.id.image_fl);


        }
    }
}


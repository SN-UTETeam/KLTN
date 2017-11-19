package pjm.tlcn.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Activity.ViewOnePost;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/14/2017.
 */

public class PostGrid_Adapter extends RecyclerView.Adapter<PostGrid_Adapter.RecyclerViewHolder> {

    private ArrayList<Photo> items;
    private Context context;

    public PostGrid_Adapter(Context context, ArrayList<Photo> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context=parent.getContext();

        View view = inflater.inflate(R.layout.item_postgird, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostGrid_Adapter.RecyclerViewHolder holder, final int position) {
         DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 3;

            //int deviceheight = displaymetrics.heightPixels / 4;

            holder.imageView.getLayoutParams().width = devicewidth;

            //if you need same height as width you can set devicewidth in holder.image_view.getLayoutParams().height
            holder.imageView.getLayoutParams().height = devicewidth;
            Picasso.with(context)
                    .load(items.get(position).getImage_path())
                    //.fit()
                    .resize(devicewidth,devicewidth)
                    .centerCrop()
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewOnePost.class);
                    intent.putExtra("photo_id",items.get(position).getPhoto_id());
                    intent.putExtra("user_id",items.get(position).getUser_id());
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
                imageView =(ImageView) itemView.findViewById(R.id.ImageView_postgrid);

        }
    }
}
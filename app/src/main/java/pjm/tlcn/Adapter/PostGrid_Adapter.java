package pjm.tlcn.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import pjm.tlcn.Activity.ViewOnePost;
import pjm.tlcn.Model.Item_GridPhoto;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/14/2017.
 */

public class PostGrid_Adapter extends RecyclerView.Adapter<PostGrid_Adapter.RecyclerViewHolder> {

    private ArrayList<Item_GridPhoto> items;
    private Context context;

    public PostGrid_Adapter(Context context, ArrayList<Item_GridPhoto> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context=parent.getContext();

        View view = inflater.inflate(R.layout.item_photo_grid, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostGrid_Adapter.RecyclerViewHolder holder, final int position) {
         DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//
            int devicewidth = displaymetrics.widthPixels / 3;

            holder.imageView.getLayoutParams().width = devicewidth;

            holder.imageView.getLayoutParams().height = devicewidth;
        //    holder.videoView.getLayoutParams().height=devicewidth;
        //    holder.videoView.getLayoutParams().width=devicewidth;

        if(items.get(position).getPath().contains("jpg")||items.get(position).getPath().contains("jpeg"))
        {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.GONE);
            Picasso.with(context).load(items.get(position).getPath()).fit().centerCrop().into(holder.imageView);
        }
        else if(items.get(position).getPath().contains("mp4")){
            holder.imageView.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
            try {
                holder.imageView.setImageBitmap(retriveVideoFrameFromVideo(items.get(position).getPath()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Touch","Image");
                Intent intent = new Intent(context, ViewOnePost.class);
                intent.putExtra("photo_id",items.get(position).getPhoto_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,videoView;
    //
        public RecyclerViewHolder(View itemView) {
            super(itemView);
                imageView =(ImageView) itemView.findViewById(R.id.item_photo2);
                videoView = (ImageView) itemView.findViewById(R.id.item_video2);

        }
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
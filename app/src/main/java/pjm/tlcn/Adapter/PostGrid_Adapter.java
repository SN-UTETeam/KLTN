package pjm.tlcn.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
            holder.videoView.getLayoutParams().height=devicewidth;
            holder.videoView.getLayoutParams().width=devicewidth;

        if(items.get(position).getPath().contains("jpg")||items.get(position).getPath().contains("jpeg"))
        {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.GONE);
            Picasso.with(context).load(items.get(position).getPath()).fit().centerCrop().into(holder.imageView);
        }
        else if(items.get(position).getPath().contains("mp4")){
            holder.imageView.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(items.get(position).getPath());
            holder.videoView.setVideoURI(uri);
            holder.videoView.requestFocus();
            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    SystemClock.sleep(10000);
                    mp.setVolume(0f,0f);
                    holder.videoView.start();

                }
            });
            holder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.d("Video","Error");
                    return true;
                }
            });
        }
        holder.videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Touch","Video");
                Intent intent = new Intent(context, ViewOnePost.class);
                intent.putExtra("photo_id",items.get(position).getPhoto_id());
                context.startActivity(intent);
                return false;
            }
        });
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
        ImageView imageView;
        VideoView videoView;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
                imageView =(ImageView) itemView.findViewById(R.id.item_photo2);
                videoView = (VideoView) itemView.findViewById(R.id.item_video2);

        }
    }

}
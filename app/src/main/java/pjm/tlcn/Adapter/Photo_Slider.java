package pjm.tlcn.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.R;

/**
 * Created by Pjm on 11/20/2017.
 */

public class Photo_Slider extends PagerAdapter {
    Context mContext;
    ArrayList<Uri> items = new ArrayList<Uri>();

    public Photo_Slider(Context context, ArrayList<Uri> items){
        this.mContext = context;
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == ((LinearLayout) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.item_photo, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.item_photo);
        final VideoView videoView = (VideoView) itemView.findViewById(R.id.item_video);

        if(items.get(position).toString().contains("jpg")||items.get(position).toString().contains("jpeg"))
        {
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Picasso.with(mContext).load(items.get(position)).fit().centerCrop().into(imageView);
            //imageView.setImageURI(items.get(position));
        }
        else if(items.get(position).toString().contains("mp4")){
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            //videoView.setMediaController(new MediaController(mContext));
            videoView.setVideoPath(items.get(position).toString());
            final String path_vid = items.get(position).toString();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    videoView.setVideoPath(path_vid);
                    videoView.start();
                }
            });
        }


        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((LinearLayout) obj);
    }

}
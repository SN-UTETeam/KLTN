package pjm.tlcn.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
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
        final JZVideoPlayerStandard videoView = (JZVideoPlayerStandard) itemView.findViewById(R.id.item_video);

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
            try {
                videoView.setUp(items.get(position).toString(),JZVideoPlayer.SCREEN_WINDOW_NORMAL,"");
                videoView.thumbImageView.setImageBitmap(retriveVideoFrameFromVideo(items.get(position).toString()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }


        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((LinearLayout) obj);
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
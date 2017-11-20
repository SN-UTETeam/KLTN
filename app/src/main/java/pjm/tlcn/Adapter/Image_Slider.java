package pjm.tlcn.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

import pjm.tlcn.R;

/**
 * Created by Pjm on 11/19/2017.
 */

public class Image_Slider extends PagerAdapter {
    Context mContext;
    ArrayList<Uri> items = new ArrayList<Uri>();

    public Image_Slider(Context context, ArrayList<Uri> items){
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
            imageView.setImageURI(items.get(position));
        }
        else if(items.get(position).toString().contains("mp4")){
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setMediaController(new MediaController(mContext));
            videoView.setVideoURI(items.get(position));
        }


        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((LinearLayout) obj);
    }

}
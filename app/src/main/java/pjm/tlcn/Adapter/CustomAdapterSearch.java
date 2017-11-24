package pjm.tlcn.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Activity.Activity_view_photo_search;
import pjm.tlcn.Model.Item_GridPhoto;
import pjm.tlcn.R;

/**
 * Created by thienphu on 11/7/2017.
 */

public class CustomAdapterSearch extends BaseAdapter {
    ArrayList<Item_GridPhoto> photo_shes;
    Activity ac;
    ImageView image_search;
    VideoView videoView;
    public static String key_discover ="";
    public CustomAdapterSearch(Activity ac, ArrayList<Item_GridPhoto> photo_shes) {
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
        videoView =(VideoView)convertView.findViewById(R.id.item_video_search);


//        Integer size =photo_shes.get(position).getImage_path().size();
      //  for(int i=0;i<size;i++)
      /* Picasso
                .with(ac)
                .load(photo_shes.get(position).getImage_path())
                .resize(200, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into(image_search);*/

        if(photo_shes.get(position).getPath().contains("jpg")||photo_shes.get(position).getPath().contains("jpeg"))
        {
            image_search.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Picasso.with(ac).load(photo_shes.get(position).getPath()).fit().centerCrop().into(image_search);
        }
        else if(photo_shes.get(position).getPath().contains("mp4")){
            image_search.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(photo_shes.get(position).getPath()));
            final String path_vid = photo_shes.get(position).getPath();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    SystemClock.sleep(200);
                    videoView.start();
                }
            });
        }


        //intent view photo
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ac,Activity_view_photo_search.class);
                v.getContext().startActivity( intent);
                // get key image
                //    key_discover = photo_shes.get(position).getUser_id();
                //   Log.d("discover",key_discover.toString());
                // intent.putExtra("sen", key_discover.toString() );
            }
        });

        return convertView;
    }

}

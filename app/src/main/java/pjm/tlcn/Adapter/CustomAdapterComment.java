package pjm.tlcn.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pjm.tlcn.Model.Comment;
import pjm.tlcn.R;

/**
 * Created by thienphu on 11/5/2017.
 */

public class CustomAdapterComment extends BaseAdapter {
    ArrayList<Comment> comment ;
   // public   static  int vtriclick =-1;
    Activity ac;
    ImageView im_comment;
    TextView usename_comment,content_comment,time;
    public CustomAdapterComment(Activity ac, ArrayList<Comment> comment) {
        this.ac = ac;
        this.comment=comment;
    }

    @Override
    public int getCount() {
        return comment.size();
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
        LayoutInflater l=ac.getLayoutInflater();
        convertView=l.inflate(R.layout.custom_adapter_comment,null);
        usename_comment=(TextView)convertView.findViewById(R.id.userid_comment);
        time=(TextView)convertView.findViewById(R.id.realtime);
        content_comment=(TextView)convertView.findViewById(R.id.conten_comment);
        im_comment=(ImageView) convertView.findViewById(R.id.img_comment);

        // set text comment
        usename_comment.setText("Name");
        content_comment.setText(comment.get(position).getComment());
        time.setText(comment.get(position).getDate_created());


        return convertView;
    }

}

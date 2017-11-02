package pjm.tlcn.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pjm.tlcn.Model.Follow;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/1/2017.
 */

public class ListViewUserMessage extends ArrayAdapter<Follow> {
    Activity context;
    int layoutId;
    ArrayList<Follow> arrayFollow;

    public ListViewUserMessage(Activity context,int layoutId,ArrayList<Follow> arrayFollow){
        super(context, layoutId ,arrayFollow);
        this.context=context;
        this.layoutId=layoutId;
        this.arrayFollow=arrayFollow;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.img_user_message = (ImageView) view.findViewById(R.id.img_user_message);
            viewHolder.tv_user_message = (TextView) view.findViewById(R.id.tv_user_message);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(arrayFollow.size()>0 && position>=0)
        viewHolder.tv_user_message.setText(arrayFollow.get(position).getUsername());
        viewHolder.img_user_message.setVisibility(View.VISIBLE);
        //viewHolder.img_user_message.setImageResource(a);
        return view;
    }
    static class ViewHolder {
        TextView tv_user_message;
        ImageView img_user_message;

    }
}

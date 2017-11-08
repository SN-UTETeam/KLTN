package pjm.tlcn.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Model.Cmt_tabProfile;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/8/2017.
 */

public class RecyclerView_TabCmt extends RecyclerView.Adapter<RecyclerView_TabCmt.RecyclerViewHolder>{

    private ArrayList<Cmt_tabProfile> item = new ArrayList<Cmt_tabProfile>();
    private Context context;


    public RecyclerView_TabCmt(ArrayList<Cmt_tabProfile> item) {
        this.item = item;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cmt_tabprofile, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.tv_username_tabcmt.setText(item.get(position).getUsername());
        holder.tv_datetime_tabcmt.setText(item.get(position).getDatetime());
        holder.tv_cmt_tabcmt.setText(item.get(position).getComment());
        Picasso.with(context).load(item.get(position).getAvatarurl()).fit().centerInside().into(holder.img_avatar_tabcmt);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView img_avatar_tabcmt;
        TextView tv_username_tabcmt,tv_datetime_tabcmt,tv_cmt_tabcmt;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            img_avatar_tabcmt = (ImageView) itemView.findViewById(R.id.img_avatar_tabcmt);
            tv_username_tabcmt = (TextView) itemView.findViewById(R.id.tv_username_tabcmt);
            tv_datetime_tabcmt = (TextView) itemView.findViewById(R.id.tv_datetime_tabcmt);
            tv_cmt_tabcmt = (TextView) itemView.findViewById(R.id.tv_cmt_tabcmt);
        }
    }
}
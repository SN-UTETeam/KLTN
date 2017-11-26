package pjm.tlcn.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pjm.tlcn.Model.User;
import pjm.tlcn.R;

/**
 * Created by Pjm on 11/26/2017.
 */

public class RecyclerView_SharePost extends RecyclerView.Adapter<RecyclerView_SharePost.RecyclerViewHolder> implements Filterable{
    private ArrayList<User> items = new ArrayList<User>();
    private ArrayList<User> arrayList;
    private Context context;
    private String text="";
    private ArrayList<User> sendmesage = new ArrayList<User>();

    public RecyclerView_SharePost(Context context, ArrayList<User> items){
        this.context=context;
        this.items=items;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sharepost, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView_SharePost.RecyclerViewHolder holder, final int position) {
        Picasso.with(context).load(items.get(position).getAvatarurl()).fit().centerInside().into(holder.item_avatar);
        if(items.get(position).getUsername().length()>15){
            holder.item_username.setText(items.get(position).getUsername().substring(0,12)+"...");
        }
        else
            holder.item_username.setText(items.get(position).getUsername());

        holder.item_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.item_chosse.setVisibility(View.VISIBLE);
                CustomBottomSheetDialogFragment c = new CustomBottomSheetDialogFragment();
                if(text.length()<1)
                    text = items.get(position).getUsername()+"";
                else
                    text = text + ", "+items.get(position).getUsername();
                c.setTextSendto(text);
                sendmesage.add(items.get(position));
            }
        });

        holder.item_chosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.item_chosse.setVisibility(View.GONE);
                text = text.replace(", " +items.get(position).getUsername()+"","");
                text = text.replace(items.get(position).getUsername()+"","");
                CustomBottomSheetDialogFragment c = new CustomBottomSheetDialogFragment();
                c.setTextSendto(text);
                sendmesage.remove(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_avatar,item_chosse;
        private TextView  item_username;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            item_avatar = (ImageView) itemView.findViewById(R.id.item_avatar);
            item_chosse = (ImageView) itemView.findViewById(R.id.item_chosse);
            item_username = (TextView) itemView.findViewById(R.id.item_username);
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                items = (ArrayList<User>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<User> FilteredArrayNames = new ArrayList<User>();

                if (arrayList == null) {
                    arrayList = new ArrayList<User>(items);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = arrayList.size();
                    results.values = arrayList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < arrayList.size(); i++) {
                        //  String dataNames = items.get(i).getUsername();
                        // String image =items.get(i).getAvatarurl();
                        if (arrayList.get(i).getUsername().toLowerCase()
                                .contains(constraint)) {
                            User u = arrayList.get(i);
                            FilteredArrayNames.add(u);
                        }
                    }

                    results.count = FilteredArrayNames.size();
                    System.out.println(results.count);

                    results.values = FilteredArrayNames;
                    Log.e("VALUES", results.values.toString());
                }

                return results;
            }
        };

        return filter;
    }
}

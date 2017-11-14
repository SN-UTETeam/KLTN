package pjm.tlcn.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pjm.tlcn.Adapter.PostGrid_Adapter;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;


public class PostGrid extends Fragment {
GridView grid_postgrid;
PostGrid_Adapter postGrid_adapter;
ArrayList<Photo> arrayPhoto = new ArrayList<Photo>();
DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_postgrid, container, false);

        //Set  grid view
        grid_postgrid = (GridView) v.findViewById(R.id.grid_postgrid);
        postGrid_adapter = new PostGrid_Adapter(getContext(),arrayPhoto);
        grid_postgrid.setAdapter(postGrid_adapter);

        //Loaddata
        Query query = databaseRef.child("photos").orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    arrayPhoto.clear();
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Photo photo = new Photo();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        photo.setCaption(objectMap.get("caption").toString());
                        photo.setPhoto_id(objectMap.get("photo_id").toString());
                        photo.setUser_id(objectMap.get("user_id").toString());
                        photo.setDate_created(objectMap.get("date_created").toString());
                        photo.setImage_path(objectMap.get("image_path").toString());

                        arrayPhoto.add(photo);
                    }
                    Collections.reverse(arrayPhoto);
                    postGrid_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            TabActivity_profile profile = (TabActivity_profile) getActivity();
//            profile.SetTablayout(0);
//
//
//        }else{
//            // fragment is no longer visible
//        }
//    }
}

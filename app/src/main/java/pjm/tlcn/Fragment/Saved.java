package pjm.tlcn.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pjm.tlcn.Activity.TabActivity_profile;
import pjm.tlcn.R;

public class Saved extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved, container, false);

        return v;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Intent intent =new Intent(getContext(), pjm.tlcn.Activity.Saved.class);
            startActivityForResult(intent,17);


        }else{
            // fragment is no longer visible
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 17) {
            TabActivity_profile profile = (TabActivity_profile) getActivity();
            profile.SetCurrentTab(0);
        }
    }
}
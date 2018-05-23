package pjm.tlcn.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceServiceID extends FirebaseInstanceIdService {
    private final String TAG=this.getClass().getSimpleName();
    @Override
    public void onTokenRefresh() {
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,token);
    }
}

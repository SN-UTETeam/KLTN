package pjm.tlcn;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;

public class MainApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        GoogleApiAvailability t=GoogleApiAvailability.getInstance();
        Log.d("Available",String.valueOf(t.isGooglePlayServicesAvailable(this)));
    }
}

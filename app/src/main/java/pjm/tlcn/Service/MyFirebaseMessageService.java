package pjm.tlcn.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import pjm.tlcn.Activity.MainActivity;
import pjm.tlcn.R;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage!=null){
            pushNotification(remoteMessage);
        }
    }

    private void pushNotification(RemoteMessage data) {
        Log.d("Data",data.toString());
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifiBuilder=new NotificationCompat.Builder(this);
        notifiBuilder.setContentText(data.getNotification().getBody())
                .setContentTitle(data.getNotification().getTitle())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent);
        NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,notifiBuilder.build());
    }
}

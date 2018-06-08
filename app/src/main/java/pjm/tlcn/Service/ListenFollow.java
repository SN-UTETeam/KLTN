package pjm.tlcn.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import pjm.tlcn.Activity.Activity_viewprofile;
import pjm.tlcn.Activity.MainActivity;
import pjm.tlcn.Activity.ViewFollow;
import pjm.tlcn.Common.SessionUser;
import pjm.tlcn.Model.Notification;
import pjm.tlcn.R;

public class ListenFollow extends Service {

    FirebaseAuth auth = null;
    FirebaseDatabase db = null;
    DatabaseReference refNotification = null;
    private SessionUser sessionUser;
    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            auth = FirebaseAuth.getInstance();
            db = FirebaseDatabase.getInstance();
            sessionUser=new SessionUser(getBaseContext());
            refNotification = db.getReference("users" + "/" + sessionUser.getSessionUser().getUser_id() + "/notifications");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (refNotification != null) {
            refNotification.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    if (notification.getStatus() == Notification.NOTIFY) {
                        showNotification(dataSnapshot.getKey(), notification);
                        notification.setStatus(Notification.NOT_SEEN);
                        refNotification.child(dataSnapshot.getKey()).setValue(notification);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else
            Toast.makeText(getBaseContext(), "refNotification is null", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification(String key, Notification notification) {
        Intent intent;

        intent = new Intent(getBaseContext(), MainActivity.class);
        int randomInt = new Random().nextInt(9999 - 1) + 1;

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), randomInt, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(android.app.Notification.DEFAULT_ALL)
                .setTicker(notification.getTitle())
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getContent())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(randomInt, builder.build());
    }
    public ListenFollow() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

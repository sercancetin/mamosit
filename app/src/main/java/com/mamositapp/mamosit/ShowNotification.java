package com.mamositapp.mamosit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mamositapp.mamosit.activity.Home;

public class ShowNotification extends Service {
    private final static String TAG = "ShowNotification";
    private Object Log;

    @Override
    public void onCreate() {
        super.onCreate();

        Intent mainIntent = new Intent(this, Home.class);

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification noti = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                //.setContentTitle("HELLO " + System.currentTimeMillis())
                .setContentTitle("Ekstra Puan")
                .setContentText("Yeni güne güzel başlamanız için size süpriz hazırladık. Almayı unutmayın.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.duyuru)
                .setTicker(getResources().getString(R.string.app_name))
                .setWhen(System.currentTimeMillis())
                .build();

        notificationManager.notify(0, noti);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}

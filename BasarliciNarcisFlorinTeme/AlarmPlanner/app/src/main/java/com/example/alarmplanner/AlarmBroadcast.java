package com.example.alarmplanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


    public class AlarmBroadcast extends BroadcastReceiver {

        public static String NOTIFICATION_ID = "notification-id";
        public static String NOTIFICATION = "notification";

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification notification = intent.getParcelableExtra(NOTIFICATION);

            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                int importance = NotificationManager.IMPORTANCE_HIGH ;
                NotificationChannel notificationChannel = new NotificationChannel("10001" , "NOTIFICATION_CHANNEL_NAME" , importance) ;
                assert manager != null;
                manager.createNotificationChannel(notificationChannel) ;
            }

            int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
            assert manager != null;
            manager.notify(id , notification) ;
        }
    }




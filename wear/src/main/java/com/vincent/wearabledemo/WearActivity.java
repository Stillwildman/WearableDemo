package com.vincent.wearabledemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;

public class WearActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

            }
        });
    }

    public void testBtnClick(View v)
    {
        showMyNotification();
        //Intent intent = new Intent(this, NotificationActivity.class);
        //startActivity(intent);
    }

    private void showMyNotification()
    {
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity
                (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /*Notification.WearableExtender notifExtender = new Notification.WearableExtender()
                .setDisplayIntent(pendingIntent)
                .setCustomSizePreset(Notification.WearableExtender.SIZE_LARGE);*/

        Notification notifBuilder = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle("Customization!")
                .setContentText(".....0.0")
                .extend(new Notification.WearableExtender()
                        .setDisplayIntent(pendingIntent)
                        .setContentIcon(android.R.drawable.ic_menu_camera)
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_LARGE))
                .build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notifBuilder);

        Log.i("Notify!!!", "DONE!!!");
    }

}

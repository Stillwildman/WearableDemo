package com.vincent.wearabledemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class NotificationActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_wearable);

        Log.i("NotifyActivity", "YOOOOO~~");
    }
}

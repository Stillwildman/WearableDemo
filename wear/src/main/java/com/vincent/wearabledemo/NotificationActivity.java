package com.vincent.wearabledemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class NotificationActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_wear);

        Log.i("NotifyActivity", "YOOOOO~~");
    }

    public void testImgClick(View v) {
        Toast.makeText(this, "Do Something~", Toast.LENGTH_SHORT).show();
    }
}

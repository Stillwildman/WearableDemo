package com.vincent.wearabledemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vincent.wearabledemo.R;

public class AmbientActivity extends WearableActivity {

    private BoxInsetLayout container;
    private TextView testText;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambient_activity);
        Log.i("NotifyActivity", "What the hell!!!!!");
        setAmbientEnabled();

        container = (BoxInsetLayout) findViewById(R.id.container);
        testText = (TextView) findViewById(R.id.testNote);
    }

    public void testImgClick(View v) {
        Toast.makeText(this, "Do Something~", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
        count++;
        String origin = testText.getText().toString();
        testText.setText(origin + " " + count);
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            container.setBackgroundColor(getResources().getColor(android.R.color.black));
            testText.setTextColor(Color.BLACK);
            testText.getPaint().setAntiAlias(false);
        } else {
            container.setBackground(null);
            testText.setTextColor(Color.GREEN);
            testText.getPaint().setAntiAlias(true);
        }
    }
}

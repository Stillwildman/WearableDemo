package com.vincent.wearabledemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;

import com.vincent.wearabledemo.R;

public class ConfirmActivity extends Activity implements DelayedConfirmationView.DelayedConfirmationListener {

    private DelayedConfirmationView delayedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirmation);

        delayedView = (DelayedConfirmationView) findViewById(R.id.delayed_confirm);
        delayedView.setListener(this);

        delayedView.setTotalTimeMs(2500);
        delayedView.start();
    }

    @Override
    public void onTimerFinished(View view) {
        callConfirmation(1);
        delayedView.reset();
        this.finish();
    }

    @Override
    public void onTimerSelected(View view) {
        callConfirmation(2);
        delayedView.reset();
    }

    private void callConfirmation(int key)
    {
        Intent intent = new Intent(this, ConfirmationActivity.class);

        // 1 = ConfirmationActivity.SUCCESS_ANIMATION
        // 2 = ConfirmationActivity.OPEN_ON_PHONE_ANIMATIO

        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, key);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,
                getString(R.string.confirmationMessage));

        startActivity(intent);
    }
}

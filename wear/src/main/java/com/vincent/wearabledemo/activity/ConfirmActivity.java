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
        delayedView.reset();
    }

    @Override
    public void onTimerSelected(View view) {
        callConfirmation();
    }

    private void callConfirmation()
    {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,
                getString(R.string.confirmationMessage));

        startActivity(intent);
    }
}

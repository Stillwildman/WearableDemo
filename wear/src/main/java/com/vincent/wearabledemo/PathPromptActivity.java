package com.vincent.wearabledemo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class PathPromptActivity extends Activity implements 
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private int screenHeight;

    private TextView promptText;
    private ImageView promptImage;
    
    private GoogleApiClient gac;

    private static final String DATA_TEXT_KEY = "path_text";
    private static final String DATA_IMG_KEY = "path_image";
    private static final String DATA_PATH = "/demo";

    private static final int PATH_GO_STRAIGHT = 0;
    private static final int PATH_TURN_RIGHT = 1;
    private static final int PATH_TURN_LEFT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.path_prompt_activity);

        promptText = (TextView) findViewById(R.id.path_prompt_text);
        promptImage = (ImageView) findViewById(R.id.path_prompt_image);

        gac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        getScreenInfo();
        setTextLayout();
    }

    private void getScreenInfo()
    {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();

        Log.i("ScreenDisplay", "" + screenWidth + " x " + screenHeight);
    }

    private void setTextLayout()
    {
        int requestHeight = (int) (screenHeight * 0.28);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, getPixels(requestHeight));

        promptText.setLayoutParams(params);

        Log.i("TextLayoutHeight", "" + promptText.getLayoutParams().height);
    }

    public static int getPixels(int dipValue)
    {
        Resources res = Resources.getSystem();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, res.getDisplayMetrics());
        Log.i("Dip to Pixels~~", "" + dipValue + " to " + px);
        return px;
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer)
    {
        for (DataEvent event: dataEventBuffer)
        {
            if (event.getType() == DataEvent.TYPE_CHANGED)
            {
                DataItem items = event.getDataItem();

                if (items.getUri().getPath().compareTo(DATA_PATH) == 0)
                {
                    DataMap dataMap = DataMapItem.fromDataItem(items).getDataMap();
                    updateText(dataMap.getString(DATA_TEXT_KEY));
                    updateImage(dataMap.getInt(DATA_IMG_KEY));

                    vibrate();

                    Log.d("PromptUpdate!!!", dataMap.getString(DATA_TEXT_KEY) + " "
                            + dataMap.getInt(DATA_IMG_KEY));
                }
            }
            else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.i("Item Deleted!", "DELETED!!!!!!!");
            }
        }
    }

    private void updateText(final String text)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                promptText.setText(text);
            }
        });
    }

    private void updateImage(final int imgKey)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (imgKey)
                {
                    case PATH_GO_STRAIGHT:
                        promptImage.setImageResource(R.mipmap.arrow_straight);
                        break;
                    case PATH_TURN_RIGHT:
                        promptImage.setImageResource(R.mipmap.arrow_turn_right);
                        break;
                    case PATH_TURN_LEFT:
                        promptImage.setImageResource(R.mipmap.arrow_turn_left);
                        break;
                }
            }
        });
    }

    private void vibrate()
    {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50, 300};
        //-1 - don't repeat
        final int indexInPatternToRepeat = -1;
        vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gac.connect();
        Log.d("PathGAC_Status", "GAC Connected!!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(gac, this);
        gac.disconnect();
        Log.d("PathGAC_Status", "GAC Disconnected!!");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(gac, this);
        Log.d("PathGAC_Status", "onConnected: " + bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("PathGAC_Status", "onDisconnected: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("PathGAC_Status", "ConnectionFailed: " + connectionResult);
    }
}

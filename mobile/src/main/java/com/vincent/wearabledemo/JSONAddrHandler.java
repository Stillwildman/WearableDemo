package com.vincent.wearabledemo;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.vincent.wearabledemo.MapActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class JSONAddrHandler extends Handler
{
    private WeakReference<MapActivity> weakActivity;

    public JSONAddrHandler(MapActivity activity) {
        weakActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message url)
    {
        MapActivity mapActivity = weakActivity.get();

        switch (url.what)
        {
            case 0:

                String JASONString = null;

                if (url.obj instanceof String)
                    JASONString = (String) url.obj;
                try
                {
                    JSONObject jb = new JSONObject(JASONString);
                    String status = jb.getString("status");
                    Log.i("LocationStatus", status);

                    JSONArray ja = jb.getJSONArray("results");
                    JSONObject result = ja.getJSONObject(0);
                    JSONObject location = result.getJSONObject("geometry").getJSONObject("location");

                    String addressName = result.getString("formatted_address");
                    Log.i("LocationFound", addressName);

                    String lat = location.getString("lat");
                    String lng = location.getString("lng");

                    double locLat = Double.parseDouble(lat) * 1E6;
                    double locLng = Double.parseDouble(lng) * 1E6;

                    mapActivity.putSearchMarker(locLat, locLng, addressName);

                    Log.i("LocationFound", lat + "," + lng);
                }
                catch (JSONException e) {
                    mapActivity.toastLong("Location Not Found!");
                    Log.e("LocationError", e.getMessage());
                }

                getLooper().quit();
        }
    }
}

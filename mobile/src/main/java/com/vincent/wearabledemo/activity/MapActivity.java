package com.vincent.wearabledemo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.vincent.wearabledemo.R;
import com.vincent.wearabledemo.Utils.PolyHelper;
import com.vincent.wearabledemo.handler.JSONAddrHandler;
import com.vincent.wearabledemo.view.DrawOverlay;
import com.vincent.wearabledemo.view.SearchOverlay;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity extends com.google.android.maps.MapActivity implements LocationListener {

    private MapView mapView;
    private MapController mapControl;
    private GeoPoint GP;
    private MyLocationOverlay myLayer;
    private LocationManager locManager;
    private Location myLocation;
    private Criteria criteria;
    private String providerType = "";

    private boolean enableTool;
    private boolean requestGPS;

    private int screenWidth;
    private int screenHeight;
    private double screenSize;
    private EditText typingText;
    private SearchOverlay searchOverlay;
    private DrawOverlay drawOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mapView = (MapView) findViewById(R.id.MapView);
        typingText = (EditText) findViewById(R.id.searchTextInput);

        findMapControl();
        getScreenInfo();
    }

    private void getScreenInfo()
    {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();

        DisplayMetrics DM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(DM);

        double diagonalPixels = Math.sqrt((Math.pow(DM.widthPixels, 2) + Math.pow(DM.heightPixels, 2)));
        screenSize = diagonalPixels / (160 * DM.density);

        Log.i("ScreenSize", "" + screenSize);
        Log.i("ScreenDisplay", "" + screenWidth + " x " + screenHeight);
    }

    private void findMapControl()
    {
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(false);

        String coordinates[] = {"23.94", "121.00"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);

        GP = new GeoPoint(
                (int)(lat * 1E6),(int)(lng * 1E6));

        mapControl = mapView.getController();
        mapControl.animateTo(GP);
        mapControl.setZoom(8);

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocationProvider();
    }

    public void locateClick(View view)
    {
        GPSinit();
    }

    private void findMyLocation()
    {
        List<Overlay> locOverlays = mapView.getOverlays();
        locOverlays.remove(myLayer);

        myLayer = new MyLocationOverlay(this, mapView);
        myLayer.enableCompass();
        myLayer.enableMyLocation();

        myLayer.runOnFirstFix(new Runnable() {
            public void run() {
                int lat = myLayer.getMyLocation().getLatitudeE6();
                int lng = myLayer.getMyLocation().getLongitudeE6();

                mapMove(lat, lng);
            }
        });
        locOverlays.add(myLayer);

        mapView.invalidate();
    }

    public void getLocationProvider()
    {
        try
        {
            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            providerType = locManager.getBestProvider(criteria, true);
            myLocation = locManager.getLastKnownLocation(providerType);
            if ((""+myLocation).equals("null"))
                myLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Log.i("myLocationInfo", "" + myLocation);
            Log.i("LocProviderType", providerType);
        }
        catch (Exception e) {
            Log.e("LocationProvider", e.getMessage());
            e.printStackTrace();
        }
    }

    public void searchClick(View view)
    {
        final String input = typingText.getText().toString().trim().replace(" ", "");

        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (input.length() > 0)
        {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addrs = null;
            Address addr;

            try {
                addrs = geocoder.getFromLocationName(input, 1);
                Log.i("Find Location", addrs.toString());
            }
            catch (IOException e) {
                toastShort("Geocoder：" + e.getMessage());
                Log.e("Find Location Error:", e.toString());
            }
            if (addrs == null || addrs.isEmpty())
            {
                Log.e("addrs Is Empty!", ".....Nothing here");
                typingText.setText("");

                new Thread()
                {
                    public void run()
                    {
                        final String JSONString = getAddressByGoogleApi(input);

                        Looper.prepare();
                        JSONAddrHandler jsonAddrHandler = new JSONAddrHandler(MapActivity.this);
                        jsonAddrHandler.obtainMessage(0, JSONString).sendToTarget();
                        Looper.loop();
                    }
                }.start();
            } else
            {
                addr = addrs.get(0);
                double geoLat = addr.getLatitude() * 1E6;
                double geoLng = addr.getLongitude() * 1E6;

                String locationName = addr.getFeatureName()
                        + " - "
                        + addr.getAdminArea()
                        + " (" + addr.getCountryName()
                        + ")";

                Log.i("Location Found", locationName);
                Log.i("Location Found", addr.getLatitude() + "," + addr.getLongitude());

                putSearchMarker(geoLat, geoLng, locationName);

                typingText.setText("");
            }
        } else {
            toastShort("Your Input is Empty!!");
        }
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private String getAddressByGoogleApi(String input)
    {
        String language = getLocaleLanguage();
        Log.i("Language", language);
        Log.i("Google Address API",
                "Using Google Address API!\n" +
                        "Using Google Address API!\n" +
                        "Using Google Address API!");

        HttpGet httpGet = new HttpGet
                ("http://maps.google.com/maps/api/geocode/json?address=" + input + "&sensor=true&language=" + language);
        try
        {
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);

            if (httpResponse.getStatusLine().getStatusCode() == 200)
                return EntityUtils.toString(httpResponse.getEntity());
        }
        catch (ClientProtocolException e)
        {
            Log.e("getLocationFiled", e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.e("getLocationFiled", e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            Log.e("getLocationFiled", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void putSearchMarker(double lat, double lng, String input)
    {
        int markerWidth;
        int markerHeight;

        if (screenSize < 6.5)
        {
            if (screenWidth >= 480 && screenWidth < 720)
            {
                markerWidth = 40;
                markerHeight = 45;
            }
            else if (screenWidth >= 720 && screenWidth < 800)
            {
                markerWidth = 50;
                markerHeight = 55;
            }
            else
            {
                markerWidth = 70;
                markerHeight = 75;
            }
        }
        else {
            if (screenWidth <= 800)
            {
                markerWidth = 55;
                markerHeight = 60;
            } else
            {
                markerWidth = 70;
                markerHeight = 75;
            }
        }

        Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.map_green_marker_icon);
        Log.i("MarkerBitmap", "Width = " + markerBitmap.getWidth() + " Height = " + markerBitmap.getHeight());
        Drawable marker = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap
                (markerBitmap, markerWidth, markerHeight, true));

        mapView.getOverlays().remove(searchOverlay);
        searchOverlay = new SearchOverlay(marker, this);

        GP = new GeoPoint((int) lat, (int) lng);

        searchOverlay.setPoint(GP, "Your searching result", input);
        searchOverlay.finish();
        mapView.getOverlays().add(searchOverlay);

        mapMove((int)lat, (int)lng);

        mapView.invalidate();
    }

    private void GPSinit()
    {
        if (requestGPS)
        {
            if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                AlertDialog.Builder infoDialog = new AlertDialog.Builder(this);

                TextView title = new TextView(this);
                title.setText("Increase Accuracy");
                title.setTextColor(getResources().getColor(R.color.md_blue_A700));
                title.setGravity(Gravity.CENTER);
                title.setPadding(0, 20, 0, 20);

                if (screenSize >= 6.5)
                {
                    title.setTextSize(30);
                    infoDialog.setCustomTitle(title);
                } else {
                    title.setTextSize(22);
                    infoDialog.setCustomTitle(title);
                }
                infoDialog
                        .setMessage("Turn on the GPS to improve location accuracy!")
                        .setCancelable(false)
                        .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                findMyLocation();
                            }
                        })
                        .setNegativeButton("NO!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                requestGPS = false;
                                findMyLocation();
                            }
                        });
                AlertDialog dialog = infoDialog.create();
                dialog.show();

                dialog.getWindow().getAttributes();

                TextView msgText = (TextView) dialog.findViewById(android.R.id.message);
                Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                positive.setTextColor(getResources().getColor(R.color.md_orange_900));
                negative.setTextColor(getResources().getColor(R.color.md_brown_600));

                if (screenSize >= 6.5)
                {
                    msgText.setTextSize(28);
                    msgText.setPadding(10, 15, 10, 15);
                    positive.setTextSize(28);
                    negative.setTextSize(28);

                } else {
                    msgText.setTextSize(18);
                    msgText.setPadding(10, 15, 10, 15);
                    positive.setTextSize(18);
                    negative.setTextSize(18);
                }
            }
            else {
                findMyLocation();
                enableTool = true;
            }
        }
        else {
            findMyLocation();
            enableTool = true;
            Log.i("onState", "First Enable!");
        }
    }

    public void routeFromSearchPoint(String searchLat, String searchLng)
    {
        if ((""+myLocation).equals("null") && locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            toastLong("Unable to get the position value! Please wait for locating or turn off GPS.");
        else {
            double lat = myLocation.getLatitude();
            double lng = myLocation.getLongitude();
            Log.i("MyPosition", ""+lat +" "+ lng);

            mapMove((int)(lat * 1E6),
                    (int)(lng * 1E6));

            new GoogleDirection().execute(lat + "," + lng, searchLat + "," + searchLng);
            loadingBarRun();
        }
    }

    private class GoogleDirection extends AsyncTask<String, Integer, String>
    {
        private final String mapAPI =
                "http://maps.google.com/maps/api/directions/json?" + "origin={0}&destination={1}&language={2}&sensor=true";
        private String from;
        private String to;
        private String language;
        private String POLY;

        @Override
        protected String doInBackground(String...params)
        {
            if (params.length < 0)
                return null;

            from = params[0];
            to = params[1];
            language = getLocaleLanguage();

            String Url = MessageFormat.format(mapAPI, from, to, language);
            Log.i("mapUrl", Url);

            HttpGet get = new HttpGet(Url);
            String strResult;

            try
            {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

                HttpClient HC = new DefaultHttpClient(httpParams);
                HttpResponse HR;
                HR = HC.execute(get);

                if (HR.getStatusLine().getStatusCode() == 200)
                {
                    strResult = EntityUtils.toString(HR.getEntity());

                    JSONObject jb = new JSONObject(strResult);
                    JSONArray ja = jb.getJSONArray("routes");
                    String status = jb.getString("status");

                    Log.i("Status", status);

                    if (status.contains("OK"))
                    {
                        String polyLine = ja.getJSONObject(0).getJSONObject("overview_polyline").getString("points");
                        if (polyLine.length() > 0)
                        {
                            POLY = polyLine;
                            return strResult;
                        }
                    } else {
                        POLY = "";
                    }
                }
            }
            catch(Exception e) {
                Log.e("mapUrlFailed", e.toString());
            }
            return null;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        protected void onPostExecute(String routeResult)
        {
            if (POLY.length() > 0)
            {
                ArrayList myPoints = (ArrayList) PolyHelper.decodePolyline(POLY);
                mapView.getOverlays().remove(drawOverlay);
                drawOverlay = new DrawOverlay(myPoints);
                mapView.getOverlays().add(drawOverlay);
                mapView.invalidate();
                getDirectionInfo(routeResult);
                loadingBarStop();
            } else {
                loadingBarStop();
                toastLong("Oops~Routed failed!!\nUnable to draw the path, it may caused by the sea crossing path.");
            }
        }
    }

    public void getDirectionInfo(String routeInJSON)
    {
        List<Map<String, String>> dirList = new ArrayList<>();
        List<String> infoList = new ArrayList<>();
        try
        {
            JSONObject jb = new JSONObject(routeInJSON);
            JSONArray routesArr = jb.getJSONArray("routes");
            JSONObject route = routesArr.getJSONObject(0);
            JSONArray legsArr = route.getJSONArray("legs");
            JSONObject leg = legsArr.getJSONObject(0);
            JSONArray stepsArr = leg.getJSONArray("steps");

            for (int i = 0; i < stepsArr.length(); i++)
            {
                JSONObject singleStep = stepsArr.getJSONObject(i);
                JSONObject distance = singleStep.getJSONObject("distance");
                JSONObject duration = singleStep.getJSONObject("duration");
                JSONObject startLocation = singleStep.getJSONObject("start_location");

                String path = singleStep.getString("html_instructions").replace("<b>", "");

                Map<String, String> listItem = new HashMap<>();

                path = path.replace("</b>", "");
                path = (i+1) + "-" + path;
                if (path.contains("<div"))
                {
                    String pathGoOn = path.substring(path.indexOf(">")+1, path.lastIndexOf("<"));
                    if (pathGoOn.contains("div"))
                    {
                        StringBuilder clearGoOn = new StringBuilder(pathGoOn);
                        clearGoOn.delete(pathGoOn.indexOf("<"), pathGoOn.lastIndexOf(">"));
                        pathGoOn = clearGoOn.toString().replace(">", "-->");
                    }
                    Log.i("pathGoOn", pathGoOn);

                    listItem.put("pathGoOn", pathGoOn);

                    StringBuilder pathClear = new StringBuilder(path);
                    pathClear.delete(path.indexOf("<"), path.lastIndexOf(">")+1);
                    path = pathClear.toString();
                }

                Log.i("Path", path);

                String disValue = distance.getString("text");
                double dis = Double.parseDouble(disValue.substring(0, disValue.indexOf(" ")));
                if (dis < 1)
                    disValue = distance.getString("value") + " 公尺";
                listItem.put("disText", disValue);
                listItem.put("durText", duration.getString("text"));
                listItem.put("startLat", startLocation.getString("lat"));
                listItem.put("startLng", startLocation.getString("lng"));

                listItem.put("path", path);

                dirList.add(listItem);
            }
            JSONObject Dis = leg.getJSONObject("distance");
            JSONObject Dur = leg.getJSONObject("duration");
            String Destination = leg.getString("end_address");
            String Duration = Dur.getString("text");
            String Distance = Dis.getString("text");
            String Summary = route.getString("summary");

            double dis = Double.parseDouble(Distance.substring(0, Distance.indexOf(" ")));
            if (dis < 1)
                Distance = Dis.getString("value") + " 公尺";

            infoList.add(Destination);	//0
            infoList.add(Duration);		//1
            infoList.add(Distance);		//2
            infoList.add(Summary);		//3

            //directionList = dirList;
            //pathInfoList = infoList;

            //directionPop();
        }
        catch (JSONException e)
        {
            Log.e("DirectionFailed", e.getMessage());
        }
    }

    public void mapMove(int lat, int lng)
    {
        GP = new GeoPoint(lat, lng);

        mapControl.animateTo(GP);

        if (mapView.getZoomLevel() < 17)
            mapControl.setZoom(17);

        /*
        if (alreadyPop)
        {
            if (popUp.isShowing() && mapStatus.equals("full"))
            {
                GP = new GeoPoint((int) (lat*0.9995),(int) (lng*0.99988));
                mapControl.animateTo(GP);
            }
        }
        mapFocusMove = false;
        */
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        providerType = locManager.getBestProvider(criteria, true);
        //locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
        //locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        locManager.requestLocationUpdates(providerType, 1000, 1, this);
        //if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))

        if (enableTool)
        {
            myLayer.enableMyLocation();
            myLayer.enableCompass();
            Log.i("ProviderType", providerType);
            Log.i("onState", "Resume");
        }
        else {
            GPSinit();
            Log.i("onState", "Resume else");
        }
        toastShort("LocationProvider : " + providerType);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (enableTool)
        {
            locManager.removeUpdates(this);
            /*
            if (directionList.size() != 0)
            {
                DirectionAdapter dirAdapter = new DirectionAdapter(this, directionList);
                dirAdapter.removeUpdate();
            }
            */
            myLayer.disableMyLocation();
            myLayer.disableCompass();
            Log.i("onState", "Pause");
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.v("mapLocation", location.toString());
        myLocation = location;

        /*
        if (countable && !(""+myLocation).equals("null"))
        {
            for (Poi poi : pois)
            {
                poi.setDistance(distance(location.getLatitude(),
                        location.getLongitude(),
                        poi.getLatitude(),
                        poi.getLongitude()));
            }
            distanceSort(pois);
            setNearestView();
        }
        if (mapFocusMove)
        {
            GP = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
            mapControl.animateTo(GP);

            mapView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    mapFocusMove = false;
                    return false;
                }
            });
        }
        */
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        toastShort("EnableProvider : " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        toastShort("DisabledProvider : " + provider);
    }

    public void toastShort (String message)
    {
        View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
        TextView toastText = (TextView) toastRoot.findViewById(R.id.myToast);
        toastText.setText(message);

        Toast toastStart = new Toast(this);
        toastStart.setGravity(Gravity.BOTTOM, 0, 60);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
    public void toastLong (String message)
    {
        View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
        TextView toastText = (TextView) toastRoot.findViewById(R.id.myToast);
        toastText.setText(message);

        Toast toastStart = new Toast(this);
        toastStart.setGravity(Gravity.BOTTOM, 0, 60);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    private void loadingBarRun()
    {
        ProgressBar loadingPlan = (ProgressBar) findViewById(R.id.loadingStatus);
        loadingPlan.setVisibility(View.VISIBLE);
    }
    private void loadingBarStop()
    {
        ProgressBar loadingPlan = (ProgressBar) findViewById(R.id.loadingStatus);
        loadingPlan.setVisibility(View.GONE);
    }

    private String getLocaleLanguage()
    {
        Locale locale = Locale.getDefault();
        return String.format("%s-%s", locale.getLanguage(), locale.getCountry());
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}

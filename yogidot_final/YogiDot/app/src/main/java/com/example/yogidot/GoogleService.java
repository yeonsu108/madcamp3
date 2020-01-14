package com.example.yogidot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;



public class GoogleService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    Double latitude, longitude = null;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 1000 * 10;


    public GoogleService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 1000 * 15, notify_interval);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
        } else {
            if (isNetworkEnable && isGPSEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }
            } else if (isGPSEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }
            }
        }
    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1004", "Yogidot", importance);
            channel.setDescription("Please");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void fn_update(Location location) {
        String Latitude = latitude.toString();
        String Longitude = longitude.toString();

        new urlTask().execute("getWish", MainActivity.uid, "/" + Latitude + "/" + Longitude + "/300");

//        if (MainActivity.yeogi == "[]" || MainActivity.yeogi == null) {
//        }else {
        Log.e("dfklas", " "+MainActivity.yeogi);
        JSONArray yeogi;
        int len = 0;
        String firstName="";
        boolean flag = true;
        try{
            yeogi = new JSONArray(MainActivity.yeogi);
            len = yeogi.length();
            JSONObject a =  (JSONObject)  yeogi.get(0);
            firstName = a.getString("addr");
        } catch (Exception e){
            flag = false;
        }
        if(flag) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "1004").setSmallIcon(R.drawable.yogidot_icon)
                    .setContentTitle("요기닷!").setContentText(firstName + "을 포함한 " + len + "개의 찜이 근처에 있어요!").setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(1004, builder.build());
        }
//        }
    }
}



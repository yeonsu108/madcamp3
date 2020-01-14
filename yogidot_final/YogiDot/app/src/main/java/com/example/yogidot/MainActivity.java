package com.example.yogidot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    static String uid = null;
    static String wishList = null;
    static String yeogi = null;

    // GoogleService
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    Double latitude,longitude;
    Geocoder geocoder;
    ImageButton btn_start;
    ImageButton btn_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start =  findViewById(R.id.btn_start);
        btn_map = findViewById(R.id.btn_map);

        uid = getSharedPreferences("uid", MODE_PRIVATE).getString("uid", "");
        if (uid.length() == 0)
            new urlTask().execute("getUser", "null", "");
        else
            new urlTask().execute("getUser", uid, "");

        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        medit = mPref.edit();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boolean_permission) {

                    if (mPref.getString("service", "").matches("")) {
                        medit.putString("service", "service").commit();

                        Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                        intent.putExtra("UID", uid);
                        startService(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Service is already running", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enable the gps", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fn_permission();


        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid != null)
                    goToMap();                                                // Map으로 넘어가는 함수 실행!
            }
        });


        // 외부앱에서 url 보내면 SiteToString으로 넘어가는거야~!
        Intent stsintent = getIntent();
        String action = stsintent.getAction();
        String type = stsintent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {                                    // 가져온 uri 가 사이트
                goToSTS(stsintent);                                                // 넘어가는 함수 실행!
            }
        } else  {
        }



    }

    @Override
    protected void onStop() {
        super.onStop();

        // Activity가 종료되기 전에 저장한다. SharedPreferences를 uid(파일 이름), 기본모드로 설정
        SharedPreferences sharedPreferences = getSharedPreferences("uid", MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", uid); // key, value를 이용하여 저장하는 형태

        editor.commit();
    }


    void goToSTS(@NotNull Intent intent) {
        String site = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (site != null) {
            Intent stsintent = new Intent(this, SiteToString.class);
            stsintent.putExtra("SITE", site);
            startActivity(stsintent);
        }
    }

    void goToMap() {
        Intent mapIntent = new Intent(this, MapViewActivity.class);
        mapIntent.putExtra("UID", uid);
        startActivity(mapIntent);
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {


            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}

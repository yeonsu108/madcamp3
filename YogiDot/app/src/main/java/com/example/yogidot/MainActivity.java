package com.example.yogidot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    static String uid = null;
    static String wishList = null;
    static String yeogi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        uid = getSharedPreferences("uid", MODE_PRIVATE).getString("uid", "");
        if (uid.length() == 0)
            new urlTask().execute("get", "http://192.249.19.254:6380/user/null", "");
        else
            new urlTask().execute("get", "http://192.249.19.254:6380/user/" + uid, "");


        // 외부앱에서 url 보내면 SiteToString으로 넘어가는거야~!
        Intent stsintent = getIntent();
        String action = stsintent.getAction();
        String type = stsintent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {                                    // 가져온 uri 가 사이트
                goToSTS(stsintent);                                                // 넘어가는 함수 실행!
            }
        } else  {
            if (uid != null)
                goToMap();                                                // Map으로 넘어가는 함수 실행!
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
}

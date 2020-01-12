package com.example.yogidot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    static String uid = null;
    static String wishList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createNotificationChannel();
        Intent alarmintent = new Intent(MainActivity.this,ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmintent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 15, 32, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


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
                goToSTS(stsintent);                                                // STS로 넘어가자!
            }
        } else  {                                                           // uri 도 없고 uid 생성 되있으면
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

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String description = "suggest restaurant";
            NotificationChannel channel = new NotificationChannel("yogidot", "push", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

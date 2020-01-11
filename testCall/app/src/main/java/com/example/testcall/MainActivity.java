package com.example.testcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    static String uid = null;
    static String wishList = null;
    static String yeogi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uid = getSharedPreferences("uid",MODE_PRIVATE).getString("uid","");
        if(uid.length() == 0) new urlTask().execute("getUser", "null", "");
        else new urlTask().execute("getUser", uid, "");
    }
    @Override
    protected void onStop() {
        super.onStop();

        // Activity가 종료되기 전에 저장한다. SharedPreferences를 uid(파일 이름), 기본모드로 설정
        SharedPreferences sharedPreferences = getSharedPreferences("uid",MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid",uid); // key, value를 이용하여 저장하는 형태

        editor.commit();
    }
}

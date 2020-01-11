package com.example.testcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String uid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uid = getSharedPreferences("uid",MODE_PRIVATE).getString("uid","");


        Log.e("print random number: ", ""+uid);
//        new urlTask().execute("get", "http://192.249.19.254:6380/user", "");
//        new urlTask().execute("delete", "http://192.249.19.254:6380/user/5e18637707ae272f06440472", "");
//        new urlTask().execute("get", "http://192.249.19.254:6380/user", "");

        //uid라는 파일을 찾아 text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환

        if(uid.length() == 0) Log.e("set txt : ", "uid is null");
        else Log.e("set txt : ", uid);

        uid = "getuid";
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

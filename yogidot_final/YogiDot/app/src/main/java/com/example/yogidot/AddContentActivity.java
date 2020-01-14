package com.example.yogidot;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AddContentActivity extends AppCompatActivity {

    public String[] arrStr;
    static String place_id;
    ImageButton button;
    ArrayList<String> latlng = new ArrayList<>();
//    String uid = getSharedPreferences("uid", MODE_PRIVATE).getString("uid", "");

    boolean flag = true;

    String eval;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontent);
        button = findViewById(R.id.save);

        Intent intent = getIntent();
        eval = intent.getStringExtra("EVALUATION");
        if (eval != null) {
            arrStr = eval.split("\\s|\\.|,|#|null");
            ChipGroup chipGroup = findViewById(R.id.chipgroup);

            for (int i = 0; i < arrStr.length; i++) {
                Chip chip = new Chip(this); // Must contain context in parameter
                chip.setText(arrStr[i]);
                chip.setId(i);
                chip.setCheckable(true);
                chip.setTextAppearanceResource(R.style.ChipTextStyle);
                chip.setChipBackgroundColorResource(R.color.colorChip);
                chipGroup.addView(chip);
            }

            chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(ChipGroup chipGroup, int i) {
                    Chip chip = chipGroup.findViewById(i);
                    if(chip != null){
                        text=chip.getText().toString();
                        EditText et=(EditText)findViewById(R.id.autocomplete);
                        int start = Math.max(et.getSelectionStart(), 0);
                        int end = Math.max(et.getSelectionEnd(), 0);
                        et.getText().replace(Math.min(start, end), Math.max(start, end),text);
                    }
                }
            });
        }

        AutoCompleteTextView autoCompleteTextView=findViewById(R.id.autocomplete);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(AddContentActivity.this,android.R.layout.simple_list_item_1));

        button.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                new Getlatlng().execute(PlaceAutoSuggestAdapter.p_id);

//                while(flag){};
//                Log.e("latlng", latlng.toString());
//
//                String lat=latlng.get(0);
//                String lng=latlng.get(1);
//
//                Log.e("get Text >>>>>>", lat);
//                Log.e("get Text >>>>>>", lng);
//                Log.e("url >>>>>>>>>>", SiteToString.htmlPageUrl);
//
//              new urlTask().execute("postWish",MainActivity.uid,SiteToString.htmlPageUrl,PlaceAutoSuggestAdapter.ad_save,lat,lng);
 //               new urlTask().execute("postWish",MainActivity.uid,"test message");

                Intent intent =new Intent(AddContentActivity.this, MapViewActivity.class);
                startActivity(intent);
            }
        });
    }
    class Getlatlng extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String input = strings[0];
            HttpURLConnection connection=null;
            StringBuilder jsonResult=new StringBuilder();
            try {
                StringBuilder sb=new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?");
                sb.append("place_id="+input);
                sb.append("&key=AIzaSyCUVnTPQsiLIBmg3RIRClu78bFJ9TaG0Q8");
                URL url =new URL(sb.toString());
                connection=(HttpURLConnection)url.openConnection();
                InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream());

                int read;

                char[] buff=new char[1024];
                while((read=inputStreamReader.read(buff))!=-1){
                    jsonResult.append(buff,0,read);
                }
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            finally{
                if(connection!=null){
                    connection.disconnect();
                }
            }

            try{
                Log.e(">>>>>>>>>>>>>>>", jsonResult.toString());
                JSONObject jsonObject=new JSONObject(jsonResult.toString());
                JSONObject result=jsonObject.getJSONArray("results").getJSONObject(0);
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                latlng.add(location.getString("lat"));
                latlng.add(location.getString("lng"));
                flag = false;
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return latlng.toString();
        }
        @Override
        protected void onPostExecute(String str){
            super.onPostExecute(str);
            new urlTask().execute("postWish",MainActivity.uid,SiteToString.htmlPageUrl,PlaceAutoSuggestAdapter.ad_save,latlng.get(0),latlng.get(1));
            return;
        }
    }
}

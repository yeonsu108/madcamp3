package com.example.yogidot.models;

import android.util.JsonReader;
import android.util.Log;

import com.example.yogidot.placearray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Getlatlng {
    public ArrayList<String> goecode(String input){
        ArrayList <String> arrayList=new ArrayList();
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
            arrayList.add(location.getString("lat"));
            arrayList.add(location.getString("lng"));
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return arrayList;
    }
}

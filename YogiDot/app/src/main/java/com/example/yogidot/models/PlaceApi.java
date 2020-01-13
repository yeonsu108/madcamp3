package com.example.yogidot.models;

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

public class PlaceApi {
    public ArrayList<placearray> autoComplete(String input){
        ArrayList <placearray> arrayList=new ArrayList();
        HttpURLConnection connection=null;
        StringBuilder jsonResult=new StringBuilder();
        try {
            StringBuilder sb=new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
            sb.append("input="+input);
            sb.append("&key=AIzaSyCUVnTPQsiLIBmg3RIRClu78bFJ9TaG0Q8&language=ko");
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
            JSONArray prediction=jsonObject.getJSONArray("predictions");
            for(int i=0;i<prediction.length();i++){
                placearray data= new placearray(prediction.getJSONObject(i).getString("description"),prediction.getJSONObject(i).getString("place_id"));
                arrayList.add(data);
                Log.e("get Text >>>>>>", prediction.getJSONObject(i).getString("place_id"));

            }
    }
        catch(JSONException e){
            e.printStackTrace();
        }
        return arrayList;
    }
}

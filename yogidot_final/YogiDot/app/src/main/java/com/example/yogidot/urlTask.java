package com.example.yogidot;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public final class urlTask extends AsyncTask<String,Void,Void> {

    @Override
    protected Void doInBackground(String... strings) {
        String method = strings[0];
        String uid = strings[1];
        String message = strings[2];
        if(method == "getUser"){
            getUser("/user/"+uid);
        } else if(method == "deleteUser"){
            delete("/user"+uid);
        } else if (method == "getWish"){
            getWish("/wish/"+uid, message);
        } else if(method == "postWish"){
            JSONObject jsonMessage = new JSONObject();
            try {
                jsonMessage.put("url", strings[2]);
                jsonMessage.put("addr", strings[3]);
                jsonMessage.put("lat", strings[4]);
                jsonMessage.put("lng", strings[5]);
            }catch(Exception e){
                Log.e("Exception", e.toString());
                //message = "{\"id\":\"" + uid +"\",\"" + "\"wish\": ["+message+"]}";
            }
            String jsonString = jsonMessage.toString();
            Log.e("jsonString ", jsonString);
            Log.e("uid in postWish", uid);

            post("/wish/"+uid, jsonString);
        } else if(method == "deleteWish"){
            delete("/wish/"+uid+"/"+message);
        }
        return null;
    }

    public void getUser(String strUrl){
        try {
            String sb = get("http://192.249.19.254:6380"+strUrl);
            JSONObject res = new JSONObject(sb);
            MainActivity.uid = res.getString("_id");
            MainActivity.wishList = res.getString("wishList");
            Log.e("uid>>>>>>>>>>>>>>", MainActivity.uid);
            Log.e("wishlist>>>>>>>>>>>>>>", MainActivity.wishList);
        } catch (Exception e){
            Log.e("getUser", e.toString());
        }
    }
    public void getWish(String strUrl, String message){
        try {
            MainActivity.yeogi = get("http://192.249.19.254:6380"+strUrl+message);
            Log.e("wishlist in getWish", MainActivity.yeogi);
        } catch (Exception e){
            Log.e("getWish", e.toString());
        }
    }

    public String get(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정

            con.setRequestMethod("GET");

            con.setDoOutput(false);

            StringBuilder sb = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Stream을 처리해줘야 하는 귀찮음이 있음.
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                System.out.println("" + sb.toString());
                return sb.toString();
            } else {
                System.out.println(con.getResponseMessage());
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return null;
    }
    public void post(String strUrl, String jsonMessage){
        try {
            URL url = new URL("http://192.249.19.254:6380"+strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정

            con.setRequestMethod("POST");

            //json으로 message를 전달하고자 할 때
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true); //POST 데이터를 OutputStream으로 넘겨 주겠다는 설정
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(jsonMessage); //json 형식의 message 전달
            wr.flush();

            StringBuilder sb = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Stream을 처리해줘야 하는 귀찮음이 있음.
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                System.out.println("" + sb.toString());
            } else {
                System.out.println(con.getResponseMessage());
            }
        } catch (Exception e){
            System.err.println(e.toString());
        }
    }

    public void delete(String strUrl){
        try {
            URL url = new URL("http://192.249.19.254:6380"+strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestMethod("DELETE");

            StringBuilder sb = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Stream을 처리해줘야 하는 귀찮음이 있음.
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                System.out.println("" + sb.toString());
            } else {
                System.out.println(con.getResponseMessage());
            }
        } catch (Exception e){
            System.err.println(e.toString());
        }
    }
}
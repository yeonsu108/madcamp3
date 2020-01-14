package com.example.yogidot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowlistActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showlist);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.yogidot_toolicon);

        //final ImageButton delete = findViewById(R.id.d_button);
        ImageButton plus=findViewById(R.id.p_button);

        plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ShowlistActivity.this, AddContentActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShowlistActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final ArrayList<showlist> wishlist = new ArrayList<>();
        try {
            new urlTask().execute("getUser", MainActivity.uid, "");
            JSONArray wisharray = new JSONArray(MainActivity.wishList);
            Log.e("wishlist", wisharray.toString());
            for (int i = 0; i < wisharray.length(); i++) {
                JSONObject eachlist = wisharray.getJSONObject(i);
                showlist data = new showlist(eachlist.getString("addr"), eachlist.getString("url"));
                wishlist.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list_recyclerAdapter list_adapter = new list_recyclerAdapter(ShowlistActivity.this, wishlist);
        mRecyclerView.setAdapter(list_adapter);
    }

}

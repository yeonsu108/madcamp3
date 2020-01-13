package com.example.yogidot;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.yogidot.models.Getlatlng;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class AddContentActivity extends AppCompatActivity {

    public String[] arrStr;
    static String place_id;

    String eval;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontent);
        Button button = findViewById(R.id.save);

        Intent intent = getIntent();
        eval = intent.getStringExtra("EVALUATION");
        if (eval != null) {
            arrStr = eval.split("\\s");
            ChipGroup chipGroup = findViewById(R.id.chipgroup);

            for (int i = 0; i < arrStr.length; i++) {
                Chip chip = new Chip(this); // Must contain context in parameter
                chip.setText(arrStr[i]);
                chip.setId(i);
                chip.setCheckable(true);
                chip.setTextAppearanceResource(R.style.ChipTextStyle);
                //chip.setChipBackgroundColorResource(R.color.background);
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
                ArrayList<String> latlng;

                Getlatlng getlatlng=new Getlatlng();
                latlng= getlatlng.goecode(PlaceAutoSuggestAdapter.p_id);

                Log.e("latlng", latlng.toString());

                String lat=latlng.get(0);
                String lng=latlng.get(1);

                Log.e("get Text >>>>>>", lat);
                Log.e("get Text >>>>>>", lng);
                Log.e("url >>>>>>>>>>", SiteToString.htmlPageUrl);

//                new urlTask().execute("postWish",MainActivity.uid,"{\"url\":\""+SiteToString.htmlPageUrl+"\",\"lat\":\""+lat+"\",\"lng\":\""+lng+"\"}");
                new urlTask().execute("postWish",MainActivity.uid,"test message");
            }
        });

    }

}

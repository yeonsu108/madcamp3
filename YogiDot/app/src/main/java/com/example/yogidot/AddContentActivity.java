package com.example.yogidot;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class AddContentActivity extends AppCompatActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,

        GoogleApiClient.ConnectionCallbacks, DialogInterface.OnClickListener
{

    public String[] arrStr;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    /**
     * google place api 를 위한 변수들 START
     **/
    private GoogleApiClient googleApiClient;
    private RecyclerView rvAutocomplateKeyword;
    private LinearLayoutManager llm;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));
    private EditText edSearch = null;
    String eval;
    /**
     * google place api 를 위한 변수들 END
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontent);

        Intent intent = getIntent();
        eval = intent.getStringExtra("EVALUATION");
        if (eval != null) {
            arrStr = eval.split("\\s");
            ChipGroup chipGroup = findViewById(R.id.chipgroup);

            for (int i = 0; i < arrStr.length; i++) {
                Chip chip = new Chip(this); // Must contain context in parameter
                chip.setText(arrStr[i]);
                chip.setCheckable(true);
                chip.setTextAppearanceResource(R.style.ChipTextStyle);
                //chip.setChipBackgroundColorResource(R.color.background);
                chipGroup.addView(chip);
            }
        }
        init();

    }

    private void init() {
        // 장소 찾기 초기화
        initPlace();
    }

    private void initPlace() {
        this.googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();



        this.rvAutocomplateKeyword = (RecyclerView) findViewById(R.id.list_search);
        this.rvAutocomplateKeyword.setHasFixedSize(true);
        this.llm = new LinearLayoutManager(AddContentActivity.this);
        this.rvAutocomplateKeyword.setLayoutManager(llm);


        this.edSearch = (EditText) findViewById(R.id.ed_search);
        this.placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, R.layout.item_search,
                googleApiClient, BOUNDS_INDIA, null);
        this.rvAutocomplateKeyword.setAdapter(placeAutocompleteAdapter);

        // 글자를 입력하면 place api를 요청한다.

        this.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }



            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    if (placeAutocompleteAdapter != null) {
                        rvAutocomplateKeyword.setVisibility(View.VISIBLE);
                    }

                } else {
                    if (placeAutocompleteAdapter != null) {
                        placeAutocompleteAdapter.clearList();
                        rvAutocomplateKeyword.setVisibility(View.GONE);
                    }
                }
                if (!s.toString().equals("") && googleApiClient.isConnected()) {
                    placeAutocompleteAdapter.getFilter().filter(s.toString());
                } else if (!googleApiClient.isConnected()) {
                    Log.e("", "NOT CONNECTED");
                }

            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }*/

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> resultList, int position) {
        if (resultList != null) {

            try {
                final String placeId = String.valueOf(resultList.get(position).placeId);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(googleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getCount() == 1) {
                            // 이곳에서 키워드를 선택한 데이터를 처리한다.
                            Location location = new Location(places.get(0).getName().toString());
                            location.setLatitude(places.get(0).getLatLng().latitude);
                            location.setLongitude(places.get(0).getLatLng().longitude);

                        } else {
                            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {

            } finally {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        placeAutocompleteAdapter.clearList();
                        rvAutocomplateKeyword.setVisibility(View.GONE);
                    }
                });
            }
        }
    }


    @Override
    public void onStart() {
        this.googleApiClient.connect();
        super.onStart();
    }


    @Override
    public void onStop() {
        this.googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
}

package com.example.karol.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Locale;

public class infoAboutBeacon extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_about_beacon);
        Button showOnMapsButton = (Button) findViewById(R.id.showOnMapsButton);
        TextView idTextView = (TextView) findViewById(R.id.beaconIDtextView);
        final double longtitude;
        double latitude = 0;
        String id = "";
        id = getIntent().getStringExtra("beaconInfo");
        longtitude = getIntent().getDoubleExtra("beaconLong",0);
        latitude = getIntent().getDoubleExtra("beaconLat",0);
        //ConnectionGet conn = new ConnectionGet();
        /*try {
            conn.getBeacon("abc",id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //longtitude = conn.lng;
        //latitude = conn.lat;

        idTextView.setText(id);
        final double finalLatitude = latitude;
        Log.i("DUPA01", "latitude: " + finalLatitude + " | longtitude: " + longtitude);
        showOnMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q="+finalLatitude+","+ longtitude, finalLatitude,longtitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }
}

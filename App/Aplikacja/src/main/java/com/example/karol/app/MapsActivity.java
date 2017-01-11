package com.example.karol.app;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //MainActivity ma;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //ArrayList<String> locations = (ArrayList<String>)getIntent().getSerializableExtra("mapsLocation");
        //Log.d("DUPA01", "Dane z intentu: " + locations.get(0) + " ; " + locations.get(1));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);  // This calls OnMapReady(..). (Asynchronously)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("DUPA01", "Maps ready");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng currLoc = new LatLng(MainActivity.mLastLocation.getLatitude(), MainActivity.mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currLoc).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
    }
    @Override
    public void onBackPressed(){
        setContentView(R.layout.activity_main);
    }
}

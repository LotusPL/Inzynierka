package com.example.karol.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BeaconActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public Beacons b = new Beacons();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        ArrayList<String> beacons = (ArrayList<String>)getIntent().getSerializableExtra("beaconList");
        final ListView beaconList = (ListView)findViewById(R.id.beaconListView);
        //TODO
        // Zmienić tę pętlę for na wybieranie z bazy danych.
        for(int i=0;i<beacons.size();i++){
            /*if(beacons.get(i).equals("dWlT")){
                beacons.set(i,"Miasteczko Studenckie AGH ("+beacons.get(i)+")");
            }
            if(beacons.get(i).equals("AQ1b")){
                beacons.set(i,"Budynek B5 - AGH ("+beacons.get(i)+")");
            }
            if(beacons.get(i).equals("W3op")){
                beacons.set(i,"Dom ("+beacons.get(i)+")");
            }*/
            beacons.set(i,b.recognition(beacons.get(i)));
        }


        ArrayAdapter<String> beaconAdapter = new ArrayAdapter<String>(this,R.layout.row,beacons);
        beaconList.setAdapter(beaconAdapter);
        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(BeaconActivity.this,"Działa",Toast.LENGTH_LONG);
                Log.i("DUPA01", "Coś jest nie tak ?");
                Intent infoAboutBeacon = new Intent(getApplicationContext(),infoAboutBeacon.class);
                Log.i("DUPA01", String.valueOf(beaconList.getItemAtPosition(i)));
                String tmp = beaconList.getItemAtPosition(i).toString();
                int ndx1=tmp.indexOf("(");
                int ndx2=tmp.indexOf(")");
                //Log.i("DUPA01", String.valueOf(ndx1) + " " + String.valueOf(ndx2) + " " + tmp.substring(ndx1+1,ndx2));
                Log.i("DUPA01", "latitude: " + String.valueOf(b.beaconLat(tmp.substring(ndx1+1,ndx2)) + " | longtitude: " + String.valueOf(b.beaconLong(tmp.substring(ndx1+1,ndx2)))));

                infoAboutBeacon.putExtra("beaconInfo", String.valueOf(beaconList.getItemAtPosition(i)));
                infoAboutBeacon.putExtra("beaconLong",b.beaconLong(tmp.substring(ndx1+1,ndx2)));
                infoAboutBeacon.putExtra("beaconLat",b.beaconLat(tmp.substring(ndx1+1,ndx2)));
                startActivity(infoAboutBeacon);

            }
        });
        //*/
        /*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        mRecycleView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);
        ArrayList<String> beacons = (ArrayList<String>)getIntent().getSerializableExtra("beaconList");
        ListView beaconList = (ListView)findViewById(R.id.beaconListView);
        ArrayAdapter<String> beaconAdapter = new ArrayAdapter<String>(this,R.layout.row,beacons);
        beaconList.setAdapter(beaconAdapter);
        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BeaconActivity.this,"Działa",Toast.LENGTH_LONG);

            }
        });*/
    }
}

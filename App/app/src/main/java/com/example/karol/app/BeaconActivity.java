package com.example.karol.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BeaconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        ArrayList<String> beacons = (ArrayList<String>)getIntent().getSerializableExtra("beaconList");
        ListView beaconList = (ListView)findViewById(R.id.beaconListView);
        //TODO
        // Zmienić tę pętlę for na wybieranie z bazy danych.
        for(int i=0;i<beacons.size();i++){
            if(beacons.get(i).equals("dWlT")){
                beacons.set(i,"Miasteczko Studenckie AGH ("+beacons.get(i)+")");
            }
            if(beacons.get(i).equals("AQ1b")){
                beacons.set(i,"Budynek B5 - AGH ("+beacons.get(i)+")");
            }
            if(beacons.get(i).equals("W3op")){
                beacons.set(i,"Dom ("+beacons.get(i)+")");
            }
        }
        /*

         */
        ArrayAdapter<String> beaconAdapter = new ArrayAdapter<String>(this,R.layout.row,beacons);
        beaconList.setAdapter(beaconAdapter);
        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BeaconActivity.this,"Działa",Toast.LENGTH_LONG);

            }
        });
    }
}

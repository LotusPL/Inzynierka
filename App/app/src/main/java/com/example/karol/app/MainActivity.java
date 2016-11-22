package com.example.karol.app;

import android.*;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.BreakIterator;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    public static GoogleApiClient mGoogleApiClient = null;
    public static Location mLastLocation;
    private ProximityManager proximityManager;

    public ArrayList<String> beacons = new ArrayList<String>();

    //##########################################################################################
    //TODO
    // - Przenieść wyszukiwanie beaconów do serwisu, żeby działało w tle
    // - dodać funkcję updatującą ArrayAdapter przy zmianie na liście beaconów
    // - Dodać stronę użytkownika
    // - Podłączyć do bazy danych
    //##########################################################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, MyIntentService.class);
        //ListView beaconView = (ListView)findViewById(R.id.beaconList);
        Button beaconsButton = (Button) findViewById(R.id.beaconsListButton);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button startServiceButton = (Button) findViewById(R.id.startService);
        Button stopServiceButton = (Button) findViewById(R.id.stopService);
        Button startBluetoothButton = (Button) findViewById(R.id.startBluetooth);
        Button stopBluetootheButton = (Button) findViewById(R.id.stopBluetooth);
        Button mapsButton = (Button) findViewById(R.id.mapsButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_login);

            }
        });
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(intent);
            }
        });
        beaconsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setContentView(R.layout.activity_beacon);
                Intent beaconActivity = new Intent(getApplicationContext(),BeaconActivity.class);
                beaconActivity.putExtra("beaconList",beacons);
                startActivity(beaconActivity);
            }
        });
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(intent);

            }
        });

        startBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothStart();
            }
        });

        stopBluetootheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert();

            }
        });

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_maps);
            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        KontaktSDK.initialize(this);
        proximityManager = new ProximityManager(this);
        proximityManager.setIBeaconListener(new IBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice iBeacon, IBeaconRegion region) {
                //Beacon discovered
                Log.i("DUPA01", "IBeacon discovered: " + iBeacon.getUniqueId());
                addBeacons(iBeacon.getUniqueId().toString());
            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
                //Beacons updated
            }

            @Override
            public void onIBeaconLost(IBeaconDevice iBeacon, IBeaconRegion region) {
                //Beacon lost
                //Log.i("DUPA01", "IBeacon lost: " + iBeacon.getPassword());
                Log.i("DUPA01", "IBeacon lost: " + iBeacon.getUniqueId());
                removeBeacons(iBeacon.getUniqueId().toString());
            }
        });

    }
    public void addBeacons(String name){
        boolean exist = false;
        for(int i = 0; i<beacons.size(); i++){
            if(beacons.get(i).equals(name)){
                exist = true;
            }
        }
        if(!exist){
            beacons.add(name);
        }

    }
    public void removeBeacons(String name){
        boolean exist = false;
        for(int i=0; i<beacons.size();i++){
            if(beacons.get(i).equals(name)){
                exist = true;
            }
        }
        if(exist){
            beacons.remove(name);
        }
    }
    protected void onStart() {
        mGoogleApiClient.connect();
        startScanning();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

/*    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                Log.i("DUPA01", "IBeacon discovered: " + ibeacon.getUniqueId());
            }
        };
    }

    private EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i("DUPA01", "Eddystone discovered: " + eddystone.getUniqueId());
            }
        };
    }*/



    public void alert() {
        final AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Czy chcesz wyłączyć BlueTooth ?");
        alertDlg.setTitle("WARNING");
        alertDlg.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bluetoothStop();
            }
        });
        alertDlg.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDlg.setCancelable(true);
        alertDlg.create().show();
    }

    public void bluetoothStart() {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (ba == null) {
            Toast.makeText(this, "To urządzenie nie posiada bluetooth!", Toast.LENGTH_LONG).show();
        } else {
            if (!ba.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    public void bluetoothStop() {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (ba != null) {
            if (ba.isEnabled()) {
                ba.disable();
            } else {
                Toast.makeText(this, "Bluetooth nie działa", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("DUPA01", "xxxxxxxxxx");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d("DUPA01", String.valueOf(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)));
            Log.d("DUPA01", String.valueOf(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)));
            Log.d("DUPA01", String.valueOf(PackageManager.PERMISSION_GRANTED));
            Log.d("DUPA01", String.valueOf(PackageManager.PERMISSION_DENIED));
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        Log.d("DUPA01",mLastLocation.getLatitude() + "; " + mLastLocation.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

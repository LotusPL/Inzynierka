package com.example.karol.app;

import java.util.HashMap;

/**
 * Created by Karol on 07.01.2017.
 */
public class Beacons {
    String str ="";
    double longtitude,latitude = 0;
    public HashMap beaconHashMap = new HashMap();
    public String recognition(String id){
        if(id.equals("dWlT")){
            str=("Miasteczko Studenckie AGH ("+id+")");
        }
        if(id.equals("AQ1b")){
            str=("Budynek B5 - AGH ("+id+")");
        }
        if(id.equals("W3op")){
            str=("Dom ("+id+")");
        }
        return str;
    }
    public double beaconLong(String id){
        if(id.equals("dWlT")){
            //str=("Miasteczko Studenckie AGH ("+id+")");
            longtitude = 19.8881403;
        }
        if(id.equals("AQ1b")){
            //str=("Budynek B5 - AGH ("+id+")");
            longtitude = 19.9093096;
        }
        if(id.equals("W3op")){
            //str=("Dom ("+id+")");
            longtitude = 19.889651;
        }
        return longtitude;
    }
    public double beaconLat(String id){
        if(id.equals("dWlT")){
            //str=("Miasteczko Studenckie AGH ("+id+")");
            latitude = 50.0704301;
        }
        if(id.equals("AQ1b")){
            //str=("Budynek B5 - AGH ("+id+")");
            latitude = 50.0678621;
        }
        if(id.equals("W3op")){
            //str=("Dom ("+id+")");
            latitude = 50.0743961;
        }
        return latitude;
    }
}

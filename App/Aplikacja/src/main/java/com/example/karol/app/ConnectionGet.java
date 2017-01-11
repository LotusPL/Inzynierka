package com.example.karol.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionGet {
    public String beaconID = "";
    public double lat,lng = 0;
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public void getBeacon(String apikey, String id) throws IOException, JSONException {
        String url = "http://localhost:8080/beacon/" + apikey + "/" + id;
        JSONObject json = readJsonFromUrl(url);
        System.out.println(json.toString());
        System.out.println(json.get("id"));

        beaconID = (String)json.get("ID");
        lat = (double)json.get("Lat");
        lng = (double)json.get("Lng");
    }

}
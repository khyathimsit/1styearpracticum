package com.example.wingman;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Wifi extends AppCompatActivity {
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        Log.d("WIFI", "Entered here");
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String in = getIntent().getStringExtra("value");
        if(in.equals("on")){
            wifiManager.setWifiEnabled(true);
        }
        if(in.equals("off")){
            wifiManager.setWifiEnabled(false);
        }
    }
}

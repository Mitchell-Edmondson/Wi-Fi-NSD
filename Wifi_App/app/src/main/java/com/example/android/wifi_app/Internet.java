package com.example.android.wifi_app;


import android.net.wifi.*;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * Created by Mitchell on 02/10/17.
 */

public class Internet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_layout);

        WifiManager manager = (WifiManager) super.getSystemService(WIFI_SERVICE);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mylayout);

        ArrayList<String> wifiList = GetListofWifis(manager);
        // TODO: 01/11/17 create sorted list by signal strength 

        for (int j = 0; j < wifiList.size(); j++) {
            Button button = new Button(this);
            linearLayout.addView(button);
            button.setText(String.valueOf(j + 1) + ": " + wifiList.get(j));
        }

    }

    private ArrayList<String> GetListofWifis(WifiManager manager) {
        //Have to have 2 arraylists rather than 1 object, because the object was
        //crashing the app for some reason
        ArrayList<String> wifiList = new ArrayList<String>();
        ArrayList<Integer> strength = new ArrayList<Integer>();
        ArrayList<String> finals = new ArrayList<String>();

        //Starts the scan
        manager.startScan();

        //List holding all the results in ScanResult objects
        List<ScanResult> list = manager.getScanResults();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mylayout);
        Button button = new Button(this);
        linearLayout.addView(button);
        button.setText("size = " + String.valueOf(list.size()));


        for (int i = 0; i < list.size(); i++) {
            //Get ScanResult object
            ScanResult result = list.get(i);
            //Get the SSID
            String SSID = getSSID(String.valueOf(list.get(i)));

            //Maybe find better way to check this
            //This check eliminates the hidden networks
            if (!SSID.equals("")) {
                //Then we have to check if we have already seen this SSID
                finals = inWifiList(SSID, wifiList, strength, result);
            }
        }
        return finals;
    }

    private ArrayList<String> inWifiList(String SSID, ArrayList<String> wifiList,
                                         ArrayList<Integer> strength, ScanResult result) {
        //Takes "n" time, have to loop through whole list, in case the
        //strongest frequency is at the bottom for a SSID. Boolean is used
        //to know if we have to add at the end or we found it in the loop
        boolean found = false;

        for (int i = 0; i < wifiList.size(); i++) {
            //If we find the SSID in the list and it has a higher frequency or we found one with a lower frequency
            if (SSID.equals(wifiList.get(i)) && (result.frequency > strength.get(i) || result.frequency < strength.get(i))) {
                if (result.frequency > strength.get(i)) {
                    //Replace that SSID with this one (Unecessary, same name)
                    wifiList.set(i, SSID);
                    //Replace that frequency with this one
                    strength.set(i, result.frequency);
                }
                found = true;
            }
        }

        if (!found) {
            //Adding SSID to list
            wifiList.add(SSID);
            strength.add(result.frequency);
        }
        return wifiList;
    }

    private String getSSID(String wifiEntry) {
        String[] parse = wifiEntry.split(", BSSID");
        String[] parse2 = parse[0].split(":");
        //Filtering out hidden networks, can't get their names
        if (parse2[1].length() > 1) {
            return parse2[1];
        }
        return "";
    }
}
package com.example.android.wifi_app;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getConnectedWifi();
    }
    private void getConnectedWifi()
    {
        TextView textView = (TextView) findViewById(R.id.currentWifi);
        WifiManager manager = (WifiManager) super.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        String name = wifiInfo.getSSID();
        textView.setText("Connected to: " +name);
    }

    public void SeeChat(View view)
    {

        //Intent intent = new Intent(this, Chat.class);
        Intent intent = new Intent(this, Nsd.class);
        startActivity(intent);
    }

    public void getInternet(View view)
    {
        //So far this activity just displays the router's ip address
        //want to make it so it displays the routers name
        Intent intent = new Intent(this, Internet.class);
        startActivity(intent);
    }

    public void startP2P(View view)
    {
        Intent intent = new Intent(this, WifiP2P.class);
        startActivity(intent);
    }
}

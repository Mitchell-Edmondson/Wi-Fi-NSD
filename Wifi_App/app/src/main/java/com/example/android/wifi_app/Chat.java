package com.example.android.wifi_app;

import android.content.Context;
import android.content.pm.ServiceInfo;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;


/**
 * Created by Mitchell on 02/11/17.
 */


public class Chat extends AppCompatActivity {
    NsdManager mNsdManager;
    NsdServiceInfo serviceInfo;
    //public final int port = 0;
    public String mServiceName = "_myapp._tcp.local";
    public static final String SERVICE_TYPE = "_http._tcp.local";
    private static final String TAG = "Chat";


    ServerSocket mServerSocket;
    int mLocalPort;


    //NsdManager.RegistrationListener mRegistrationListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.ResolveListener mResolveListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        initializeServerSocket();

    }

    public void initializeServerSocket() {
        // Initialize a server socket on the next available port.
        try {
            mServerSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store the chosen port.
        mLocalPort = mServerSocket.getLocalPort();
      //  TextView textView = (TextView) findViewById(R.id.textView);
      //  textView.setText(String.valueOf(mLocalPort));
        //registerService(mLocalPort);

        registerService(mLocalPort);

    }

    public void sendMessage(View view) {
        ScrollView scrollView = (ScrollView) findViewById(R.id.myscroll);
        EditText editText = (EditText) findViewById(R.id.editText);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mylinear);
        TextView textView = new TextView(this);
        linearLayout.addView(textView);

        textView.setText(editText.getText().toString());
        editText.setText("");
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

    public void registerService(int port) {
        serviceInfo = new NsdServiceInfo();

        serviceInfo.setServiceName("Mitchells-App");
        serviceInfo.setServiceType("_Mitchellss-App._tcp");
        serviceInfo.setPort(port);

        mNsdManager = (NsdManager)getSystemService(Context.NSD_SERVICE);

        new register().execute();


    }

    private class register extends AsyncTask<Void, Void, Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {

            mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
            return null;

        }
    }


    NsdManager.RegistrationListener  mRegistrationListener = new NsdManager.RegistrationListener() {

        @Override
        public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
            Log.d(TAG, "onRegistrationFailed");


        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
            Log.d(TAG, "onUNregistrationFailed");
        }

        @Override
        public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
            Log.d(TAG, "onServiceRegisted (It worked!) (I think)");
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
            Log.d(TAG, "onServiceUNRegisterded");
        }

    };


// initializeResolveListener();
        // initializeDiscoveryListener();
        // initializeRegistrationListener();



        //new register();
        //new discover();
        //new resolve();


    class Registration implements Runnable
    {
        @Override
        public void run()
        {

        }
    }
}


























































/*
     public class resolve extends AsyncTask<Void, Void, Void>
     {
         @Override
         protected Void doInBackground(Void... voids) {
             mNsdManager.resolveService(serviceInfo, mResolveListener);
             return null;
         }
     }

     public void initializeResolveListener()
     {
         final TextView textView = (TextView) findViewById(R.id.textView);
         mResolveListener = new NsdManager.ResolveListener() {
             @Override
             public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {

             }

             @Override
             public void onServiceResolved(NsdServiceInfo nsdServiceInfo)
             {
                mService = serviceInfo;
                 //int port = mService.getPort();
                 int port = mService.getPort();
                 InetAddress host = mService.getHost();


                 //Now we have this information we can connect with a standard socket
             }

         };
         textView.setText(String.valueOf(port));
     }

     private class discover extends AsyncTask<Void, Void, Void>
     {

         @Override
         protected Void doInBackground(Void... voids) {


             NsdManager.DiscoveryListener  mDiscoveryListener = new NsdManager.DiscoveryListener() {
                 @Override
                 public void onStartDiscoveryFailed(String s, int i) {
                     Log.d(TAG, "Failedsdfsd");

                 }

                 @Override
                 public void onStopDiscoveryFailed(String s, int i) {
                     Log.d(TAG, "Failedsdfsd2");

                 }

                 @Override
                 public void onDiscoveryStarted(String s) {
                     Log.d(TAG, "Failedsdfsd3");

                 }

                 @Override
                 public void onDiscoveryStopped(String s) {
                     Log.d(TAG, "Failedsdfsd4");
                 }

                 @Override
                 public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
                     Log.d(TAG, "Failedsdfsd5");
                 }

                 @Override
                 public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
                     Log.d(TAG, "Service discovery success" + nsdServiceInfo);
                 }
             };

             mNsdManager.discoverServices(mServiceName, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

             return null;
         }
     }


   //  public void initializeDiscoveryListener()
    // {
/*
       NsdManager.DiscoveryListener  mDiscoveryListener = new NsdManager.DiscoveryListener() {
             @Override
             public void onStartDiscoveryFailed(String s, int i) {
                Log.d(TAG, "Failedsdfsd");

             }

             @Override
             public void onStopDiscoveryFailed(String s, int i) {
                 Log.d(TAG, "Failedsdfsd2");

             }

             @Override
             public void onDiscoveryStarted(String s) {
                 Log.d(TAG, "Failedsdfsd3");

             }

             @Override
             public void onDiscoveryStopped(String s) {
                 Log.d(TAG, "Failedsdfsd4");
             }

             @Override
             public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
                 Log.d(TAG, "Failedsdfsd5");
             }

             @Override
             public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
                 Log.d(TAG, "Service discovery success" + nsdServiceInfo);
             }
         };
 **

     private class register extends AsyncTask<Void, Void, Void>
     {
         boolean found = false;

         @Override
         protected Void doInBackground(Void... voids) {

             registerService(port);
             return null;
         }
     }


     public void registerService(int port)
     {

         //Gets an instance of nsdmanager

        serviceInfo = new NsdServiceInfo();

         serviceInfo.setPort(port);
         serviceInfo.setServiceName(mServiceName);
         serviceInfo.setServiceType(SERVICE_TYPE);
         mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);



         mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
     }

//     public void initializeRegistrationListener()
//     {
//         final TextView textView = (TextView) findViewById(R.id.textView);
          NsdManager.RegistrationListener  mRegistrationListener = new NsdManager.RegistrationListener()
            {
                @Override
                public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
                    //mServiceName = nsdServiceInfo.getServiceName();
                    Log.d(TAG, "Failedsdfsd");

                }

                @Override
                public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
                    //mServiceName = nsdServiceInfo.getServiceName();
                    Log.d(TAG, "Failedsdfsd");
                }

                @Override
                public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
                    //mServiceName = nsdServiceInfo.getServiceName();
                    Log.d(TAG, "Failedsdfsd");
                }

                @Override
                public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
                    //mServiceName = nsdServiceInfo.getServiceName();
                    Log.d(TAG, "Failedsdfsd");
                }
            };



*/
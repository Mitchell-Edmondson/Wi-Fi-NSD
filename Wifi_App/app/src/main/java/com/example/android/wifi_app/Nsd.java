package com.example.android.wifi_app;

/**
 * Created by Mitchell on 10/11/17.
 */


import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import static android.os.Build.ID;


/**
 * Created by Mitchell on 02/11/17.
 */

//Todo mains
    /*Mains:
        Figure out the problem of registering a serbice with a name but when discovering it can't see the same service name.
        Have it so you can search for a specific service (discover)

        Less:
        Making it so you can edittext and register your own service name
        Having a toast pop up saying registering
     */

public class Nsd extends AppCompatActivity {
    NsdManager mNsdManager;
    //NsdServiceInfo serviceInfo;
    public String mServiceName = "myapp";// + ID;
    public String mDiscoverServiceName = "";
    //This searches for anything/everything
    public static final String SERVICE_TYPE = "_services._dns-sd._udp";
    public static final String httpServiceType = "_http._tcp.";
    public static final String tcplocalServiceType = "_tcp.local.";
    private static final String TAG = "Chat";
    int port = -1;


   ArrayList<NsdServiceInfo> servicelist = new ArrayList<NsdServiceInfo>();


    DiscoverMe d;
    RegisterMe r;
    ResolveMe c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_layout);
    }



    //After the service is registered we can attempt to connect to it
    public void connect(View view) {
        Log.d("LOL", "LOL2");
       /* TextView textView = (TextView) findViewById(R.id.resolve_textview);
        textView.setText("Host: " + serviceInfo.getServiceName() + " \n"
        + "Port: " + String.valueOf(serviceInfo.getPort()) + " \n"
                + "ServiceType: " + serviceInfo.getServiceType());
*/

        c = new ResolveMe();
        c.start();
    }


    public void initializeServerSocket(View view) {
        // Initialize a server socket on the next available port.
        ServerSocket mServerSocket = null;
        try {
            mServerSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store the chosen port.
        int mLocalPort = mServerSocket.getLocalPort();

        registerService(mLocalPort);

    }

    public void registerService(int port) {
        //Local NsdServiceInfo object
        //changed from local to a global, so that way if one device registers it,
        //another can discover and find it and connect
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        EditText editText = (EditText) findViewById(R.id.edittext);
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(String.valueOf(editText.getText()));
        serviceInfo.setServiceType(httpServiceType);

        TextView textView = (TextView) findViewById(R.id.register_textview);
        textView.setText("Registering: " + serviceInfo.getServiceName() +
                "\nServiceType: " + serviceInfo.getServiceType() +
                "\nPort: " + String.valueOf(port));

        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        r = new RegisterMe(serviceInfo);
        r.start();
    }


    private class RegisterMe implements Runnable {
        private NsdServiceInfo serviceInfo;

        public RegisterMe(NsdServiceInfo nsdServiceInfo) {
            this.serviceInfo = nsdServiceInfo;
        }

        @Override
        public void run() {
            Log.d(TAG, "Registering: " + serviceInfo.getServiceName() +
                    "\nServiceType: " + serviceInfo.getServiceType() +
                    "\nPort: " + String.valueOf(serviceInfo.getPort()));
            mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
        }

        public void start() {
            Thread thread = new Thread(this);
            thread.start();
        }

        public void interrupt() {
            Log.d(TAG, "Registering Thread stopped");
            return;
        }

    }

    //Stops the register thread and unregisters the service
    public void stopRegister(View view) {
        mNsdManager.unregisterService(mRegistrationListener);
        TextView textview = (TextView) findViewById(R.id.register_textview);
        textview.setText(mServiceName + " unregistered.");
        r.interrupt();
    }

    //Starts the discover thread and process
    public void startDiscover(View view) {
        //Clear the arraylist
        servicelist.clear();
        TextView textview = (TextView) findViewById(R.id.discover_textview);
        textview.setText("Starting discoverServices");
        // TODO: 21/11/17 clear the linearlayout in scrollview
        d = new DiscoverMe();
        d.start();
    }


    //Discover thread
    class DiscoverMe implements Runnable {
        @Override
        public void run() {
            //Searching for only http, returns back service name. Doing the global search can't find name
            mNsdManager.discoverServices(httpServiceType, NsdManager.PROTOCOL_DNS_SD, mDiscoverListener);
        }

        public void interrupt() {
            Log.d(TAG, "Discovery Thread Stopped");
            return;
        }

        public void start() {
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    //Stop discover thread and display all found services
    public void stopDiscover(View view) {
        mNsdManager.stopServiceDiscovery(mDiscoverListener);
        TextView textview = (TextView) findViewById(R.id.discover_textview);
        textview.setText("Stopped serviceDiscovery " + "\nFound " + servicelist.size() + " service(s):\n");

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mylinear);

        for (int i = 0; i < servicelist.size(); i++) {
            //textview.append(String.valueOf(i + 1) + ": " + servicelist.get(i).toString() + "\n\n");

            TextView textView = new TextView(this);
            linearLayout.addView(textView);

            textView.setText(String.valueOf(i + 1) + ": " + servicelist.get(i).getServiceName() + "\n\n");
            Log.d("checkingshit", String.valueOf(i + 1) + ": " + servicelist.get(i).toString());

        }
        //Stop the discovery thread
        d.interrupt();
    }

    private class ResolveMe implements Runnable {

        @Override
        public void run() {
            Log.d("onServiceResolved", "Connecting?");
            //mNsdManager.resolveService(serviceInfo, mResolveListener);
        }

        public void start() {
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    //ResolveListener callback
    NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {
        @Override
        public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {
            Log.d(TAG, "onResolveFailed. Errorcode: " + String.valueOf(i) + " " +
                    "NsdServiceInfo: " + nsdServiceInfo.getServiceName());
        }

        @Override
        public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
            Log.d(TAG, "onServiceResolved. Found: " + nsdServiceInfo.getServiceName());

            if (mServiceName.equals(nsdServiceInfo.getServiceName())) {
                //serviceInfo = nsdServiceInfo;
                //int port = serviceInfo.getPort();
                //InetAddress host = serviceInfo.getHost();
                //Log.d(TAG, "onServiceResolved! Port: " + String.valueOf(port) + " Host: " + String.valueOf(host));
            }

        }
    };

    //This gets a callback for every device found
    //The Discovery Listener
    NsdManager.DiscoveryListener mDiscoverListener = new NsdManager.DiscoveryListener() {

        @Override
        public void onStartDiscoveryFailed(String s, int i) {
            Log.d(TAG, "onStartDiscoveryFailed. For " + s + " and " + String.valueOf(i));
        }

        @Override
        public void onStopDiscoveryFailed(String s, int i) {
            Log.d(TAG, "onStopDiscoveryFailed");
        }

        @Override
        public void onDiscoveryStarted(String s) {
            Log.d(TAG, "onDiscoveryStarted. Searching for: " + s);
            mDiscoverServiceName = s;
        }

        @Override
        public void onDiscoveryStopped(String s) {
            Log.d(TAG, "onDiscoveryStopped");
        }

        @Override
        public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
            Log.d(TAG, "onServiceFound. (Good): Found: " + nsdServiceInfo.getServiceName() +
                    "\nPort: " + String.valueOf(nsdServiceInfo.getPort())
                    + "\nType: " + nsdServiceInfo.getServiceType()
                    + "\nHost: " + String.valueOf(nsdServiceInfo.getHost()));
            servicelist.add(nsdServiceInfo);
            mDiscoverServiceName = nsdServiceInfo.toString();
            if (!nsdServiceInfo.getServiceType().equals(tcplocalServiceType)) {
                Log.d(TAG, "Unknown Service Type: " + nsdServiceInfo.getServiceType());
            } else if (nsdServiceInfo.getServiceName().equals(mServiceName)) {
                Log.d(TAG, "Same machine: " + mServiceName);
            } else if (nsdServiceInfo.getServiceName().contains("work")) {
                mNsdManager.resolveService(nsdServiceInfo, mResolveListener);
            } else {
                Log.d(TAG, "No Match");
            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
            Log.d(TAG, "onServiceLost");
        }

    };

    //The registration listener
    NsdManager.RegistrationListener mRegistrationListener = new NsdManager.RegistrationListener() {
        @Override
        public void onRegistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
            Log.d(TAG, "onRegistrationFailed. For: " + nsdServiceInfo.getServiceName() +
                    " ServiceType: " + nsdServiceInfo.getServiceType() + " Int: " + String.valueOf(i));
            r.interrupt();
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo nsdServiceInfo, int i) {
            Log.d(TAG, "onUnRegistrationFailed");
            //Kill the register thread
            r.interrupt();
        }

        @Override
        public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
            mServiceName = nsdServiceInfo.getServiceName();
            port = nsdServiceInfo.getPort();
            Log.d(TAG, "onServiceRegisted. Registered: " + mServiceName + " Port: " + String.valueOf(port));
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo nsdServiceInfo) {
            Log.d(TAG, "onServiceUnRegistered: " + nsdServiceInfo.getServiceName());
        }
    };
}
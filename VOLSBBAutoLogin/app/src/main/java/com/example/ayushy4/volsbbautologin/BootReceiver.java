package com.example.ayushy4.volsbbautologin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ayushy4 on 28-09-2015.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        //Log.i("Bdcst", "Normally Started Service");
        Intent serviceIntent=new Intent(context,logService.class);

        //context.startService(serviceIntent);
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()==true) {
            Log.i("Brdcst", "You are connected to WIFI " + wifi.getConnectionInfo());

            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //NetworkInfo mWifi = connManager.getActiveNetworkInfo();

           if(mWifi.isConnectedOrConnecting())
            {
                Toast.makeText(context, "Connecting..", Toast.LENGTH_LONG).show();
                //Log.i("Brdcst","Toast Shown");
            }
            context.startService(serviceIntent);
        } else {
            Log.i("Brdcst","You are NOT connected to WIFI");
            context.stopService(serviceIntent);
            Log.i("Brdcst", "Service Stopped");

        }


    }
}

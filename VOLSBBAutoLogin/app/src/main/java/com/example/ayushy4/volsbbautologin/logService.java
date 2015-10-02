package com.example.ayushy4.volsbbautologin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ayushy4 on 28-09-2015.
 */
public class logService extends Service {
    @Nullable
    /*
    private Timer timer ;
    private TimerTask check=new TimerTask() {
        @Override
        public void run() {
            Log.i("Timer","Its Running");
        }
    };
    */
    final String FILE3="auto";
    final String FILE4="data";

    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate()
    {
        super.onCreate();
        Log.i("Service", "Created");
        SharedPreferences settings=getSharedPreferences(FILE3,0);
        boolean val=settings.getBoolean("enable",false);
        if(val)
        {
            Log.i("Service","Checked");


            try{
                String path1 = getFilesDir().getAbsolutePath() + "/" + FILE4;
                File file1 = new File(path1);
                if(file1.exists())
                {
                    FileInputStream fis1=openFileInput(FILE4);
                    String data="";
                    int k;
                    while((k=fis1.read())!=-1)
                    {
                        data+=(char) k;
                    }
                    //Log.i("Service-Data", data);
                    fis1.close();
                    final String finalData = data;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                //NetworkInfo mWifi = connManager.getActiveNetworkInfo();

                                while(!mWifi.isConnectedOrConnecting())
                                {
                                    mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                }
                                Log.i("Service","Type Wifi And Connected");
                                BufferedReader reader=null;
                                URL url = new URL("http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://www.msftncsi.com/redirect/");
                                URLConnection conn=url.openConnection();
                                conn.setDoOutput(true);
                                OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
                                wr.write(finalData);
                                wr.flush();

                                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                StringBuilder sb = new StringBuilder();
                                String line = null;

                                // Read Server Response
                                while((line = reader.readLine()) != null)
                                {
                                     // Append server response in string
                                    sb.append(line + "\n");
                                }

                                reader.close();
                                Log.i("Service-Login-RESPONSE", sb.toString());

                                //Toast.makeText(this, "Surf Now..",Toast.LENGTH_SHORT).show();
                                //Toast.makeText(logService.this, "Authenticated", Toast.LENGTH_LONG).show();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();







                }
                else
                {
                    Log.i("FILE","Internal Files On Data Does Not Exist");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }




        }
        else
            Log.i("Service","UnChecked");
        /*
        timer =new Timer("VolsTimer");
        timer.schedule(check,1000L, 1000L);
        */

    }
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("Service","Destroyed");
//        timer.cancel();
//        timer=null;
    }
}

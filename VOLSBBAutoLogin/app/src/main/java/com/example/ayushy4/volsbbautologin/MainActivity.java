package com.example.ayushy4.volsbbautologin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    EditText user, pass;
    CheckBox autologin;
    Button savebtn,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user =(EditText)findViewById(R.id.userId);
        pass =(EditText)findViewById(R.id.password);
        autologin =(CheckBox)findViewById(R.id.autocheck);
        savebtn = (Button)findViewById(R.id.save);
        logout = (Button) findViewById(R.id.logout);
        final String FILE1="username";
        final String FILE2="password";
        final String FILE3="auto";
        final String FILE4="data";

        SharedPreferences settings=getSharedPreferences(FILE3,0);
        boolean val=settings.getBoolean("enable",false);
        if (val)
        {
         autologin.setChecked(true);
        }

//        try{
//            startService(new Intent(logService.class.getName()));
//            Log.i("Activity","Service Started");
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        try{
            String path1 = getFilesDir().getAbsolutePath() + "/" + FILE1 , path2=getFilesDir().getAbsolutePath() + "/" + FILE2;
            File file1 = new File(path1) , file2 = new File(path2);
            if(file1.exists()&& file2.exists())
            {
                FileInputStream fis1=openFileInput(FILE1);
                //InputStreamReader iread=new InputStreamReader(fis1);
                //BufferedReader b_read=new BufferedReader(new InputStreamReader(fis1));
                String data="";
                        //=b_read.readLine();
                int k;
                while((k=fis1.read())!=-1)
                {
                   data+=(char) k;
                }

                //data=fis1.toString();
                //Log.i("Data-U", data);
                user.setText(data);
                fis1.close();
                data="";
                fis1=openFileInput(FILE2);
                while((k=fis1.read())!=-1)
                {
                    data += (char) k;
                }
                //data=fis1.toString();
                //Log.i("Data-P", data);
                pass.setText(data);
                fis1.close();
            }
            else
            {
                Log.i("FILE","Internal Files Does Not Exist");
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        autologin.setOnCheckedChangeListener(
                new CheckBox.OnCheckedChangeListener(){

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                        {
                            Log.i("CheckBox","Its Checked");

                            SharedPreferences settings=getSharedPreferences(FILE3,0);
                            SharedPreferences.Editor editor=settings.edit();
                            editor.putBoolean("enable",true);
                            editor.commit();
                            Log.i("CheckBox", "Its Checked-Saved");
                        }
                        else
                        {
                            Log.i("CheckBox","Its Un-Checked");
                            SharedPreferences settings=getSharedPreferences(FILE3,0);
                            SharedPreferences.Editor editor=settings.edit();
                            editor.putBoolean("enable",false);
                            editor.commit();
                            Log.i("CheckBox", "Its Un-Checked-Saved");
                        }

                    }
                }
        );
        savebtn.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v)
                    {
                        try {
                            FileOutputStream fos1=openFileOutput(FILE1,Context.MODE_PRIVATE);
                            fos1.write(user.getText().toString().getBytes());
                            fos1.close();
                            fos1=openFileOutput(FILE2,Context.MODE_PRIVATE);
                            fos1.write(pass.getText().toString().getBytes());
                            fos1.close();
                            Log.i("FILE","Data Saved");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                            if(isWifiConnected())
                            {


                                Log.i("Status","Logging In..");
                                new Thread(new Runnable(){
                                    @Override
                                    public void run() {
                                        //do network action in this function


                                        try {
                                            BufferedReader reader=null;
                                            String data= URLEncoder.encode("userId", "UTF-8")+"="+URLEncoder.encode(user.getText().toString(),"UTF-8");
                                            data +="&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(pass.getText().toString(),"UTF-8");
                                            data +="&"+URLEncoder.encode("serviceName", "UTF-8")+"="+URLEncoder.encode("ProntoAuthentication","UTF-8");
                                            //Log.i("My data", data + "DATA");
                                            FileOutputStream fos1=openFileOutput(FILE4,Context.MODE_PRIVATE);
                                            fos1.write(data.getBytes());
                                            fos1.close();

                                            URL url = new URL("http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://www.msftncsi.com/redirect/");
                                            URLConnection conn=url.openConnection();
                                            conn.setDoOutput(true);
                                            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
                                            wr.write(data);
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
                                            Log.i("Login-RESPONSE", sb.toString());





                                            //Toast.makeText(getApplicationContext(),"Surf Now..",Toast.LENGTH_LONG).show();
                                        } catch (MalformedURLException e1) {
                                            e1.printStackTrace();
                                        } catch (UnsupportedEncodingException e1) {
                                            e1.printStackTrace();
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }).start();
                                Toast.makeText(getApplicationContext(),"Surf Now..",Toast.LENGTH_SHORT).show();
                            }
                        else
                            {
                                Log.i("Wifi","Not Connected");
                                Toast.makeText(getApplicationContext(),"Connect to Wi-Fi first.",Toast.LENGTH_SHORT).show();
                            }



                    }
                }
        );


        logout.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v)
                    {

                        if(isWifiConnected())
                        {
                            Log.i("Status","Logging out");
                            new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    //do network action in this function


                                    try {
                                        BufferedReader reader=null;
                                        String data="";
                                        URL url = new URL("http://phc.prontonetworks.com/cgi-bin/authlogout?");
                                        URLConnection conn=url.openConnection();
                                        conn.setDoOutput(true);
                                        OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
                                        wr.write(data);
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


                                        Log.i("Logout-RESPONSE:",sb.toString());
                                    } catch (MalformedURLException e1) {
                                        e1.printStackTrace();
                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }).start();
                            Toast.makeText(getApplicationContext(),"Logged out..",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.i("Wifi","Not Connected");
                            Toast.makeText(getApplicationContext(),"Connect to Wi-Fi first.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );




    }

    public boolean isWifiConnected()
    {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            // Do whatever
            return true;
        }
        else
        {
            return false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("About");
            builder.setMessage("CodeName: LAVA-Lets Automate Volsbb Authentication \n\ngithub.com/ayushya ")
                    .setCancelable(true)
                    .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    })
            ;
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

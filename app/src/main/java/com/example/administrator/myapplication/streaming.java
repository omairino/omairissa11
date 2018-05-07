package com.example.administrator.myapplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.net.Socket;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
//import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

public class streaming extends AppCompatActivity implements SensorEventListener {

   //striming
    VideoView streamView;
    VideoView streamView1;
    MediaController mediaController;
    String src,Port_Streaming,Port_Sensor;


//sensor
    private SensorFusion sensorFusion;
    private SensorManager sensorManager = null;
    private DecimalFormat d = new DecimalFormat("#.##");
    //private TextView azimuthText, pithText, rollText;
    private static TextView ultrasonic;
    //Button btnUp,btnDown;
    //TextView txt2;

    public static String wifiModuleIp="";
    public static int wifiModulePort=0;
    //Data
    int m=0,m1=0,change,change1;
    static  String str="0",str1="0";

//oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        streamView = findViewById(R.id.streamview);
        streamView1 = findViewById(R.id.streamview1);
        //Intent intent=getIntent();
        src=getIntent().getStringExtra("IP");
        Port_Streaming=getIntent().getStringExtra("Port_Striming");
        Port_Sensor=getIntent().getStringExtra("Port_Sensor");
        Striming();



        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        registerSensorManagerListeners();

        d.setMaximumFractionDigits(2);
        d.setMinimumFractionDigits(2);

        sensorFusion = new SensorFusion();
        sensorFusion.setMode(SensorFusion.Mode.ACC_MAG);
           //ultrasonic = findViewById(R.id.text5);
           //azimuthText = findViewById(R.id.azmuth);
          //   pithText =  findViewById(R.id.pitch);
          //rollText =  findViewById(R.id.roll);
        //multiple();

        sensorFusion.setMode(SensorFusion.Mode.ACC_MAG);
       // btnUp=findViewById(R.id.btn1);
        //btnDown=findViewById(R.id.btn2);
        //txt2=findViewById(R.id.txt2);


        //  ultraSonic();
    }

    void Striming(){  //striming video
        String x="http://";
        x=x+src+":"+Port_Streaming;
        Log.d("AA",x);
        Uri UriSrc = Uri.parse(x);
        Toast.makeText(streaming.this,
                x, Toast.LENGTH_LONG).show();
        if(UriSrc == null){
            Toast.makeText(streaming.this,
                    "UriSrc == null", Toast.LENGTH_LONG).show();
        }else{
            streamView.setVideoURI(UriSrc);
            streamView1.setVideoURI(UriSrc);
            mediaController = new MediaController(this);
            streamView.setMediaController(mediaController);
            streamView1.setMediaController(mediaController);
            //streamView.setRotation(180f);
            streamView.start();
            streamView1.start();
            Toast.makeText(streaming.this,
                    "Connect: " + src,
                    Toast.LENGTH_LONG).show();
        }

    }





    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startMyTask(Socket_Async1 task) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }
    public void ultraSonic(){

        Socket_Async1 cmd_increase_servo1 = new Socket_Async1();
        // getIpandPort();
        startMyTask(cmd_increase_servo1);
//       cmd_increase_servo1.execute();
    }


    int x;
    int y;
    int pos=0;
    public void updateOrientationDisplay() {

        double azimuthValue = sensorFusion.getAzimuth();
        double rollValue =  sensorFusion.getRoll();
        double pitchValue =  sensorFusion.getPitch();

        m=(int)azimuthValue;
        m1=(int)rollValue;

        if(m>-143&&m<19){
            if(m<=0)
                x=m*(-1)+28;
            else
                x=(m-28)*-1;
            x=x/10;
            x=x*10;}

        if(m1>9&&m1<171){
            y=m1;
            y=y/10;
            y=y*10;
        }

        if(pos==0)
        {
            change=x;
            change1=y;
            pos=1;
        }

        if(!(change==x&&change1==y)) {
            pos=0;
            str = String.valueOf(x);
            str1 = String.valueOf(180 - y);
            //azimuthText.setText(String.valueOf(d.format(x)));
            //pithText.setText(String.valueOf(d.format(pitchValue)));
            //rollText.setText(String.valueOf(d.format(y)));

            getIpandPort();
            Socket_Async cmd_increase_servo = new Socket_Async();
            //startMyTask1(cmd_increase_servo);
            cmd_increase_servo.execute();
        }
    }

    public void registerSensorManagerListeners() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);
    }





    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerSensorManagerListeners();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        streamView.stopPlayback();
    }



    public void getIpandPort(){
        String ipandport=src;
        Log.d("MyTest","IP String: "+ipandport);
        //String temp[]=ipandport.split(":");
        wifiModuleIp=src;
        wifiModulePort=Integer.valueOf(Port_Sensor);
        Log.d("MyTest","IP : "+wifiModuleIp);
        Log.d("MyTest","port : "+wifiModulePort);

    }

    //sensor
    @Override
    public void onSensorChanged(SensorEvent event) {



        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorFusion.setAccel(event.values);
                sensorFusion.calculateAccMagOrientation();
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorFusion.setMagnet(event.values);
                break;
        }
        updateOrientationDisplay();



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    //static  Socket socket;
    //InetAddress inetAddress;
    static public class Socket_Async extends AsyncTask<Void,Void,Void>
    {
        Socket socket;
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                InetAddress inetAddress =InetAddress.getByName(streaming.wifiModuleIp);
                socket=new java.net.Socket(inetAddress,streaming.wifiModulePort);

                DataOutputStream dataOutputStream=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                dataOutputStream.writeBytes(str);
                dataOutputStream.writeBytes(":");
                dataOutputStream.writeBytes(str1);

                dataOutputStream.close();

                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.getMessage();
            }
            return null;
        }
    }
    static Handler handler=new Handler();

    static public class Socket_Async1 extends AsyncTask<Void,Void,Void>
    {
        Socket socket;
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                InetAddress inetAddress =InetAddress.getByName(streaming.wifiModuleIp);
                socket=new java.net.Socket(inetAddress,streaming.wifiModulePort);
                while(true) {
                    DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));


                    int   l = in.read();
                    ultrasonic.setText(l + " ");
                    //  handler.post(new Runnable(){

                    //   @Override
                    //   public void run() {


                    //   ultrasonic.setText(l + " ");
                    //    }

                    // }
                    // );
                    //in.close();
                    // socket.close();
                    Thread.sleep(500);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.getMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
            //end of while
        }
    }
}

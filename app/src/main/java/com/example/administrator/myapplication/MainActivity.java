package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText IP;
    EditText Port_Striming;
    EditText Port_Sensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IP=findViewById(R.id.editText);
        Port_Striming=findViewById(R.id.editText2);
        Port_Sensor=findViewById(R.id.editText3);
    }

    public void Connect(View view) {
        Intent i = new Intent(MainActivity.this, streaming.class);

        i.putExtra("IP", IP.getText().toString());
        i.putExtra("Port_Striming",  Port_Striming.getText().toString());
        i.putExtra("Port_Sensor",  Port_Sensor.getText().toString());
        if(IP.getText()!=null&&Port_Striming.getText()!=null&&Port_Sensor.getText()!=null){
            startActivity(i);
        }
    }
}

package com.example.stepscount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;

    TextView tv_steps;
    boolean running = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_steps  = (TextView) findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }
    @Override
    protected void onResume(){
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        System.out.println("This has been created");
        if (countSensor != null){
            sensorManager.registerListener( MainActivity.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
            System.out.println("Sensor is there");
        }
        else{
            Toast.makeText(this,"Sensor not found!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause(){

        super.onPause();
        running = false;

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running){
            tv_steps.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}



package com.example.stepscount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;


import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class SignUp extends AppCompatActivity {


    TextView name, mail;
    Button logout;
    private TextView textView;

    private ImageView imageView2;
    private double magnitudePrevious = 0;

    private static Integer stepCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        logout = findViewById(R.id.logout);
        name = findViewById(R.id.name);
//        mail = findViewById(R.id.mail);
        textView = findViewById(R.id.textView);
        imageView2 = findViewById(R.id.imageView2);


        CircularProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            name.setText(signInAccount.getDisplayName());
//            mail.setText(signInAccount.getEmail());
            Uri photoUrl = signInAccount.getPhotoUrl();
//          Picasso.get().load(photoUrl).into(imageView2);
            Picasso.get().load(photoUrl).transform(new RoundedCornersTransformation(100, 0)).resize(100, 100).into(imageView2);

        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event != null) {
                    float x_acceleration = event.values[0];
                    float y_acceleration = event.values[1];
                    float z_acceleration = event.values[2];

                    double magnitude = Math.sqrt((x_acceleration * x_acceleration) + (y_acceleration * y_acceleration) + (z_acceleration * z_acceleration));
                    double magnitudeDelta = magnitude - magnitudePrevious;
                    magnitudePrevious = magnitude;


                    if (magnitudeDelta > 5) {
                        stepCount = stepCount+1;
                    }
                    textView.setText(stepCount.toString());
                    circularProgressBar.setProgress(stepCount);

//                    circularProgressBar.setProgressWithAnimation(stepCount, (long) 1000);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
        Toast.makeText(this, "On Pause", Toast.LENGTH_SHORT).show();
    }

    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", stepCount);
        editor.apply();
        Toast.makeText(this, "On Stop", Toast.LENGTH_SHORT).show();
    }

    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount", 0);
        Toast.makeText(this, "On Resume", Toast.LENGTH_SHORT).show();

    }

}
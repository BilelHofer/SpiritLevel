package com.example.bubblelevel;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textX, textY;
    private ImageView ball;

    private List<Float> lastX = new ArrayList<>();
    private List<Float> lastY = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des variables
        ball = findViewById(R.id.ball);
        textX = findViewById(R.id.textX);
        textY = findViewById(R.id.textY);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometreSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Crée le listener
        SensorEventListener gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (lastX.size() < 6) {
                    lastX.add(event.values[0]);
                    lastY.add(event.values[1] * -1);
                } else {
                    lastX.remove(0);
                    lastY.remove(0);
                    lastX.add(event.values[0]);
                    lastY.add(event.values[1] * -1);
                }

                // Mets à jour la position de la balle
                ball.setX((getWindowManager().getDefaultDisplay().getWidth() - ball.getWidth()) / 2 + getAverage(lastX) * 50);
                ball.setY((getWindowManager().getDefaultDisplay().getHeight() - ball.getHeight()) / 2 + getAverage(lastY) * 50);

                // Calcule l'inclinaisons
                int xPercent = (int) (100 * event.values[1] / SensorManager.GRAVITY_EARTH);
                int yPercent = (int) (100 * event.values[0] / SensorManager.GRAVITY_EARTH);
                textX.setText("X: " + xPercent + "%");
                textY.setText("Y: " + yPercent + "%");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        // Active le listener
        sensorManager.registerListener(gyroscopeEventListener, accelerometreSensor, 1);
    }

    /**
     * Permet d'obtenir la moyenne d'une liste de float
     * @param list la liste
     * @return la moyenne en float
     */
    private float getAverage(List<Float> list) {
        float tot = 0;
        for (float f : list) {
            tot += f;
        }
        return tot / list.size();
    }
}
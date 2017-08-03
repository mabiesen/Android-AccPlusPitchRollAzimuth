package com.example.root.azimuthacccomp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];

    //vals for print
    float ax;
    float ay;
    float az;

    //views

    TextView xtxt;
    TextView ytxt;
    TextView ztxt;
    TextView pitchtxt;
    TextView rolltxt;
    TextView azimuthtxt;



    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineViews();

        SensorManager sManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void defineViews(){
        xtxt = (TextView)findViewById(R.id.xValTxt);
        ytxt = (TextView)findViewById(R.id.yValTxt);
        ztxt = (TextView)findViewById(R.id.zValTxt);
        azimuthtxt = (TextView)findViewById(R.id.azimuthValTxt);
        pitchtxt = (TextView)findViewById(R.id.pitchValTxt);
        rolltxt = (TextView)findViewById(R.id.rollValTxt);
    }

    public void setViewText(){
        xtxt.setText(Float.toString(ax));
        ytxt.setText(Float.toString(ay));
        ztxt.setText(Float.toString(az));
        azimuthtxt.setText(Float.toString(azimuth));
        pitchtxt.setText(Float.toString(pitch));
        rolltxt.setText(Float.toString(roll));

    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mags = event.values.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    accels = event.values.clone();
                    ax = event.values[0];
                    ay = event.values[1];
                    az = event.values[2];
                    break;
            }

            if (mags != null && accels != null) {
                gravity = new float[9];
                magnetic = new float[9];
                SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
                float[] outGravity = new float[9];
                SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);
                SensorManager.getOrientation(outGravity, values);

                azimuth = values[0] * 57.2957795f;
                pitch =values[1] * 57.2957795f;
                roll = values[2] * 57.2957795f;

                //Insert our text changed event here
                setViewText();

                mags = null;
                accels = null;
            }
        }
    };

}

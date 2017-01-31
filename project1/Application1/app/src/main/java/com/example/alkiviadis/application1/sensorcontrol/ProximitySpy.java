package com.example.alkiviadis.application1.sensorcontrol;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.ConnectivityManager;

import state.CurrentState;

public class ProximitySpy implements SensorEventListener {
    public static float [] values = new float[] { 0,0,0};
    private CurrentState state;
    private ConnectivityManager cm;

    public ProximitySpy(CurrentState state, ConnectivityManager cm) {
        this.state = state;
        this.cm = cm;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        values = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

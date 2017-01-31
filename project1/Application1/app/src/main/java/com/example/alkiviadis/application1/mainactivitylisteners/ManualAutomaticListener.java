package com.example.alkiviadis.application1.mainactivitylisteners;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.alkiviadis.application1.MainActivity;
import com.example.alkiviadis.application1.sensorcontrol.SensorNameSpace;

import state.CurrentState;

public class ManualAutomaticListener implements View.OnClickListener {
    private MainActivity mainActivity;
    public static boolean manual = true;
    private Button button;

    public ManualAutomaticListener(Button button, MainActivity mainActivity) {
        this.mainActivity=mainActivity;
        this.button=button;
    }

    @Override
    public void onClick(View v) {
        if (manual == false) {
            manual = true;

            button.setText("you are in manual mode");
            button.setBackgroundColor(Color.GREEN);

            Log.i("SensorController", "Controller start()");

        } else if (manual ==true) {
            manual = false;

            button.setText("you are in automatic mode");
            button.setBackgroundColor(Color.BLUE);

            Log.i("SensorController", "Controller stop()");
        }
    }
}

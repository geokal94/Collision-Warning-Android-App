package com.example.alkiviadis.application1.mainactivitylisteners;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.alkiviadis.application1.MainActivity;
import com.example.alkiviadis.application1.sensorcontrol.SensorNameSpace;

import state.CurrentState;

public class OnlineOfflineListener implements View.OnClickListener {
    private MainActivity mainActivity;
    public static boolean online = true;
    public static Button button;

    public OnlineOfflineListener(Button button, MainActivity mainActivity) {
        this.mainActivity=mainActivity;
        this.button=button;
    }

    public static void updategui(Button button) {
        if (online == true) {
            button.setText("you are working online");
            button.setBackgroundColor(Color.GREEN);

            Log.i("SensorController", "Controller start()");

        } else if (online ==false) {

            button.setText("you are working offline");
            button.setBackgroundColor(Color.RED);

            Log.i("SensorController", "Controller stop()");
        }
    }

    @Override
    public void onClick(View v) {
        if (online == false) {
            online = true;

            button.setText("you are working online");
            button.setBackgroundColor(Color.GREEN);

            Log.i("SensorController", "Controller start()");

        } else if (online ==true) {
            online = false;

            button.setText("you are working offline");
            button.setBackgroundColor(Color.RED);

            Log.i("SensorController", "Controller stop()");
        }
    }
}

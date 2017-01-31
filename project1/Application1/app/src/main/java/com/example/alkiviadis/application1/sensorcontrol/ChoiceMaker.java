package com.example.alkiviadis.application1.sensorcontrol;

import android.net.ConnectivityManager;

public class ChoiceMaker {
    public boolean useMqtt(boolean manual, boolean online, boolean internet_available){
        if (manual == true && online == true && internet_available == true) {
            return true;
        }
        if (manual == true && online == true && internet_available == false) {
            return false;
        }
        if (manual == true && online == false && internet_available == true) {
            return false;
        }
        if (manual == true && online == false && internet_available == false) {
            return false;
        }
        if (manual == false && online == true && internet_available == true) {
            return true;
        }
        if (manual == false && online == true && internet_available == false) {
            return false;
        }
        if (manual == false && online == false && internet_available == true) {
            return true;
        }
        if (manual == false && online == false && internet_available == false) {
            return false;
        }
        return false;
    }
}

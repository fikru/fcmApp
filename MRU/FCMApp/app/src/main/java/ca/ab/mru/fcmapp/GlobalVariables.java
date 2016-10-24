package ca.ab.mru.fcmapp;

import android.app.Application;

/**
 * Created by fikru on 14/09/16.
 */
public class GlobalVariables extends Application {
    private int FCMStatus, appStatus;
    private boolean intensity;

    public void setFCMStaus(int FCMStatus){ this.FCMStatus = FCMStatus; }

    public int getFCMStatus(){
        return this.FCMStatus;
    }

    public void setIntensity(boolean intensity){
        this.intensity = intensity;
    }

    public boolean getIntensity(){
        return this.intensity;
    }
}

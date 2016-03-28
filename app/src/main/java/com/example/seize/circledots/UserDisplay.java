package com.example.seize.circledots;

import android.app.Activity;
import android.os.Build;
import android.support.v7.appcompat.*;
import android.support.v7.appcompat.BuildConfig;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.FrameStats;

/**
 * Created by Rifat Rashid on 3/28/2016.
 * CLASS NAME: UserDisplay
 * PURPOSE: Retrieve the user's device dimensions in pixels or in inches
 */

public class UserDisplay {

    DisplayRequestType displayRequestType;
    private DisplayMetrics dm;
    private static final String TAG = "UserDisplayClass";

    public enum DisplayRequestType {
        HEIGHT, WIDTH, DIAGONAL
    }

    //default constructor
    public UserDisplay(Activity userFocusActivity) {
        onCreateUserScreenProfile(userFocusActivity);
    }

    private void onCreateUserScreenProfile(Activity userFocusActivity) {
        try {
            dm = new DisplayMetrics();
            userFocusActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getDisplayMeasurements(DisplayRequestType requestType) {
        try {
            switch (requestType) {
                case HEIGHT:
                    return this.dm.heightPixels / this.dm.ydpi;
                case WIDTH:
                    return this.dm.widthPixels / this.dm.xdpi;
                case DIAGONAL:
                    double x = Math.pow(this.dm.widthPixels / this.dm.xdpi, 2);
                    double y = Math.pow(this.dm.heightPixels / this.dm.ydpi, 2);
                    return Math.sqrt(x + y);
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Error with getDisplayMeasurements method");
            } else {
                //do nothing <Release Version>
            }
            e.printStackTrace();
        }
        return 1;
    }

    public double[] getAllDisplayMeasurements() {
        //allDisplayMeasurements[0] = display width
        //allDisplayMeasurements[1] = display height
        //allDisplayMeasurements[2] = display diagonal
        double[] allDisplayMeasurements = new double[3];
        allDisplayMeasurements[0] = getDisplayMeasurements(DisplayRequestType.WIDTH);
        allDisplayMeasurements[1] = getDisplayMeasurements(DisplayRequestType.HEIGHT);
        allDisplayMeasurements[2] = getDisplayMeasurements(DisplayRequestType.DIAGONAL);
        return allDisplayMeasurements;
    }

    @Override
    public String toString() {
        StringBuilder toStringText = new StringBuilder();
        toStringText.append("Device Manufacturer: " + Build.MANUFACTURER + "\n");
        toStringText.append("Device Model: " + Build.MODEL + "\n");
        toStringText.append("Device Dimensions{ \n");
        double[] temp_measurements_array = getAllDisplayMeasurements();
        for (int i = 0; i < temp_measurements_array.length; i++) {
            //are we at the last index?
            switch (i) {
                case 0:
                    toStringText.append("\t Width: " + String.valueOf(temp_measurements_array[i]) + "\n");
                    break;
                case 1:
                    toStringText.append("\t Height: " + String.valueOf(temp_measurements_array[i]) + "\n");
                    break;
                case 2:
                    toStringText.append("\t Diagonal: " + String.valueOf(temp_measurements_array[i]) + "\n");
                    break;
            }
        }
        toStringText.append("}");
        return toStringText.toString();
    }


}

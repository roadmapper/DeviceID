package com.vinay.deviceid;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class Helper {

    public static class Technology {
        public static final String GSM = "GSM";
        public static final String TD_SCDMA = "TD-SCDMA";
        public static final String CDMA = "CDMA";
        public static final String UMTS = "UMTS";
        public static final String LTE = "LTE";
    }

    public static class Preferences {
        public static final String PREFS_NAME = "MyPrefsFile";
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(View rootLayout, String msg) {
        Snackbar.make(rootLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static Band createGSM_TDSCDMABand(String bandString, String technology) {
        String frequency = bandString.replace(technology, ""); // make it "850"
        return new Band(frequency, technology);
    }

    public static Band createUMTS_LTEBand(String bandString, String technology) {
        String temp = bandString.replace(technology, ""); // make it "850 (B0)"
        int bandNum = Integer.parseInt(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")")).replace("B", ""));
        String frequency = temp.substring(0, temp.indexOf("(") - 1);
        return new Band(frequency, bandNum, technology);
    }


    public static Band createCDMABand(String bandString) {
        String temp = bandString.replace(Technology.CDMA, ""); // make it "800 (BC0)"
        int bandNum = Integer.parseInt(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")")).replace("BC", ""));
        String frequency = temp.substring(0, temp.indexOf("(") - 1);
        return new Band(frequency, bandNum, Technology.CDMA);
    }
}

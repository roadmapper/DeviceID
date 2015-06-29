package com.vinay.deviceid;

import android.content.Context;
import android.widget.Toast;

public class Helper {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

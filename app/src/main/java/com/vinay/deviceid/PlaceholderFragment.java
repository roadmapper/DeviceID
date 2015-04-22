package com.vinay.deviceid;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String TAG = "PLACE";

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Context context = getActivity().getApplicationContext();
        String serviceName = context.TELEPHONY_SERVICE;
        TelephonyManager m_telephonyManager = (TelephonyManager) context.getSystemService(serviceName);
        String imei, imsi;
        imei = m_telephonyManager.getDeviceId();
        imsi = m_telephonyManager.getSubscriberId();

        Log.d(TAG, Build.MODEL); // Model number/name
        TextView imei_t = (TextView) rootView.findViewById(R.id.imei);
        imei_t.setText(imei_t.getText() + imei);
        TextView imsi_t = (TextView) rootView.findViewById(R.id.imsi);
        imsi_t.setText(imsi_t.getText() + imsi);

        TextView bands = (TextView) rootView.findViewById(R.id.bands);
        TextView model = (TextView) rootView.findViewById(R.id.model);
        model.setText(model.getText() + Build.MANUFACTURER.toUpperCase() + " " + Build.MODEL + "(" + Build.PRODUCT + ")");

        getDeviceSpecs(getDeviceInfo(), bands);

        return rootView;
    }

    private String getDeviceInfo() {
        /*Log.d("PLACE", Build.DEVICE); // Device code name
        Log.d("PLACE", Build.BRAND);
        Log.d("PLACE", Build.PRODUCT); // Product name internal
        Log.d("PLACE", Build.MANUFACTURER);*/
        return Build.MODEL;
    }

    private void getDeviceSpecs(String s, TextView bands) {
        s = s.replaceAll(" ", "%20");
        Log.d(TAG, s);

        GetDeviceTask t = new GetDeviceTask(bands);
        Toast.makeText(getActivity().getApplicationContext(), "Connecting to server...", Toast.LENGTH_SHORT).show();
        t.execute(s);

    }
}

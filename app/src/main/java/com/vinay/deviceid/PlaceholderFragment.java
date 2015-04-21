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
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        getDeviceSpecs(getDeviceInfo());

        return rootView;
    }

    private String getDeviceInfo() {
        Context context = getActivity().getApplicationContext();
        String serviceName = context.TELEPHONY_SERVICE;
        TelephonyManager m_telephonyManager = (TelephonyManager) context.getSystemService(serviceName);
        String imei, imsi;
        imei = m_telephonyManager.getDeviceId();
        imsi = m_telephonyManager.getSubscriberId();
        Log.d("PLACE", imei);
        Log.d("PLACE", imsi);
        Log.d("PLACE", Build.MODEL); // Model number/name
        Log.d("PLACE", Build.DEVICE); // Device code name
        Log.d("PLACE", Build.BRAND);
        Log.d("PLACE", Build.PRODUCT); // Product name internal
        Log.d("PLACE", Build.MANUFACTURER);
        return Build.MODEL;
    }

    private void getDeviceSpecs(String s) {
        Document doc = null;
        s = s.replaceAll(" ", "%20");
        Log.d("PLACE", s);

        GetDeviceTask t = new GetDeviceTask();
        Toast.makeText(getActivity().getApplicationContext(), "Connecting to server...", Toast.LENGTH_SHORT).show();
        t.execute("http://pdadb.net/index.php?m=search&quick=1&exp=");

    }
}

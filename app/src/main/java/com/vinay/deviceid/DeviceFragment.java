package com.vinay.deviceid;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class DeviceFragment extends Fragment {

    private static final String TAG = "DeviceFragment";

    private static ArrayList<HashMap<String, String>> list = new ArrayList<>();
    private static String[] from = {"name", "data"};
    private static int[] to = {android.R.id.text1, android.R.id.text2};
    private static SimpleAdapter adapter;

    private static ArrayList<Band> bands;
    int color;

    public DeviceFragment() {
    }

    @SuppressLint("ValidFragment")
    public DeviceFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_device, container, false);
        final ListView listview = (ListView) view.findViewById(R.id.listView);

        adapter = new SimpleAdapter(getActivity().getApplicationContext(), list,
                android.R.layout.simple_list_item_2, from, to);
        listview.setAdapter(adapter);
        listview.setLongClickable(true);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            @TargetApi(10)
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                HashMap<String, String> item = (HashMap<String, String>) listview.getItemAtPosition(position);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                    // do something for phones running an SDK before Honeycomb
                    android.text.ClipboardManager cm = (android.text.ClipboardManager) getActivity()
                            .getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(item.get(from[1]));
                } else {
                    // Do something for Honeycomb and above versions
                    android.content.ClipboardManager cm = (android.content.ClipboardManager) getActivity()
                            .getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText(item.get(from[0]), item.get(from[1])));
                }
                showToast("Copied " + item.get(from[0]) + " to the clipboard.");
                return true;
            }
        });

        getOnboardInfo();

        //bands = new ArrayList<Band>();
        Log.d(TAG, Build.MODEL); // Model number/name


        getDeviceSpecs(getModel());

        return view;
    }

    private String getModel() {
        /*Log.d("PLACE", Build.DEVICE); // Device code name
        Log.d("PLACE", Build.BRAND);
        Log.d("PLACE", Build.PRODUCT); // Product name internal
        Log.d("PLACE", Build.MANUFACTURER);*/
        insertData("Model", Build.MANUFACTURER.toUpperCase()
                + " " + Build.MODEL + " (" + Build.PRODUCT + ")");
        adapter.notifyDataSetChanged();
        return Build.MODEL;
    }

    private void getOnboardInfo() {

        String serviceName = getActivity().getApplicationContext().TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().
                getApplicationContext().getSystemService(serviceName);
        String imei_meid_esn, imsi;
        boolean isCellularDevice = true;
        boolean isGsm = true;

        switch (telephonyManager.getPhoneType()) {
            case TelephonyManager.PHONE_TYPE_CDMA:
                isGsm = false;
                break;
            case TelephonyManager.PHONE_TYPE_GSM:
                break;
            default:
                isCellularDevice = false;
                break;
        }

        if (isCellularDevice) {
            imei_meid_esn = telephonyManager.getDeviceId();
            imsi = telephonyManager.getSubscriberId();
            String id;
            if (isGsm) {
                id = "IMEI";
            } else {
                id = "MEID/ESN";
            }
            insertData(id + " (Device)", imei_meid_esn);
            insertData("IMSI (SIM)", imsi);
        }
    }

    private void getDeviceSpecs(String s) {
        s = s.replaceAll(" ", "%20");
        Log.d(TAG, s);

        GetDeviceTask t = new GetDeviceTask();
        showToast("Connecting to server...");
        t.execute(s);

    }

    public static void insertData(String name, String data) {
        HashMap<String, String> item = new HashMap<>();
        item.put(from[0], name);
        item.put(from[1], data);
        list.add(item);
        adapter.notifyDataSetChanged();
    }

    public static ArrayList<Band> getBands() {
        return bands;
    }

    public static void setBands(ArrayList<Band> bands1) {
        bands = bands1;
    }


    private void showToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}

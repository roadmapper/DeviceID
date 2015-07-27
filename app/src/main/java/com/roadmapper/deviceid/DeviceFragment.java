package com.roadmapper.deviceid;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.HashMap;

public class DeviceFragment extends Fragment {

    private static final String TAG = "DeviceFragment";

    private static Context context;

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
        context = getActivity().getApplicationContext();
        getActivity().setTheme(R.style.AppTheme);

        final View view = inflater.inflate(R.layout.fragment_device, container, false);
        final ListView listview = (ListView) view.findViewById(R.id.listView);

        adapter = new SimpleAdapter(context, list,
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
                //Helper.showToast(getActivity().getApplicationContext(), "Copied " + item.get(from[0]) + " to the clipboard.");
                Helper.showSnackbar(getView(), "Copied " + item.get(from[0]) + " to the clipboard.");
                return true;
            }
        });

        if (!initListFromPreferences()) {
            getOnboardInfo();
            getDeviceSpecs(getModel());
        }
        return view;
    }

    private boolean initListFromPreferences() {
        boolean isCellularDevice = true;
        boolean isGsm = true;
        String deviceId = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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
            if (isGsm) {
                deviceId = "IMEI (Device)";
            } else {
                deviceId = "MEID/ESN (Device)";
            }
            String device = readData(deviceId);
            if (device != null)
                postData(deviceId, device);
            else
                return false;

            String imsi = readData("IMSI (SIM)");
            if (imsi != null)
                postData("IMSI (SIM)", imsi);
            else
                return false;
        }

        String model = readData("Model");
        if (model != null)
            postData("Model", model);
        else
            return false;

        String bands = readData("Bands");
        if (bands != null)
            postData("Bands", bands);
        else
            return false;

        return true;
    }

    private String getModel() {
        Log.d(TAG, Build.MODEL); // Model number/name
        Log.d(TAG, Build.DEVICE); // Device code name
        Log.d(TAG, Build.BRAND);
        Log.d(TAG, Build.PRODUCT); // Product name internal
        Log.d(TAG, Build.MANUFACTURER);
        String name = "Model";
        String data = Build.MANUFACTURER.toUpperCase()
                + " " + Build.MODEL + " (" + Build.PRODUCT + ")";
        if (readData(name) == null) {
            insertData(name, data);
        } else {
            postData(name, data);
        }

        String model = Build.MODEL;

        // need to strip the "LG-" as pdadb.net does not like that
        if (model.contains("LG-"))
            model = model.substring(3);

        return model;
    }

    private void getOnboardInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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
            imei_meid_esn = telephonyManager.getDeviceId().toUpperCase();
            imsi = telephonyManager.getSubscriberId();

            String deviceId;
            if (isGsm) {
                deviceId = "IMEI (Device)";
            } else {
                deviceId = "MEID/ESN (Device)";
            }
            if (readData(deviceId) == null)
                insertData(deviceId, imei_meid_esn);
            else
                postData(deviceId, imei_meid_esn);
            if (readData("IMSI (SIM)") == null)
                insertData("IMSI (SIM)", imsi);
            else
                postData("IMSI (SIM)", imsi);
        }
    }

    private void getDeviceSpecs(String s) {
        if (readData("Bands") == null) {
            s = s.replaceAll(" ", "%20");
            Log.d(TAG, s);

            GetDeviceTask task = new GetDeviceTask();
            Helper.showToast(context, "Connecting to server...");
            task.execute(s);
        }

    }

    public static void insertData(String name, String data) {
        postData(name, data);

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = context.getSharedPreferences(Helper.Preferences.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, data);

        // Commit the edits!
        editor.commit();
    }

    public static String readData(String name) {
        // Restore preferences
        SharedPreferences settings = context.getSharedPreferences(Helper.Preferences.PREFS_NAME, 0);
        return settings.getString(name, null);
    }

    public static void postData(String name, String data) {
        HashMap<String, String> item = new HashMap<>();
        item.put(from[0], name);
        item.put(from[1], data);
        boolean alreadyExists = false;
        for (HashMap<String, String> i : list) {
            if (i.get(from[0]).equals(name) && i.get(from[1]).equals(data))
                alreadyExists = true;
        }
        if (!alreadyExists) {
            list.add(item);
            adapter.notifyDataSetChanged();
        }
    }
}

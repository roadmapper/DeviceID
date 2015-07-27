package com.roadmapper.deviceid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class NetworkFragment extends Fragment {
    private static final String TAG = "NetworkFragment";

    static Context context;

    Spinner countrySpinner;
    static ListView listView;
    static NetworkAdapter adapter;
    ArrayList<Network> networks;

    int color;

    public NetworkFragment() {
    }

    @SuppressLint("ValidFragment")
    public NetworkFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_network, container, false);
        context = this.getActivity().getApplicationContext();
        listView = (ListView) view.findViewById(R.id.listView2);

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetNetworksTask task = new GetNetworksTask();
                HashMap<String, String> tech = new HashMap<String, String>();
                tech.put("UMTS", (String) countrySpinner.getSelectedItem());
                task.execute(tech);
                Helper.showSnackbar(view, "Finding networks...");
            }
        });

        /*Spinner region = (Spinner) view.findViewById(R.id.regions);
        region.setSelection(0);
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position > 0) {
                    Log.d(TAG, getResources().getStringArray(R.array.regions)[position]);
                } else {
                    //Helper.showToast(getActivity().getApplicationContext(), "Select region");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        countrySpinner = (Spinner) view.findViewById(R.id.countries);
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        for (Locale locale : locales) {
            //
            // locale.get
            String country = locale.getDisplayCountry();
            if (country.trim().length()>0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this.getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, countries);
        countrySpinner.setAdapter(locationAdapter);



        return view;
    }

    public static void updateList(ArrayList<Network> networks){
        ArrayList<Network> supported_networks = new ArrayList<>();

        SharedPreferences settings = context.getSharedPreferences(Helper.Preferences.PREFS_NAME, 0);
        String bands = settings.getString("Bands", null);

        String[] bands_arr = bands.split(",");
        ArrayList<Band> bandsArrL = new ArrayList<>();
        for (int i = 0; i < bands_arr.length; i++) {
            Band band = null;
            bands_arr[i] = bands_arr[i].replaceFirst(" ", "");
            if (bands_arr[i].contains(Helper.Technology.GSM)) {
                band = Helper.createGSM_TDSCDMABand(bands_arr[i], Helper.Technology.GSM);
            } else if (bands_arr[i].contains(Helper.Technology.TD_SCDMA)) {
                band = Helper.createGSM_TDSCDMABand(bands_arr[i], Helper.Technology.TD_SCDMA);
            } else if (bands_arr[i].contains(Helper.Technology.UMTS)) {
                band = Helper.createUMTS_LTEBand(bands_arr[i], Helper.Technology.UMTS);
            } else if (bands_arr[i].contains(Helper.Technology.LTE)) { // Covers TD-LTE and LTE
                band = Helper.createUMTS_LTEBand(bands_arr[i], Helper.Technology.LTE);
            } else if (bands_arr[i].contains(Helper.Technology.CDMA)) {
                band = Helper.createCDMABand(bands_arr[i]);
            }
            bandsArrL.add(band);
        }

        for(Network network : networks) {
            if (bandsArrL.contains(network.getBand())) {
                Log.d(TAG, network.getBand().toString());
                supported_networks.add(network);
            }
        }
        adapter = new NetworkAdapter(context, supported_networks);
        listView.setAdapter(adapter);
    }

    private static class NetworkAdapter extends ArrayAdapter<Network> {
        public NetworkAdapter(Context context, ArrayList<Network> networks) {
            super(context, 0, networks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Network network = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            /*// Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
            // Populate the data into the template view using the data object
            tvName.setText(user.name);
            tvHome.setText(user.hometown);*/
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(network.getOperator());

            TextView tv2 = (TextView) convertView.findViewById(android.R.id.text2);
            tv2.setText(network.getBand().getTechnology() + ": " + network.getBand().getFrequency() + " MHz (Band " + network.getBand().getBand() + ")");

            // Return the completed view to render on screen
            return convertView;
        }
    }
}

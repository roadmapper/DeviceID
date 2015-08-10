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
import android.widget.BaseAdapter;
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

    private static Context context;
    private static View view;

    private Spinner countrySpinner;
    private static ListView listView;
    private static NetworkAdapter adapter;
    ArrayList<Network> networks;

    private static final ArrayList<String> notCountries = new ArrayList<String>() {{
        add("Latin America");
        add("Europe");
    }};

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

        view = inflater.inflate(R.layout.fragment_network, container, false);
        context = this.getActivity().getApplicationContext();
        listView = (ListView) view.findViewById(R.id.listView2);

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetNetworksTask task = new GetNetworksTask();
                HashMap<String, String> tech = new HashMap<String, String>();
                tech.put("UMTS", (String) countrySpinner.getSelectedItem());
                tech.put("LTE", (String) countrySpinner.getSelectedItem());
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
            if (country.trim().length() > 0 && !countries.contains(country) && !notCountries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this.getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, countries);
        countrySpinner.setAdapter(locationAdapter);



        return view;
    }

    public static void updateList(ArrayList<Network> networks){
        if (networks.size() != 0) {
            ArrayList<Network> supported_networks = new ArrayList<>();
            HashMap<Key, ArrayList<Network>> supported_consolidated_networks = new HashMap<>();

            SharedPreferences settings = context.getSharedPreferences(Helper.Preferences.PREFS_NAME, 0);
            String bands = settings.getString("Bands", "");

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

            for (Network network : supported_networks) {
                if (!supported_consolidated_networks.containsKey(new Key(network.getOperator(), network.getBand().getTechnology()))){
                    ArrayList<Network> all_networks = new ArrayList<>();
                    all_networks.add(network);
                    supported_consolidated_networks.put(new Key(network.getOperator(), network.getBand().getTechnology()), all_networks);
                } else {
                    supported_consolidated_networks.get(new Key(network.getOperator(), network.getBand().getTechnology())).add(network);
                }
            }

            adapter = new NetworkAdapter(supported_consolidated_networks);
            listView.setAdapter(adapter);
        }
        else {
            listView.setAdapter(null);
            Helper.showSnackbar(view, "No networks found.");
        }
    }

    private static class NetworkAdapter extends BaseAdapter {

        private HashMap<Key, ArrayList<Network>> mData = new HashMap<>();
        private Key[] mKeys;
        public NetworkAdapter(HashMap<Key, ArrayList<Network>> data){
            mData  = data;
            mKeys = mData.keySet().toArray(new Key[data.size()]);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public ArrayList<Network> getItem(int position) {
            return mData.get(mKeys[position]);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            Key network_key = mKeys[position];
            ArrayList<Network> networks = getItem(position);

            //Network network = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(NetworkFragment.context).inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(network_key.operator);

            StringBuilder sb = new StringBuilder();
            sb.append(network_key.technology);
            sb.append(": ");
            for (Network network : networks) {
                sb.append(network.getBand().getFrequency());
                sb.append(" MHz (Band ");
                sb.append(network.getBand().getBand());
                sb.append("), ");
            }
            sb.replace(sb.length() - 2, sb.length(), "");


            TextView tv2 = (TextView) convertView.findViewById(android.R.id.text2);
            tv2.setText(sb.toString());

            // Return the completed view to render on screen
            return convertView;
        }
    }

    private static class Key {

        private final String operator;
        private final String technology;

        public Key(String operator, String technology) {
            this.operator = operator;
            this.technology = technology;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return operator.equals(key.operator) && technology.equals(key.technology);
        }

        @Override
        public int hashCode() {
            return 31 * operator.hashCode() + technology.hashCode();
        }

    }
}

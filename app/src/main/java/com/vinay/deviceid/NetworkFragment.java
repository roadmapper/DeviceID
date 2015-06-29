package com.vinay.deviceid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

public class NetworkFragment extends Fragment {
    private static final String TAG = "NetworkFragment";

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
        Spinner region = (Spinner) view.findViewById(R.id.regions);
        region.setSelection(0);
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position > 0){
                    Log.d(TAG, getResources().getStringArray(R.array.regions)[position]);
                }
                else{
                    Helper.showToast(getActivity().getApplicationContext(), "Select region");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }
}

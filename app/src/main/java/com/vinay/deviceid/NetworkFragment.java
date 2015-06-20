package com.vinay.deviceid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView stuff = (TextView) view.findViewById(R.id.text_stuff);
        stuff.setText("network");
        return view;
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

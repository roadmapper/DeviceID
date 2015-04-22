package com.vinay.deviceid;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GetDeviceTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "GETDEVICETASK";

    private TextView bands_t;

    public GetDeviceTask(TextView bands_t){
        this.bands_t = bands_t;
    }

    @Override
    protected String doInBackground(String... params) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://pdadb.net/index.php?m=search&quick=1&exp=" + params[0]).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String bands_string = "";
        Elements devices = doc.select("table > tbody > tr > td > h1 > a");
        Element device = null;
        if (devices != null) {
            device = devices.first();
        } else {
            bands_string = "Could not find device at pdadb.net";
        }

        if (device != null) {
            Log.d(TAG, device.attr("href"));
            Log.d(TAG, device.attr("abs:href"));
            try {
                doc = Jsoup.connect(device.attr("abs:href")).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements bands = doc.select("tr > td");

            for (int i = 0; i < bands.size(); i++) {
                if (bands.get(i).text().equals("Cellular Networks:")){
                    Log.d(TAG, "" + i);
                    Log.d(TAG, bands.get(i+1).text());
                    bands_string += bands.get(i+1).text();
                }
                if (bands.get(i).text().equals("Secondary Cellular Networks:")){
                    Log.d(TAG, "" + i);
                    Log.d(TAG,bands.get(i+1).text());
                    bands_string += "," + bands.get(i+1).text();
                }

            }
        } else {
            bands_string = "Cannot connect to pdadb.net";
        }
        return bands_string;
    }

    @Override
    protected void onPostExecute(String bands) {
        bands_t.setText(bands);
    }
}

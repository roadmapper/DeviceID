package com.vinay.deviceid;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetDeviceTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "GETDEVICETASK";

    //private TextView bands_t;

    public GetDeviceTask(/*TextView bands_t*/) {
        //this.bands_t = bands_t;
    }

    @Override
    protected String doInBackground(String... params) {
        Document doc = null;
        try {
            String url = "http://pdadb.net/index.php?m=search&quick=1&exp=" + params[0];
            Log.d(TAG, url);
            doc = Jsoup.connect(url).get();
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
                if (bands.get(i).text().equals("Cellular Networks:")) {
                    Log.d(TAG, "" + i);
                    Log.d(TAG, bands.get(i + 1).text());
                    bands_string += bands.get(i + 1).text();
                }
                if (bands.get(i).text().equals("Secondary Cellular Networks:")) {
                    Log.d(TAG, "" + i);
                    Log.d(TAG, bands.get(i + 1).text());
                    bands_string += ", " + bands.get(i + 1).text();
                }

            }
        } else {
            bands_string = "Cannot connect to pdadb.net";
        }
        return bands_string;
    }

    @Override
    protected void onPostExecute(String bands) {
        //bands_t.setText(bands);
        DeviceFragment.insertData("Bands", bands);
        String[] bands_arr = bands.split(",");
        ArrayList<Band> bandsArrL = new ArrayList<>();
        for (int i = 0; i < bands_arr.length; i++) {
            Band band = null;
            bands_arr[i] = bands_arr[i].replaceFirst(" ", "");
            if (bands_arr[i].contains("GSM")) {
                band = new Band(bands_arr[i].replace("GSM", ""), "GSM");
            }
            else if (bands_arr[i].contains("TD-SCDMA")) {
                band = new Band(bands_arr[i].replace("TD-SCDMA", ""), "TD-SCDMA");
            }
            else if (bands_arr[i].contains("UMTS")) {
                band = createUMTS_LTEBand(bands_arr[i], "UMTS");
            }
            else if (bands_arr[i].contains("LTE")) { // Covers TD-LTE and LTE
                band = createUMTS_LTEBand(bands_arr[i], "LTE");
            }
            else if (bands_arr[i].contains("CDMA")) {
                band = createCDMABand(bands_arr[i]);
            }
            bandsArrL.add(band);
        }
        Log.d(TAG, bandsArrL.toString());
        //DeviceFragment.setBands();
    }

    private Band createUMTS_LTEBand(String bandString, String technology) {
        String temp = bandString.replace(technology, ""); // make it "850 (B0)"
        int bandNum = Integer.parseInt(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")")).replace("B", ""));
        String frequency = temp.substring(0, temp.indexOf("(") - 1);
        return new Band(frequency, bandNum, technology);
    }

    private Band createCDMABand(String bandString) {
        String temp = bandString.replace("CDMA", ""); // make it "800 (BC0)"
        int bandNum = Integer.parseInt(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")")).replace("BC", ""));
        String frequency = temp.substring(0, temp.indexOf("(") - 1);
        return new Band(frequency, bandNum, "CDMA");
    }
}

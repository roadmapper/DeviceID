package com.vinay.deviceid;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class GetDeviceTask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://pdadb.net/index.php?m=search&quick=1&exp=" + params[0]).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element device = doc.select("a[href]").first();
        Log.d("PLACE", device.attr("href"));
        return device.attr("href");
    }
}

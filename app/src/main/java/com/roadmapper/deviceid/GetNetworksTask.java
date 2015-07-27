package com.roadmapper.deviceid;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GetNetworksTask extends AsyncTask<HashMap<String, String>, Void, ArrayList<Network>> {

    private static String TAG = "GetNetworks";

    private ArrayList<Network> networks = new ArrayList<Network>();

    @Override
    protected ArrayList<Network> doInBackground(HashMap<String, String>... maps) {
        if (maps[0].containsKey(Helper.Technology.UMTS)) {
            String country = maps[0].get(Helper.Technology.UMTS);
            Log.d(TAG, country);
            Document doc = null;
            try {
                doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_UMTS_networks").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getUMTS(doc, country);
        }
        if (maps[0].containsKey(Helper.Technology.LTE)) {
            String country = maps[0].get(Helper.Technology.LTE);
            Log.d(TAG, country);
            Document doc = null;
            try {
                doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_LTE_networks").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getLTE(doc, country);
        }
        return networks;
    }

    @Override
    protected void onPostExecute(ArrayList<Network> networks) {
        NetworkFragment.updateList(networks);
    }

    private void getUMTS(Document doc, String country) {
        ArrayList<Element> regions = new ArrayList<>();
        regions.add(doc.getElementById("Africa"));
        regions.add(doc.getElementById("Americas"));
        regions.add(doc.getElementById("Asia"));
        regions.add(doc.getElementById("Europe"));
        regions.add(doc.getElementById("Middle_East"));
        regions.add(doc.getElementById("Oceania"));

        for (Element region : regions) {
            getNetworksUMTS(region, country);
        }

    }

    private void getNetworksUMTS(Element region, String country) {
        Element table = region.parent().nextElementSibling();
        Elements rows = table.select("tr");
        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements tds = row.select("td");
            //Log.d(TAG, tds.get(0).text() + ":" + tds.get(1).text());
            if (tds.get(1).text().contains(country)) {
                if (!(tds.get(3).text().contains("?"))) {
                    if (tds.get(3).text().contains("/")){
                        String[] temp = tds.get(3).text().split(" / ");
                        ArrayList<String> bands = new ArrayList<>(Arrays.asList(temp));
                        for (String band : bands) {
                            Log.d(TAG, tds.get(0).text() + ", " + tds.get(1).text().trim() + ", " + Helper.getBand(band, "UMTS").getFrequency());
                            networks.add(new Network(tds.get(0).text(), tds.get(1).text().trim(), Helper.getBand(band, "UMTS")));
                        }
                    }
                    else {
                        Log.d(TAG, tds.get(0).text() + ", " + tds.get(1).text().trim() + ", " + Helper.getBand(tds.get(3).text(), "UMTS").getFrequency());
                        networks.add(new Network(tds.get(0).text(), tds.get(1).text().trim(), Helper.getBand(tds.get(3).text(), "UMTS")));
                    }

                }
            }

        }
    }

    private void getLTE(Document doc, String country) {
        ArrayList<Element> regions = new ArrayList<>();
        regions.add(doc.getElementById("Africa"));
        regions.add(doc.getElementById("Americas"));
        regions.add(doc.getElementById("Asia"));
        regions.add(doc.getElementById("Europe"));
        regions.add(doc.getElementById("Middle_East"));
        regions.add(doc.getElementById("Oceania"));

        for (Element region : regions) {
            getNetworksLTE(region, country);
        }

    }

    private void getNetworksLTE(Element region, String country) {
        Element table = region.parent().nextElementSibling();
        Elements rows = table.select("tr");
        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements tds = row.select("td");
            //Log.d(TAG, tds.get(0).text() + ":" + tds.get(1).text());
            if (tds.get(1).text().contains(country)) {
                if (!(tds.get(3).text().contains("?"))) {
                    if (tds.get(3).text().contains("/")){
                        String[] temp = tds.get(3).text().split(" / ");
                        ArrayList<String> bands = new ArrayList<>(Arrays.asList(temp));
                        for (String band : bands) {
                            Log.d(TAG, tds.get(0).text() + ", " + tds.get(1).text().trim() + ", " + Helper.getBand(band, "UMTS").getFrequency());
                            networks.add(new Network(tds.get(0).text(), tds.get(1).text().trim(), Helper.getBand(band, "UMTS")));
                        }
                    }
                    else {
                        Log.d(TAG, tds.get(0).text() + ", " + tds.get(1).text().trim() + ", " + Helper.getBand(tds.get(3).text(), "UMTS").getFrequency());
                        networks.add(new Network(tds.get(0).text(), tds.get(1).text().trim(), Helper.getBand(tds.get(3).text(), "UMTS")));
                    }

                }
            }

        }
    }
}

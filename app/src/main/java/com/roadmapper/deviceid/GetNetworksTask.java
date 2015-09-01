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
            Document doc = null, doc2 = null;
            try {
                doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_LTE_networks").get();
                doc2 = Jsoup.connect(
                        "https://en.wikipedia.org/wiki/List_of_LTE_networks_in_Europe").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            getLTE(doc, country, false);
            getLTE(doc2, country, true); // Handle Europe differently
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
                    if (tds.get(3).text().contains("/")) {
                        String[] temp = tds.get(3).text().replaceAll(" ", "").split("/");
                        ArrayList<String> bands = new ArrayList<>(Arrays.asList(temp));
                        for (String band : bands) {
                            Log.d(TAG, tds.get(0).text() + ", " + tds.get(1).text().trim() + ", "
                                    + Helper.getBand(band, "UMTS").getFrequency());
                            networks.add(new Network(tds.get(0).text(), tds.get(1).text().trim(),
                                    Helper.getBand(band, "UMTS")));
                        }
                    } else {
                        Log.d(TAG, tds.get(0).text() + ", " + tds.get(1).text().trim() + ", "
                                + Helper.getBand(tds.get(3).text(), "UMTS").getFrequency());
                        networks.add(new Network(tds.get(0).text(), tds.get(1).text().trim(),
                                Helper.getBand(tds.get(3).text(), "UMTS")));
                    }

                }
            }

        }
    }

    private void getLTE(Document doc, String country, boolean isEurope) {
        if (isEurope)
            getNetworksLTE(doc.getElementById("Commercial_deployments")
                    .parent().nextElementSibling(), country);
        else {
            ArrayList<Element> regions = new ArrayList<>();
            // weird divs before tables on visible on wiki
            regions.add(doc.getElementById("Africa").parent().nextElementSibling().child(0));
            regions.add(doc.getElementById("Caribbean").parent().nextElementSibling().child(0));
            regions.add(
                    doc.getElementById("South_America_and_Central_America_.28APT_band_plan.29")
                            .parent().nextElementSibling().child(0));
            regions.add(
                    doc.getElementById("USA.2C_US_Territories_.26_Canada_.28FCC_band_plan.29")
                            .parent().nextElementSibling().nextElementSibling().child(0));
            // wiki table has a paragraph after this that needs to be skipped
            // TODO what do do about grayed out ones?
            regions.add(doc.getElementById("Asia").parent().nextElementSibling().child(0));
            //regions.add(doc.getElementById("Europe")); wrong
            // https://en.wikipedia.org/wiki/List_of_LTE_networks_in_Europe
            regions.add(doc.getElementById("Middle_East").parent().nextElementSibling().child(0));
            regions.add(doc.getElementById("Oceania").parent().nextElementSibling().child(0));

            for (Element region : regions) {
                getNetworksLTE(region, country);
            }
        }


    }

    private void getNetworksLTE(Element table, String country) {
        Elements rows = table.select("tr");
        Log.d(TAG, "" + rows.size());
        String tblCountry = "";
        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements tds = row.select("td");
            //Log.d(TAG, tds.get(0).text() + ":" + tds.get(1).text());
            if (!tds.get(0).attr("rowspan").equals("") && tds.get(0).children().size() != 0 && tds.get(0).child(0).attr("class").equals("flagicon")) {
                tblCountry = tds.get(0).text();
                if (tds.get(0).text().contains(country)) {
                    if (!(tds.get(3).text().contains("?"))) { // Band should not be "?"
                        Log.d(TAG, tblCountry + ", " + tds.get(1).text().trim() + ", " + Helper.getBand(cleanFootnote(tds.get(3).text()), "LTE").getFrequency());
                        networks.add(new Network(tds.get(1).text().trim(), tblCountry, Helper.getBand(cleanFootnote(tds.get(3).text()), "LTE")));
                    }
                }
            } else if (tds.get(0).attr("rowspan").equals("") && tds.get(0).children().size() != 0 && tds.get(0).child(0).attr("class").equals("flagicon")) {
                if (tds.get(0).text().contains(country)) {
                    if (!(tds.get(3).text().contains("?"))) { // Band should not be "?"
                        Log.d(TAG, tds.get(0).text() + ", " + tds.get(1).text().trim() + ", " + Helper.getBand(cleanFootnote(tds.get(3).text()), "LTE").getFrequency());
                        networks.add(new Network(tds.get(1).text().trim(), tds.get(0).text(), Helper.getBand(cleanFootnote(tds.get(3).text()), "LTE")));
                    }
                }
            } else {
                if (tblCountry.contains(country)) {
                    if (!(tds.get(2).text().contains("?"))) { // Band should not be "?"
                        Log.d(TAG, tblCountry + ", " + tds.get(0).text().trim() + ", " + Helper.getBand(cleanFootnote(tds.get(2).text()), "LTE").getFrequency());
                        networks.add(new Network(tds.get(0).text().trim(), tblCountry, Helper.getBand(cleanFootnote(tds.get(2).text()), "LTE")));
                    }
                }
            }


        }
    }

    private String cleanFootnote(String input) {
        return input.replaceAll("\\[\\d*\\]", "");
    }
}

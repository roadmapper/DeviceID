package com.vinay.deviceid;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetNetworksTask extends AsyncTask<ArrayList<String>, Void, Void> {

    @Override
    protected Void doInBackground(ArrayList<String>... arrayLists) {
        if (arrayLists[0].contains(Helper.Technology.UMTS)) {
            Document doc2 = null;
            try {
                doc2 = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_UMTS_networks").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element region1 = doc2.getElementById("Africa");
            Element table = region1.parent().nextElementSibling();
            Elements rows = table.select("tr");
            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements tds = row.select("td");
                System.out.println(tds.get(0).text() + ":" + tds.get(1).text());

            }
        }
        if (arrayLists[0].contains(Helper.Technology.LTE)) {
        }
        return null;
    }

    /*
    Document doc2 = null;
	        try {
	            doc2 = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_UMTS_networks").get();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        Element region1 = doc2.getElementById("Africa");
	        Element table = region1.parent().nextElementSibling();
	        Elements rows = table.select("tr");
	        for (int i = 1; i < rows.size(); i++) {
	        	Element row = rows.get(i);
	        	Elements tds = row.select("td");
	            System.out.println(tds.get(0).text() + ":" + tds.get(1).text());

	        }
     */
}

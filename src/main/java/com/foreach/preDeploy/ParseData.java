package com.foreach.preDeploy;

import org.jsoup.Jsoup;
import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ParseData {
    public static final String SITE_URL = "https://www.iban.com/currency-codes";
    public static final  String RATES_SITE_URL = "https://www.cbr-xml-daily.ru/latest.js";
    public static final int CURRENCY_CODE = 2;
    public static final int CURRENCY_NAME = 1;

    public static List<String> getDataCurrency() {
        List<String> ret_arr = new ArrayList<>();
        HashSet<String> checkEntity = new HashSet<>();
        try {

            var document = Jsoup.connect(SITE_URL).get();

            var responce  = document.select("tr");
            for (var element : responce) {

                String currencyName = "";
                String currencyCode = "";

                int i = 0;

                for (var el : element.children()) {
                    if (i == CURRENCY_NAME) {
                        if (el.text().equals("Currency")) continue;
                        if (el.text().equals("Number")) continue;
                        if (el.text().equals("Code")) continue;
                        if (el.text().equals("No universal currency")) continue;

                        currencyName = el.text();
                    } else if (i == CURRENCY_CODE) {
                        if (el.text().equals("Code")) continue;
                        if (el.text().equals("Number")) continue;
                        currencyCode = el.text();
                    }
                    i++;

                }
                if (!(currencyName.length() == 0)
                    && !checkEntity.contains(currencyCode)
                ) {
                    ret_arr.add(currencyName);
                    ret_arr.add(currencyCode);
                    checkEntity.add(currencyCode);
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret_arr;
    }
    public static List<String> getDataRates() {
        List<String> ret_arr = new ArrayList<>();

        try {
            String jsonString = Jsoup.connect(RATES_SITE_URL).ignoreContentType(true).execute().body();

            JSONObject obj = new JSONObject(jsonString);

            obj.getJSONObject("rates").keys().forEachRemaining(key -> {
                Object rate =  obj.getJSONObject("rates").get(key);
                String targetCurrency = key;

                ret_arr.add(targetCurrency);
                ret_arr.add(rate.toString());
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ret_arr;
    }

}

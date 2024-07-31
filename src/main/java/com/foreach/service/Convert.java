package com.foreach.service;

import com.foreach.model.dao.RateServeiceMySQL;
import com.foreach.model.dto.RatesDTO;
import org.json.JSONObject;

public class Convert {
    public void getConvertedAmount(String base, String target, int amount, JSONObject json ) {
        float convertedAmount = 0;
        RateServeiceMySQL rateServiceMySQL = new RateServeiceMySQL();

        RatesDTO resultRate = rateServiceMySQL.getRateByBaseAndTargetOnCode(base, target);


        if (resultRate.id != 0 ) {
            convertedAmount = amount * resultRate.rate;
            json.put("rate", resultRate.rate);
            json.put("convertedAmount", String.format("%.2f",convertedAmount));
        } else {
            RatesDTO resultRate2 = rateServiceMySQL.getRateByBaseAndTargetOnCode(target, base);
            if ( resultRate2 != null) {
                convertedAmount = amount / resultRate2.rate;
                json.put("rate", 1 / resultRate2.rate);
                json.put("convertedAmount", String.format("%.2f",convertedAmount));
            }
        }
    }
}

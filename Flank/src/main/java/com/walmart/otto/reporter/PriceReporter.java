package com.walmart.otto.reporter;


import com.walmart.otto.Constants;
import com.walmart.otto.configurator.ConfigReader;
import com.walmart.otto.configurator.Configurator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceReporter {
    private static Configurator configurator;

    private static HashMap<String, BigDecimal> estimates = new HashMap<String, BigDecimal>();

    public static HashMap<String, BigDecimal>  getTotalPrice(List<Integer> times)  {

        configurator = new ConfigReader(Constants.CONFIG_PROPERTIES).getConfiguration();

        HashMap<String, Double> mapOfPrices = configurator.getPricePerMinForDevice();

        for (Map.Entry<String, Double> entry : mapOfPrices.entrySet()) {
            BigDecimal price = new BigDecimal(0.00);
            for(Integer executionTime: times) {
                try {
                    price = price.add(calculatePrice(executionTime, entry.getValue()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                estimates.put(entry.getKey(), price);
            }
        }
        return estimates;
    }

    public static BigDecimal calculatePrice(Number totalTimeInSecs, double pricePerMin) throws ParseException {

        double time = totalTimeInSecs.doubleValue()/60;
        int roundedTime = (int) Math.ceil(time);
        double actualPrice = roundedTime * pricePerMin;

        BigDecimal a = new BigDecimal(actualPrice);
        BigDecimal roundOffPrice = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return roundOffPrice;
    }


}
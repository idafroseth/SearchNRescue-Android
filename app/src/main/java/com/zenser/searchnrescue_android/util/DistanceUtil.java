package com.zenser.searchnrescue_android.util;

import java.text.DecimalFormat;

public class DistanceUtil {

    public static final double ONE_THOUSAND = 1000d;
    public static final double ONE_MILLION = 1000000d;
    public static final double TEN_THOUSAND = 10000d;

    public static String printWithBestDistanceScale(double distanceInMeters) {
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        if (distanceInMeters > ONE_MILLION) {
            double distanceInScandinavianMil = distanceInMeters / TEN_THOUSAND;
            return "Avstand: ca " + decimalFormat.format(distanceInScandinavianMil) + " mil.";
        }

        if (distanceInMeters > ONE_THOUSAND) {
            double distanceInKilometers = distanceInMeters / ONE_THOUSAND;
            return "Avstand: ca " + decimalFormat.format(distanceInKilometers) + " kilometer.";
        } else {

            decimalFormat = new DecimalFormat("###");
            return "Avstand: " + decimalFormat.format(distanceInMeters) + " meter.";
        }
    }
}

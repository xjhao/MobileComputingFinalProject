package edu.wpi.zqinxhao.playerpooling.model;

import java.util.Map;

/**
 * Created by xhao on 4/22/16.
 */
public class DistanceUtils {
    public static double getDistance (Map<String, String> src, Map<String, String> dst) {
        double srcLa = Double.parseDouble(src.get("latitude"));
        double srcLo = Double.parseDouble(src.get("longitude"));
        double dstLa = Double.parseDouble(dst.get("latitude"));
        double dstLo = Double.parseDouble(dst.get("longitude"));

        return Math.sqrt(Math.pow((srcLa - dstLa), 2) + Math.pow((srcLo - dstLo), 2));
    }
}

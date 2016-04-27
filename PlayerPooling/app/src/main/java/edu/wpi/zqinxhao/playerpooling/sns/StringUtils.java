package edu.wpi.zqinxhao.playerpooling.sns;

/**
 * Created by xhao on 4/26/16.
 */
public class StringUtils {
    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }

        if (s.length() < 1) {
            return true;
        }

        return false;
    }

    public static boolean isBlank(String s) {
        if (isEmpty(s)) {
            return true;
        }

        if (isEmpty(s.trim())) {
            return true;
        }

        return false;
    }
}

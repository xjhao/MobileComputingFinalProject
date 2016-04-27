package edu.wpi.zqinxhao.playerpooling.sns;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by xhao on 4/26/16.
 */
public class SampleMessageGenerator {
    /*
	 * This message is delivered if a platform specific message is not specified
	 * for the end point. It must be set. It is received by the device as the
	 * value of the key "default".
	 */
    public static final String defaultMessage = "This is the default message";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static enum Platform {
        // Google Cloud Messaging
        GCM;
    }

    public static String jsonify(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw (RuntimeException) e;
        }
    }


    public static String getAndroidMessage(String message) {
        Map<String, String> payload = new HashMap<String, String>();
        Map<String, Object> androidMessageMap = new HashMap<String, Object>();

        payload.put("message", message);

        androidMessageMap.put("collapse_key", "Welcome");
        androidMessageMap.put("data", payload);
        androidMessageMap.put("delay_while_idle", true);
        androidMessageMap.put("time_to_live", 12555);
        androidMessageMap.put("dry_run", false);
        return jsonify(androidMessageMap);
    }

}

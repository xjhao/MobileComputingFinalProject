package edu.wpi.zqinxhao.playerpooling.sns;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import edu.wpi.zqinxhao.playerpooling.sns.SampleMessageGenerator.Platform;
import edu.wpi.zqinxhao.playerpooling.sns.AmazonSNSClientWrapper;

/**
 * Created by xhao on 4/26/16.
 */
public class SNSMobilePush {
    private AmazonSNSClientWrapper snsClientWrapper;

    public SNSMobilePush(AmazonSNS snsClient) {
        this.snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
    }

    public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();
    static {
        attributesMap.put(Platform.GCM, null);
    }


    public void demoAndroidAppNotification(String registrationId) {
        // TODO: Please fill in following values for your application. You can
        // also change the notification payload as per your preferences using
        // the method
        // com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAndroidMessage()
        String serverAPIKey = "AIzaSyD6dQqQN27e614XK0OsjxXP0uAjsdk3qCU";
        String applicationName = "PlayerPooling";
        //String registrationId = "fMDlkOYq7gc:APA91bH1uMusx5PzhWItzVnhN7cSYHh14_A4Yjta2wy9vhP9QV6XMwZ-h4fxwCfL0RMj_9I7YH9CJw8RM8mWnBurcs1z5RrCOOFAedNDzIyZpH-7IqlmA_tHCz2zi1wBqni0NtVNIWOI";
        snsClientWrapper.demoNotification(Platform.GCM, "", serverAPIKey,
                registrationId, applicationName, attributesMap);
    }

}

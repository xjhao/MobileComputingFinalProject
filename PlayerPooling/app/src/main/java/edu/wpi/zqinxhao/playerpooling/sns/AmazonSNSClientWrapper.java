package edu.wpi.zqinxhao.playerpooling.sns;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeletePlatformApplicationRequest;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;

import edu.wpi.zqinxhao.playerpooling.model.Constants;
import edu.wpi.zqinxhao.playerpooling.sns.SampleMessageGenerator.Platform;

/**
 * Created by xhao on 4/26/16.
 */
public class AmazonSNSClientWrapper {
    private final AmazonSNS snsClient;

    public AmazonSNSClientWrapper(AmazonSNS client) {
        this.snsClient = client;
    }

//    private CreatePlatformEndpointResult createPlatformEndpoint(
//            String customData, String platformToken,
//            String applicationArn) {
//        CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
//        platformEndpointRequest.setCustomUserData(customData);
//        String token = platformToken;
//        String userId = null;
//        platformEndpointRequest.setToken(token);
//        platformEndpointRequest.setPlatformApplicationArn(applicationArn);
//        return snsClient.createPlatformEndpoint(platformEndpointRequest);
//    }

    public String createPlatformTopic(String topicName) {
        //create a new SNS topic
        CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);
        CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);

        //print TopicArn
        System.out.println(createTopicResult);
        //get request id for CreateTopicRequest from SNS metadata
        System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));

        return createTopicResult.getTopicArn();
    }

    public void deleteplatformTopic(String topicARN) {
        //delete an SNS topic
        DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicARN);
        snsClient.deleteTopic(deleteTopicRequest);
        //get request id for DeleteTopicRequest from SNS metadata
        System.out.println("DeleteTopicRequest - " + snsClient.getCachedResponseMetadata(deleteTopicRequest));
    }

    public PublishResult publishToTopic(String topicArn, Platform platform,
    Map<Platform, Map<String, MessageAttributeValue>> attributesMap, String androidMessage) {
        PublishRequest publishRequest = new PublishRequest();
        Map<String, MessageAttributeValue> notificationAttributes = getValidNotificationAttributes(attributesMap
                .get(platform));
        if (notificationAttributes != null && !notificationAttributes.isEmpty()) {
            publishRequest.setMessageAttributes(notificationAttributes);
        }
        publishRequest.setMessageStructure("json");
        // If the message attributes are not set in the requisite method,
        // notification is sent with default attributes
        String message = SampleMessageGenerator.getAndroidMessage(androidMessage);
        Map<String, String> messageMap = new HashMap<String, String>();
        messageMap.put(platform.name(), message);
        messageMap.put("default", "Please use GCM");
        message = SampleMessageGenerator.jsonify(messageMap);
        // For direct publish to mobile end points, topicArn is not relevant.
        publishRequest.setTopicArn(topicArn);

        // Display the message that will be sent to the endpoint/
        System.out.println("{Message Body: " + message + "}");
        StringBuilder builder = new StringBuilder();
        builder.append("{Message Attributes: ");
        for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes
                .entrySet()) {
            builder.append("(\"" + entry.getKey() + "\": \""
                    + entry.getValue().getStringValue() + "\"),");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        System.out.println(builder.toString());

        publishRequest.setMessage(message);
        return snsClient.publish(publishRequest);
    }

    public void subscribeToTopic(String topicARN, String endpointARN) {
        //subscribe to an SNS topic
        SubscribeRequest subRequest = new SubscribeRequest(topicARN, "application", endpointARN);
        snsClient.subscribe(subRequest);
        //get request id for SubscribeRequest from SNS metadata
        System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
    }

    public String createPlatformEndpoint(String customData, String platformToken,
                                          String applicationArn) {
        CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
        platformEndpointRequest.setCustomUserData(customData);
        String token = platformToken;
        String userId = null;
        platformEndpointRequest.setToken(token);
        platformEndpointRequest.setPlatformApplicationArn(applicationArn);
        return snsClient.createPlatformEndpoint(platformEndpointRequest).getEndpointArn();
    }

    public PublishResult publishToEndpoint(String endpointArn, Platform platform,
                                  Map<Platform, Map<String, MessageAttributeValue>> attributesMap,
                                           String androidMessage) {
        PublishRequest publishRequest = new PublishRequest();
        Map<String, MessageAttributeValue> notificationAttributes = getValidNotificationAttributes(attributesMap
                .get(platform));
        if (notificationAttributes != null && !notificationAttributes.isEmpty()) {
            publishRequest.setMessageAttributes(notificationAttributes);
        }
        publishRequest.setMessageStructure("json");
        // If the message attributes are not set in the requisite method,
        // notification is sent with default attributes
        String message = SampleMessageGenerator.getAndroidMessage(androidMessage);
        Map<String, String> messageMap = new HashMap<String, String>();
        messageMap.put(platform.name(), message);
        message = SampleMessageGenerator.jsonify(messageMap);
        // For direct publish to mobile end points, topicArn is not relevant.
        publishRequest.setTargetArn(endpointArn);

//        // Display the message that will be sent to the endpoint/
//        System.out.println("{Message Body: " + message + "}");
//        StringBuilder builder = new StringBuilder();
//        builder.append("{Message Attributes: ");
//        for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes
//                .entrySet()) {
//            builder.append("(\"" + entry.getKey() + "\": \""
//                    + entry.getValue().getStringValue() + "\"),");
//        }
//        builder.deleteCharAt(builder.length() - 1);
//        builder.append("}");
//        System.out.println(builder.toString());

        publishRequest.setMessage(message);
        return snsClient.publish(publishRequest);
    }


    public static Map<String, MessageAttributeValue> getValidNotificationAttributes(
            Map<String, MessageAttributeValue> notificationAttributes) {
        Map<String, MessageAttributeValue> validAttributes = new HashMap<String, MessageAttributeValue>();

        if (notificationAttributes == null) return validAttributes;

        for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes
                .entrySet()) {
            if (!StringUtils.isBlank(entry.getValue().getStringValue())) {
                validAttributes.put(entry.getKey(), entry.getValue());
            }
        }
        return validAttributes;
    }
}

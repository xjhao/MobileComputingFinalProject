package edu.wpi.zqinxhao.playerpooling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.zqinxhao.playerpooling.sns.AmazonSNSClientWrapper;
import edu.wpi.zqinxhao.playerpooling.sns.SampleMessageGenerator;

import static edu.wpi.zqinxhao.playerpooling.sns.SampleMessageGenerator.*;

public class HostGameActivity extends AppCompatActivity {
    public static boolean isFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        final Button bPushNotification = (Button) findViewById(R.id.bPushNotification);


        final Map<SampleMessageGenerator.Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<SampleMessageGenerator.Platform, Map<String, MessageAttributeValue>>();
        attributesMap.put(Platform.GCM, null);


        bPushNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask(){
                    @Override
                    protected Object doInBackground(Object[] params) {
                        AmazonSNS sns = null;
                        try {
                            AssetManager am = getApplicationContext().getAssets();
                            InputStream is = am.open("AwsCredentials.properties");
                            PropertiesCredentials pc = new PropertiesCredentials(is);
                            sns = new AmazonSNSClient(pc);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sns.setEndpoint("https://sns.us-west-2.amazonaws.com");

                        try {
                            String endpointARN = LoginActivity.getLoginUser().getEndpointARN();
                            AmazonSNSClientWrapper snsClientWrapper = new AmazonSNSClientWrapper(sns);
                            String topicARN = snsClientWrapper.createPlatformTopic("testTopic");
                            //snsClientWrapper.publishToEndpoint(endpointARN, Platform.GCM, attributesMap, "hello baobao");
                            //snsClientWrapper.publishToTopic(topicARN, Platform.GCM, attributesMap, "oh yeah!");
                            //snsClientWrapper.ptt(topicARN, Platform.GCM, attributesMap, "ohhoho");
                            //snsClientWrapper.deleteplatformTopic(topicARN);
                            snsClientWrapper.subscribeToTopic(topicARN, endpointARN);

                        } catch (AmazonServiceException ase) {
                            System.out
                                    .println("Caught an AmazonServiceException, which means your request made it "
                                            + "to Amazon SNS, but was rejected with an error response for some reason.");
                            System.out.println("Error Message:    " + ase.getMessage());
                            System.out.println("HTTP Status Code: " + ase.getStatusCode());
                            System.out.println("AWS Error Code:   " + ase.getErrorCode());
                            System.out.println("Error Type:       " + ase.getErrorType());
                            System.out.println("Request ID:       " + ase.getRequestId());
                        } catch (AmazonClientException ace) {
                            System.out
                                    .println("Caught an AmazonClientException, which means the client encountered "
                                            + "a serious internal problem while trying to communicate with SNS, such as not "
                                            + "being able to access the network.");
                            System.out.println("Error Message: " + ace.getMessage());
                        }
                        return true;
                    }
                }.execute(null, null, null);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;

        LocalBroadcastManager.getInstance(this).registerReceiver(mRequestBroadcastReceiver,
                new IntentFilter("REQUEST"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRequestBroadcastReceiver);
    }

    private BroadcastReceiver mRequestBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
            if(i.getAction().equals("REQUEST")) {
                Toast.makeText(HostGameActivity.this, i.getStringExtra("message"),
                        Toast.LENGTH_LONG).show();
            }
        }
    };
}

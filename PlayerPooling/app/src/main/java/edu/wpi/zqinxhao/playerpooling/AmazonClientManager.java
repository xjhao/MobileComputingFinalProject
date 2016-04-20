package edu.wpi.zqinxhao.playerpooling;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
/**
 * Created by zishanqin on 4/20/16.
 */
public class AmazonClientManager {
    private static final String LOG_TAG="AmazonClientManager";
    private AmazonDynamoDBClient ddb=null;
    private Context context;

    public AmazonClientManager(Context context){
        this.context=context;
    }
    public void validateCredentials(){
        if(ddb==null){
            //first launch
            initClients();
        }
    }

    private void initClients() {

        CognitoCachingCredentialsProvider credentials=new CognitoCachingCredentialsProvider(context,
                Constants.IDENTITY_POOL_ID,
                Regions.US_EAST_1);
        ddb =new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.US_WEST_2));
    }
    public AmazonDynamoDBClient getDDB(){
        return this.ddb;
    }
}

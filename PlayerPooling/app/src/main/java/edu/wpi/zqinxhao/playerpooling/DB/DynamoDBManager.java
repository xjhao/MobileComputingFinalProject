package edu.wpi.zqinxhao.playerpooling.DB;

import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import edu.wpi.zqinxhao.playerpooling.LoginActivity;
import edu.wpi.zqinxhao.playerpooling.model.User;

/**
 * Created by zishanqin on 4/20/16.
 */
public class DynamoDBManager {
    private  static final String TAG="DynamoDbManager";
    public static void insertUser(User userInserted){
        try {
            AmazonDynamoDB ddb = LoginActivity.getAmzClientManager().getDDB();
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
            mapper.save(userInserted);
        }catch(AmazonServiceException ex){
            Log.e(TAG, "Error when insert User");

        }
    }
    public boolean authenticateUser(String username, String passwordEncrption){

        try {
            AmazonDynamoDB ddb = LoginActivity.getAmzClientManager().getDDB();
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
            User user=mapper.load(User.class, username);
//            if(user.getPassword().equals(passwordEncrption)){
//                return true;
//            }else{
//                return false;
//            }
        }catch(AmazonServiceException ex){
            Log.e(TAG, "Error when authenticate User");

        }
    }
}

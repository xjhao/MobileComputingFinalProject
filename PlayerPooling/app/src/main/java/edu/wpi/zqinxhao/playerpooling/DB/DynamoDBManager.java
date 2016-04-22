package edu.wpi.zqinxhao.playerpooling.DB;

import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import edu.wpi.zqinxhao.playerpooling.LoginActivity;
import edu.wpi.zqinxhao.playerpooling.exceptions.DuplicateEmailException;
import edu.wpi.zqinxhao.playerpooling.model.Constants;
import edu.wpi.zqinxhao.playerpooling.model.User;


public class DynamoDBManager {
    private  static final String TAG="DynamoDbManager";

    public static boolean insertUser(User userInserted) throws AmazonServiceException, DuplicateEmailException{
        try {
            AmazonDynamoDBClient ddb = LoginActivity.getAmzClientManager().getDDB();
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
            if (isExistUser(userInserted)) {
                throw new DuplicateEmailException();
                //return false;
            }
            mapper.save(userInserted);
            return true;
        }catch(AmazonServiceException ex){
            Log.e(TAG, "Error when insert User");
            return false;
        }
    }

    private static boolean isExistUser(User userInserted) {
        AmazonDynamoDBClient ddb = LoginActivity.getAmzClientManager().getDDB();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        User user = mapper.load(User.class, userInserted.getEmail());
        return user != null;
    }

    public static String getUserTableStatus() {

        try {
            AmazonDynamoDBClient ddb = LoginActivity.getAmzClientManager()
                    .getDDB();

            DescribeTableRequest request = new DescribeTableRequest()
                    .withTableName(Constants.USER_TABLE_NAME);
            DescribeTableResult result = ddb.describeTable(request);

            String status = result.getTable().getTableStatus();
            return status == null ? "" : status;

        } catch (ResourceNotFoundException e) {
        } catch (AmazonServiceException ex) {
            Log.e(TAG,"");
        }

        return "";
    }

    public static boolean authenticateUser(String userEmail, String userPwd)throws AmazonServiceException{

        try {
            AmazonDynamoDBClient ddb = LoginActivity.getAmzClientManager().getDDB();
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
            User user = mapper.load(User.class, userEmail);

            if (user == null) {
                return false;
            }

            if(user.getHashPassword().equals(userPwd)){
                LoginActivity.setLoginUser(user);
                return true;
            }else{
                return false;
            }
        }catch(AmazonServiceException ex){
            Log.e(TAG, "Error when authenticate User");
            return false;

        }
    }


}

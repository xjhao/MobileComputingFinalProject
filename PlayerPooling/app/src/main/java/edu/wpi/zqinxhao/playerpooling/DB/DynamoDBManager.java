package edu.wpi.zqinxhao.playerpooling.DB;

import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.zqinxhao.playerpooling.LoginActivity;
import edu.wpi.zqinxhao.playerpooling.exceptions.DuplicateEmailException;
import edu.wpi.zqinxhao.playerpooling.model.Constants;
import edu.wpi.zqinxhao.playerpooling.model.DistanceUtils;
import edu.wpi.zqinxhao.playerpooling.model.Game;
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
    public static boolean insertGame(Game game){
        try {
            AmazonDynamoDBClient ddb = LoginActivity.getAmzClientManager().getDDB();
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
            mapper.save(game);
            return true;
        }catch(AmazonServiceException ex){
            Log.e(TAG, "Error when insert Game");
            return false;
        }

    }
    public static ArrayList<Game> searchGames(LatLng location){
        try {
            AmazonDynamoDBClient ddb = LoginActivity.getAmzClientManager().getDDB();
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
            Map<String, AttributeValue> expressionForOnlyActiveGames = new HashMap<String, AttributeValue>();
            expressionForOnlyActiveGames.put(":val", new AttributeValue().withS(Constants.ACTIVE_STATE));
            DynamoDBScanExpression expression = new DynamoDBScanExpression().withFilterExpression("game_status = :val").withExpressionAttributeValues(expressionForOnlyActiveGames);
            List<Game> scanResult= mapper.scan(Game.class, expression);

            for(Game g: scanResult){
                double distanceMax=Double.parseDouble(g.getMaxDistance());
                Map<String,String> gameLoc = g.getLocation();
                Map<String, String> requesterLoc= new HashMap<String, String>() ;
                requesterLoc.put(Constants.LATITUDE, String.valueOf(location.latitude));
                requesterLoc.put(Constants.LONGTITUDE, String.valueOf(location.longitude));
                if(DistanceUtils.getDistance(requesterLoc,gameLoc)>distanceMax){
                    scanResult.remove(g);
                }
            }
            ArrayList<Game> result=  new ArrayList<Game>();
            result.addAll(scanResult);
            return scanResult==null?new ArrayList<Game>(): result;

        }catch(AmazonServiceException ex){
            Log.e(TAG, "Error when authenticate User");
            return new ArrayList<Game>();

        }
        //return new ArrayList<Game>();
    }
    public static String getGameTableStatus() {

        try {
            AmazonDynamoDBClient ddb = LoginActivity.getAmzClientManager()
                    .getDDB();

            DescribeTableRequest request = new DescribeTableRequest()
                    .withTableName(Constants.GAME_TABLE_NAME);
            DescribeTableResult result = ddb.describeTable(request);

            String status = result.getTable().getTableStatus();
            return status == null ? "" : status;

        } catch (ResourceNotFoundException e) {
        } catch (AmazonServiceException ex) {
            Log.e(TAG,"");
        }

        return "";
    }
}

package edu.wpi.zqinxhao.playerpooling.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.Map;

/**
 * Created by xhao on 4/22/16.
 */
@DynamoDBTable(tableName="Game")
public class Game {

    private String host;

    private String creatTime;

    private String hostNumber;

    private String gameName;

    private int playerNum;

    private int maxDistance;

    private Map<String, String> location;

    private String description;

    private String status;

    @DynamoDBHashKey(attributeName="host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    @DynamoDBRangeKey(attributeName="creatTime")
    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }
    @DynamoDBAttribute(attributeName = "gameName")
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    @DynamoDBAttribute(attributeName = "playerNum")
    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }
    @DynamoDBAttribute(attributeName = "maxDistance")
    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }
    @DynamoDBAttribute(attributeName = "location")
    public Map<String,String> getLocation() {
        return location;
    }

    public void setLocation(Map<String, String> location) {
        this.location = location;
    }
    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @DynamoDBAttribute(attributeName = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDBAttribute(attributeName = "hostNumber")
    public String getHostNumber() {
        return hostNumber;
    }

    public void setHostNumber(String hostNumber) {
        this.hostNumber = hostNumber;
    }
}

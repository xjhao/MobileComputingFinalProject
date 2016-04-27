package edu.wpi.zqinxhao.playerpooling.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xhao on 4/22/16.
 */
@DynamoDBTable(tableName="Game")
public class Game implements Parcelable{

    private String host;

    private String creatTime;

    private String hostNumber;

    private String gameName;

    private int playerNum;

    private String maxDistance;

    private Map<String, String> location;

    private String gameAddress;

    private String description;

    private String game_status;


    @DynamoDBAttribute(attributeName="hostArn")
    private String hostArn;

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
    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }
    @DynamoDBAttribute(attributeName = "location")
    public Map<String,String> getLocation() {
        return location;
    }

    public void setGameAddress(String gameAddress) {
        this.gameAddress = gameAddress;
    }
    @DynamoDBAttribute(attributeName = "gameAddress")
    public String getGameAddress() {
        return gameAddress;
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
    @DynamoDBAttribute(attributeName = "game_status")
    public String getGameStatus() {
        return game_status;
    }

    public void setGameStatus(String status) {
        this.game_status = status;
    }

    @DynamoDBAttribute(attributeName = "hostNumber")
    public String getHostNumber() {
        return hostNumber;
    }

    public void setHostNumber(String hostNumber) {
        this.hostNumber = hostNumber;
    }

    public void setHostArn(String hostArn) {
        this.hostArn = hostArn;
    }
    @DynamoDBAttribute(attributeName = "hostArn")
    public String getHostArn() {
        return hostArn;
    }


    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(host);
        dest.writeString(creatTime);
        dest.writeString(hostNumber);
        dest.writeString(gameName);
        dest.writeInt(playerNum);
        dest.writeString(maxDistance);
        dest.writeInt(location.size());
        for (String s: location.keySet()) {
            dest.writeString(s);
            dest.writeString(location.get(s));
        }
        dest.writeString(gameAddress);
        dest.writeString(description);
        dest.writeString(game_status);
    }
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
    public Game(Parcel in){
        host= in.readString();
        creatTime= in.readString();
        hostNumber=in.readString();
        gameName= in.readString();
        playerNum=in.readInt();
        maxDistance=in.readString();
        location=new HashMap<String,String>();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            location.put(in.readString(), in.readString());
        }
        gameAddress=in.readString();
        description = in.readString();
        game_status=in.readString();
    }
    public Game(){};
}

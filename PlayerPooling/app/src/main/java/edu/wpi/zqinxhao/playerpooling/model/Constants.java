package edu.wpi.zqinxhao.playerpooling.model;

/**
 * Created by zishanqin on 4/20/16.
 */
public class Constants {
    //AWS Cognico use, to verify the unauthorized user
    public static final String IDENTITY_POOL_ID="us-east-1:f5b96688-8ecc-40ca-990b-4f9ed0caaef0";
    public static final String USER_TABLE_NAME="User";

    public static final String GAME_TABLE_NAME ="Game" ;
    public static final String ACTIVE_STATE ="GAME_ACTIVE" ;
    public static final String INACTIVE_STATE ="GAME_INACTIVE" ;
    public static final String CLOSE_STATE ="GAME_CLOSE" ;

    public static final String LONGTITUDE="longitude";//jing du
    public static final String LATITUDE="latitude";// wei du
    public static final String GOOGLE_API_KEY = "AIzaSyDdvysQRBY90cIkqtVsz4qN3uJnsExdnpE";
}

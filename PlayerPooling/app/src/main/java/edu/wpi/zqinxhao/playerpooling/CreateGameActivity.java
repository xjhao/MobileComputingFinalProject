package edu.wpi.zqinxhao.playerpooling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.wpi.zqinxhao.playerpooling.model.Game;

public class CreateGameActivity extends AppCompatActivity {
    private static Game gameCreated;
    public static Game getGame() {
        return gameCreated;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        //for test only
//        Game testGame=new Game();
//        testGame.setHost("zqin@wpi.edu");
//        testGame.setCreatTime(new Date().toString());
//        testGame.setDescription("This is a test Game");
//        testGame.setGameName("blackjack");
//        Map<String, String> loc=new HashMap<String, String>();
//        loc.put("latitude","30.3333");
//        loc.put("langtitude","20.000");
//        testGame.setLocation(loc);
//        testGame.setMaxDistance(5000);
//        testGame.setPlayerNum(5);
//        testGame.setStatus("Active");
//        testGame.setHostNumber("5086671535");
//        gameCreated=testGame;
//        try {
//            //DynamoDBManager.insertUser(user);
//            DynamoDBManagerTask insertGameTask=new DynamoDBManagerTask();
//            //insertGameTask.setActivity(CreateGameActivity.this);
//            //insertUserTask.setUser(user);
//            insertGameTask.setGame(gameCreated);
//            insertGameTask.execute(DynamoDBManagerType.INSEERT_GAME);
//
//        }catch(AmazonServiceException e) {
//            //success = false;
//        }
//

    }
}

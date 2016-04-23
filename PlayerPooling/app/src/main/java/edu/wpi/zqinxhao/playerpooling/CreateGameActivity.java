package edu.wpi.zqinxhao.playerpooling;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

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
        final EditText etGameName = (EditText) findViewById(R.id.etGameName);
        final EditText etPlayerNum = (EditText) findViewById(R.id.etPlayerNum);
        final EditText etMaxDistance = (EditText) findViewById(R.id.etMaxDistance);
        final EditText etLocation = (EditText) findViewById(R.id.etLocation);
        final EditText etPhone = (EditText) findViewById(R.id.etPhone);
        final EditText etDescription = (EditText) findViewById(R.id.etDescription);
        final ImageButton ibGoogleMap = (ImageButton) findViewById(R.id.ibGoogleMap);
        final Button bCreateGame = (Button) findViewById(R.id.bCreateGame);

        ibGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        //for test only
//        Game testGame=new Game();
//        testGame.setHost("zqin@wpi.edu");
//        testGame.setCreatTime(new Date().toString());
//        testGame.setDescription("This is a test Game");
//        testGame.setGameName("blackjack");
//        Map<String, String> loc=new HashMap<String, String>();
//        loc.put("latitude","30.3333");
//        loc.put("longitude","20.000");
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

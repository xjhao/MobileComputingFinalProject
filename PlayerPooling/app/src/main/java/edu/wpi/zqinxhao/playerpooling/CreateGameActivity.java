package edu.wpi.zqinxhao.playerpooling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amazonaws.AmazonServiceException;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManagerTask;
import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManagerType;
import edu.wpi.zqinxhao.playerpooling.model.Constants;
import edu.wpi.zqinxhao.playerpooling.model.Game;

public class CreateGameActivity extends AppCompatActivity {
    private static Game gameCreated;
    Map<String,String> latlng=new HashMap<String,String>();
    public static Game getGame() {
        return gameCreated;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        final Button bCreateGame = (Button) findViewById(R.id.bCreateGame);
        final ImageButton ibGoogleMap = (ImageButton) findViewById(R.id.ibGoogleMap);
        gameCreated=new Game();
        ibGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchLocationOnMapIntent = new Intent(CreateGameActivity.this, GooglePlacesActivity.class);
                final String etLocation = ((EditText) findViewById(R.id.etLocation)).getText().toString();
                searchLocationOnMapIntent.putExtra("location", etLocation.toString());

                startActivityForResult(searchLocationOnMapIntent, 1);
            }
        });

        bCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String etGameName = ((EditText) findViewById(R.id.etGameName)).getText().toString();
                int  etPlayerNum = Integer.parseInt(((EditText) findViewById(R.id.etPlayerNum)).getText().toString());
                final String etMaxDistance = ((EditText) findViewById(R.id.etMaxDistance)).getText().toString();

                final String etPhone = ((EditText) findViewById(R.id.etPhone)).getText().toString();
                final String etDescription = ((EditText) findViewById(R.id.etDescription)).getText().toString();

                setGameCreated(etGameName,etPlayerNum,etMaxDistance,etPhone,etDescription);
                try {

                DynamoDBManagerTask insertGameTask=new DynamoDBManagerTask();
                insertGameTask.setGame(gameCreated);
                insertGameTask.execute(DynamoDBManagerType.INSEERT_GAME);

                }catch(AmazonServiceException e) {

                }
            }
        });


    }
    private void setGameCreated(String etGameName, int etPlayerNum, String etMaxDistance, String etPhone, String etDescription){

        gameCreated.setGameName(etGameName);
        gameCreated.setCreatTime(String.valueOf(new Date().getTime()));
        gameCreated.setPlayerNum(etPlayerNum);
        gameCreated.setMaxDistance(etMaxDistance);
        gameCreated.setHostNumber(etPhone);
        gameCreated.setDescription(etDescription);
        gameCreated.setHost(LoginActivity.getUserEmail());
        gameCreated.setGameStatus(Constants.ACTIVE_STATE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            //System.out.println(data.getStringExtra("locationNAmeReturned"));
            LatLng ltlg=data.getParcelableExtra("locationReturned");
            latlng.put(Constants.LATITUDE,String.valueOf(ltlg.latitude));
            latlng.put(Constants.LONGTITUDE, String.valueOf(ltlg.longitude));

            gameCreated.setLocation(latlng);

            String locationName= data.getStringExtra("locationName");
            EditText etLocationName= (EditText) findViewById(R.id.etLocation);
            etLocationName.setText(locationName);
        }
    }
}

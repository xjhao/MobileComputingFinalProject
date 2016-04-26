package edu.wpi.zqinxhao.playerpooling;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManagerTask;
import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManagerType;
import edu.wpi.zqinxhao.playerpooling.model.User;

public class UserAreaActivity extends AppCompatActivity implements LocationListener {
    User user = null;
    LatLng currentPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = LoginActivity.getLoginUser();
        setContentView(R.layout.activity_user_area);

        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);
        welcomeMessage.setText("Welcome, " + user.getName() + "!");

        final Button bHost = (Button) findViewById(R.id.bHost);
        final Button bJoin = (Button) findViewById(R.id.bJoin);

        bHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createActivityIntent = new Intent(UserAreaActivity.this, CreateGameActivity.class);
                UserAreaActivity.this.startActivity(createActivityIntent);
            }
        });

        bJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent browseGamesActivity =new Intent(UserAreaActivity.this, BrowseGamesActivity.class);
//                UserAreaActivity.this.startActivity(browseGamesActivity);


                DynamoDBManagerTask browseGames=new DynamoDBManagerTask();
                browseGames.setLocation(currentPos);
                browseGames.setUserAreaActivity(UserAreaActivity.this);
                browseGames.execute(DynamoDBManagerType.QUERY_GAME);
            }
        });
        createLocationUpdateRequest();
    }
    private void createLocationUpdateRequest(){
        if (ActivityCompat.checkSelfPermission(UserAreaActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(UserAreaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location myLoc = locationManager.getLastKnownLocation(bestProvider);
        if(myLoc!=null){
            onLocationChanged(myLoc);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        currentPos= new LatLng(location.getLongitude(),location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

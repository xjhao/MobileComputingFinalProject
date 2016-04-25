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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import edu.wpi.zqinxhao.playerpooling.model.Constants;
import edu.wpi.zqinxhao.playerpooling.model.googlePlaces.GooglePlacesSearchTask;

/**
 * Created by zishanqin on 4/23/16.
 */
public class GooglePlacesActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,

        GoogleApiClient.OnConnectionFailedListener {
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    LatLng location;
    private int PROXIMITY_RADIUS = 5000;
    GooglePlacesSearchTask googlePlacesSearchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_places);
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        fragment.getMapAsync(this);
        buildGoogleApiClient();
        Button btnSelect=  (Button) findViewById(R.id.button_select);
        Button btnCancle=(Button) findViewById(R.id.button_cancel);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = googlePlacesSearchTask.getSelectedLocation();
                Intent intent=getIntent();
                intent.putExtra("locationName", googlePlacesSearchTask.getSelectedLocationName());
                intent.putExtra("locationReturned", location);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //location = googlePlacesSearchTask.getSelectedLocation();
                //Intent intent=getIntent();
                //intent.putExtra("locationSelected", null);
                //intent.putExtra("");
                if (ActivityCompat.checkSelfPermission(GooglePlacesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(GooglePlacesActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {            return;
                }

                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria,true);
                Location myLoc = locationManager.getLastKnownLocation(bestProvider);
                location=new LatLng(myLoc.getLatitude(),myLoc.getLongitude());
                Intent intent=getIntent();
                intent.putExtra("locationReturned", location);
                finish();
            }
        });



    }

    private void buildGoogleApiClient() {
        this.googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(this.location));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {            return;
        }
        googleMap.setMyLocationEnabled(true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        //this.location=location;
        if(location !=null) {
            onLocationChanged(location);
        }

    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria,true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if(location !=null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);


        Intent intent =getIntent();
        String type = intent.getStringExtra("location");
        StringBuilder googlePlacesUrl =new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?");
        if(location!=null) {
            googlePlacesUrl.append("location=" + this.location.latitude + "," + this.location.longitude);
        }
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + Constants.GOOGLE_API_KEY);
        googlePlacesSearchTask= new GooglePlacesSearchTask();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesSearchTask.execute(toPass);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

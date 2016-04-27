package edu.wpi.zqinxhao.playerpooling.model.googlePlaces;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zishanqin on 4/24/16.
 */
public class PlaceDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> implements GoogleMap.OnMarkerClickListener {

    JSONObject googlePlacesJson;
    GoogleMap googleMap;
    private String currentlocationName;
    private LatLng currentLocation;

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        googleMap.clear();
        googleMap.setOnMarkerClickListener(this);
        Log.d("PlaceDisplayTask", list.size() + "");
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = list.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            googleMap.addMarker(markerOptions);
        }

    }

    public boolean onMarkerClick(Marker marker) {
        currentlocationName = marker.getTitle();
        currentLocation = marker.getPosition();
        Log.d("LOCATION NAME?", currentlocationName + "");
        return false;
    }

    public String getCurrentlocationName() {
        Log.d("THIS CALLED? location name", currentlocationName + "");
        return currentlocationName;
    }


    public LatLng getCurrentLocation() {
        Log.d("THIS CALLED? location", currentLocation.latitude + "");
        return currentLocation;
    }
}


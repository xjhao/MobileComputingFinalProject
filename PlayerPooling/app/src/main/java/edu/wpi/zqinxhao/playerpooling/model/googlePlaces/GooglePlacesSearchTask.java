package edu.wpi.zqinxhao.playerpooling.model.googlePlaces;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class GooglePlacesSearchTask  extends AsyncTask<Object, Integer, String> {
    PlaceDisplayTask placesDisplayTask;
    String googlePlacesData = null;
    GoogleMap googleMap;
    @Override
    protected String doInBackground(Object... input) {

            googleMap = (GoogleMap) input[0];
            String googlePlacesUrl = (String) input[1];
            Http http =new Http();
        try {
            googlePlacesData =http.read(googlePlacesUrl);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("GooglePlacesSearchTask", e.toString());
        }
        return googlePlacesData;
    }
    @Override
    protected void onPostExecute(String result){
        placesDisplayTask = new PlaceDisplayTask();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = result;
        Log.d("PASSED? GOOGLE?", "");
        placesDisplayTask.execute(toPass);
    }

    public LatLng getSelectedLocation(){
        LatLng currentLoc = this.placesDisplayTask.getCurrentLocation();
        return currentLoc;
    }

    public String getSelectedLocationName() {
        return placesDisplayTask.getCurrentlocationName();
    }
}

class Http {

    public String read(String httpUrl) throws IOException {
        String httpData = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(httpUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            httpData = stringBuffer.toString();
            bufferedReader.close();
        } catch (Exception e) {
            Log.d("WhereYouApp", "Exception - reading Http url: " + e.toString());
        } finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return httpData;
    }
}
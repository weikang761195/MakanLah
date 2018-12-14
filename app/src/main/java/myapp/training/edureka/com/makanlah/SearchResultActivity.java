package myapp.training.edureka.com.makanlah;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class SearchResultActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationlistener;
    private FusedLocationProviderClient mFusedLocationClient;
    private double user_latitude;
    private double user_longitude;
    private String last_user_latitude ;
    private String last_user_longitude ;
    String s_user_latitude;
    String s_user_longitude;
    ArrayList<String> last_user_latitude_array = new ArrayList<String>();
    ArrayList<String> last_user_longitude_array = new ArrayList<String>();
    String[] intAry = new String[1];



    DecimalFormat df = new DecimalFormat(".#######");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        final RequestQueue queue = Volley.newRequestQueue(this);


        requestPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        final TextView search_restaurant_name = (TextView) findViewById(R.id.restaurant_name);
        final TextView latitude = (TextView) findViewById(R.id.latitude_id);
        final TextView longitude = (TextView) findViewById(R.id.longitude_id);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                user_latitude = location.getLatitude();
                user_longitude = location.getLongitude();
                s_user_latitude =  Double.toString(Double.parseDouble(df.format(user_latitude))) ;
                s_user_longitude = Double.toString(Double.parseDouble(df.format(user_longitude)));
                latitude.setText(s_user_latitude);
                longitude.setText(s_user_longitude);
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
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            last_user_latitude = Double.toString(Double.parseDouble(df.format(location.getLatitude())));
                            last_user_longitude = Double.toString(Double.parseDouble(df.format(location.getLongitude())));
                            Log.d("Lat", "onCreate: "+ last_user_latitude);
                            Log.d("Long", "onCreate: "+ last_user_longitude);

                            //nearby restaurant API
                            //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=6.0206893%20,116.1222303&radius=500&&types=restaurant&key=AIzaSyCtku1c45Aim05YBXl7eCpFi3eq54rqRYw
                            String Google_API_Key = "AIzaSyCtku1c45Aim05YBXl7eCpFi3eq54rqRYw";
                            String page_token = "60";
                            String radius = "5000";
                            String nearbyRestaurant_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+last_user_latitude+","+last_user_longitude+"&radius="+radius+"&types=restaurant&page_token="+page_token+"&key="+Google_API_Key+"";

                            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, nearbyRestaurant_URL, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Response", response.toString());
                                    try{
                                        JSONArray array = response.getJSONArray("results");
                                        Integer number_of_result = array.length();
                                        Random rand = new Random();
                                        int n = rand.nextInt(number_of_result) + 0;
                                        JSONObject restaurant_object = array.getJSONObject(n);
                                        String restaurant_name = restaurant_object.getString("name");
                                        search_restaurant_name.setText(restaurant_name);
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            queue.add(jor);
                        }
                    }
                });



    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
            }
        }
    }


}

package myapp.training.edureka.com.makanlah;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class SearchResultActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        requestPermission();
        final TextView search_restaurant_name = (TextView) findViewById(R.id.restaurant_name);


        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location", "onLocationChanged: " + "jibai");

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Location", "onLocationChanged: " + "jibai");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Location", "onLocationChanged: " + "jibai");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Location", "onLocationChanged: " + "jibai");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);







        //nearby restaurant API
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=6.0206893%20,116.1222303&radius=500&&types=restaurant&key=AIzaSyCtku1c45Aim05YBXl7eCpFi3eq54rqRYw
        String Google_API_Key = "AIzaSyCtku1c45Aim05YBXl7eCpFi3eq54rqRYw";
        String latitude = "6.0206893";
        String longitude = "116.1222303";
        String page_token = "60";
        String radius = "5000";
        String nearbyRestaurant_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius="+radius+"&types=restaurant&page_token="+page_token+"&key="+Google_API_Key+"";
        RequestQueue queue = Volley.newRequestQueue(this);
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
                    Log.d("number_of_result", number_of_result.toString());
                    String restaurant_name = restaurant_object.getString("name");
                    search_restaurant_name.setText(restaurant_name);
//                    for (int i = 0; i<number_of_result;i++){
//                        JSONObject restaurant_object = array.getJSONObject(i);
//                        String restaurant_name = restaurant_object.getString("name");
//                        Log.d("results", restaurant_name.toString());
//
//
//                    }



                    Log.d("results", restaurant_name.toString());


                } catch (JSONException e) {
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

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
    }


}

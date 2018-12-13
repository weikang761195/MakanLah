package myapp.training.edureka.com.makanlah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EditText searchBar = findViewById(R.id.search_bar_id);


        BottomNavigationView topNav = findViewById(R.id.top_navigation);
        topNav.setOnNavigationItemSelectedListener(navListener);

        //auto show home fragment after user log in
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch(menuItem.getItemId()){
                        case R.id.menu_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.menu_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.menu_more:
                            selectedFragment = new DoMoreFragment();
                            break;
                        case R.id.menu_history:
                            selectedFragment = new HistoryFragment();
                            break;
                        case R.id.menu_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.imageView:
                            selectedFragment = new TapResultFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };

    public void search(View v){
        Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
        startActivity(intent);

    }



}

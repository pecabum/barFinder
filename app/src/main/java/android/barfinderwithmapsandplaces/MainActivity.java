package android.barfinderwithmapsandplaces;

import android.Manifest;
import android.barfinderwithmapsandplaces.adapters.ViewPagerAdapter;
import android.barfinderwithmapsandplaces.fragments.PlacesListFragment;
import android.barfinderwithmapsandplaces.fragments.PlacesMapFragment;
import android.barfinderwithmapsandplaces.models.Place;
import android.barfinderwithmapsandplaces.ui.CustomViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final long MIN_TIME_UPDATE = 5000;
    private static final float MIN_DISTANCE_UPDATE = 1000;

    private ArrayList<Place> placeList;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        setupViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLocation();
    }

    /**
     * Enabling location with checking for permissions
     */
    private void enableLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (lastKnownLocation != null) {
            configPlaces(lastKnownLocation);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     *
     */
    private void setupViewPager() {
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PlacesListFragment(), "List");
        adapter.addFragment(PlacesMapFragment.newInstance(), "Map");
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    /**
     * @param location
     */
    private void configPlaces(Location location) {
        // To be more readable
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://maps.googleapis.com/maps/api/place/nearbysearch/"); // base url
        urlBuilder.append("json?"); // return output
        urlBuilder.append("location=");
        urlBuilder.append(location.getLatitude()); // latitute
        urlBuilder.append(",");
        urlBuilder.append(location.getLongitude()); // longitute
        urlBuilder.append("&radius=");
        urlBuilder.append("1000"); // in meters
        urlBuilder.append("&type=");
        urlBuilder.append("bar");   // type param
        urlBuilder.append("&key=");
        urlBuilder.append(getString(R.string.browser_key));

        getPlaces(urlBuilder.toString(), location);

    }


    /**
     * Getting places - the call must be in specific class but for the demo and because is just one
     * single call so i placed it inside the activity
     *
     * @param url
     * @param location
     */
    private void getPlaces(String url, final Location location) {
        Log.d(TAG, "getPlaces: " + url);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);

                        try {
                            JSONArray jArray = response.getJSONArray("results");
                            placeList = new ArrayList<>(jArray.length());
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    Gson gson = new Gson();
                                    Place place = gson.fromJson(jArray.get(i).toString(), Place.class);
                                    place.setDistance((int) location.distanceTo(place.getLocation()));

                                    // here must be an algorithm for best performance that remove only places which doesn't exist
                                    // in the new response and add only the newest to the group
                                    placeList.add(place);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            ((PlacesMapFragment) adapter.getItem(1)).loadMap(placeList, location);
                            ((PlacesListFragment) adapter.getItem(0)).loadData(placeList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                        Toast.makeText(MainActivity.this, " No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    @Override
    public void onLocationChanged(Location location) {
        configPlaces(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged() called with: " + "provider = [" + provider + "], status = [" + status + "], extras = [" + extras + "]");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled() called with: " + "provider = [" + provider + "]");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled() called with: " + "provider = [" + provider + "]");
        // Provider not enabled, prompt user to enable it
        Toast.makeText(this, R.string.please_turn_on_gps, Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(myIntent);
    }


}

package main.com.cineramamaps.draweractivity;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.NotificationUtils;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.activity.ChangeLanguageAct;
import main.com.cineramamaps.activity.ChangePasswordActivity;
import main.com.cineramamaps.activity.InviteFriendsActivity;
import main.com.cineramamaps.activity.LoginAct;
import main.com.cineramamaps.activity.NotificationActivity;
import main.com.cineramamaps.activity.PrivacyPolicyActivity;
import main.com.cineramamaps.activity.TermsConditionActivity;
import main.com.cineramamaps.activity.WalletActivity;
import main.com.cineramamaps.activity.WritetoUsActivity;
import main.com.cineramamaps.app.Config;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityBaseBinding;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BaseActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private static final String TAG = "MyLocationService";
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    Location location;
    Location oldlocation;
    private String FireBaseToken="";
    GPSTracker tracker;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 3000;
    private static final float LOCATION_DISTANCE = 10f;
    static String latitudes = "", longitudes = "";
    GPSTracker gpsTracker;

    public static double longitude = 0.0, latitude = 0.0;
    private double oldlongitude = 0.0, oldlatitude = 0.0;
    Location location_ar;
    LocationManager locationManager;
    private GoogleApiClient googleApiClient;


    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationview;
    boolean exit = false;
    SessionManager session;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ActivityBaseBinding binding;
    private TextView usernametv,mobiletv,notification_count;

    private RelativeLayout faqlay,walletlay,chanpasslay,moneysavedlay,notification_lay_tool,myratinglay,favouritelay,contactuslay,supportlay,messagelay,myprofilelay,termslay,privacypolicy,langlay,referfriends,notification_lay,logoutlay;
    private CircleImageView userimage;
    private CardView drawerbut;

    public static String address="", lat="0.0",lon="0.0";
    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider!=null){
            if(!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        }
    }
    private String language = "";
    MyLanguageSession myLanguageSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        //setContentView(R.layout.activity_base);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_base);
        session= SessionManager.get(this);
        longitude = 0.0;
        latitude = 0.0;
        address="";
        lat="0.0";
        lon="0.0";
        idinit();
        adddrawer();
        tracker = new GPSTracker(this);

        getLatLong();
        getCurrentLocation();
        initGps();

        gpsTracker = new GPSTracker(BaseActivity.this);

        // check if GPS enabled
        if (gpsTracker.canGetLocation()) {

//            currentLatitude = gpsTracker.getLatitude();
//            currentLongitude = gpsTracker.getLongitude();
            latitudes = String.valueOf(gpsTracker.getLatitude());
            longitudes = String.valueOf(gpsTracker.getLongitude());
            lat = ""+latitudes;
            lon = ""+longitudes;
        }
        initializeLocationManager();
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Log.e("BASEUSER", "" + message);
                    JSONObject data = null;
                    try {
                        data = new JSONObject(message);
                        String keyMessage = data.getString("key").trim();
                        getProfile();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        clickevent();

      /*  if (session.isUserLogin()){
            logiventv.setText(getResources().getString(R.string.logout));
        }
        else {
            logiventv.setText(getResources().getString(R.string.login));
        }
*/
    }

    private void clickevent() {
        faqlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        referfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(BaseActivity.this, InviteFriendsActivity.class);
                startActivity(i);
            }
        });

        walletlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent i = new Intent(BaseActivity.this, WalletActivity.class);
startActivity(i);
            }
        });
        chanpasslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BaseActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        langlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BaseActivity.this, ChangeLanguageAct.class);
                startActivity(i);
            }
        });
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BaseActivity.this, PrivacyPolicyActivity.class);
                startActivity(i);
            }
        });
        termslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BaseActivity.this, TermsConditionActivity.class);
                startActivity(i);
            }
        });
        contactuslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(BaseActivity.this, WritetoUsActivity.class);
                startActivity(i);
            }
        });
        logoutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.Logout();
                Intent i = new Intent(BaseActivity.this, LoginAct.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });
        notification_lay_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BaseActivity.this, NotificationActivity.class);
                startActivity(i);
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }



    private void idinit() {



        faqlay = findViewById(R.id.faqlay);

        walletlay = findViewById(R.id.walletlay);
        notification_lay_tool = findViewById(R.id.notification_lay_tool);
        userimage = findViewById(R.id.userimage);
        usernametv = findViewById(R.id.usernametv);
        mobiletv = findViewById(R.id.mobiletv);
        logoutlay = findViewById(R.id.logoutlay);
        contactuslay = findViewById(R.id.contactuslay);
        termslay = findViewById(R.id.termslay);
        langlay = findViewById(R.id.langlay);
        privacypolicy = findViewById(R.id.privacypolicy);
        referfriends = findViewById(R.id.referfriends);
        moneysavedlay = findViewById(R.id.moneysavedlay);
        chanpasslay = findViewById(R.id.chanpasslay);
        drawer_layout = findViewById(R.id.drawer_layout);
        drawerbut = findViewById(R.id.drawerbut);
        toolbar =findViewById(R.id.toolbar);
        navigationview = findViewById(R.id.navigationview);

    }


    private void adddrawer() {

        drawerbut.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });
        navigationview.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.END);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        String oldLanguage = language;
        language = myLanguageSession.getLanguage();
        if (!oldLanguage.equals(language)) {
            finish();
            startActivity(getIntent());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());
        if (session.getUserDetails()!=null){
            usernametv.setText(session.getUserDetails().getFirstName()+" "+session.getUserDetails().getLastName());
            mobiletv.setText(session.getUserDetails().getMobileWithCode());

            if (session.getUserDetails().getImage()!=null&&!session.getUserDetails().getImage().equalsIgnoreCase("")){
                Picasso.get().load(session.getUserDetails().getImage()).placeholder(R.drawable.profile_ic).into(userimage);

            }

        }


    }
    private void updateAddress() {
        //progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().updateAddress(getParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("Give Rating >", " >" + response);
              //  binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
//                            Toast.makeText(getApplicationContext(), "" + getResources().getString(R.string.submittedsuccessfully),
//                                    Toast.LENGTH_SHORT).show();
//                            finish();

                        } else {
//                            Toast.makeText(getApplicationContext(), "" + object.getString("message"),
//                                    Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed

                t.printStackTrace();
              //  binding.progressbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }

    private HashMap<String, String> getParam() {

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("address", ""+address);
        param.put("lat", ""+lat);
        param.put("lon", ""+lon);
        return param;


    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, getResources().getString(R.string.pressagain),
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }




    private void getProfile() {

        ApiCall.get().Create().getProfile(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyProfileDetail >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                         JSONObject jsonObject1 = object.getJSONObject("result");
                            String address1 = jsonObject1.getString("address");
//if(!address1.equalsIgnoreCase("")){
//    address = jsonObject1.getString("address");
//    lat = jsonObject1.getString("lat");
//    lon = jsonObject1.getString("lon");
//    addresstxt.setText(address);
//}else{
//    lat = ""+latitude;
//    lon = ""+longitude;
//}
//                            String noti_count = jsonObject1.getString("noti_count");
//                            if (noti_count==null||noti_count.equalsIgnoreCase("")||noti_count.equalsIgnoreCase("0")){
//                                notification_count.setVisibility(View.GONE);
//                            }
//                            else {
//                                notification_count.setVisibility(View.VISIBLE);
//                                notification_count.setText(noti_count);
//                            }

                        }
                        else {
                            session.Logout();
                            Intent i = new Intent(BaseActivity.this, LoginAct.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    void initGps() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        System.out.println("ssssssssssss location callll");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        Location lastLocation = locationManager.getLastKnownLocation(
                LocationManager.GPS_PROVIDER);
        if (lastLocation != null) {
            onLocationChanged(lastLocation);

        }
    }

    private void initializeLocationManager() {
        Log.e("Track", "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    /**
     * The service is starting, due to a call to startService()
     */


    /**
     * A client is binding to the service with bindService()
     */

    @Override
    public void onLocationChanged(Location locations) {
        location = locations;
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e("Track ", "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e("Track ", "onLocationChanged: " + location);
            if (mLastLocation == null) {

            } else {
                mLastLocation.set(location);
                String latitudes1="",longitudes1="";
                latitudes1 = String.valueOf(location.getLatitude());
                longitudes1 = String.valueOf(location.getLongitude());
                if(latitudes1.equalsIgnoreCase("") && longitudes1.equalsIgnoreCase("")){

                }else{
                    latitudes = String.valueOf(location.getLatitude());
                    longitudes = String.valueOf(location.getLongitude());

                }


            }


        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e("Track ", "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("Track ", "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("Track ", "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }
//    @Override
//    public void onDestroy() {
//
//        if (mLocationManager != null) {
//            for (int i = 0; i < mLocationListeners.length; i++) {
//                try {
//                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    mLocationManager.removeUpdates(mLocationListeners[i]);
//                } catch (Exception ex) {
//                    Log.i(TAG, "fail to remove location listener, ignore", ex);
//                }
//            }
//        }
 //   }



    private void getLatLong() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(MainActivity.this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // location_ar = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        oldlocation = location;
        //speedvalue =	""+getSpeed(location,oldlocation);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + latitude);
//			address_complete = loadAddress(latitude, longitude);
//			pickuplocation.setText(address_complete);

//			Date currentTime = Calendar.getInstance().getTime();
//			speedvalue = 	String.format("%.5f", 0.0);
//			lattxt.setText("This is location cordinates -\nLatitude - "+latitude+"\nLongitude - "+longitude);
//			speedtxt.setText("Speed - "+speedvalue+" Km/hr");
//			timetxt.setText(""+currentTime);


        } else {
            System.out.println("----------------geting Location from GPS----------------");

            location_ar = tracker.getLocation();
            if (location_ar == null) {
                latitude = 56.1304;
                longitude = 106.3468;

//				address_complete = loadAddress(latitude, longitude);
//				pickuplocation.setText(address_complete);

            } else {
                longitude = location_ar.getLongitude();
                latitude = location_ar.getLatitude();
                Log.e("Lat >>", "GPS " + latitude);
//                if (latitude == 0.0) {
//                    latitude = SplashActivity.latitude;
//                    longitude = SplashActivity.longitude;
//
//                }
//				address_complete = loadAddress(latitude, longitude);
//				pickuplocation.setText(address_complete);
            }
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + longitude);
            //moving the map to location

        }
    }

    private class MyLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //getSpeed(location,oldlocation);
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
}



/*
Complete the feedback of given

Change the status of screen a
*/
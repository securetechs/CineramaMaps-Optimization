package main.com.cineramamaps.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Preferences;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.restapi.ApiCall;
import main.com.cineramamaps.tabactivity.MainTabActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {
    private static int SPLASH_TIME_OUT = 2000;
    private String [] mPermission= {

                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };

    private String [] mPermissionlatest= {

            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
    };

    private SessionManager session;
    Context mContext = this;
    //new code location chekc
    protected static final String TAG = "MainActivity";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;
    public static double latitude = 0.0, longitude = 0.0;
    GPSTracker gpsTracker;
    private String language = "";
    MyLanguageSession myLanguageSession;
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
         setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(SplashActivity.this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(SplashActivity.this,mPermission , 101);
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this,mPermissionlatest , 101);
        }
        session= SessionManager.get(SplashActivity.this);
        genratehashkey();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        gpsTracker = new GPSTracker(SplashActivity.this);

        Preferences.save(mContext,Preferences.KEY_CURRENCY, "SAR");
        getExchangeRate();
        //checkCountry();
        loadCurrencyFromAssets();

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
            }
        });

    }



    public void genratehashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        boolean isAccepted=false;
        for (int  i:grantResults) {
            isAccepted=i==0;
            if (!isAccepted){
                MyHandler();
               // return;
            }
        }
        if (isAccepted){
            MyHandler();
        }else {

            /*Intent i = new Intent(SplashActivity.this, LocPerActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);*/
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(SplashActivity.this,mPermission , 101);
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this,mPermissionlatest , 101);
            }
        }
    }

private void MyHandler(){
    new Handler().postDelayed(new Runnable() {

        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */

        @Override
        public void run() {
            if (session.isUserLogin()){
                Toast.makeText(mContext,Preferences.get(mContext,Preferences.KEY_CURRENCY), Toast.LENGTH_SHORT).show();
                Log.e("TYPE "," >> "+session.getUserDetails().getType());
                Intent i = new Intent(SplashActivity.this, MainTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtra("comefrom","splash");
                startActivity(i);
            }
            else {
                Toast.makeText(mContext,Preferences.get(mContext,Preferences.KEY_CURRENCY), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SplashActivity.this, WelcomeSliderAct.class);
                startActivity(i);
                finish();
            }

        }
    }, SPLASH_TIME_OUT);
}

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

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

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            // updateUI();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }




    private void getExchangeRate() {
        ApiCall.get().Create().getExchange("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/sar.json").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("MyItems  ", " >> " + response);
                if (response.isSuccessful()) {
                    try{
                        Preferences.save(mContext,Preferences.KEY_EXACHANGE_JSON,response.body().string());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public void loadCurrencyFromAssets() {
        String json = null;
        try {
            InputStream inputStream = mContext.getAssets().open("currency.json"); // Open the file
            int size = inputStream.available(); // Get the file size
            byte[] buffer = new byte[size]; // Create a buffer
            inputStream.read(buffer); // Read the file into the buffer
            inputStream.close(); // Close the stream
            json = new String(buffer, StandardCharsets.UTF_8); // Convert to a string

            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String countryCode = telephonyManager.getNetworkCountryIso(); // Get country code

            JSONArray jsonArray = new JSONArray(json);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("countryCode").toLowerCase().equalsIgnoreCase(countryCode.toLowerCase())){
                    Preferences.save(mContext,Preferences.KEY_CURRENCY, jsonObject.getString("currencyCode"));
                }
            }

            Log.e("Currentt",json);

        } catch (Exception e) {
            Preferences.save(mContext,Preferences.KEY_CURRENCY, "SAR");
            e.printStackTrace();
        }
    }



}
//https://issuetracker.google.com/issues/37127697
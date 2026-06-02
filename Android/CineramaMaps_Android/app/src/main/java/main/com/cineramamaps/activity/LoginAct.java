package main.com.cineramamaps.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.ACProgressCustom;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityLoginBinding;
import main.com.cineramamaps.model.CategoryBean;
import main.com.cineramamaps.model.ProfileResponse;
import main.com.cineramamaps.model.UserDetails;
import main.com.cineramamaps.restapi.ApiCall;
import main.com.cineramamaps.tabactivity.MainTabActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAct extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private static final int REQ_CODE = 9001;
    private String ImagePath="";
    private MultipartBody.Part body;


    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    Button signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;
    ProgressDialog progress_dialog;
    String id = "";

    String personName, ppersonPhotoUrl, pemail;

    public static String FireBaseToken="";
    private String firebase_regid="",come_from="",otp_code="";
    public static  UserDetails successData;

    private static final int RC_SIGN_IN = 007;
    String facebook_id="",facebook_name="",face_username="",facebook_email="",facebook_image="";
    ActivityLoginBinding binding;
    boolean for_eyes = true;
    SessionManager session;






    String mobile_str_with_code="";



    int status;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    Location location;
    Location location_ar;
    LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    public static double longitude = 0.0, latitude = 0.0;
    GPSTracker tracker;
    public static    String signup_status = "";


    private ArrayList<String> languagelist;
    private CustomLangAdapter customLangAdapter;
    String country_code_str="";
    private String language = "";
    MyLanguageSession myLanguageSession;
    @Override
    protected void onResume() {
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
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
        Tools.get().updateResources(this);

        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        Locale locale = new Locale(""+language); // French language
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getApplicationContext().getResources().updateConfiguration(config, null);
//        Locale locale1 = new Locale("ar"); // Example for French
//        Locale.setDefault(locale1);
//        Configuration config1 = new Configuration();
//        config.locale = locale1;
//        getResources().updateConfiguration(config1, getResources().getDisplayMetrics());

// After changing the language, you can update the picker
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(LoginAct.this))
                .enableAutoManage(LoginAct.this, 0, connectionResult -> {
                    Snackbar.make(binding.googlelay, "Connection failed..", Snackbar.LENGTH_SHORT).show();
                    Log.e("TAG", "Google connection Error: " + connectionResult.getErrorMessage());
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        //Log.e(TAG,"mGoogleApiClient is connected");
                        mGoogleApiClient.clearDefaultAccountAndReconnect();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        buidNewGoogleApiClient();
        google_api_availability = GoogleApiAvailability.getInstance();
        //   status = google_api_availability.isGooglePlayServicesAvailable(this);


//        google_api_client = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API, Plus.PlusOptions.builder().build())
//                .addScope(Plus.SCOPE_PLUS_LOGIN)
//                .build();
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login);


        session= SessionManager.get(this);
        languagelist = new ArrayList<>();
        languagelist.add(""+getResources().getString(R.string.english));
        //  languagelist.add("French");
        languagelist.add(""+getResources().getString(R.string.arabic));
        //  languagelist.add(""+getResources().getString(R.string.turkish));
        clickevent();

        tracker = new GPSTracker(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null&&!bundle.isEmpty()){
            if (bundle.containsKey("come_from"));
            {
                come_from = bundle.getString("come_from");

            }
        }


        getLatLong();
        getCurrentLocation();
//        binding.eyes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(for_eyes){
//                    Picasso.get().load(R.drawable.view).into(binding.eyes);
//                    binding.passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
////                eyes.ima(R.drawable.view);
//                    for_eyes = false;
//                }else{
////                    eyes.setBackgroundResource(R.drawable.view_eye);
//                    Picasso.get().load(R.drawable.view_eye).into(binding.eyes);
//                    binding.passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                    for_eyes = true;
//                }
//
//            }
//        });


        country_code_str = binding.ccp.getSelectedCountryCode();

        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country_code_str = binding.ccp.getSelectedCountryCode();
            }
        });
        binding.backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                FireBaseToken=s;
            }
        });
        //       binding.googlelay.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               gPlusSignIn();
//           }
//       });
        binding.languageSpinner.setDropDownVerticalOffset(100);
        customLangAdapter = new CustomLangAdapter(LoginAct.this, languagelist);
        binding.languageSpinner.setAdapter(customLangAdapter);
        if (language.equalsIgnoreCase("ar")) {
            binding.languageSpinner.setSelection(1);
        }
        else if (language.equalsIgnoreCase("tr")) {
            binding.languageSpinner.setSelection(2);
        }
        else {
            binding.languageSpinner.setSelection(0);
        }
        String text = getResources().getString(R.string.byregisteragreeterm)
                + " <font color=#00adb5><u>"
                + getResources().getString(R.string.termtt)
                + "</u></font> .";
      //  String text = getResources().getString(R.string.byregisteragreeterm)+" <font color=#00adb5> "+getResources().getString(R.string.termtt)+"</font> .";
        // String text = getResources().getString(R.string.ireadtermscond);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            binding.termTv.setText(
//                    Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
//            );
//        } else {
//            binding.termTv.setText(Html.fromHtml(text));
//        }
        binding.termTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(LoginAct.this,TermsConditionActivity.class);
                startActivity(ii);
            }
        });

        binding.languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (languagelist.get(i).equalsIgnoreCase("İngilizce") || languagelist.get(i).equalsIgnoreCase("English") || languagelist.get(i).equalsIgnoreCase("الإنجليزية") ) {
                    myLanguageSession.insertLanguage("en");
                    myLanguageSession.setLangRecreate("en");
                    Log.e("LANGUAGE_EN ", " >> " + language);
                    myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
                    String oldLanguage = language;
                    language = myLanguageSession.getLanguage();
                    if (!oldLanguage.equals(language)) {
                        finish();
                        startActivity(getIntent());
                    }
                }
                else  if (languagelist.get(i).equalsIgnoreCase("Türkçe") || languagelist.get(i).equalsIgnoreCase("Turkish") || languagelist.get(i).equalsIgnoreCase("اللغة التركية") ) {
                    myLanguageSession.insertLanguage("tr");
                    myLanguageSession.setLangRecreate("tr");
                    Log.e("LANGUAGE_EN ", " >> " + language);
                    myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
                    String oldLanguage = language;
                    language = myLanguageSession.getLanguage();
                    if (!oldLanguage.equals(language)) {
                        finish();
                        startActivity(getIntent());
                    }
                }

                else {
                    myLanguageSession.insertLanguage("ar");
                    myLanguageSession.setLangRecreate("ar");
                    Log.e("LANGUAGE_DE ", " >> " + language);
                    myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
                    String oldLanguage = language;
                    language = myLanguageSession.getLanguage();
                    if (!oldLanguage.equals(language)) {
                        finish();
                        startActivity(getIntent());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.googlelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();


                // gPlusSignIn();
//                if (status != ConnectionResult.SUCCESS) {
//                    if (google_api_availability.isUserResolvableError(status)) {
//                        // Handle the specific error for "SERVICE_INVALID" (ConnectionResult = 16)
//                        if (status == ConnectionResult.SERVICE_INVALID) {
//                            showGooglePlayServicesErrorDialog();
//                        } else {
//                            google_api_availability.getErrorDialog(LoginAct.this, status, RC_SIGN_IN).show();
//                        }
//                    } else {
//                        Log.e("GooglePlayServices", "This device is not supported.");
//                    }
//                } else {
//                    // Proceed with your API calls if Google Play Services are available
//                    gPlusSignIn();
//                }
            }
        });
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail() // Request the user's email address
//                .requestProfile() // Optional: Request basic profile information
//                .build();
//
//
//        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
//            @Override
//            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
//                if (task.isSuccessful()) {
//                    // User is already signed in
//                    GoogleSignInAccount account = task.getResult();
//                    updateUI(account);
//                } else {
//                    // User is not signed in, proceed with regular sign-in
//                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                    startActivityForResult(signInIntent, RC_SIGN_IN);
//                }
//            }
//        });
    }


    private void clickevent() {


        binding.donthaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginAct.this,SignupActivity.class);
                startActivity(i);
            }
        });
        binding.backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.skipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginAct.this, MainTabActivity.class);
                startActivity(i);
            }
        });

        binding.forgotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginAct.this,ForgotPasswordNew.class);
                startActivity(i);
            }
        });
//        binding.phoneEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // No action needed here
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // No action needed here
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0 && s.charAt(0) == '0') {
//                    // Remove the leading zero
//                    s.delete(0, 1);
//                }
//            }
//        });
        binding.backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile_str_with_code = country_code_str+binding.phoneEt.getText().toString();
                if (Validation()){
                    DoLogin();
                }





            }
        });
    }









    private boolean Validation(){
        boolean valid=false;
        if (binding.phoneEt.getText().toString().isEmpty()){
            binding.phoneEt.setError(getResources().getString(R.string.required));
            binding.phoneEt.requestFocus();
        }
        else if (!Tools.isValidEmail(binding.phoneEt.getText().toString())){
            binding.phoneEt.setError(getResources().getString(R.string.emailvalid));
            binding.phoneEt.requestFocus();
        }
//        else if (binding.passwordEt.getText().toString().isEmpty()){
//            binding.passwordEt.setError(getResources().getString(R.string.required));
//            binding.passwordEt.requestFocus();
//        }
        else {
            valid=true;
        }
        return valid;
    }
    private HashMap<String, String> getParam() {

        String lat = ""+SplashActivity.latitude;
        String lon = ""+SplashActivity.longitude;

        if (SplashActivity.latitude==0){
            lat = ""+  latitude;
            lon = ""+  longitude;
        }
        Log.e("LOGIN_LATITUDE "," >> "+lat);
        HashMap<String,String>param=new HashMap<>();
        param.put("email",binding.phoneEt.getText().toString());
        param.put("mobile",binding.phoneEt.getText().toString());
        param.put("mobile_with_code",mobile_str_with_code);
        param.put("register_id",FireBaseToken);
        param.put("lat",""+lat);
        param.put("lon",""+lon);
        return param;
    }
    private void DoLogin(){
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().Send_Otp_Email(getParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("LOGINURL "," >> "+response);
                if (response.isSuccessful()){

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("LOGIN_PARSED_JSON", object.toString());
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            String codedetails = object.getString("result");
                            JSONObject ko = new JSONObject(codedetails);

                            otp_code = ko.getString("code");
                            signup_status =     object.getString("signup_statu");
                            String   user_details =     object.getString("user_details");
                            if(object.getString("signup_statu").equalsIgnoreCase("Yes")){
                                successData = new Gson().fromJson(user_details, UserDetails.class);
                                // session.CreateSession(successData);
                                JSONObject userDetailsJson = new JSONObject(user_details);
                                String userId = userDetailsJson.getString("id");


                                SharedPreferences preferences = getSharedPreferences("my_app_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("id", userId);
                                editor.apply();

                                Log.e("USER_ID_SAVED", userId);
                            }

                            Intent i = new Intent(LoginAct.this, EnterVerificationAct.class);
//                            Log.e("param string signup>> "," >> "+paramdata);
//                            i.putExtra("param",paramdata);
                            i.putExtra("mobile_str_with_code",mobile_str_with_code);
                            i.putExtra("mobile",binding.phoneEt.getText().toString());
                            i.putExtra("otp_str",otp_code);
                            startActivity(i);
//                            ProfileResponse successData = new Gson().fromJson(responseData, ProfileResponse.class);
//
//                            session.CreateSession(successData.getResult());
//                            Intent i = new Intent(LoginAct.this, MainTabActivity.class);
//                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            startActivity(i);

                        }
                        else if (object.getString("status").equalsIgnoreCase("2")) {
                            //  pendingAccount();
                        }

                        else {
                            Toast.makeText(LoginAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                }else {
                    System.out.println("sssssssssssssss response1 "+response.message());
                    Toast.makeText(LoginAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                System.out.println("sssssssssssssss response2 "+t.getLocalizedMessage());
                Toast.makeText(LoginAct.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (location_ar != null) {
            //Getting longitude and latitude
            longitude = location_ar.getLongitude();
            latitude = location_ar.getLatitude();
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + latitude);



        } else {
            System.out.println("----------------geting Location from GPS----------------");

            location_ar = tracker.getLocation();
            if (location_ar == null) {


            } else {
                longitude = location_ar.getLongitude();
                latitude = location_ar.getLatitude();
                Log.e("Lat >>", "GPS " + latitude);

            }
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + longitude);
            //moving the map to location

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
//        if (!result.hasResolution()) {
//            google_api_availability.getErrorDialog(this, result.getErrorCode(), request_code).show();
//            return;
//        }

        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }

    }



    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
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

    private void getLatLong() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location_ar = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }





    /*
        private void pendingAccount() {
            final Dialog dialogSts = new Dialog(LoginAct.this);
            dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSts.setCancelable(false);
            dialogSts.setContentView(R.layout.areyousure);
            dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RelativeLayout closepopup = dialogSts.findViewById(R.id.closepopup);
            TextView yes_tv = dialogSts.findViewById(R.id.yes_tv);
            TextView message_tv = dialogSts.findViewById(R.id.message_tv);
            message_tv.setText(""+getResources().getString(R.string.stillpending));
            yes_tv.setText(""+getResources().getString(R.string.ok));

            closepopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSts.dismiss();


                }
            });
            yes_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSts.dismiss();


                }
            });
            dialogSts.show();
        }
    */
    public class CustomLangAdapter extends BaseAdapter {
        private Context context; //context
        private ArrayList<String> languagelist;

        public CustomLangAdapter(Context context, ArrayList<String> languagelist) {
            this.context = context;
            this.languagelist = languagelist;
        }

        @Override
        public int getCount() {
            //return 10; //returns total of items in the list
            return languagelist == null ? 0 : languagelist.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.spinner_lay_lang, parent, false);

            }
            TextView pname = convertView.findViewById(R.id.pname);
            pname.setText(""+languagelist.get(position));

            return convertView;
        }
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("dddddddddddddd", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("ddddd", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("", "printHashKey()", e);
        }
    }



    private void SocialLgin(){

        String lat = ""+SplashActivity.latitude;
        String lon = ""+SplashActivity.longitude;

        if (SplashActivity.latitude==0){
            lat = ""+  latitude;
            lon = ""+  longitude;
        }

        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().socialLogin(facebook_id,facebook_name,face_username,facebook_image,facebook_email,FireBaseToken,"",lat,lon,"USER").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("SOCIALLOGIN "," >> "+response);
                if (response.isSuccessful()){


                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")||object.getString("status").equalsIgnoreCase("2")) {
                            ProfileResponse successData = new Gson().fromJson(responseData, ProfileResponse.class);

                            session.CreateSession(successData.getResult());

                        }
                        else {
                            Toast.makeText(LoginAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                }else {
                    Toast.makeText(LoginAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(LoginAct.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Check which request we're responding to
//        if (requestCode == SIGN_IN_CODE) {
//            request_code = requestCode;
//            if (resultCode != RESULT_OK) {
//                is_signInBtn_clicked = false;
//                binding.progressbar.setVisibility(View.GONE);
//                // ac_dialog.dismiss();
//
//            }
//
//            is_intent_inprogress = false;
//
//            if (!google_api_client.isConnecting()) {
//                google_api_client.connect();
//            }
//        } else {
//            //  callbackManager.onActivityResult(requestCode, resultCode, data);
//            // facebook.authorizeCallback(requestCode, resultCode, data);
//        }
//    }

    @Override
    public void onConnected(Bundle arg0) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout
        getProfileInfo();
        // Update the UI after signin


    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();

    }



/*
  Sign-in into the Google + account
 */

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            binding.progressbar.setVisibility(View.VISIBLE);
            resolveSignInError();

        }
    }

    protected void onStart() {
        super.onStart();
        google_api_client.connect();
    }

    protected void onStop() {
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }



/*
  Method to resolve any signin errors
 */

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

/*
  Sign-out from Google+ account
 */

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();

        }
    }

/*
 Revoking access from Google+ account
 */

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
                            buidNewGoogleApiClient();
                            google_api_client.connect();
                            apiCall();
//                            SocialLogin fbtask1 = new SocialLogin();
//                            fbtask1.execute();
                            //  Toast.makeText(LoginAct.this,"Social login api",Toast.LENGTH_SHORT).show();
                            //new JsonTask().execute(id, personName, pemail, ppersonPhotoUrl);
                        }

                    });
        }
    }

/*
 get user's information name, email, profile pic,Date of birth,tag line and about me
 */

    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                setPersonalInfo(currentPerson);

            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

/*
 set the User information into the views defined in the layout
 */

    private void setPersonalInfo(Person currentPerson) {

        String pid = currentPerson.getId();
        String gmailUsername = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        String email = Plus.AccountApi.getAccountName(google_api_client);
        /*progress_dialog.dismiss();*/
        // Toast.makeText(this, "Person information is shown!", Toast.LENGTH_LONG).show();


        facebook_id = pid;
        face_username = gmailUsername;
        facebook_email = email;
        facebook_image = personPhotoUrl;

        Log.e("value", "" + id + personName + pemail + ppersonPhotoUrl);

        if (id == null) {

        } else {
            gPlusRevokeAccess();
        }


    }

    private void buidNewGoogleApiClient() {

        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }



    private HashMap<String,String> getParam1() {

        HashMap<String,String>param=new HashMap<>();
        param.put("social_id",facebook_id);
        param.put("first_name",face_username);
        param.put("last_name","");
        param.put("mobile","");
        param.put("mobile_with_code",mobile_str_with_code);
        param.put("gender","");
        param.put("dob","");
        param.put("email",facebook_email);
        param.put("register_id",""+LoginAct.FireBaseToken);
        param.put("ios_register_id","");
        param.put("address","");
        param.put("lat","");
        param.put("lon","");
        param.put("image",""+facebook_image);

        return param;

    }
    private void apiCall(){
        if (ImagePath != null && ImagePath.equalsIgnoreCase("")) {
            body = MultipartBody.Part.createFormData("image", "");
        } else {
            File file = new File(ImagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            Log.e("Imagepath", ImagePath);
        }
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getSociallogin(getParam1(),body)
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        binding.progressbar.setVisibility(View.GONE);
                        Log.e("UpdateProfile_Basic "," .. "+ response);
                        if (response.isSuccessful()) {
                            session.CreateSession(response.body().getResult());
                            //  Toast.makeText(CreateProfileActivity.this, "" + getResources().getString(R.string.registersuccessfully), Toast.LENGTH_SHORT).show();
                            signupSuccess();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        Log.e("onFailure", t.getLocalizedMessage());
                        binding.progressbar.setVisibility(View.GONE);
                    }
                });
    }

    private void signupSuccess() {
        final Dialog dialogSts = new Dialog(LoginAct.this);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.popup_success_signup);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView backhome = dialogSts.findViewById(R.id.backhome);


        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();

                Intent i = new Intent(LoginAct.this, MainTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });
        dialogSts.show();
    }



    public void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            // G+
//            Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//            System.out.println("Display Name: " + person.getDisplayName());
//            System.out.println("Gender: " + person.getGender());
//            System.out.println("AboutMe: " + person.getAboutMe());
//            System.out.println("Birthday: " + person.getBirthday());
//            System.out.println("Current Location: " + person.getCurrentLocation());
//            System.out.println("Language: " + person.getLanguage());

        }
    }


    public void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            // Get account information

            if (acct != null) {
                String Name = acct.getDisplayName();
                String Email = "";
                if (acct.getEmail() != null) {
                    Email = acct.getEmail();
                } else {
                    Email = "";
                }

                String SocialUserId = acct.getId();
                String Gender = "";

                String idToken = acct.getIdToken();
                String profileURL = Objects.requireNonNull(acct.getPhotoUrl()).toString();

                String status = "Status: \nFullname: " + Name + "\n Email: " + Email + "\nProfile URI: " + profileURL;
                Log.i("TAG", "Google signin " + status);
                Log.i("TAG", "ID Token: " + idToken);
                Log.i("TAG", "ID: " + acct.getId());
                facebook_id = acct.getId();
                face_username = Name;
                facebook_email = Email;
                //  facebook_image = acct;
                apiCall();
                //TODO Temporary "acct.getCompId()" pass "idToken"

                //  checkIsUserExists();
            }

        } else {
            //  hideProgressBar();
            Log.e("TAG", "Failed!! Google Result " + result.getStatus());

            int status_code = result.getStatus().getStatusCode();
            switch (status_code) {
                case GoogleSignInStatusCodes.SIGN_IN_CANCELLED:
                    Snackbar.make(binding.googlelay, "Google sign in has been cancelled.", Snackbar.LENGTH_SHORT).show();
                    break;
                case GoogleSignInStatusCodes.NETWORK_ERROR:
                    Snackbar.make(binding.googlelay, "Application is unable to connect with internet", Snackbar.LENGTH_SHORT).show();
                default:
                    //AppUtils.showSnackBar(LandingActivity.this, btnLogin, GoogleSignInStatusCodes.getStatusCodeString(result.getStatus().getStatusCode()), R.integer.snackbar_duration_3sec);
                    break;
            }
        }
    }
}







package main.com.cineramamaps.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityShedulePlaceListBinding;
import main.com.cineramamaps.databinding.ActivityTripScheduleListBinding;
import main.com.cineramamaps.databinding.LayoutCustomerTriplistBinding;
import main.com.cineramamaps.databinding.TripschedulelistitemBinding;
import main.com.cineramamaps.model.DayWiseTrip;
import main.com.cineramamaps.model.PlaceSheduleBeanList;
import main.com.cineramamaps.model.PlacesheduleBean;
import main.com.cineramamaps.model.TripBean;
import main.com.cineramamaps.model.TripBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShedulePlaceListActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
        , ResultCallback<LocationSettingsResult> {

    private SessionManager session;
    ActivityShedulePlaceListBinding binding;
    private ArrayList<PlaceSheduleBeanList> tripBeanListArrayList;
    NotificationAdapter adapter;
    private String language = "",tripid="",id="",name="",name_ar="";
    int pos =0;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 3000;
    private static final float LOCATION_DISTANCE = 10f;
    static String latitudes = "", longitudes = "";
    GPSTracker gpsTracker;
    private static final String TAG = "MyLocationService";
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    Location location;
    GPSTracker tracker;
    String lat="0.0",lon="0.0";
    Location oldlocation;
    public static double longitude = 0.0, latitude = 0.0;
    private double oldlongitude = 0.0, oldlatitude = 0.0;
    Location location_ar;
    String table_map_name="";
    LocationManager locationManager;
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_shedule_place_list);
        Bundle b = getIntent().getExtras();
        table_map_name = b.getString("table_map_name");
       id = b.getString("id");
       name = b.getString("name");
       name_ar = b.getString("name_ar");
        BindView();
        clickevent();
        if(language.equalsIgnoreCase("en")) {
            binding.citynametv.setText("" + table_map_name);
        }else{
            binding.citynametv.setText("" + table_map_name);
        }
         tracker = new GPSTracker(this);

        getLatLong();
        getCurrentLocation();
        initGps();
        //   getNotification();

        //  binding.notificaitonlist.setAdapter(new NotificationAdapter(ShedulePlaceListActivity.this, null, session.IsEnglish()));
        getTrip();
    }



    private void clickevent() {
        binding.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent ii = new Intent(ShedulePlaceListActivity.this,CreateTripActivity.class);
//                ii.putExtra("id","");
//                startActivity(ii);
            }
        });
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void BindView() {
        session = SessionManager.get(ShedulePlaceListActivity.this);


    }


    private void getTrip() {
        binding.progressbar.setVisibility(View.VISIBLE);
        tripBeanListArrayList = new ArrayList<>();
        ApiCall.get().Create().getPlaceTrip(session.getUserID(),id,""+lat,""+lon,table_map_name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("MyItems  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            binding.datalay.setVisibility(View.VISIBLE);
                            binding.emptylay.setVisibility(View.GONE);
                            tripBeanListArrayList = new ArrayList<>();
                            PlacesheduleBean successData = new Gson().fromJson(responseData, PlacesheduleBean.class);
                            tripBeanListArrayList.addAll(successData.getResult());

                            adapter =   new NotificationAdapter(ShedulePlaceListActivity.this, tripBeanListArrayList, session.IsEnglish());
                            binding.notificaitonlist.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        } else {

                            binding.datalay.setVisibility(View.GONE);
                            binding.emptylay.setVisibility(View.VISIBLE);

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
        Context context;
        boolean b;
        List<PlaceSheduleBeanList> tripBeanListArrayList;

        public NotificationAdapter(Context context, ArrayList<PlaceSheduleBeanList> tripBeanListArrayList, boolean b) {
            this.context = context;
            this.tripBeanListArrayList = tripBeanListArrayList;
            this.b = b;
        }


        @NonNull
        @Override
        public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutCustomerTriplistBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_customer_triplist, parent, false);
            return new NotificationAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
            if(language.equalsIgnoreCase("en")) {
                holder.binding.titletv.setText("" + tripBeanListArrayList.get(position).getDayName());
            }else{
                holder.binding.titletv.setText("" + tripBeanListArrayList.get(position).getDayNameAr());

            }
            if(tripBeanListArrayList.get(position).getDayWiseTrip()!=null&& tripBeanListArrayList.get(position).getDayWiseTrip().size()>0){
                NotificationAdapter1 adapter1 =   new NotificationAdapter1(ShedulePlaceListActivity.this, tripBeanListArrayList.get(position).getDayWiseTrip(), session.IsEnglish());
                holder.binding.notificaitonlist1.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
            }




            holder.binding.arrwolay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.binding.notificaitonlist1.getVisibility()==View.VISIBLE){
                        holder.binding.notificaitonlist1.setVisibility(View.GONE);
                        holder.binding.arrowimg.setImageResource(R.drawable.downarow);
                        holder.binding.detaillay.setCardBackgroundColor(context.getResources().getColor(R.color.screenback2));
                    }
                    else {
                        holder.binding.notificaitonlist1.setVisibility(View.VISIBLE);
                        holder.binding.arrowimg.setImageResource(R.drawable.uparrow);
                        holder.binding.detaillay.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                    }
                }
            });
//            holder.binding.mainlay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(ShedulePlaceListActivity.this,ShedulePlaceListActivity.class);
//                    i.putExtra("id",tripBeanListArrayList.get(position).getPlaceId());
//                    i.putExtra("name",tripBeanListArrayList.get(position).getMapType());
//                    i.putExtra("name_ar",tripBeanListArrayList.get(position).getMapTypeAr());
//                    startActivity(i);
//                }
//            });


//        if (notificationBeanListArrayList.get(position).getUserDetails() != null) {
//            String image = notificationBeanListArrayList.get(position).getUserDetails().getImage();
//
//            if (image != null && !image.equalsIgnoreCase("")) {
//                Picasso.get().load(image).placeholder(R.drawable.profile_ic).into(holder.binding.userpic);
//            }
//
//
//        }
        }

        @Override
        public int getItemCount() {
            // return 3;
            return tripBeanListArrayList == null ? 0 : tripBeanListArrayList.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LayoutCustomerTriplistBinding binding;

            public ViewHolder(@NonNull LayoutCustomerTriplistBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
    }


    private void Delete_call() {
        binding.progressbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> resultCall = ApiCall.get().Create().delete_trip(tripid);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String responedata = response.body().string();
                        Log.e("get address response", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");
                        if (error.equals("1")) {
                            // if (isInternetPresent) {
//                            tripBeanListArrayList.remove(pos);
//                            adapter.notifyDataSetChanged();
//                            } else {
//                                Toast.makeText(GetUserAddress.this,"No Internet",Toast.LENGTH_LONG).show();
//                            }
                            getTrip();
                        } else {
                            String message = object.getString("result");
                            if (getApplicationContext() != null) {
                                Toast.makeText(getApplicationContext(), "Some Error found Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("hgdhgfgdf", "dtrdfuydrfgjhjjfyt");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.server_problem), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class NotificationAdapter1 extends RecyclerView.Adapter<NotificationAdapter1.ViewHolder> {
        Context context;
        boolean b;
        List<DayWiseTrip> DayWiseTrip;

        public NotificationAdapter1(Context context, List<DayWiseTrip> DayWiseTrip, boolean b) {
            this.context = context;
            this.DayWiseTrip = DayWiseTrip;
            this.b = b;
        }


        @NonNull
        @Override
        public NotificationAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TripschedulelistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.tripschedulelistitem, parent, false);
            return new NotificationAdapter1.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationAdapter1.ViewHolder holder, int position) {
//            if(language.equalsIgnoreCase("en")) {
//                holder.binding.maptypetv.setText("" + DayWiseTrip.get(position).getMapType() + "," + DayWiseTrip.get(position).getCountryName());
//            }else{
//                holder.binding.maptypetv.setText("" + DayWiseTrip.get(position).getMapTypeAr() + "," + DayWiseTrip.get(position).getCountryNameAr());
//
//            }
            holder.binding.maptypetv.setText("" + DayWiseTrip.get(position).getAddress());
            holder.binding.distancelay.setVisibility(View.VISIBLE);
            holder.binding.distancetv.setText("" + DayWiseTrip.get(position).getDistance()+" "+getResources().getString(R.string.kmawayfromyou));
            holder.binding.timetv.setText("" + DayWiseTrip.get(position).getTime());

            holder.binding.titletv.setText("" + DayWiseTrip.get(position).getTripName());
            holder.binding.titletv.setVisibility(View.VISIBLE);
            holder.binding.datetv.setText(getResources().getString(R.string.lastupdate)+" " + DayWiseTrip.get(position).getDateTime());
            holder.binding.datetv.setVisibility(View.GONE);
            if(DayWiseTrip.get(position).getTripByCineramap().equalsIgnoreCase("Yes")){
                holder.binding.createdbytv.setVisibility(View.VISIBLE);
            }else{
                holder.binding.createdbytv.setVisibility(View.GONE);
            }
            holder.binding.editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ShedulePlaceListActivity.this,CreateTripActivity.class);
                    i.putExtra("id",DayWiseTrip.get(position).getId());
                    i.putExtra("place_id",""+DayWiseTrip.get(position).getTrip_place_id());
                    i.putExtra("map_place_id",""+DayWiseTrip.get(position).getMap_place_id());
                    i.putExtra("city_id",""+id);
                    i.putExtra("city_name",""+name);
                    i.putExtra("city_name_ar",""+name_ar);
                    i.putExtra("place_name",""+DayWiseTrip.get(position).getTripName());
                    i.putExtra("place_name_ar",""+DayWiseTrip.get(position).getTripNamear());
                    i.putExtra("address",""+DayWiseTrip.get(position).getAddress());
                    i.putExtra("lat",""+DayWiseTrip.get(position).getLat());
                    i.putExtra("lon",""+DayWiseTrip.get(position).getLon());
                    startActivity(i);
                }
            });
            holder.binding.deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tripid = DayWiseTrip.get(position).getId();
                    DayWiseTrip.remove(position);
                   // pos = position;
                    Delete_call();
                }
            });
            holder.binding.mainlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent i = new Intent(ShedulePlaceListActivity.this,ShedulePlaceListActivity.class);
//                    i.putExtra("id",DayWiseTrip.get(position).getPlaceId());
//                    i.putExtra("name",DayWiseTrip.get(position).getMapType());
//                    i.putExtra("name_ar",DayWiseTrip.get(position).getMapTypeAr());
//                    startActivity(i);
                    String uri="";
                    if(DayWiseTrip.get(position).getAddress()!= null &&DayWiseTrip.get(position).getLat()!=null) {

                        uri = "http://maps.google.com/maps?q=loc:" + DayWiseTrip.get(position).getLat() + "," + DayWiseTrip.get(position).getLon() + " (" + DayWiseTrip.get(position).getTripName() + ")";


                        System.out.println("ssssssssssssssssssssssssss = " + Uri.parse(uri));
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    }
                }
            });


//        if (notificationBeanListArrayList.get(position).getUserDetails() != null) {
//            String image = notificationBeanListArrayList.get(position).getUserDetails().getImage();
//
//            if (image != null && !image.equalsIgnoreCase("")) {
//                Picasso.get().load(image).placeholder(R.drawable.profile_ic).into(holder.binding.userpic);
//            }
//
//
//        }
        }

        @Override
        public int getItemCount() {
            // return 3;
            return DayWiseTrip == null ? 0 : DayWiseTrip.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TripschedulelistitemBinding binding;

            public ViewHolder(@NonNull TripschedulelistitemBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
    }

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
            lat=""+latitude;
            lon=""+longitude;
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
                lat=""+latitude;
                lon=""+longitude;
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
    public void onLocationChanged(@NonNull Location location) {

    }
    private void initializeLocationManager() {
        Log.e("Track", "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
//   LocationListener[] mLocationListeners = new LocationListener[]{
//            new LocationListener(LocationManager.PASSIVE_PROVIDER)
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
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
//    }


    private class MyLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            lat=""+latitude;
            lon = ""+longitude;
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
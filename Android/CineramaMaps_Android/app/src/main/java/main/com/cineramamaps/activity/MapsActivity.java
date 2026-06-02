package main.com.cineramamaps.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityMapsBinding;
import main.com.cineramamaps.databinding.ActivityMapsListBinding;
import main.com.cineramamaps.databinding.MapgridlistitemBinding;
import main.com.cineramamaps.draweractivity.BaseActivity;
import main.com.cineramamaps.model.MapBean;
import main.com.cineramamaps.model.MapBeanList;
import main.com.cineramamaps.model.ProviderBean;
import main.com.cineramamaps.model.ProviderBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
        , ResultCallback<LocationSettingsResult> {
    ActivityMapsBinding binding;
    ArrayList<MapBeanList> searchbuymessageBeanListArrayList = new ArrayList<>();
    ArrayList<MapBeanList> mapBeanLists = new ArrayList<>();
    String category_id="";
    private SessionManager session;
    String type="List";
    private LatLng latlong =null;
    String mark_img="";
    AllRestaurnatAdapter    adapter;
    TextView cleartxt;

    String diet_type_vegan="",day_name="",start_time="",end_time="";
    Marker marker, myloc_marker;
    private GoogleApiClient googleApiClient;
    GPSTracker gpsTracker;
    ArrayList<ProviderBeanList> friendsBeanLstArrayList = new ArrayList<>();
    private GoogleMap gmap;
    public static double longitude = 0.0, latitude = 0.0;
    List<Marker> markerList = new ArrayList<Marker>();
    private MarkerOptions options = new MarkerOptions();
    String country_id="",order_id="",paid_amount="",come_from="",duration="";
    Marker googlemarker_pos, my_job_location_marker;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0;
    Location location_ar;
    Location location;
    LocationManager locationManager;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);

        session = SessionManager.get(MapsActivity.this);
      //  Bundle bundle = getIntent().getExtras();
        diet_type_vegan="";
        day_name="";
        start_time="";
        end_time="";
//        Bundle b = getIntent().getExtras();
//
//        if (b != null) {
//            country_id = b.getString("country_id");
//            order_id = b.getString("order_id");
//            paid_amount = b.getString("paid_amount");
//            come_from = b.getString("come_from");
//            type = b.getString("type");
//            duration = b.getString("duration");
//        }
//        if (bundle!=null&&!bundle.isEmpty()){
//            category_id = bundle.getString("category_id");
//            if(bundle.containsKey("diet_type_vegan")){
//                diet_type_vegan = bundle.getString("diet_type_vegan");
//                day_name = bundle.getString("day_name");
//                start_time = bundle.getString("start_time");
//                end_time = bundle.getString("end_time");
//            }
//        }

        clickevent();
        //  initilizeMap();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        binding.providerrecyclerview.setLayoutManager(mLayoutManager);


        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s==null){

                }
                else {


                    if (s.toString().length() > 0) {

                        if (adapter == null) {

                        } else {
                            Log.e("COME "," >> "+s.toString());
                            adapter.filter(s.toString());
                        }

                    } else {

                        if (adapter == null) {

                        } else {
                            adapter.filter("");
                        }
                    }


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        getMaps();
    }
    private void initilizeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void getMaps() {

        binding.progressbar.setVisibility(View.VISIBLE);
        mapBeanLists = new ArrayList<>();
        searchbuymessageBeanListArrayList = new ArrayList<>();
        mapBeanLists.clear();
        searchbuymessageBeanListArrayList.clear();
        //  binding.norestfound.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getMaplist(session.getUserID(), "","All").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("Restaurants  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            norestfound.setVisibility(View.GONE);
//                            shimmer_view_restarant.stopShimmerAnimation();
//                            shimmer_view_restarant.setVisibility(View.GONE);
                            MapBean successData = new Gson().fromJson(responseData, MapBean.class);
                            mapBeanLists.addAll(successData.getResult());
                            searchbuymessageBeanListArrayList.addAll(successData.getResult());
                            adapter =   new AllRestaurnatAdapter(MapsActivity.this,mapBeanLists );
                            binding.providerrecyclerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        } else {
//                            norestfound.setVisibility(View.VISIBLE);
//                            shimmer_view_restarant.stopShimmerAnimation();
//                            shimmer_view_restarant.setVisibility(View.GONE);
//                            horizontalRestaurantAdapter = new HomeActivity.HorizontalRestaurantAdapter(null, HomeActivity.this, session.IsEnglish1());
//                            restaurnatlistrecyler.setAdapter(horizontalRestaurantAdapter);


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
    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(MapsActivity.this,FilterActivity.class);
                startActivity(ii);
            }
        });
        binding.itemtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="List";
                binding.itemlay.setVisibility(View.VISIBLE);
                binding.magiclay.setVisibility(View.GONE);
                binding.markerlay.setVisibility(View.GONE);

                binding.itemtv.setBackgroundResource(R.drawable.button_round_dra);
                binding.magicbagtv.setBackgroundResource(R.color.grey_back_col);

                binding.itemtv.setTextColor(getResources().getColor(R.color.white));
                binding.magicbagtv.setTextColor(getResources().getColor(R.color.black));
                getRestaurants();

            }
        });
        binding.magicbagtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Map";
                binding.itemlay.setVisibility(View.GONE);
                binding.magiclay.setVisibility(View.VISIBLE);

                binding.itemtv.setBackgroundResource(R.color.grey_back_col);
                binding.magicbagtv.setBackgroundResource(R.drawable.button_round_dra);

                binding.magicbagtv.setTextColor(getResources().getColor(R.color.white));
                binding.itemtv.setTextColor(getResources().getColor(R.color.black));
                getRestaurants();
            }
        });
    }



    private void getRestaurants() {
        binding.shimmerViewRestarant.setVisibility(View.VISIBLE);
        binding.shimmerViewRestarant.startShimmerAnimation();
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getRestaurants(session.getUserID(), "", ""+category_id,""+BaseActivity.lat,""+BaseActivity.lon,diet_type_vegan,day_name,start_time,end_time).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("Restaurants  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        binding.shimmerViewRestarant.setVisibility(View.GONE);
                        binding.shimmerViewRestarant.stopShimmerAnimation();
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            ProviderBean successData = null;
                            successData = new Gson().fromJson(responseData, ProviderBean.class);
                            if(type.equalsIgnoreCase("List")) {

                                //  binding.providerrecyclerview.setAdapter(new AllRestaurnatAdapter(MapsActivity.this, successData.getResult()));
                            }else {
                                try {

                                    markerList = new ArrayList<>();

                                    markerList.clear();

                                    friendsBeanLstArrayList = new ArrayList<>();
                                    markerList = new ArrayList<>();
                                    friendsBeanLstArrayList.addAll(successData.getResult());
                                    if (gmap == null) {

                                    } else {
                                        gmap.clear();

                                        if (markerList != null) {
                                            for (int i = 0; i < markerList.size(); i++) {
                                                markerList.get(i).remove();
                                            }

                                        }

                                    }

                                    for (int k = 0; k < friendsBeanLstArrayList.size(); k++) {
                                        LatLng latLng;
                                        if (friendsBeanLstArrayList.get(k).getProviderLat() == null || friendsBeanLstArrayList.get(k).getProviderLon().equalsIgnoreCase("")) {
                                            latLng = new LatLng(Double.parseDouble("0.0"), Double.parseDouble("0.0"));
                                        } else {
                                            latLng = new LatLng(Double.parseDouble(friendsBeanLstArrayList.get(k).getProviderLat()), Double.parseDouble(friendsBeanLstArrayList.get(k).getProviderLon()));
                                        }
                                        //   if(friendsBeanLstArrayList.get(k).getBusinnessImages()!=null && friendsBeanLstArrayList.get(k).getBusinnessImages().size()>0) {
                                        addCustomMarker(latLng.latitude, latLng.longitude, friendsBeanLstArrayList.get(k).getStoreLogo(), friendsBeanLstArrayList.get(k).getProviderName(), k);
                                        //  }


                                    }
                                    LatLng latLng = new LatLng(Double.parseDouble(friendsBeanLstArrayList.get(friendsBeanLstArrayList.size() - 1).getProviderLat()), Double.parseDouble(friendsBeanLstArrayList.get(friendsBeanLstArrayList.size() - 1).getProviderLon()));
                                    MarkerOptions marker1 = new MarkerOptions().position(latLng).flat(true).anchor(0.5f, 0.5f);
                                    // MarkerOptions marker1 = new MarkerOptions().position(new LatLng(40.04, -75.487)).flat(true).anchor(0.5f, 0.5f);
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);


                                    //  gmap.addMarker(marker1);
                                    //  marker = gmap.addMarker(marker1);
                                    //  marker.setTag("0");
//
                                    // gmap.animateCamera( )
                                    gmap.animateCamera(cameraUpdate);


                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                            }

                        } else {
                            binding.providerrecyclerview.setAdapter(new AllRestaurnatAdapter(MapsActivity.this,null));


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        if (gmap == null) {

                        }else{
                            gmap.clear();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                if (gmap == null) {

                }else{
                    gmap.clear();
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        gmap = googleMap;

//        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }


        gmap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
//                gmap.setMapStyle(
//                        MapStyleOptions.loadRawResourceStyle(
//                                getActivity(), R.raw.stylemap_3));


                gmap.setBuildingsEnabled(false);
                // gMap.setMyLocationEnabled(true);
                gmap.getUiSettings().setMyLocationButtonEnabled(false);
                gmap.getUiSettings().setMapToolbarEnabled(false);
                gmap.getUiSettings().setZoomControlsEnabled(false);

                LatLng latLng = new LatLng(40.04, -75.487);

                latLng = new LatLng(Double.parseDouble(BaseActivity.lat), Double.parseDouble(BaseActivity.lon));

                MarkerOptions marker1 = new MarkerOptions().position(new LatLng(Double.parseDouble(BaseActivity.lat), Double.parseDouble(BaseActivity.lon))).flat(true).anchor(0.5f, 0.5f);
                // MarkerOptions marker1 = new MarkerOptions().position(new LatLng(40.04, -75.487)).flat(true).anchor(0.5f, 0.5f);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                marker1.title(getResources().getString(R.string.you));


                //  gmap.addMarker(marker1);
                //  marker = gmap.addMarker(marker1);
                //  marker.setTag("0");
//
                // gmap.animateCamera( )
                gmap.animateCamera(cameraUpdate);
                gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        if (marker.getTitle().equalsIgnoreCase(getResources().getString(R.string.you))) {

                        } else {
                            System.out.println("ssssssssssssss = " + marker.getTag());
                            int aa = Integer.parseInt(""+marker.getTag());
                            System.out.println("ssssssssssssss5555555 = " + aa);

                            //   seeContracts((Integer) marker.getTag());
//                            Intent i = new Intent(FindBuddyActivity.this, NearFriendsProfileDetActivity.class);
//                            i.putExtra("friend_id",""+friendsBeanLstArrayList.get(aa).getId());
//                            startActivity(i);

                            binding.markerlay.setVisibility(View.VISIBLE);
                            if(friendsBeanLstArrayList.get(aa).getToday_open_time()!=null) {
                                binding.timeTv.setText(friendsBeanLstArrayList.get(aa).getToday_open_time());
                            }
                            binding.distanceTv.setText(friendsBeanLstArrayList.get(aa).getDistance()+" "+getResources().getString(R.string.km));
                            binding.ratingtv.setText(friendsBeanLstArrayList.get(aa).getTotal_rating_count());
                            binding.ratingbar.setRating(Float.parseFloat(friendsBeanLstArrayList.get(aa).getTotal_rating_count()));
                            binding.restnameTv.setText(friendsBeanLstArrayList.get(aa).getProviderName());
                            //  binding.quantitylefttv.setText(friendsBeanLstArrayList.get(aa).getItem_left_quantity()+" "+getResources().getString(R.string.items)+" "+getResources().getString(R.string.left)+"\n"+friendsBeanLstArrayList.get(aa).getMagic_food_left_quantity()+" "+getResources().getString(R.string.magicbags)+" "+getResources().getString(R.string.left));
                            binding.locationTv.setText(friendsBeanLstArrayList.get(aa).getProviderStreatAddress());
                            if (friendsBeanLstArrayList.get(aa).getStoreLogo() != null && !friendsBeanLstArrayList.get(aa).getStoreLogo().isEmpty()) {
                                Picasso.get().load(friendsBeanLstArrayList.get(aa).getStoreLogo()).placeholder(R.color.lightgrey).into(binding.restimage);
                            }

//                            businessid = friendsBeanLstArrayList.get(aa).getId();
//                            usernametv1.setText(friendsBeanLstArrayList.get(aa).getBusinessName());
//                            markerbusinesstxt.setText(friendsBeanLstArrayList.get(aa).getCatName());
//
//                            if (friendsBeanLstArrayList.get(aa).getBusinessImages() != null && friendsBeanLstArrayList.get(aa).getBusinessImages().size() > 0) {
//                                Picasso.get().load(friendsBeanLstArrayList.get(aa).getBusinessImages().get(0).getImage()).placeholder(R.color.verylightgrey).into(userprof1);
//                            }
//                            markerratingtxt.setText("by "+friendsBeanLstArrayList.get(aa).getRating_count()+" users");
//                            markerratingbar.setRating(Float.parseFloat(friendsBeanLstArrayList.get(aa).getRating()));

                        }
                        return false;
                    }
                });

                //  addCustomMarker();
            }
        });

        binding.cancelimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.markerlay.setVisibility(View.GONE);
            }
        });
        gmap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if (googlemarker_pos != null) {
                    googlemarker_pos.setPosition(latLng1);
                }
            }
        });


    }
    private void addCustomMarker(double latitude, double longitude, String image, String name, int k) {
        Log.e("FRIENDIMAGE "," >> "+image);
        LatLng latLng = new LatLng(latitude, longitude);

        if (gmap == null) {
            return;
        }
        latlong = new LatLng(latitude,longitude);
        mark_img = image;
        //  new LoadMarker().execute(image);
        // adding a marker on map with image from  drawable
        Marker marker =   gmap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(createStoreMarker(image,name,k))));
        marker.setTag(k);
        marker.setTitle(name);
        MarkerOptions marker1 = new MarkerOptions().position(latLng).flat(true).anchor(0.5f, 0.5f);
        // MarkerOptions marker1 = new MarkerOptions().position(new LatLng(40.04, -75.487)).flat(true).anchor(0.5f, 0.5f);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);



        //  gmap.addMarker(marker1);
        //  marker = gmap.addMarker(marker1);
        //  marker.setTag("0");
//
        // gmap.animateCamera( )
        gmap.animateCamera(cameraUpdate);
        markerList.add(marker);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class LoadMarker extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            // Get bitmap from server
            Log.e("DDDDDD "," > "+mark_img);
            Bitmap overlay;
            try {
                URL url = new URL(mark_img);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                overlay = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return overlay;
        }

        protected void onPostExecute(Bitmap bitmap) {


            createStoreMarker("","Shyam", 0);
            // If received bitmap successfully, draw it on our drawable
/*
            if (bitmap != null) {
                Log.e("DDDDDD latlong "," > "+latlong);
if (latlong!=null){
    View custom_layout = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
    ImageView iv_category_logo = (ImageView) custom_layout.findViewById(R.id.markimage);
  //  Bitmap pinbit = Bitmap.createScaledBitmap(bitmap, 40, 60, false);
    iv_category_logo.setImageBitmap(bitmap);
    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(createDrawableFromView(FindBuddyActivity.this, custom_layout));


    Marker marker =   gmap.addMarker(new MarkerOptions()
            .position(latlong)
            .icon(bitmapDescriptor));
    //  marker.setTag(k);
    markerList.add(marker);
}




            }
*/
        }

        /**
         * Convert a view to bitmap
         *
         * @param context
         * @param view
         * @return
         */
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

    private Bitmap createStoreMarker(String image, String name, int k) {
        View markerLayout = getLayoutInflater().inflate(R.layout.view_custom_marker, null);

        ImageView markimage = (ImageView) markerLayout.findViewById(R.id.markimage);
        // markimage.setImageBitmap(bitmapa);
        try {
//            if (k==0){
//                markimage.setImageResource(R.drawable.pinone);
            Picasso.get().load(image).placeholder(R.drawable.pintwo).into(markimage);
            //   }
//            if (k==1){
//                markimage.setImageResource(R.drawable.pintwo);
//               // Picasso.get().load(image).placeholder(R.drawable.pintwo).into(markimage);
//            }
//            if (k==2){
//                markimage.setImageResource(R.drawable.pinthree);
//                //Picasso.get().load(image).placeholder(R.drawable.pinthree).into(markimage);
//            }


        }catch (Exception e){
            e.printStackTrace();
        }
        TextView markerRating = (TextView) markerLayout.findViewById(R.id.markname);
        //    markerImage.setImageResource(R.drawable.ic_home_marker);
        markerRating.setText(name);



        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,          RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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

    public class AllRestaurnatAdapter extends RecyclerView.Adapter<AllRestaurnatAdapter.ViewHolder> {
        Context context;
        List<MapBeanList> mapBeanLists;

        public AllRestaurnatAdapter(Context context, List<MapBeanList> mapBeanLists) {
            this.context = context;
            this.mapBeanLists = mapBeanLists;
        }


        @NonNull
        @Override
        public AllRestaurnatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MapgridlistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.mapgridlistitem, parent, false);
            return new AllRestaurnatAdapter.ViewHolder(binding);
        }
        public void filter(String charText) {
            Log.e("COMEADDD","DDDDDDD");
            //charText = charText.toLowerCase(Locale.getDefault());
            charText = charText.toString().toLowerCase();
            mapBeanLists.clear();
            if (charText.length() == 0) {
                mapBeanLists.addAll(searchbuymessageBeanListArrayList);
            } else {
                for (MapBeanList wp : searchbuymessageBeanListArrayList) {
                    if (wp.getName().toLowerCase().contains(charText) || wp.getNameAr().toLowerCase().contains(charText))//.toLowerCase(Locale.getDefault())
                    {
                        Log.e("COMEADDD","DDD");
                        mapBeanLists.add(wp);
                    }
                }
                if (mapBeanLists==null||mapBeanLists.isEmpty()){
                    //nodatatxt.setVisibility(View.VISIBLE);
                    // noitemtv.setText(""+getResources().getString(R.string.noitmewithsearch));
                }
                else {
                    // nodatatxt.setVisibility(View.GONE);
                }
            }
            notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(@NonNull AllRestaurnatAdapter.ViewHolder holder, int position) {
            if(language.equalsIgnoreCase("en")) {
                holder.binding.usernametv.setText(mapBeanLists.get(position).getName());
            }else{
                holder.binding.usernametv.setText(mapBeanLists.get(position).getNameAr());
            }

            if (mapBeanLists.get(position).getImage() != null && !mapBeanLists.get(position).getImage().isEmpty()) {
                Picasso.get().load(mapBeanLists.get(position).getImage()).placeholder(R.color.lightgrey).into(holder.binding.mapimage);
            }
            if (mapBeanLists.get(position).getCountryIcon() != null && !mapBeanLists.get(position).getCountryIcon().isEmpty()) {
                Picasso.get().load(mapBeanLists.get(position).getCountryIcon()).placeholder(R.color.lightgrey).into(holder.binding.countryimage);
            }
//
//            if (providerBeanLists.get(position).getFav_provider().equalsIgnoreCase("Yes")){
//                holder.binding.favic.setBackgroundResource(R.drawable.fav_selected);
//            }else{
//                holder.binding.favic.setBackgroundResource(R.drawable.fav);
//            }
//
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // if(mapBeanLists.get(position).getSubscription_status().equalsIgnoreCase("No")) {
                        Intent i = new Intent(MapsActivity.this, SubMapsList.class);
                        i.putExtra("country_id", mapBeanLists.get(position).getId());
                        i.putExtra("country_name", mapBeanLists.get(position).getName());
                        i.putExtra("country_name_ar", mapBeanLists.get(position).getNameAr());
                        i.putExtra("type", type);
                        i.putExtra("duration", duration);
                        i.putExtra("order_id", "" + order_id);
                        i.putExtra("come_from", "order");
                        i.putExtra("paid_amount", "" + paid_amount);
                        //   i.putExtra("type","USER");
                        startActivity(i);
//                    }else{
//                        final Dialog dialogSts = new Dialog(MapsActivity.this);
//                        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialogSts.setCancelable(false);
//                        dialogSts.setContentView(R.layout.areyousure);
//                        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        RelativeLayout closepopup = dialogSts.findViewById(R.id.closepopup);
//                        TextView yes_tv = dialogSts.findViewById(R.id.yes_tv);
//                        TextView message_tv = dialogSts.findViewById(R.id.message_tv);
//                        message_tv.setText(""+getResources().getString(R.string.youhaveactive));
//
//                        closepopup.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogSts.dismiss();
//
//                            }
//                        });
//                        yes_tv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogSts.dismiss();
//                                Intent i = new Intent(MapsActivity.this, SubMapsList.class);
//                                i.putExtra("country_id",mapBeanLists.get(position).getId());
//                                i.putExtra("country_name",mapBeanLists.get(position).getName());
//                                i.putExtra("country_name_ar",mapBeanLists.get(position).getNameAr());
//                                startActivity(i);
//                            }
//                        });
//                        dialogSts.show();
//                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            // return 6;
            return mapBeanLists == null ? 0 : mapBeanLists.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            MapgridlistitemBinding binding;

            public ViewHolder(@NonNull MapgridlistitemBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
    }


}
package main.com.cineramamaps.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;

import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityMapDetailsBinding;
import main.com.cineramamaps.databinding.ActivityPlacesDetailsBinding;
import main.com.cineramamaps.databinding.ActivityServiceDetailBinding;
import main.com.cineramamaps.databinding.LayoutCustmOpencloseBinding;
import main.com.cineramamaps.databinding.LayoutCustmReviewBinding;
import main.com.cineramamaps.databinding.TaglistitemBinding;
import main.com.cineramamaps.model.AllProductList;
import main.com.cineramamaps.model.Bannerdata;
import main.com.cineramamaps.model.PlaceBeanNew;
import main.com.cineramamaps.model.PlaceBeanNewData;
import main.com.cineramamaps.model.PlaceDetail;
import main.com.cineramamaps.model.PlacesBean;
import main.com.cineramamaps.model.ProductAdditional;
import main.com.cineramamaps.model.Review;
import main.com.cineramamaps.model.SinglePlaceBean;
import main.com.cineramamaps.model.SingleServiceBean;
import main.com.cineramamaps.model.TagDetail;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
        , ResultCallback<LocationSettingsResult> {
    GPSTracker tracker;
    ArrayList<Bannerdata> bannerlist=new ArrayList<>();
    String lat="0.0",lon="0.0";
    boolean openclosestatus=false;
   List<String> weekdays = new ArrayList<>();
    private LocationManager mLocationManager = null;
    private PlaceDetail placeDetail;
    private static final int LOCATION_INTERVAL = 3000;
    private boolean isCurrentUserFavorite;
    private static final float LOCATION_DISTANCE = 10f;
    static String latitudes = "", longitudes = "";
    GPSTracker gpsTracker;
    private static final String TAG = "MyLocationService";
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    Location location;
    Location oldlocation;
    public static double longitude = 0.0, latitude = 0.0;
    private double oldlongitude = 0.0, oldlatitude = 0.0;
    Location location_ar;
    LocationManager locationManager;
    String mark_img = "";
    private LatLng latlong = null;
    private GoogleMap gmap;
    List<Marker> markerList = new ArrayList<Marker>();
    private MarkerOptions options = new MarkerOptions();
    Marker googlemarker_pos;
    ActivityPlacesDetailsBinding binding;
    SessionManager session;
    public static String fav="";
String likestatus="";
    int item_count_val = 0;
    private int dotscount;
    private ImageView[] dots;
    float finalAmount;
    String id,place_id="", itemprice_main = "", like_status = "", itemprice_total, cat_id = "", cat_name = "", extraitem_total_according_quantity_str = "", extra_item_name_str = "", extra_item_qty_str = "", extra_item_id = "", extra_item_price_str = "";
    int remainingitemquantity=0;
    PlaceBeanNew successData;

    private String language = "";
    MyLanguageSession myLanguageSession;
    boolean currentUserFavourite;

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
        if(!fav.equalsIgnoreCase("")){
            fav = "";
            getItemDetail();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_places_details);
        isCurrentUserFavorite = getIntent().getBooleanExtra("currentUserFavourite", false);
        session = SessionManager.get(PlacesDetailsActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            id = bundle.getString("id");
            place_id = bundle.getString("place_id");
            currentUserFavourite = bundle.getBoolean("currentUserFavourite");
            toggleHeartIcon(isCurrentUserFavorite);
        }
        tracker = new GPSTracker(this);

        getLatLong();
        getCurrentLocation();
        initGps();
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("placeId", id);
                resultIntent.putExtra("currentUserFavourite", isCurrentUserFavorite);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        binding.heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                favorite();
            }
        });


        binding.createtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(PlacesDetailsActivity.this,CreateTripActivity.class);
                ii.putExtra("id","");
                ii.putExtra("place_id",""+successData.getResult().getId());
                ii.putExtra("map_place_id",""+successData.getResult().getPlaceid());
                ii.putExtra("city_id",""+successData.getResult().getCityId());
                ii.putExtra("city_name",""+successData.getResult().getCityDetails().getName());
                ii.putExtra("city_name_ar",""+successData.getResult().getCityDetails().getNameAr());
                ii.putExtra("place_name",""+successData.getResult().getPlaceName());
                ii.putExtra("place_name_ar",""+successData.getResult().getPlaceNameAr());
                ii.putExtra("address",""+successData.getResult().getAddress());
                ii.putExtra("lat",""+successData.getResult().getLat());
                ii.putExtra("lon",""+successData.getResult().getLon());
                startActivity(ii);
            }
        });

        gpsTracker = new GPSTracker(PlacesDetailsActivity.this);

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
//        try {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    LOCATION_INTERVAL,
//                    LOCATION_DISTANCE
//                    mLocationListeners[0]
//            );
//        } catch (SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
//        }
        bindview();
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        clickevent();
getItemDetail();
binding.phonelay.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(successData.getResult().getGoogleMap().getResult().getFormattedPhoneNumber()!=null) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", successData.getResult().getGoogleMap().getResult().getFormattedPhoneNumber(), null));
            startActivity(intent);
        }else{
            Toast.makeText(PlacesDetailsActivity.this,""+getResources().getString(R.string.phonenumberisnotthere),Toast.LENGTH_LONG).show();
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

        gmap.setBuildingsEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //  return;
        }
        gmap.setMyLocationEnabled(true);
        gmap.getUiSettings().setMyLocationButtonEnabled(true);
        gmap.getUiSettings().setMapToolbarEnabled(false);
        gmap.getUiSettings().setZoomControlsEnabled(true);

        LatLng latLng = new LatLng(40.04, -75.487);

        latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

        MarkerOptions marker1 = new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).flat(true).anchor(0.5f, 0.5f);
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
//                    Intent ii = new Intent(PlacesListActivity.this,PlacesDetailsActivity.class);
//                    ii.putExtra("id",listt.get(aa).getId());
//                    startActivity(ii);
                    //   seeContracts((Integer) marker.getTag());
//                            Intent i = new Intent(FindBuddyActivity.this, NearFriendsProfileDetActivity.class);
//                            i.putExtra("friend_id",""+friendsBeanLstArrayList.get(aa).getId());
//                            startActivity(i);


//                    markerlay.setVisibility(View.VISIBLE);
//                    providerrecyclerview.setVisibility(View.VISIBLE);
////                            if(friendsBeanLstArrayList.get(aa).getToday_open_time()!=null) {
////                                binding.timeTv.setText(friendsBeanLstArrayList.get(aa).getToday_open_time());
////                            }
//                    // distance_tv.setText(listt.get(aa).getDistance()+" "+getResources().getString(R.string.km));
//                    //  ratingtv.setText(friendsBeanLstArrayList.get(aa).getTotal_rating_count());
//                    //  binding.ratingbar.setRating(Float.parseFloat(friendsBeanLstArrayList.get(aa).getTotal_rating_count()));
//                    if(language.equalsIgnoreCase("en")) {
//                        restname_tv.setText(listt.get(aa).getPlaceName());
//                    }else{
//                        restname_tv.setText(listt.get(aa).getPlaceNameAr());
//                    }
//                    //  binding.quantitylefttv.setText(friendsBeanLstArrayList.get(aa).getItem_left_quantity()+" "+getResources().getString(R.string.items)+" "+getResources().getString(R.string.left)+"\n"+friendsBeanLstArrayList.get(aa).getMagic_food_left_quantity()+" "+getResources().getString(R.string.magicbags)+" "+getResources().getString(R.string.left));
//                    location_tv.setText(listt.get(aa).getAddress());
////                            if (friendsBeanLstArrayList.get(aa).getStoreLogo() != null && !friendsBeanLstArrayList.get(aa).getStoreLogo().isEmpty()) {
////                                Picasso.get().load(friendsBeanLstArrayList.get(aa).getStoreLogo()).placeholder(R.color.lightgrey).into(binding.restimage);
////                            }
//
////                            businessid = friendsBeanLstArrayList.get(aa).getId();
////                            usernametv1.setText(friendsBeanLstArrayList.get(aa).getBusinessName());
////                            markerbusinesstxt.setText(friendsBeanLstArrayList.get(aa).getCatName());
////
////                            if (friendsBeanLstArrayList.get(aa).getBusinessImages() != null && friendsBeanLstArrayList.get(aa).getBusinessImages().size() > 0) {
////                                Picasso.get().load(friendsBeanLstArrayList.get(aa).getBusinessImages().get(0).getImage()).placeholder(R.color.verylightgrey).into(userprof1);
////                            }
////                            markerratingtxt.setText("by "+friendsBeanLstArrayList.get(aa).getRating_count()+" users");
////                            markerratingbar.setRating(Float.parseFloat(friendsBeanLstArrayList.get(aa).getRating()));
//                    PlacesFragment.AllRestaurnatAdapter adapter =   new PlacesFragment.AllRestaurnatAdapter(getActivity(), listt.get(aa).getTagDetails());
//                    binding..setAdapter(adapter);
                }
                return false;
            }
        });
        gmap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
//                gmap.setMapStyle(
//                        MapStyleOptions.loadRawResourceStyle(
//                                getActivity(), R.raw.stylemap_3));




                //  addCustomMarker();
            }
        });

//        cancelimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                markerlay.setVisibility(View.GONE);
//                providerrecyclerview.setVisibility(View.GONE);
//            }
//        });
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
    private void addCustomMarker(double latitude, double longitude, String image, String name, int k, PlaceBeanNewData placeDetail) {
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
                .icon(BitmapDescriptorFactory.fromBitmap(mapMarker(placeDetail))));
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

//    private Bitmap createStoreMarker(String image, String name, int k) {
//        View markerLayout = getLayoutInflater().inflate(R.layout.view_custom_marker, null);
//
//        ImageView markimage = (ImageView) markerLayout.findViewById(R.id.markimage);
//        // markimage.setImageBitmap(bitmapa);
//        try {
////            if (k==0){
////                markimage.setImageResource(R.drawable.pinone);
//            Picasso.get().load(image).placeholder(R.drawable.pintwo).into(markimage);
//            //   }
////            if (k==1){
////                markimage.setImageResource(R.drawable.pintwo);
////               // Picasso.get().load(image).placeholder(R.drawable.pintwo).into(markimage);
////            }
////            if (k==2){
////                markimage.setImageResource(R.drawable.pinthree);
////                //Picasso.get().load(image).placeholder(R.drawable.pinthree).into(markimage);
////            }
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        TextView markerRating = (TextView) markerLayout.findViewById(R.id.markname);
//        //    markerImage.setImageResource(R.drawable.ic_home_marker);
//        markerRating.setText(name);
//
//
//
//        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());
//
//        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        markerLayout.draw(canvas);
//        return bitmap;
//    }
private Bitmap mapMarker(PlaceBeanNewData placeDetail) {

    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View markerLayout = inflater.inflate(R.layout.activity_map_marker, null);
    List<String> colorList = placeDetail.getTagDetails()
            .stream()
            .map(TagDetail::getColorCode)
            .collect(Collectors.toList());

    ImageView innerLayer = markerLayout.findViewById(R.id.pieCircle);
    ImageView innerFrame = markerLayout.findViewById(R.id.innerFrame);
    ImageView outerFrame = markerLayout.findViewById(R.id.outerFrame);
    ImageView pinIcon = markerLayout.findViewById(R.id.pinIcon);
    if (!placeDetail.getIconBackgroundColor().isEmpty() ){
        ColorStateList tint = ColorStateList.valueOf(Color.parseColor(placeDetail.getIconBackgroundColor()));
        outerFrame.setImageTintList(tint);
        outerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
    }
    Glide.with(this)  // Replace context with your activity or fragment context
            .load(placeDetail.getIcon())
            .into(pinIcon);
    if (!colorList.isEmpty()) {
        String lastColor = colorList.get(colorList.size() - 1); // Get the last element
        ColorStateList tint = ColorStateList.valueOf(Color.parseColor(lastColor));
        innerFrame.setImageTintList(tint);
        innerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
    }
    applyPieChart(innerLayer, colorList);
    markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());
    Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(),
            markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    markerLayout.draw(canvas);
    return bitmap;
}
    public void applyPieChart(ImageView innerLayer, List<String> colorList) {
        Bitmap pieChartBitmap = createPieChartBitmap(innerLayer,colorList);
        BitmapDrawable pieChartDrawable = new BitmapDrawable(innerLayer.getResources(), pieChartBitmap);
        innerLayer.setImageDrawable(pieChartDrawable);
    }
    private Bitmap createPieChartBitmap(ImageView innerLayer,List<String> colorList) {
        int width = 100;
        int height = 100;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        float startAngle = 0;
        for (String color : colorList) {
            paint.setColor(Color.parseColor(color));
            canvas.drawArc(new RectF(0, 0, width, height), startAngle, 360f / colorList.size(), true, paint);
            startAngle += 360f / colorList.size(); // Update angle for next slice
        }
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
    private void bindview() {
        binding.extraitemslist.setExpanded(true);


    }

    private void itemAddedCartSucc() {
//        final Dialog dialogSts = new Dialog(PlacesDetailsActivity.this);
//        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogSts.setCancelable(false);
//        dialogSts.setContentView(R.layout.popup_item_added_cart);
//        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        TextView dismiss = dialogSts.findViewById(R.id.dismiss);
//        TextView gotocart = dialogSts.findViewById(R.id.gotocart);
//
//
//        dismiss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogSts.dismiss();
//                finish();
//
//            }
//        });
//        gotocart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(PlacesDetailsActivity.this, MainTabActivity.class);
//                i.putExtra("WhichIndex", 2);
//                startActivity(i);
//                finish();
//
//
//            }
//        });
//        dialogSts.show();
    }


    private void clickevent() {

        binding.textlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.maplaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 Intent ii = new Intent(PlacesDetailsActivity.this,PlaceMapActivity.class);
//                 ii.putExtra("id",""+id);
//                 ii.putExtra("lat",""+successData.getResult().getGoogleMap().getResult().getGeometry().getLocation().getLat());
//                 ii.putExtra("lon",""+successData.getResult().getGoogleMap().getResult().getGeometry().getLocation().getLng());
//                 ii.putExtra("icon",""+successData.getResult().getGoogleMap().getResult().getIcon());
//                 ii.putExtra("name",""+successData.getResult().getGoogleMap().getResult().getName());
//                 startActivity(ii);

                if(!successData.getResult().getGoogle_map_link().equalsIgnoreCase("")) {
                    String uri = successData.getResult().getGoogle_map_link();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }else{
                    Toast.makeText(PlacesDetailsActivity.this,""+getResources().getString(R.string.maplinknotavaiaible),Toast.LENGTH_LONG).show();
                }
//                  if(language.equalsIgnoreCase("en")) {
//                      if(successData.getResult().getGoogleMap().getResult().getGeometry()!= null && successData.getResult().getGoogleMap().getResult().getGeometry().getLocation()!=null && successData.getResult().getGoogleMap().getResult().getGeometry().getLocation().getLat()!=null) {
//                          uri = "http://maps.google.com/maps?q=loc:" + successData.getResult().getGoogleMap().getResult().getGeometry().getLocation().getLat() + "," + successData.getResult().getGoogleMap().getResult().getGeometry().getLocation().getLng() + " (" + successData.getResult().getPlaceName() + ")";
//                      }else{
//                          uri = "http://maps.google.com/maps?q=loc:" + successData.getResult().getLat() + "," + successData.getResult().getLon()+ " (" + successData.getResult().getPlaceName() + ")";
//
//                      }
//                      System.out.println("ssssssssssssssssssssssssss = "+Uri.parse(uri));
//                      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                      startActivity(intent);
//                  }else{
//                      if(successData.getResult().getGoogleMap().getResult().getGeometry()!= null && successData.getResult().getGoogleMap().getResult().getGeometry().getLocation()!=null && successData.getResult().getGoogleMap().getResult().getGeometry().getLocation().getLat()!=null) {
//                          uri = "http://maps.google.com/maps?q=loc:" + successData.getResult().getGoogleMap().getResult().getGeometry().getLocation().getLat() + "," + successData.getResult().getGoogleMap().getResult().getGeometry().getLocation().getLng() + " (" + successData.getResult().getPlaceNameAr() + ")";
//                      }else{
//                          uri = "http://maps.google.com/maps?q=loc:" + successData.getResult().getLat() + "," + successData.getResult().getLon()+ " (" + successData.getResult().getPlaceNameAr() + ")";
//
//                      }
//                      System.out.println("ssssssssssssssssssssssssss = "+Uri.parse(uri));
//                      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                      startActivity(intent);
//                  }
            }
        });
        binding.videolay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"), "video/mp4");
//                startActivity(intent);
//           String vv =     "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                intent.setDataAndType(Uri.parse(vv), "video/*");
//                                startActivity(intent);
//           String vv =     "https://www.youtube.com/watch?v=ANI8A8GPaU4";
//                playYouTubeVideo(""+vv);
                if(language.equalsIgnoreCase("en")) {
                    if (successData.getResult().getVideo_link_en() != null) {
                        if(!successData.getResult().getVideo_link_en().equalsIgnoreCase("")) {
                            if(successData.getResult().getVideo_link_en().contains("youtube")){
                                playYouTubeVideo(""+successData.getResult().getVideo_link_en());
                            }else {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse(successData.getResult().getVideo_link_en()), "video/*");
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(PlacesDetailsActivity.this,""+getResources().getString(R.string.videolinknotavaiaible),Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    if (successData.getResult().getVideo_link_ar() != null) {
                        if(!successData.getResult().getVideo_link_ar().equalsIgnoreCase("")) {
                            if(successData.getResult().getVideo_link_ar().contains("youtube")){
                                playYouTubeVideo(""+successData.getResult().getVideo_link_ar());
                            }else {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse(successData.getResult().getVideo_link_ar()), "video/*");
                                startActivity(intent);
                            }
                        }
                        else{
                            Toast.makeText(PlacesDetailsActivity.this,""+getResources().getString(R.string.videolinknotavaiaible),Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        binding.seeallreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlacesDetailsActivity.this, GiveRatingActivity.class);
                i.putExtra("provider_id",id);
                i.putExtra("order_id",id);
                i.putExtra("reviewtype","Place");
                startActivity(i);
            }
        });





    }
    private void playYouTubeVideo(String videoUrl) {
        // Create an intent to view the YouTube URL
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));

        // Check if the YouTube app is available to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);  // This will open the URL in the default YouTube app or browser
        }
    }

    private void getItemDetail() {
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getPlaceDetail1(session.getUserID(),id, place_id, lat,lon,language).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            successData = new Gson().fromJson(responseData, PlaceBeanNew.class);

                            // Initialize placeDetail here
                            placeDetail = new PlaceDetail();
                            placeDetail.setCurrentUserFavorite(successData.getResult().getFavStatus().equals("Like"));
                            placeDetail.setId(successData.getResult().getId());
                            // Set other properties as needed

                            // Rest of your code...
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
    public class AllRestaurnatAdapter extends RecyclerView.Adapter<AllRestaurnatAdapter.ViewHolder> {
        Context context;
        List<TagDetail> mapBeanLists;

        public AllRestaurnatAdapter(Context context, List<TagDetail> mapBeanLists) {
            this.context = context;
            this.mapBeanLists = mapBeanLists;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TaglistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.taglistitem, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if(language.equalsIgnoreCase("en")) {
                holder.binding.tagname.setText(mapBeanLists.get(position).getTagName());
            }else{
                holder.binding.tagname.setText(mapBeanLists.get(position).getTagNameAr());
            }
            holder.binding.tagname.setBackgroundColor(Color.parseColor(""+mapBeanLists.get(position).getColorCode()));


//

        }

        @Override
        public int getItemCount() {
            // return 6;
            return mapBeanLists == null ? 0 : mapBeanLists.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TaglistitemBinding binding;

            public ViewHolder(@NonNull TaglistitemBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
    }

    public class OpencloseAdapter extends RecyclerView.Adapter<OpencloseAdapter.ViewHolder> {
        Context context;
        List<String> mapBeanLists;

        public OpencloseAdapter(Context context, List<String> mapBeanLists) {
            this.context = context;
            this.mapBeanLists = mapBeanLists;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutCustmOpencloseBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_openclose, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                holder.binding.nametv.setText(mapBeanLists.get(position));

          //  holder.binding.lay.setBackgroundColor(Color.parseColor(""+mapBeanLists.get(position).getColorCode()));


//

        }

        @Override
        public int getItemCount() {
            // return 6;
            return mapBeanLists == null ? 0 : mapBeanLists.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LayoutCustmOpencloseBinding binding;

            public ViewHolder(@NonNull LayoutCustmOpencloseBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
    }
    public class ExtraItemAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        private ArrayList<ProductAdditional> productAdditionalArrayList;

        public ExtraItemAdapter(Context applicationContext, ArrayList<ProductAdditional> productAdditionalArrayList) {
            this.context = applicationContext;
            this.productAdditionalArrayList = productAdditionalArrayList;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return productAdditionalArrayList == null ? 0 : productAdditionalArrayList.size();
            // return count_list1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.layout_custm_extra_item, null);
            TextView extraitemname1 = view.findViewById(R.id.extraitemname1);
            TextView extpricetv = view.findViewById(R.id.extpricetv);
            CheckBox extcheckbox = view.findViewById(R.id.extcheckbox);
            extpricetv.setText(getResources().getString(R.string.currency) + "" + productAdditionalArrayList.get(i).getItemPrice());
            extraitemname1.setText(productAdditionalArrayList.get(i).getItemName());


            extcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        productAdditionalArrayList.get(i).setSelected(true);
                        getAmount(productAdditionalArrayList.get(i), "plus");
                    } else {
                        productAdditionalArrayList.get(i).setSelected(false);
                        getAmount(productAdditionalArrayList.get(i), "minus");
                    }
                }
            });
            return view;
        }
    }


    private float getAmount(ProductAdditional additional, String minus_plus) {
        int price = 0;
        try {
            price = Integer.parseInt(additional.getItemPrice().trim());
            Log.e("GetAmountMainItemCount ", " >> " + item_count_val);
            Log.e("GetAmountMainprice ", " >> " + price);

            price = price * item_count_val;
            Log.e("ygg ", " >> " + price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (additional.isSelected()) {
            finalAmount += price;

        } else {
            finalAmount -= price;

        }
        itemprice_total = String.valueOf(finalAmount);
        String price_st = String.valueOf(finalAmount);
        if (price_st != null && !price_st.equalsIgnoreCase("")) {
            double tot = Double.parseDouble(price_st);
            binding.bottomFinalPriceTv.setText(getResources().getString(R.string.currency) + "" + String.format("%.2f", tot) + " for " + item_count_val + " " + getResources().getString(R.string.items));
            binding.btompricelay.setVisibility(View.VISIBLE);
        }

        return finalAmount;

    }


//    private HashMap<String, String> getParam() {
//
//
//        HashMap<String, String> param = new HashMap<>();
//        param.put("user_id", session.getUserID());
//        param.put("product_id", successData.getResult().getId());
//        param.put("cat_id", successData.getResult().getCatId());
//        param.put("cat_name", "" + successData.getResult().getCatName());
//        param.put("product_name", "" + successData.getResult().getItemName());
//        param.put("product_price", "" + itemprice_main);
//        param.put("provider_id", "" + successData.getResult().getProviderId());
//        param.put("total_amount", "" + finalAmount);
//        param.put("before_discount_amount", "" + finalAmount);
//        param.put("extra_item_id", "" + extra_item_id);
//        param.put("extra_item_name", "" + extra_item_name_str);
//        param.put("extra_item_price", "" + extra_item_price_str);
//        param.put("extra_item_qty", "" + extra_item_qty_str);
//        param.put("total_extra_item_price", "" + extraitem_total_according_quantity_str);
//        param.put("quantity", "" + item_count_val);
//        return param;
//
//    }



//    private void addToCart() {
//
//
//        binding.progressbar.setVisibility(View.VISIBLE);
//        ApiCall.get().Create().addToCart(getParam()).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                binding.progressbar.setVisibility(View.GONE);
//                Log.e("AddToCart ", " >> " + response);
//
//                if (response.isSuccessful()) {
//
//                    try {
//                        String responseData = response.body().string();
//                        JSONObject object = new JSONObject(responseData);
//                        Log.e("AddToCart Response ", " >> " + responseData);
//                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            // getCartData();
//                            //Toast.makeText(PlacesDetailsActivity.this, getResources().getString(R.string.itemaddedtocart), Toast.LENGTH_SHORT).show();
//                            itemAddedCartSucc();
//
//                        } else {
//                            Toast.makeText(PlacesDetailsActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    Toast.makeText(PlacesDetailsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                binding.progressbar.setVisibility(View.GONE);
//                Toast.makeText(PlacesDetailsActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    public   class ViewPagerAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Bannerdata> images;

        public ViewPagerAdapter(Context context, List<Bannerdata> images) {
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            // return 4;
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_slide_item, null);
            final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress);
            //progress.setVisibility(View.VISIBLE);

            Picasso.get().load(images.get(position).getBannername()).placeholder(R.color.lightgrey).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                        Intent ii = new Intent(context, ItemImageBigActivity.class);
//                        ii.putExtra("image",""+images.get(position).getBannername());
//                        String im = images.get(position).getBannername().replace(BaseUrl.image_baseurl,"");
//                        ii.putExtra("name",""+im);
//                        context.startActivity(ii);
                }
            });
//if(position == 0){
            // imageView.setImageResource(R.drawable.service1);
//}
//        if(position == 1){
//            imageView.setImageResource(R.drawable.service2);
//        }
//        if(position == 2){
//            imageView.setImageResource(R.drawable.service3);
//        }
//        if(position == 3){
//            imageView.setImageResource(R.drawable.service4);
//        }


            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }
    }

    private HashMap<String, String> getFavParam() {


        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("place_id", id);
        param.put("status", likestatus);

        return param;

    }

    private void addTofavCall() {



        ApiCall.get().Create().addTolike(getFavParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("AddToCart ", " >> " + response);

                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("AddToFav Response ", " >> " + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                           getItemDetail();

                        } else {
                            Toast.makeText(PlacesDetailsActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(PlacesDetailsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(PlacesDetailsActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class ReveiwListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        private List<Review> ratingBeanLists;
        public ReveiwListAdapter(Context applicationContext, List<Review> ratingBeanLists) {
            this.context = applicationContext;
            this.ratingBeanLists = ratingBeanLists;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            //  return 2;
            return ratingBeanLists == null ? 0 : ratingBeanLists.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            //  view = inflter.inflate(R.layout.layout_yachts_item_new_sec, null);


            YachtsViewHolder holder;

            if (view == null) {
                LayoutCustmReviewBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_review,viewGroup,false);

                holder = new YachtsViewHolder(binding);
                holder.view = binding.getRoot();
                holder.view.setTag(holder);
            }
            else {
                holder = (YachtsViewHolder) view.getTag();
            }

            holder.binding.commenttv.setText(ratingBeanLists.get(position).getText());
            holder.binding.datetv.setText(""+ratingBeanLists.get(position).getTime());
            holder.binding.nametv.setText(ratingBeanLists.get(position).getAuthorName());
            holder.binding.ratingtv.setText(""+ratingBeanLists.get(position).getRating());
            holder.binding.ratingbar.setRating(Float.parseFloat(""+ratingBeanLists.get(position).getRating()));
            if (ratingBeanLists.get(position).getProfilePhotoUrl() != null && !ratingBeanLists.get(position).getProfilePhotoUrl().isEmpty()) {
                Picasso.get().load(ratingBeanLists.get(position).getProfilePhotoUrl()).placeholder(R.color.lightgrey).into(holder.binding.profileic);
            }


            return holder.view;



        }

        private  class YachtsViewHolder {
            private View view;
            private LayoutCustmReviewBinding binding;

            YachtsViewHolder(LayoutCustmReviewBinding binding) {
                this.view = binding.getRoot();
                this.binding = binding;
            }
        }
    }



    public class OpenListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        private List<String> ratingBeanLists;
        public OpenListAdapter(Context applicationContext, List<String> ratingBeanLists) {
            this.context = applicationContext;
            this.ratingBeanLists = ratingBeanLists;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            //  return 2;
            return ratingBeanLists == null ? 0 : ratingBeanLists.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            //  view = inflter.inflate(R.layout.layout_yachts_item_new_sec, null);


           YachtsViewHolder holder;

            if (view == null) {
                LayoutCustmOpencloseBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_openclose,viewGroup,false);

                holder = new YachtsViewHolder(binding);
                holder.view = binding.getRoot();
                holder.view.setTag(holder);
            }
            else {
                holder = (YachtsViewHolder) view.getTag();
            }


            holder.binding.nametv.setText(ratingBeanLists.get(position));



            return holder.view;



        }

        private  class YachtsViewHolder {
            private View view;
            private LayoutCustmOpencloseBinding binding;

            YachtsViewHolder(LayoutCustmOpencloseBinding binding) {
                this.view = binding.getRoot();
                this.binding = binding;
            }
        }
    }

    private void getPhoto() {

        ApiCall.get().Create().getPhoto(place_id,id,language).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
System.out.println("sssssssssssssssssssssss "+response);

                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyProfileDetail >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
if(object.getString("result").contains("p_photo")) {
    JSONArray jsonObject1 = object.getJSONArray("result");
    if(jsonObject1.length()>0){
                                  //  bannerlist = new ArrayList<>();
                           // bannerlist.clear();

                            for (int n = 0; n < jsonObject1.length(); n++) {
                                Bannerdata b = new Bannerdata();
                                b.setBannername(jsonObject1.getJSONObject(n).getString("p_photo"));
                                bannerlist.add(b);
                            }
                            if (bannerlist.size() > 0) {
                                binding.pagerlay.setVisibility(View.VISIBLE);
                                dotscount = 0;
                                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(PlacesDetailsActivity.this, bannerlist);
                                binding.viewPager.setAdapter(viewPagerAdapter);
                                viewPagerAdapter.notifyDataSetChanged();
                                dotscount = bannerlist.size();
                                dots = new ImageView[]{};
                                dots = new ImageView[dotscount];

                                for (int i = 0; i < bannerlist.size(); i++) {

                                    dots[i] = new ImageView(PlacesDetailsActivity.this);
                                    dots[i].setImageDrawable(ContextCompat.getDrawable(PlacesDetailsActivity.this, R.drawable.non_active_dot));
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(8, 0, 8, 0);
                                    binding.sliderDots.addView(dots[i], params);

                                    dots[0].setImageDrawable(ContextCompat.getDrawable(PlacesDetailsActivity.this, R.drawable.active));

                                    binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                        @Override
                                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//if(position== dots.length-1){
//    viewPager.setCurrentItem(0);
//}
                                        }

                                        @Override
                                        public void onPageSelected(int position) {

                                            for (int i = 0; i < dotscount; i++) {
                                                try {
                                                    dots[i].setImageDrawable(ContextCompat.getDrawable(PlacesDetailsActivity.this, R.drawable.non_active_dot));
                                                } catch (Exception e) {
//                                e.printStackTrace();
                                                }
                                            }
                                            try {
                                                dots[position].setImageDrawable(ContextCompat.getDrawable(PlacesDetailsActivity.this, R.drawable.active));
                                            } catch (Exception e) {
                                            }
                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int state) {

                                        }
                                    });
                                }


                            }

                         else {
                            binding.pagerlay.setVisibility(View.GONE);
                        }
    }
}


                        }
                        else {

                            binding.pagerlay.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        binding.pagerlay.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.pagerlay.setVisibility(View.GONE);
            }
        });
    }


    private void getLatLong() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(MainActivity.this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // location_ar = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        oldlocation = location;
        //speedvalue =	""+getSpeed(location,oldlocation);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    private class MyLocationListener implements LocationListener {
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
//    private void favorite() {
//        ApiCall.get().Create().addTofavorite(
//                session.getUserID(),
//                id
//        ).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        String responseData = response.body().string();
//                        JSONObject object = new JSONObject(responseData);
//                        if (object.getString("status").equalsIgnoreCase("1")) {
////                            placeDetail.setCurrentUserFavorite(newFavoriteStatus);
//                            boolean newFavoriteStatus = false;
//                            if (object.get("result") == "Removed From Favorites") {
//                                newFavoriteStatus = false;
//
//                            }else {
//                                newFavoriteStatus = true;
//                            }
//                            toggleHeartIcon();
//
//                            Toast.makeText(PlacesDetailsActivity.this,
//                                    newFavoriteStatus ? "Added to favorites" : "Removed from favorites",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//
//                            Toast.makeText(PlacesDetailsActivity.this,
//                                    object.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//                }
//            }

    private void favorite() {
        ApiCall.get().Create().addTofavorite(
                session.getUserID(),
                id
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equalsIgnoreCase("1")) {
                            if(object.getString("result").equalsIgnoreCase("Removed From Favorites")) {
                                placeDetail.setCurrentUserFavorite(false);
                                toggleHeartIcon(false);
//                                Toast.makeText(PlacesDetailsActivity.this,
//                                        object.getString("result"), Toast.LENGTH_SHORT).show();
                            } else {
                                placeDetail.setCurrentUserFavorite(true);
                                toggleHeartIcon(true);
//                                Toast.makeText(PlacesDetailsActivity.this,
//                                        object.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PlacesDetailsActivity.this,
                                    object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        placeDetail.setCurrentUserFavorite(currentUserFavourite);
//                        toggleHeartIcon(currentUserFavourite);
                    }
                } else {
//                    placeDetail.setCurrentUserFavorite(currentUserFavourite);
//                    toggleHeartIcon(currentUserFavourite);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                placeDetail.setCurrentUserFavorite(currentUserFavourite);
                toggleHeartIcon(currentUserFavourite);
                Toast.makeText(PlacesDetailsActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void toggleHeartIcon(boolean isCurrentUserFavorite) {
        this.isCurrentUserFavorite = isCurrentUserFavorite;
        if (isCurrentUserFavorite) {
            binding.heartIcon.setImageResource(R.drawable.ic_fav);
        } else {
            binding.heartIcon.setImageResource(R.drawable.ic_favorite);
        }
    }

}
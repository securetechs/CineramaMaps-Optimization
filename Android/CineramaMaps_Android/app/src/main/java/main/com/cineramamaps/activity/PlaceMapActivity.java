package main.com.cineramamaps.activity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityPlaceMapBinding;
import main.com.cineramamaps.databinding.ActivityPlacesDetailsBinding;
import main.com.cineramamaps.draweractivity.BaseActivity;
import main.com.cineramamaps.model.SinglePlaceBean;

public class PlaceMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
        , ResultCallback<LocationSettingsResult> {
    String mark_img = "";
    private LatLng latlong = null;
    private GoogleMap gmap;
    String lat="",lon="",icon="",placename="";
    public static double longitude = 0.0, latitude = 0.0;
    List<Marker> markerList = new ArrayList<Marker>();
    private MarkerOptions options = new MarkerOptions();
    Marker googlemarker_pos;
    ActivityPlaceMapBinding binding;
    SessionManager session;
    public static String fav="";
    String likestatus="";
    int item_count_val = 0;
    private int dotscount;
    private ImageView[] dots;
    float finalAmount;
    String id, itemprice_main = "", like_status = "", itemprice_total, cat_id = "", cat_name = "", extraitem_total_according_quantity_str = "", extra_item_name_str = "", extra_item_qty_str = "", extra_item_id = "", extra_item_price_str = "";
    int remainingitemquantity=0;


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
        if(!fav.equalsIgnoreCase("")){
            fav = "";

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_map);
        session = SessionManager.get(PlaceMapActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            id = bundle.getString("id");
            lat = bundle.getString("lat");
            lon = bundle.getString("lon");
            icon = bundle.getString("icon");
            placename = bundle.getString("name");
        }

        bindview();
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        clickevent();




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
        try {

            markerList = new ArrayList<>();

            markerList.clear();

            markerList = new ArrayList<>();

            if (gmap == null) {

            } else {
                gmap.clear();

                if (markerList != null) {
                    for (int i = 0; i < markerList.size(); i++) {
                        markerList.get(i).remove();
                    }

                }

            }

            //   for (int k = 0; k < successData.getResult().getPlaceDetails().size(); k++) {
            LatLng latLng1;
            if (lat == null || lon.equalsIgnoreCase("")) {
                latLng1 = new LatLng(Double.parseDouble("0.0"), Double.parseDouble("0.0"));
            } else {
                latLng1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            }
            //   if(friendsBeanLstArrayList.get(k).getBusinnessImages()!=null && friendsBeanLstArrayList.get(k).getBusinnessImages().size()>0) {
            addCustomMarker(latLng1.latitude, latLng1.longitude, icon, placename, 0);
            //  }


            //   }
            LatLng latLng2 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            MarkerOptions marker2 = new MarkerOptions().position(latLng2).flat(true).anchor(0.5f, 0.5f);
            // MarkerOptions marker2 = new MarkerOptions().position(new LatLng(40.04, -75.487)).flat(true).anchor(0.5f, 0.5f);
            CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(latLng2, 10);


            //  gmap.addMarker(marker2);
            //  marker = gmap.addMarker(marker2);
            //  marker.setTag("0");
//
            // gmap.animateCamera( )
            gmap.animateCamera(cameraUpdate1);


        } catch (Exception e) {

            e.printStackTrace();
        }

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
    private Bitmap mapMarker() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View markerLayout = inflater.inflate(R.layout.activity_map_marker, null);
        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(),
                markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    private void bindview() {
       // binding.extraitemslist.setExpanded(true);


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
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
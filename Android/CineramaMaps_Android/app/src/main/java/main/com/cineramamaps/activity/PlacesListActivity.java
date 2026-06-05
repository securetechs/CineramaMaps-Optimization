package main.com.cineramamaps.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PictureDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import main.com.cineramamaps.Entity.Room.PlacesEntity;
import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.ImageCacheUtils;
import main.com.cineramamaps.Utils.JsonCacheUtils;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.TagsAdapter;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.database.AppDatabase;
import main.com.cineramamaps.databinding.ActivityPlacesListBinding;
import main.com.cineramamaps.databinding.PlacelistitemBinding;
import main.com.cineramamaps.databinding.TaglistitemBinding;
import main.com.cineramamaps.enums.TagType;
import main.com.cineramamaps.model.PlaceDetail;
import main.com.cineramamaps.model.PlaceItem;
import main.com.cineramamaps.model.PlaceRenderer;
import main.com.cineramamaps.model.PlacesBean;
import main.com.cineramamaps.model.PlacesBeanList;
import main.com.cineramamaps.model.PlacesImage;
import main.com.cineramamaps.model.Tag;
import main.com.cineramamaps.model.TagDetail;
import main.com.cineramamaps.model.TripBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesListActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, ResultCallback<LocationSettingsResult> {
    String cityid = "", placeid = "", likestatus = "", listshow = "No";
    String placeName = "";
    private SessionManager session;
    private LatLng latlong = null;
    String citylat = "", citylon = "";
    public ActivityPlacesListBinding binding;
    private ArrayList<TripBeanList> tripBeanListArrayList;
    ArrayList<PlaceDetail> listt = new ArrayList<>();
    NotificationAdapter adapter1;
    private String language = "", address = "", id = "";
    private String description = "";
    private String totalunfavplace = "";
    private String totalfavplace = "";
    private String distance = "";
    private String placeNameAr = "";
    private String rating = "";
    private String icon = "";
    private String suggestedTime = "";
    private String advice = "";
    private String adviceAr = "";
    private String videoLinkEn = "";
    private String videoLinkAr = "";
    private String discountPercentage = "";
    private String descriptionAr = "";
    private String favstatus = "";
    private List<TagDetail> tagDetails = null;
    private List<PlacesImage> imageDetail = null;
    private String tagname = "";
    private String tagnameAr = "";
    private String promoCodeAndDiscount = "";
    MyLanguageSession myLanguageSession;
    String mark_img = "";
    private GoogleMap gmap;
    public static double longitude = 0.0, latitude = 0.0;
    List<MarkerOptions> markerListOptione = new ArrayList<MarkerOptions>();
    private MarkerOptions options = new MarkerOptions();
    Marker googlemarker_pos;

    GPSTracker tracker;
    String lat = "0.0", lon = "0.0";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 3000;
    private static final float LOCATION_DISTANCE = 10f;
    static String latitudes = "", longitudes = "";
    GPSTracker gpsTracker;
    private static final String TAG = "MyLocationService";
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    Location location;
    Location oldlocation;
    private double oldlongitude = 0.0, oldlatitude = 0.0;
    Location location_ar;
    LocationManager locationManager;
    private static final float MIN_ZOOM_LEVEL = 10.0f;
    boolean isFav = PlacesBeanList.isCurrentUserFavorite();
    TagType tag = TagType.alltags;
    String tagColors = "";
    List<PlaceDetail> placeData;
    List<Tag> tagData;
    List<PlacesImage> imageData;
    boolean isMapUpdate = false;
    double currentZoomPosition = 0;
    private float lastProcessedZoom = -1;
    private Handler cameraIdleHandler = new Handler(Looper.getMainLooper());
    private Runnable cameraIdleRunnable;
    private static final long CAMERA_IDLE_DELAY_MS = 500; // 0.5 second debounce
    private ClusterManager<PlaceItem> clusterManager;
    private PlaceRenderer placeRenderer;
    private static final float ZOOM_THRESHOLD = 14.0F;
    float zoomLevel = 8.0F;

    // Performance monitoring
    private int markersLoaded = 0;
    private long mapLoadStartTime = 0;
    private static final int MAX_MARKERS_PER_BATCH = 20;
    private Handler markerBatchHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String placeTitle = data.getStringExtra("placeTitle");
            boolean isFav = data.getBooleanExtra("currentUserFavourite", false);

            for (PlaceDetail place : listt) {
                if (place.getPlaceName().equals(placeTitle)) {
                    System.out.println("isFav = " + isFav);
                    place.setCurrentUserFavorite(isFav);
                    break;
                }
            }
            if (tag == TagType.favouritetags) {
                favouriteTags();
            }
        }
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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        if (markerExecutor != null && !markerExecutor.isShutdown()) {
            markerExecutor.shutdownNow();
        }
        if (bitmapExecutor != null && !bitmapExecutor.isShutdown()) {
            bitmapExecutor.shutdownNow();
        }
        if (gmap != null) {
            gmap.clear();
        }
        super.onDestroy();
    }
    private boolean isLongPressActive = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_places_list);
        cityid = getIntent().getExtras().getString("city_id");
        ListViewContentWrapper.setRestaurantContent(binding.composeView);
        ListViewContentWrapper.setOnFavoriteClick(new OnFavoriteClick() {
            @Override
            public void onClick(String placeId, BooleanCallback toggleHeartIcon) {
                favorite(placeId, (isFavorite, returnedPlaceId) -> {
                    toggleHeartIcon.invoke(isFavorite);
                });
            }
        });
        BindView();
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        getCountryMaps();
        clickevent();
        tracker = new GPSTracker(this);

        getLatLong();
        getCurrentLocation();
        initGps();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PlacesListActivity.this, 3);
        binding.providerrecyclerview.setLayoutManager(mLayoutManager);
        gpsTracker = new GPSTracker(PlacesListActivity.this);

        // check if GPS enabled
        if (gpsTracker.canGetLocation()) {
            latitudes = String.valueOf(gpsTracker.getLatitude());
            longitudes = String.valueOf(gpsTracker.getLongitude());
            lat = "" + latitudes;
            lon = "" + longitudes;
        }
        initializeLocationManager();

        adapter1 = new NotificationAdapter(PlacesListActivity.this, null, session.IsEnglish());
        binding.notificaitonlist.setAdapter(adapter1);
        binding.tagbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gravity = GravityCompat.START;
                binding.drawerLayout.openDrawer(gravity);
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gravity = GravityCompat.START;
                binding.drawerLayout.closeDrawer(gravity);
            }
        });

        binding.cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.taglay.setVisibility(View.GONE);
            }
        });

        binding.tagsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);

                new Thread(() -> {
                    try {
                        markerListOptione.clear();
                        runOnUiThread(() -> clusterManager.clearItems());

                        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                        final boolean[] hasMarkers = {false}; // Wrapper to allow mutation inside lambda

                        for (int i = 0; i < listt.size(); i++) {
                            PlaceDetail place = listt.get(i);
                            try {
                                double latitude = Double.parseDouble(place.getLat());
                                double longitude = Double.parseDouble(place.getLon());
                                String iconUrl = place.getIcon();
                                String name = place.getPlaceName();
                                int finalI = i;

                                // Add marker on UI thread
                                runOnUiThread(() -> addCustomMarker(latitude, longitude, iconUrl, name, finalI, place));

                                // Include marker in bounds for auto zoom
                                boundsBuilder.include(new LatLng(latitude, longitude));
                                hasMarkers[0] = true;

                            } catch (Exception e) {
                                Log.e("allTags", "Error adding marker for place: " + place.getPlaceName(), e);
                            }
                        }

                        runOnUiThread(() -> {
                            try {
                                // Cluster remaining markers
                                clusterManager.cluster();

                                // Auto-zoom to include all markers
                                if (hasMarkers[0]) {
                                    LatLngBounds bounds = boundsBuilder.build();
                                    gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150)); // 150 = padding
                                }

                                binding.drawerLayout.closeDrawer(GravityCompat.START);

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                binding.progressBar.setVisibility(View.GONE);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));
                    }
                }).start();
            }
        });

        binding.favLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouriteTags();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    @SuppressLint({"PotentialBehaviorOverride", "ClickableViewAccessibility"})
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        // Add this instead of the previous style in onMapReady
        try {
            String minimalistStyle = "[" +
                    "  {" +
                    "    \"featureType\": \"poi\"," +
                    "    \"elementType\": \"all\"," +
                    "    \"stylers\": [{\"visibility\": \"off\"}]" +
                    "  }," +
                    "  {" +
                    "    \"featureType\": \"transit\"," +
                    "    \"elementType\": \"all\"," +
                    "    \"stylers\": [{\"visibility\": \"off\"}]" +
                    "  }," +
                    "  {" +
                    "    \"featureType\": \"road\"," +
                    "    \"elementType\": \"labels\"," +
                    "    \"stylers\": [{\"visibility\": \"simplified\"}]" +
                    "  }," +
                    "  {" +
                    "    \"featureType\": \"administrative.locality\"," +
                    "    \"elementType\": \"labels\"," +
                    "    \"stylers\": [{\"visibility\": \"off\"}]" +
                    "  }," +
                    "  {" +
                    "    \"featureType\": \"administrative.neighborhood\"," +
                    "    \"elementType\": \"labels\"," +
                    "    \"stylers\": [{\"visibility\": \"off\"}]" +
                    "  }" +
                    "]";

            gmap.setMapStyle(new MapStyleOptions(minimalistStyle));
        } catch (Exception e) {
            Log.e("MapStyle", "Error applying minimalist style", e);
        }
        mapLoadStartTime = System.currentTimeMillis();

        // === CLUSTER MANAGER SETUP ===
        clusterManager = new ClusterManager<>(this, gmap);
        NonHierarchicalDistanceBasedAlgorithm<PlaceItem> algorithm = new NonHierarchicalDistanceBasedAlgorithm<>();
        algorithm.setMaxDistanceBetweenClusteredItems(50);
        clusterManager.setAlgorithm(algorithm);
        placeRenderer = new PlaceRenderer(this, gmap, clusterManager);
        clusterManager.setRenderer(placeRenderer);

        // === PERMISSIONS CHECK ===
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // === MAP SETTINGS ===
        gmap.setMyLocationEnabled(true);
        gmap.getUiSettings().setMyLocationButtonEnabled(false);
        gmap.getUiSettings().setMapToolbarEnabled(false);
        gmap.getUiSettings().setZoomControlsEnabled(false);

        // === CUSTOM MY LOCATION BUTTON ===
        RelativeLayout myLocationButton = findViewById(R.id.btn_my_location);
        myLocationButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location myLocation = gmap.getMyLocation();
            if (myLocation != null) {
                LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
            }
        });

        // === CUSTOMIZE DEFAULT LOCATION BUTTON ===
        View mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getView();
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent())
                    .findViewById(Integer.parseInt("2"));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params.setMargins(0, 0, 30, 30);
            locationButton.setLayoutParams(params);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(Color.parseColor("#FAFAFA"));
            shape.setStroke(2, Color.WHITE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                locationButton.setBackground(shape);
            } else {
                locationButton.setBackgroundDrawable(shape);
            }
            int padding = 12;
            locationButton.setPadding(padding, padding, padding, padding);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                locationButton.setElevation(8f);
            }
        }

        // === CLUSTER ITEM CLICK HANDLERS ===
        clusterManager.setOnClusterItemClickListener(item -> clickOnMapIcon(item));
        clusterManager.setOnClusterClickListener(cluster -> {
            PlaceItem firstItem = cluster.getItems().iterator().next();
            return clickOnMapIcon(firstItem);
        });

        // === LONG PRESS HANDLER ===
        View overlay = findViewById(R.id.mapOverlay);
        TextView label = findViewById(R.id.longPressLabel);
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                LatLng touchLatLng = gmap.getProjection().fromScreenLocation(new Point((int) e.getX(), (int) e.getY()));
                String nameToShow = "";
                double minDistance = Double.MAX_VALUE;

                for (PlaceDetail place : listt) {
                    LatLng markerLatLng = new LatLng(Double.parseDouble(place.getLat()), Double.parseDouble(place.getLon()));
                    Point markerPoint = gmap.getProjection().toScreenLocation(markerLatLng);
                    double dx = e.getX() - markerPoint.x;
                    double dy = e.getY() - markerPoint.y;
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    if (distance < minDistance) {
                        minDistance = distance;
                        String lang = Locale.getDefault().getLanguage();
                        nameToShow = lang.equals("ar") ? place.getPlaceNameAr() : place.getPlaceName();
                    }
                }

                if (minDistance < 50) {
                    label.setText(!nameToShow.isEmpty() ? nameToShow : "Unknown place");
                    label.setVisibility(View.VISIBLE);
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (v != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            v.vibrate(60);
                        }
                    }
                    isLongPressActive = true;
                }
            }
        });

        overlay.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (isLongPressActive) {
                    label.setVisibility(View.GONE);
                    isLongPressActive = false;
                }
            }
            if (mapView != null) {
                mapView.dispatchTouchEvent(event);
            }
            return true;
        });

        // === CAMERA IDLE LISTENER FOR CLUSTERING ===
        gmap.setOnCameraIdleListener(() -> {
            clusterManager.onCameraIdle();

            // Swap marker icons based on zoom level
            float currentZoom = gmap.getCameraPosition().zoom;
            boolean shouldBeZoomed = currentZoom >= ZOOM_THRESHOLD;
            if (placeRenderer != null) {
                placeRenderer.updateZoomState(shouldBeZoomed);
            }
        });

        // === MAP LOADED CALLBACK ===
        gmap.setOnMapLoadedCallback(() -> {
            Log.d("Performance", "Map loaded in: " + (System.currentTimeMillis() - mapLoadStartTime) + "ms");

            // Add all markers using your addCustomMarker() function
            addAllMarkersAndCluster();

            // Automatically adjust camera to fit all markers
            moveCameraToFitMarkers(listt);
        });

        // === MY LOCATION TRACKING ===
        gmap.setOnMyLocationChangeListener(location -> {
            LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (googlemarker_pos != null) {
                googlemarker_pos.setPosition(latLng1);
            }
        });
    }

    // === HELPER FUNCTION TO MOVE CAMERA TO FIT MARKERS ===
    private void moveCameraToFitMarkers(List<PlaceDetail> places) {
        if (places.isEmpty() || gmap == null) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (PlaceDetail place : places) {
            try {
                double lat = Double.parseDouble(place.getLat());
                double lon = Double.parseDouble(place.getLon());
                boundsBuilder.include(new LatLng(lat, lon));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LatLngBounds bounds = boundsBuilder.build();
        gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150)); // 150 padding
    }

    // === HELPER FUNCTION TO ADD ALL MARKERS AND CLUSTER ===
    private void addAllMarkersAndCluster() {
        if (listt == null || listt.isEmpty() || gmap == null) return;

        for (int i = 0; i < listt.size(); i++) {
            PlaceDetail place = listt.get(i);
            try {
                double latitude = Double.parseDouble(place.getLat());
                double longitude = Double.parseDouble(place.getLon());
                addCustomMarker(latitude, longitude, place.getIcon(), place.getPlaceName(), i, place);
            } catch (Exception e) {
                Log.e("MapMarkers", "Error adding marker for: " + place.getPlaceName(), e);
            }
        }
        clusterManager.cluster();
    }

    // Optimized executors with smaller thread pools
    private final ExecutorService markerExecutor = Executors.newFixedThreadPool(2);
    private final ExecutorService bitmapExecutor = Executors.newFixedThreadPool(2);

    private void addCustomMarker(double latitude, double longitude, String image, String name, int k, PlaceDetail placeDetail) {
        String iconUrl = placeDetail.getIcon();
        LatLng latLng = new LatLng(latitude, longitude);

        if (gmap == null || iconUrl == null) return;

        double zoomed = 18;
        double unZoomed = 12;

        // Check rendered marker bitmap cache first (fastest path)
        String markerCacheKey = iconUrl + "_" + placeDetail.getPlaceName();
        Bitmap[] cachedMarkerBitmaps = ImageCacheUtils.getMarkerBitmaps(markerCacheKey);
        if (cachedMarkerBitmaps != null) {
            addMarkerToMap(latLng, name, k, cachedMarkerBitmaps[0], cachedMarkerBitmaps[1], placeDetail);
            return;
        }

        PictureDrawable memSvg = ImageCacheUtils.getSvgFromMemory(iconUrl);
        Bitmap memBitmap = ImageCacheUtils.getBitmapFromMemory(iconUrl);

        if (memSvg != null || memBitmap != null) {
            // Create bitmaps in background thread
            createMarkerBitmapAsync(placeDetail, memSvg, memBitmap, memSvg != null, zoomed, unZoomed,
                    (zoomedBitmap, normalBitmap) -> {
                        ImageCacheUtils.putMarkerBitmaps(markerCacheKey, zoomedBitmap, normalBitmap);
                        addMarkerToMap(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
                    });
            return;
        }

        // 2️⃣ Check disk cache next
        File cachedFile = ImageCacheUtils.getCachedFile(this, iconUrl);
        if (cachedFile.exists()) {
            markerExecutor.execute(() -> {
                try {
                    if (iconUrl.endsWith(".svg")) {
                        SVG svg = SVG.getFromInputStream(new FileInputStream(cachedFile));
                        PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());
                        ImageCacheUtils.putSvgToMemory(iconUrl, drawable);

                        createMarkerBitmapAsync(placeDetail, drawable, null, true, zoomed, unZoomed,
                                (zoomedBitmap, normalBitmap) -> {
                                    ImageCacheUtils.putMarkerBitmaps(markerCacheKey, zoomedBitmap, normalBitmap);
                                    addMarkerToMap(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
                                });
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(cachedFile.getAbsolutePath());
                        ImageCacheUtils.putBitmapToMemory(iconUrl, bitmap);

                        createMarkerBitmapAsync(placeDetail, null, bitmap, false, zoomed, unZoomed,
                                (zoomedBitmap, normalBitmap) -> {
                                    ImageCacheUtils.putMarkerBitmaps(markerCacheKey, zoomedBitmap, normalBitmap);
                                    addMarkerToMap(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
                                });
                    }
                } catch (Exception e) {
                    Log.e("MarkerCache", "Error loading cached marker", e);
                }
            });
            return;
        }

        // 3️⃣ Not cached → load via Glide (async)
        if (iconUrl.endsWith(".svg")) {
            if (!PlacesListActivity.this.isFinishing()
                    && !PlacesListActivity.this.isDestroyed()) {
                Glide.with(this)
                        .as(PictureDrawable.class)
                        .load(iconUrl)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(new CustomTarget<PictureDrawable>() {
                            @Override
                            public void onResourceReady(@NonNull PictureDrawable resource, @Nullable Transition<? super PictureDrawable> transition) {
                                ImageCacheUtils.putSvgToMemory(iconUrl, resource);
                                markerExecutor.execute(() -> {
                                    try {
                                        // Save SVG raw bytes async without blocking
                                        Glide.with(getApplicationContext())
                                                .as(byte[].class)
                                                .load(iconUrl)
                                                .into(new CustomTarget<byte[]>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull byte[] data, @Nullable Transition<? super byte[]> transition) {
                                                        ImageCacheUtils.saveBytesToCache(cachedFile, data);
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    }
                                                });
                                    } catch (Exception e) {
                                        Log.e("MarkerLoad", "Error saving SVG cache", e);
                                    }

                                    createMarkerBitmapAsync(placeDetail, resource, null, true, zoomed, unZoomed,
                                            (zoomedBitmap, normalBitmap) -> {
                                                ImageCacheUtils.putMarkerBitmaps(markerCacheKey, zoomedBitmap, normalBitmap);
                                                addMarkerToMap(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
                                            });
                                });
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
        } else {
            if (!PlacesListActivity.this.isFinishing()
                    && !PlacesListActivity.this.isDestroyed()) {
                Glide.with(this)
                        .asBitmap()
                        .load(iconUrl)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                ImageCacheUtils.putBitmapToMemory(iconUrl, bitmap);
                                markerExecutor.execute(() -> {
                                    ImageCacheUtils.saveBitmapToCache(cachedFile, bitmap);
                                    createMarkerBitmapAsync(placeDetail, null, bitmap, false, zoomed, unZoomed,
                                            (zoomedBitmap, normalBitmap) -> {
                                                ImageCacheUtils.putMarkerBitmaps(markerCacheKey, zoomedBitmap, normalBitmap);
                                                addMarkerToMap(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
                                            });
                                });
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
        }
    }

    // Async bitmap creation interface
    interface BitmapPairCallback {
        void onBitmapsReady(Bitmap zoomedBitmap, Bitmap normalBitmap);
    }

    // Move heavy bitmap operations to background thread
    private void createMarkerBitmapAsync(PlaceDetail placeDetail, PictureDrawable drawable, Bitmap bitmap,
                                         boolean isSVG, double zoomed, double unZoomed, BitmapPairCallback callback) {
        bitmapExecutor.execute(() -> {
            long startTime = System.currentTimeMillis();

            Bitmap zoomedBitmap = mapMarker(placeDetail, drawable, bitmap, isSVG, zoomed);
            Bitmap normalBitmap = mapMarker(placeDetail, drawable, bitmap, isSVG, unZoomed);

            long duration = System.currentTimeMillis() - startTime;
            if (duration > 50) {
                Log.w("Performance", "Slow bitmap creation: " + duration + "ms");
            }

            runOnUiThread(() -> callback.onBitmapsReady(zoomedBitmap, normalBitmap));
        });
    }

    private void addMarkerToMap(LatLng latLng, String name, int k, Bitmap zoomedBitmap, Bitmap normalBitmap, PlaceDetail placeDetail) {
        markersLoaded++;

        // Batch marker additions to prevent UI freezing
        if (markersLoaded % MAX_MARKERS_PER_BATCH == 0) {
            // Allow UI to breathe between batches
            markerBatchHandler.postDelayed(() -> {
                addSingleMarkerToMap(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
            }, 10);
        } else {
            addSingleMarkerToMap(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
        }
    }

    private void addSingleMarkerToMap(LatLng latLng, String name, int k, Bitmap zoomedBitmap, Bitmap normalBitmap, PlaceDetail placeDetail) {
        MarkerOptions option = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(zoomedBitmap))
                .visible(true);
        option.title(name + "||" + k);
        markerListOptione.add(option);

        // Create the cluster item
        PlaceItem item = new PlaceItem(option.getPosition(), name, Integer.toString(k),
                option.getIcon(), zoomedBitmap, normalBitmap);

        clusterManager.addItem(item);

        // Update progress for large datasets
        if (markersLoaded % 10 == 0 && markersLoaded > 0) {
            Log.d("Progress", "Markers loaded: " + markersLoaded + "/" + listt.size());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private Bitmap mapMarker(PlaceDetail placeDetail, PictureDrawable imageView, Bitmap image, Boolean isSVG, double zoom) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View markerLayout = inflater.inflate(R.layout.activity_map_marker, null);
        ImageView outerFrame = markerLayout.findViewById(R.id.outerFrame);
        ImageView innerLayer = markerLayout.findViewById(R.id.pieCircle);
        ImageView innerFrame = markerLayout.findViewById(R.id.innerFrame);
        ImageView pinIcon = markerLayout.findViewById(R.id.pinIcon);
        ImageView ppp = markerLayout.findViewById(R.id.ppp);
        OutlinedTextView outlinedText = markerLayout.findViewById(R.id.placeTitle);

        String name = "en".equals(myLanguageSession.getLanguage()) ? placeDetail.getPlaceName() : placeDetail.getPlaceNameAr();
        outlinedText.setText(name);
        if (zoom > 16) {
            outlinedText.setAlpha(1);
        } else {
            outlinedText.setAlpha(0);
        }

        if (placeDetail.getShowOnlyIcon().equals("1")) {
            if (placeDetail.getTagDetails() != null) {
                List<String> colorList = placeDetail.getTagDetails().stream().filter(Objects::nonNull)
                        .map(TagDetail::getColorCode).filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (!colorList.isEmpty()) {
                    String lastColor = colorList.stream().findFirst().get();
                    ColorStateList tint = ColorStateList.valueOf(Color.parseColor(lastColor));
                    innerFrame.setImageTintList(tint);
                    innerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                }
                applyPieChart(innerLayer, colorList);
            }
            if (!placeDetail.getPromoCodeAndDiscount().isEmpty()) {
                if (placeDetail.getEndDate() != null && !placeDetail.getEndDate().isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateFormat.setTimeZone(TimeZone.getDefault());

                    try {
                        Date endDate = dateFormat.parse(placeDetail.getEndDate());
                        Date currentDate = new Date();

                        if (endDate != null && !endDate.before(currentDate)) {
                            ColorStateList tint = ColorStateList.valueOf(Color.parseColor("#F1D280"));
                            outerFrame.setImageTintList(tint);
                            outerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                        } else {
                            ColorStateList tint = ColorStateList.valueOf(Color.WHITE);
                            outerFrame.setImageTintList(tint);
                            outerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                        }
                    } catch (ParseException e) {
                        Log.e("DateParse", "Error parsing date", e);
                        ColorStateList tint = ColorStateList.valueOf(Color.WHITE);
                        outerFrame.setImageTintList(tint);
                        outerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                    }
                } else {
                    ColorStateList tint = ColorStateList.valueOf(Color.WHITE);
                    outerFrame.setImageTintList(tint);
                    outerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                }
            } else {
                ColorStateList tint = ColorStateList.valueOf(Color.WHITE);
                outerFrame.setImageTintList(tint);
                outerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            if (isSVG) {
                pinIcon.setImageDrawable(imageView);
            } else {
                pinIcon.setImageBitmap(image);
            }
        } else {
            innerLayer.setVisibility(View.GONE);
            innerFrame.setVisibility(View.GONE);
            pinIcon.setVisibility(View.GONE);
            ppp.setVisibility(View.GONE);
            if (isSVG) {
                outerFrame.setImageDrawable(imageView);
            } else {
                outerFrame.setImageBitmap(image);
            }
        }

        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }

    public void applyPieChart(ImageView innerLayer, List<String> colorList) {
        Bitmap pieChartBitmap = createPieChartBitmap(innerLayer, colorList);
        BitmapDrawable pieChartDrawable = new BitmapDrawable(innerLayer.getResources(), pieChartBitmap);
        innerLayer.setImageDrawable(pieChartDrawable);
    }

    private Bitmap createPieChartBitmap(ImageView innerLayer, List<String> colorList) {
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
            startAngle += 360f / colorList.size();
        }
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

    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listshow = "No";
                binding.mapbtn.setBackgroundResource(R.drawable.roundbtn1);
                binding.listbtn.setBackgroundResource(R.drawable.roundbtn2);
                binding.maptxt.setTextColor(getResources().getColor(R.color.white));
                binding.listtxt.setTextColor(getResources().getColor(R.color.black));
                binding.maplay.setVisibility(View.VISIBLE);
                binding.notificaitonlist.setVisibility(View.GONE);
                binding.composeView.setVisibility(View.GONE);
                binding.emptylay.setVisibility(View.GONE);
                favouriteTags();
            }
        });

        binding.searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlacesListActivity.this, DetailListActivity.class);
                intent.putExtra("CITY_ID", cityid);
                startActivity(intent);
            }
        });

        binding.listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listshow = "Yes";
                binding.mapbtn.setBackgroundResource(R.drawable.roundbtn2);
                binding.listbtn.setBackgroundResource(R.drawable.roundbtn1);
                binding.maptxt.setTextColor(getResources().getColor(R.color.black));
                binding.listtxt.setTextColor(getResources().getColor(R.color.white));
                binding.maplay.setVisibility(View.GONE);
                binding.composeView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void BindView() {
        session = SessionManager.get(PlacesListActivity.this);
        listt = new ArrayList<>();
        listt.clear();
    }

    private boolean mapDataLoaded = false;
    private String currentCityId = "";

    private void getCountryMaps() {
        binding.progressbar.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            String cityId = getProductParam("Item").get("city_id");
            currentCityId = cityId;

            PlacesEntity cached = AppDatabase.getInstance(this)
                    .placesDao()
                    .getPlacesByCity(cityId);

            runOnUiThread(() -> {

                // 1️⃣ LOAD CACHED DATA INSTANTLY (IF EXISTS)
                if (cached != null) {
                    File file = new File(cached.getFilePath());
                    if (file.exists()) {
                        try {
                            String jsonData = JsonCacheUtils.readJsonFromFile(cached.getFilePath());
                            loadDataFromJson(jsonData, true); // show immediately
                            binding.progressbar.setVisibility(View.GONE); // hide spinner after cache loads
                        } catch (Exception e) {
                            Log.e("Cache", "Cache read error", e);
                        }
                    }
                }

                // 2️⃣ ALWAYS FETCH FRESH DATA FROM API
                // (IF DIFFERENT → UI UPDATES AUTOMATICALLY)
                fetchFromApi();
            });
        });
    }

    private void fetchFromApi() {
        ApiCall.get().Create().getCityMapsDetails(getProductParam("Item"))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String responseData = response.body().string();
                                String cityId = getProductParam("Item").get("city_id");

                                // Save on background thread
                                Executors.newSingleThreadExecutor().execute(() -> {
                                    try {
                                        String filePath = JsonCacheUtils.saveJsonToFile(getApplicationContext(), cityId, responseData);

                                        PlacesEntity entity = new PlacesEntity();
                                        entity.setCityId(cityId);
                                        entity.setFilePath(filePath);
                                        entity.setTimestamp(System.currentTimeMillis());

                                        AppDatabase.getInstance(getApplicationContext())
                                                .placesDao()
                                                .insert(entity);

                                        Log.d("CacheSave", "Saved cache for " + cityId);
                                    } catch (Exception e) {
                                        Log.e("CacheSave", "Error saving cache", e);
                                    }
                                });

                                // 3️⃣ UPDATE UI IMMEDIATELY IF NEW DATA CAME
                                loadDataFromJson(responseData, false);

                            } catch (Exception e) {
                                Log.e("ApiCall", "Error processing response", e);
                            }
                        } else {
                            Log.e("ApiCall", "Response failed: " + response.code());
                        }

                        binding.progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        binding.progressbar.setVisibility(View.GONE);
                        Log.e("ApiCall", "Failed: " + t.getMessage());
                    }
                });
    }

    private void loadDataFromJson(String jsonData, boolean isFromCache) {
        try {
            PlacesBean successData = new Gson().fromJson(jsonData, PlacesBean.class);

            if (successData.getStatus().equalsIgnoreCase("1")) {

                binding.datalay.setVisibility(View.VISIBLE);
                binding.emptylay.setVisibility(View.GONE);

                if (language.equalsIgnoreCase("en")) {
                    binding.headtxt.setText(successData.getResult().getName());
                } else {
                    binding.headtxt.setText(successData.getResult().getNameAr());
                }

                placeData = successData.getResult().getPlaceDetails();
                ListViewContentWrapper.updatePlaces(this, placeData);

                tagData = successData.getResult().getTags();
                ListViewContentWrapper.updateTags(tagData);

                imageData = successData.getResult().getPlacesImages();
                ListViewContentWrapper.updateImage(imageData);

                citylat = "" + successData.getResult().getLat();
                citylon = "" + successData.getResult().getLon();

                listt.clear();
                listt.addAll(successData.getResult().getPlaceDetails());
                adapter1 = new NotificationAdapter(PlacesListActivity.this, listt, session.IsEnglish());
                binding.notificaitonlist.setAdapter(adapter1);

                adapter1.notifyDataSetChanged();

                // TAG VISIBILITY
                if (successData.getResult().getTags() != null &&
                        !successData.getResult().getTags().isEmpty()) {

                    binding.tagbtn.setVisibility(View.VISIBLE);
                    binding.tagtxt.setText(getString(R.string.tags) + "(" + tagData.size() + ")");
                    tagsRecyclerView(tagData);
                } else {
                    binding.tags.setVisibility(View.GONE);
                    binding.tagbtn.setVisibility(View.GONE);
                }

                // MAP — draw once only
                if (!mapDataLoaded && gmap != null) {
                    gmap.clear();
                    markersLoaded = 0;
                    loadMarkersInBatches(listt);
                    mapDataLoaded = true;
                    Log.d("MapUpdate", isFromCache ? "Loaded from cache" : "Loaded from API");
                }

            } else {

                if (listshow.equalsIgnoreCase("Yes")) {
                    binding.datalay.setVisibility(View.GONE);
                    binding.emptylay.setVisibility(View.VISIBLE);
                } else {
                    binding.datalay.setVisibility(View.VISIBLE);
                    binding.emptylay.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            Log.e("LoadData", "Error loading JSON", e);
        }
    }

    // Load markers in batches to prevent ANR
    private void loadMarkersInBatches(List<PlaceDetail> places) {
        final int batchSize = 10;
        final int totalBatches = (int) Math.ceil((double) places.size() / batchSize);

        Log.d("MarkerLoad", "Loading " + places.size() + " markers in " + totalBatches + " batches");

        for (int batch = 0; batch < totalBatches; batch++) {
            final int currentBatch = batch;
            markerBatchHandler.postDelayed(() -> {
                int start = currentBatch * batchSize;
                int end = Math.min(start + batchSize, places.size());

                for (int k = start; k < end; k++) {
                    PlaceDetail place = places.get(k);
                    LatLng latLng;
                    if (place.getLat() == null || place.getLon().equalsIgnoreCase("")) {
                        latLng = new LatLng(0.0, 0.0);
                    } else {
                        latLng = new LatLng(Double.parseDouble(place.getLat()), Double.parseDouble(place.getLon()));
                    }

                    if (!place.getIcon().equalsIgnoreCase("" + BaseUrl.image_baseurl)
                            && !place.getIcon().equalsIgnoreCase("")) {
                        addCustomMarker(latLng.latitude, latLng.longitude,
                                place.getIcon(), place.getPlaceName(), k, place);
                    } else {
                        addCustomMarker(latLng.latitude, latLng.longitude,
                                place.getMap_icon(), place.getPlaceName(), k, place);
                    }
                }

                // Update progress
                Log.d("MarkerLoad", "Batch " + (currentBatch + 1) + "/" + totalBatches + " completed");

            }, batch * 30L); // Stagger batches by 30ms
        }

        // Final camera update after all markers are loaded
        markerBatchHandler.postDelayed(() -> {
            if (!places.isEmpty()) {
                LatLng latLng = new LatLng(Double.parseDouble(places.get(places.size() - 1).getLat()),
                        Double.parseDouble(places.get(places.size() - 1).getLon()));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
                gmap.animateCamera(cameraUpdate);
            }
        }, totalBatches * 30L);
    }

    private HashMap<String, String> getProductParam(String type) {
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("city_id", "" + cityid);
        param.put("lang", "" + language);
        param.put("lat", "" + lat);
        param.put("lon", "" + lon);
        return param;
    }

    public void filterMarkersByColor(String colorCode) {
        if (colorCode == null || gmap == null || listt == null) {
            return;
        }
        gmap.clear();
        markersLoaded = 0;

        for (int i = 0; i < listt.size(); i++) {
            PlaceDetail place = listt.get(i);

            boolean hasMatchingTag = false;
            if (place.getTagDetails() != null) {
                for (TagDetail tagDetail : place.getTagDetails()) {
                    if (tagDetail.getColorCode() != null && tagDetail.getColorCode().trim().equalsIgnoreCase(colorCode.trim())) {
                        hasMatchingTag = true;
                        break;
                    }
                }
            }

            if (hasMatchingTag) {
                try {
                    LatLng latLng = new LatLng(Double.parseDouble(place.getLat()), Double.parseDouble(place.getLon()));

                    String iconUrl = place.getIcon();
                    if (iconUrl != null && !iconUrl.equalsIgnoreCase(BaseUrl.image_baseurl) && !iconUrl.isEmpty()) {
                        addCustomMarker(latLng.latitude, latLng.longitude, iconUrl, place.getPlaceName(), i, place);
                    } else {
                        addCustomMarker(latLng.latitude, latLng.longitude, place.getMap_icon(), place.getPlaceName(), i, place);
                    }
                } catch (Exception e) {
                    Log.e("FilterMarker", "Error filtering marker", e);
                }
            }
        }
    }

    // ... (Adapter classes remain the same as in your original code)
    public class AllRestaurnatAdapter1 extends RecyclerView.Adapter<AllRestaurnatAdapter1.ViewHolder> {
        Context context;
        List<Tag> mapBeanLists;

        public AllRestaurnatAdapter1(Context context, List<Tag> mapBeanLists) {
            this.context = context;
            this.mapBeanLists = mapBeanLists;
        }

        @NonNull
        @Override
        public AllRestaurnatAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TaglistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.taglistitem, parent, false);
            return new AllRestaurnatAdapter1.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AllRestaurnatAdapter1.ViewHolder holder, int position) {
            if (language.equalsIgnoreCase("en")) {
                holder.binding.tagname.setText(mapBeanLists.get(position).getTotalTagPlaceCount() + " " + mapBeanLists.get(position).getTagName());
            } else {
                holder.binding.tagname.setText(mapBeanLists.get(position).getTotalTagPlaceCount() + " " + mapBeanLists.get(position).getTagNameAr());
            }
            holder.binding.tagname.setBackgroundColor(Color.parseColor("" + mapBeanLists.get(position).getColorCode()));
        }

        @Override
        public int getItemCount() {
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

    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
        Context context;
        boolean b;
        List<PlaceDetail> listt;

        public NotificationAdapter(Context context, ArrayList<PlaceDetail> listt, boolean b) {
            this.context = context;
            this.listt = listt;
            this.b = b;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            PlacelistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.placelistitem, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.textlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    id = listt.get(position).getId();
                    address = listt.get(position).getAddress();
                    getPlaceId(id);
                }
            });

            holder.binding.maptypetv.setText(listt.get(position).getAddress());
            holder.binding.titletv.setText("" + listt.get(position).getPlaceName());
            holder.binding.distancetv.setText("" + getResources().getString(R.string.awayfromyou) + " " + listt.get(position).getDistance() + " " + getResources().getString(R.string.km));

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PlacesListActivity.this, 3);
            holder.binding.providerrecyclerview.setLayoutManager(mLayoutManager);
            AllRestaurnatAdapter adapter = new AllRestaurnatAdapter(PlacesListActivity.this, listt.get(position).getTagDetails());
            holder.binding.providerrecyclerview.setAdapter(adapter);

            holder.binding.likecounttv.setText(listt.get(position).getTotal_fav_place());
            holder.binding.dislikecounttv.setText(listt.get(position).getTotal_unfav_place());

            if (listt.get(position).getFav_status().equalsIgnoreCase("")) {
                holder.binding.likeimg.setBackgroundResource(R.drawable.like_unselect);
                holder.binding.dislikeimg.setBackgroundResource(R.drawable.dislike_unselect);
            }
            if (listt.get(position).getFav_status().equalsIgnoreCase("Like")) {
                holder.binding.likeimg.setBackgroundResource(R.drawable.like_select);
                holder.binding.dislikeimg.setBackgroundResource(R.drawable.dislike_unselect);
            } else if (listt.get(position).getFav_status().equalsIgnoreCase("Unlike")) {
                holder.binding.likeimg.setBackgroundResource(R.drawable.like_unselect);
                holder.binding.dislikeimg.setBackgroundResource(R.drawable.dislike_select);
            } else {
                holder.binding.likeimg.setBackgroundResource(R.drawable.like_unselect);
                holder.binding.dislikeimg.setBackgroundResource(R.drawable.dislike_unselect);
            }

            holder.binding.likelay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeid = listt.get(position).getId();
                    if (listt.get(position).getFav_status().equalsIgnoreCase("Like")) {
                        listt.get(position).setFav_status("");
                        likestatus = "";
                    } else {
                        listt.get(position).setFav_status("Like");
                        likestatus = "Like";
                    }
                    adapter1.notifyDataSetChanged();
                    addTofavCall();
                }
            });

            holder.binding.dislikelay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeid = listt.get(position).getId();
                    if (listt.get(position).getFav_status().equalsIgnoreCase("Unlike")) {
                        listt.get(position).setFav_status("");
                        likestatus = "";
                    } else {
                        listt.get(position).setFav_status("Unlike");
                        likestatus = "Unlike";
                    }
                    adapter1.notifyDataSetChanged();
                    addTofavCall();
                }
            });

            holder.binding.mainlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    id = listt.get(position).getId();
                    address = listt.get(position).getAddress();
                    getPlaceId(id);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listt == null ? 0 : listt.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            PlacelistitemBinding binding;

            public ViewHolder(@NonNull PlacelistitemBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
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
            if (language.equalsIgnoreCase("en")) {
                holder.binding.tagname.setText(mapBeanLists.get(position).getTagName());
                holder.binding.tagname.setTextColor(Color.parseColor(mapBeanLists.get(position).getColorCode()));
                holder.binding.tagcount.setText(String.valueOf(mapBeanLists.get(position).getTotalTagPlaceCount()));
                holder.binding.tagcount.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(mapBeanLists.get(position).getColorCode())));
            } else {
                holder.binding.tagname.setText(mapBeanLists.get(position).getTagNameAr());
            }
        }

        @Override
        public int getItemCount() {
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

    private HashMap<String, String> getFavParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("place_id", placeid);
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
                            getCountryMaps();
                        } else {
                            Toast.makeText(PlacesListActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("AddToFav", "Error processing response", e);
                    }
                } else {
                    Toast.makeText(PlacesListActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(PlacesListActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPlaceId(String id) {
        ApiCall.get().Create().getPlaceid(address, id, language).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("sssssssssssssssss "+response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            JSONObject jsonObject1 = object.getJSONObject("result");
                            String place_id = jsonObject1.getString("place_id");

                            boolean currentUserFavourite = false;
                            for (PlaceDetail place : listt) {
                                if (place.getPlaceName().equals(placeName)) {
                                    currentUserFavourite = place.isCurrentUserFavorite();
                                    break;
                                }
                            }

                            Intent ii = new Intent(PlacesListActivity.this, DetailsActivity.class);
                            ii.putExtra("id", id);
                            ii.putExtra("placeName", placeName);
                            ii.putExtra("place_id", place_id);
                            ii.putExtra("address", address);
                            ii.putExtra("distance", distance);
                            ii.putExtra("placeNameAr", placeNameAr);
                            ii.putExtra("descriptionAr", descriptionAr);
                            ii.putExtra("favstatus", favstatus);
                            ii.putExtra("LOCATION_DISTANCE", LOCATION_DISTANCE);
                            ii.putExtra("description", description);
                            ii.putExtra("totalunfavplace", totalunfavplace);
                            ii.putExtra("totalfavplace", totalfavplace);
                            Gson gson = new Gson();
                            String json = gson.toJson(tagDetails);
                            ii.putExtra("placeDetails", json);
                            Gson place = new Gson();
                            String image = place.toJson(imageData);
                            ii.putExtra("images", image);
                            ii.putExtra("tagname", tagname);
                            ii.putExtra("tagnameAr", tagnameAr);
                            ii.putExtra("promoCodeAndDiscount", promoCodeAndDiscount);
                            ii.putExtra("lat", lat);
                            ii.putExtra("lng", lon);
                            ii.putExtra("currentUserFavourite", currentUserFavourite);
                            ii.putExtra("suggestedTime", suggestedTime);
                            ii.putExtra("advice", advice);
                            ii.putExtra("videoLinkEn", videoLinkEn);
                            ii.putExtra("videoLinkAr", videoLinkAr);
                            ii.putExtra("adviceArabic", adviceAr);
                            ii.putExtra("promoCodeAndDiscount", promoCodeAndDiscount);
                            ii.putExtra("promoCodePercentage", discountPercentage);
                            ii.putExtra("icon", icon);
                            ii.putExtra("avgRating", rating);

                            startActivityForResult(ii, 1);
                        } else {
                            Toast.makeText(PlacesListActivity.this, getResources().getString(R.string.thisplacedetailsisnotavailable), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e("GetPlaceId", "Error getting place ID", e);
                        Toast.makeText(PlacesListActivity.this, getResources().getString(R.string.serverproblem), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PlacesListActivity.this, getResources().getString(R.string.serverproblem), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getLatLong() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        oldlocation = location;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            lat = "" + latitude;
            lon = "" + longitude;
        } else {
            location_ar = tracker.getLocation();
            if (location_ar == null) {
                latitude = 56.1304;
                longitude = 106.3468;
            } else {
                longitude = location_ar.getLongitude();
                latitude = location_ar.getLatitude();
                lat = "" + latitude;
                lon = "" + longitude;
            }
        }
    }

    void initGps() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            lat = "" + latitude;
            lon = "" + longitude;
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

    private final List<Tag> favoriteTagsList = new ArrayList<>();
    private TagsAdapter favTagsAdapter;

    private void tagsRecyclerView(List<Tag> tags) {
        if (tags != null && !tags.isEmpty()) {
            binding.tags.setVisibility(View.VISIBLE);
            boolean isArabic = language.equalsIgnoreCase("ar");

            TagsAdapter.OnTagClickListener tagClickListener = new TagsAdapter.OnTagClickListener() {
                @Override
                public void onTagClick(String tagColor) {
                    tagColors = tagColor;
                    filterMarkersByTag(tagColor);
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }
            };
            TagsAdapter tagsAdapter = new TagsAdapter(this, tags, isArabic, false, tagClickListener);
            binding.tags.setLayoutManager(new LinearLayoutManager(this));
            binding.tags.setAdapter(tagsAdapter);
        }
    }

    private void filterMarkersByTag(String tagColor) {
        if (gmap != null) {
            tag = TagType.selectedtags;
            markerListOptione.clear();
            clusterManager.clearItems();
            markersLoaded = 0;

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boolean hasMarkers = false;

            for (int k = 0; k < listt.size(); k++) {
                PlaceDetail placeDetail = listt.get(k);
                if (placeDetail.getTagDetails() != null) {
                    boolean hasTag = placeDetail.getTagDetails().stream()
                            .anyMatch(tagDetail -> tagDetail.getColorCode().equals(tagColor));
                    if (hasTag) {
                        double latitude = Double.parseDouble(placeDetail.getLat());
                        double longitude = Double.parseDouble(placeDetail.getLon());
                        addCustomMarker(latitude, longitude, placeDetail.getIcon(), placeDetail.getPlaceName(), k, placeDetail);
                        boundsBuilder.include(new LatLng(latitude, longitude));
                        hasMarkers = true;
                    }
                }
            }
            if (hasMarkers) {
                LatLngBounds bounds = boundsBuilder.build();
                gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150)); // 150 = padding
            }
            clusterManager.cluster();
        }
    }

    private void favouriteTags() {
        if (gmap != null) {
            tag = TagType.favouritetags;
            markerListOptione.clear();
            clusterManager.clearItems();
            markersLoaded = 0;

            List<PlaceDetail> favoritePlaces = listt.stream()
                    .filter(PlaceDetail::isCurrentUserFavorite)
                    .collect(Collectors.toList());

            if (favoritePlaces.isEmpty()) {
                return;
            }

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

            for (int i = 0; i < favoritePlaces.size(); i++) {
                PlaceDetail place = favoritePlaces.get(i);
                try {
                    double latitude = Double.parseDouble(place.getLat());
                    double longitude = Double.parseDouble(place.getLon());
                    String iconUrl = place.getIcon();
                    String name = place.getPlaceName();
                    int indexInMainList = listt.indexOf(place);
                    addCustomMarker(latitude, longitude, iconUrl, name, indexInMainList, place);
                    boundsBuilder.include(new LatLng(latitude, longitude));

                } catch (Exception e) {
                    Log.e("FavouriteTags", "Error adding favorite marker", e);
                }
            }

            LatLngBounds bounds = boundsBuilder.build();
            gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
            clusterManager.cluster();
        }
    }

    private void favorite(String placeId, BiConsumer<Boolean, String> toggleHeartIcon) {
        ApiCall.get().Create().addTofavorite(session.getUserID() != null ? session.getUserID() : "", placeId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {
                            boolean isFavorite = !object.getString("result").equalsIgnoreCase("Removed From Favorites");

                            if (toggleHeartIcon != null) {
                                toggleHeartIcon.accept(isFavorite, placeId);
                            }
                            if (placeData != null) {
                                for (PlaceDetail place : placeData) {
                                    if (place.getId().equals(placeId)) {
                                        place.setCurrentUserFavorite(isFavorite);
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Favorite", "Error processing favorite response", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(PlacesListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    Boolean clickOnMapIcon(PlaceItem item) {
        if (item.getTitle().equalsIgnoreCase(getResources().getString(R.string.you))) {
            //Todo: open current location
        } else {
            int aa = Integer.parseInt("" + item.getTag());
            id = listt.get(aa).getId();
            placeid = listt.get(aa).getPlaceId();
            address = listt.get(aa).getAddress();
            description = listt.get(aa).getDescription();
            descriptionAr = listt.get(aa).getDescriptionAr();
            lat = listt.get(aa).getLat();
            lon = listt.get(aa).getLon();
            totalfavplace = listt.get(aa).getTotal_fav_place();
            totalunfavplace = listt.get(aa).getTotal_unfav_place();
            distance = listt.get(aa).getDistance();
            tagname = listt.get(aa).getTag();
            tagnameAr = listt.get(aa).getTagAr();
            favstatus = listt.get(aa).getFav_status();
            tagDetails = listt.get(aa).getTagDetails();
            promoCodeAndDiscount = listt.get(aa).getPromoCodeAndDiscount();
            placeName = listt.get(aa).getPlaceName();
            placeNameAr = listt.get(aa).getPlaceNameAr();
            icon = listt.get(aa).getIcon();
            rating = listt.get(aa).getRating();
            suggestedTime = listt.get(aa).getSuggestedTime();
            advice = listt.get(aa).getAdvice();
            adviceAr = listt.get(aa).getAdviceArabic();
            videoLinkAr = listt.get(aa).getVideo_link_ar();
            videoLinkEn = listt.get(aa).getVideo_link_en();
            discountPercentage = listt.get(aa).getPromoCodePercentage();

            getPlaceId(id);
        }
        return true;
    }
}

interface BooleanCallback {
    void invoke(boolean value);
}

//interface OnFavoriteClick {
//    void onClick(String placeId, BooleanCallback toggleHeartIcon);
//}
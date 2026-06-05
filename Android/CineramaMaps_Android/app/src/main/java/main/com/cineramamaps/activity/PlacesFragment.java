package main.com.cineramamaps.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.caverock.androidsvg.SVGParseException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.TagsAdapter;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.TaglistitemBinding;
import main.com.cineramamaps.enums.TagType;
import main.com.cineramamaps.model.Menudata;
import main.com.cineramamaps.model.PlaceDetail;
import main.com.cineramamaps.model.PlaceItem;
import main.com.cineramamaps.model.PlaceRenderer;
import main.com.cineramamaps.model.PlacesBean;
import main.com.cineramamaps.model.Tag;
import main.com.cineramamaps.model.TagDetail;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, ResultCallback<LocationSettingsResult> {

    private GoogleApiClient googleApiClient;
    CardView listviewbut;
    List<PlaceDetail> listt = new ArrayList<>();
    private LatLng latlong = null;
    String mark_img = "";
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    Location location;
    GPSTracker tracker;
    Location location_ar;
    LocationManager locationManager;
    RecyclerView providerrecyclerview;
    private View view;
    private RecyclerView rv_product;
    ArrayList<Menudata> menulist = new ArrayList<>();
    String itemid = "0", status = "No", cart_cat_id = "";
    String item_id = "", item_price = "", cartcount = "", catid = "", subcatid = "", childsubcatid = "", suppliesstatus = "No";
    boolean btnclick = false;
    ProgressBar progress_bar;
    RelativeLayout botumlay;
    Button continuebtn;
    TextView cart_item_txt, totalservicetxt;
    String image = "";
    String cityid = "";
    SessionManager session;
    ProgressBar progressbar;
    LinearLayout progressContainer;
    TextView progressText;
    private String language = "";
    MyLanguageSession myLanguageSession;
    private GoogleMap gmap;
    public static double longitude = 0.0, latitude = 0.0;
    List<Marker> markerList = new ArrayList<Marker>();
    private MarkerOptions options = new MarkerOptions();
    RelativeLayout markerlay, cancelimg;
    TextView restname_tv, location_tv, distance_tv;
    Marker googlemarker_pos, my_job_location_marker;
    private static final float MIN_ZOOM_LEVEL = 10.0f;
    private static final float MAX_ZOOM_BEFORE_HIDE = 12.0f;
    private int totalPlaces = 0;
    TagType tag = TagType.alltags;
    private List<Tag> tagList = new ArrayList<>();
    String tagColors = "";

    // Clustering related fields
    private ClusterManager<PlaceItem> clusterManager;
    private PlaceRenderer placeRenderer;
    private final ExecutorService markerExecutor = Executors.newFixedThreadPool(2);
    private final ExecutorService bitmapExecutor = Executors.newFixedThreadPool(2);
    private int markersLoaded = 0;
    private static final int MAX_MARKERS_PER_BATCH = 20;
    private Handler markerBatchHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onResume() {
        super.onResume();
        listviewbut.setVisibility(View.GONE);
        getCountryMaps();
    }

    public PlacesFragment(ArrayList<Menudata> menulist, String image, String cityid) {
        this.menulist = menulist;
        this.image = image;
        this.cityid = cityid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        myLanguageSession = new MyLanguageSession(getActivity());
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        view = inflater.inflate(R.layout.fragment_places, container, false);
        tracker = new GPSTracker(getActivity());

        if (MapDetailsActivity.citylat.equalsIgnoreCase("")) {
            getLatLong();
            getCurrentLocation();
        } else {
            latitude = Double.parseDouble("" + MapDetailsActivity.citylat);
            longitude = Double.parseDouble("" + MapDetailsActivity.citylon);
        }

        session = SessionManager.get(getActivity());
        progressbar = view.findViewById(R.id.progressbar);
        progressContainer = view.findViewById(R.id.progressContainer);
        progressText = view.findViewById(R.id.progressText);
        listviewbut = view.findViewById(R.id.listviewbut);
        providerrecyclerview = view.findViewById(R.id.providerrecyclerview);
        markerlay = view.findViewById(R.id.markerlay);
        cancelimg = view.findViewById(R.id.cancelimg);
        restname_tv = view.findViewById(R.id.restname_tv);
        location_tv = view.findViewById(R.id.location_tv);
        distance_tv = view.findViewById(R.id.distance_tv);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        providerrecyclerview.setLayoutManager(mLayoutManager);

        SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        listviewbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getActivity(), PlacesListActivity.class);
                ii.putExtra("city_id", cityid);
                startActivity(ii);
            }
        });

        return view;
    }

    private void getCountryMaps() {
        progressContainer.setVisibility(View.VISIBLE);
        progressbar.setProgress(0);
        progressText.setText("0%");
        markersLoaded = 0;

        ApiCall.get().Create().getCityMapsDetails(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("GetProducts  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.i("-------------PLACES-----------", responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equalsIgnoreCase("1")) {
                            PlacesBean successData = new Gson().fromJson(responseData, PlacesBean.class);
                            listt.clear();
                            listt = successData.getResult().getPlaceDetails();
                            totalPlaces = listt.size();

                            boolean isArabic = language.equalsIgnoreCase("ar");

                            AllRestaurnatAdapter adapter = new AllRestaurnatAdapter(
                                    getActivity(),
                                    successData.getResult().getTags(),
                                    tagList,
                                    isArabic,
                                    tagColor -> {
                                        tag = TagType.selectedtags;
                                        filterMarkersByTag(tagColor);
                                    }
                            );
                            providerrecyclerview.setAdapter(adapter);

                            // Update progress: data loaded = 30%
                            progressbar.setProgress(30);
                            progressText.setText("30%");

                            try {
                                markerList.clear();
                                if (gmap != null) {
                                    gmap.clear();
                                }

                                // Load markers in batches to prevent ANR
                                loadMarkersInBatches(listt);

                                if (!listt.isEmpty()) {
                                    LatLng latLng = new LatLng(
                                            Double.parseDouble(listt.get(listt.size() - 1).getLat()),
                                            Double.parseDouble(listt.get(listt.size() - 1).getLon())
                                    );
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                                    gmap.animateCamera(cameraUpdate);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    progressContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressContainer.setVisibility(View.GONE);
            }
        });
    }

    private HashMap<String, String> getProductParam(String type) {
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("city_id", "" + cityid);
        param.put("lang", "" + language);
        return param;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        gmap.setBuildingsEnabled(false);
        gmap.getUiSettings().setMyLocationButtonEnabled(true);
        gmap.getUiSettings().setMapToolbarEnabled(false);
        gmap.getUiSettings().setZoomControlsEnabled(true);

        // Hide Google POIs (coffee shops, restaurants) and transit
        try {
            String style = "[{\"featureType\":\"poi\",\"stylers\":[{\"visibility\":\"off\"}]},"
                    + "{\"featureType\":\"transit\",\"stylers\":[{\"visibility\":\"off\"}]}]";
            gmap.setMapStyle(new com.google.android.gms.maps.model.MapStyleOptions(style));
        } catch (Exception ignored) {}

        // Setup ClusterManager
        setupClusterManager();

        LatLng latLng = new LatLng(Double.parseDouble("" + latitude), Double.parseDouble("" + longitude));
        MarkerOptions marker1 = new MarkerOptions().position(latLng).flat(true).anchor(0.5f, 0.5f);
        marker1.title(getResources().getString(R.string.you));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        gmap.animateCamera(cameraUpdate);

        cancelimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerlay.setVisibility(View.GONE);
                providerrecyclerview.setVisibility(View.VISIBLE);
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

        gmap.setOnCameraIdleListener(() -> {
            if (clusterManager != null) {
                clusterManager.onCameraIdle();
            }

            float currentZoom = gmap.getCameraPosition().zoom;

            // Update renderer zoom state
            if (placeRenderer != null) {
                placeRenderer.updateZoomState(currentZoom >= 14.0f);
            }

            // Hide all pins when zoom level reaches 12 (client requirement)
            boolean shouldHide = currentZoom >= MAX_ZOOM_BEFORE_HIDE;
            for (Marker marker : markerList) {
                marker.setVisible(!shouldHide);
            }
        });

        getCountryMaps();
    }

    private void setupClusterManager() {
        if (gmap == null) return;

        clusterManager = new ClusterManager<>(requireContext(), gmap);
        NonHierarchicalDistanceBasedAlgorithm<PlaceItem> algorithm = new NonHierarchicalDistanceBasedAlgorithm<>();
        algorithm.setMaxDistanceBetweenClusteredItems(50);
        clusterManager.setAlgorithm(algorithm);
        placeRenderer = new PlaceRenderer(requireContext(), gmap, clusterManager);
        clusterManager.setRenderer(placeRenderer);

        // Set click listeners
        clusterManager.setOnClusterItemClickListener(item -> clickOnMapIcon(item));
        clusterManager.setOnClusterClickListener(cluster -> {
            PlaceItem firstItem = cluster.getItems().iterator().next();
            return clickOnMapIcon(firstItem);
        });

        // Set camera idle listener with zoom-based icon swapping
        gmap.setOnCameraIdleListener(() -> {
            clusterManager.onCameraIdle();
            float currentZoom = gmap.getCameraPosition().zoom;
            boolean shouldBeZoomed = currentZoom >= 14.0f;
            if (placeRenderer != null) {
                placeRenderer.updateZoomState(shouldBeZoomed);
            }
        });
    }

    // Load markers in batches
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

                    if (!place.getIcon().equalsIgnoreCase("") && !place.getIcon().equalsIgnoreCase("" + BaseUrl.image_baseurl)) {
                        addCustomMarker(latLng.latitude, latLng.longitude, place.getIcon(), place.getPlaceName(), k, place);
                    } else {
                        addCustomMarker(latLng.latitude, latLng.longitude, place.getMap_icon(), place.getPlaceName(), k, place);
                    }
                }

                Log.d("MarkerLoad", "Batch " + (currentBatch + 1) + "/" + totalBatches + " completed");

            }, batch * 100L); // Stagger batches by 100ms
        }
    }

  /*  private void addCustomMarker(double latitude, double longitude, String image, String name, int k, PlaceDetail placeDetail) {
        Log.e("FRIENDIMAGE", " >> " + image);
        LatLng latLng = new LatLng(latitude, longitude);

        if (gmap == null) return;

        String iconUrl = placeDetail.getIcon();

        if (iconUrl != null) {
            if (iconUrl.endsWith(".svg")) {
                RequestBuilder<PictureDrawable> requestBuilder = Glide.with(this)
                        .as(PictureDrawable.class)
                        .load(iconUrl)
                        .error(R.drawable.plane)
                        .listener(new RequestListener<PictureDrawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<PictureDrawable> target, boolean isFirstResource) {
                                Log.e("SVG Load Error", e != null ? e.getMessage() : "Unknown error");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(@NonNull PictureDrawable resource, Object model, Target<PictureDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                createMarkerBitmapAsync(placeDetail, resource, null, true,
                                        (zoomedBitmap, normalBitmap) -> {
                                            addMarkerToCluster(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
                                        });
                                return false;
                            }
                        });
                requestBuilder.into(new CustomTarget<PictureDrawable>() {
                    @Override
                    public void onResourceReady(@NonNull PictureDrawable resource, @Nullable Transition<? super PictureDrawable> transition) {
                        Log.d("Request Success", "Drawable loaded");
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.d("Resource Cleared", "Drawable placeholder cleared");
                    }
                });
            } else {
                RequestBuilder<Bitmap> requestBuilder = Glide.with(this)
                        .asBitmap()
                        .load(iconUrl)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                Log.e("Bitmap Load Error", e != null ? e.getMessage() : "Unknown error");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(@NonNull Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                createMarkerBitmapAsync(placeDetail, null, bitmap, false,
                                        (zoomedBitmap, normalBitmap) -> {
                                            addMarkerToCluster(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
                                        });
                                return false;
                            }
                        });
                requestBuilder.into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Log.d("Request Success", "Bitmap loaded");
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.d("Resource Cleared", "Bitmap placeholder cleared");
                    }
                });
            }
        }
    }*/

    private void addCustomMarker(double latitude, double longitude, String image, String name, int k, PlaceDetail placeDetail) {

        if (!isAdded() || getContext() == null || getView() == null) {
            Log.e("FragmentState", "Fragment not attached, skipping...");
            return;
        }

        LatLng latLng = new LatLng(latitude, longitude);
        if (gmap == null) return;

        String iconUrl = placeDetail.getIcon();

        if (iconUrl != null) {

            if (iconUrl.endsWith(".svg")) {

                RequestBuilder<PictureDrawable> requestBuilder =
                        Glide.with(requireContext().getApplicationContext())
                                .as(PictureDrawable.class)
                                .load(iconUrl)
                                .error(R.drawable.plane)
                                .listener(new RequestListener<PictureDrawable>() {

                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<PictureDrawable> target, boolean isFirstResource) {
                                        Log.e("SVG Load Error", e != null ? e.getMessage() : "Unknown error");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(@NonNull PictureDrawable resource, Object model, Target<PictureDrawable> target, DataSource dataSource, boolean isFirstResource) {

                                        if (!isAdded()) return false;

                                        createMarkerBitmapAsync(placeDetail, resource, null, true,
                                                (zoomedBitmap, normalBitmap) -> {
                                                    if (!isAdded()) return;
                                                    addMarkerToCluster(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
                                                });
                                        return false;
                                    }
                                });

                requestBuilder.into(new CustomTarget<PictureDrawable>() {
                    @Override
                    public void onResourceReady(@NonNull PictureDrawable resource, @Nullable Transition<? super PictureDrawable> transition) {
                        Log.d("Request Success", "Drawable loaded");
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.d("Resource Cleared", "Drawable cleared");
                    }
                });

            } else {

                RequestBuilder<Bitmap> requestBuilder =
                        Glide.with(requireContext().getApplicationContext())
                                .asBitmap()
                                .load(iconUrl)
                                .listener(new RequestListener<Bitmap>() {

                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                        Log.e("Bitmap Load Error", e != null ? e.getMessage() : "Unknown error");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(@NonNull Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                                        if (!isAdded()) return false;

                                        createMarkerBitmapAsync(placeDetail, null, bitmap, false,
                                                (zoomedBitmap, normalBitmap) -> {
                                                    if (!isAdded()) return;
                                                    addMarkerToCluster(latLng, name, k, zoomedBitmap, normalBitmap, placeDetail);
                                                });

                                        return false;
                                    }
                                });

                requestBuilder.into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Log.d("Request Success", "Bitmap loaded");
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.d("Resource Cleared", "Bitmap cleared");
                    }
                });
            }
        }
    }

    interface BitmapPairCallback {
        void onBitmapsReady(Bitmap zoomedBitmap, Bitmap normalBitmap);
    }

    private void createMarkerBitmapAsync(PlaceDetail placeDetail, PictureDrawable drawable, Bitmap bitmap,
                                         boolean isSVG, BitmapPairCallback callback) {
        double zoomed = 18;
        double unZoomed = 12;

        bitmapExecutor.execute(() -> {
            long startTime = System.currentTimeMillis();
            Bitmap zoomedBitmap = mapMarker(placeDetail, drawable, bitmap, isSVG, zoomed);
            Bitmap normalBitmap = mapMarker(placeDetail, drawable, bitmap, isSVG, unZoomed);

            long duration = System.currentTimeMillis() - startTime;
            if (duration > 50) {
                Log.w("Performance", "Slow bitmap creation: " + duration + "ms");
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> callback.onBitmapsReady(zoomedBitmap, normalBitmap));
            }
        });
    }

    private void addMarkerToCluster(LatLng latLng, String name, int k, Bitmap zoomedBitmap, Bitmap normalBitmap, PlaceDetail placeDetail) {
        markersLoaded++;

        // Create the cluster item
        PlaceItem item = new PlaceItem(latLng, name, Integer.toString(k),
                BitmapDescriptorFactory.fromBitmap(zoomedBitmap), zoomedBitmap, normalBitmap);

        if (clusterManager != null) {
            clusterManager.addItem(item);
        }

        // Batch cluster updates
        if (markersLoaded % MAX_MARKERS_PER_BATCH == 0) {
            clusterManager.cluster();
        }

        // Update progress bar (30% for data load + 70% for markers)
        if (totalPlaces > 0) {
            int markerProgress = (int) (30 + (70.0 * markersLoaded / totalPlaces));
            progressbar.setProgress(Math.min(markerProgress, 100));
            progressText.setText(Math.min(markerProgress, 100) + "%");
        }

        // All markers loaded — hide progress and do final cluster
        if (markersLoaded >= totalPlaces) {
            progressbar.setProgress(100);
            progressText.setText("100%");
            // Small delay so user sees 100% before hiding
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                progressContainer.setVisibility(View.GONE);
            }, 500);
            clusterManager.cluster();
        }
    }

    private void addMarkerToMap(LatLng latLng, String name, int k, Bitmap bitmap) {
        Marker marker = gmap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .visible(true));

        marker.setTag(k);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        gmap.animateCamera(cameraUpdate);
        markerList.add(marker);
    }

   /* private Bitmap mapMarker(PlaceDetail placeDetail, PictureDrawable imageView, Bitmap image, Boolean isSVG, double zoom) {
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View markerLayout = inflater.inflate(R.layout.activity_map_marker, null);
        ImageView outerFrame = markerLayout.findViewById(R.id.outerFrame);
        ImageView innerLayer = markerLayout.findViewById(R.id.pieCircle);
        ImageView innerFrame = markerLayout.findViewById(R.id.innerFrame);
        ImageView pinIcon = markerLayout.findViewById(R.id.pinIcon);
        ImageView ppp = markerLayout.findViewById(R.id.ppp);
        OutlinedTextView outlinedText = markerLayout.findViewById(R.id.placeTitle);

        // Hide titles on mini-map
        outlinedText.setVisibility(View.GONE);

        if (placeDetail.getShowOnlyIcon().equals("1")) {
            if (placeDetail.getTagDetails() != null) {
                List<String> colorList = placeDetail.getTagDetails().stream()
                        .filter(Objects::nonNull)
                        .map(TagDetail::getColorCode)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (!colorList.isEmpty()) {
                    String lastColor = colorList.stream().findFirst().get();
                    ColorStateList tint = ColorStateList.valueOf(Color.parseColor(lastColor));
                    innerFrame.setImageTintList(tint);
                    innerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                }
                applyPieChart(innerLayer, colorList);
            }

            ColorStateList tint;
            if (!placeDetail.getPromoCodeAndDiscount().isEmpty()) {
                tint = ColorStateList.valueOf(Color.parseColor("#F1D280"));
            } else {
                tint = ColorStateList.valueOf(Color.WHITE);
            }
            outerFrame.setImageTintList(tint);
            outerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);

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
        Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(),
                markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }*/

    private Bitmap mapMarker(PlaceDetail placeDetail, PictureDrawable imageView, Bitmap image, Boolean isSVG, double zoom) {

        Context context = getContext();
        if (context == null) {
            Log.e("Context", "Context null, skip marker");
            return null;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View markerLayout = inflater.inflate(R.layout.activity_map_marker, null);

        ImageView outerFrame = markerLayout.findViewById(R.id.outerFrame);
        ImageView innerLayer = markerLayout.findViewById(R.id.pieCircle);
        ImageView innerFrame = markerLayout.findViewById(R.id.innerFrame);
        ImageView pinIcon = markerLayout.findViewById(R.id.pinIcon);
        ImageView ppp = markerLayout.findViewById(R.id.ppp);
        OutlinedTextView outlinedText = markerLayout.findViewById(R.id.placeTitle);

        // Hide titles on mini-map
        outlinedText.setVisibility(View.GONE);

        if (placeDetail.getShowOnlyIcon().equals("1")) {

            if (placeDetail.getTagDetails() != null) {
                List<String> colorList = placeDetail.getTagDetails().stream()
                        .filter(Objects::nonNull)
                        .map(TagDetail::getColorCode)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (!colorList.isEmpty()) {
                    String lastColor = colorList.get(0);
                    innerFrame.setImageTintList(ColorStateList.valueOf(Color.parseColor(lastColor)));
                    innerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
                }

                applyPieChart(innerLayer, colorList);
            }

            ColorStateList tint = !placeDetail.getPromoCodeAndDiscount().isEmpty()
                    ? ColorStateList.valueOf(Color.parseColor("#F1D280"))
                    : ColorStateList.valueOf(Color.WHITE);

            outerFrame.setImageTintList(tint);
            outerFrame.setImageTintMode(PorterDuff.Mode.SRC_ATOP);

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

        markerLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(
                markerLayout.getMeasuredWidth(),
                markerLayout.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888
        );

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

    @Override
    public void onDestroy() {
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

    public class AllRestaurnatAdapter extends RecyclerView.Adapter<AllRestaurnatAdapter.ViewHolder> {
        Context context;
        List<Tag> mapBeanLists;
        List<Tag> tagList;
        boolean isArabic;
        TagsAdapter.OnTagClickListener onTagClickListener;

        public AllRestaurnatAdapter(Context context, List<Tag> mapBeanLists, List<Tag> tagList, boolean isArabic, TagsAdapter.OnTagClickListener onTagClickListener) {
            this.context = context;
            this.mapBeanLists = mapBeanLists;
            this.tagList = tagList;
            this.isArabic = isArabic;
            this.onTagClickListener = onTagClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TaglistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.taglistitem, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Tag tag = mapBeanLists.get(position);
            holder.binding.tagname.setText(isArabic ? tag.getTagNameAr() : tag.getTagName());
            holder.binding.tagname.setTextColor(Color.parseColor(tag.getColorCode()));
            holder.binding.tagcount.setText(String.valueOf(tag.getTotalTagPlaceCount()));
            holder.binding.tagcount.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(tag.getColorCode())));

            holder.binding.item.setOnClickListener(v -> {
                if (onTagClickListener != null) {
                    onTagClickListener.onTagClick(tag.getColorCode());
                }
            });
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

    String address = "", id = "";

    private void getPlaceId() {
        ApiCall.get().Create().getPlaceid(address, id, language).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyProfileDetail >", " >" + response);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            JSONObject jsonObject1 = object.getJSONObject("result");
                            String place_id = jsonObject1.getString("place_id");
                            if (MapDetailsActivity.plan_purcahse_status.equalsIgnoreCase("Yes")) {
//                                Intent ii = new Intent(getActivity(), PlacesDetailsActivity.class);
//                                ii.putExtra("id", id);
//                                ii.putExtra("place_id", place_id);
//                                startActivity(ii);
                            } else {
                                Toast.makeText(getActivity(), "" + getResources().getString(R.string.youhavenosubscriptionplan), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "" + getResources().getString(R.string.thisplacedetailsisnotavailable), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "" + getResources().getString(R.string.serverproblem), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "" + getResources().getString(R.string.serverproblem), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (location_ar != null) {
            longitude = location_ar.getLongitude();
            latitude = location_ar.getLatitude();
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + latitude);
        } else {
            System.out.println("----------------geting Location from GPS----------------");
            location_ar = tracker.getLocation();
            if (location_ar == null) {
                // Handle null location
            } else {
                longitude = location_ar.getLongitude();
                latitude = location_ar.getLatitude();
            }
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + longitude);
        }
    }

    private void getLatLong() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        location_ar = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    private void filterMarkersByTag(String tagColor) {
        if (gmap != null && clusterManager != null) {
            tag = TagType.selectedtags;
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
                        addCustomMarker(latitude, longitude, placeDetail.getIcon(),
                                placeDetail.getPlaceName(), k, placeDetail);
                        boundsBuilder.include(new LatLng(latitude, longitude));
                        hasMarkers = true;
                    }
                }
            }

            if (hasMarkers) {
                LatLngBounds bounds = boundsBuilder.build();
                gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
            }
            clusterManager.cluster();
        }
    }

    private boolean clickOnMapIcon(PlaceItem item) {
        if (item.getTitle().equalsIgnoreCase(getResources().getString(R.string.you))) {
            // Handle current location click
        } else {
            int index = Integer.parseInt("" + item.getTag());
            if (index >= 0 && index < listt.size()) {
                id = listt.get(index).getId();
                address = listt.get(index).getAddress();
                getPlaceId();
            }
        }
        return true;
    }
}
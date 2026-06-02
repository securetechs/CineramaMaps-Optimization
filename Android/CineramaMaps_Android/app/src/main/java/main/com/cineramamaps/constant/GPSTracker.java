package main.com.cineramamaps.constant;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class GPSTracker extends Service {
    private final Context mContext;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;

    private FusedLocationProviderClient fusedClient;
    private LocationCallback locationCallback;

    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 2000;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            LocationManager locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                return null;
            }

            this.canGetLocation = true;

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

            fusedClient = LocationServices.getFusedLocationProviderClient(mContext);

            // Get last known location immediately (fastest path)
            fusedClient.getLastLocation().addOnSuccessListener(loc -> {
                if (loc != null) {
                    location = loc;
                    latitude = loc.getLatitude();
                    longitude = loc.getLongitude();
                    Log.d("GPSTracker", "Last location: " + latitude + ", " + longitude);
                }
            });

            // Request updates for continuous tracking
            LocationRequest locationRequest = new LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL)
                    .setMinUpdateIntervalMillis(FASTEST_INTERVAL)
                    .build();

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location loc = locationResult.getLastLocation();
                    if (loc != null) {
                        location = loc;
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();
                    }
                }
            };

            fusedClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void stopUsingGPS() {
        if (fusedClient != null && locationCallback != null) {
            fusedClient.removeLocationUpdates(locationCallback);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
        });
        alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}

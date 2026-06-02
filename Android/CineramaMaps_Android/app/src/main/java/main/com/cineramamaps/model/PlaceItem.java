package main.com.cineramamaps.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class PlaceItem implements ClusterItem {
    private final LatLng position;
    private final String name;
    private final String tag;
    private final BitmapDescriptor icon; // <— store icon
    private final Bitmap iconBitmap;
    private final Bitmap iconDrawable;

    public PlaceItem(LatLng position, String name, String tag, BitmapDescriptor icon, Bitmap iconBitmap, Bitmap iconDrawable) {
        this.position = position;
        this.name = name;
        this.tag = tag;
        this.icon = icon;
        this.iconBitmap = iconBitmap;
        this.iconDrawable = iconDrawable;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return "";
    }

    @Nullable
    @Override
    public Float getZIndex() {
        return 0f;
    }

    public String getTag() {
        return tag;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public Bitmap getIconDrawable() {
        return iconDrawable;
    }
}

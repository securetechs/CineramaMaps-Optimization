package main.com.cineramamaps.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceRenderer extends DefaultClusterRenderer<PlaceItem> {

    private boolean isZoomedIn = false;
    private final Map<PlaceItem, Marker> itemMarkerMap = new HashMap<>();

    public PlaceRenderer(Context context, GoogleMap map, ClusterManager<PlaceItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(PlaceItem item, @NonNull MarkerOptions markerOptions) {
        if (item.getIcon() != null) {
            markerOptions.icon(item.getIcon());
        } else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        markerOptions.title(item.getTitle());
        markerOptions.snippet(item.getTag());
    }

    @Override
    protected void onBeforeClusterRendered(@NonNull Cluster<PlaceItem> cluster, @NonNull MarkerOptions markerOptions) {
        PlaceItem firstItem = cluster.getItems().iterator().next();

        if (firstItem.getIcon() != null) {
            List<PlaceItem> items = new ArrayList<>(cluster.getItems());
            Bitmap clusterBmp = buildClusterIcon(items);
            if (clusterBmp != null) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(clusterBmp));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
        } else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        markerOptions.title(firstItem.getTitle());
        markerOptions.snippet(firstItem.getTag());
    }

    @Override
    protected void onClusterItemRendered(@NonNull PlaceItem item, @NonNull Marker marker) {
        super.onClusterItemRendered(item, marker);
        itemMarkerMap.put(item, marker);
    }

    @Override
    protected void onClusterItemUpdated(@NonNull PlaceItem item, @NonNull Marker marker) {
        if (item.getIcon() != null) {
            marker.setIcon(item.getIcon());
        } else {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        marker.setTitle(item.getTitle());
        marker.setSnippet(item.getTag());
    }

    @Override
    protected void onClusterUpdated(@NonNull Cluster<PlaceItem> cluster, @NonNull Marker marker) {
        PlaceItem firstItem = cluster.getItems().iterator().next();
        if (firstItem.getIcon() != null) {
            List<PlaceItem> items = new ArrayList<>(cluster.getItems());
            Bitmap clusterBmp = buildClusterIcon(items);
            if (clusterBmp != null) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(clusterBmp));
            } else {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
        } else {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        marker.setTitle(firstItem.getTitle());
        marker.setSnippet(firstItem.getTag());
    }

    /**
     * Called from PlacesListActivity when zoom changes.
     */
    public void updateZoomState(boolean zoomed) {
        this.isZoomedIn = zoomed;
    }

    public boolean isZoomedIn() {
        return isZoomedIn;
    }

    private Bitmap buildClusterIcon(List<PlaceItem> items) {
        Bitmap bmp1 = items.get(0).getIconBitmap();
        if (bmp1 == null) return null;
        int width = bmp1.getWidth();
        int height = bmp1.getHeight();
        Bitmap combined = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combined);
        Bitmap bmp2 = items.get(1 < items.size() ? 1 : 0).getIconDrawable();
        if (bmp2 == null) bmp2 = bmp1;

        if (items.size() > 2) {
            canvas.drawBitmap(bmp2, 0, -8, null);
            canvas.drawBitmap(bmp2, 0, -4, null);
        } else {
            canvas.drawBitmap(bmp2, 0, -4, null);
        }
        canvas.drawBitmap(bmp2, 0, 0, null);
        return combined;
    }
}

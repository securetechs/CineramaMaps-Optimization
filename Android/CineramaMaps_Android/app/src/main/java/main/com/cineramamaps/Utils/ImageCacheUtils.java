package main.com.cineramamaps.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;
import android.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCacheUtils {

    // In-memory caches
    private static final LruCache<String, Bitmap> bitmapMemoryCache;
    private static final LruCache<String, PictureDrawable> svgMemoryCache;
    private static final LruCache<String, Bitmap[]> markerBitmapCache;

    static {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        bitmapMemoryCache = new LruCache<>(cacheSize);
        svgMemoryCache = new LruCache<>(cacheSize);
        markerBitmapCache = new LruCache<>(200);
    }

    // Rendered marker bitmap pair cache (zoomed + unzoomed)
    public static void putMarkerBitmaps(String key, Bitmap zoomed, Bitmap normal) {
        if (zoomed != null && normal != null && markerBitmapCache.get(key) == null) {
            markerBitmapCache.put(key, new Bitmap[]{zoomed, normal});
        }
    }

    public static Bitmap[] getMarkerBitmaps(String key) {
        return markerBitmapCache.get(key);
    }

    public static void putBitmapToMemory(String key, Bitmap bitmap) {
        if (bitmap != null && bitmapMemoryCache.get(key) == null) {
            bitmapMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemory(String key) {
        return bitmapMemoryCache.get(key);
    }

    public static void putSvgToMemory(String key, PictureDrawable drawable) {
        if (drawable != null && svgMemoryCache.get(key) == null) {
            svgMemoryCache.put(key, drawable);
        }
    }

    public static PictureDrawable getSvgFromMemory(String key) {
        return svgMemoryCache.get(key);
    }

    // 🗂 File-based cache
    public static File getCacheDir(Context context) {
        return new File(context.getCacheDir(), "map_icons");
    }

    public static File getCachedFile(Context context, String url) {
        File dir = getCacheDir(context);
        if (!dir.exists()) dir.mkdirs();
        String fileName = String.valueOf(url.hashCode());
        return new File(dir, fileName);
    }

    public static void saveBitmapToCache(File file, Bitmap bitmap) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 80, out);
            } else {
                bitmap.compress(Bitmap.CompressFormat.WEBP, 80, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBytesToCache(File file, byte[] data) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

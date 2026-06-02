package main.com.cineramamaps.constant;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Smoolider_Utils {

    static int currentPage = 0;
    static Timer timer;
    static final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    static final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int stretch_width = Math.round((float)width / (float)reqWidth);
        int stretch_height = Math.round((float)height / (float)reqHeight);

        if (stretch_width <= stretch_height)
            return stretch_height;
        else
            return stretch_width;
    }


    public static void autoplay_viewpager(final SmoothViewpager vPager, final int num_pages){

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == num_pages) {
                    currentPage = 0;
                }
                if (vPager!=null){
                    vPager.setCurrentItem(currentPage++, true);
                }

            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

    }


    public static void  stop_autoplay_ViewPager(){
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    /**
     * Open a web page of a specified URL
     * @param url URL to open
     */
    public static void openWebPage(Context context, String url){
        if(url.equalsIgnoreCase("") || url.isEmpty()){
            Toast.makeText(context, "Bad url", Toast.LENGTH_SHORT).show();
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }
    }

}


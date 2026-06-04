package main.com.cineramamaps.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;


import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

 import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import main.com.cineramamaps.restapi.ApiCall;

/**
 * Created by shubham on 22/01/25.
 */
public class MyApplication extends MultiDexApplication  implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;
    public Activity mActivity;
    private Activity mCurrentActivity;
    public static MyApplication REF_SMART_APPLICATION;
    public static boolean appMinimized = false;
    public static String appActivity = "";

    public Activity getmCurrentActivity() {
        return this.mCurrentActivity;
    }

    public void setmCurrentActivity(final Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    @Override
    public void onCreate() {
        /**
         Edited by Parminder Singh on 1/30/17 at 3:47 PM
         **/
        //  Mint.initAndStartSession(this, "14115934");
        super.onCreate();
        try {
            mInstance = this;
            ApiCall.init(this);
            registerActivityLifecycleCallbacks(this);
        } catch (Exception e) {
            e.printStackTrace();
        }



        // init socket
        REF_SMART_APPLICATION = this;

        try {
            ProviderInstaller.installIfNeeded (getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }


        if(MyApplication.getInstance().isOnline()){
            final String[] token = {""};
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (task.isSuccessful()){
                        if (task.isComplete()) {
                            token[0] = task.getResult();
                            Log.e("AppConstants", "onComplete: new Token got: " + token[0]);
                        }
                    }
                }
            });
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
        MultiDex.install(this);
    }



    public static synchronized MyApplication getInstance() {
        return mInstance;
    }



    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }


    public int appVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String osVersion() {
        return Build.VERSION.RELEASE;
    }

    public String country() {
        return getResources().getConfiguration().locale.getDisplayCountry(Locale.getDefault());
    }


    public String deviceName() {
        return Build.MANUFACTURER + Build.MODEL;
    }


    public String getIPAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof java.net.Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return "No Connection";
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return "No Connection";
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return "Wi-Fi";
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return "Mobile Data";
            } else {
                return "No Connection";
            }
        } else {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    return "Wi-Fi";
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return "Mobile Data";
                }
            }
            return "No Connection";
        }
    }



    private Toast toast;

    public Toast getToast() {
        return toast;
    }

    public void setToast(Toast toast) {
        this.toast = toast;
    }


    public boolean isOnline() {
        try {
            ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectManager.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            } else {
                return false;
            }
            return false;
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.e("MyCurretnActvit onActivityResumed",activity.getComponentName().getClassName()+"++"+appMinimized);
        appMinimized= false;
        appActivity = activity.getComponentName().getClassName();
        //MyApplication.REF_SMART_APPLICATION.getSocketRequestManager().connectSocket(this);
        //MyApplication.REF_SMART_APPLICATION.getSocketManager().connectSocket(this);

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.e("MyCurretnActvit",""+appMinimized);
        Log.e("MyCurretnActvit onActivityPaused",activity.getComponentName().getClassName()+"++"+appMinimized);
        appMinimized = true;
        appActivity = activity.getComponentName().getClassName();

        //MyApplication.REF_SMART_APPLICATION.getSocketRequestManager().disconnectSocket();
        //MyApplication.REF_SMART_APPLICATION.getSocketManager().disconnectSocket();

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }


}

package main.com.cineramamaps.notificationservices;



import static main.com.cineramamaps.Utils.NotificationUtils.isAppIsInBackground;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.NotificationUtils;
import main.com.cineramamaps.activity.NotificationActivity;
import main.com.cineramamaps.app.Config;
import main.com.cineramamaps.tabactivity.MainTabActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    public static String notification_data = "";
    private SessionManager session;
    String type_str="",user_id="";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        session= SessionManager.get(getApplicationContext());
       /* PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
*/

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean result = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && powerManager.isInteractive() || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH && powerManager.isScreenOn();

        if (!result) {
            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MH24_SCREENLOCK");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MH24_SCREENLOCK");
            wl_cpu.acquire(10000);
        } else {
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();
        }

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }
    private void handleNotification(String message) {

        if (!isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();

        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }


    private void handleDataMessage(JSONObject json) {
        String format = "";
        // notification_data = "";
        notification_data = json.toString();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format = simpleDateFormat.format(new Date());
            Log.e(TAG, "push json: " + json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject data = json.getJSONObject("message");
            String keyMessage = data.getString("noti_key").trim();
         //   String receiver_id = data.getString("receiver_id").trim();
            String message = "";
            Log.e("LANGUAGE "," >> "+session.IsEnglish1());
if (session.IsEnglish1()){
    message= data.getString("noti_message");
}
else {
    message = data.getString("noti_message");
}

            Log.e("IN Service", "KEY: " + keyMessage);
            Log.e("IN sessionuserid", "KEY: " + session.getUserID());
           // Log.e("IN u_id", "KEY: " + receiver_id);
            if (session.isUserLogin()) {
               // if (session.getUserID().equalsIgnoreCase(receiver_id)) {
 if (!isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", data.toString());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    if (keyMessage.equalsIgnoreCase("You have a new message")) {
                      /*  Intent resultIntent = new Intent(getApplicationContext(), ChatingAct.class);
                        resultIntent.putExtra("receiver_id", data.getString("sender_id"));
                        resultIntent.putExtra("receiver_name", data.getString("first_name"));
                        resultIntent.putExtra("receiver_img", data.getString("image"));
                        resultIntent.putExtra("request_id", ""+data.getString("request_id"));


                        Log.e("MESSAGE "," >> "+data.getString("message"));
                        showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), "" + message, format, resultIntent, null);
*/

                    }
                    else if (keyMessage.equalsIgnoreCase("New booking request")) {
                        Intent resultIntent = new Intent(getApplicationContext(), MainTabActivity.class);

                        Log.e("MESSAGE "," >> "+data.getString("message"));
                        showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), "" + message, format, resultIntent, null);



                    }
                    else {
                      // if (data.getString("type").equalsIgnoreCase("Provider")){
                            Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                            resultIntent.putExtra("message", data.toString());
                            showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), ""  +getResources().getString(R.string.youhavenewmessage), format, resultIntent, null);

//                        }
//                        else {
//                            Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
//                            resultIntent.putExtra("message", data.toString());
//                            showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), "" + message, format, resultIntent, null);
//
//                        }

                    }


                }
                else {
//Booking request has been canceled by user


                    String msg = keyMessage;
                    if (keyMessage.equalsIgnoreCase("You have a new message")) {
                      /*  Intent resultIntent = new Intent(getApplicationContext(), ChatingAct.class);
                        resultIntent.putExtra("receiver_id", data.getString("sender_id"));
                        resultIntent.putExtra("receiver_name", data.getString("first_name"));
                        resultIntent.putExtra("receiver_img", data.getString("image"));
                        resultIntent.putExtra("request_id", ""+data.getString("request_id"));

                        showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), "" +message, format, resultIntent, null);
*/
                    }


                    else {
                     //   if (data.getString("type").equalsIgnoreCase("Provider")){
                            Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                            resultIntent.putExtra("message", data.toString());
                            showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), "" +getResources().getString(R.string.youhavenewmessage), format, resultIntent, null);

//                        }
//                        else {
//                            Intent resultIntent = new Intent(getApplicationContext(), MainTabActivity.class);
//                            resultIntent.putExtra("message", data.toString());
//                            showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), "" + message, format, resultIntent, null);
//
//                        }

                    }

                    //shared user detail
                }
                }



        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }
    }



    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent, String route_img) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, route_img);

    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("FirebaseToken","===>"+s);
    }
}
//TODO : ALL Done or kuch bhi karna h?are meko egister id kha milegi kis page me

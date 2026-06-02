package main.com.cineramamaps.tabactivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ScheduledExecutorService;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.activity.AllServicesList;
import main.com.cineramamaps.activity.FavouriteActivity;
import main.com.cineramamaps.activity.HomeActivity;
import main.com.cineramamaps.activity.MapsListActivity;
import main.com.cineramamaps.activity.OfferActivity_New;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.model.CartBean;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainTabActivity extends TabActivity {

    int WhichIndex = 0;

    private Boolean exit ;
    String scrsts="";

    public static String notification_data="",notification_unseen_count,cart_unseen_count="";
    TextView counter_cart,counter_message;
    ScheduledExecutorService scheduleTaskExecutor;

    SessionManager session;
    private String language = "";
    MyLanguageSession myLanguageSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        setContentView(R.layout.activity_main_tab);
        session= SessionManager.get(this);
        notification_unseen_count="";
        cart_unseen_count="";


        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.e("Get Notification >>", "NULL");
        } else {
            String message = bundle.getString("message");
            Log.e("Get Notification >>", "" + message);
            if (message == null || message.equalsIgnoreCase("") || message.equalsIgnoreCase("null")) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    String keys = jsonObject.getString("key").trim();
                    notification_data = message;


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


        Bundle extra = getIntent().getExtras();
        exit = false;
        if (extra == null) {


        } else {
            scrsts = extra.getString("scrsts");
            if (scrsts==null||scrsts.equalsIgnoreCase("")){
                if (extra.containsKey("WhichIndex")) {

                    WhichIndex = extra.getInt("WhichIndex", 0);
                }


            }
            else {
                if (scrsts.equalsIgnoreCase("signup")){
                    WhichIndex = extra.getInt("WhichIndex", 4);
                }
                if (scrsts.equalsIgnoreCase("auction")){
                    WhichIndex = extra.getInt("WhichIndex", 1);
                }

                if (extra.containsKey("WhichIndex")) {

                    WhichIndex = extra.getInt("WhichIndex", 0);
                }

            }

        }

        TabHost tabHost = getTabHost();

        TabHost.TabSpec homespec = tabHost.newTabSpec("Home");
        View tabIndicator1 = LayoutInflater.from(this).inflate(R.layout.tab_indicator_main, getTabWidget(), false);
        ((ImageView) tabIndicator1.findViewById(R.id.icon)).setImageResource(R.drawable.homedrawable);
        ((TextView) tabIndicator1.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.home));
        TextView counter = ((TextView) tabIndicator1.findViewById(R.id.reqcount));
       // LinearLayout tabitemmaillay = tabIndicator1.findViewById(R.id.tabitemmaillay);
       // tabitemmaillay.setBackgroundResource(R.drawable.tabdrawable);
        homespec.setIndicator(tabIndicator1);
        Intent Intent1 = new Intent(this, HomeActivity.class);
        homespec.setContent(Intent1);


        TabHost.TabSpec favourite = tabHost.newTabSpec("favourite");
        View contractsvv = LayoutInflater.from(this).inflate(R.layout.tab_indicator_main, getTabWidget(), false);
        ((ImageView) contractsvv.findViewById(R.id.icon)).setImageResource(R.drawable.favouritedrawable);
        ((TextView) contractsvv.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.mymaps));
        //  LinearLayout tabitemmaillays = tabIndicator1.findViewById(R.id.tabitemmaillay);
       // tabitemmaillays.setBackgroundResource(R.drawable.tabdrawable);
        favourite.setIndicator(contractsvv);

        Intent intentd;


        if (session.isUserLogin()){
            intentd = new Intent(this, FavouriteActivity.class);
        }
        else {
            intentd = new Intent(this, FavouriteActivity.class);
        }

        favourite.setContent(intentd);
        // Tab for OnSale



        // Tab for Profile
        TabHost.TabSpec cart = tabHost.newTabSpec("cart");
        View tabIndicator15 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        ((ImageView) tabIndicator15.findViewById(R.id.icon)).setImageResource(R.drawable.mycart);
        ((TextView) tabIndicator15.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.home));
        counter_cart = ((TextView) tabIndicator15.findViewById(R.id.reqcount));
        cart.setIndicator(tabIndicator15);
//        Intent Intent51;
//
//        if (session.isUserLogin()){
//            Intent51 = new Intent(this, MyCartActivity.class);
//        }
//        else {
//            Intent51 = new Intent(this, MyCartActivity.class);
//        }
//
//
//        Intent51.putExtra("type","EMPLOYEE");
//        cart.setContent(Intent51);


        // Tab for Profile
        TabHost.TabSpec myorders = tabHost.newTabSpec("SERVICE");
        View tabIndicator5 = LayoutInflater.from(this).inflate(R.layout.tab_indicator_main, getTabWidget(), false);
        ((ImageView) tabIndicator5.findViewById(R.id.icon)).setImageResource(R.drawable.orderdrawable);
        ((TextView) tabIndicator5.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.services));
        myorders.setIndicator(tabIndicator5);
        Intent Intent5;

        if (session.isUserLogin()){
           // Intent5 = new Intent(this, OfferActivity_New.class);
            Intent5 = new Intent(this, AllServicesList.class);
        }
        else {
            Intent5 = new Intent(this, AllServicesList.class);
        }


        Intent5.putExtra("type","EMPLOYEE");
        myorders.setContent(Intent5);

        // Tab for Profile
        TabHost.TabSpec profile = tabHost.newTabSpec("MAPS");
        View tabIndicator6 = LayoutInflater.from(this).inflate(R.layout.tab_indicator_main, getTabWidget(), false);
        ((ImageView) tabIndicator6.findViewById(R.id.icon)).setImageResource(R.drawable.profiledrawable);
        ((TextView) tabIndicator6.findViewById(R.id.tabtext)).setText(getResources().getString(R.string.maps));
        profile.setIndicator(tabIndicator6);
        Intent Intent6;

        if (session.isUserLogin()){
            Intent6 = new Intent(this, MapsListActivity.class);
        }
        else {
            Intent6 = new Intent(this, MapsListActivity.class);
        }


        Intent6.putExtra("type","EMPLOYEE");
        profile.setContent(Intent6);

        // Adding all TabSpec to TabHost
         tabHost.addTab(homespec);
        tabHost.addTab(profile);
        tabHost.addTab(myorders);
         tabHost.addTab(favourite);
        // tabHost.addTab(cart);






        tabHost.setCurrentTab(WhichIndex);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (scheduleTaskExecutor==null){

        }
        else {
            scheduleTaskExecutor.shutdown();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (scheduleTaskExecutor==null){

        }
        else {
            scheduleTaskExecutor.shutdown();
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
       // getCartData();

    }
    private void getCartData() {
        ApiCall.get().Create().getMyCart(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyCartResponse >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            CartBean successData = new Gson().fromJson(responseData, CartBean.class);
                            String cartcount = successData.getTotalCart();
                           String cart_total_amt = successData.getTotalAmount();
                            if (cart_total_amt!=null&&!cart_total_amt.equalsIgnoreCase("")) {
                                double tot = Double.parseDouble(cart_total_amt);
                                cart_total_amt = String.format("%.2f", tot);
                            }

                            if(!cartcount.equalsIgnoreCase("") && cartcount !=null && !cartcount.equalsIgnoreCase("null") && !cartcount.equalsIgnoreCase("0")) {
                                counter_cart.setText(cartcount);

                               // total_tv.setText(getResources().getString(R.string.total)+" ( Kr "+cart_total_amt+" )");
                                counter_cart.setVisibility(View.VISIBLE);
                              //  cartlay_botum.setVisibility(View.VISIBLE);
                            }else{
                                counter_cart.setText("0");
                                //total_tv.setText("");
                                counter_cart.setVisibility(View.GONE);
                              //  cartlay_botum.setVisibility(View.GONE);

                            }


                        }
                        else {
                           // cartcount ="";
                           // olditembranch ="";
                           // total_tv.setText("");
                            counter_cart.setText("0");
                            counter_cart.setVisibility(View.GONE);
                            //cartlay_botum.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();

                Log.e("TAG", t.toString());
            }
        });
    }


}

/*




 */
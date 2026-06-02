package main.com.cineramamaps.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.moyasar.android.sdk.PaymentConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.AppConstant;
import main.com.cineramamaps.Utils.NotificationUtils;
import main.com.cineramamaps.Utils.Preferences;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.CurrencyAdapter;
import main.com.cineramamaps.adapter.HorizontalGuidelineAdapter;
import main.com.cineramamaps.app.Config;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.CoverimageslideritemBinding;
import main.com.cineramamaps.databinding.LayoutCustmHomeMapBinding;
import main.com.cineramamaps.draweractivity.BaseActivity;
import main.com.cineramamaps.model.Bannerdata;
import main.com.cineramamaps.model.CategoryBean;
import main.com.cineramamaps.model.CurrencyModel;
import main.com.cineramamaps.model.GuidelineBean;
import main.com.cineramamaps.model.ItemBean;
import main.com.cineramamaps.model.MapBean;
import main.com.cineramamaps.model.MapBeanList;
import main.com.cineramamaps.model.ProviderBean;
import main.com.cineramamaps.model.ProviderBeanList;
import main.com.cineramamaps.model.ServiceBeanListNew;
import main.com.cineramamaps.model.ServiceBeanNew;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    HorizontalPagerAdapter horizontalpagerAdapter;
    String addressid = "",subscription_status="";
    TextView usernametv,useremailtv;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    CircleImageView userimage;
    private SessionManager session;
    private ProgressBar progressbar;
    private TextView seeallcat, seealldiscountfoods, seeallmagicbags, seeallrestaurant, seeallcategory;
    LinearLayout count_view;
    private RecyclerView restaurnatlistrecyler, pagerlistrecyler, magicbagsrecycler, categorylist, discountfoodsrecyler;
    private RelativeLayout searchlay;
    Location location_ar;
    public static double longitude = 0.0, latitude = 0.0;
    GPSTracker tracker;
    String address = "", lat = "", lon = "";
    ImageView filterbutton;
    HorizontalRestaurantAdapter horizontalRestaurantAdapter;
    String restid = "";
    public static String favcall = "";
    int pos = 0;
    private TextView norestfound, nomagicbagfoundtv, nodiscountfoodtv;
    private ShimmerFrameLayout shimmer_view_category, shimmer_view_restarant, shimmer_view_magic;
    LinearLayout addresslay;
    RelativeLayout addshedulelay;
    TextView addresstxt;
    String day_name = "";
    private ViewPager viewPager;
    CustomViewPager viewPager1;
    private LinearLayout sliderDots,sliderDots1, ll_Store, ll_Pick;
    private int dotscount,dotscount1;
    private ImageView[] dots;
    private ImageView[] dots1;
    ArrayList<Bannerdata> bannerlist = new ArrayList<>();
    List<ServiceBeanListNew> ServiceListNew = new ArrayList<>();
    List<ServiceBeanListNew> ServiceListNew1 = new ArrayList<>();
    private String language = "";
    MyLanguageSession myLanguageSession;
    Context mContext = this;
    LinearLayout llCurrency;
    TextView tvCurrencyHome;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    private int currentPage = 0;
    private boolean isUserTouching = false;











    @Override
    protected void onResume() {
        super.onResume();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        String oldLanguage = language;
        language = myLanguageSession.getLanguage();
        setDynmaicData();
        if (!oldLanguage.equals(language)) {
            finish();
            startActivity(getIntent());
        }
        if (!favcall.equalsIgnoreCase("")) {
            favcall = "";
            getMaps();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());

//        if (SetLocation.pickuplocation_str != null && !SetLocation.pickuplocation_str.equalsIgnoreCase("")) {
//            addresstxt.setText("" + SetLocation.pickuplocation_str);
//            address = "" + SetLocation.pickuplocation_str;
//            lat = "" + SetLocation.originlatlong.latitude;
//            lon = "" + SetLocation.originlatlong.longitude;
//            //  radius = SetLocation.radius;
//            SetLocation.pickuplocation_str = "";
////            getRestaurants();
////            getMagicFoods();
////            getDiscountFoods();
////            updateAddress();
//        }else{
//           / getProfile();
//        }
        if (!GetUserAddress.full.equalsIgnoreCase("")) {
            addresstxt.setText("" + GetUserAddress.full);
            addressid = "" + GetUserAddress.addressid;
            lat = "" + GetUserAddress.lat;
            lon = "" + GetUserAddress.lon;
            GetUserAddress.addressid = "";
            GetUserAddress.full = "";
            GetUserAddress.lat = "0.0";
            GetUserAddress.lon = "0.0";
            //  getTimeSlots();
        } else {
            session = SessionManager.get(HomeActivity.this);
            if (session.isUserLogin()) {
                usernametv.setText(session.getUserDetails().getFirstName() + " " + session.getUserDetails().getLastName());
                useremailtv.setText(getResources().getString(R.string.userid) + " " + session.getUserDetails().getId());

                if (session.getUserDetails().getImage() != null && !session.getUserDetails().getImage().equalsIgnoreCase("")) {
                    Picasso.get().load(session.getUserDetails().getImage()).placeholder(R.drawable.profile_ic).into(userimage);
                }
            }
        }
getProfile();
     //   getGuideline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        // scheduleTaskExecutor.shutdown();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        setContentView(R.layout.activity_home);
        usernametv = findViewById(R.id.usernametv);
        useremailtv = findViewById(R.id.useremailtv);
        userimage = findViewById(R.id.userimage);
        addresslay = findViewById(R.id.addresslay);
        addshedulelay = findViewById(R.id.addshedulelay);
        addresstxt = findViewById(R.id.addresstxt);
        seeallcat = findViewById(R.id.seeallcat);
        llCurrency = findViewById(R.id.llCurrency);
        tvCurrencyHome = findViewById(R.id.tvCurrencyHome);
        setDynmaicData();
//        FrameLayout contentFrameLayout;
//        contentFrameLayout = findViewById(R.id.contentFrame); //Remember this is the FrameLayout area within your activity_main.xml
//        getLayoutInflater().inflate(R.layout.activity_home, contentFrameLayout);
        viewPager = findViewById(R.id.viewPager);
       // viewPager.arrowScroll(View.TEXT_DIRECTION_FIRST_STRONG_LTR);
        sliderDots = findViewById(R.id.sliderDots);

        viewPager1 = findViewById(R.id.viewPager1);

        //viewPager1.arrowScroll(View.TEXT_DIRECTION_FIRST_STRONG_LTR);
        sliderDots1 = findViewById(R.id.sliderDots1);
        favcall = "";
        BindView();
        getCurrentLocation();
        clickevents();
        setAdapters();
        RelativeLayout notification_lay_tool = findViewById(R.id.notification_lay_tool);
        notification_lay_tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(ii);
            }
        });
        RelativeLayout chatbtn = findViewById(R.id.chatbtn);
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent ii = new Intent(HomeActivity.this, ChatActivty.class);
                Intent ii = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(ii);
            }
        });
        addresslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(HomeActivity.this, GetUserAddress.class);
                startActivity(ii);
            }
        });
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
        Date d1 = new Date();
        day_name = sdf1.format(d1);
        // getCategory();


        horizontalRestaurantAdapter = new HorizontalRestaurantAdapter(null, HomeActivity.this, session.IsEnglish1());
        restaurnatlistrecyler.setAdapter(horizontalRestaurantAdapter);
        horizontalRestaurantAdapter.notifyDataSetChanged();

        horizontalpagerAdapter = new HorizontalPagerAdapter(null, HomeActivity.this, session.IsEnglish1());
        pagerlistrecyler.setAdapter(horizontalpagerAdapter);
        horizontalpagerAdapter.notifyDataSetChanged();
        // getProfile();

        getMaps();

        getService();
        getBanner();
        getGuideline();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Log.e("Push Chat: ", "" + message);
                    JSONObject data = null;
                    try {
                        data = new JSONObject(message);
                        getProfile();
//                        String keyMessage = data.getString("key").trim();
//                        if (keyMessage.equalsIgnoreCase("You have a new message")) {
//                            Log.e("Push Chat Come: ", "True");
//
//                            new ChatDetailsActivity.MyConverSession().execute();
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void setAdapters() {

        HorizontalGuidelineAdapter horizontalAdapter = new HorizontalGuidelineAdapter(null, HomeActivity.this, session.IsEnglish1());
        categorylist.setAdapter(horizontalAdapter);


    }



    private void clickevents() {
        llCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencyopup();
            }
        });
        addshedulelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subscription_status.equalsIgnoreCase("Yes")) {
                    Intent i = new Intent(HomeActivity.this, TripScheduleListActivity.class);
                    startActivity(i);
                }else{
                    final Dialog dialogSts = new Dialog(HomeActivity.this);
                    dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogSts.setCancelable(false);
                    dialogSts.setContentView(R.layout.areyousure);
                    dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    RelativeLayout closepopup = dialogSts.findViewById(R.id.closepopup);
                    TextView yes_tv = dialogSts.findViewById(R.id.yes_tv);
                    TextView message_tv = dialogSts.findViewById(R.id.message_tv);
                    message_tv.setText(""+getResources().getString(R.string.forthisaddsheduleyouneedtosubscribe));
                    yes_tv.setText(""+getResources().getString(R.string.ok));

                    closepopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogSts.dismiss();


                        }
                    });
                    yes_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogSts.dismiss();


                        }
                    });
                    dialogSts.show();
                }
            }
        });
        searchlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, MapsListActivity.class);
                startActivity(i);
            }
        });
        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, FilterActivity.class);
                startActivity(i);
            }
        });
        seeallrestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, MapsListActivity.class);
                startActivity(i);
            }
        });

        seeallcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, GuidelineListActivity.class);
                startActivity(i);
            }
        });


        seeallcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, AllServicesList.class);
                startActivity(i);
            }
        });

    }

    private void BindView() {
        shimmer_view_category = findViewById(R.id.shimmer_view_category);
        shimmer_view_restarant = findViewById(R.id.shimmer_view_restarant);
        shimmer_view_magic = findViewById(R.id.shimmer_view_magic);


        filterbutton = findViewById(R.id.filterbutton);


        norestfound = findViewById(R.id.norestfound);
        nomagicbagfoundtv = findViewById(R.id.nomagicbagfoundtv);
        nodiscountfoodtv = findViewById(R.id.nodiscountfoodtv);

        //recycler
        pagerlistrecyler = findViewById(R.id.pagerlistrecyler);
        restaurnatlistrecyler = findViewById(R.id.restaurnatlistrecyler);
        magicbagsrecycler = findViewById(R.id.magicbagsrecycler);
        discountfoodsrecyler = findViewById(R.id.discountfoodsrecyler);
        categorylist = findViewById(R.id.categorylist);


        seeallrestaurant = findViewById(R.id.seeallrestaurant);
        seeallmagicbags = findViewById(R.id.seeallmagicbags);
        seealldiscountfoods = findViewById(R.id.seealldiscountfoods);
        seeallcategory = findViewById(R.id.seeallcategory);


        searchlay = findViewById(R.id.searchlay);
        progressbar = findViewById(R.id.progressbar);


        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        categorylist.setLayoutManager(horizontalLayoutManagaer);


        LinearLayoutManager horbar
                = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        restaurnatlistrecyler.setLayoutManager(horbar);

        LinearLayoutManager horbar1
                = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        pagerlistrecyler.setLayoutManager(horbar1);


//        LinearLayoutManager homeonlybarberd
//                = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
//        restaurnatlistrecyler.setLayoutManager(homeonlybarberd);

        LinearLayoutManager horrecsaloon
                = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        discountfoodsrecyler.setLayoutManager(horrecsaloon);
        session = SessionManager.get(HomeActivity.this);


    }


    private void getCategory() {
        shimmer_view_category.setVisibility(View.VISIBLE);
        shimmer_view_category.startShimmerAnimation();

        ApiCall.get().Create().getCategory(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                Log.e("AllFeeds  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            shimmer_view_category.setVisibility(View.GONE);
                            shimmer_view_category.stopShimmerAnimation();
//                            CategoryBean successData = new Gson().fromJson(responseData, CategoryBean.class);
//                            HorizontalGuidelineAdapter horizontalAdapter = new HorizontalGuidelineAdapter(successData.getResult(), HomeActivity.this, session.IsEnglish1());
//                            categorylist.setAdapter(horizontalAdapter);
                        } else {
                            shimmer_view_category.stopShimmerAnimation();
                            shimmer_view_category.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void getService() {
        progressbar.setVisibility(View.VISIBLE);
        shimmer_view_magic.setVisibility(View.VISIBLE);
        nomagicbagfoundtv.setVisibility(View.VISIBLE);
        shimmer_view_magic.startShimmerAnimation();
        ApiCall.get().Create().getServices(getProductParam("Magic Food")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                Log.e("GetProducts  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            nomagicbagfoundtv.setVisibility(View.GONE);
//                            shimmer_view_magic.stopShimmerAnimation();
//                            shimmer_view_magic.setVisibility(View.GONE);
                            ServiceBeanNew successData = new Gson().fromJson(responseData, ServiceBeanNew.class);
                            ServiceListNew = successData.getResult();
                            //Collections.reverse(ServiceListNew);
                            List<Bannerdata> servicebannerlist = new ArrayList<>();
                            servicebannerlist.clear();
                            Bannerdata b1 = new Bannerdata();
                            b1.setBannerid("" + ServiceListNew.get(0).getId());
                            b1.setBannername("" + ServiceListNew.get(0).getImage1());
                            servicebannerlist.add(b1);
                            Bannerdata b2 = new Bannerdata();
                            b2.setBannerid("" + ServiceListNew.get(0).getId());
                            b2.setBannername("" + ServiceListNew.get(0).getImage2());
                            servicebannerlist.add(b2);
                            Bannerdata b3 = new Bannerdata();
                            b3.setBannerid("" + ServiceListNew.get(0).getId());
                            b3.setBannername("" + ServiceListNew.get(0).getImage3());
                            servicebannerlist.add(b3);
                            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(HomeActivity.this, servicebannerlist);
                            viewPager.setAdapter(viewPagerAdapter);
//                            horizontalpagerAdapter = new HorizontalPagerAdapter(servicebannerlist, HomeActivity.this, session.IsEnglish1());
//                            pagerlistrecyler.setAdapter(horizontalpagerAdapter);
//                            horizontalpagerAdapter.notifyDataSetChanged();
                            dotscount = viewPagerAdapter.getCount();
                            dots = new ImageView[dotscount];

                            for (int i = 0; i < dotscount; i++) {
                                try {

                                    dots[i] = new ImageView(HomeActivity.this);
                                    dots[i].setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.non_active_dot));
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(8, 0, 8, 0);
                                    sliderDots.addView(dots[i], params);

                                    dots[0].setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.active));

                                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                        @Override
                                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                        }

                                        @Override
                                        public void onPageSelected(int position) {

                                            for (int i = 0; i < dotscount; i++) {
                                                try {
                                                    dots[i].setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.non_active_dot));
                                                } catch (Exception e) {
//                                e.printStackTrace();
                                                }
                                            }
                                            try {
                                                dots[position].setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.active));
                                            } catch (Exception e) {
                                            }
                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int state) {

                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

//                            Timer timer = new Timer();
//                            timer.scheduleAtFixedRate(new SliderTimer_one(), 6000, 6000);
                        } else {
                            nomagicbagfoundtv.setVisibility(View.VISIBLE);
                            shimmer_view_magic.stopShimmerAnimation();
                            shimmer_view_magic.setVisibility(View.GONE);

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getBanner() {
        progressbar.setVisibility(View.VISIBLE);
        ServiceListNew1 = new ArrayList<>();
        ServiceListNew1.clear();

        ApiCall.get().Create().get_Banner(getProductParam("Magic Food")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                Log.e("GetProducts  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            ServiceBeanNew successData = new Gson().fromJson(responseData, ServiceBeanNew.class);
                            ServiceListNew1.addAll(successData.getResult());
                            List<Bannerdata> servicebannerlist = new ArrayList<>();
                            servicebannerlist.clear();
                            if(ServiceListNew1.size()>0){
                                for(int j=0;j<ServiceListNew1.size();j++){
                                    Bannerdata b1 = new Bannerdata();
                                    b1.setBannerid("" + ServiceListNew1.get(j).getId());
                                    b1.setBannername("" + ServiceListNew1.get(j).getImage());
                                    servicebannerlist.add(b1);
                                }
                                // ADAPTER

                                ViewPagerAdapter1 viewPagerAdapter1 =
                                        new ViewPagerAdapter1(HomeActivity.this, servicebannerlist);

                                viewPager1.setAdapter(viewPagerAdapter1);

                                if (language.equals("ar")) {
                                    viewPager1.setRotation(180);
                                }


// DOTS

                                dotscount1 = viewPagerAdapter1.getCount();

                                dots1 = new ImageView[dotscount1];

                                sliderDots1.removeAllViews();

                                for (int i = 0; i < dotscount1; i++) {

                                    dots1[i] = new ImageView(HomeActivity.this);

                                    dots1[i].setImageDrawable(
                                            ContextCompat.getDrawable(
                                                    HomeActivity.this,
                                                    R.drawable.non_active_dot));

                                    LinearLayout.LayoutParams params =
                                            new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                                    params.setMargins(8, 0, 8, 0);

                                    sliderDots1.addView(dots1[i], params);
                                }


// FIRST DOT ACTIVE

                                if (dotscount1 > 0) {

                                    dots1[0].setImageDrawable(
                                            ContextCompat.getDrawable(
                                                    HomeActivity.this,
                                                    R.drawable.active));
                                }


// PAGE CHANGE

                                viewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                    @Override
                                    public void onPageScrolled(int position,
                                                               float positionOffset,
                                                               int positionOffsetPixels) {

                                    }

                                    @Override
                                    public void onPageSelected(int position) {

                                        currentPage = position;

                                        for (int i = 0; i < dotscount1; i++) {

                                            dots1[i].setImageDrawable(
                                                    ContextCompat.getDrawable(
                                                            HomeActivity.this,
                                                            R.drawable.non_active_dot));
                                        }

                                        dots1[position].setImageDrawable(
                                                ContextCompat.getDrawable(
                                                        HomeActivity.this,
                                                        R.drawable.active));
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                        if (state == ViewPager.SCROLL_STATE_DRAGGING) {

                                            // USER START TOUCH
                                            sliderHandler.removeCallbacks(sliderRunnable);
                                        }

                                        else if (state == ViewPager.SCROLL_STATE_IDLE) {

                                            // RESTART AFTER TOUCH RELEASE
                                            sliderHandler.removeCallbacks(sliderRunnable);

                                            sliderHandler.postDelayed(sliderRunnable, 5000);
                                        }
                                    }
                                });


// AUTO SCROLL

                                sliderRunnable = new Runnable() {
                                    @Override
                                    public void run() {

                                        currentPage++;

                                        if (currentPage >= dotscount1) {
                                            currentPage = 0;
                                        }

                                        viewPager1.setCurrentItem(currentPage, true);

                                        sliderHandler.postDelayed(this, 5000);
                                    }
                                };


// START AUTO SCROLL

                                sliderHandler.postDelayed(sliderRunnable, 5000);



// TOUCH DETECT


//

//                                Timer timer = new Timer();
//                                timer.scheduleAtFixedRate(new SliderTimer_one(), 6000, 6000);
                            }




                        } else {
                            nomagicbagfoundtv.setVisibility(View.VISIBLE);
                            shimmer_view_magic.stopShimmerAnimation();
                            shimmer_view_magic.setVisibility(View.GONE);

                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        });
    }



    protected void onDestroy() {
        super.onDestroy();

        sliderHandler.removeCallbacks(sliderRunnable);
    }
    private void getMaps() {
        shimmer_view_restarant.setVisibility(View.VISIBLE);
        shimmer_view_restarant.startShimmerAnimation();
        progressbar.setVisibility(View.VISIBLE);
        norestfound.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getMaplist(session.getUserID(), "", "All").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                Log.e("Restaurants  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            norestfound.setVisibility(View.GONE);
                            shimmer_view_restarant.stopShimmerAnimation();
                            shimmer_view_restarant.setVisibility(View.GONE);
                            MapBean successData = new Gson().fromJson(responseData, MapBean.class);
                            horizontalRestaurantAdapter = new HorizontalRestaurantAdapter(successData.getResult(), HomeActivity.this, session.IsEnglish1());
                            restaurnatlistrecyler.setAdapter(horizontalRestaurantAdapter);
                            horizontalRestaurantAdapter.notifyDataSetChanged();


                        } else {
                            norestfound.setVisibility(View.VISIBLE);
                            shimmer_view_restarant.stopShimmerAnimation();
                            shimmer_view_restarant.setVisibility(View.GONE);
                            horizontalRestaurantAdapter = new HorizontalRestaurantAdapter(null, HomeActivity.this, session.IsEnglish1());
                            restaurnatlistrecyler.setAdapter(horizontalRestaurantAdapter);


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getGuideline() {

        HashMap<String, String> params = new HashMap<>();



        params.put("user_id", session.getUserID());


        shimmer_view_category.setVisibility(View.VISIBLE);
        shimmer_view_category.startShimmerAnimation();
        progressbar.setVisibility(View.VISIBLE);
        //  norestfound.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getGuidelinelistnew(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                Log.e("Restaurants  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        Log.d("guidlineresponse", "onResponse: " + responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            norestfound.setVisibility(View.GONE);
                            shimmer_view_category.stopShimmerAnimation();
                            shimmer_view_category.setVisibility(View.GONE);
                            GuidelineBean successData = new Gson().fromJson(responseData, GuidelineBean.class);
                            HorizontalGuidelineAdapter horizontalAdapter = new HorizontalGuidelineAdapter(successData.getResult(), HomeActivity.this, session.IsEnglish1());
                            categorylist.setAdapter(horizontalAdapter);

                        } else {
//                            norestfound.setVisibility(View.VISIBLE);
                            shimmer_view_category.stopShimmerAnimation();
                            shimmer_view_category.setVisibility(View.GONE);
//                            horizontalRestaurantAdapter = new HorizontalRestaurantAdapter(null, HomeActivity.this, session.IsEnglish1());
//                            restaurnatlistrecyler.setAdapter(horizontalRestaurantAdapter);


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getCurrentLocation() {
        tracker = new GPSTracker(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (location_ar != null) {
            //Getting longitude and latitude
            longitude = location_ar.getLongitude();
            latitude = location_ar.getLatitude();
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + latitude);


        } else {
            System.out.println("----------------geting Location from GPS----------------");

            location_ar = tracker.getLocation();
            if (location_ar == null) {


            } else {
                longitude = location_ar.getLongitude();
                latitude = location_ar.getLatitude();
                Log.e("Lat >>", "GPS " + latitude);

            }
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + longitude);
            //moving the map to location

        }
    }

    private HashMap<String, String> getProductParam(String type) {


        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("type", "");
        // param.put("token",session.getUserDetails().getToken());
        param.put("country_id", "");
        param.put("city_id", "");


        return param;

    }


    public class HorizontalRestaurantAdapter extends RecyclerView.Adapter<HorizontalRestaurantAdapter.ViewHolder> {

        private ArrayList<Bitmap> horizontalList;
        private List<MapBeanList> mapBeanLists;
        boolean b;


        public HorizontalRestaurantAdapter(List<MapBeanList> mapBeanLists, Activity context, boolean b) {

            this.b = b;
            this.mapBeanLists = mapBeanLists;
        }


        @NonNull
        @Override
        public HorizontalRestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutCustmHomeMapBinding binding = DataBindingUtil.inflate(LayoutInflater.from(HomeActivity.this), R.layout.layout_custm_home_map, parent, false);
            return new HorizontalRestaurantAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull HorizontalRestaurantAdapter.ViewHolder holder, int position) {
            if (language.equalsIgnoreCase("en")) {
                holder.binding.usernametv.setText(mapBeanLists.get(position).getName());
            } else {
                holder.binding.usernametv.setText(mapBeanLists.get(position).getNameAr());
            }

            if (mapBeanLists.get(position).getImage() != null && !mapBeanLists.get(position).getImage().isEmpty()) {
                Picasso.get().load(mapBeanLists.get(position).getImage()).placeholder(R.color.lightgrey).into(holder.binding.mapimage);
            }
            if (mapBeanLists.get(position).getCountryIcon() != null && !mapBeanLists.get(position).getCountryIcon().isEmpty()) {
                Picasso.get().load(mapBeanLists.get(position).getCountryIcon()).placeholder(R.color.lightgrey).into(holder.binding.countryimage);
            }
//
//            if (providerBeanLists.get(position).getFav_provider().equalsIgnoreCase("Yes")){
//                holder.binding.favic.setBackgroundResource(R.drawable.fav_selected);
//            }else{
//                holder.binding.favic.setBackgroundResource(R.drawable.fav);
//            }
//
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // if(mapBeanLists.get(position).getSubscription_status().equalsIgnoreCase("Yes")){
                    Intent i = new Intent(HomeActivity.this, SubMapsList.class);
                    i.putExtra("country_id", mapBeanLists.get(position).getId());
                    i.putExtra("country_name", mapBeanLists.get(position).getName());
                    i.putExtra("country_name_ar", mapBeanLists.get(position).getNameAr());
                    startActivity(i);
//                    }else{
//                        final Dialog dialogSts = new Dialog(HomeActivity.this);
//                        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialogSts.setCancelable(false);
//                        dialogSts.setContentView(R.layout.areyousure);
//                        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        RelativeLayout closepopup = dialogSts.findViewById(R.id.closepopup);
//                        TextView yes_tv = dialogSts.findViewById(R.id.yes_tv);
//                        TextView message_tv = dialogSts.findViewById(R.id.message_tv);
//                        message_tv.setText(""+getResources().getString(R.string.youhavenosubscriptionplan));
//
//                        closepopup.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogSts.dismiss();
//
//                            }
//                        });
//                        yes_tv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialogSts.dismiss();
//                                Intent i = new Intent(HomeActivity.this, SubscriptionPlanActivity.class);
//                                i.putExtra("country_id",mapBeanLists.get(position).getId());
//                                i.putExtra("city_id","");
//                                startActivity(i);
//                            }
//                        });
//                        dialogSts.show();
//                    }
                }
            });
//            holder.binding.favic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    restid = providerBeanLists.get(position).getId();
//                    pos = position;
//                    if (providerBeanLists.get(position).getFav_provider().equalsIgnoreCase("Yes")){
//                        providerBeanLists.get(position).setFav_provider("No");
//                    }else{
//                        providerBeanLists.get(position).setFav_provider("Yes");
//                    }
//                    horizontalRestaurantAdapter.notifyDataSetChanged();
//                    addTofavCall();
//                }
//            });

        }


        class ViewHolder extends RecyclerView.ViewHolder {
            LayoutCustmHomeMapBinding binding;

            public ViewHolder(@NonNull LayoutCustmHomeMapBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }


        @Override
        public int getItemCount() {
            // return 5;
            return mapBeanLists == null ? 0 : mapBeanLists.size();

        }

        private HashMap<String, String> getFavParam() {


            HashMap<String, String> param = new HashMap<>();
            param.put("user_id", session.getUserID());
            param.put("provider_id", restid);

            return param;

        }

        private void addTofavCall() {


            ApiCall.get().Create().addTofav1(getFavParam()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    progressbar.setVisibility(View.GONE);
                    Log.e("AddToCart ", " >> " + response);

                    if (response.isSuccessful()) {

                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            Log.e("AddToFav Response ", " >> " + responseData);
                            if (object.getString("status").equalsIgnoreCase("1")) {


                            } else {
                                Toast.makeText(HomeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(HomeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(HomeActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getProfile() {

        ApiCall.get().Create().getProfile(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyProfileDetail >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            JSONObject jsonObject1 = object.getJSONObject("result");
                          String  noti_count = jsonObject1.getString("noti_count");
                          TextView notification_count = findViewById(R.id.notification_count);
                          if(noti_count.equalsIgnoreCase("") || noti_count.equalsIgnoreCase("0") ){
                              notification_count.setText(""+noti_count);
                              notification_count.setVisibility(View.GONE);
                          }else{
                              notification_count.setText(""+noti_count);
                              notification_count.setVisibility(View.VISIBLE);
                          }
                            subscription_status = jsonObject1.getString("subscription_status");

//                            getRestaurants();
//                            getMagicFoods();
//                            getDiscountFoods();

//                            String noti_count = jsonObject1.getString("noti_count");
//                            if (noti_count==null||noti_count.equalsIgnoreCase("")||noti_count.equalsIgnoreCase("0")){
//                                notification_count.setVisibility(View.GONE);
//                            }
//                            else {
//                                notification_count.setVisibility(View.VISIBLE);
//                                notification_count.setText(noti_count);
//                            }

                        } else {
                            session.Logout();
                            Intent i = new Intent(HomeActivity.this, LoginAct.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void updateAddress() {
        //progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().updateAddress(getParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("Give Rating >", " >" + response);
                //  binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
//                            Toast.makeText(getApplicationContext(), "" + getResources().getString(R.string.submittedsuccessfully),
//                                    Toast.LENGTH_SHORT).show();
//                            finish();

                        } else {
//                            Toast.makeText(getApplicationContext(), "" + object.getString("message"),
//                                    Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed

                t.printStackTrace();
                //  binding.progressbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }

    private HashMap<String, String> getParam() {

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("address", "" + address);
        param.put("lat", "" + lat);
        param.put("lon", "" + lon);
        return param;


    }


    public void setDynmaicData(){
        tvCurrencyHome.setText(Preferences.get(mContext,Preferences.KEY_CURRENCY));
    }


    void currencyopup() {
        try {
            final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.getWindow().getAttributes().windowAnimations = R.style.Animations_LoadingDialogFade;
            dialog.setContentView(R.layout.dialog_currency_list);

             RecyclerView recyclerList = dialog.findViewById(R.id.recyclerList);
            recyclerList.setHasFixedSize(true);
            //  recyclerview_shortMessage.setLayoutManager(layoutManager);
            recyclerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            ArrayList<CurrencyModel> mCurrencyModel = AppConstant.loadCurrencyFromAssets(mContext);
            CurrencyAdapter currencyAdapter = new CurrencyAdapter(this,mCurrencyModel,dialog);
            recyclerList.setAdapter(currencyAdapter);


            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public class HorizontalPagerAdapter extends RecyclerView.Adapter<HorizontalPagerAdapter.ViewHolder> {

        private ArrayList<Bitmap> horizontalList;
        private List<Bannerdata> mapBeanLists;
        boolean b;


        public HorizontalPagerAdapter(List<Bannerdata> mapBeanLists, Activity context, boolean b) {

            this.b = b;
            this.mapBeanLists = mapBeanLists;
        }


        @NonNull
        @Override
        public HorizontalPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CoverimageslideritemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(HomeActivity.this), R.layout.coverimageslideritem, parent, false);
            return new HorizontalPagerAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull HorizontalPagerAdapter.ViewHolder holder, int position) {


            holder.binding.mainlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ii = new Intent(HomeActivity.this, ServiceDetailActivity.class);
                    ii.putExtra("id",""+mapBeanLists.get(position).getBannerid());
                    startActivity(ii);
                }
            });

            Picasso.get().load(mapBeanLists.get(position).getBannername()).placeholder(R.color.lightgrey).into(holder.binding.imageView);

        }


        class ViewHolder extends RecyclerView.ViewHolder {
            CoverimageslideritemBinding binding;

            public ViewHolder(@NonNull CoverimageslideritemBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }


        @Override
        public int getItemCount() {
            // return 5;
            return mapBeanLists == null ? 0 : mapBeanLists.size();

        }

    }

    public class ViewPagerAdapter1 extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Bannerdata> images;

        public ViewPagerAdapter1(Context context, List<Bannerdata> images) {
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            // return 3;
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.coverimageslideritem, null);
            final RelativeLayout mainlay = (RelativeLayout) view.findViewById(R.id.mainlay);
            final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress);
            progress.setVisibility(View.GONE);
            mainlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ii = new Intent(context, BannerDetailActivity.class);
                    ii.putExtra("id",""+ServiceListNew1.get(position).getId());
                    ii.putExtra("image",""+ServiceListNew1.get(position).getImage());
                    ii.putExtra("title",""+ServiceListNew1.get(position).getTitle());
                    ii.putExtra("title_ar",""+ServiceListNew1.get(position).getTitle_ar());
                    ii.putExtra("description",""+ServiceListNew1.get(position).getDescription());
                    ii.putExtra("description_ar",""+ServiceListNew1.get(position).getDescriptionAr());
                    context.startActivity(ii);
                }
            });
            if(language.equals("ar")){
                imageView.setRotation(180);
            }
            Picasso.get().load(images.get(position).getBannername()).placeholder(R.color.lightgrey).into(imageView);

//        Toast.makeText(context, String.valueOf(images.size()), Toast.LENGTH_SHORT).show();

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(images.get(position).getLink()));
//                    context.startActivity(i);
//                }catch(Exception e){
//                    e.printStackTrace();
//                    Toast.makeText(context,"Link not valid.", Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });
//if(position == 0){
//    imageView.setImageResource(R.drawable.banner1);
//}
//        if(position == 1){
//            imageView.setImageResource(R.drawable.banner2);
//        }
//        if(position == 2){
//            imageView.setImageResource(R.drawable.banner1);
//        }
//        if(position == 3){
//            imageView.setImageResource(R.drawable.service4);
//        }


            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }
    }
}
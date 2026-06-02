package main.com.cineramamaps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.AppConstant;
import main.com.cineramamaps.Utils.Preferences;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.CurrencyAdapter;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityMapDetailsBinding;
import main.com.cineramamaps.databinding.ActivitySubMapsListBinding;
import main.com.cineramamaps.draweractivity.BaseActivity;
import main.com.cineramamaps.model.AllProductList;
import main.com.cineramamaps.model.CityMapBean;
import main.com.cineramamaps.model.CurrencyModel;
import main.com.cineramamaps.model.ImageSlideBean;
import main.com.cineramamaps.model.ImageSlideBeanList;
import main.com.cineramamaps.model.PlacesBean;
import main.com.cineramamaps.model.PlacesImage;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Locale;
public class MapDetailsActivity extends AppCompatActivity {
    ViewPagerAdapter adapter;

    LinearLayoutManager layoutManager ;
    RecyclerItemAdapter1 adapter1;
    Context mContext = this;
    int days;
    ActivityMapDetailsBinding binding;
    public static String currency="",currency_ar="";
    String   cityprice="",citymonth="", cityfav="", cityid="",cityname="",cityname_ar="",cityimage="",cityrating="",cityaddress="",type = "Restaurant",restid="",productid="",day_name="";
    public static  String citylat="",citylon="",youtubelink="",youtubelink_arabic="";
    SessionManager session;
    ArrayList<AllProductList> alllistdata =  new ArrayList<AllProductList>();
    private  String language = "",country_id="";
  static   public String plan_purcahse_status="";
    MyLanguageSession myLanguageSession;
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
        getPurchasedetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_details);
        session = SessionManager.get(MapDetailsActivity.this);
        citylat="";
        citylon="";
        SubMapsList.favcall="";
        plan_purcahse_status = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
        Date d1 = new Date();
        day_name = sdf1.format(d1);
        Bundle b = getIntent().getExtras();
        youtubelink = b.getString("youtubelink");
        youtubelink_arabic = b.getString("youtubelink_arabic");
        country_id = b.getString("country_id");
        cityid = b.getString("city_id");
        cityfav = b.getString("fav_status");
        cityname = b.getString("city_name");
        cityname_ar = b.getString("city_name_ar");
        cityimage = b.getString("city_image");
        cityaddress = b.getString("city_address");
        citylat = b.getString("city_lat");
        citylon = b.getString("city_lon");
        cityrating = b.getString("city_rating");
        cityprice = b.getString("city_price");
        citymonth = b.getString("city_month");
        currency = b.getString("currency");
        currency_ar = b.getString("currency_ar");
        int cityMonth = Integer.parseInt(citymonth);  // 12
        days= (int) (cityMonth * 30.42);
        Log.d("citymonth","citymonth = "+days);
        if (language.equals("ar")){
            binding.cityname1.setText(cityname_ar);
            binding.nametv.setText(cityname_ar);
        }else {
            binding.cityname1.setText(cityname);
            binding.nametv.setText(cityname);
        }

        binding.ratingtv.setText(cityrating);
        binding.addresstv.setText(cityaddress);
//         gridLayoutManager = new GridLayoutManager(MapDetailsActivity.this, 1);
//            gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
//            binding.providerrecyclerview.setLayoutManager(gridLayoutManager);
         layoutManager =
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false);

        binding.providerrecyclerview.setLayoutManager(layoutManager);

// set LayoutManager to RecyclerView
//        LinearLayoutManager layoutManager =
//                new LinearLayoutManager(this,
//                        LinearLayoutManager.HORIZONTAL,
//                        false);
//
//        binding.providerrecyclerview.setLayoutManager(layoutManager);
//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(binding.providerrecyclerview);
        getCountryMaps();
         setDynmaicData();

         binding.llCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencyopup();
            }
        });

        binding.ratingbar.setRating(Float.parseFloat(""+cityrating));
        if (cityimage != null && !cityimage.isEmpty()) {
            Picasso.get().load(cityimage).placeholder(R.color.lightgrey).into(binding.cityimage);
        }
        binding.favic.setVisibility(View.VISIBLE);
        if(cityfav.equalsIgnoreCase("Yes")){
           binding.favicimg.setBackgroundResource(R.drawable.favselected);
        }else{
           binding.favicimg.setBackgroundResource(R.drawable.favunselected);
        }
        binding.favic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cityfav.equalsIgnoreCase("Yes")) {
                    cityfav="No";
                    binding.favicimg.setBackgroundResource(R.drawable.favunselected);
                }else {
                   cityfav="Yes";
                    binding.favicimg.setBackgroundResource(R.drawable.favselected);
                }

                addTofavCall();
            }
        });
        clickevent();
         adapter = new ViewPagerAdapter(getSupportFragmentManager());
        System.out.println("sssssss22 "+alllistdata.size());
        // for(int j=0;j<alllistdata.size();j++) {
//        for(int j=0;j<4;j++) {
//
//            //  adapter.addFragment(new ProductLisFragment(alllistdata.get(j).getMenulist(),alllistdata.get(j).getImage()), alllistdata.get(j).getCategoryname());
//           if(j==0) {
//               adapter.addFragment(new AboutFragment(null, "aa",cityid), ""+getResources().getString(R.string.aboutthemap));
//               binding.viewPager.setAdapter(adapter);
//               binding.tabLayout.setupWithViewPager(binding.viewPager);
//           }
//           else if(j==1) {
//               adapter.addFragment(new PlacesFragment(null, "aa",cityid), ""+getResources().getString(R.string.places));
//               binding.viewPager.setAdapter(adapter);
//               binding.tabLayout.setupWithViewPager(binding.viewPager);
//           }
//           else if(j==2) {
//               adapter.addFragment(new ReviewFragment(null, "aa",cityid), ""+getResources().getString(R.string.reviews));
//               binding.viewPager.setAdapter(adapter);
//               binding.tabLayout.setupWithViewPager(binding.viewPager);
//           }
//           else if(j==3) {
//               adapter.addFragment(new ImagesFragment(null, "aa",cityid), ""+getResources().getString(R.string.images));
//               binding.viewPager.setAdapter(adapter);
//               binding.tabLayout.setupWithViewPager(binding.viewPager);
//           }
//
//
//
//        }
        adapter.addFragment(new AboutFragment(null, "aa", cityid), "" + getResources().getString(R.string.aboutthemap));
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        adapter.addFragment(new PlacesFragment(null, "aa", cityid), "" + getResources().getString(R.string.places));
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        adapter.addFragment(new ReviewFragment(null, "aa", cityid), "" + getResources().getString(R.string.reviews));
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        adapter.addFragment(new ImagesFragment(null, "aa", cityid), "" + getResources().getString(R.string.video));
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
//                           }else{
//                               adapter.addFragment(new AboutFragment(null, "aa", cityid), "" + getResources().getString(R.string.aboutthemap));
//                               binding.viewPager.setAdapter(adapter);
//                               binding.tabLayout.setupWithViewPager(binding.viewPager);
//                           }
        adapter.notifyDataSetChanged();


    }


    public void setDynmaicData() {

        binding.tvCurrencyMap.setText(Preferences.get(mContext, Preferences.KEY_CURRENCY));
        System.out.println("ssssssssssssssssssssss = "+binding.tvCurrencyMap.getText().toString());
        double mCityPrice = Double.parseDouble(cityprice);
        double priceToSar = mCityPrice * AppConstant.getExchangeValue(this);
      //  String finalRoundOffValue = String.format("%.2f", priceToSar);
        String finalRoundOffValue =
                String.format(Locale.ENGLISH, "%.2f", priceToSar);
        String dynamicPrice;

        if (language.equals("ar")) {
            // Arabic sentence structure
            dynamicPrice = finalRoundOffValue + " " + Preferences.get(mContext, Preferences.KEY_CURRENCY) + " " +
                    getResources().getString(R.string.for1) + " " +
                    citymonth + " " + getResources().getString(R.string.month);
        } else {
            // English sentence structure
            dynamicPrice = finalRoundOffValue + " " + Preferences.get(mContext, Preferences.KEY_CURRENCY) + " " +
                    getResources().getString(R.string.for1) + " " +
                    citymonth + " " + getResources().getString(R.string.month);
        }

        Log.e("mearge", "msg" + dynamicPrice);
        binding.pricetxt.setText(dynamicPrice);
    }

    private HashMap<String, String> getFavParam() {


        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("city_id", cityid);
        return param;

    }
    private void addTofavCall() {



        ApiCall.get().Create().addTofav1(getFavParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("AddToCart ", " >> " + response);

                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("AddToFav Response ", " >> " + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                           SubMapsList.favcall="Call";

                        } else {
                            Toast.makeText(MapDetailsActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MapDetailsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(MapDetailsActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.subscribebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent ii = new Intent(MapDetailsActivity.this,SubscriptionPlanActivity.class);
//                ii.putExtra("country_id",country_id);
//                ii.putExtra("city_id",cityid);
//                startActivity(ii);

                Intent i = new Intent(MapDetailsActivity.this, ConfirmPayment.class);
                i.putExtra("country_id",country_id);
                i.putExtra("city_id",cityid);
                i.putExtra("type",cityname);
                i.putExtra("duration",citymonth);
                i.putExtra("order_id", ""+cityid);
                i.putExtra("come_from", "order");
                i.putExtra("paid_amount", ""+cityprice);
                //   i.putExtra("type","USER");
                startActivity(i);
            }
        });

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



    private void getPurchasedetails() {
//binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getPurchaseDetails(session.getUserID(),cityid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("sssssssssssssss = "+response);
binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyProfileDetail >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            plan_purcahse_status = object.getString("plan_purcahse_status");
                            System.out.println("ssssssssssssssssss "+plan_purcahse_status);
                            if(plan_purcahse_status.equalsIgnoreCase("Yes")){
                                binding.subscribebut.setVisibility(View.GONE);
                               // Toast.makeText(MapDetailsActivity.this,""+getResources().getString(R.string.alreadyhaveplan),Toast.LENGTH_LONG).show();
                            }else{
                                binding.subscribebut.setVisibility(View.VISIBLE);

//                        final Dialog dialogSts = new Dialog(MapDetailsActivity.this);
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
//                                Intent i = new Intent(MapDetailsActivity.this, SubscriptionPlanActivity.class);
//                                i.putExtra("country_id",country_id);
//                                i.putExtra("city_id",""+cityid);
//                                startActivity(i);
//                            }
//                        });
//                        dialogSts.show();
//
                            }
                          // if(plan_purcahse_status.equalsIgnoreCase("Yes")) {


                           // JSONObject jsonObject1 = object.getJSONObject("result");

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

                        }
                        else {


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


    class RecyclerItemAdapter1 extends RecyclerView.Adapter<RecyclerItemAdapter1.ViewHolder> {

        private List<ImageSlideBeanList> friends;
        private Activity activity;
        private int pos;

        public RecyclerItemAdapter1(Activity activity, List<ImageSlideBeanList> friends, int pos) {
            this.friends = friends;
            this.activity = activity;
            this.pos = pos;
        }

        @Override
        public RecyclerItemAdapter1.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            //inflate your layout and pass it to view holder
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.imageslideritemlay, viewGroup, false);
            RecyclerItemAdapter1.ViewHolder viewHolder = new RecyclerItemAdapter1.ViewHolder(view);
            return viewHolder;
        }

        public void filter(String charText) {
//            Log.e("COMEADDD","DDDDDDD");
//            //charText = charText.toLowerCase(Locale.getDefault());
//            charText = charText.toString().toLowerCase();
//            menulist.clear();
//            if (charText.length() == 0) {
//                menulist.addAll(searchbuymessageBeanListArrayList);
//                System.out.println("sssssssssssssssss 11"+menulist.size());
//                System.out.println("sssssssssssssssss 22"+searchbuymessageBeanListArrayList.size());
//            } else {
//                for (CompanyOffer wp : searchbuymessageBeanListArrayList) {
//                    if (wp.getCompanyName().toLowerCase().contains(charText) || wp.getCompanyNameAr().toLowerCase().contains(charText)|| wp.getDiscountCode().toLowerCase().contains(charText))//.toLowerCase(Locale.getDefault())
//                    {
//                        Log.e("COMEADDD","DDD");
//                        menulist.add(wp);
//                    }
//                }
//                if (menulist==null||menulist.isEmpty()){
//                    //nodatatxt.setVisibility(View.VISIBLE);
//                    // noitemtv.setText(""+getResources().getString(R.string.noitmewithsearch));
//                }
//                else {
//                    // nodatatxt.setVisibility(View.GONE);
//                }
//            }
//            notifyDataSetChanged();
        }



        @Override
        public void onBindViewHolder(final RecyclerItemAdapter1.ViewHolder viewHolder, final int position) {


//        viewHolder.submit.setText(menulist.get(position).getDiscountCode()+" "+menulist.get(position).getDiscountPercentage()+""+""+getResources().getString(R.string.percentdiscount));
//           // viewHolder.datetv.setText(""+menulist.get(position).getDescription());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                viewHolder.datetv.setText(Html.fromHtml(""+menulist.get(position).getDescription(), Html.FROM_HTML_MODE_COMPACT));
//            } else {
//                viewHolder.datetv.setText(Html.fromHtml(""+menulist.get(position).getDescription()));
//            }
            if (!friends.get(position).getImage().equalsIgnoreCase("")) {
                Picasso.get().load(friends.get(position).getImage()).into(viewHolder.catimage);
            }
            viewHolder.shimmerLayout.setVisibility(View.VISIBLE);
            viewHolder.shimmerLayout.startShimmerAnimation();

            if (friends.get(position).getImage() != null
                    && !friends.get(position).getImage().equalsIgnoreCase("")) {

                Picasso.get()
                        .load(friends.get(position).getImage())
                        .placeholder(R.color.lightgrey)
                        .error(R.color.lightgrey)
                        .into(viewHolder.catimage,
                                new com.squareup.picasso.Callback() {

                                    @Override
                                    public void onSuccess() {

                                        viewHolder.shimmerLayout.stopShimmerAnimation();
                                        viewHolder.shimmerLayout.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                        viewHolder.shimmerLayout.stopShimmerAnimation();
                                        viewHolder.shimmerLayout.setVisibility(View.GONE);
                                    }
                                });

            } else {

                viewHolder.shimmerLayout.stopShimmerAnimation();
                viewHolder.shimmerLayout.setVisibility(View.GONE);

                viewHolder.catimage.setImageResource(R.color.lightgrey);
            }
//            viewHolder.fulllay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent ii = new Intent(getActivity(),HotelDetails.class);
//                    startActivity(ii);
//                }
//            });



        }

        @Override
        public int getItemCount() {
            return (null != friends ? friends.size() : 0);
            // return  10;
        }
        protected class ViewHolder extends RecyclerView.ViewHolder {
            private TextView datetv, submit,txt_name,txt_duration,pricetxt,pricetxt1,txt_count,customizetxt,salepricetxt,txt_qnty;
          ShimmerFrameLayout shimmerLayout;
            ImageView catimage, offeric;
            //   SeeMoreTextView txt_term;
            ImageView favouritests,likeimg;
            RelativeLayout fulllay, ly1,minuslay,pluslay,likelay1;
            LinearLayout addminusbtnlay;
            Button btn_add;
            CheckBox checkb;
            public ViewHolder(View view) {
                super(view);

                shimmerLayout =  view.findViewById(R.id.shimmerLayout);
                catimage =  view.findViewById(R.id.catimage);
//                datetv =  view.findViewById(R.id.datetv);
//                submit =  view.findViewById(R.id.submit);
//                txt_name = (TextView) view.findViewById(R.id.txt_name);
//                txt_qnty1 = (TextView) view.findViewById(R.id.txt_qnty1);
//                txt_term = (SeeMoreTextView) view.findViewById(R.id.txt_term);
//                txt_term.setVisibility(View.VISIBLE);
//                pricetxt = (TextView) view.findViewById(R.id.txt_price);
//                pricetxt1 = (TextView) view.findViewById(R.id.txt_price1);
//                salepricetxt = (TextView) view.findViewById(R.id.txt_mrpprice);
//                txt_qnty = (TextView) view.findViewById(R.id.txt_qnty);
//                img_service = (ImageView) view.findViewById(R.id.pro_image);
//                likeimg = (ImageView) view.findViewById(R.id.likeimg1);
//                likelay1 = (RelativeLayout) view.findViewById(R.id.likelay1);
//                checkb = (CheckBox) view.findViewById(R.id.checkb);
//
            }
        }
    }
    private void getCountryMaps() {
        binding.progressbar.setVisibility(View.VISIBLE);


        ApiCall.get().Create().getCityImages(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("GetProducts  ", " >> " + response);
                System.out.println("sssssssssssssssssssssss lll "+response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("-------------PLACES-----------", responseData);
                        JSONObject object = new JSONObject(responseData);


                        if (object.getString("status").equalsIgnoreCase("1")) {

                            ImageSlideBean successData = new Gson().fromJson(responseData, ImageSlideBean.class);

                            if( successData.getResult()!=null &&  successData.getResult().size()>0) {

                                adapter1 = new RecyclerItemAdapter1(MapDetailsActivity.this, successData.getResult(), 0);
                                binding.providerrecyclerview.setAdapter(adapter1);

                                setupDots(successData.getResult().size(), 0);
                                adapter1.notifyDataSetChanged();
                                binding.providerrecyclerview.post(() -> {
                                    setupDots(successData.getResult().size(), 0);
                                });
                                binding.providerrecyclerview.addOnScrollListener(
                                        new RecyclerView.OnScrollListener() {

                                            @Override
                                            public void onScrolled(@NonNull RecyclerView recyclerView,
                                                                   int dx,
                                                                   int dy) {

                                                super.onScrolled(recyclerView, dx, dy);

                                                int position =
                                                        layoutManager.findFirstCompletelyVisibleItemPosition();

                                                if (position != RecyclerView.NO_POSITION) {
                                                    setupDots(successData.getResult().size(), position);
                                                }
                                            }
                                        });
                                binding.providerrecyclerview.setVisibility(View.VISIBLE);
                                binding.layoutDots.setVisibility(View.VISIBLE);
                                binding.cityimage.setVisibility(View.GONE);
                            }else{
                                binding.providerrecyclerview.setVisibility(View.GONE);
                                binding.layoutDots.setVisibility(View.GONE);
                                binding.cityimage.setVisibility(View.VISIBLE);
                            }

                        } else {

                            binding.providerrecyclerview.setVisibility(View.GONE);
                            binding.layoutDots.setVisibility(View.GONE);
                            binding.cityimage.setVisibility(View.VISIBLE);

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        binding.layoutDots.setVisibility(View.GONE);
                        binding.providerrecyclerview.setVisibility(View.GONE);
                        binding.cityimage.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
            }
        });

    }
    private HashMap<String,String> getProductParam(String type) {
        HashMap<String,String>param=new HashMap<>();
        param.put("user_id",session.getUserID());
        //  param.put("token",session.getUserDetails().getToken());
        param.put("city_id",""+cityid);
        param.put("lang",""+language);


        return param;

    }

    private void setupDots(int count, int position) {

        binding.layoutDots.removeAllViews();

        for (int i = 0; i < count; i++) {

            ImageView dot = new ImageView(this);

            if (i == position) {
                dot.setImageResource(R.drawable.active_dot);
            } else {
                dot.setImageResource(R.drawable.inactive_dot);
            }

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            20,   // width
                            20);  // height

            params.setMargins(8,0,8,0);

            dot.setLayoutParams(params);

            binding.layoutDots.addView(dot);
        }

        binding.layoutDots.invalidate();
    }
}
package main.com.cineramamaps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivitySubMapsListBinding;
import main.com.cineramamaps.databinding.SubmaplistitemBinding;
import main.com.cineramamaps.draweractivity.BaseActivity;
import main.com.cineramamaps.model.CityMapBean;
import main.com.cineramamaps.model.CityMapBeanList;
import main.com.cineramamaps.model.ItemBean;
import main.com.cineramamaps.model.ItemBeanList;
import main.com.cineramamaps.model.ProviderBean;
import main.com.cineramamaps.model.ProviderBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubMapsList extends AppCompatActivity {
    ActivitySubMapsListBinding binding;
    String city_id="",type = "Restaurant",country_id="",country_name_ar="",country_name="",productid="",day_name="";
    AllRestaurnatAdapter adapter1;
    SessionManager session;
    public static String favcall="";
    private String language = "";
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
//        if(!favcall.equalsIgnoreCase("")){
//            favcall = "";
//            getCountryMaps();
   //     }
        // getFavfoods();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_maps_list);
        session = SessionManager.get(SubMapsList.this);
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
        Date d1 = new Date();
        day_name = sdf1.format(d1);
        Bundle b = getIntent().getExtras();
        country_id = b.getString("country_id");
        country_name = b.getString("country_name");
        country_name_ar = b.getString("country_name_ar");
        favcall="";
        if(language.equalsIgnoreCase("en")) {
            binding.headtxt.setText("" + country_name );
            binding.useremailtv.setText(getResources().getString(R.string.citymapsonrepublic)+" " + country_name + ".");
        }else{
            binding.headtxt.setText("" + country_name_ar );
            binding.useremailtv.setText(getResources().getString(R.string.citymapsonrepublic)+" " + country_name_ar + ".");

        }
        clickevent();
        getCountryMaps();

    }

    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.itemtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Restaurant";


                binding.itemtv.setBackgroundResource(R.drawable.button_round_dra);
                binding.magicbagtv.setBackgroundResource(R.color.grey_back_col);

                binding.itemtv.setTextColor(getResources().getColor(R.color.white));
                binding.magicbagtv.setTextColor(getResources().getColor(R.color.black));
                getCountryMaps();
            }
        });
        binding.magicbagtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Item";
                binding.itemtv.setBackgroundResource(R.color.grey_back_col);
                binding.magicbagtv.setBackgroundResource(R.drawable.button_round_dra);

                binding.magicbagtv.setTextColor(getResources().getColor(R.color.white));
                binding.itemtv.setTextColor(getResources().getColor(R.color.black));
                getCountryMaps();
            }
        });
    }



    private void getCountryMaps() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.shimmerViewFavroite.setVisibility(View.VISIBLE);
        binding.shimmerViewFavroite.startShimmerAnimation();
        if(type.equalsIgnoreCase("Restaurant")){
            ApiCall.get().Create().getCityMaps(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    binding.progressbar.setVisibility(View.GONE);
                    Log.e("GetProducts  ", " >> " + response);
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);

                            binding.shimmerViewFavroite.setVisibility(View.GONE);
                            binding.shimmerViewFavroite.stopShimmerAnimation();
                            if (object.getString("status").equalsIgnoreCase("1")) {

                                CityMapBean successData = new Gson().fromJson(responseData, CityMapBean.class);
                                adapter1 = new AllRestaurnatAdapter(SubMapsList.this,successData.getResult());
                                binding.discountfoodsrecyler.setAdapter(adapter1);
                                adapter1.notifyDataSetChanged();


                            } else {
                                adapter1 =     new AllRestaurnatAdapter(SubMapsList.this,null);
                                binding.discountfoodsrecyler.setAdapter(adapter1);


                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    binding.progressbar.setVisibility(View.GONE);
                }
            });
        }
    }
    private HashMap<String,String> getProductParam(String type) {


        HashMap<String,String>param=new HashMap<>();
        param.put("user_id",session.getUserID());
        //  param.put("token",session.getUserDetails().getToken());
        param.put("country_id",""+country_id);


        return param;

    }


    public class AllRestaurnatAdapter extends RecyclerView.Adapter<AllRestaurnatAdapter.ViewHolder> {
        Context context;
        List<CityMapBeanList> CityMapList;

        public AllRestaurnatAdapter(Context context, List<CityMapBeanList> CityMapList) {
            this.context = context;
            this.CityMapList = CityMapList;
        }


        @NonNull
        @Override
        public AllRestaurnatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SubmaplistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.submaplistitem, parent, false);
            return new AllRestaurnatAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AllRestaurnatAdapter.ViewHolder holder, int position) {
            if(language.equalsIgnoreCase("en")) {
                holder.binding.nametv.setText(CityMapList.get(position).getName());
            }else{
                holder.binding.nametv.setText(CityMapList.get(position).getNameAr());
            }
            holder.binding.addresstv.setText(CityMapList.get(position).getAddress());
            holder.binding.ratingtv.setText(CityMapList.get(position).getAvgRating());
            if(!CityMapList.get(position).getAvgRating().equalsIgnoreCase("")) {
                holder.binding.ratingbar.setRating(Float.parseFloat("" + CityMapList.get(position).getAvgRating()));
            }
//                        if (CityMapList.get(position).getImage() != null && !CityMapList.get(position).getImage().isEmpty()) {
//                Picasso.get().load(CityMapList.get(position).getImage()).placeholder(R.color.lightgrey).into(holder.binding.citymapimg);
//            }


            holder.binding.shimmerLayout.setVisibility(View.VISIBLE);
            holder.binding.shimmerLayout.startShimmerAnimation();

            if (CityMapList.get(position).getImage() != null
                    && !CityMapList.get(position).getImage().isEmpty()) {

                Picasso.get()
                        .load(CityMapList.get(position).getImage())
                        .placeholder(R.color.lightgrey)
                        .error(R.color.lightgrey)
                        .into(holder.binding.citymapimg,
                                new com.squareup.picasso.Callback() {

                                    @Override
                                    public void onSuccess() {

                                        holder.binding.shimmerLayout.stopShimmerAnimation();
                                        holder.binding.shimmerLayout.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                        holder.binding.shimmerLayout.stopShimmerAnimation();
                                        holder.binding.shimmerLayout.setVisibility(View.GONE);
                                    }
                                });

            } else {

                holder.binding.shimmerLayout.stopShimmerAnimation();
                holder.binding.shimmerLayout.setVisibility(View.GONE);

                holder.binding.citymapimg.setImageResource(R.color.lightgrey);
            }
            //            if(providerBeanLists.get(position).getToday_open_time()!=null) {
//                holder.binding.timeTv.setText(providerBeanLists.get(position).getToday_open_time());
//            }
//            holder.binding.distanceTv.setText(providerBeanLists.get(position).getDistance()+" "+context.getResources().getString(R.string.km));
//            holder.binding.ratingtv.setText(providerBeanLists.get(position).getTotal_rating_count());
//            holder.binding.ratingbar.setRating(Float.parseFloat(providerBeanLists.get(position).getTotal_rating_count()));
//            holder.binding.locationTv.setText(providerBeanLists.get(position).getProviderStreatAddress());
//            holder.binding.favic.setVisibility(View.VISIBLE);
//            holder.binding.quantitylefttv.setText(providerBeanLists.get(position).getItem_left_quantity()+" "+getResources().getString(R.string.items)+"\n"+providerBeanLists.get(position).getMagic_food_left_quantity()+" "+getResources().getString(R.string.magicbags)+" "+getResources().getString(R.string.left));
//
//            if (providerBeanLists.get(position).getStoreLogo() != null && !providerBeanLists.get(position).getStoreLogo().isEmpty()) {
//                Picasso.get().load(providerBeanLists.get(position).getStoreLogo()).placeholder(R.color.lightgrey).into(holder.binding.restimage);
//            }
            holder.binding.favic.setVisibility(View.GONE);
            if(CityMapList.get(position).getFav_status().equalsIgnoreCase("Yes")){
                holder.binding.favicimg.setBackgroundResource(R.drawable.favselected);
            }else{
                holder.binding.favicimg.setBackgroundResource(R.drawable.favunselected);
            }
            holder.binding.favic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    city_id = CityMapList.get(position).getId();
                    if(CityMapList.get(position).getFav_status().equalsIgnoreCase("Yes")) {
                        CityMapList.get(position).setFav_status("No");
                    }else {
                        CityMapList.get(position).setFav_status("Yes");
                    }
                    adapter1.notifyDataSetChanged();
                    addTofavCall();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if(CityMapList.get(position).getSubscription_status().equalsIgnoreCase("Yes")){
//                        Intent i = new Intent(context, PlacesListActivity.class);
//                        // i.putExtra("rest_id","");
//                        i.putExtra("country_id", country_id);
//                        i.putExtra("city_id", CityMapList.get(position).getId());
//                        i.putExtra("fav_status", CityMapList.get(position).getFav_status());
//                        i.putExtra("city_image", CityMapList.get(position).getImage());
//                        i.putExtra("city_name", CityMapList.get(position).getName());
//                        i.putExtra("city_address", CityMapList.get(position).getAddress());
//                        i.putExtra("city_lat", CityMapList.get(position).getLat());
//                        i.putExtra("city_lon", CityMapList.get(position).getLon());
//                        i.putExtra("city_rating", CityMapList.get(position).getAvgRating());
//                        i.putExtra("city_price", CityMapList.get(position).getCity_map_price());
//                        i.putExtra("city_month", CityMapList.get(position).getCity_map_month());
//                        context.startActivity(i);
//                    }else {
                        Intent i = new Intent(context, MapDetailsActivity.class);
                        // i.putExtra("rest_id","");
                        i.putExtra("youtubelink", CityMapList.get(position).getYoutube_video_link());
                        i.putExtra("youtubelink_arabic", CityMapList.get(position).getYoutube_video_link_arabic());
                        i.putExtra("country_id", country_id);
                        i.putExtra("city_id", CityMapList.get(position).getId());
                        i.putExtra("fav_status", CityMapList.get(position).getFav_status());
                        i.putExtra("city_image", CityMapList.get(position).getImage());
                        i.putExtra("city_name", CityMapList.get(position).getName());
                        i.putExtra("city_name_ar", CityMapList.get(position).getNameAr());
                        i.putExtra("city_address", CityMapList.get(position).getAddress());
                        i.putExtra("city_lat", CityMapList.get(position).getLat());
                        i.putExtra("city_lon", CityMapList.get(position).getLon());
                        i.putExtra("currency",CityMapList.get(position).getCurrency());
                        i.putExtra("currency_ar",CityMapList.get(position).getCurrencyAr());
                        i.putExtra("city_rating", CityMapList.get(position).getAvgRating());
                        i.putExtra("city_price", CityMapList.get(position).getCity_map_price());
                        i.putExtra("city_month", CityMapList.get(position).getCity_map_month());
                        context.startActivity(i);
                 //   }
                }
            });

        }

        @Override
        public int getItemCount() {
           // return 6;
              return CityMapList == null ? 0 : CityMapList.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            SubmaplistitemBinding binding;

            public ViewHolder(@NonNull SubmaplistitemBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
    }




    private HashMap<String, String> getFavParam() {


        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("city_id", city_id);

        return param;

    }
    private HashMap<String, String> getFavParam1() {


        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("product_id", productid);

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


                        } else {
                            Toast.makeText(SubMapsList.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SubMapsList.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SubMapsList.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addTofavCall1() {



        ApiCall.get().Create().addTofav(getFavParam1()).enqueue(new Callback<ResponseBody>() {
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


                        } else {
                            Toast.makeText(SubMapsList.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SubMapsList.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SubMapsList.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
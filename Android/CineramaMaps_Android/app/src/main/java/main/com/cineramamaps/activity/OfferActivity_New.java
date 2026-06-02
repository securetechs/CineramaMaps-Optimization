package main.com.cineramamaps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityOfferNewBinding;
import main.com.cineramamaps.databinding.FavlistitemBinding;
import main.com.cineramamaps.draweractivity.BaseActivity;
import main.com.cineramamaps.model.AllProductList;
import main.com.cineramamaps.model.ItemBean;
import main.com.cineramamaps.model.ItemBeanList;
import main.com.cineramamaps.model.OfferBeanList_New;
import main.com.cineramamaps.model.OfferBeanNew;
import main.com.cineramamaps.model.ProviderBean;
import main.com.cineramamaps.model.ProviderBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferActivity_New extends AppCompatActivity {
    ActivityOfferNewBinding binding;
    Context context;
    String id="",type = "Restaurant",restid="",productid="",day_name="";
    AllRestaurnatAdapter adapter1;
    SessionManager session;
    List<OfferBeanList_New> alllistdata =  new ArrayList<OfferBeanList_New>();
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

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offer_new);
        context = OfferActivity_New.this;
        session = SessionManager.get(OfferActivity_New.this);
        // binding.discountfoodsrecyler.setAdapter(new AllDiscountFoodsAdapter(OfferActivity_New.this,null));
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
        Date d1 = new Date();
        day_name = sdf1.format(d1);

        clickevent();
        getFavfoods();
//        adapter1 = new AllRestaurnatAdapter(OfferActivity_New.this,null);
//        binding.discountfoodsrecyler.setAdapter(adapter1);
//        adapter1.notifyDataSetChanged();
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
                getFavfoods();
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
                getFavfoods();
            }
        });
    }



    private void getFavfoods() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.shimmerViewFavroite.setVisibility(View.VISIBLE);
        binding.shimmerViewFavroite.startShimmerAnimation();

            ApiCall.get().Create().getcompanyOffernew(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
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
                                System.out.println("sssssssssssssssssssss = Tab0");
                                OfferBeanNew successData = new Gson().fromJson(responseData, OfferBeanNew.class);
                                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                                System.out.println("sssssss22 "+alllistdata.size());
                                alllistdata = successData.getResult();
                                 for(int j=0;j<alllistdata.size();j++) {
                                     // for(int j=0;j<5;j++) {
                                     if (language.equalsIgnoreCase("en")){
                                         System.out.println("sssssssssssssssssssss = Tab00");
                                         adapter.addFragment(new TabFragment(alllistdata, alllistdata, alllistdata.get(j).getImage(), language,context), "Category");
                                 }else {
                                         adapter.addFragment(new TabFragment(alllistdata, alllistdata, alllistdata.get(j).getImage(), language,context), "Category Ar");
                                     }
                                    // adapter.addFragment(new ProductLisFragment(null,"aa"), "Restaurants and Cafes");
                                    binding.viewPager.setAdapter(adapter);

                                }

                                binding.tabLayout.setupWithViewPager(binding.viewPager);


                            } else {
//                                adapter1 = new AllRestaurnatAdapter(OfferActivity_New.this, null);
//                                binding.discountfoodsrecyler.setAdapter(adapter1);


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
    private HashMap<String,String> getProductParam(String type) {


        HashMap<String,String>param=new HashMap<>();
        param.put("user_id",session.getUserID());
        //  param.put("token",session.getUserDetails().getToken());
        param.put("city_id","");
        param.put("country_id","");
        return param;

    }


    public class AllRestaurnatAdapter extends RecyclerView.Adapter<AllRestaurnatAdapter.ViewHolder> {
        Context context;
        List<ProviderBeanList> providerBeanLists;

        public AllRestaurnatAdapter(Context context, List<ProviderBeanList> providerBeanLists) {
            this.context = context;
            this.providerBeanLists = providerBeanLists;
        }


        @NonNull
        @Override
        public AllRestaurnatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FavlistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.favlistitem, parent, false);
            return new AllRestaurnatAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AllRestaurnatAdapter.ViewHolder holder, int position) {

//            holder.binding.restnameTv.setText(providerBeanLists.get(position).getProviderName());
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
//            holder.binding.favic.setBackgroundResource(R.drawable.fav_selected);
////            if(providerBeanLists.get(position).getFav_provider().equalsIgnoreCase("Yes")){
////                holder.binding.favic.setBackgroundResource(R.drawable.fav_selected);
////            }else{
////                holder.binding.favic.setBackgroundResource(R.drawable.fav);
////            }
//            holder.binding.favic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    restid = providerBeanLists.get(position).getId();
//                    providerBeanLists.remove(position);
//                    adapter1.notifyDataSetChanged();
//                    addTofavCall();
//                }
//            });
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(context, MenuActivity.class);
//                    i.putExtra("rest_id","1");
//                  //  i.putExtra("rest_id",providerBeanLists.get(position).getId());
//                    context.startActivity(i);
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return 10;
            //  return providerBeanLists == null ? 0 : providerBeanLists.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            FavlistitemBinding binding;

            public ViewHolder(@NonNull FavlistitemBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
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
}
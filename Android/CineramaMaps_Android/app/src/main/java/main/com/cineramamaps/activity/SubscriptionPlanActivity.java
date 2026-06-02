package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.ReveiwListAdapter;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityChoosePaymentTypeBinding;
import main.com.cineramamaps.databinding.ActivitySettingBinding;
import main.com.cineramamaps.databinding.ActivitySubscriptionPlanBinding;
import main.com.cineramamaps.databinding.LayoutCustmReviewBinding;
import main.com.cineramamaps.databinding.LayoutSubscriptionlistitemBinding;
import main.com.cineramamaps.model.PlacesBean;
import main.com.cineramamaps.model.RatingReview;
import main.com.cineramamaps.model.SubscriptionBean;
import main.com.cineramamaps.model.SubscriptionBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionPlanActivity extends AppCompatActivity {
    SubscriptionListAdapter    adapter1;
    private SessionManager session;
    ActivitySubscriptionPlanBinding binding;
    private String ImagePath="";
    private MultipartBody.Part body;
    private String language = "";
    Animation bounce;
    public static String payment_type = "CASH";
    int pos = 0;
    String country_id = "",city_id="";
    MyLanguageSession myLanguageSession;
    List<SubscriptionBeanList> planlist = new ArrayList<>();
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
    private void BindView() {
        session = SessionManager.get(SubscriptionPlanActivity.this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding= DataBindingUtil.setContentView(this, R.layout.activity_subscription_plan);
        bounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.sample_animation);
        country_id = getIntent().getExtras().getString("country_id");
        city_id = getIntent().getExtras().getString("city_id");
        BindView();
        binding.reviewexplist.setExpanded(true);
        clickevent();
        getCountryMaps();
    }
    private void getCountryMaps() {
        binding.progressbar.setVisibility(View.VISIBLE);


        ApiCall.get().Create().getPlan(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("GetProducts  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);


                        if (object.getString("status").equalsIgnoreCase("1")) {

                            SubscriptionBean successData = new Gson().fromJson(responseData, SubscriptionBean.class);
                            planlist = new ArrayList<>();
                            planlist.clear();
                            planlist = successData.getResult();
                            if (planlist.get(0).getOfferImage() != null && !planlist.get(0).getOfferImage().isEmpty()) {
                                Picasso.get().load(planlist.get(0).getOfferImage()).placeholder(R.color.lightgrey).into(binding.discountimg1);
                            }
                                adapter1 = new SubscriptionListAdapter(SubscriptionPlanActivity.this, planlist);
                            binding.reviewexplist.setAdapter(adapter1);
                            adapter1.notifyDataSetChanged();

                        } else {



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
        param.put("city_id",""+city_id);


        return param;

    }
    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(payment_type.equalsIgnoreCase("CASH")){
//                    successpopup();
//                }else {
                if(planlist.get(pos).getIs_select().equalsIgnoreCase("Yes")) {
                    Intent i = new Intent(SubscriptionPlanActivity.this, ConfirmPayment.class);
                    i.putExtra("country_id",country_id);
                    i.putExtra("city_id",city_id);
                    i.putExtra("type",planlist.get(pos).getName());
                    i.putExtra("duration",planlist.get(pos).getMonth());
                    i.putExtra("order_id", ""+planlist.get(pos).getId());
                    i.putExtra("come_from", "order");
                    i.putExtra("paid_amount", ""+planlist.get(pos).getPrice());
                    //   i.putExtra("type","USER");
                    startActivity(i);
                }else{
                    Toast.makeText(SubscriptionPlanActivity.this,""+getResources().getString(R.string.pleaseselectatleastoneplan),Toast.LENGTH_LONG).show();
                }
              //  }
            }
        });
    }

    public class SubscriptionListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        private List<SubscriptionBeanList> planlist;
        public SubscriptionListAdapter(Context applicationContext, List<SubscriptionBeanList> planlist) {
            this.context = applicationContext;
            this.planlist = planlist;

            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            //  return 2;
            return planlist == null ? 0 : planlist.size();
        }
        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            //  view = inflter.inflate(R.layout.layout_yachts_item_new_sec, null);


          YachtsViewHolder holder;

            if (view == null) {
                LayoutSubscriptionlistitemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_subscriptionlistitem,viewGroup,false);

                holder = new YachtsViewHolder(binding);
                holder.view = binding.getRoot();
                holder.view.setTag(holder);
            }
            else {
                holder = (YachtsViewHolder) view.getTag();
            }
            if(planlist.get(position).getIs_select().equalsIgnoreCase("Yes")){
                System.out.println("sssssssssssssssssss = Yes"+planlist.get(position).getIs_select());
                holder.binding.userlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                holder.binding.usertv.setTextColor(getResources().getColor(R.color.white));
                            if (planlist.get(position).getOfferImage() != null && !planlist.get(position).getOfferImage().isEmpty()) {
                Picasso.get().load(planlist.get(position).getOfferImage()).placeholder(R.color.lightgrey).into(binding.discountimg1);
          pos = position;
            }


            }else{
                System.out.println("sssssssssssssssssss = No"+planlist.get(position).getIs_select());
                holder.binding.userlay.setBackgroundColor(getResources().getColor(R.color.white));
                holder.binding.usertv.setTextColor(getResources().getColor(R.color.black));
            }
            holder.binding.userlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int j =0;j<planlist.size();j++) {
                        if(j==position) {
                            System.out.println("sssssssssssssssssss = Posss "+planlist.get(position).getIs_select());
                            if(planlist.get(position).getIs_select().equalsIgnoreCase("No")) {
                                System.out.println("sssssssssssssssssss = Yes1"+planlist.get(position).getIs_select());
                                planlist.get(position).setIs_select("Yes");

                            }else{
                                System.out.println("sssssssssssssssssss = No1"+planlist.get(position).getIs_select());
                                planlist.get(position).setIs_select("No");
                            }
                        }else{
                            planlist.get(j).setIs_select("No");
                            System.out.println("sssssssssssssssssss = No2"+planlist.get(position).getIs_select());
                        }
                    }
                    adapter1.notifyDataSetChanged();
                }

            });
           if(language.equalsIgnoreCase("en")) {
               holder.binding.usertv.setText(planlist.get(position).getName());
               holder.binding.destv.setText(planlist.get(position).getDescription());
           }else{
               holder.binding.usertv.setText(planlist.get(position).getNameAr());
               holder.binding.destv.setText(planlist.get(position).getDescriptionAr());
           }

            holder.binding.amounttxt.setText(planlist.get(position).getPrice()+" "+getResources().getString(R.string.currency)+" "+planlist.get(position).getMonth()+" "+getResources().getString(R.string.month));

//            if (ratingBeanLists.get(position).getImage() != null && !ratingBeanLists.get(position).getImage().isEmpty()) {
//                Picasso.get().load(ratingBeanLists.get(position).getImage()).placeholder(R.color.lightgrey).into(holder.binding.profileic);
//            }


            return holder.view;



        }

        public  class YachtsViewHolder {
            private View view;
            private LayoutSubscriptionlistitemBinding binding;

            YachtsViewHolder(LayoutSubscriptionlistitemBinding binding) {
                this.view = binding.getRoot();
                this.binding = binding;
            }
        }
    }

}
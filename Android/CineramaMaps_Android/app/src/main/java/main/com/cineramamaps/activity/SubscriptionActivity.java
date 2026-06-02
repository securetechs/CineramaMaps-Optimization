package main.com.cineramamaps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.NotificationAdapter;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityNotificationBinding;
import main.com.cineramamaps.databinding.ActivitySubscriptionBinding;
import main.com.cineramamaps.databinding.LayoutCustomNotificationBinding;
import main.com.cineramamaps.databinding.SubscriptionlistitemBinding;
import main.com.cineramamaps.draweractivity.BaseActivity;
import main.com.cineramamaps.model.CityMapBean;
import main.com.cineramamaps.model.CityMapBeanList;
import main.com.cineramamaps.model.NotificationBean;
import main.com.cineramamaps.model.NotificationBeanList;
import main.com.cineramamaps.model.SubscriptionBean;
import main.com.cineramamaps.model.SubscriptionBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionActivity extends AppCompatActivity {
    NotificationAdapter    adapter1;
    private SessionManager session;
    String countryid="",cityid="";
    int pos =0;
    ActivitySubscriptionBinding binding;
    private ArrayList<SubscriptionBeanList> planBeanListArrayList;
    ArrayList<CityMapBeanList> searchbuymessageBeanListArrayList1 = new ArrayList<>();
    ArrayList<CityMapBeanList> CityMapList = new ArrayList<>();
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription);
        BindView();
        clickevent();
          // getPlan();
        getCountryMaps();

      //  binding.notificaitonlist.setAdapter(new NotificationAdapter(SubscriptionActivity.this, null, session.IsEnglish()));

    }

    private void clickevent() {

        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void BindView() {
        session = SessionManager.get(SubscriptionActivity.this);


    }


    private void getPlan() {
        binding.progressbar.setVisibility(View.VISIBLE);
        planBeanListArrayList = new ArrayList<>();
        ApiCall.get().Create().getPlan(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("MyItems  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            binding.datalay.setVisibility(View.VISIBLE);
                            binding.emptylay.setVisibility(View.GONE);
                            planBeanListArrayList = new ArrayList<>();
                            SubscriptionBean successData = new Gson().fromJson(responseData, SubscriptionBean.class);
                            planBeanListArrayList.addAll(successData.getResult());


                          //  binding.notificaitonlist.setAdapter(new NotificationAdapter(SubscriptionActivity.this, planBeanListArrayList, session.IsEnglish()));


                        } else {

                            binding.datalay.setVisibility(View.GONE);
                            binding.emptylay.setVisibility(View.VISIBLE);

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
    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
        Context context;
        boolean b;
        List<CityMapBeanList> planBeanListArrayList;

        public NotificationAdapter(Context context, ArrayList<CityMapBeanList> planBeanListArrayList, boolean b) {
            this.context = context;
            this.planBeanListArrayList = planBeanListArrayList;
            this.b = b;
        }


        @NonNull
        @Override
        public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SubscriptionlistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.subscriptionlistitem, parent, false);
            return new NotificationAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        if(language.equalsIgnoreCase("en")){
            holder.binding.titletv.setText("" + planBeanListArrayList.get(position).getName());
          //  holder.binding.pricetxt.setText(getResources().getString(R.string.currency)+"" + planBeanListArrayList.get(position).getCity_map_price()+" "+getResources().getString(R.string.permonth));
            holder.binding.pricetxt.setText(""+planBeanListArrayList.get(position).getCity_map_price()+" "+getResources().getString(R.string.currency)+" "+getResources().getString(R.string.for1)+" "+planBeanListArrayList.get(position).getCity_map_month()+" "+getResources().getString(R.string.month));
            holder.binding.destv.setText("" + planBeanListArrayList.get(position).getAboutCity());
        }else{
            holder.binding.titletv.setText("" + planBeanListArrayList.get(position).getNameAr());
            holder.binding.pricetxt.setText(""+planBeanListArrayList.get(position).getCity_map_price()+" "+getResources().getString(R.string.currency)+" "+getResources().getString(R.string.for1)+" "+planBeanListArrayList.get(position).getCity_map_month()+" "+getResources().getString(R.string.month));
            holder.binding.destv.setText("" + planBeanListArrayList.get(position).getAboutCityAr());
        }
            holder.binding.submit.setText(getResources().getString(R.string.expirydate)+": " + planBeanListArrayList.get(position).getExp_date());
        holder.binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                countryid = ""+planBeanListArrayList.get(position).getCountryId();
//                cityid = ""+planBeanListArrayList.get(position).getId();
//                pos = position;
//                itemAddedCartSucc();
////                Intent ii = new Intent(SubscriptionActivity.this,MapsActivity.class);
////                ii.putExtra("type",planBeanListArrayList.get(position).getName());
////                ii.putExtra("duration",planBeanListArrayList.get(position).getMonth());
////                ii.putExtra("order_id", ""+planBeanListArrayList.get(position).getId());
////                ii.putExtra("come_from", "order");
////                ii.putExtra("paid_amount", ""+planBeanListArrayList.get(position).getPrice());
////                //   i.putExtra("type","USER");
////                startActivity(ii);
            }
        });

//        holder.binding.datetv.setText("" + notificationBeanListArrayList.get(position).getDateTime());
//        holder.binding.nametv.setText("" + notificationBeanListArrayList.get(position).getUserDetails().getFirstName() + " " + notificationBeanListArrayList.get(position).getUserDetails().getLastName());
//
//        if (notificationBeanListArrayList.get(position).getUserDetails() != null) {
//            String image = notificationBeanListArrayList.get(position).getUserDetails().getImage();
//
//            if (image != null && !image.equalsIgnoreCase("")) {
//                Picasso.get().load(image).placeholder(R.drawable.profile_ic).into(holder.binding.userpic);
//            }
//
//
//        }
        }

        @Override
        public int getItemCount() {
         //   return 2;
             return planBeanListArrayList == null ? 0 : planBeanListArrayList.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            SubscriptionlistitemBinding binding;

            public ViewHolder(@NonNull SubscriptionlistitemBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
    }


    private void getCountryMaps() {
        binding.progressbar.setVisibility(View.VISIBLE);
        CityMapList = new ArrayList<>();
        CityMapList.clear();
        searchbuymessageBeanListArrayList1 = new ArrayList<>();
        searchbuymessageBeanListArrayList1.clear();

        ApiCall.get().Create().getCityMaps1(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("GetProducts  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);


                        if (object.getString("status").equalsIgnoreCase("1")) {

                            CityMapBean successData = new Gson().fromJson(responseData, CityMapBean.class);
                            CityMapList.addAll(successData.getResult());
                            searchbuymessageBeanListArrayList1.addAll(successData.getResult());
                                adapter1=     new NotificationAdapter(SubscriptionActivity.this, CityMapList, session.IsEnglish());
                            binding.notificaitonlist.setAdapter(adapter1);
                            adapter1.notifyDataSetChanged();
//                            binding.discountfoodsrecyler.setVisibility(View.VISIBLE);
//                            binding.providerrecyclerview.setVisibility(View.GONE);
//                            binding.norestfound.setVisibility(View.GONE);


                        } else {
//                            adapter1 =     new MapsListActivity.AllRestaurnatAdapter(MapsListActivity.this,null);
//                            binding.discountfoodsrecyler.setAdapter(adapter1);
//                            binding.discountfoodsrecyler.setVisibility(View.GONE);
//                            binding.providerrecyclerview.setVisibility(View.GONE);
//                            binding.norestfound.setVisibility(View.VISIBLE);

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
        param.put("country_id","");


        return param;

    }
    private void itemAddedCartSucc() {
        final Dialog dialogSts = new Dialog(SubscriptionActivity.this);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.popup_logout);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView head1 = dialogSts.findViewById(R.id.head1);
        TextView head2 = dialogSts.findViewById(R.id.head2);
        head1.setText(""+getResources().getString(R.string.cancelsubscription));
        head2.setText(""+getResources().getString(R.string.areyousurewanttocancelsubscription));
        TextView dismiss = dialogSts.findViewById(R.id.dismiss);
        TextView gotocart = dialogSts.findViewById(R.id.gotocart);


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();


            }
        });
        gotocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                getRemoveplan();
            }
        });
        dialogSts.show();
    }


    private void getRemoveplan() {

        ApiCall.get().Create().getRemovePlan(session.getUserID(),countryid,cityid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyProfileDetail >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                           // JSONObject jsonObject1 = object.getJSONObject("result");
                           CityMapList.remove(pos);
                           adapter1.notifyDataSetChanged();
                            Toast.makeText(SubscriptionActivity.this,""+getResources().getString(R.string.subscriptionplancanceledsuccess),Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SubscriptionActivity.this,""+object.getString("message"),Toast.LENGTH_LONG).show();


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

}
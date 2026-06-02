package main.com.cineramamaps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivitySubscriptionBinding;
import main.com.cineramamaps.databinding.ActivityTripScheduleListBinding;
import main.com.cineramamaps.databinding.SubscriptionlistitemBinding;
import main.com.cineramamaps.databinding.TripschedulelistitemBinding;
import main.com.cineramamaps.databinding.TripshedulenewlistBinding;
import main.com.cineramamaps.model.NotificationBean;
import main.com.cineramamaps.model.NotificationBeanList;
import main.com.cineramamaps.model.TripBean;
import main.com.cineramamaps.model.TripBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripScheduleListActivity extends AppCompatActivity {

    private SessionManager session;
    ActivityTripScheduleListBinding binding;
    private ArrayList<TripBeanList> tripBeanListArrayList;
    NotificationAdapter  adapter;
    private String language = "",tripid="";
    int pos =0;
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_schedule_list);
        BindView();
        clickevent();
        //   getNotification();

      //  binding.notificaitonlist.setAdapter(new NotificationAdapter(TripScheduleListActivity.this, null, session.IsEnglish()));
        getTrip();
    }



    private void clickevent() {
binding.addbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent ii = new Intent(TripScheduleListActivity.this,CreateTripActivity.class);
        ii.putExtra("id","");
        startActivity(ii);
    }
});
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void BindView() {
        session = SessionManager.get(TripScheduleListActivity.this);


    }


    private void getTrip() {
        binding.progressbar.setVisibility(View.VISIBLE);
        tripBeanListArrayList = new ArrayList<>();
        ApiCall.get().Create().getUserTrip(session.getUserID()).enqueue(new Callback<ResponseBody>() {
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
                            tripBeanListArrayList = new ArrayList<>();
                            TripBean successData = new Gson().fromJson(responseData, TripBean.class);
                            tripBeanListArrayList.addAll(successData.getResult());

                              adapter =   new NotificationAdapter(TripScheduleListActivity.this, tripBeanListArrayList, session.IsEnglish());
                            binding.notificaitonlist.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


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
        List<TripBeanList> tripBeanListArrayList;

        public NotificationAdapter(Context context, ArrayList<TripBeanList> tripBeanListArrayList, boolean b) {
            this.context = context;
            this.tripBeanListArrayList = tripBeanListArrayList;
            this.b = b;
        }


        @NonNull
        @Override
        public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TripshedulenewlistBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.tripshedulenewlist, parent, false);
            return new NotificationAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
if(language.equalsIgnoreCase("en")) {
    holder.binding.maptypetv.setText("" + tripBeanListArrayList.get(position).getMapType() + "," + tripBeanListArrayList.get(position).getCountryName());
}else{
    holder.binding.maptypetv.setText("" + tripBeanListArrayList.get(position).getMapTypeAr() + "," + tripBeanListArrayList.get(position).getCountryNameAr());

}
        holder.binding.titletv.setText("" + tripBeanListArrayList.get(position).getMap_name());
            holder.binding.titletv.setVisibility(View.VISIBLE);
        holder.binding.datetv.setText(getResources().getString(R.string.lastupdate)+" " + tripBeanListArrayList.get(position).getDateTime());
        if(tripBeanListArrayList.get(position).getTripByCineramap().equalsIgnoreCase("Yes")){
            holder.binding.createdbytv.setVisibility(View.VISIBLE);
        }else{
            holder.binding.createdbytv.setVisibility(View.GONE);
        }
//        holder.binding.editbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(TripScheduleListActivity.this,CreateTripActivity.class);
//                i.putExtra("id",tripBeanListArrayList.get(position).getId());
//                startActivity(i);
//            }
//        });
        holder.binding.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripid = tripBeanListArrayList.get(position).getPlaceId();
                pos = position;
                Delete_call();
            }
        });
        holder.binding.mainlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TripScheduleListActivity.this,ShedulePlaceListActivity.class);
                i.putExtra("table_map_name",tripBeanListArrayList.get(position).getMap_name());
                i.putExtra("id",tripBeanListArrayList.get(position).getPlaceId());
                i.putExtra("name",tripBeanListArrayList.get(position).getMapType());
                i.putExtra("name_ar",tripBeanListArrayList.get(position).getMapTypeAr());
                startActivity(i);
            }
        });


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
           // return 3;
             return tripBeanListArrayList == null ? 0 : tripBeanListArrayList.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TripshedulenewlistBinding binding;

            public ViewHolder(@NonNull TripshedulenewlistBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
    }


    private void Delete_call() {
       binding.progressbar.setVisibility(View.VISIBLE);
        Call<ResponseBody> resultCall = ApiCall.get().Create().delete_trip_city(session.getUserID(),tripid);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String responedata = response.body().string();
                        Log.e("get address response", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");
                        if (error.equals("1")) {
                            // if (isInternetPresent) {
                            tripBeanListArrayList.remove(pos);
                            adapter.notifyDataSetChanged();
//                            } else {
//                                Toast.makeText(GetUserAddress.this,"No Internet",Toast.LENGTH_LONG).show();
//                            }
                        } else {
                            String message = object.getString("result");
                            if (getApplicationContext() != null) {
                                Toast.makeText(getApplicationContext(), "Some Error found Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("hgdhgfgdf", "dtrdfuydrfgjhjjfyt");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.server_problem), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
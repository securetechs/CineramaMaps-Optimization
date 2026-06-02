package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.constant.SetLocation;
import main.com.cineramamaps.databinding.ActivityCreateTripBinding;
import main.com.cineramamaps.databinding.ActivityTripScheduleListBinding;
import main.com.cineramamaps.model.CategoryBean;
import main.com.cineramamaps.model.CityMapBean;
import main.com.cineramamaps.model.CityMapBeanList;
import main.com.cineramamaps.model.DayBean;
import main.com.cineramamaps.model.DayBeanList;
import main.com.cineramamaps.model.NotificationBeanList;
import main.com.cineramamaps.model.SingleTrip;
import main.com.cineramamaps.model.UserDetails;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTripActivity extends AppCompatActivity {
    CustomAdapter customAdapter;
    private SessionManager session;
    ActivityCreateTripBinding binding;
    CustomAdapter2  customAdapter2;
    private String language = "";
    ArrayList<CityMapBeanList> mapnameBeanLists = new ArrayList<>();
    ArrayList<CityMapBeanList> mapBeanLists = new ArrayList<>();
    ArrayList<DayBeanList> dayBeanLists = new ArrayList<>();
    CustomAdapter1   customAdapter1;
    String map1="",tablemap1="", map_name="",table_map_name="",table_map_name1="", map_place_id="", place_id="", place_name="",place_name_ar="", city_id="",city_name="",city_name_ar="", dayid1="",dayid="",dayname="",daynamear="",  countryid="", countryname="",countryname_ar="",mapid1="", trip_id="",  name="",mapid="",maptype="",maptypeAr="",address="",lat="",lon="",daycount="",checkvalue="";
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

        if (SetLocation.pickuplocation_str!=null&&!SetLocation.pickuplocation_str.equalsIgnoreCase("")){
            binding.addresstv.setText(""+SetLocation.pickuplocation_str);
            address = SetLocation.pickuplocation_str;
            lat = ""+SetLocation.originlatlong.latitude;
            lon = ""+SetLocation.originlatlong.longitude;
            SetLocation.pickuplocation_str ="";
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
         trip_id = getIntent().getExtras().getString("id");
         Bundle b = getIntent().getExtras();
         if(b.containsKey("city_id")){
             place_id= b.getString("place_id");
             address= b.getString("address");
             lat= b.getString("lat");
             lon= b.getString("lon");
             map_place_id= b.getString("map_place_id");
             city_id= b.getString("city_id");
             city_name= b.getString("city_name");
             city_name_ar= b.getString("city_name_ar");
             place_name= b.getString("place_name");
             place_name_ar= b.getString("place_name_ar");
         }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_trip);
        session = SessionManager.get(CreateTripActivity.this);
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.addresstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateTripActivity.this, SetLocation.class);
                i.putExtra("comefrom","Signup");
                startActivity(i);
            }
        });
        binding.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(CreateTripActivity.this,TripScheduleListActivity.class);
                startActivity(ii);
                finish();
            }
        });

        if(!trip_id.equalsIgnoreCase("")){
            getTripDetails();
        }else{
                    getCountryMaps();
        getDays();
            getMapname();
        }
        if(!city_id.equalsIgnoreCase("")){
            binding.headtxt.setText(""+getResources().getString(R.string.addplacetotable));
            binding.typespi.setEnabled(false);
            binding.typespi.setClickable(false);
            binding.nametv.setEnabled(false);
            binding.nametv.setClickable(false);
            if(language.equalsIgnoreCase("en")) {
                binding.nametv.setText("" + place_name);
            }else{
                binding.nametv.setText(""+place_name_ar);
            }
            binding.extraitemlay.setVisibility(View.VISIBLE);
            binding.mapnamelay.setVisibility(View.GONE);
        }else{
            binding.headtxt.setText(""+getResources().getString(R.string.createschedule));
            binding.mapnamelay.setVisibility(View.VISIBLE);
            binding.extraitemlay.setVisibility(View.GONE);
        }
        binding.createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   address = binding.addresstv.getText().toString();
                map_name =    binding.mapnametv.getText().toString();
//                place_name_ar =    binding.nametv.getText().toString();
              //  daycount = binding.daytv.getText().toString();
                if(binding.checkbycinerama.isChecked()==true){
                 checkvalue = "Yes";
                }else{
                    checkvalue = "No";
                }

//                if(name.equalsIgnoreCase("")){
//                    Toast.makeText(CreateTripActivity.this,""+getResources().getString(R.string.pleaseentermapname),Toast.LENGTH_LONG).show();
//                }
////               else if(address.equalsIgnoreCase("")){
////                    Toast.makeText(CreateTripActivity.this,""+getResources().getString(R.string.pleaseselectaddress),Toast.LENGTH_LONG).show();
////                }
//                else if(dayid.equalsIgnoreCase("")){
//                    Toast.makeText(CreateTripActivity.this,""+getResources().getString(R.string.pleaseentertripday),Toast.LENGTH_LONG).show();
//                }
//                else{
                if(city_id.equalsIgnoreCase("") && map_name.equalsIgnoreCase("")){
                                        Toast.makeText(CreateTripActivity.this,""+getResources().getString(R.string.pleaseentermapname),Toast.LENGTH_LONG).show();

                }
               else if(!city_id.equalsIgnoreCase("") && table_map_name.equalsIgnoreCase("")){
                    Toast.makeText(CreateTripActivity.this,""+getResources().getString(R.string.pleaseselecttablemapname),Toast.LENGTH_LONG).show();
                }else{
                    DoSubmit();
                }

               // }

            }
        });

                binding.typespi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                  mapid = mapBeanLists.get(i).getId();
                  maptype = mapBeanLists.get(i).getName();
                  maptypeAr = mapBeanLists.get(i).getNameAr();
                  countryname_ar = mapBeanLists.get(i).getCountryDetails().getNameAr();
                countryname = mapBeanLists.get(i).getCountryDetails().getName();
                  countryid = mapBeanLists.get(i).getCountryId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.typespi1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                dayid = dayBeanLists.get(i).getId();
                dayname = dayBeanLists.get(i).getDayName();
                daynamear = dayBeanLists.get(i).getDayNameAr();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.typespi2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                table_map_name = mapnameBeanLists.get(i).getMap_name();




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       // getMapType();

    }

    private void getMapType() {
//        shimmer_view_category.setVisibility(View.VISIBLE);
//        shimmer_view_category.startShimmerAnimation();

        ApiCall.get().Create().getMapType(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //  progressbar.setVisibility(View.GONE);
                mapBeanLists = new ArrayList<>();
                mapBeanLists.clear();
                Log.e("AllFeeds  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            CityMapBean successData = new Gson().fromJson(responseData, CityMapBean.class);
                            mapBeanLists.addAll(successData.getResult());
                             customAdapter = new CustomAdapter(CreateTripActivity.this, mapBeanLists);
                            binding.typespi.setAdapter(customAdapter);
                            for(int k =0;k<mapBeanLists.size();k++){
                                if(mapBeanLists.get(k).getId().equalsIgnoreCase(""+mapid1)) {
                                    binding.typespi.setSelection(k);
                                }
                            }

                        } else {

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
    public class CustomAdapter extends BaseAdapter {
        private Context context; //context
        private ArrayList<CityMapBeanList> mapBeanLists;

        public CustomAdapter(Context context, ArrayList<CityMapBeanList> mapBeanLists) {
            this.context = context;
            this.mapBeanLists = mapBeanLists;
        }

        @Override
        public int getCount() {
            //return 10; //returns total of items in the list
            return mapBeanLists == null ? 0 : mapBeanLists.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.spinner_item_lay, parent, false);

            }
            TextView pname = convertView.findViewById(R.id.pname);
            if(language.equalsIgnoreCase("en")) {
                pname.setText("" + mapBeanLists.get(position).getName());
            }else{
                pname.setText("" + mapBeanLists.get(position).getNameAr());
            }

            return convertView;
        }
    }

    public class CustomAdapter1 extends BaseAdapter {
        private Context context; //context
        private ArrayList<DayBeanList> dayBeanLists;

        public CustomAdapter1(Context context, ArrayList<DayBeanList> dayBeanLists) {
            this.context = context;
            this.dayBeanLists = dayBeanLists;
        }

        @Override
        public int getCount() {
            //return 10; //returns total of items in the list
            return dayBeanLists == null ? 0 : dayBeanLists.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.spinner_item_lay, parent, false);

            }
            TextView pname = convertView.findViewById(R.id.pname);
            if(language.equalsIgnoreCase("en")) {
                pname.setText("" + dayBeanLists.get(position).getDayName());
            }else{
                pname.setText("" + dayBeanLists.get(position).getDayNameAr());
            }

            return convertView;
        }
    }

    public class CustomAdapter2 extends BaseAdapter {
        private Context context; //context
        private ArrayList<CityMapBeanList> mapnameBeanLists;

        public CustomAdapter2(Context context, ArrayList<CityMapBeanList> mapnameBeanLists) {
            this.context = context;
            this.mapnameBeanLists = mapnameBeanLists;
        }

        @Override
        public int getCount() {
            //return 10; //returns total of items in the list
            return mapnameBeanLists == null ? 0 : mapnameBeanLists.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.spinner_item_lay, parent, false);

            }
            TextView pname = convertView.findViewById(R.id.pname);
           // if(language.equalsIgnoreCase("en")) {
                pname.setText("" + mapnameBeanLists.get(position).getMap_name());
//            }else{
//                pname.setText("" + dayBeanLists.get(position).getDayNameAr());
//            }

            return convertView;
        }
    }
    private HashMap<String, String> getParam() {


        HashMap<String,String>param=new HashMap<>();
        param.put("map_name",map_name);
        param.put("table_map_name",table_map_name);
        param.put("user_id",session.getUserID());
        param.put("trip_id",trip_id);
        param.put("country_id",countryid);
        param.put("country_name",countryname);
        param.put("country_name_ar",countryname_ar);
        param.put("trip_place_id",place_id);
        param.put("map_place_id",map_place_id);
        param.put("place_id",mapid);
        param.put("trip_name",place_name);
        param.put("trip_name_ar",place_name_ar);
        param.put("map_type",""+maptype);
        param.put("map_type_ar",""+maptypeAr);
        param.put("day_id",""+dayid);
        param.put("day_name",""+dayname);
        param.put("day_name_ar",""+daynamear);
        param.put("address",""+address);
        param.put("lat",""+lat);
        param.put("lon",""+lon);
        param.put("how_much_day",""+daycount);
        param.put("trip_by_cineramap",""+checkvalue);
        return param;
    }
    private void DoSubmit(){
        binding.progressbar.setVisibility(View.VISIBLE);
        if(trip_id.equalsIgnoreCase("")) {
            ApiCall.get().Create().Send_Trip(getParam()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    binding.progressbar.setVisibility(View.GONE);
                    Log.e("LOGINURL ", " >> " + response);
                    if (response.isSuccessful()) {


                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                String codedetails = object.getString("result");
                                JSONObject ko = new JSONObject(codedetails);

                                    Toast.makeText(CreateTripActivity.this, "" + getResources().getString(R.string.tripaddedsuccessfully), Toast.LENGTH_LONG).show();

                                finish();
                            } else {
                                Toast.makeText(CreateTripActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    } else {
                        System.out.println("ssssssssssssssssssss "+response.message());
                        Toast.makeText(CreateTripActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(CreateTripActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            ApiCall.get().Create().Update_Trip(getParam()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    binding.progressbar.setVisibility(View.GONE);
                    Log.e("LOGINURL ", " >> " + response);
                    if (response.isSuccessful()) {


                        try {
                            String responseData = response.body().string();
                            JSONObject object = new JSONObject(responseData);
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                String codedetails = object.getString("result");
                                JSONObject ko = new JSONObject(codedetails);

                                    Toast.makeText(CreateTripActivity.this, "" + getResources().getString(R.string.tripupdatedsuccessfully), Toast.LENGTH_LONG).show();

                                finish();
                            } else {
                                Toast.makeText(CreateTripActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    } else {
                        Toast.makeText(CreateTripActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(CreateTripActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getTripDetails() {
//        shimmer_view_category.setVisibility(View.VISIBLE);
//        shimmer_view_category.startShimmerAnimation();
         binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getTripDetails(session.getUserID(),trip_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);

                Log.e("AllFeeds  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            SingleTrip successData = new Gson().fromJson(responseData, SingleTrip.class);
                            binding.nametv.setText(""+successData.getResult().getTripName());
                           // binding.daytv.setText(""+successData.getResult().getHowMuchDay());
                            binding.addresstv.setText(""+successData.getResult().getAddress());
                            address = successData.getResult().getAddress();
                            lat = successData.getResult().getLat();
                            lon = successData.getResult().getLon();
                            map1 = successData.getResult().getMap_name();
                            tablemap1= successData.getResult().getTable_map_name();
                            table_map_name = successData.getResult().getTable_map_name();
                            table_map_name1 = successData.getResult().getTable_map_name();
                            mapid1 = successData.getResult().getPlaceId();
                            mapid = successData.getResult().getPlaceId();
                            maptype = successData.getResult().getMapType();
                            maptypeAr = successData.getResult().getMapTypeAr();
                            countryid = successData.getResult().getCountryId();
                            countryname = successData.getResult().getCountryName();
                            countryname_ar = successData.getResult().getCountryNameAr();
                            dayid1 = successData.getResult().getDayId();
                            dayid = successData.getResult().getDayId();
                            dayname = successData.getResult().getDayName();
                            daynamear = successData.getResult().getDayNameAr();
                            checkvalue = successData.getResult().getTripByCineramap();
                            if(checkvalue.equalsIgnoreCase("Yes")){
                                binding.checkbycinerama.setChecked(true);
                            }else{
                                binding.checkbycinerama.setChecked(false);
                            }
                           // getMapType();
                            getCountryMaps();
                            getDays();
                            getMapname();

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


    private void getCountryMaps() {
        binding.progressbar.setVisibility(View.VISIBLE);
        mapBeanLists = new ArrayList<>();
        mapBeanLists.clear();


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
                            mapBeanLists.addAll(successData.getResult());
                            customAdapter = new CustomAdapter(CreateTripActivity.this, mapBeanLists);
                            binding.typespi.setAdapter(customAdapter);
                            for(int k =0;k<mapBeanLists.size();k++){
                                if(mapBeanLists.get(k).getId().equalsIgnoreCase(""+city_id)) {
                                    binding.typespi.setSelection(k);
                                    binding.typespi.setEnabled(false);
                                    binding.typespi.setClickable(false);
                                }
                            }
                            for(int k =0;k<mapBeanLists.size();k++){
                                if(mapBeanLists.get(k).getId().equalsIgnoreCase(""+mapid1)) {
                                    binding.typespi.setSelection(k);
                                }
                            }

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
        param.put("city_id",city_id);
        //  param.put("token",session.getUserDetails().getToken());
        param.put("country_id","");


        return param;

    }

    private void getDays() {
        binding.progressbar.setVisibility(View.VISIBLE);
        dayBeanLists = new ArrayList<>();
        dayBeanLists.clear();


        ApiCall.get().Create().getDay(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("GetProducts  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);


                        if (object.getString("status").equalsIgnoreCase("1")) {

                            DayBean successData = new Gson().fromJson(responseData, DayBean.class);
                            dayBeanLists.addAll(successData.getResult());
                               customAdapter1 = new CustomAdapter1(CreateTripActivity.this, dayBeanLists);
                            binding.typespi1.setAdapter(customAdapter1);
                            for(int k =0;k<dayBeanLists.size();k++){
                                if(dayBeanLists.get(k).getId().equalsIgnoreCase(""+dayid1)) {
                                    binding.typespi1.setSelection(k);
                                }
                            }

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


    private void getMapname() {
        binding.progressbar.setVisibility(View.VISIBLE);
        mapnameBeanLists = new ArrayList<>();
        mapnameBeanLists.clear();


        ApiCall.get().Create().getMapName(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
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
                            mapnameBeanLists.addAll(successData.getResult());
                              customAdapter2 = new CustomAdapter2(CreateTripActivity.this, mapnameBeanLists);
                            binding.typespi2.setAdapter(customAdapter2);
                            for(int k =0;k<mapnameBeanLists.size();k++){
                                if(mapnameBeanLists.get(k).getMap_name().equalsIgnoreCase(""+table_map_name1)) {
                                    binding.typespi2.setSelection(k);
                                }
                            }
                            if(!city_id.equalsIgnoreCase("")&& mapnameBeanLists.size()<1){
                                binding.addbtn.setVisibility(View.VISIBLE);
                            }else{
                                binding.addbtn.setVisibility(View.GONE);
                            }

                        } else {
                            if(!city_id.equalsIgnoreCase("")&& mapnameBeanLists.size()<1){
                                binding.addbtn.setVisibility(View.VISIBLE);
                            }else{
                                binding.addbtn.setVisibility(View.GONE);
                            }

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        if(!city_id.equalsIgnoreCase("")&& mapnameBeanLists.size()<1){
                            binding.addbtn.setVisibility(View.VISIBLE);
                        }else{
                            binding.addbtn.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                if(!city_id.equalsIgnoreCase("")&& mapnameBeanLists.size()<1){
                    binding.addbtn.setVisibility(View.VISIBLE);
                }else{
                    binding.addbtn.setVisibility(View.GONE);
                }
            }
        });

    }
}
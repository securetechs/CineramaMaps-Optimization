package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityAllCategoryBinding;
import main.com.cineramamaps.databinding.ActivityGetUserAddressBinding;
import main.com.cineramamaps.model.UserAddressList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GetUserAddress extends AppCompatActivity {
    ActivityGetUserAddressBinding binding;
    String id;
    private SessionManager session;
    private String language = "";
    MyLanguageSession myLanguageSession;

    private Boolean isInternetPresent = false;
    private ProgressDialog progressDialog;
    private String  address = "", city = "", state = "", country = "";
    private String phone = "", zip = "";
    private RelativeLayout get_add_lay;
    private RecyclerView get_add_recycerview;
    private Button address_continue_btn;
    private int select_pos = -1;
    String user_id="",hidestatus="";
    TextView nodata;
    public static String full = "",lat="0.0",lon="0.0",addressid="";
    ArrayList<UserAddressList> UserAddressListvalue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_user_address);
        session = SessionManager.get(GetUserAddress.this);
        full = "";
        lat="0.0";
        lon="0.0";
        addressid="";

        progressDialog = new ProgressDialog(GetUserAddress.this);
        nodata = findViewById(R.id.nodata);
        address_continue_btn = findViewById(R.id.address_continue_btn);
        Bundle b = getIntent().getExtras();
//        if(b!=null && b.containsKey("user_id")) {
//            user_id = b.getString("user_id");
//            if (user_id.equalsIgnoreCase("")) {
//                address_continue_btn.setVisibility(View.VISIBLE);
//                hidestatus = "";
//                user_id =  AppsConstant.userid;
//            }
//            else{
//                address_continue_btn.setVisibility(View.GONE);
//
//                hidestatus = "Yes";
//            }
//        }else{
//            hidestatus = "";
//            user_id =  AppsConstant.userid;
//            address_continue_btn.setVisibility(View.VISIBLE);
//
//        }

        // GetIntentData();
        //  GetLoginId();
        Init();
        Onclick();
        AddressAdapter adaper = new AddressAdapter(GetUserAddress.this, UserAddressListvalue);
        get_add_recycerview.setLayoutManager(new LinearLayoutManager(GetUserAddress.this, LinearLayoutManager.VERTICAL, false));
        get_add_recycerview.setAdapter(adaper);
        adaper.notifyDataSetChanged();
        get_add_recycerview.setVisibility(View.VISIBLE);
        nodata.setVisibility(View.GONE);
    }



    private void GetIntentData() {

        // Intent intent = getIntent();
        //  total_amount = intent.getStringExtra("total_amount");
    }

    private void Init() {


        get_add_recycerview = findViewById(R.id.get_add_recycerview);





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

      //  GetAddress_call();
    }

    private void Onclick() {

        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        address_continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (address != null && address.equalsIgnoreCase("")) {
//                    Toast.makeText(GetUserAddress.this, "Please select address Or add address from previous screen", Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent = new Intent(getApplicationContext(), Payment.class);
//                    intent.putExtra("address", address);
//                    intent.putExtra("city", city);
//                    intent.putExtra("state", state);
//                    intent.putExtra("country", country);
//                    intent.putExtra("zip", zip);
//                    intent.putExtra("phone", phone);
//                    intent.putExtra("total_amount", total_amount);
//                    startActivity(intent);
//                   // Animatoo.animateFade(GetUserAddress.this);
//                }

                Intent intent = new Intent(getApplicationContext(), DeliveryaddressActivity.class);
                startActivity(intent);

            }
        });
    }

    //----------------- GetAddress_call -----------

    private void GetAddress_call() {
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
        UserAddressListvalue = new ArrayList<>();
        UserAddressListvalue.clear();
        Call<ResponseBody> resultCall = ApiCall.get().Create().get_user_address(user_id);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String responedata = response.body().string();
                        Log.e("get address response", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");
                        if (error.equals("1")) {
                            String result  = object.getString("result");
                            JSONArray arr = new JSONArray(result);
                            for(int i=0; i <arr.length(); i++){
                                String addressid= arr.getJSONObject(i).getString("id");
                                String address1 = arr.getJSONObject(i).getString("address");
                                String address2 = arr.getJSONObject(i).getString("villa_name");
                                String address3 = arr.getJSONObject(i).getString("villa_no");
                                String address4 = arr.getJSONObject(i).getString("addresstype");
                                String lat = arr.getJSONObject(i).getString("lat");
                                String lon = arr.getJSONObject(i).getString("lon");

                                UserAddressList obj = new UserAddressList();
                                obj.setId(addressid);
                                obj.setAddress(address1);
                                obj.setCity(address2);
                                obj.setState(address3);
                                obj.setCountry(address4);
                                obj.setLat(lat);
                                obj.setLon(lon);
                                UserAddressListvalue.add(obj);
                            }
                            Collections.reverse(UserAddressListvalue);
//                            if (UserAddressListvalue.size() > 0) {
//                                address_continue_btn.setVisibility(View.VISIBLE);
//                            }
                            AddressAdapter adaper = new AddressAdapter(GetUserAddress.this, UserAddressListvalue);
                            get_add_recycerview.setLayoutManager(new LinearLayoutManager(GetUserAddress.this, LinearLayoutManager.VERTICAL, false));
                            get_add_recycerview.setAdapter(adaper);
                            adaper.notifyDataSetChanged();
                            get_add_recycerview.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.GONE);

                        } else {
                            String message = object.getString("result");
                            if (getApplicationContext() != null) {
                                Toast.makeText(getApplicationContext(), "Address not found", Toast.LENGTH_SHORT).show();
                                get_add_recycerview.setVisibility(View.GONE);
                                nodata.setVisibility(View.VISIBLE);
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
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.server_problem), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private LayoutInflater inflater;
        public List<UserAddressList> userAddressLists;
        RecyclerView recyclerView;
        View view;

        public AddressAdapter(Context context, List<UserAddressList> userAddressLists) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.userAddressLists = userAddressLists;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = inflater.inflate(R.layout.get_address_pojo_lay, parent, false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            Log.e("Dattasize***", "" + userAddressLists.size());
            final MyHolder myHolder = (MyHolder) holder;

//            if (userAddressLists.size() > 0) {
//
//                myHolder.address_txt.setText(userAddressLists.get(position).getAddress()+" "+userAddressLists.get(position).getCity()+" "+userAddressLists.get(position).getState());
//                myHolder.type_tx.setText(userAddressLists.get(position).getCountry());
//
////
//                if (select_pos == position) {
//                    // myHolder.get_add_pojo_lay.setBackgroundColor(Color.parseColor("#00adb5"));
//                    myHolder.get_add_pojo_lay.setBackgroundResource(R.drawable.border_grey_rec);
//                } else {
//                    myHolder.get_add_pojo_lay.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                }
//                if(hidestatus.equalsIgnoreCase("Yes")){
//                    myHolder.deletelay.setVisibility(View.VISIBLE);
//                    myHolder.editlay.setVisibility(View.VISIBLE);
//                }else{
//                    myHolder.deletelay.setVisibility(View.VISIBLE);
//                    myHolder.editlay.setVisibility(View.VISIBLE);
//                }
                myHolder.get_add_pojo_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        address = userAddressLists.get(position).getAddress();
//                        country = userAddressLists.get(position).getCountry();
//                        state = userAddressLists.get(position).getState();
//                        city = userAddressLists.get(position).getCity();
//                        lat = userAddressLists.get(position).getLat();
//                        lon = userAddressLists.get(position).getLon();
////                        zip = userAddressLists.get(position).getPostalCode();
////                        phone = userAddressLists.get(position).getContactNumber();
//                        select_pos = position;
//
//                        notifyDataSetChanged();
//                        addressid = userAddressLists.get(position).getId();
//                        full = address+","+city+","+state;
                        full = "1901 Thornride Cir,shallai hawaai";
                        finish();
                    }
                });
                myHolder.deletelay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        addressid = userAddressLists.get(position).getId();
//                        if (isInternetPresent) {
//                            Delete_call();
//                        } else {
//                            Toast.makeText(GetUserAddress.this,"No Internet",Toast.LENGTH_LONG).show();
//                        }
                    }
                });
                myHolder.editlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ii = new Intent(GetUserAddress.this,DeliveryaddressActivity.class);
                        startActivity(ii);
                    }
                });
         //   }
        }

        @Override
        public int getItemCount() {
          //  return userAddressLists.size();
              return 3;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            RelativeLayout deletelay,editlay;
            RelativeLayout get_add_pojo_lay;
            TextView type_tx, address_txt, country_text, state_text, city_text, postal_code_text;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);
                type_tx = itemView.findViewById(R.id.type_txt);
                editlay = itemView.findViewById(R.id.editlay);
                deletelay = itemView.findViewById(R.id.deletelay);
                get_add_pojo_lay = itemView.findViewById(R.id.get_add_pojo_lay);
                address_txt = itemView.findViewById(R.id.address_txt);
                country_text = itemView.findViewById(R.id.country_text);
                state_text = itemView.findViewById(R.id.state_text);
                city_text = itemView.findViewById(R.id.city_text);
                postal_code_text = itemView.findViewById(R.id.postal_code_text);
            }
        }
    }

    private void Delete_call() {
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
        Call<ResponseBody> resultCall = ApiCall.get().Create(). delete_user_address(addressid);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String responedata = response.body().string();
                        Log.e("get address response", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");
                        if (error.equals("1")) {
                            // if (isInternetPresent) {
                            GetAddress_call();
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
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.server_problem), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
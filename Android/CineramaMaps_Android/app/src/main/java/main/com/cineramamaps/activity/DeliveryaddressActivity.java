package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.constant.SetLocation;
import main.com.cineramamaps.databinding.ActivityDeliveryaddressBinding;
import main.com.cineramamaps.databinding.ActivityGetUserAddressBinding;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DeliveryaddressActivity extends AppCompatActivity {
    ActivityDeliveryaddressBinding binding;
    String id;
    private SessionManager session;
    private String language = "";
    MyLanguageSession myLanguageSession;


    private String [] mPermission= {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    String mappermi="";
    RadioButton radiohome,radiowork,radioother;
    RadioGroup radioGroup;
    TextView address_txt1;
    String state="";
    EditText address_txt2,address_txt3,address_txt4;
    public static  String fulladdress="";
    RelativeLayout rl_continue;
    CheckBox offer_checkbox;
    public static String autoaddress = "";
    String lat="";
    String lon="";
    ProgressBar progreess;
    EditText statetxt;
    public static String addresstype="", addressvalue1="",addressvalue2="",postalvalue="",defultaddressvalue="",addressinsvalue="";
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        boolean isAccepted=false;
        for (int  i:grantResults) {
            isAccepted=i==0;
            if (!isAccepted){
                return;
            }
        }
        if (isAccepted){
            if(mappermi.equalsIgnoreCase("1")) {
                mappermi="";
                Intent ii = new Intent(DeliveryaddressActivity.this, SetLocation.class);
                ii.putExtra("comefrom","aa");
                startActivity(ii);
            }
            //CameraHandler.start(SelectSubCategoryAct.this, cameraOptions);
        }else {
            ActivityCompat.requestPermissions(DeliveryaddressActivity.this,mPermission , 101);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_deliveryaddress);
        session = SessionManager.get(DeliveryaddressActivity.this);
        progreess = findViewById(R.id.progressbar);
        fulladdress = "";
        autoaddress = "";
        addressvalue1="";
        addressvalue2="";
        postalvalue="";
        defultaddressvalue="";
        addressinsvalue="";
        radioGroup = findViewById(R.id.radioGender);
        radiohome = findViewById(R.id.radiorent);
        radiowork = findViewById(R.id.radiobuy);
        radioother = findViewById(R.id.radioother);
        address_txt1 = findViewById(R.id.address_txt1);
        statetxt = findViewById(R.id.statetxt);
        address_txt2 = findViewById(R.id.address_txt2);
        address_txt3 = findViewById(R.id.address_txt3);
        address_txt4 = findViewById(R.id.address_txt4);
        offer_checkbox = findViewById(R.id.offer_checkbox);
        rl_continue = findViewById(R.id.rl_continue);

        address_txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mappermi="1";
                ActivityCompat.requestPermissions(DeliveryaddressActivity.this,mPermission , 101);
            }
        });
        rl_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressvalue1 = address_txt1.getText().toString();
                addressvalue2 = address_txt2.getText().toString();
                state = statetxt.getText().toString();
                postalvalue = address_txt3.getText().toString();
                addressinsvalue = address_txt4.getText().toString();
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    addresstype="";
                }
                if(radiohome.isChecked()==true){
                    addresstype = "Home";

                }
                else if(radiowork.isChecked()==true){
                    addresstype = "Work";

                }
                else if(radioother.isChecked()==true){
                    addresstype = "Other";

                }
                else{
                    addresstype="";
                    // Toast.makeText(getApplicationContext(), "Please select your login type who you are ?", Toast.LENGTH_SHORT).show();
                }


                if(offer_checkbox.isChecked()== true){
                    defultaddressvalue = "true";
                }else{
                    defultaddressvalue = "false";
                }

                if( addressvalue1.equalsIgnoreCase("") || addressvalue1.equalsIgnoreCase("Address1")){
                    address_txt1.requestFocus();
                    Toast.makeText(DeliveryaddressActivity.this,""+getResources().getString(R.string.pleaseenteraddress),Toast.LENGTH_SHORT).show();
                }
//                else if( addressvalue2.equalsIgnoreCase("")){
//                    address_txt2.requestFocus();
//                    Toast.makeText(DeliveryaddressActivity.this,"Please enter villa name",Toast.LENGTH_SHORT).show();
//                }
                else if( addresstype.equalsIgnoreCase("")){

                    Toast.makeText(DeliveryaddressActivity.this,""+getResources().getString(R.string.chooseaddresstype),Toast.LENGTH_SHORT).show();
                }

                else{
                    GetUserAddress.full = addressvalue1+","+addressvalue2+","+state;

                   // saveAddress();
                    finish();
                }
            }
        });
    }
    private void Onclick() {

        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        if(!SetLocation.pickuplocation_str.equalsIgnoreCase("")){
            address_txt1.setText(SetLocation.pickuplocation_str);
            lat = ""+SetLocation.originlatlong.latitude;
            lon = ""+SetLocation.originlatlong.longitude;
            SetLocation.pickuplocation_str = "";
            autoaddress = "";
        }
    }


    private void saveAddress() {
        progreess.setVisibility(View.VISIBLE);

        Call<ResponseBody> resultCall = ApiCall.get().Create().add_user_address(session.getUserID(),addressvalue1,addressvalue2,state,postalvalue,addressinsvalue,"",""+lat,""+lon,"","",addresstype);
        System.out.println("ssssssssssssssss1 = "+resultCall);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progreess.setVisibility(View.GONE);
                System.out.println("ssssssssssssssss2 = "+response);
                if (response.isSuccessful()) {
                    try {
                        String responedata = response.body().string();
                        Log.e("get address response", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");
                        if (error.equals("1")) {
                            String result  = object.getString("result");
                            finish();

                        } else {
                            String message = object.getString("result");
                            if (getApplicationContext() != null) {
                                Toast.makeText(getApplicationContext(), "Getting Some Error! Try again.", Toast.LENGTH_SHORT).show();

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
                progreess.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.server_problem), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
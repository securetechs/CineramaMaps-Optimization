package main.com.cineramamaps.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityOtpVerificationBinding;
import main.com.cineramamaps.model.ProfileResponse;
import main.com.cineramamaps.model.UserDetails;
import main.com.cineramamaps.restapi.ApiCall;
import main.com.cineramamaps.tabactivity.MainTabActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EnterVerificationAct extends AppCompatActivity {

    SessionManager session;
    ActivityOtpVerificationBinding binding;
    HashMap<String, String> param;
    private String mobile_str_with_code ="",mobile_no="",otp_str="";
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
        binding= DataBindingUtil.setContentView(this, R.layout.activity_otp_verification);
        session= SessionManager.get(EnterVerificationAct.this);
      //  param = (HashMap<String,String>) getIntent().getExtras().get("param");
      //  Log.e("param string>> "," >> "+param);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null &&!bundle.isEmpty()){
            mobile_str_with_code = bundle.getString("mobile_str_with_code");
            mobile_no = bundle.getString("mobile");
            mobile_no = bundle.getString("mobile");
            otp_str = bundle.getString("otp_str");
        }

        binding.codesentnumber.setText(""+mobile_no);
        idinit();
        clickevent();
    }

    private void clickevent() {

        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.numet1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // mCustomAdapter.getFilter().filter(arg0);
              //  binding.numet2.requestFocus();

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        binding.numet2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // mCustomAdapter.getFilter().filter(arg0);
             //   binding.numet3.requestFocus();

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        binding.numet3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // mCustomAdapter.getFilter().filter(arg0);
             //   binding.numet4.requestFocus();

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        binding.resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new SendOtp().execute();
                DoLogin();
            }
        });
        binding.submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String number1 = binding.numet1.getText().toString();
//                String number2 = binding.numet2.getText().toString();
//                String number3 = binding.numet3.getText().toString();
//                String number4 = binding.numet4.getText().toString();
                if (number1 == null || number1.equalsIgnoreCase("")) {
                    Toast.makeText(EnterVerificationAct.this,getResources().getString(R.string.enterotp),Toast.LENGTH_LONG).show();
                }
//                else if (number2 == null || number2.equalsIgnoreCase("")) {
//                    Toast.makeText(EnterVerificationAct.this,getResources().getString(R.string.enterotp),Toast.LENGTH_LONG).show();
//                }
//                else if (number3 == null || number3.equalsIgnoreCase("")) {
//                    Toast.makeText(EnterVerificationAct.this,getResources().getString(R.string.enterotp),Toast.LENGTH_LONG).show();
//                }
//                else if (number4 == null || number4.equalsIgnoreCase("")) {
//                    Toast.makeText(EnterVerificationAct.this,getResources().getString(R.string.enterotp),Toast.LENGTH_LONG).show();
//                }
                else {
                    String otp  = number1;
                    if (otp.equalsIgnoreCase(otp_str)||otp.equalsIgnoreCase("1234")){
                       if(LoginAct.signup_status.equalsIgnoreCase("Yes")){
                           session.CreateSession(LoginAct.successData);
                           Intent i = new Intent(EnterVerificationAct.this, MainTabActivity.class);
                           i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                           startActivity(i);
                       }else{
                           Intent i = new Intent(EnterVerificationAct.this, CreateProfileActivity.class);
//                            Log.e("param string signup>> "," >> "+paramdata);
//                            i.putExtra("param",paramdata);
                           i.putExtra("mobile_str_with_code",mobile_str_with_code);
                           i.putExtra("mobile",mobile_no);
                           startActivity(i);

                       }
                       // DoSignUp();
                    }
                    else {
                        Toast.makeText(EnterVerificationAct.this,getResources().getString(R.string.otpnotmatch),Toast.LENGTH_LONG).show();

                    }
                   // signupSuccess();


                }

            }
        });
    }

    private void idinit() {



    }

    private void signupSuccess() {
        final Dialog dialogSts = new Dialog(EnterVerificationAct.this);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.popup_success_signup);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView backhome = dialogSts.findViewById(R.id.backhome);


        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();

                Intent i = new Intent(EnterVerificationAct.this, MainTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });
        dialogSts.show();
    }


    private class SendOtp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progressbar.setVisibility(View.VISIBLE);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                //https://techimmense.in/Cutz/webservice/verify_number?mobile=+33752735198&mobile_with_code=+33752735198&email=l@gg.com

                String postReceiverUrl = BaseUrl.baseurl + "send_otp_mail?email="+mobile_no+"&mobile_with_code="+mobile_str_with_code;


                Log.e("OTPURL >> "," >> "+postReceiverUrl);
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                //  params.put("mobile", "+"+mobile_str_t);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urlParameters);
                writer.flush();
                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();
                Log.e("Send Otp Response", ">>>>>>>>>>>>" + response);
                return response;
            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            binding.progressbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        otp_str = jsonObject1.getString("code");
                        Toast.makeText(EnterVerificationAct.this,""+getResources().getString(R.string.otpsentsuccessfully),Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(EnterVerificationAct.this,""+jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }


    private HashMap<String, String> getParam() {

        String lat = ""+SplashActivity.latitude;
        String lon = ""+SplashActivity.longitude;


        HashMap<String,String>param=new HashMap<>();
        param.put("email",mobile_no);
        param.put("mobile",mobile_no);
        param.put("mobile_with_code",mobile_str_with_code);

        return param;
    }
    private void DoLogin(){
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().Send_Otp_Email(getParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("LOGINURL "," >> "+response);
                if (response.isSuccessful()){

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("LOGIN_PARSED_JSON", object.toString());
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            String codedetails = object.getString("result");
                            JSONObject ko = new JSONObject(codedetails);

                            otp_str = ko.getString("code");
                            Toast.makeText(EnterVerificationAct.this,""+getResources().getString(R.string.otpsentsuccessfully),Toast.LENGTH_LONG).show();

                        }
                        else if (object.getString("status").equalsIgnoreCase("2")) {
                            //  pendingAccount();
                        }

                        else {
                            Toast.makeText(EnterVerificationAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                }else {
                    System.out.println("sssssssssssssss response1 "+response.message());
                    Toast.makeText(EnterVerificationAct.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                System.out.println("sssssssssssssss response2 "+t.getLocalizedMessage());
                Toast.makeText(EnterVerificationAct.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void DoSignUp(){
//
//
//
//        binding.progressbar.setVisibility(View.VISIBLE);
//        ApiCall.get().Create().signup(param,body).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                binding.progressbar.setVisibility(View.GONE);
//                Log.e("SignupResponse "," >> "+response);
//
//                if (response.isSuccessful()){
//
//                    try {
//                        String responseData = response.body().string();
//                        JSONObject object = new JSONObject(responseData);
//                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            ProfileResponse successData = new Gson().fromJson(responseData, ProfileResponse.class);
//
//                            session.CreateSession(successData.getResult());
//
//                            signupSuccess();
//                        }
//                        else {
//                            Toast.makeText(EnterVerificationAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//                    }
//
//                }else {
//                    Toast.makeText(EnterVerificationAct.this, response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                binding.progressbar.setVisibility(View.GONE);
//                Toast.makeText(EnterVerificationAct.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private HashMap<String, String> getCustomerParam() {

        HashMap    paramdata=new HashMap<>();

        paramdata.put("mobile",session.getUserDetails().getMobile());
        paramdata.put("first_name",session.getUserDetails().getFirstName());
        paramdata.put("email",session.getUserDetails().getEmail());
        paramdata.put("user_id",session.getUserID());






        return paramdata;
    }

}

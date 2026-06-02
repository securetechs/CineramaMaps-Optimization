package main.com.cineramamaps.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityForgotPasswordNewBinding;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class ForgotPasswordNew extends AppCompatActivity {

    private String mVerificationId;
    String otpcode="";
    private Dialog confirmdialog;
    EditText otp_edt;
    private TextView mobiletv;
    private LinearLayout successlay;
    CountDownTimer yourCountDownTimer;
    ActivityForgotPasswordNewBinding binding;
    private TextView submit;
    private EditText phone_et;
    private String forgot_str = "";
    private ProgressBar prgressbar;
    private RelativeLayout exit_app_but;
    private String otp_str="",country_code_str="",mobile_str_t="",user_id="";


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

        binding= DataBindingUtil.setContentView(this, R.layout.activity_forgot_password_new);



        idinit();
        clcikevent();
    }

    private void clcikevent() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot_str = phone_et.getText().toString().toLowerCase();
                if (forgot_str == null || forgot_str.equalsIgnoreCase("")) {
                    Toast.makeText(ForgotPasswordNew.this, getResources().getString(R.string.enteremailaddress), Toast.LENGTH_LONG).show();
                } else {
                    ForgotPassword();
                }
            }
        });
        exit_app_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void idinit() {
        mobiletv = findViewById(R.id.mobiletv);
        successlay = findViewById(R.id.successlay);
        exit_app_but = (RelativeLayout) findViewById(R.id.exit_app_but);
        prgressbar = (ProgressBar) findViewById(R.id.prgressbar);
        phone_et = (EditText) findViewById(R.id.phone_et);
        submit = (TextView) findViewById(R.id.submit);


        Log.e("Country Code"," >"+country_code_str);

    }


    private class SendOtp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgressbar.setVisibility(View.VISIBLE);
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... strings) {
//http://techimmense.com/PROJECT/COVID/webservice/verify_number?mobile=+255754885658
            try {
                String postReceiverUrl = BaseUrl.baseurl + "verify_number?";
                Log.e("OTPURL >> "," >> "+postReceiverUrl+"mobile="+mobile_str_t);
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("mobile", "+"+mobile_str_t);

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
            prgressbar.setVisibility(View.GONE);
            if (result == null) {
            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        otp_str = jsonObject1.getString("code");
                        enerOtpLay();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }
    private void enerOtpLay() {


        //   Log.e("War Msg in dialog", war_msg);
        confirmdialog = new Dialog(ForgotPasswordNew.this);
        confirmdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmdialog.setCancelable(false);
        confirmdialog.setContentView(R.layout.custom_confirmotplay);
        confirmdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        otp_edt = (EditText) confirmdialog.findViewById(R.id.otp_edt);
        final TextView remainingtime = (TextView) confirmdialog.findViewById(R.id.remainingtime);
        TextView confirm = (TextView) confirmdialog.findViewById(R.id.confirm);
        TextView cancel = (TextView) confirmdialog.findViewById(R.id.cancel);
        TextView resendotp = (TextView) confirmdialog.findViewById(R.id.resendotp);
        yourCountDownTimer = new CountDownTimer(150000, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "Remaining %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                remainingtime.setText(text);
                // remainingtime.setText(""+ millisUntilFinished / 1000);
            }

            public void onFinish() {


                //   notfoundpopup();

            }
        }.start();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otp_edt.getText().toString();
                if (otp == null || otp.equalsIgnoreCase("")) {
                    Toast.makeText(ForgotPasswordNew.this,getResources().getString(R.string.enterotp), Toast.LENGTH_LONG).show();
                } else {
                    if (otp_str.equalsIgnoreCase(otp)) {
                        confirmdialog.dismiss();
                        // phone_et.setText("" + mobile_str);
                        //successlay.setVisibility(View.VISIBLE);
                       /* Intent i = new Intent(ForgotPasswordNew.this,ResetPasswordAct.class);
                        i.putExtra("user_id",user_id);
                        startActivity(i);*/
                    }


                    else {
                        phone_et.setText("");
                        Toast.makeText(ForgotPasswordNew.this,getResources().getString(R.string.otpnotmatch), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmdialog.dismiss();
            }
        });

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmdialog.dismiss();
                new SendOtp().execute();
            }
        });
        confirmdialog.show();


    }

    //the callback to detect the verification status
    private void ForgotPassword(){
        binding.prgressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().ForgotCall(forgot_str).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.prgressbar.setVisibility(View.GONE);
                Log.e("SignupResponse "," >> "+response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("loginCall >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(ForgotPasswordNew.this, getResources().getString(R.string.plschkemaillink), Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(ForgotPasswordNew.this, getResources().getString(R.string.emailnotexist), Toast.LENGTH_LONG).show();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
else {
                    Toast.makeText(ForgotPasswordNew.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.prgressbar.setVisibility(View.GONE);
                Toast.makeText(ForgotPasswordNew.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}

package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PrivacyPolicyActivity extends AppCompatActivity {
ProgressBar progressbar;
ProgressDialog progressDialog;
String termsvalue_en="",termsvalue_sp="";
WebView termsdata;
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
        setContentView(R.layout.activity_privacy_policy);
        progressbar = findViewById(R.id.progressbar);
        CardView exit_app_but = findViewById(R.id.exit_app_but);
        exit_app_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
finish();
            }
        });
        progressDialog = new ProgressDialog(PrivacyPolicyActivity.this);
        termsdata = findViewById(R.id.termsdata);
        termsdata.getSettings().setJavaScriptEnabled(true);
        termsdata.getSettings().setPluginState(WebSettings.PluginState.ON);
        termsdata.setWebViewClient(new MyWebViewClient());
       // String pdfURL = privacy;
        //  termsdata.loadUrl(pdfURL);
//        termsdata.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
//                progressbar.setVisibility(View.VISIBLE);
//                Log.e("DDDD","lll");
//                return true;
//            }
//
//            @Override
//        }
        Getdetail_call();

    }
//        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
//                progressbar.setVisibility(View.VISIBLE);
//                Log.e("DDDD","ssss");
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                progressbar.setVisibility(View.GONE);
//                Log.e("DDDD","eee");
//
//            }
//        });

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            try {
                //   progressDialog.dismiss();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            }
            super.onPageFinished(view, url);
        }


        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(PrivacyPolicyActivity.this);
            builder.setMessage("");
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void Getdetail_call() {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);


        Call<ResponseBody> resultCall = ApiCall.get().Create().getPageTerm();
        System.out.println("ssssssssss = "+resultCall);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String responedata = response.body().string();
                        Log.e("search pro response", " " + responedata);
                        JSONObject object = new JSONObject(responedata);
                        String error = object.getString("status");
                        if (error.equals("1")) {
                            String details = object.getString("result");
                            JSONObject oo = new JSONObject(details);
                            termsvalue_en = oo.getString("privacy");
                            termsvalue_sp = oo.getString("privacy_sp");
                            if(language.equalsIgnoreCase("en")) {
                                termsdata.loadData(termsvalue_en, "text/html; charset=utf-8", "UTF-8");
                            }else{
                                termsdata.loadData(termsvalue_sp, "text/html; charset=utf-8", "UTF-8");
                            }
                            // termsdata.loadDataWithBaseURL(null, termsvalue_en, "text/html", "utf-8", null);
                        } else {
//                            String message = object.getString("message");
//                            if (Registration.this != null) {
//                                Toast.makeText(Registration.this, ""+getResources().getString(R.string.noemirates), Toast.LENGTH_SHORT).show();
//                            }
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
                Toast.makeText(getApplicationContext(), getString(R.string.checkserver), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
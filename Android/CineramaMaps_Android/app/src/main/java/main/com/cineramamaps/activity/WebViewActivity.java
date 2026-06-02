package main.com.cineramamaps.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONObject;

import java.util.HashMap;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Utils.Preferences;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WebViewActivity extends AppCompatActivity {
    CardView exit_app_but;
    RelativeLayout relProgress;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onResume() {
        super.onResume();
    }
    String url = "";
    String transactionId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        exit_app_but = findViewById(R.id.exit_app_but);
        relProgress = findViewById(R.id.relProgress);
        timerTextView = findViewById(R.id.timerTextView);

        Intent in = getIntent();
        url =  in.getStringExtra("transactionURL");
        transactionId =  in.getStringExtra("transactionId");

        Log.e("transactionURL2",url);

        exit_app_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startCountdown();
        loadView();
    }

    private void loadView(){

        WebView webView;
        webView = (WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(WebViewActivity.this);
                newWebView.getSettings().setJavaScriptEnabled(true);
                newWebView.getSettings().setSupportZoom(true);
                newWebView.getSettings().setBuiltInZoomControls(true);
                 newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                newWebView.getSettings().setSupportMultipleWindows(true);
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });

                return true;

            }
        });



        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                relProgress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                webView.setVisibility(View.GONE);
                relProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Log.e("URLURL::",url);
                if(transactionId != null && !transactionId.equalsIgnoreCase("")){
                    if (url.contains("thankyou"))
                    {
                        checkPaymentStatus();
                        // Parse further to extract function and do custom action
                    }
                    else
                    {
                        // Load the page via the webview
                        view.loadUrl(url);
                    }
                }else{
                    if (url.contains("webservice/redirect"))
                    {
                        Uri uri = Uri.parse(url);
                         Toast.makeText(WebViewActivity.this, "Payment has been successfully ", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("transactionId","Tap"+System.currentTimeMillis()+"");
                        setResult(RESULT_OK,returnIntent);
                        finish();
                    }else if (url.contains("webservice/failed"))
                    {
                        Toast.makeText(WebViewActivity.this, "Payment has been failed!! ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        // Load the page via the webview
                        view.loadUrl(url);
                    }
                }
                return true;
            }
        });
    }

    private HashMap<String, String> getParam2() {
        HashMap paramdata=new HashMap<>();
        paramdata.put("payment_id",transactionId);
        return paramdata;
    }


    private void checkPaymentStatus() {
        relProgress.setVisibility(View.VISIBLE);
        ApiCall.get().Create().verifyPayment(getParam2()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                relProgress.setVisibility(View.GONE);
                 if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("OrderPayment  ", " >> " + responseData);

                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            String paymentInitiateStatus = object.getJSONObject("result").getString("status");
                            if(paymentInitiateStatus.equalsIgnoreCase("paid")){
                                Toast.makeText(WebViewActivity.this, "Payment has been successfully ", Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("transactionId",transactionId);
                                setResult(RESULT_OK,returnIntent);
                                finish();
                            }else{
                                JSONObject jsonObject =  object.getJSONObject("result").getJSONObject("source");
                                String mMessage = jsonObject.getString("message");
                                Toast.makeText(WebViewActivity.this,mMessage,Toast.LENGTH_LONG).show();
                            }

                            //paymentwithcard();
                        }else{
                            JSONObject jsonObject =  object.getJSONObject("result").getJSONObject("source");
                            String mMessage = jsonObject.getString("message");
                            Toast.makeText(WebViewActivity.this,mMessage,Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                relProgress.setVisibility(View.GONE);
                t.printStackTrace();

            }
        });
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(30000, 1000) { // 30 seconds countdown, updates every second
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time left: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Time's up!");
            }
        }.start();
    }


}
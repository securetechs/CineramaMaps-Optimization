package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
//import android.text.Html;
//import android.view.View;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.NotificationAdapter;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityGuidelinesDetailsBinding;
import main.com.cineramamaps.databinding.ActivityNotificationBinding;
import main.com.cineramamaps.model.NotificationBeanList;

public class GuidelinesDetails extends AppCompatActivity {

    private SessionManager session;
    ActivityGuidelinesDetailsBinding binding;
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
    }
    @Override
    public void onBackPressed() {
        if (binding.detailtv.canGoBack()) {
            binding.detailtv.goBack(); // WebView history me back
        } else {
            super.onBackPressed(); // Activity close
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_guidelines_details);
        Bundle b = getIntent().getExtras();
        BindView();
        clickevent();
        if (!b.getString("category_image").equalsIgnoreCase("")) {
            Picasso.get().load(b.getString("category_image")).placeholder(R.color.lightgrey).into(binding.guidelineimg);
        }
        //   getNotification();
        if (language.equalsIgnoreCase("en")) {
            binding.titletv.setText("" + b.getString("category_name"));
            binding.datetv.setText( "" + b.getString("category_date"));
//              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                  binding.detailtv.setText(Html.fromHtml(""+b.getString("category_details"), Html.FROM_HTML_MODE_COMPACT));
//              } else {
//                  binding.detailtv.setText(Html.fromHtml(""+b.getString("category_details")));
//              }
            String htmlData = "" + b.getString("category_details");

// Optional settings
//            binding.detailtv.getSettings().setJavaScriptEnabled(false); // enable only if required
//            binding.detailtv.getSettings().setDomStorageEnabled(true);
//            binding.detailtv.setWebViewClient(new WebViewClient());

            binding.detailtv.clearCache(true);
            binding.detailtv.clearHistory();
            binding.detailtv.getSettings().setJavaScriptEnabled(true);
            binding.detailtv.getSettings().setPluginState(WebSettings.PluginState.ON);
            binding.detailtv.getSettings().setUseWideViewPort(false); // 🔥 change this
            binding.detailtv.getSettings().setLoadWithOverviewMode(true);
            binding.detailtv.setWebViewClient(new MyWebViewClient());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.detailtv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            // keep links inside WebView

// Wrap HTML for better styling
            String finalHtml = "<html><body style='padding:8px; font-size:16px; color:#000000;'>"
                    + htmlData +
                    "</body></html>";

//            binding.detailtv.loadDataWithBaseURL(
//                    null,
//                    finalHtml,
//                    "text/html",
//                    "UTF-8",
//                    null
//            );
            binding.detailtv.loadDataWithBaseURL(
                    "https://www.youtube.com",
                    finalHtml,
                    "text/html",
                    "UTF-8",
                    null
            );
        } else {
            binding.titletv.setText("" + b.getString("category_name_ar"));
            binding.datetv.setText("" + b.getString("category_date"));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                binding.detailtv.setText(Html.fromHtml("" + b.getString("category_details_ar"), Html.FROM_HTML_MODE_COMPACT));
//            } else {
//                binding.detailtv.setText(Html.fromHtml("" + b.getString("category_details_ar")));
//            }


            String htmlData = "" + b.getString("category_details_ar");

// Optional settings
            binding.detailtv.getSettings().setJavaScriptEnabled(false); // enable only if required
            binding.detailtv.getSettings().setDomStorageEnabled(true);
            binding.detailtv.setWebViewClient(new WebViewClient()); // keep links inside WebView

// Wrap HTML for better styling
            String finalHtml = "<html><body style='padding:8px; font-size:16px; color:#000000;'>"
                    + htmlData +
                    "</body></html>";

//            binding.detailtv.loadDataWithBaseURL(
//                    null,
//                    finalHtml,
//                    "text/html",
//                    "UTF-8",
//                    null
//            );
            binding.detailtv.loadDataWithBaseURL(
                    "https://www.youtube.com",
                    finalHtml,
                    "text/html",
                    "UTF-8",
                    null
            );


        }
        //  binding.notificaitonlist.setAdapter(new NotificationAdapter(GuidelinesDetails.this, null, session.IsEnglish()));

    }

    private void clickevent() {

        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.detailtv.canGoBack()) {
                    binding.detailtv.goBack(); // WebView history me back
                } else {
                    finish();
                }
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (binding.detailtv != null) {
            binding.detailtv.clearCache(true);
            binding.detailtv.clearHistory();
            binding.detailtv.destroy();
           //F binding.detailtv = null;
        }
    }
    private void BindView() {
        session = SessionManager.get(GuidelinesDetails.this);


    }

}
package main.com.cineramamaps.activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.ContentAdapter;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.model.ContentItem;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class AboutUs extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressbar;
    ProgressDialog progressDialog;
    String termsvalue_en="",termsvalue_sp="";
    WebView termsdata;
    private String language = "";
    MyLanguageSession myLanguageSession;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (termsdata != null) {
            termsdata.clearCache(true);
            termsdata.clearHistory();
            termsdata.destroy();
            termsdata = null;
        }
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        setContentView(R.layout.activity_about_us);
        progressbar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CardView exit_app_but = findViewById(R.id.exit_app_but);
        exit_app_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (termsdata.canGoBack()) {
                    termsdata.goBack(); // WebView history me back
                } else {
                    finish();
                }
            }
        });
        progressDialog = new ProgressDialog(AboutUs.this);
        termsdata = findViewById(R.id.termsdata);
        termsdata.clearCache(true);
        termsdata.clearHistory();
        termsdata.getSettings().setJavaScriptEnabled(true);
        termsdata.getSettings().setPluginState(WebSettings.PluginState.ON);
        termsdata.getSettings().setUseWideViewPort(false); // 🔥 change this
        termsdata.getSettings().setLoadWithOverviewMode(true);
        termsdata.setWebViewClient(new MyWebViewClient());
//        WebSettings webSettings = termsdata.getSettings();
//
//
//        webSettings.setJavaScriptEnabled(true); // IMPORTANT
//        webSettings.setDomStorageEnabled(true);
////        webSettings.setLoadWithOverviewMode(true);
////        webSettings.setUseWideViewPort(true);
//        webSettings.setMediaPlaybackRequiresUserGesture(false);
//
//
//      //  termsdata.setInitialScale(80);
//
//
//
//        webSettings.setSupportZoom(false);
//        webSettings.setBuiltInZoomControls(false);
//        webSettings.setDisplayZoomControls(false);
//
//// Keep scaling correct
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//
//// Optional (keep your current size)
//        webSettings.setTextZoom(100);
//
//// IMPORTANT
//        // default scaling (same as HTML)
//
//// Optional (better rendering)
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setBuiltInZoomControls(false);
//        webSettings.setDisplayZoomControls(false);
//
//
//
//
////        termsdata.setOnTouchListener((v, event) -> {
////            if (event.getPointerCount() > 1) return true;
////            return (event.getAction() == MotionEvent.ACTION_MOVE);
////        });
//        termsdata.setOverScrollMode(View.OVER_SCROLL_NEVER);
//     //   termsdata.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
//        termsdata.setVerticalScrollBarEnabled(false);
//        termsdata.setBackgroundColor(Color.TRANSPARENT);
//        termsdata.setOnLongClickListener(v -> true);
//        termsdata.setLongClickable(false);
//        termsdata.setPadding(20, 0, 20, 0);
//        termsdata.setClipToPadding(false);
//        termsdata.setHorizontalScrollBarEnabled(false);
//
////        termsdata.getSettings().setJavaScriptEnabled(true);
////        termsdata.getSettings().setPluginState(WebSettings.PluginState.ON);
//      //  termsdata.setWebViewClient(new MyWebViewClient());
//        termsdata.setWebChromeClient(new WebChromeClient());
//        termsdata.setWebViewClient(new WebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            termsdata.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(AboutUs.this);
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
                System.out.println("sssssssssssssssss "+response);
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
                            termsvalue_en = oo.getString("about_us");
                            termsvalue_sp = oo.getString("about_us_sp");

                            System.out.println("sssssssssssssss "+termsvalue_sp);

                            if(language.equalsIgnoreCase("en")) {
//                                termsvalue_en = termsvalue_en
//                                        .replaceAll("width=\"[^\"]*\"", "")
//                                        .replaceAll("style=\"[^\"]*\"", "");
                              //  termsdata.loadData(termsvalue_en, "text/html; charset=utf-8", "UTF-8");
                                termsdata.loadDataWithBaseURL(
                                        "https://www.youtube.com",
                                        termsvalue_en,
                                        "text/html",
                                        "UTF-8",
                                        null
                                );
                             //  loadHtml(termsvalue_en);

                              //  termsdata.loadData(termsvalue_en, "text/html; charset=utf-8", "UTF-8");

//                                List<ContentItem> list = parseHtml(termsvalue_en);
//
//                                recyclerView.setAdapter(new ContentAdapter(AboutUs.this, list));
                            //    termsdata.loadData(termsvalue_en, "text/html; charset=utf-8", "UTF-8");
                            }else{
//                                termsvalue_sp = termsvalue_sp
//                                        .replaceAll("width=\"[^\"]*\"", "")
//                                        .replaceAll("style=\"[^\"]*\"", "");
                             //   termsdata.loadData(termsvalue_sp, "text/html; charset=utf-8", "UTF-8");
                                termsdata.loadDataWithBaseURL(
                                        "https://www.youtube.com",
                                        termsvalue_sp,
                                        "text/html",
                                        "UTF-8",
                                        null
                                );

                             //   termsdata.loadData(termsvalue_sp, "text/html; charset=utf-8", "UTF-8");
                             //   loadHtml(termsvalue_sp);

//                                List<ContentItem> list = parseHtml(termsvalue_sp);
//
//                                recyclerView.setAdapter(new ContentAdapter(AboutUs.this, list));
                              //  termsdata.loadData(termsvalue_sp, "text/html; charset=utf-8", "UTF-8");
                            }
//                            if(language.equalsIgnoreCase("en")) {
//                                termsdata.loadData(termsvalue_en, "text/html; charset=utf-8", "UTF-8");
//                            }else{
//                                termsdata.loadData(termsvalue_sp, "text/html; charset=utf-8", "UTF-8");
//                            }
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
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), getString(R.string.checkserver), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private List<ContentItem> parseHtml(String html) {

        List<ContentItem> list = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        parseElement(doc.body(), list);

        return list;
    }

    private void parseElement(Element element, List<ContentItem> list) {

        for (Element el : element.children()) {

            String tag = el.tagName();

            // TEXT (HTML preserve)
            if (tag.equals("p") || tag.equals("li") || tag.matches("h[1-6]")) {
                list.add(new ContentItem(ContentItem.TYPE_TEXT, el.html()));
            }

            // IMAGE
            else if (tag.equals("img")) {
                list.add(new ContentItem(ContentItem.TYPE_IMAGE, el.absUrl("src")));
            }

            // IFRAME
            else if (tag.equals("iframe")) {

                String src = el.attr("src");

                // YOUTUBE
                if (src.contains("youtube")) {
                    list.add(new ContentItem(ContentItem.TYPE_VIDEO, src));
                }

                // MAP
                else if (src.contains("google.com/maps")) {

                    double lat = 23.25, lng = 77.41;

                    try {
                        String[] parts = src.split("!");
                        for (String p : parts) {
                            if (p.startsWith("2d")) lng = Double.parseDouble(p.replace("2d", ""));
                            if (p.startsWith("3d")) lat = Double.parseDouble(p.replace("3d", ""));
                        }
                    } catch (Exception ignored) {}

                    list.add(new ContentItem(lat, lng));
                }
            }

            // RECURSION
            parseElement(el, list);
        }
    }

    @Override
    public void onBackPressed() {
        if (termsdata.canGoBack()) {
            termsdata.goBack(); // WebView history me back
        } else {
            super.onBackPressed(); // Activity close
        }
    }


    private void loadHtml(String htmlData) {

        String finalHtml =
                "<html><head>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +

                        // 👇 CSS for better UI
                        "<style>" +
                        "body{font-family:sans-serif;padding:10px;line-height:1.6;}" +
                        "img{max-width:100%;height:auto;}" +
                        "iframe{max-width:100%;}" +
                        "</style>" +

                        "</head><body>" +
                        htmlData +
                        "</body></html>";

        termsdata.loadDataWithBaseURL(
                "https://www.youtube.com",
                finalHtml,
                "text/html",
                "UTF-8",
                null
        );

        applyReadMoreJS();
    }

    private void applyReadMoreJS() {

        termsdata.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                String js =
                        "javascript:(function() {" +
                                "var limit = 300;" +
                                "var elements = document.getElementsByTagName('p');" +

                                "for (var i = 0; i < elements.length; i++) {" +
                                "   var fullText = elements[i].innerText;" +

                                "   if (fullText.length > limit) {" +
                                "       var shortText = fullText.substring(0, limit);" +

                                "       elements[i].setAttribute('data-full', fullText);" +
                                "       elements[i].setAttribute('data-short', shortText);" +
                                "       elements[i].setAttribute('data-state', 'short');" +

                                "       elements[i].innerHTML = shortText + '... ' +" +
                                "       '<span style=\"color:#2196F3;cursor:pointer;font-weight:bold;\" onclick=\"toggleText(this)\">Read More</span>';" +
                                "   }" +
                                "}" +

                                "window.toggleText = function(el) {" +
                                "   var parent = el.parentNode;" +
                                "   var state = parent.getAttribute('data-state');" +

                                "   if (state === 'short') {" +
                                "       parent.innerHTML = parent.getAttribute('data-full') + ' ' +" +
                                "       '<span style=\"color:#E91E63;cursor:pointer;font-weight:bold;\" onclick=\"toggleText(this)\">Read Less</span>';" +
                                "       parent.setAttribute('data-state', 'full');" +
                                "   } else {" +
                                "       parent.innerHTML = parent.getAttribute('data-short') + '... ' +" +
                                "       '<span style=\"color:#2196F3;cursor:pointer;font-weight:bold;\" onclick=\"toggleText(this)\">Read More</span>';" +
                                "       parent.setAttribute('data-state', 'short');" +
                                "   }" +
                                "};" +

                                "})()";

                view.evaluateJavascript(js, null);
            }
        });
    }
}

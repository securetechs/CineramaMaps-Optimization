package main.com.cineramamaps.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityBannerDetailBinding;
import main.com.cineramamaps.databinding.ActivityServiceDetailBinding;
import main.com.cineramamaps.model.ProductAdditional;
import main.com.cineramamaps.model.SingleServiceBean;

public class BannerDetailActivity extends AppCompatActivity {

    ActivityBannerDetailBinding binding;
    SessionManager session;
    String id = "", image = "", title = "", title_ar = "", description = "", description_ar = "";


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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_banner_detail);
        session = SessionManager.get(BannerDetailActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            id = bundle.getString("id");
            image = bundle.getString("image");
            title = bundle.getString("title");
            title_ar = bundle.getString("title_ar");
            description = bundle.getString("description");
            description_ar = bundle.getString("description_ar");
        }

        bindview();
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (language.equalsIgnoreCase("en")) {
            binding.nametv.setText("" + title);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            binding.descriptiontv.setText(Html.fromHtml("" + description, Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            binding.descriptiontv.setText(Html.fromHtml("" + description));
//        }

            String htmlData = "" + description;

// Enable basic settings
            binding.descriptiontv.getSettings().setJavaScriptEnabled(false);
            binding.descriptiontv.getSettings().setDomStorageEnabled(true);
            binding.descriptiontv.setWebViewClient(new WebViewClient());

// Optional styling wrapper (recommended)
            String finalHtml = "<html><body style='padding:8px; font-size:16px; color:#000000;'>"
                    + htmlData +
                    "</body></html>";

            binding.descriptiontv.loadDataWithBaseURL(
                    null,
                    finalHtml,
                    "text/html",
                    "UTF-8",
                    null
            );
        } else {
            binding.nametv.setText("" + title_ar);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                binding.descriptiontv.setText(Html.fromHtml("" + description_ar, Html.FROM_HTML_MODE_COMPACT));
//            } else {
//                binding.descriptiontv.setText(Html.fromHtml("" + description_ar));
//            }

            String htmlData = "" + description_ar;

// Enable basic settings
            binding.descriptiontv.getSettings().setJavaScriptEnabled(false);
            binding.descriptiontv.getSettings().setDomStorageEnabled(true);
            binding.descriptiontv.setWebViewClient(new WebViewClient());

// Optional styling wrapper (recommended)
            String finalHtml = "<html><body style='padding:8px; font-size:16px; color:#000000;'>"
                    + htmlData +
                    "</body></html>";

            binding.descriptiontv.loadDataWithBaseURL(
                    null,
                    finalHtml,
                    "text/html",
                    "UTF-8",
                    null
            );
        }
        if (!image.equalsIgnoreCase("")) {
            Picasso.get().load("" + image).placeholder(R.color.lightgrey).into(binding.bannerimg);

        }

//        binding.reviewexplist.setExpanded(true);
//        binding.reviewexplist.setAdapter(new ReveiwListAdapter(ServiceDetailActivity.this, null));

    }

    private void bindview() {
        binding.extraitemslist.setExpanded(true);


    }


}
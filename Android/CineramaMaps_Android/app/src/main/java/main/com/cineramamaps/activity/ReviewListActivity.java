package main.com.cineramamaps.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import org.json.JSONObject;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.ReviewAdapter;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityReviewBinding;
import main.com.cineramamaps.model.RatingBean;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewListActivity extends AppCompatActivity {
    ActivityReviewBinding binding;
    String provider_id="";

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
        getReview();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null&&!bundle.isEmpty()){
            provider_id = bundle.getString("provider_id");
        }



        clickevent();

     }

    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void getReview() {
        binding.progressbar.setVisibility(View.VISIBLE);

        ApiCall.get().Create().getProviderRating(provider_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("AllFeeds  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            RatingBean successData = new Gson().fromJson(responseData, RatingBean.class);
                            binding.reviewrecycler.setAdapter(new ReviewAdapter(ReviewListActivity.this,successData.getResult()));
                        } else {
                            binding.reviewrecycler.setAdapter(new ReviewAdapter(ReviewListActivity.this,null));

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


}
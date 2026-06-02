package main.com.cineramamaps.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONObject;

import java.util.HashMap;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityGiveRatingBinding;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class GiveRatingActivity extends AppCompatActivity {
    ActivityGiveRatingBinding binding;
    SessionManager session;
    private String order_id = "", provider_id = "",reviewtype="";

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
        session = SessionManager.get(GiveRatingActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_give_rating);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            order_id = bundle.getString("order_id");
            provider_id = bundle.getString("provider_id");
            reviewtype = bundle.getString("reviewtype");
        }

        clickevent();

    }

    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.postitembut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.ratingbar.getRating()==0){
                    Toast.makeText(GiveRatingActivity.this,getResources().getString(R.string.selectrating),Toast.LENGTH_LONG).show();
                }
                else {
                    if(reviewtype.equalsIgnoreCase("Service")){
                        giveRating1();
                    }else {
                        giveRating();
                    }
                }


            }

        });


    }

    private void giveRating() {
        binding.progressbar.setVisibility(View.VISIBLE);

        ApiCall.get().Create().giveRating(getParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("Give Rating >", " >" + response);
                binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(getApplicationContext(), "" + getResources().getString(R.string.submittedsuccessfully),
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            if(reviewtype.equalsIgnoreCase("place")) {
                                if (PlacesDetailsActivity.fav != null) {
                                    PlacesDetailsActivity.fav = "Yes";
                                }
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "" + object.getString("message"),
                                    Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed

                t.printStackTrace();
                binding.progressbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void giveRating1() {
        binding.progressbar.setVisibility(View.VISIBLE);

        ApiCall.get().Create().giveRating1(getParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("Give Rating >", " >" + response);
                binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(getApplicationContext(), "" + getResources().getString(R.string.submittedsuccessfully),
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            if(reviewtype.equalsIgnoreCase("place")) {
                                if (PlacesDetailsActivity.fav != null) {
                                    PlacesDetailsActivity.fav = "Yes";
                                }
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "" + object.getString("message"),
                                    Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed

                t.printStackTrace();
                binding.progressbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }


    private HashMap<String, String> getParam() {

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("request_id", order_id);
        param.put("type", reviewtype);
        if(reviewtype.equalsIgnoreCase("City")) {
            param.put("place_id", "");
            param.put("city_id", provider_id);
        }
     else   if(reviewtype.equalsIgnoreCase("Service")) {
            param.put("place_id", "");
            param.put("service_id", provider_id);
        }
        else{
            param.put("city_id", "");
            param.put("place_id", provider_id);
        }
        param.put("driver_id", "");
        param.put("rating", ""+binding.ratingbar.getRating());
        param.put("review", binding.commentEt.getText().toString());


        return param;


    }


}
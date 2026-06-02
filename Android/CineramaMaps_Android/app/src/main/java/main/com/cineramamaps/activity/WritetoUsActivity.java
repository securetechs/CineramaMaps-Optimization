package main.com.cineramamaps.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONObject;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityGiveRatingBinding;
import main.com.cineramamaps.databinding.ActivityWriteToUsBinding;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WritetoUsActivity extends AppCompatActivity {
    ActivityWriteToUsBinding binding;
    SessionManager session;

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
        session = SessionManager.get(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_to_us);
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

                if (binding.messageEt.getText().toString() == null || binding.messageEt.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(WritetoUsActivity.this, getResources().getString(R.string.writesomething), Toast.LENGTH_LONG).show();
                } else {
                    sendFeedbakc();
                }

            }

        });


    }

    private void sendFeedbakc() {
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().sendFeedback(session.getUserID(),""+session.getUserDetails().getEmail(),""+session.getUserDetails().getFirstName(),""+session.getUserDetails().getMobile(), binding.messageEt.getText().toString()).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.e("ContactUs>", " >" + response);
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {

                            Toast.makeText(WritetoUsActivity.this, getResources().getString(R.string.wewillcontactsoon), Toast.LENGTH_LONG).show();

                            finish();
                        }
                    } catch (Exception e) {


                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(WritetoUsActivity.this, getResources().getString(R.string.serverproblem), Toast.LENGTH_LONG).show();

                t.printStackTrace();
                binding.progressbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }


}
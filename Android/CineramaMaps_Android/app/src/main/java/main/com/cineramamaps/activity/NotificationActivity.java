package main.com.cineramamaps.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.NotificationAdapter;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityNotificationBinding;
import main.com.cineramamaps.model.NotificationBean;
import main.com.cineramamaps.model.NotificationBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private SessionManager session;
    ActivityNotificationBinding binding;
    private ArrayList<NotificationBeanList> notificationBeanListArrayList;

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        BindView();
        clickevent();
         getNotification();

      //  binding.notificaitonlist.setAdapter(new NotificationAdapter(NotificationActivity.this, null, session.IsEnglish()));

    }

    private void clickevent() {

        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void BindView() {
        session = SessionManager.get(NotificationActivity.this);


    }


    private void getNotification() {
        binding.progressbar.setVisibility(View.VISIBLE);
        notificationBeanListArrayList = new ArrayList<>();
        ApiCall.get().Create().getMyNotifications(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("MyItems  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            binding.datalay.setVisibility(View.VISIBLE);
                            binding.emptylay.setVisibility(View.GONE);
                            notificationBeanListArrayList = new ArrayList<>();
                            NotificationBean successData = new Gson().fromJson(responseData, NotificationBean.class);
                            notificationBeanListArrayList.addAll(successData.getResult());


                            binding.notificaitonlist.setAdapter(new NotificationAdapter(NotificationActivity.this, notificationBeanListArrayList, session.IsEnglish(),language));


                        } else {

                            binding.datalay.setVisibility(View.GONE);
                            binding.emptylay.setVisibility(View.VISIBLE);

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }


}
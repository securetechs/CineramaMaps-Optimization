package main.com.cineramamaps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.WalletTransactionAdapter;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityWalletBinding;
import main.com.cineramamaps.model.TopUpBean;
import main.com.cineramamaps.model.TransactionBean;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {
    ActivityWalletBinding binding;
    String top_up_amount = "";
    private SessionManager session;
    ArrayList<TopUpBean> topUpBeanArrayList;

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
        getTransactions();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        session = SessionManager.get(WalletActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {


        }
        setTopUpAmount();

     //   binding.amountrecyclerveiw.setAdapter(new AmountSelectionAdapter(topUpBeanArrayList, WalletActivity.this, true));

        clickevent();

    }

    private void setTopUpAmount() {
        topUpBeanArrayList = new ArrayList<>();
        TopUpBean topUpBean = new TopUpBean();
        topUpBean.setAmount("10");
        topUpBean.setSelected(false);
        topUpBeanArrayList.add(topUpBean);

        TopUpBean topUpBean1 = new TopUpBean();
        topUpBean1.setAmount("20");
        topUpBean1.setSelected(false);
        topUpBeanArrayList.add(topUpBean1);

        TopUpBean topUpBean2 = new TopUpBean();
        topUpBean2.setAmount("30");
        topUpBean2.setSelected(false);
        topUpBeanArrayList.add(topUpBean2);


        TopUpBean topUpBean3 = new TopUpBean();
        topUpBean3.setAmount("40");
        topUpBean3.setSelected(false);
        topUpBeanArrayList.add(topUpBean3);

        TopUpBean topUpBean4 = new TopUpBean();
        topUpBean4.setAmount("50");
        topUpBean4.setSelected(false);
        topUpBeanArrayList.add(topUpBean4);

        TopUpBean topUpBean5 = new TopUpBean();
        topUpBean5.setAmount("100");
        topUpBean5.setSelected(false);
        topUpBeanArrayList.add(topUpBean5);

        TopUpBean topUpBean6 = new TopUpBean();
        topUpBean6.setAmount("150");
        topUpBean6.setSelected(false);
        topUpBeanArrayList.add(topUpBean6);

    }

    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.topupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                top_up_amount = "";
                if (topUpBeanArrayList != null && !topUpBeanArrayList.isEmpty()) {
                    for (int k = 0; k < topUpBeanArrayList.size(); k++) {
                        if (topUpBeanArrayList.get(k).isSelected()) {
                            top_up_amount = topUpBeanArrayList.get(k).getAmount();
                            break;
                        }
                    }
                }
                if (top_up_amount == null || top_up_amount.equalsIgnoreCase("")) {
                    Toast.makeText(WalletActivity.this, getResources().getString(R.string.selectamount), Toast.LENGTH_LONG).show();
                } else {
                    binding.topuplayview.setVisibility(View.GONE);
                    Intent i = new Intent(WalletActivity.this, ChoosePaymentType.class);
                    i.putExtra("order_id", "");
                    i.putExtra("come_from", "wallet");
                    i.putExtra("paid_amount", top_up_amount);
                    startActivity(i);
                }

            }
        });
        binding.topuplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bottomUp = AnimationUtils.loadAnimation(WalletActivity.this,
                        R.anim.bottom_up);

                binding.topuplayview.startAnimation(bottomUp);
                binding.topuplayview.setVisibility(View.VISIBLE);
            }
        });
        binding.closeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bottomDown = AnimationUtils.loadAnimation(WalletActivity.this,
                        R.anim.bottom_down);

                binding.topuplayview.startAnimation(bottomDown);
                binding.topuplayview.setVisibility(View.GONE);
            }
        });
    }



    private void getTransactions() {
        binding.progressbar.setVisibility(View.VISIBLE);

        ApiCall.get().Create().getTransactions(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("MyItems  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        binding.walletamount.setText(getResources().getString(R.string.currency)+object.getString("wallet"));
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            binding.datalay.setVisibility(View.VISIBLE);
                            binding.emptylay.setVisibility(View.GONE);

                            TransactionBean successData = new Gson().fromJson(responseData, TransactionBean.class);
                            binding.transactionrecycler.setAdapter(new WalletTransactionAdapter(WalletActivity.this, successData.getResult()));





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
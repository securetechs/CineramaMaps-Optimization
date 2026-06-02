package main.com.cineramamaps.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.moyasar.android.sdk.PaymentConfig;
import com.stripe.android.model.Card;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.AppConstant;
import main.com.cineramamaps.Utils.Preferences;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.constant.CreditCardFormatTextWatcher;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityConfirmPaymentBinding;
import main.com.cineramamaps.restapi.ApiCall;
import main.com.cineramamaps.tabactivity.MainTabActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConfirmPayment extends AppCompatActivity {
    Context mContext = this;
    String Appliedcode="";
    ProgressDialog progressDialog;
    String strnamecard,cardnumber,strexpiry_date,cvv_number,stryear,after_discount="",offer_id="";
    String currencySendTOserver = "";
    double amountSendTOserver =0;
    double mywalletamount=0.0,afterusewallet=0.0,paidwalletamount=0.0;
    Dialog dialog;
    boolean applystatus= false;
    String usewalletstatus="",paymenttype="Card";
    private CreditCardFormatTextWatcher tv;
    String applycouponcode_str="", type="",duration="",transaction_id="";
    int month, year_int;
    private String token_id="",come_from="",paid_amount = "",order_id="",country_id="",city_id="";

    private SessionManager session;
    ActivityConfirmPaymentBinding binding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_payment);


        session = SessionManager.get(ConfirmPayment.this);
        progressDialog = new ProgressDialog(this);
        Bundle  b = getIntent().getExtras();

        if (b != null) {
            country_id = b.getString("country_id");
            city_id = b.getString("city_id");
            order_id = b.getString("order_id");
            paid_amount = b.getString("paid_amount");
            after_discount = b.getString("paid_amount");
            come_from = b.getString("come_from");
            type = b.getString("type");
            duration = b.getString("duration");
            binding.nametxt.setText(""+type);
            binding.durationtxt.setText(""+duration+" "+getResources().getString(R.string.month));

            double mCityPrice = Double.parseDouble(paid_amount);
            double dynamicPrice = mCityPrice * AppConstant.getExchangeValue(this);

             String finalRoundOffValue = String.format(Locale.US,"%.2f", dynamicPrice);

            amountSendTOserver = Double.parseDouble(finalRoundOffValue);

            binding.cartpricetv.setText(Preferences.get(mContext,Preferences.KEY_CURRENCY)+" "+amountSendTOserver);
            binding.cartpricetv1.setText(Preferences.get(mContext,Preferences.KEY_CURRENCY)+" "+amountSendTOserver);
            if (come_from.equalsIgnoreCase("Wallet")){
                binding.walletlay.setVisibility(View.GONE);
            }

            if(Preferences.get(mContext,Preferences.KEY_CURRENCY).equalsIgnoreCase("SAR")){
                binding.llCardDetail.setVisibility(View.GONE);
            }else{
                binding.llCardDetail.setVisibility(View.GONE);
            }

        }else
        {
          //  Toast.makeText(this,"payment is null",Toast.LENGTH_SHORT).show();
        }


        super.onStart();



        dialog = new Dialog(ConfirmPayment.this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        binding.checkoffertxt.setVisibility(View.GONE);
        binding.checkoffertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(ConfirmPayment.this,OfferActivity.class);
                startActivity(ii);
            }
        });
        tv = new CreditCardFormatTextWatcher(binding.edtCardnumber);
        binding.edtCardnumber.addTextChangedListener(tv);
        binding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applycouponcode_str = binding.promocodeEt.getText().toString().trim();
                if (applycouponcode_str==null || applycouponcode_str.equalsIgnoreCase("")){
                    Toast.makeText(ConfirmPayment.this,getResources().getString(R.string.enteroffercode),Toast.LENGTH_LONG).show();
                }
                else {

                    applyCode();
                    // Toast.makeText(BookServiceAct.this,getResources().getString(R.string.appliedsucc),Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.sendingPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.swithcwallet.isChecked()==true){
                    usewalletstatus= "Yes";
                    paymenttype="Wallet";
                    if(mywalletamount >= Double.parseDouble(paid_amount)){
                       paidwalletamount = Double.parseDouble(paid_amount);
                    }
                  else  if(mywalletamount < Double.parseDouble(paid_amount)){
                      //  paid_amount = ""+(Double.parseDouble(paid_amount)-mywalletamount);
                        paidwalletamount = mywalletamount;
                        usewalletstatus = "";
                        paymenttype="Card";
                    }
                }else{
                    paidwalletamount=0;
                    usewalletstatus = "";
                    paymenttype="Card";
                }
                if(usewalletstatus.equalsIgnoreCase("")) {

                    openPayTab();


//                    if(Preferences.get(mContext,Preferences.KEY_CURRENCY).equalsIgnoreCase("SAR")){
//                        strnamecard = binding.namecard.getText().toString().trim();
//                        cardnumber = binding.edtCardnumber.getText().toString().trim();
//                        strexpiry_date = binding.expiryDate.getText().toString().trim();
//                        stryear = binding.year.getText().toString().trim();
//                        cvv_number = binding.securityCode.getText().toString().trim();
//                        if (strnamecard.equals("")) {
//                            binding.namecard.setError(getResources().getString(R.string.cardnotempty));
//                        }
//                        if (cardnumber.equals("")) {
//                            binding.edtCardnumber.setError(getResources().getString(R.string.cardnumbernotempty));
//                        }
//                        if (strexpiry_date.equals("")) {
//                            binding.expiryDate.setError(getResources().getString(R.string.expdateempty));
//                        }
//                        if (stryear.equals("")) {
//                            binding.year.setError(getResources().getString(R.string.yearnotempty));
//                        }
//                        if (cvv_number.equals("")) {
//                            binding.securityCode.setError(getResources().getString(R.string.securitycodenotemp));
//                        } else {
//                            binding.progressbar.setVisibility(View.VISIBLE);
//                            month = Integer.parseInt(strexpiry_date);
//                            year_int = Integer.parseInt(stryear);
//                            checkPayment();
//                        }
//                    }else{
//                        openPayTab();
//                    }



                }
                else{
                    checkPayment();
                }

            }
        });

        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getTransactions();
    }

    private void paymentwithcard() {
       //
        if (come_from.equalsIgnoreCase("wallet")){
            addTopUp();
        }
        else {
           // addPayment();
        }


    }


    private void successpopup() {
        final Dialog dialogSts = new Dialog(ConfirmPayment.this);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.popup_success_order_placed);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView pickuporder = dialogSts.findViewById(R.id.pickuporder);
        RelativeLayout cancel1 = dialogSts.findViewById(R.id.cancel1);
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(ConfirmPayment.this, PlacesListActivity.class);
                i.putExtra("city_id",city_id);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

                finish();

            }
        });

        pickuporder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(ConfirmPayment.this, PlacesListActivity.class);
                i.putExtra("city_id",city_id);
               i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);


                finish();

            }
        });
        dialogSts.show();
    }

    public void onClickSomething(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {
        Card card = new Card(cardNumber, cardExpMonth, cardExpYear, cardCVC);
        card.validateNumber();
        card.validateCVC();
    }




    private void stripeChargeApi() {
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().stripeChargeApi(session.getUserID(),order_id,paid_amount,"Card",token_id,"CAD","").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                binding.progressbar.setVisibility(View.GONE);
                Log.e("StripeCharge ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("StripeCharge ", " >> " + responseData);

                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            JSONObject jsonObject = object.getJSONObject("result");

                            String transaction_id = jsonObject.getString("id");





                        }
                        else {

                            Toast.makeText(ConfirmPayment.this,""+ object.toString(),Toast.LENGTH_SHORT).show();


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
                Log.e("CountryResponseFail ", " >> " + t.getMessage());
                Log.e("TAG", t.toString());
            }
        });
    }


    private void topupsuccesspopup() {
        final Dialog dialogSts = new Dialog(ConfirmPayment.this);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.popup_success_topup);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView backhome = dialogSts.findViewById(R.id.backhome);


        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
finish();

            }
        });
        dialogSts.show();
    }

    private void addTopUp() {
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().addTopUpinWallet(getParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("TopUp  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        topupsuccesspopup();

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
    private void addPayment(String transaction_id) {
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().addPayment(getOrderSuccessParam(transaction_id)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("OrderPayment  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        successpopup();
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


    private HashMap<String, String> getParam() {
        HashMap paramdata=new HashMap<>();
        paramdata.put("user_id",session.getUserID());
        paramdata.put("payment_method","Card");
        paramdata.put("total_amount",paid_amount);
        paramdata.put("currency","CAD");
        paramdata.put("token",token_id);
        paramdata.put("transaction_type","Top Up");
        return paramdata;
    }

    private void checkPayment() {
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().checkPayment(getParam2()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("OrderPayment  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("0")) {

                            String paymentInitiateStatus = object.getJSONObject("result").getString("status");
                            if(paymentInitiateStatus.equalsIgnoreCase("failed")){
                                JSONObject jsonObject =  object.getJSONObject("result").getJSONObject("source");
                                String mMessage = jsonObject.getString("message");
                                Toast.makeText(ConfirmPayment.this,mMessage,Toast.LENGTH_LONG).show();
                            }else{
                                JSONObject jsonObject =  object.getJSONObject("result").getJSONObject("source");
                                String transactionURL = jsonObject.getString("transaction_url");
                                String transactionId = object.getJSONObject("result").getString("id");
                                Log.e("transactionURL",transactionURL);
                                Intent i = new Intent(ConfirmPayment.this,WebViewActivity.class);
                                i.putExtra("transactionURL",transactionURL);
                                i.putExtra("transactionId",transactionId);
                                startActivityForResult(i,101);
                            }


                            //paymentwithcard();
                        }else{
                            Toast.makeText(ConfirmPayment.this,""+object.getString("message"),Toast.LENGTH_LONG).show();
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


    private HashMap<String, String> getParam2() {
        HashMap paramdata=new HashMap<>();
        paramdata.put("user_id",session.getUserID());
        paramdata.put("payment_method","Card");
        paramdata.put("currency", Preferences.get(mContext,Preferences.KEY_CURRENCY));
        paramdata.put("transaction_type","Top Up");
        paramdata.put("type","creditcard1");
        paramdata.put("name",""+strnamecard);
        paramdata.put("number",""+cardnumber);
        paramdata.put("cvc",""+cvv_number);
        paramdata.put("month",""+strexpiry_date);
        paramdata.put("year",""+stryear);
        paramdata.put("total_amount",amountSendTOserver);
        return paramdata;
    }


    private HashMap<String, String> getOrderSuccessParam(String transaction_id) {
        HashMap paramdata=new HashMap<>();
        paramdata.put("user_id",session.getUserID());
        paramdata.put("plan_id",""+order_id);

        if(applystatus==true) {
            paramdata.put("offer_code", "" + Appliedcode);
            paramdata.put("offer_id",""+offer_id);
        }else{
            paramdata.put("offer_code", "");
            paramdata.put("offer_id","");
        }
        paramdata.put("amount",""+amountSendTOserver);
        paramdata.put("map_country_id",""+country_id);
        paramdata.put("map_city_id",""+city_id);
        paramdata.put("transaction_id",transaction_id);
        paramdata.put("currency", Preferences.get(mContext,Preferences.KEY_CURRENCY));
        return paramdata;
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

                        double mCityPrice = Double.parseDouble(object.getString("wallet"));
                        double dynamicPrice = mCityPrice * AppConstant.getExchangeValue(ConfirmPayment.this);
                        int walletAmt = (int) Math.ceil(dynamicPrice);


                        binding.walletamount.setText(getResources().getString(R.string.youhave)+" "+Preferences.get(mContext,Preferences.KEY_CURRENCY)+walletAmt);
                        if(!object.getString("wallet").equalsIgnoreCase("")){
                           mywalletamount = Double.parseDouble(object.getString("wallet"));
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


    private void openPayTab() {

        double mPriceSAR = Double.parseDouble(paid_amount);
        double dynamicPriceUSD = mPriceSAR * AppConstant.getUSDExchangeValue(ConfirmPayment.this);
        String USDPrice = String.format(Locale.US,"%.2f", dynamicPriceUSD);
        Double usdPriceToSend = Double.parseDouble(USDPrice);


         String finalUrl = BaseUrl.baseurl+"checkout?usd_amount="+usdPriceToSend+"&lang="+myLanguageSession.getLanguage()+"&first_name="+session.getUserDetails().getFirstName()+"&last_name="+session.getUserDetails().getLastName()+"&email="+session.getUserDetails().getEmail()+"&order_id="+order_id+"&amount="+amountSendTOserver+"&currency="+Preferences.get(mContext,Preferences.KEY_CURRENCY);
        //String finalUrl = "https://staginglabs.in/tab-pay?first_name="+session.getUserDetails().getFirstName()+"&last_name="+session.getUserDetails().getLastName()+"&email="+session.getUserDetails().getEmail()+"&order_id="+order_id+"&amount="+amountSendTOserver+"&currency="+Preferences.get(mContext,Preferences.KEY_CURRENCY);

        Log.e("FinalUrl==",finalUrl);
        Intent i = new Intent(ConfirmPayment.this,WebViewActivity.class);
        i.putExtra("transactionURL",finalUrl);
        i.putExtra("transactionId","");
        startActivityForResult(i,101);
    }



    private void applyCode() {
        binding.progressbar.setVisibility(View.VISIBLE);
       //http://techimmense.com/PROJECT/Edamah/webservice/apply_offer?user_id=1&offer_code=QWERTY
        Call<ResponseBody> call = ApiCall.get().Create().applyCoupon(applycouponcode_str,session.getUserID(),paid_amount);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("MYBOOKINGS  ", " >> " + responseData);

                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            offer_id = object.getString("offer_id");
                            Toast.makeText(ConfirmPayment.this,getResources().getString(R.string.appliedsucc),Toast.LENGTH_LONG).show();
                            binding.promocodeEt.setText("");
                            after_discount = object.getString("after_discount");
                            paid_amount = object.getString("total_amount");

                            double mPaidAmt = Double.parseDouble(paid_amount);
                            double dynamicPaidAmt = mPaidAmt * AppConstant.getExchangeValue(ConfirmPayment.this);
                            String finalRoundOffValue = String.format(Locale.US,"%.2f", dynamicPaidAmt);


                            double mAfterDisc = Double.parseDouble(after_discount);
                            double dynamicmAfterDisc = mAfterDisc * AppConstant.getExchangeValue(ConfirmPayment.this);

                            String finalAfterDisc = String.format(Locale.US,"%.2f", dynamicmAfterDisc);

                            amountSendTOserver = Double.parseDouble(finalAfterDisc);


                            binding.cartpricetv.setText(""+Preferences.get(mContext,Preferences.KEY_CURRENCY)+" "+finalRoundOffValue);
                            binding.cartpricetv1.setText(""+Preferences.get(mContext,Preferences.KEY_CURRENCY)+" "+finalAfterDisc);

                            applystatus = true;
                         Appliedcode =   applycouponcode_str;
                        }
                        else {
                            applycouponcode_str ="";
                            offer_id ="";
                            applystatus = false;
                            Toast.makeText(ConfirmPayment.this,object.getString("result"),Toast.LENGTH_LONG).show();
//
                        }
                    } catch (Exception e) {
                        applycouponcode_str ="";
                        offer_id ="";
                        applystatus = false;
                        Log.e("CountryResponse ", " >> 11");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                binding.progressbar.setVisibility(View.GONE);
                applycouponcode_str ="";
                applystatus = false;
                Log.e("CountryResponseFail ", " >> " + t.getMessage());
                Log.e("TAG", t.toString());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (come_from.equalsIgnoreCase("wallet")){
                addTopUp();
            }
            else {
                if(data!= null &&  data.getStringExtra("transactionId") != null){
                    String transactionId = data.getStringExtra("transactionId");
                    addPayment(transactionId);
                }else{
                    Toast.makeText(this, "Payment has been cancelled!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
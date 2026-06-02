package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityChoosePaymentTypeBinding;
import main.com.cineramamaps.tabactivity.MainTabActivity;

public class ChoosePaymentType extends AppCompatActivity {

    Animation bounce;
    public static String payment_type = "CASH";
    ActivityChoosePaymentTypeBinding binding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_payment_type);
        bounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.sample_animation);
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.userlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment_type = "CASH";
                binding.usertv.startAnimation(bounce);
                binding.userlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.providerlay.setBackgroundColor(getResources().getColor(R.color.white));
                binding.usertv.setTextColor(getResources().getColor(R.color.white));
                binding.providertv.setTextColor(getResources().getColor(R.color.black));

            }
        });
        binding.providerlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment_type = "CARD";
                binding.providerlay.startAnimation(bounce);
                binding.providerlay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.userlay.setBackgroundColor(getResources().getColor(R.color.white));
                binding.providertv.setTextColor(getResources().getColor(R.color.white));
                binding.usertv.setTextColor(getResources().getColor(R.color.black));



            }
        });

        binding.continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(payment_type.equalsIgnoreCase("CASH")){
                    successpopup();
                }else {
                    Intent i = new Intent(ChoosePaymentType.this, ConfirmPayment.class);
                    i.putExtra("type", payment_type);
                    i.putExtra("order_id","1");
                    i.putExtra("type","Standard");
                    i.putExtra("duration","1");
                    i.putExtra("come_from","order");
                    i.putExtra("paid_amount","600.00");
                    //   i.putExtra("type","USER");
                    startActivity(i);
                }
            }
        });
    }
    private void successpopup() {
        final Dialog dialogSts = new Dialog(ChoosePaymentType.this);
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
                Intent i = new Intent(ChoosePaymentType.this, MainTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

            }
        });

        pickuporder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();
                Intent i = new Intent(ChoosePaymentType.this, MainTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

            }
        });
        dialogSts.show();
    }
}
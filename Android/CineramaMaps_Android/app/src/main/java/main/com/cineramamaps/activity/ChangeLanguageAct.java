package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityChangeLanguageBinding;
import main.com.cineramamaps.databinding.ActivityChangePasswordBinding;

public class ChangeLanguageAct extends AppCompatActivity {
    ActivityChangeLanguageBinding binding;
    private SessionManager session;
    String lang="en";
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

        binding= DataBindingUtil.setContentView(this, R.layout.activity_change_language);
        session = SessionManager.get(ChangeLanguageAct.this);

        if (language.equalsIgnoreCase("en")) {
            binding.englishrb.setChecked(true);
        }
      else  if (language.equalsIgnoreCase("ar")) {
            binding.arabicrb.setChecked(true);
        }
        else  if (language.equalsIgnoreCase("tr")) {
            binding.turkishrb.setChecked(true);
        }
        else {
            binding.englishrb.setChecked(true);
        }

        clcikevent();
    }

    private void clcikevent() {
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.englishrb.isChecked()){
                    lang = "en";
                }
               else if (binding.arabicrb.isChecked()){
                    lang = "ar";
                }
                else if (binding.turkishrb.isChecked()){
                    lang = "tr";
                }
               else{
                    lang = "en";
                }
                Log.e("lang_selected "," >> "+lang);
                myLanguageSession.insertLanguage(lang);
                finish();
//                session.setLanguage(lang);
//                Tools.get().updateResources(ChangeLanguageAct.this);
//                Intent i = new Intent(ChangeLanguageAct.this, MainTabActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(i);
//                finish();
            }
        });
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
}
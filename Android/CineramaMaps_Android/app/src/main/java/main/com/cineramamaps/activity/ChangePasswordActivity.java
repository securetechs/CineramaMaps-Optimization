package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityChangePasswordBinding;
import main.com.cineramamaps.databinding.ActivityForgotPasswordNewBinding;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ChangePasswordActivity extends AppCompatActivity {
    ActivityChangePasswordBinding binding;
    private SessionManager session;
    private TextView submit;
    private EditText phone_et;
    private String forgot_str = "";
    private ProgressBar prgressbar;
    private RelativeLayout exit_app_but;
    private String user_id="",oldpassstr="",newpassstr="",confirmpassstr="";
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
        binding= DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        session = SessionManager.get(ChangePasswordActivity.this);


        idinit();
        clcikevent();
    }

    private void clcikevent() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpassstr = binding.oldpasswordEt.getText().toString();
                newpassstr = binding.newpassEt.getText().toString();
                confirmpassstr = binding.confirmpassEt.getText().toString();
                if (oldpassstr == null || oldpassstr.equalsIgnoreCase("")) {
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.enteroldpassword), Toast.LENGTH_LONG).show();
                }
               else if (newpassstr == null || newpassstr.equalsIgnoreCase("")) {
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.enternewpassword), Toast.LENGTH_LONG).show();
                }
             else   if (!confirmpassstr.equalsIgnoreCase(""+newpassstr)) {
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.confirmpasswordnotmatched), Toast.LENGTH_LONG).show();
                }
                else {
                    ResetPassword();
                }
            }
        });
        exit_app_but.setOnClickListener(new View.OnClickListener() {
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

    private void idinit() {
        exit_app_but = (RelativeLayout) findViewById(R.id.exit_app_but);
        prgressbar = (ProgressBar) findViewById(R.id.prgressbar);
        submit = (TextView) findViewById(R.id.submit);


     //   Log.e("Country Code"," >"+country_code_str);

    }


    private void ResetPassword(){
        binding.prgressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().changepassword(session.getUserID(),newpassstr,oldpassstr).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.prgressbar.setVisibility(View.GONE);
                Log.e("SignupResponse "," >> "+response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("loginCall >", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.passwordchangedsuccessfully), Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(ChangePasswordActivity.this, ""+getResources().getString(R.string.youroldpasswordnotmatched), Toast.LENGTH_LONG).show();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(ChangePasswordActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.prgressbar.setVisibility(View.GONE);
                Toast.makeText(ChangePasswordActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
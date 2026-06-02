package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.Compress;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivitySettingBinding;
import main.com.cineramamaps.databinding.ActivityUpdateProfileBinding;
import okhttp3.MultipartBody;

public class SettingActivity extends AppCompatActivity {
    private SessionManager session;
    ActivitySettingBinding binding;
    private String ImagePath="";
    private MultipartBody.Part body;
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
        session= SessionManager.get(SettingActivity.this);
        if (session.isUserLogin()){
            binding.useremailtv.setText(session.getUserDetails().getFirstName()+" "+session.getUserDetails().getLastName());
            binding.setUser(session.getUserDetails());
            if (session.getUserDetails().getImage()!=null&&!session.getUserDetails().getImage().equalsIgnoreCase("")){
                Picasso.get().load(session.getUserDetails().getImage()).placeholder(R.drawable.profile_ic).into(binding.userimage);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        BindView();
        clickevent();

    }

    private void clickevent() {

        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.aboutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, AboutUs.class);
                startActivity(i);
            }
        });
        binding.subscriptionlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, SubscriptionActivity.class);
                startActivity(i);
//                Intent ii = new Intent(SettingActivity.this,MapsActivity.class);
//                ii.putExtra("type","");
//                ii.putExtra("duration","");
//                ii.putExtra("order_id", ""+"");
//                ii.putExtra("come_from", "order");
//                ii.putExtra("paid_amount", ""+"");
//                //   i.putExtra("type","USER");
//                startActivity(ii);
            }
        });


        binding.profilelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, UpdateProfileActivity.class);
                startActivity(i);
            }
        });
        binding.changepasswordlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        binding.historylay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, PrivacyPolicyActivity.class);
                startActivity(i);
            }
        });
        binding.termlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, TermsConditionActivity.class);
                startActivity(i);
            }
        });
        binding.changelanglay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, ChangeLanguageAct.class);
                startActivity(i);
            }
        });
        binding.instalay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.instagram.com/cineramamaps/"));
                startActivity(intent);
            }
        });
        binding.tiktoklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.tiktok.com/@cineramamaps"));
                startActivity(intent);
            }
        });
        binding.snaplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.snapchat.com/add/cineramamaps?invite_id=5-f0rPyl&locale=en_SA%40calendar%3Dgregorian&share_id=dklrrBY6Sty1jg1nSK544A&sid=96dba25ac24e427a9251e17b20126cea"));
                startActivity(intent);
            }
        });
        binding.twitterlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://x.com/i/flow/login?redirect_after_login=%2Fcineramamaps"));
                startActivity(intent);
            }
        });

        binding.logouttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                session.Logout();
//                Intent i = new Intent(SettingActivity.this, LoginAct.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(i);
                itemAddedCartSucc();
            }
        });
        binding.shareapplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getResources().getString(R.string.sharetxt));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        binding.helplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent ii = new Intent(SettingActivity.this,WritetoUsActivity.class);
//                startActivity(ii);
                try {
                    String text = "";// Replace with your message.

                    String toNumber = "966582456792"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
                    //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void BindView() {
        session= SessionManager.get(SettingActivity.this);
        if (session.isUserLogin()){
            binding.useremailtv.setText(session.getUserDetails().getFirstName()+" "+session.getUserDetails().getLastName());
            binding.setUser(session.getUserDetails());
            if (session.getUserDetails().getImage()!=null&&!session.getUserDetails().getImage().equalsIgnoreCase("")){
                Picasso.get().load(session.getUserDetails().getImage()).placeholder(R.drawable.profile_ic).into(binding.userimage);
            }
        }




    }
//    boolean Validation() {
//        if (binding.firstName.getText().toString().isEmpty()) {
//            binding.firstName.setError(getString(R.string.required));
//            binding.firstName.requestFocus();
//            return false;
//        }
//        if (binding.lastName.getText().toString().isEmpty()) {
//            binding.lastName.setError(getString(R.string.required));
//            binding.lastName.requestFocus();
//            return false;
//        }
//        if (binding.phoneEt.getText().toString().isEmpty()) {
//            binding.phoneEt.setError(getString(R.string.required));
//            binding.phoneEt.requestFocus();
//            return false;
//        }
//        return true;
//    }
//
//    private HashMap<String,String> getParam() {
//
//        HashMap<String,String>param=new HashMap<>();
//        param.put("user_id",session.getUserID());
//        //param.put("token",session.getUserDetails().getToken());
//        param.put("token","");
//        param.put("first_name",binding.firstName.getText().toString());
//        param.put("last_name",binding.lastName.getText().toString());
//        param.put("mobile",binding.phoneEt.getText().toString());
//
//        param.put("lat","");
//        param.put("lon","");
//
//        return param;
//
//    }
    private void selectImage() {
        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {
                dialog.dismiss();
                if (r.getError() == null) {
                    ImagePath = r.getPath();

                    // binding.userimage.setImageURI(r.getUri());
                    Log.e("DECODE_PATH_PROF", "ImagePath " + ImagePath);
                    Compress.get(SettingActivity.this).setQuality(80).execute(new Compress.onSuccessListener() {
                        @Override
                        public void response(boolean status, String message, File file) {
                            binding.userimage.setImageURI(Uri.fromFile(file));
                            ImagePath=file.getPath();
                            Log.e("URIIN", "ff " + ImagePath);



                        }
                    }).CompressedImage(ImagePath);

                } else {
                }
            }

        }).show(SettingActivity.this);
    }

    private void itemAddedCartSucc() {
        final Dialog dialogSts = new Dialog(SettingActivity.this);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.popup_logout);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView head1 = dialogSts.findViewById(R.id.head1);
        TextView head2 = dialogSts.findViewById(R.id.head2);
        head1.setText(""+getResources().getString(R.string.logout));
        head2.setText(""+getResources().getString(R.string.sureforlogout));
        TextView dismiss = dialogSts.findViewById(R.id.dismiss);
        TextView gotocart = dialogSts.findViewById(R.id.gotocart);


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();


            }
        });
        gotocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.Logout();
                Intent i = new Intent(SettingActivity.this, LoginAct.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                dialogSts.dismiss();
//                Toast.makeText(SettingActivity.this,""+getResources().getString(R.string.deletesuccess),Toast.LENGTH_LONG).show();
//                finish();
            }
        });
        dialogSts.show();
    }


}
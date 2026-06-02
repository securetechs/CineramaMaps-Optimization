package main.com.cineramamaps.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.Compress;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityCreateProfileBinding;
import main.com.cineramamaps.databinding.ActivityUpdateProfileBinding;
import main.com.cineramamaps.model.ProfileResponse;
import main.com.cineramamaps.model.ProfileResponse;
import main.com.cineramamaps.restapi.ApiCall;
import main.com.cineramamaps.tabactivity.MainTabActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateProfileActivity extends AppCompatActivity {
    String country_code_str="";
    private SessionManager session;
   public static ActivityCreateProfileBinding binding;
    private String ImagePath="",mobile_str_with_code="",mobile_no="",gender="",dob="";
    private MultipartBody.Part body;
    CustomGenderAdapter customGenderAdapter;
    private ArrayList<String> genderlist;
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
        EdgeToEdge.enable(this);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_profile);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null &&!bundle.isEmpty()){
            mobile_str_with_code = bundle.getString("mobile_str_with_code");
            mobile_no = bundle.getString("mobile");

        }

        genderlist = new ArrayList<>();
        genderlist.add(""+getResources().getString(R.string.male));
        //  languagelist.add("French");
        genderlist.add(""+getResources().getString(R.string.female));
        BindView();
        country_code_str = binding.ccp.getSelectedCountryCode();

        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country_code_str = binding.ccp.getSelectedCountryCode();
            }
        });
        clickevent();
        binding.emailEt.setText(mobile_no);
binding.dobEt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //Tools.BirhtDatePicker(CreateProfileActivity.this,binding.dobEt::setText);
//        final Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateProfileActivity.this,R.style.DialogTheme,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
//                        String mon = MONTHS[monthOfYear];
//                        int mot = monthOfYear + 1;
//                        String month = "";
//                        if (mot >= 10) {
//                            month = String.valueOf(mot);
//                        } else {
//                            month = "0" + String.valueOf(mot);
//                        }
//                        String daysss = "";
//                        if (dayOfMonth >= 10) {
//                            daysss = String.valueOf(dayOfMonth);
//                        } else {
//                            daysss = "0" + String.valueOf(dayOfMonth);
//                        }
//                        String   date_str = "" + year + "-" + month + "-" + daysss;
//                        Calendar c = Calendar.getInstance();
//                        Date date = c.getTime();
//                        //test2 = dayOfMonth + "/" + month + "/" + year;
//
//                        binding.dobEt.setText(date_str);
//
//
//
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
//        datePickerDialog.show();
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(CreateProfileActivity.this);
        datePickerDialog.show(getSupportFragmentManager(), "custom_date_picker");
    }
});
         customGenderAdapter = new CustomGenderAdapter(CreateProfileActivity.this, genderlist);
        binding.genderSpi.setAdapter(customGenderAdapter);
        if (gender.equalsIgnoreCase("Female")) {
            binding.genderSpi.setSelection(1);
        }
        else {
            binding.genderSpi.setSelection(0);
        }

        binding.genderSpi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (genderlist.get(i).equalsIgnoreCase("Female") ){
                 gender = "Female";
                }else{
                    gender = "Male";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public class CustomGenderAdapter extends BaseAdapter {
        private Context context; //context
        private ArrayList<String> genderlist;

        public CustomGenderAdapter(Context context, ArrayList<String> languagelist) {
            this.context = context;
            this.genderlist = languagelist;
        }

        @Override
        public int getCount() {
            //return 10; //returns total of items in the list
            return genderlist == null ? 0 : genderlist.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.spinner_lay_lang, parent, false);

            }
            TextView pname = convertView.findViewById(R.id.pname);
            pname.setText(""+genderlist.get(position));

            return convertView;
        }
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("dddddddddddddd", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("ddddd", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("", "printHashKey()", e);
        }
    }
    private void clickevent() {

        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.imgselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.updatebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile_str_with_code = country_code_str+binding.phoneEt.getText().toString();
                if (Validation()) {
                    dob = binding.dobEt.getText().toString();
                    if (ImagePath != null && ImagePath.equalsIgnoreCase("")) {
                        body = MultipartBody.Part.createFormData("image", "");
                    } else {
                        File file = new File(ImagePath);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                        Log.e("Imagepath", ImagePath);
                    }
                    Log.e("DATABASIC "," >> "+getParam().toString());




                    binding.progressbar.setVisibility(View.VISIBLE);
                    ApiCall.get().Create().signup(getParam(),body)
                            .enqueue(new Callback<ProfileResponse>() {
                                @Override
                                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                                    binding.progressbar.setVisibility(View.GONE);
                                    Log.e("UpdateProfile_Basic "," .. "+ response);
                                    if (response.isSuccessful()) {
                                        session.CreateSession(response.body().getResult());
                                      //  Toast.makeText(CreateProfileActivity.this, "" + getResources().getString(R.string.registersuccessfully), Toast.LENGTH_SHORT).show();
                                        signupSuccess();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                                    Log.e("onFailure", t.getLocalizedMessage());
                                    binding.progressbar.setVisibility(View.GONE);
                                }
                            });
                }

            }
        });

    }

    private void BindView() {
        session= SessionManager.get(CreateProfileActivity.this);
        if (session.isUserLogin()){
            binding.setUser(session.getUserDetails());
            if (session.getUserDetails().getImage()!=null&&!session.getUserDetails().getImage().equalsIgnoreCase("")){
                Picasso.get().load(session.getUserDetails().getImage()).placeholder(R.drawable.profile_ic).into(binding.userimage);
            }
            String mobilecode =    session.getUserDetails().getMobileWithCode().replace(session.getUserDetails().getMobile(),"");
            System.out.println("sss "+mobilecode);
            System.out.println("sss "+session.getUserDetails().getMobileWithCode()+"      "+session.getUserDetails().getMobile());
            if(mobilecode!=null && !mobilecode.equalsIgnoreCase("")) {
                binding.ccp.setCountryForPhoneCode(Integer.parseInt(mobilecode));

            }
            binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
                @Override
                public void onCountrySelected() {
                    country_code_str = binding.ccp.getSelectedCountryCode();
                }
            });
        }

        binding.phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed here
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.charAt(0) == '0') {
                    // Remove the leading zero
                    s.delete(0, 1);
                }
            }
        });




    }
    boolean Validation() {
        if (binding.firstName.getText().toString().isEmpty()) {
            binding.firstName.setError(getString(R.string.required));
            binding.firstName.requestFocus();
            return false;
        }
        if (binding.lastName.getText().toString().isEmpty()) {
            binding.lastName.setError(getString(R.string.required));
            binding.lastName.requestFocus();
            return false;
        }
        if (binding.phoneEt.getText().toString().isEmpty()) {
            binding.phoneEt.setError(getString(R.string.required));
            binding.phoneEt.requestFocus();
            return false;
        }
//        if (!Tools.isValidEmail(binding.emailEt.getText().toString())){
//            binding.emailEt.setError(getResources().getString(R.string.emailvalid));
//            binding.emailEt.requestFocus();
//            return false;
//        }
        if (binding.dobEt.getText().toString().isEmpty()) {
            binding.dobEt.setError(getString(R.string.required));
            binding.dobEt.requestFocus();
            Toast.makeText(CreateProfileActivity.this,""+getResources().getString(R.string.pleaseselectdateofbirth),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private HashMap<String,String> getParam() {

        HashMap<String,String>param=new HashMap<>();
        param.put("first_name",binding.firstName.getText().toString());
        param.put("last_name",binding.lastName.getText().toString());
        param.put("mobile",binding.phoneEt.getText().toString());
        param.put("mobile_with_code",mobile_str_with_code);
        param.put("gender",gender);
        param.put("dob",dob);
        param.put("email",binding.emailEt.getText().toString());
        param.put("register_id",""+LoginAct.FireBaseToken);
        param.put("ios_register_id","");
        param.put("address","");
        param.put("lat","");
        param.put("lon","");

        return param;

    }
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
                    Compress.get(CreateProfileActivity.this).setQuality(80).execute(new Compress.onSuccessListener() {
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

        }).show(CreateProfileActivity.this);
    }
    private void signupSuccess() {
        final Dialog dialogSts = new Dialog(CreateProfileActivity.this);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.popup_success_signup);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView backhome = dialogSts.findViewById(R.id.backhome);


        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();

                Intent i = new Intent(CreateProfileActivity.this, MainTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });
        dialogSts.show();
    }
}
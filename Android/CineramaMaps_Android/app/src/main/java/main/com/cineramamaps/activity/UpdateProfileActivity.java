package main.com.cineramamaps.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.Compress;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityUpdateProfileBinding;
import main.com.cineramamaps.model.ProfileResponse;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity  {
    private SessionManager session;
    String country_code_str="";
    String mobile_str_with_code="";
  public static   ActivityUpdateProfileBinding binding;
    private String ImagePath="",gender="",dob="";
    CustomGenderAdapter customGenderAdapter;
    private ArrayList<String> genderlist;
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding= DataBindingUtil.setContentView(this, R.layout.activity_update_profile);

        BindView();
        country_code_str = binding.ccp.getSelectedCountryCode();

        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country_code_str = binding.ccp.getSelectedCountryCode();
            }
        });
        clickevent();
        genderlist = new ArrayList<>();
        genderlist.add(""+getResources().getString(R.string.male));
        //  languagelist.add("French");
        genderlist.add(""+getResources().getString(R.string.female));

binding.dobEt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //  Tools.BirhtDatePicker(UpdateProfileActivity.this,binding.dobEt::setText);
//            final Calendar c = Calendar.getInstance();
//
//            int mYear = c.get(Calendar.YEAR);
//            int mMonth = c.get(Calendar.MONTH);
//            int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateProfileActivity.this,R.style.DialogTheme,
//                    new DatePickerDialog.OnDateSetListener() {
//
//                        @SuppressLint("SetTextI18n")
//                        @Override
//                        public void onDateSet(DatePicker view, int year,
//                                              int monthOfYear, int dayOfMonth) {
//                           // view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                            final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
//                            String mon = MONTHS[monthOfYear];
//                            int mot = monthOfYear + 1;
//                            String month = "";
//                            if (mot >= 10) {
//                                month = String.valueOf(mot);
//                            } else {
//                                month = "0" + String.valueOf(mot);
//                            }
//                            String daysss = "";
//                            if (dayOfMonth >= 10) {
//                                daysss = String.valueOf(dayOfMonth);
//                            } else {
//                                daysss = "0" + String.valueOf(dayOfMonth);
//                            }
//                         String   date_str = "" + year + "-" + month + "-" + daysss;
//                            Calendar c = Calendar.getInstance();
//                            Date date = c.getTime();
//                            //test2 = dayOfMonth + "/" + month + "/" + year;
//
//                                binding.dobEt.setText(date_str);
//
//
//
//                        }
//                    }, mYear, mMonth, mDay);
//
//            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
//            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                try {
//                    DatePicker datePicker = datePickerDialog.getDatePicker();
//                    Method method = datePicker.getClass().getMethod("setSpinnersShown", boolean.class);
//                    method.invoke(datePicker, true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//           // datePickerDialog.getDatePicker().setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            datePickerDialog.show();

            CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(UpdateProfileActivity.this);
            datePickerDialog.show(getSupportFragmentManager(), "custom_date_picker");
        }
    });
           customGenderAdapter = new CustomGenderAdapter(UpdateProfileActivity.this, genderlist);
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
                    ApiCall.get().Create().updateProfile(getParam(),body)
                            .enqueue(new Callback<ProfileResponse>() {
                                @Override
                                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                                    binding.progressbar.setVisibility(View.GONE);
                                    Log.e("UpdateProfile_Basic "," .. "+ response);
                                    if (response.isSuccessful()) {
                                        session.CreateSession(response.body().getResult());
                                        Toast.makeText(UpdateProfileActivity.this, "" + getResources().getString(R.string.updatedsucessfully), Toast.LENGTH_SHORT).show();
                                  finish();
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
        session= SessionManager.get(UpdateProfileActivity.this);
        if (session.isUserLogin()){
            binding.dobEt.setText(session.getUserDetails().getDob());
            binding.usernametv.setText(session.getUserDetails().getFirstName()+" "+session.getUserDetails().getLastName());
            gender = session.getUserDetails().getGender();
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
        if (binding.emailEt.getText().toString().isEmpty()) {
            binding.emailEt.setError(getString(R.string.required));
            binding.emailEt.requestFocus();
            return false;
        }
        if (!Tools.isValidEmail(binding.emailEt.getText().toString())){
            binding.emailEt.setError(getResources().getString(R.string.emailvalid));
            binding.emailEt.requestFocus();
            return false;
        }
        if (binding.dobEt.getText().toString().isEmpty()) {
            binding.dobEt.setError(getString(R.string.required));
            binding.dobEt.requestFocus();
            Toast.makeText(UpdateProfileActivity.this,""+getResources().getString(R.string.pleaseselectdateofbirth),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private HashMap<String,String> getParam() {

        HashMap<String,String>param=new HashMap<>();
        param.put("user_id",session.getUserID());
        //param.put("token",session.getUserDetails().getToken());
        param.put("token","");
        param.put("mobile",binding.phoneEt.getText().toString());
        param.put("mobile_with_code",mobile_str_with_code);
        param.put("first_name",binding.firstName.getText().toString());
        param.put("last_name",binding.lastName.getText().toString());
        param.put("email",binding.emailEt.getText().toString());
        param.put("gender",gender);
        param.put("dob",dob);
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
                    Compress.get(UpdateProfileActivity.this).setQuality(80).execute(new Compress.onSuccessListener() {
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

        }).show(UpdateProfileActivity.this);
    }

}
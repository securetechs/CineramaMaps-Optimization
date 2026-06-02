package main.com.cineramamaps.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.constant.SetLocation;
import main.com.cineramamaps.databinding.ActivitySignupBinding;
import main.com.cineramamaps.model.ProfileResponse;
import main.com.cineramamaps.restapi.ApiCall;
import main.com.cineramamaps.tabactivity.MainTabActivity;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SignupActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private FirebaseAuth mAuth;
    private String mVerificationId="";
    boolean for_eyes = true,for_eyes1=true;
    private String lat="",lon="",country_code_str="",otp_str="",mobile_with_code="",mobile_str="",number="",ImagePath="";
    private String FireBaseToken="";
    EditText otp_edt;
    private Dialog confirmdialog;
    CountDownTimer yourCountDownTimer;
    private MultipartBody.Part body;

    HashMap<String,String> paramdata;
    private String [] mPermission= {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String [] mPermissionlatest= {

            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    SessionManager session;
 public static    ActivitySignupBinding binding;


    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in Milliseconds
    Location location;
    Location location_ar;
    LocationManager locationManager;
    private GoogleApiClient googleApiClient;
    public static double longitude = 0.0, latitude = 0.0;
    GPSTracker tracker;
    private String type="User",mobile_str_with_code="";
    private String userlat,userlon;

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
        if (SetLocation.pickuplocation_str!=null&&!SetLocation.pickuplocation_str.equalsIgnoreCase("")){
            binding.areaNameTv.setText(""+SetLocation.pickuplocation_str);
            userlat = ""+SetLocation.originlatlong.latitude;
            userlon = ""+SetLocation.originlatlong.longitude;
            SetLocation.pickuplocation_str ="";
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        binding= DataBindingUtil.setContentView(this, R.layout.activity_signup);
        session= SessionManager.get(this);
        mAuth = FirebaseAuth.getInstance();
        String text = getResources().getString(R.string.iread)+" <font color=#00adb5> "+getResources().getString(R.string.termcon)+"</font> "+getResources().getString(R.string.iacceptthem);
       // String text = getResources().getString(R.string.ireadtermscond);
        binding.providerPrivacyText.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        tracker = new GPSTracker(this);
        getLatLong();
        getCurrentLocation();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null&&!bundle.isEmpty()){
            type = bundle.getString("type");
        }



        clickevent();
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                FireBaseToken=s;
            }
        });
        binding.eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(for_eyes){
                    Picasso.get().load(R.drawable.view).into(binding.eyes);
                    binding.passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//                eyes.ima(R.drawable.view);
                    for_eyes = false;
                }else{
//                    eyes.setBackgroundResource(R.drawable.view_eye);
                    Picasso.get().load(R.drawable.view_eye).into(binding.eyes);
                    binding.passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    for_eyes = true;
                }

            }
        });

        binding.eyes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(for_eyes1){
                    Picasso.get().load(R.drawable.view).into(binding.eyes1);
                    binding.conPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//                eyes.ima(R.drawable.view);
                    for_eyes1 = false;
                }else{
//                    eyes.setBackgroundResource(R.drawable.view_eye);
                    Picasso.get().load(R.drawable.view_eye).into(binding.eyes1);
                    binding.conPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    for_eyes1 = true;
                }

            }
        });
    }

    private void clickevent() {
        country_code_str = binding.ccp.getSelectedCountryCode();
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country_code_str = binding.ccp.getSelectedCountryCode();
            }
        });
        binding.providerPrivacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this,TermsConditionActivity.class);
                i.putExtra("comefrom","Signup");
                startActivity(i);
            }
        });


        binding.imageLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

              /*  Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), 1);*/
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(SignupActivity.this,mPermission , 101);
                } else {
                    ActivityCompat.requestPermissions(SignupActivity.this,mPermissionlatest , 101);
                }
            }
        });

        binding.backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.arealay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, SetLocation.class);
                i.putExtra("comefrom","Signup");
                startActivity(i);
            }
        });
        binding.alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginAct.class);

                startActivity(i);
                finish();
            }
        });
        binding.signupbut.setOnClickListener(v->{
            if (Validation()){
                mobile_str_with_code = country_code_str+binding.phoneEt.getText().toString();
             mobile_str =    binding.phoneEt.getText().toString();
               // new SendOtp().execute();

                if (binding.privacyCheckbox.isChecked()){
//                    getParam();
//                   // verifyMobile();
//                    sendVerificationCode("+"+mobile_str_with_code);
                    signupSuccess();
                }
                else {
                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.tocontinue),Toast.LENGTH_LONG).show();
                }

            }




        });
    }
    private boolean Validation(){
        if (binding.firstName.getText().toString().isEmpty()){
            binding.firstName.setError(getResources().getString(R.string.required));
            binding.firstName.requestFocus();
            return false;
        }if (binding.lastName.getText().toString().isEmpty()){
            binding.lastName.setError(getResources().getString(R.string.required));
            binding.lastName.requestFocus();
            return false;
        }
        if (binding.phoneEt.getText().toString().isEmpty()){
            binding.phoneEt.setError(getResources().getString(R.string.required));
            binding.phoneEt.requestFocus();
            return false;
        }
        if (binding.emailEt.getText().toString().isEmpty()){
            binding.emailEt.setError(getResources().getString(R.string.required));
            binding.emailEt.requestFocus();
            return false;
        }if (binding.areaNameTv.getText().toString().isEmpty()){
            binding.areaNameTv.setError(getResources().getString(R.string.required));
            binding.areaNameTv.requestFocus();
            return false;
        }
        if (!Tools.isValidEmail(binding.emailEt.getText().toString())){
            binding.emailEt.setError(getResources().getString(R.string.emailvalid));
            binding.emailEt.requestFocus();
            return false;
        }
        if (binding.passwordEt.getText().toString().isEmpty()){
            binding.passwordEt.setError(getResources().getString(R.string.required));
            binding.passwordEt.requestFocus();
            return false;
        }

       /* if (binding.aboutEt.getText().toString().isEmpty()) {
            binding.aboutEt.setError(getString(R.string.required));
            binding.aboutEt.requestFocus();
            return false;
        }*/
        if (!binding.passwordEt.getText().toString().equals(binding.conPasswordEt.getText().toString())){
            binding.passwordEt.setError(getResources().getString(R.string.passwordnoatmatched));
            binding.passwordEt.requestFocus();
            return false;
        }
        return true;
    }
    private HashMap<String, String> getParam() {

        String lat = ""+SplashActivity.latitude;
        String lon = ""+SplashActivity.longitude;

        if (SplashActivity.latitude==0){
            lat = ""+  latitude;
            lon = ""+  longitude;
        }

        number= binding.phoneEt.getText().toString();
        paramdata=new HashMap<>();
        paramdata.put("mobile",number);
        paramdata.put("mobile_with_code",mobile_str_with_code);
        paramdata.put("first_name",binding.firstName.getText().toString());
        paramdata.put("last_name",binding.lastName.getText().toString());
        paramdata.put("email",binding.emailEt.getText().toString());
        paramdata.put("password",binding.passwordEt.getText().toString());
        paramdata.put("register_id",""+FireBaseToken);
        paramdata.put("ios_register_id","");
        paramdata.put("type","USER");

        paramdata.put("address",binding.areaNameTv.getText().toString());
        paramdata.put("lat","");
        paramdata.put("lon","");




        return paramdata;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        boolean isAccepted=false;
        for (int  i:grantResults) {
            isAccepted=i==0;
            if (!isAccepted){
                return;
            }
        }
        if (isAccepted){
            selectImage();

          /*  Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), 1);*/
            //CameraHandler.start(SelectSubCategoryAct.this, cameraOptions);
        }else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(SignupActivity.this,mPermission , 101);
            } else {
                ActivityCompat.requestPermissions(SignupActivity.this,mPermissionlatest , 101);
            }        }
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

                if (r.getError() == null) {
                    ImagePath = r.getPath();

                    Log.e("DECODE PATH PROF", "ff " + ImagePath);
                  /*  Compress.get(UpdateChildrenAct.this).setQuality(80).execute(new Compress.onSuccessListener() {
                        @Override
                        public void response(boolean status, String message, File file) {
                            user_img.setImageURI(Uri.fromFile(file));
                            ImagePath=file.getPath();
                        }
                    }).CompressedImage(ImagePath);*/
                    binding.userimg.setImageBitmap(r.getBitmap());


                } else {
                }
            }

        }).show(SignupActivity.this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void verifyMobile(){



        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().verifyOtp(verifyOtpParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("VerifyOtp "," >> "+response);

                if (response.isSuccessful()){

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("VerifyOtpresponseData "," >> "+responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            JSONObject jsonObject1 = object.getJSONObject("result");
                            otp_str = jsonObject1.getString("code");

                            Intent i = new Intent(SignupActivity.this, EnterVerificationAct.class);
                            Log.e("param string signup>> "," >> "+paramdata);
                            i.putExtra("param",paramdata);
                            i.putExtra("mobile_str_with_code",mobile_str_with_code);
                            i.putExtra("mobile",binding.phoneEt.getText().toString());
                            i.putExtra("otp_str",otp_str);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(SignupActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(SignupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SignupActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            switch (requestCode) {
                case 1:
                    Uri selectedImage = data.getData();
                    getPath(selectedImage);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String FinalPath = cursor.getString(columnIndex);
                    cursor.close();
                    String ImagePath = getPath(selectedImage);
                    Log.e("PATH From Gallery", "" + FinalPath);
                    Log.e("PATH Get Gallery", "" + getPath(selectedImage));
                    decodeFile(ImagePath);
                    break;
                case 2:

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // File file = new File(photo);
                    //  save(file.getAbsolutePath());
                    ImagePath = saveToInternalStorage(photo);
                    Log.e("PATH Camera", "" + ImagePath);

                    //  avt_imag.setImageBitmap(photo);
                    break;

                case 3:
                    Uri selectedImage3 = data.getData();
                    getPath(selectedImage3);
                    String[] filePathColumn3 = {MediaStore.Images.Media.DATA};
                    Cursor cursor3 = getContentResolver().query(selectedImage3, filePathColumn3, null, null, null);
                    cursor3.moveToFirst();
                    int columnIndex3 = cursor3.getColumnIndex(filePathColumn3[0]);
                    String FinalPath3 = cursor3.getString(columnIndex3);
                    cursor3.close();
                    String ImagePath3 = getPath(selectedImage3);
                    Log.e("PATH From Gallery", "" + FinalPath3);
                    Log.e("PATH Get Gallery", "" + getPath(selectedImage3));
                    decodeFiles(ImagePath3);
                    break;
            }
        }
    }


    public String getPath(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //  Log.e("image_path.===..", "" + path);
        }
        cursor.close();
        return path;
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        ContextWrapper cw = new ContextWrapper(SignupActivity.this);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile_" + dateToStr + ".JPEG");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }


    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        ImagePath = saveToInternalStorage(bitmap);
        Log.e("DECODE PATH PROF", "ff " + ImagePath);
        binding.userimg.setImageBitmap(bitmap);



    }

    public void decodeFiles(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        ImagePath = saveToInternalStorage(bitmap);
        Log.e("DECODE PATH IDT", "ff " + ImagePath);
        binding.userimg.setImageBitmap(bitmap);

    }
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (location_ar != null) {
            //Getting longitude and latitude
            longitude = location_ar.getLongitude();
            latitude = location_ar.getLatitude();
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + latitude);



        } else {
            System.out.println("----------------geting Location from GPS----------------");

            location_ar = tracker.getLocation();
            if (location_ar == null) {


            } else {
                longitude = location_ar.getLongitude();
                latitude = location_ar.getLatitude();
                Log.e("Lat >>", "GPS " + latitude);

            }
            System.out.println("-------------getCurrentLocation--------------" + latitude + " , " + longitude);
            //moving the map to location

        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    private void getLatLong() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location_ar = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }


    private HashMap<String, String> verifyOtpParam() {




        HashMap  paramdata=new HashMap<>();


        paramdata.put("mobile",binding.phoneEt.getText().toString());
        paramdata.put("mobile_with_code",mobile_str_with_code);
        paramdata.put("email",binding.emailEt.getText().toString());

        return paramdata;
    }

    String otpcode = "";
    private void enerOtpLayFirebase() {


        //   Log.e("War Msg in dialog", war_msg);
        confirmdialog = new Dialog(SignupActivity.this);
        confirmdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmdialog.setCancelable(false);
        confirmdialog.setContentView(R.layout.otplay);
        confirmdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        otp_edt = (EditText) confirmdialog.findViewById(R.id.otp_edt);
        TextView resendotp = (TextView) confirmdialog.findViewById(R.id.resendotp);
        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmdialog.dismiss();
                sendVerificationCode("+"+country_code_str+mobile_str);
            }
        });
        TextView confirm = (TextView) confirmdialog.findViewById(R.id.confirm);
        TextView cancel = (TextView) confirmdialog.findViewById(R.id.cancel);
        ImageView cross = (ImageView) confirmdialog.findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmdialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpcode = otp_edt.getText().toString();
                if (otpcode!=null&&!otpcode.equalsIgnoreCase("")){
                    if(otpcode.equalsIgnoreCase("12345") || otpcode.equalsIgnoreCase("12345")){
                     //   DoSignUp();
                    }else {
                        if(mVerificationId!=null && !mVerificationId.equalsIgnoreCase("")) {
                            verifyVerificationCode(otpcode);
                        }else{
                            Toast.makeText(SignupActivity.this,getResources().getString(R.string.pleaseentervalidcode),Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    Toast.makeText(SignupActivity.this,getResources().getString(R.string.plsenterotp),Toast.LENGTH_LONG).show();
                }


            }
        });

        confirmdialog.show();


    }




    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                SignupActivity.this,
                mCallbacks);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            otpcode = phoneAuthCredential.getSmsCode();
            Log.e("Come ","Success "+otpcode);
            System.out.println("ssssssssssss "+otpcode);


            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (otpcode != null) {
                otp_edt.setText(otpcode);
                //  editTextCode.setText(code);
                //verifying the code
                //verifyVerificationCode(otpcode);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("FailedVer ", " .. " +e.getMessage());
            enerOtpLayFirebase();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.e("Come ","Code Sent ");
            //storing the verification id that is sent to the user
            mVerificationId = s;
            enerOtpLayFirebase();
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        if(mVerificationId!=null && !mVerificationId.equalsIgnoreCase("")) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

            //signing the user
            signInWithPhoneAuthCredential(credential);
        }else{

        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            if (confirmdialog!=null){
                                confirmdialog.dismiss();
                            }

//                            int rbid = lang_group.getCheckedRadioButtonId();
//                            if (rbid==R.id.english_rb){
//                                language = "en";
//                            }
//                            else if (rbid==R.id.kiswahi_rb){
//                                language = "sw";
//                            }
//                            else {
//                                language = "en";
//                            }
//                            myLanguageSession.insertLanguage(language);
                            //  callSignupapi(first_name_str,last_name_str,phone_et_str,email_str, password_str,firebase_regid,""+latitude,""+longitude,"USER",country_str,state_str,city_str);
                         //   DoSignUp();
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toast.makeText(SignupActivity.this,message,Toast.LENGTH_LONG).show();


                        }
                    }
                });
    }

//    private void DoSignUp(){
//
//
//
//        binding.progressbar.setVisibility(View.VISIBLE);
//        ApiCall.get().Create().signup(paramdata).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                binding.progressbar.setVisibility(View.GONE);
//                Log.e("SignupResponse "," >> "+response);
//
//                if (response.isSuccessful()){
//
//                    try {
//                        String responseData = response.body().string();
//                        JSONObject object = new JSONObject(responseData);
//                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            ProfileResponse successData = new Gson().fromJson(responseData, ProfileResponse.class);
//
//                            session.CreateSession(successData.getResult());
//
//                            signupSuccess();
//                        }
//                        else {
//                            Toast.makeText(SignupActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//                    }
//
//                }else {
//                    Toast.makeText(SignupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                binding.progressbar.setVisibility(View.GONE);
//                Toast.makeText(SignupActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private void signupSuccess() {
        final Dialog dialogSts = new Dialog(SignupActivity.this);
        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSts.setCancelable(false);
        dialogSts.setContentView(R.layout.popup_success_signup);
        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView backhome = dialogSts.findViewById(R.id.backhome);


        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSts.dismiss();

                Intent i = new Intent(SignupActivity.this, MainTabActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });
        dialogSts.show();
    }


}

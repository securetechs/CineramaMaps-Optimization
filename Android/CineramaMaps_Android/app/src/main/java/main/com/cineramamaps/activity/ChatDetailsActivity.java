package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledExecutorService;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.NotificationUtils;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.app.Config;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.constant.MultipartUtility;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityChatActivtyBinding;
import main.com.cineramamaps.databinding.ActivityChatDetailsBinding;
import main.com.cineramamaps.model.ChatBean;
import main.com.cineramamaps.model.ChatListBean;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatDetailsActivity extends AppCompatActivity {
    final Handler handler = new Handler();
    private ListView chatlist;
    final int delay = 15000;
    private TextView send_tv, titletext;
    private ArrayList<ChatListBean> chatListBeanArrayList;
    String messagetext = "", ImagePath = "",type="",address="",lat="",lon="";
    String callstatus="0";
    EditText message_et;
    TextView propertytitletxt;
    ScheduledExecutorService scheduleTaskExecutor;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ConversessionAdapter conversessionAdapter;
    public String image_url = "",product_id="",product_name="", block_status = "", firstname_str = "", lastname_str = "", date_time = "", user_log_data = "", time_zone = "", request_id = "", user_id = "", receiver_id = "", receiver_img = "", receiver_name = "";
    int beforelength = 0;
    ImageView camera_img;
    private boolean prosts = false;
    ProgressBar prgressbar;
    public static boolean isInFront = false;
    private RelativeLayout bottumlay, optionmenu;
    private CircleImageView chatuser_img;
    private Toolbar toolbar;
    TextView addresstxt;
    boolean resume = false;



    ActivityChatDetailsBinding binding;
    String id;
    private SessionManager session;
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
        isInFront = true;
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());

//        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
//        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
//            public void run() {
//                new MyConverSession().execute();
//
//            }
//        }, 0, 7, TimeUnit.SECONDS);
        // 1000 milliseconds == 1 second
        //  if(!callstatus.equalsIgnoreCase("0")) {
        handler.postDelayed(new Runnable() {
            public void run() {
                if (isInFront){
                    System.out.println("myHandler: here!"); // Do your work here
                    new MyConverSession().execute();
                    handler.postDelayed(this, delay);
                }


            }
        }, delay);
//        }else{
//            callstatus="";
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_details);
        session = SessionManager.get(ChatDetailsActivity.this);
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        time_zone = tz.getID();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            receiver_id = bundle.getString("receiver_id");
            receiver_name = bundle.getString("receiver_name");
            receiver_img = bundle.getString("receiver_img");
            product_id = bundle.getString("product_id");
            request_id = bundle.getString("product_id");
            product_name = bundle.getString("product_name");
            //  propertytitletxt.setText(getResources().getString(R.string.jobtitle)+": "+product_name);
//            propertytitletxt.setText(""+product_name);
            //  block_status = bundle.getString("block_status");
            type = bundle.getString("type");
            Log.e("block_status chat>>"," >"+block_status);

        }
        idinit();
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar == null) {
//            throw new Error("Can't find tool bar, did you forget to add it in Activity layout file?");
//        }

    //    setSupportActionBar(toolbar);
        clickevent();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Log.e("Push Chat: ", "" + message);
                    JSONObject data = null;
                    try {
                        data = new JSONObject(message);
                        String keyMessage = data.getString("key").trim();
                        if (keyMessage.equalsIgnoreCase("You have a new message")) {
                            Log.e("Push Chat Come: ", "True");

                            new MyConverSession().execute();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

//        if(callstatus.equalsIgnoreCase("0")) {
//            new MyConverSession().execute();
//        }
        conversessionAdapter = new ConversessionAdapter(ChatDetailsActivity.this, chatListBeanArrayList,"");
        chatlist.setAdapter(conversessionAdapter);
      //  chatlist.setSelection(chatListBeanArrayList.size() - 1);
        conversessionAdapter.notifyDataSetChanged();
    }

    private void getProfile() {

        ApiCall.get().Create().getProfile(receiver_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //  binding.progressbar.setVisibility(View.GONE);
                Log.e("MyProfileDetail >", " >" + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyProfileDetail >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            JSONObject object1 = object.getJSONObject("result");
                            // binding.nametv.setText(""+object1.getString("first_name")+" "+object1.getString("last_name"));
                            address=     object1.getString("address");

                            lat = ""+object1.getString("lat");
                            lon = ""+object1.getString("lon");
//                            if(lat !=null && !lat.equalsIgnoreCase("")) {
//                                latitude = Double.parseDouble(lat);
//                                longitude = Double.parseDouble(lon);
//
//
//                            }
//                            addresstxt.setText(address);
//                            MarkerOptions markers = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.employee)).flat(true).anchor(0.5f, 0.5f);
//                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
//                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
//                            gMap.animateCamera(zoom);
//                            gMap.addMarker(markers);
//                            gMap.moveCamera(center);
//                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 8);
//                            gMap.animateCamera(zoom);
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // binding.progressbar.setVisibility(View.GONE);
            }
        });
    }




//    private void checkGps() {
//        gpsTracker = new GPSTracker(this);
//        if (gpsTracker.canGetLocation()) {
//
//            latitude = gpsTracker.getLatitude();
//            longitude = gpsTracker.getLongitude();
//
//
//        } else {
//            // if gps off get lat long from network
//            //   locationfromnetwork();
//            gpsTracker.showSettingsAlert();
//        }
////        latitude = 22.7196;
////        longitude = 75.8577;
//
//    }
//    private void initilizeMap() {
//        mapFragment.getMapAsync(this);
//
//    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gMap = googleMap;
//        if (ActivityCompat.checkSelfPermission(ChatDetailsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ChatDetailsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        gMap.setBuildingsEnabled(false);
//        gMap.setMyLocationEnabled(true);
//        gMap.getUiSettings().setMyLocationButtonEnabled(false);
//        gMap.getUiSettings().setMapToolbarEnabled(false);
//        gMap.getUiSettings().setZoomControlsEnabled(false);
//        gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        MarkerOptions markers = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.employee)).flat(true).anchor(0.5f, 0.5f);
//        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(25);
//        gMap.animateCamera(zoom);
//        gMap.addMarker(markers);
//        gMap.moveCamera(center);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 8);
//        gMap.animateCamera(zoom);
//
//
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  scheduleTaskExecutor.shutdown();


        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        // scheduleTaskExecutor.shutdown();

        if (handler!=null){

        }
        isInFront = false;
    }



    private void clickevent() {

        send_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messagetext = message_et.getText().toString().trim();
                if (messagetext == null || messagetext.equalsIgnoreCase("")) {
                    Toast.makeText(ChatDetailsActivity.this,getResources().getString(R.string.typemessage), Toast.LENGTH_LONG).show();

                } else {
                    Date today = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    date_time = format.format(today);
                    System.out.println("CURRENT " + date_time);

                    if (prosts==false){
                       // new SendMessage().execute();
                        messagetext = "";
                        message_et.setText("");
                        ImagePath = "";
                    }


                }

            }
        });
        camera_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagetext = message_et.getText().toString();
//                if (messagetext == null || messagetext.equalsIgnoreCase("")) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);//
//                    startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
//                }

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
                            Date today = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                            date_time = format.format(today);
                            System.out.println("CURRENT " + date_time);
                            prosts = false;
                           // new SendMessage().execute();


//                            binding.userimage.setImageURI(r.getUri());
//                            Log.e("DECODE_PATH_PROF", "ImagePath " + ImagePath);


                        } else {
                        }
                    }

                }).show(ChatDetailsActivity.this);

            }
        });

    }




    private void idinit() {

        bottumlay = (RelativeLayout) findViewById(R.id.bottumlay);

        chatuser_img = (CircleImageView) findViewById(R.id.chatuser_img);
        prgressbar = (ProgressBar) findViewById(R.id.prgressbar);
        camera_img = (ImageView) findViewById(R.id.camera_img);
        message_et = (EditText) findViewById(R.id.message_et);
        send_tv = (TextView) findViewById(R.id.send_tv);
        titletext = (TextView) findViewById(R.id.titletext);
        chatlist = (ListView) findViewById(R.id.chatlist);
        titletext.setText("" + receiver_name);
        //  titletext.setText("Receiver name");
//        if (receiver_img != null && !receiver_img.equalsIgnoreCase("")) {
//            Picasso.with(ChatDetailsActivity.this).load(receiver_img).into(chatuser_img);
//        }

    }

    public class ConversessionAdapter extends BaseAdapter {
        String[] result;
        Context context;
        String finder_sts;
        private ArrayList<ChatListBean> converSessionArrayList;
        private LayoutInflater inflater = null;

        public ConversessionAdapter(ChatDetailsActivity chatActivity, ArrayList<ChatListBean> converSessionArrayList,String finder_status) {
            // TODO Auto-generated constructor stub
            context = chatActivity;
            this.finder_sts = finder_status;
            this.converSessionArrayList = converSessionArrayList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
           // return converSessionArrayList.size();
            return 3;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View rowView;
            RelativeLayout mylayout, otheruserlay;
            TextView mymessage, mydate, myname, othermsg, otherdate,otherusername;
            CircleImageView myimage, otherimage;
            rowView = inflater.inflate(R.layout.custom_chat_item, null);
            mylayout = rowView.findViewById(R.id.mylayout);
            mymessage = rowView.findViewById(R.id.mymessage);
            ImageView   messageimage1 = rowView.findViewById(R.id.messageimage1);
            ImageView messageimage2 = rowView.findViewById(R.id.messageimage2);
            mydate = rowView.findViewById(R.id.mydate);
            myimage = rowView.findViewById(R.id.myimage);


            otheruserlay = rowView.findViewById(R.id.otheruserlay);
            othermsg = (TextView) rowView.findViewById(R.id.othermsg);
            otherusername = (TextView) rowView.findViewById(R.id.otherusername);
            otherdate = (TextView) rowView.findViewById(R.id.otherdate);
            otherimage = rowView.findViewById(R.id.otherimage);
            String idd1="";
//            if(AppsConstant.userid.equalsIgnoreCase("")){
//                idd1 = HomeFragment.guestuserid;
//            }else{
            idd1 = session.getUserID();
            //  }

          //  if (idd1.equalsIgnoreCase(converSessionArrayList.get(position).getSenderId())){
                 if (position==2){
                mylayout.setVisibility(View.VISIBLE);
                otheruserlay.setVisibility(View.GONE);

//                if(!converSessionArrayList.get(position).getChatMessage().equalsIgnoreCase("")) {
//                    mymessage.setText("" + converSessionArrayList.get(position).getChatMessage());
//                    mymessage.setVisibility(View.VISIBLE);
//                    //   messageimage2.setVisibility(View.GONE);
//                }else{
//                    mymessage.setVisibility(View.GONE);
//                }



//                if(!converSessionArrayList.get(position).getChatImage().equalsIgnoreCase("") && !converSessionArrayList.get(position).getChatImage().equalsIgnoreCase(""+BaseUrl.image_baseurl)) {
//                    if(!converSessionArrayList.get(position).getChatImage().equalsIgnoreCase("")) {
//                        //  Picasso.with(ChatDetailsActivity.this).load("" + converSessionArrayList.get(position).getChatImage()).placeholder(R.drawable.placeholder).fit().centerCrop().into(otherimage);
//                        Picasso.get().load(converSessionArrayList.get(position).getChatImage()).placeholder(R.drawable.placeholder).into(messageimage1);
//                    }
//
//                    messageimage1.setVisibility(View.VISIBLE);
//                    //   messageimage2.setVisibility(View.GONE);
//                }else{
//                    messageimage1.setVisibility(View.GONE);
//                }
//
//                mydate.setText(""+converSessionArrayList.get(position).getDateTime());
//                if(!session.getUserDetails().getImage().equalsIgnoreCase("")) {
//                    // Picasso.with(ChatDetailsActivity.this).load("" + session.getUserDetails().getImage()).placeholder(R.drawable.profile_ic).fit().centerCrop().into(myimage);
//                    Picasso.get().load(session.getUserDetails().getImage()).placeholder(R.drawable.profile_ic).into(myimage);
//
//                }
            }
            else {

                mylayout.setVisibility(View.GONE);
                otheruserlay.setVisibility(View.VISIBLE);

//                if(!converSessionArrayList.get(position).getChatMessage().equalsIgnoreCase("")) {
//                    othermsg.setText("" + converSessionArrayList.get(position).getChatMessage());
//                    othermsg.setVisibility(View.VISIBLE);
//                    //   messageimage2.setVisibility(View.GONE);
//                }else{
//                    othermsg.setVisibility(View.GONE);
//                }
//
//
//
//                if(!converSessionArrayList.get(position).getChatImage().equalsIgnoreCase("") && !converSessionArrayList.get(position).getChatImage().equalsIgnoreCase(""+BaseUrl.image_baseurl)) {
//                    if(!converSessionArrayList.get(position).getChatImage().equalsIgnoreCase("")) {
//                        //  Picasso.with(ChatDetailsActivity.this).load("" + converSessionArrayList.get(position).getChatImage()).placeholder(R.drawable.placeholder).fit().centerCrop().into(otherimage);
//                        Picasso.get().load(converSessionArrayList.get(position).getChatImage()).placeholder(R.drawable.placeholder).into(messageimage2);
//                    }
//
//                    messageimage2.setVisibility(View.VISIBLE);
//                    //   messageimage2.setVisibility(View.GONE);
//                }else{
//                    messageimage2.setVisibility(View.GONE);
//                }
//
//
//                otherusername.setText(converSessionArrayList.get(position).getSenderDetail().getFirst_name()+" "+converSessionArrayList.get(position).getSenderDetail().getLast_name());
//                otherdate.setText(converSessionArrayList.get(position).getDateTime());
//                if(receiver_img!=null && !receiver_img.equalsIgnoreCase("") && !receiver_img.equalsIgnoreCase("null")) {
//                    // Picasso.with(ChatDetailsActivity.this).load(receiver_img).placeholder(R.drawable.profile_ic).fit().centerCrop().into(otherimage);
//                    Picasso.get().load(receiver_img).placeholder(R.drawable.profile_ic).into(otherimage);
//
//                }

            }





            return rowView;
        }

    }

    public class SendMessage extends AsyncTask<String, String, String> {
        String idd1="";
        protected void onPreExecute() {
            try {
                prosts = true;
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            prgressbar.setVisibility(View.VISIBLE);
//            if(AppsConstant.userid.equalsIgnoreCase("")){
//                idd1 = HomeFragment.guestuserid;
//            }else{
            idd1 = session.getUserID();
            //   }
            System.out.println("yyyyyyyyyyyy = "+idd1);
        }
        @Override
        protected String doInBackground(String... strings) {
//http://mobileappdevelop.co/NAXCAN/webservice/insert_chat?sender_id=1&receiver_id=2&chat_message=hello
            String charset = "UTF-8";
            String requestURL = BaseUrl.baseurl + "insert_chat";
            Log.e("requestURL >>", requestURL+"?sender_id="+idd1+"&child_id="+request_id+"&type="+type+"&receiver_id="+receiver_id+"&chat_message="+messagetext+"&request_id="+request_id+"&timezone="+time_zone+"&request_name="+product_name+"&date_time="+date_time+"&chat_image=");

            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                Log.e("SENDER ", "ID" + session.getUserID());
                multipart.addFormField("sender_id", idd1);
                System.out.println("yyyyyyyyyyyy33333 = "+idd1);
                multipart.addFormField("type", type);

                multipart.addFormField("address", address);
                multipart.addFormField("lat", lat);
                multipart.addFormField("lon", lon);
                multipart.addFormField("child_id", request_id);
                multipart.addFormField("receiver_id", receiver_id);
                multipart.addFormField("chat_message", messagetext);
                multipart.addFormField("request_id", request_id);
                multipart.addFormField("timezone", time_zone);
                multipart.addFormField("property_id", product_id);
                multipart.addFormField("property_name", product_name);
                multipart.addFormField("request_name", product_name);
                multipart.addFormField("date_time", date_time);

                Log.e("request_id >>", "  ll ss "+session.getUserID()+" " + receiver_id+" "+type);
                if (ImagePath.equalsIgnoreCase("")) {
                    multipart.addFormField("chat_image", "");
                } else {
                    File ImageFile = new File(ImagePath);
                    multipart.addFilePart("chat_image", ImageFile);
                }
                List<String> response = multipart.finish();
                String Jsondata = "";
                for (String line : response) {
                    Jsondata = line;
                    Log.e("Send msg Response ====", Jsondata);
                }
                JSONObject object = new JSONObject(Jsondata);
                return Jsondata;

            } catch (UnsupportedEncodingException e) {


                e.printStackTrace();
            } catch (IOException e) {


                e.printStackTrace();
            } catch (JSONException e) {


                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String lenghtOfFile) {
            prgressbar.setVisibility(View.GONE);
            prosts = false;
            if (lenghtOfFile==null){
                messagetext = "";
                message_et.setText("");
                ImagePath = "";
                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();
            }
            else if (lenghtOfFile.equalsIgnoreCase("")){
                messagetext = "";
                message_et.setText("");
                ImagePath = "";
                prgressbar.setVisibility(View.GONE);
                new MyConverSession().execute();
            }
            else {
                try {
                    JSONObject jsonObject = new JSONObject(lenghtOfFile);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")){
                        messagetext = "";
                        message_et.setText("");
                        ImagePath = "";

                        new MyConverSession().execute();
                    }
                    else if (jsonObject.getString("status").equalsIgnoreCase("0")){
                        if (jsonObject.getString("result").equalsIgnoreCase("blocked user unblock first")){
                            // Toast.makeText(ChatDetailsActivity.this,getResources().getString(R.string.donotmessagesend),Toast.LENGTH_LONG).show();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }


    }

    private class MyConverSession extends AsyncTask<String, String, String> {
        String idd1="";
        @Override
        protected void onPreExecute() {
            if(callstatus.equalsIgnoreCase("0")) {
                prgressbar.setVisibility(View.VISIBLE);
            }else{
                callstatus="";
            }
            chatListBeanArrayList = new ArrayList<>();
            chatListBeanArrayList.clear();
            super.onPreExecute();
            try {
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if(AppsConstant.userid.equalsIgnoreCase("")){
//                idd1 = HomeFragment.guestuserid;
//            }else{
            idd1 = session.getUserID();
            //  }
        }
        @Override
        protected String doInBackground(String... strings) {
//http://mobileappdevelop.co/NAXCAN/webservice/get_chat_detail?sender_id=1&receiver_id=2&chat_message=hello%20mil%20gyi%20ws
            try {
                String postReceiverUrl = BaseUrl.baseurl + "get_chat_detail?";
                Log.e("GET CHAT DETAIL",">>"+postReceiverUrl+"sender_id="+receiver_id+"&receiver_id="+ idd1 +"&request_id="+request_id+"&type="+type);
                System.out.println("sssssssssssssssssssssss = "+postReceiverUrl+"sender_id="+receiver_id+"&receiver_id="+ idd1 +"&request_id="+request_id+"&type="+type);
                URL url = new URL(postReceiverUrl);
                Map<String, Object> params = new LinkedHashMap<>();
                Log.e("request_id >>", "  ll " + request_id);
                params.put("sender_id", receiver_id);
                params.put("receiver_id", idd1);
                params.put("request_id", request_id);
                params.put("type", "Normal");
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(urlParameters);
                writer.flush();

                String response = "";
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                writer.close();
                reader.close();


                return response;

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            prgressbar.setVisibility(View.GONE);
            Log.e("ChatMessages >>", "" + result);
            if (result == null) {

            } else if (result.isEmpty()) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int jsonlenth = 0;
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                        ChatBean successData = new Gson().fromJson(result, ChatBean.class);
                        chatListBeanArrayList.addAll(successData.getResult());
                        jsonlenth = successData.getResult().size();
                        if (jsonlenth > beforelength) {
                            // Collections.reverse(converSessionArrayList);
                            conversessionAdapter = new ConversessionAdapter(ChatDetailsActivity.this, chatListBeanArrayList,successData.getFinder_status());
                            chatlist.setAdapter(conversessionAdapter);
                            chatlist.setSelection(chatListBeanArrayList.size() - 1);
                            conversessionAdapter.notifyDataSetChanged();
                            beforelength = jsonlenth;
//                            address = chatListBeanArrayList.get(0).getAddress();
//                            lat = chatListBeanArrayList.get(0).getLat();
//                            lon = chatListBeanArrayList.get(0).getLon();
//
//                            if(lat !=null && !lat.equalsIgnoreCase("")) {
//                                latitude = Double.parseDouble(lat);
//                                longitude = Double.parseDouble(lon);
//
//
//                            }
//                            addresstxt.setText(address);
//                            MarkerOptions markers = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.employee)).flat(true).anchor(0.5f, 0.5f);
//                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
//                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
//                            gMap.animateCamera(zoom);
//                            gMap.addMarker(markers);
//                            gMap.moveCamera(center);
//                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 8);
//                            gMap.animateCamera(zoom);
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }


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
                    Log.e("PATHFromGallery", "" + FinalPath);
                    Log.e("PATHGetGallery", "" + getPath(selectedImage));
                    decodeFile(ImagePath);
                    break;
                case 2:

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ImagePath = saveToInternalStorage(photo);
                    Log.e("PATHCamera", "" + ImagePath);
                    //  avt_imag.setImageBitmap(photo);
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
        ContextWrapper cw = new ContextWrapper(ChatDetailsActivity.this);
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
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        date_time = format.format(today);
        System.out.println("CURRENT " + date_time);
        prosts = false;
        new SendMessage().execute();


    }
}
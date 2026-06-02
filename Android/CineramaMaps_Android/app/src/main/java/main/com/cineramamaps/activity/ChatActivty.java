package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityAllCategoryBinding;
import main.com.cineramamaps.databinding.ActivityChatActivtyBinding;
import main.com.cineramamaps.model.Chatdata;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivty extends AppCompatActivity {
    private LinearLayout buyinglay, sellinglay;
    private TextView bid_item_msg_tv, selected_item_msg_tv;
    private View sellingview, buyingview;
    private ListView sellinglist, buyinglist;


    private View view;
    ListView favoritelistview;
    TextView nodatatxt;
    String imeinumber="";
    ProgressBar progress_bar,progressbar;
    RecyclerView rv_cat2;
    EditText search_et;
    private BuyingChatAdapter myPostItemsAdapter;
    private ArrayList<Chatdata> buymessageBeanListArrayList = new ArrayList<>();


    private String user_log_data = "", user_id = "",type_str="BUYER";
    final Handler handler = new Handler();
    final int delay = 7000;
    private ArrayList<String> artBeanListArrayList,mybiditemlist;
    private RelativeLayout sellingrellay, buyinglayrel;
    String callstatus="";


    ActivityChatActivtyBinding binding;
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
      //  getMyMessagesConList();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_activty);
        session = SessionManager.get(ChatActivty.this);
        rv_cat2= findViewById(R.id.rv_cat2);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(ChatActivty.this, 1);
        gridLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        rv_cat2.setLayoutManager(gridLayoutManager1);
        idinti();
        clickevent();
        myPostItemsAdapter = new BuyingChatAdapter(ChatActivty.this,buymessageBeanListArrayList);
        buyinglist.setAdapter(myPostItemsAdapter);
        selected_item_msg_tv.setVisibility(View.GONE);
        buyinglist.setVisibility(View.VISIBLE);
    }


    private void getMyMessagesConList() {
        String idd1="";
//        if(AppsConstant.userid.equalsIgnoreCase("")){
//            idd1 = imeinumber;
//        }else{
//            idd1 = AppsConstant.userid;
//        }
        buymessageBeanListArrayList = new ArrayList<>();
        buymessageBeanListArrayList.clear();// searchbuymessageBeanListArrayList = new ArrayList<>();

        progressbar.setVisibility(View.VISIBLE);


        //Call<ResponseBody> call = ApiClient.getApiInterface().getConversationList(user_id,"BUYER");
        System.out.println("sssssssssssss = "+ BaseUrl.baseurl+"get_conversation_detail?receiver_id="+session.getUserID());
        Call<ResponseBody> call = ApiCall.get().Create().getBuyConversationList(""+session.getUserID());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("sssssssssssssssssssss = "+response);

                progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        System.out.println("sssssssssssssssssssss "+responseData);
                        Log.e("BUYER > ", " >" + responseData);
                        if (object.getString("status").equals("1")) {
                            // MessageBean successData = new Gson().fromJson(responseData, MessageBean.class);
                            //  buymessageBeanListArrayList.addAll(successData.getResult());
                            //  searchbuymessageBeanListArrayList.addAll(successData.getResult());
                            String details = object.getString("result");
                            JSONArray ja = new JSONArray(details);
                            for(int y=0;y<ja.length();y++) {
                                Chatdata oo = new Chatdata();
                                oo.setChatid("");
                                oo.setCarid(ja.getJSONObject(y).getString("child_id"));
                                // oo.setCarname(ja.getJSONObject(y).getString("request_name"));
//                                oo.setCarid("");
                                oo.setCarname("");
                                oo.setDate(ja.getJSONObject(y).getString("date"));
                                oo.setLastmessage(ja.getJSONObject(y).getString("last_message"));
                                oo.setNo_of_message(ja.getJSONObject(y).getInt("no_of_message"));

                                //oo.setSenderid(ja.getJSONObject(y).getString("u_id"));
                                if(ja.getJSONObject(y).getString("first_name").equalsIgnoreCase("")) {
                                    oo.setSendername(ja.getJSONObject(y).getString("user_name") + " " + ja.getJSONObject(y).getString("last_name"));
                                    oo.setSenderid(ja.getJSONObject(y).getString("id"));
                                }else{
                                    oo.setSendername(ja.getJSONObject(y).getString("first_name") + " " + ja.getJSONObject(y).getString("last_name"));
                                    oo.setSenderid(ja.getJSONObject(y).getString("id"));
                                }
                                oo.setSenderimage(ja.getJSONObject(y).getString("image"));
                                buymessageBeanListArrayList.add(oo);
                            }
                            myPostItemsAdapter = new BuyingChatAdapter(ChatActivty.this,buymessageBeanListArrayList);
                            buyinglist.setAdapter(myPostItemsAdapter);
                            selected_item_msg_tv.setVisibility(View.GONE);
                            buyinglist.setVisibility(View.VISIBLE);

                        }else{
                            selected_item_msg_tv.setVisibility(View.VISIBLE);
                            buyinglist.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                t.printStackTrace();
                progressbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }



    public class BuyingChatAdapter extends BaseAdapter {
        private Context context; //context
        ArrayList<Chatdata> buymessageBeanListArrayList;
        public BuyingChatAdapter(Context context, ArrayList<Chatdata> buymessageBeanListArrayList) {
            this.context = context;
            this.buymessageBeanListArrayList = buymessageBeanListArrayList;
        }

        @Override
        public int getCount() {
             return 5; //returns total of items in the list
          //  return buymessageBeanListArrayList == null ? 0 : buymessageBeanListArrayList.size();
            // return 3;

        }

        @Override
        public Object getItem(int position) {
            return null;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }
        public void filter(String charText) {
//            Log.e("COMEADDD","DDDDDDD");
//            //charText = charText.toLowerCase(Locale.getDefault());
//            charText = charText.toString().toLowerCase();
//            buymessageBeanListArrayList.clear();
//            if (charText.length() == 0) {
//                buymessageBeanListArrayList.addAll(searchbuymessageBeanListArrayList);
//            } else {
//                for (MessageBeanList wp : searchbuymessageBeanListArrayList) {
//                    if (wp.getUserDetails().getUserName().toLowerCase().contains(charText))//.toLowerCase(Locale.getDefault())
//                    {
//                        Log.e("COMEADDD","DDD");
//                        buymessageBeanListArrayList.add(wp);
//                    }
//                }
//                if (buymessageBeanListArrayList==null||buymessageBeanListArrayList.isEmpty()){
//                    //no.setVisibility(View.VISIBLE);
//                    // noitemtv.setText(""+getResources().getString(R.string.noitmewithsearch));
//                }
//                else {
//                    // noitemtv.setVisibility(View.GONE);
//                }
//            }
//            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // inflate the layout for each list row
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.custom_last_msg_lay, parent, false);
            //find view from adapter item layout
            CircleImageView userimg= convertView.findViewById(R.id.userimg);
            TextView username_tv = convertView.findViewById(R.id.username_tv);
            TextView unreadcount = convertView.findViewById(R.id.unreadcount);
            TextView date_time_tv = convertView.findViewById(R.id.date_time_tv);
            TextView chatfor = convertView.findViewById(R.id.chatfor);
            TextView lastmessage = convertView.findViewById(R.id.lastmessage);
//            lastmessage.setText(""+buymessageBeanListArrayList.get(position).getLastmessage());
//            chatfor.setText("Chat for "+buymessageBeanListArrayList.get(position).getCarname());
//            username_tv.setText(""+buymessageBeanListArrayList.get(position).getSendername());
//            date_time_tv.setText(""+buymessageBeanListArrayList.get(position).getDate());
//            //  Picasso.with(ChatActivty.this).load(buymessageBeanListArrayList.get(position).getSenderimage()).placeholder(R.drawable.profile_ic).into(userimg);
//            Picasso.get().load(buymessageBeanListArrayList.get(position).getSenderimage()).placeholder(R.drawable.profile_ic).into(userimg);
            if (position==0 && position==2){
           // if (buymessageBeanListArrayList.get(position).getNo_of_message()==0){
                unreadcount.setVisibility(View.GONE);
            }
            else {
                unreadcount.setVisibility(View.GONE);
              //  unreadcount.setText(""+buymessageBeanListArrayList.get(position).getNo_of_message());
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ChatActivty.this, ChatDetailsActivity.class);
//                    i.putExtra("receiver_id", "" + buymessageBeanListArrayList.get(position).getSenderid());
//                    i.putExtra("receiver_name", "" +buymessageBeanListArrayList.get(position).getSendername() );
//                    i.putExtra("receiver_img", "" +buymessageBeanListArrayList.get(position).getSenderimage() );
//                    i.putExtra("product_id", "" + buymessageBeanListArrayList.get(position).getCarid());
//                    i.putExtra("product_name", "" + buymessageBeanListArrayList.get(position).getCarname());
//                    i.putExtra("type", "Normal");
                    i.putExtra("receiver_id", "1");
                    i.putExtra("receiver_name", "Sender name" );
                    i.putExtra("receiver_img", "Sender" );
                    i.putExtra("product_id", "1");
                    i.putExtra("product_name", "Maruti Swift VDI");
                    i.putExtra("type", "Normal");
                    startActivity(i);
                }
            });

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

    }

    private void idinti() {
        search_et = findViewById(R.id.search_et);
        buyinglayrel = findViewById(R.id.buyinglayrel);
        selected_item_msg_tv = findViewById(R.id.selected_item_msg_tv);
        buyinglist = findViewById(R.id.buyinglist);
        sellingrellay = findViewById(R.id.sellingrellay);
        bid_item_msg_tv = findViewById(R.id.bid_item_msg_tv);
        progressbar = findViewById(R.id.progressbar);

        sellinglist = findViewById(R.id.sellinglist);


//        myPostItemsAdapter = new BuyingChatAdapter(ChatActivty.this,buymessageBeanListArrayList);
//        buyinglist.setAdapter(myPostItemsAdapter);
//        addTextListener();
    }
}
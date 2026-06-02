package main.com.cineramamaps.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.database.DBManager;
import main.com.cineramamaps.databinding.ActivityFilterBinding;
import main.com.cineramamaps.databinding.LayoutCustmFilterCategoryBinding;
import main.com.cineramamaps.model.CategoryBean;
import main.com.cineramamaps.model.CategoryBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FilterActivity extends AppCompatActivity {
    ActivityFilterBinding binding;
    private DBManager dbManager;
    String category_id="", diet_type_vegan="",day_name="",start_time="",end_time="";
    private SessionManager session;
    FilterCategoryAdapter adapter;
    ArrayList<CategoryBeanList> catBeanLists = new ArrayList<>();
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        session = SessionManager.get(FilterActivity.this);
        clickevent();
getCategory();

    }

    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.timegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = binding.timegroup.findViewById(checkedId);
                int index = binding.timegroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        Date d = new Date();
                        day_name = sdf.format(d);
                        start_time = "";
                        end_time = "";
                       binding.starttimetxt.setText("");
                       binding.endtimetxt.setText("");
                        break;
                    case 1: // secondbutton

                        day_name = "";
                        start_time = "";
                        end_time = "";
                        SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
                        Date d1 = new Date();
                        day_name = sdf1.format(d1);

                        break;
                }
            }
        });
        binding.starttimetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeFunction("start");
            }
        });
        binding.endtimetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeFunction("end");
            }
        });
binding.searchbut.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(binding.swichbtn.isChecked()==true){
            diet_type_vegan = "Yes";
        }else{
            diet_type_vegan = "";
        }

        ArrayList<String> idlist = new ArrayList<>();
        idlist.clear();


        for (int i = 0; i < catBeanLists.size(); i++) {
            if (catBeanLists.get(i).getStatus().equalsIgnoreCase("YES")) {
                idlist.add(catBeanLists.get(i).getId());
            }
        }


        StringBuilder commaSepValueBuilder1 = new StringBuilder();
        for (int ii = 0; ii < idlist.size(); ii++) {
            // append the value into the builder
            commaSepValueBuilder1.append(idlist.get(ii));
            if (ii != idlist.size() - 1) {
                commaSepValueBuilder1.append(",");
            }
        }
        category_id = commaSepValueBuilder1.toString();
        start_time = binding.starttimetxt.getText().toString();
        end_time = binding.endtimetxt.getText().toString();
        System.out.println("sssssssssssssssss category_id "+category_id);
        System.out.println("sssssssssssssssss diet_type_vegan "+diet_type_vegan);
        System.out.println("sssssssssssssssss day_name "+day_name);
        System.out.println("sssssssssssssssss start_time "+start_time);
        System.out.println("sssssssssssssssss end_time "+end_time);
        Intent i = new Intent(FilterActivity.this, MapsListActivity.class);
        i.putExtra("category_id",""+category_id);
        i.putExtra("diet_type_vegan",""+diet_type_vegan);
        i.putExtra("day_name",""+day_name);
        i.putExtra("start_time",""+start_time);
        i.putExtra("end_time",""+end_time);
        startActivity(i);
    }
});



    }
    private void TimeFunction(final String tv_type) {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(FilterActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                int hour = selectedHour;
                int fullhour = selectedHour;
                int minutes = selectedMinute;
                /*String timeSet = "";
                if (hour > 12) {
                    hour -= 12;
                    timeSet = "PM";
                } else if (hour == 0) {
                    hour += 12;
                    timeSet = "AM";
                } else if (hour == 12) {
                    timeSet = "PM";
                } else {
                    timeSet = "AM";
                }*/
                String hour_str="";
                if (hour<10){
                    hour_str = "0"+hour;
                }
                else {
                    hour_str = ""+hour;
                }
                String min = "";
                if (minutes < 10) {
                    min = "0" + minutes;
                }
                else {
                    min = String.valueOf(minutes);
                    String time_str = "" + hour_str + ":" + min ;
                    //s_time = selectedHour + ":" + selectedMinute;
                    if (tv_type.equalsIgnoreCase("start")) {
                        binding.starttimetxt.setText("" + time_str);
                    } else {
                        binding.endtimetxt.setText("" + time_str);
                    }

                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void getCategory() {
//        shimmer_view_category.setVisibility(View.VISIBLE);
//        shimmer_view_category.startShimmerAnimation();

        ApiCall.get().Create().getCategory(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              //  progressbar.setVisibility(View.GONE);
                catBeanLists = new ArrayList<>();
                catBeanLists.clear();
                Log.e("AllFeeds  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            CategoryBean successData = new Gson().fromJson(responseData, CategoryBean.class);
                            catBeanLists.addAll(successData.getResult());
                            for(int h=0;h<catBeanLists.size();h++){
                                catBeanLists.get(h).setStatus("No");
                            }
                            adapter =       new FilterCategoryAdapter(catBeanLists,FilterActivity.this,true);
                            binding.categorylistrecy.setAdapter(adapter);
                        } else {

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }


    public class FilterCategoryAdapter extends RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder> {

        private ArrayList<Bitmap> horizontalList;

        Activity context;
        boolean b;


        public FilterCategoryAdapter(List<CategoryBeanList> categoryBeanListArrayList, Activity context, boolean b) {
            this.context = context;
            this.b = b;
            this.horizontalList = horizontalList;

        }








        @NonNull
        @Override
        public FilterCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutCustmFilterCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_filter_category, parent, false);
            return new FilterCategoryAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FilterCategoryAdapter.ViewHolder holder, int position) {
     holder.binding.nametv.setText("Service "+(position+1));
    // holder.binding.nametv.setText(catBeanLists.get(position).getCategoryName());

            if (catBeanLists.get(position).getStatus().equalsIgnoreCase("Yes")){
                holder.binding.catlay.setBackgroundColor(getResources().getColor(R.color.buttoncol));
            }else{
                holder.binding.catlay.setBackgroundColor(getResources().getColor(R.color.white));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    restid = providerBeanLists.get(position).getId();
//                    pos = position;
                    if (catBeanLists.get(position).getStatus().equalsIgnoreCase("Yes")){
                        catBeanLists.get(position).setStatus("No");
                    }else{
                        catBeanLists.get(position).setStatus("Yes");
                    }
                    adapter.notifyDataSetChanged();

                }
            });

        }


        class ViewHolder extends RecyclerView.ViewHolder {
            LayoutCustmFilterCategoryBinding binding;

            public ViewHolder(@NonNull LayoutCustmFilterCategoryBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }



        @Override
        public int getItemCount() {
           // return 8;
             return catBeanLists == null ? 0 : catBeanLists.size();

        }


    }



}
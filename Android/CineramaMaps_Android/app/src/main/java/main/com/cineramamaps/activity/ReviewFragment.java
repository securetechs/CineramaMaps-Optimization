package main.com.cineramamaps.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.adapter.ReveiwListAdapter;
import main.com.cineramamaps.constant.ExpandableHeightListView;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.model.Menudata;
import main.com.cineramamaps.model.PlacesBean;
import main.com.cineramamaps.model.RatingReview;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {
    List<RatingReview> reviewlist = new ArrayList<>();
    ExpandableHeightListView reviewexplist;
    private String language = "";
    MyLanguageSession myLanguageSession;
    TextView submitbut;
    private View view;
    ProgressBar progressbar;
    SessionManager session;
    private RecyclerView rv_product;
    ArrayList<Menudata> menulist = new ArrayList<>();
    String itemid="0",status="No",cart_cat_id="";
    String item_id="",item_price="",cartcount="",catid="",subcatid="",childsubcatid="",suppliesstatus="No";
    boolean btnclick = false;
    ProgressBar progress_bar;
    RelativeLayout botumlay;
    Button continuebtn;
    TextView cart_item_txt,totalservicetxt ;
    ReveiwListAdapter adapter1;
    String image ="",cityid="";
    ProductLisFragment.RecyclerItemAdapter1 adapter;
    public ReviewFragment(ArrayList<Menudata> menulist,String image,String cityid) {
        this.menulist = menulist;
        this.image = image;
        this.cityid = cityid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review, container, false);
//        cart_item_txt = getActivity().findViewById(R.id.cart_item_txt);
         reviewexplist = view.findViewById(R.id.reviewexplist);
        session = SessionManager.get(getActivity());
        progressbar = view.findViewById(R.id.progressbar);
        submitbut = view.findViewById(R.id.submitbut);
//        if (!image.equalsIgnoreCase("")) {
//            Picasso.with(getActivity()).load(image).placeholder(R.color.lightgrey).into(imagee);
//        }
        reviewexplist.setExpanded(true);
        getPurchasedetails();
        submitbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), GiveRatingActivity.class);
                i.putExtra("provider_id",cityid);
                i.putExtra("order_id",cityid);
                i.putExtra("reviewtype","City");
                startActivity(i);
            }
        });


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
//        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
//        String oldLanguage = language;
//        language = myLanguageSession.getLanguage();
//        if (!oldLanguage.equals(language)) {
//            getActivity().finish();
//            startActivity(getActivity().getIntent());
//        }
        // getFavfoods();
        // if(MapDetailsActivity.plan_purcahse_status.equalsIgnoreCase("Yes")) {
        getCountryMaps();
       // submitbut.setVisibility(View.VISIBLE);

//        }else{
//            listviewbut.setVisibility(View.GONE);
//            Toast.makeText(getActivity(),""+getResources().getString(R.string.youhavenosubscriptionplan),Toast.LENGTH_LONG).show();
//        }
    }


    private void getCountryMaps() {
        progressbar.setVisibility(View.VISIBLE);


        ApiCall.get().Create().getCityMapsDetails(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                Log.e("GetProducts  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.i("-------------PLACES-----------", responseData);
                        JSONObject object = new JSONObject(responseData);


                        if (object.getString("status").equalsIgnoreCase("1")) {

                            PlacesBean successData = new Gson().fromJson(responseData, PlacesBean.class);
                            reviewlist = new ArrayList<>();
                            reviewlist.clear();
                            reviewlist = successData.getResult().getRatingReview();
                             adapter1 = new ReveiwListAdapter(getActivity(), reviewlist);
                            reviewexplist.setAdapter(adapter1);

                        } else {



                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        });

    }
    private HashMap<String,String> getProductParam(String type) {


        HashMap<String,String>param=new HashMap<>();
        param.put("user_id",session.getUserID());
        //  param.put("token",session.getUserDetails().getToken());
        param.put("city_id",""+cityid);
        param.put("lang",""+language);


        return param;

    }
    private void getPurchasedetails() {
       // binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getPurchaseDetails(session.getUserID(),cityid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("sssssssssssssss = "+response);
               // binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyProfileDetail >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                          String  plan_purcahse_status = object.getString("plan_purcahse_status");
                            System.out.println("ssssssssssssssssss "+plan_purcahse_status);
                            if(plan_purcahse_status.equalsIgnoreCase("Yes")){
                                submitbut.setVisibility(View.VISIBLE);
                                // Toast.makeText(MapDetailsActivity.this,""+getResources().getString(R.string.alreadyhaveplan),Toast.LENGTH_LONG).show();
                            }else{
                                submitbut.setVisibility(View.GONE);


                            }

                        }
                        else {


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
}
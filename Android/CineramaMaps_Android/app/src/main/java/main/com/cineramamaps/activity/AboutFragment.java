package main.com.cineramamaps.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.model.CityMapBean;
import main.com.cineramamaps.model.Menudata;
import main.com.cineramamaps.model.PlacesBean;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutFragment extends Fragment {

    private View view;
    private RecyclerView rv_product;
    ArrayList<Menudata> menulist = new ArrayList<>();
    String itemid="0",status="No",cart_cat_id="";
    String item_id="",item_price="",cartcount="",catid="",subcatid="",childsubcatid="",suppliesstatus="No";
    boolean btnclick = false;
    ProgressBar progressbar;
    RelativeLayout botumlay;
    Button continuebtn;
    TextView cart_item_txt,totalservicetxt ;
    String image ="",cityid="";
    SessionManager session;
    TextView carpolicenumbername,policenumbername, abouttv,currencytv,languagetxt,clothingtv,visittv,healthtv,sockettv,communicationtv,weathertv,carenumbertv,policnumbertv;
    private String language = "";
    MyLanguageSession myLanguageSession;
    @Override
    public void onResume() {
        super.onResume();
//        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
//        String oldLanguage = language;
//        language = myLanguageSession.getLanguage();
//        if (!oldLanguage.equals(language)) {
//            finish();
//            startActivity(getIntent());
//        }
        // getFavfoods();
    }

    public AboutFragment(ArrayList<Menudata> menulist,String image,String cityid) {
        this.menulist = menulist;
        this.image = image;
        this.cityid = cityid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        myLanguageSession = new MyLanguageSession(getActivity());
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        view = inflater.inflate(R.layout.fragment_about, container, false);
        session = SessionManager.get(getActivity());
        progressbar = view.findViewById(R.id.progressbar);
          abouttv = view.findViewById(R.id.abouttv);
          currencytv = view.findViewById(R.id.currencytv);
          languagetxt = view.findViewById(R.id.languagetxt);
          clothingtv = view.findViewById(R.id.clothingtv);
          visittv = view.findViewById(R.id.visittv);
          healthtv = view.findViewById(R.id.healthtv);
          sockettv = view.findViewById(R.id.sockettv);
          communicationtv = view.findViewById(R.id.communicationtv);
          weathertv = view.findViewById(R.id.weathertv);
          carenumbertv = view.findViewById(R.id.carenumbertv);
          policnumbertv = view.findViewById(R.id.policnumbertv);
        carpolicenumbername = view.findViewById(R.id.carpolicenumbername);
        policenumbername = view.findViewById(R.id.policenumbername);

        getCountryMaps();

//        cart_item_txt = getActivity().findViewById(R.id.cart_item_txt);
//        ImageView imagee = view.findViewById(R.id.imagee);
//        if (!image.equalsIgnoreCase("")) {
//            Picasso.with(getActivity()).load(image).placeholder(R.color.lightgrey).into(imagee);
//        }


        return view;
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
                            if(language.equalsIgnoreCase("en")) {
                               // abouttv.setText(successData.getResult().getAboutCity());
                                setExpandableText(abouttv, successData.getResult().getAboutCity());
                                currencytv.setText(successData.getResult().getCurrency());
                                languagetxt.setText(successData.getResult().getOfficalLanguage());
                                clothingtv.setText(successData.getResult().getClothing());
                                visittv.setText(successData.getResult().getBestTimeToVisit());
                                healthtv.setText(successData.getResult().getHealth());
                                sockettv.setText(successData.getResult().getElectricalSocket());
                                communicationtv.setText(successData.getResult().getCommunications());
                                weathertv.setText(successData.getResult().getTheWaether());
                                carenumbertv.setText(successData.getResult().getCarPoliceNumber());
                                policnumbertv.setText(successData.getResult().getPoliceNumber());
                                carpolicenumbername.setText(successData.getResult().getCar_police_number_name());
                                policenumbername.setText(successData.getResult().getPolice_number_name());
                            }else{
                              //  abouttv.setText(successData.getResult().getAboutCityAr());
                                setExpandableText(abouttv, successData.getResult().getAboutCityAr());
                                currencytv.setText(successData.getResult().getCurrencyAr());
                                languagetxt.setText(successData.getResult().getOfficalLanguageAr());
                                clothingtv.setText(successData.getResult().getClothingAr());
                                visittv.setText(successData.getResult().getBestTimeToVisitAr());
                                healthtv.setText(successData.getResult().getHealthAr());
                                sockettv.setText(successData.getResult().getElectricalSocketAr());
                                communicationtv.setText(successData.getResult().getCommunicationsAr());
                                weathertv.setText(successData.getResult().getTheWaetherAr());
                                carenumbertv.setText(successData.getResult().getCarPoliceNumber());
                                policnumbertv.setText(successData.getResult().getPoliceNumber());
                                carpolicenumbername.setText(successData.getResult().getCar_police_number_name_ar());
                                policenumbername.setText(successData.getResult().getPolice_number_name_ar());
                            }

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

    private boolean isExpanded = false;
    private final int MAX_LINES = 4;



    private void setExpandableText(TextView textView, String fullText) {

        textView.post(() -> {

            if (!isExpanded) {

                textView.setMaxLines(5);
                textView.setText(fullText);

                textView.post(() -> {

                    Layout layout = textView.getLayout();

                    if (layout != null && layout.getLineCount() > 5) {

                        int end =
                                layout.getLineEnd(4);

                        String showText =
                                fullText.substring(0, end - 10)
                                        + "..."+getResources().getString(R.string.read_more);

                        SpannableString spannable =
                                new SpannableString(showText);

                        ClickableSpan clickableSpan =
                                new ClickableSpan() {

                                    @Override
                                    public void onClick(View widget) {
                                        isExpanded = true;
                                        setExpandableText(textView, fullText);
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setUnderlineText(false);
                                    }
                                };

                        spannable.setSpan(
                                clickableSpan,
                                showText.indexOf(""+getResources().getString(R.string.read_more)),
                                showText.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        );

                        textView.setText(spannable);
                        textView.setMovementMethod(
                                LinkMovementMethod.getInstance()
                        );

                    } else {
                        textView.setText(fullText);
                    }
                });

            } else {

                String showText =
                        fullText + " "+getResources().getString(R.string.read_less);

                SpannableString spannable =
                        new SpannableString(showText);

                ClickableSpan clickableSpan =
                        new ClickableSpan() {

                            @Override
                            public void onClick(View widget) {
                                isExpanded = false;
                                setExpandableText(textView, fullText);
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setUnderlineText(false);
                            }
                        };

                spannable.setSpan(
                        clickableSpan,
                        showText.indexOf(""+getResources().getString(R.string.read_less)),
                        showText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                textView.setText(spannable);
                textView.setMaxLines(Integer.MAX_VALUE);

                textView.setMovementMethod(
                        LinkMovementMethod.getInstance()
                );
            }
        });
    }
}
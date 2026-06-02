package main.com.cineramamaps.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import main.com.cineramamaps.R;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.model.SliderList;

public class WelcomeSliderAct1 extends AppCompatActivity {

    ArrayList<SliderList> sliderListArrayList;
    private ViewPager viewpager_img;
    private ProgressBar progressbar;
    private CustomItemImgSlide myCustomPagerAdapter;

    private String user_log_data="",user_id="";
    private RelativeLayout backbtn,botumlay;


    private ImageView questoon_img;
    private LinearLayout pager_dots;
    private ImageView[] ivArrayDotsPager;
    private TextView starttv;
    private CirclePageIndicator indicator;
    private ArrayList<String> languagelist;
    private CustomLangAdapter customLangAdapter;
    Spinner languageSpinner;

    private String language = "";
    MyLanguageSession myLanguageSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        setContentView(R.layout.activity_welcome_slider_act1);
        languageSpinner = findViewById(R.id.language_spinner);
        languagelist = new ArrayList<>();
        languagelist.add(""+getResources().getString(R.string.english));
        //  languagelist.add("French");
        languagelist.add(""+getResources().getString(R.string.arabic));

        adddata();
        idinit();
        clickevent();
        languageSpinner.setDropDownVerticalOffset(100);
        customLangAdapter = new CustomLangAdapter(WelcomeSliderAct1.this, languagelist);
        languageSpinner.setAdapter(customLangAdapter);
        if (language.equalsIgnoreCase("ar")) {
            languageSpinner.setSelection(1);
        }
        else if (language.equalsIgnoreCase("tr")) {
            languageSpinner.setSelection(2);
        }
        else {
            languageSpinner.setSelection(0);
        }

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (languagelist.get(i).equalsIgnoreCase("İngilizce") || languagelist.get(i).equalsIgnoreCase("English") || languagelist.get(i).equalsIgnoreCase("الإنجليزية") ) {
                    myLanguageSession.insertLanguage("en");
                    myLanguageSession.setLangRecreate("en");
                    Log.e("LANGUAGE_EN ", " >> " + language);
                    myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
                    String oldLanguage = language;
                    language = myLanguageSession.getLanguage();
                    if (!oldLanguage.equals(language)) {
                        finish();
                        startActivity(getIntent());
                    }
                }
//                else  if (languagelist.get(i).equalsIgnoreCase("Türkçe") || languagelist.get(i).equalsIgnoreCase("Turkish") || languagelist.get(i).equalsIgnoreCase("اللغة التركية") ) {
//                    myLanguageSession.insertLanguage("tr");
//                    myLanguageSession.setLangRecreate("tr");
//                    Log.e("LANGUAGE_EN ", " >> " + language);
//                    myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
//                    String oldLanguage = language;
//                    language = myLanguageSession.getLanguage();
//                    if (!oldLanguage.equals(language)) {
//                        finish();
//                        startActivity(getIntent());
//                    }
//                }

                else {
                    myLanguageSession.insertLanguage("ar");
                    myLanguageSession.setLangRecreate("ar");
                    Log.e("LANGUAGE_DE ", " >> " + language);
                    myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
                    String oldLanguage = language;
                    language = myLanguageSession.getLanguage();
                    if (!oldLanguage.equals(language)) {
                        finish();
                        startActivity(getIntent());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void adddata() {
        sliderListArrayList = new ArrayList<>();
        SliderList sliderList = new SliderList();
        SliderList sliderList1 = new SliderList();
        SliderList sliderList2 = new SliderList();


//        sliderList.setMessage(""+getResources().getString(R.string.slidetxt1));
//        sliderList.setMessage1(""+getResources().getString(R.string.slidetxt2));
//        sliderList1.setMessage(""+getResources().getString(R.string.slidetxt3));
//        sliderList1.setMessage1(""+getResources().getString(R.string.slidetxt4));
//        sliderList2.setMessage(""+getResources().getString(R.string.slidetxt5));
//        sliderList2.setMessage1(""+getResources().getString(R.string.slidetxt6));

//        sliderList.setImagedrawable(R.drawable.bg);
//        sliderList1.setImagedrawable(R.drawable.bg);
//        sliderList2.setImagedrawable(R.drawable.bg);
//        sliderList3.setImagedrawable(R.drawable.bg);
//        sliderList4.setImagedrawable(R.drawable.bg);
        sliderListArrayList.add(sliderList);
        sliderListArrayList.add(sliderList1);
        sliderListArrayList.add(sliderList2);
//        sliderListArrayList.add(sliderList3);
//        sliderListArrayList.add(sliderList4);
    }


    private void clickevent() {

    }

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

    private void idinit() {

        indicator = findViewById(R.id.indicator);
        starttv = findViewById(R.id.starttv);
        botumlay = findViewById(R.id.botumlay);
        pager_dots = findViewById(R.id.pager_dots);

        viewpager_img = findViewById(R.id.viewpager_img);
        myCustomPagerAdapter = new CustomItemImgSlide(WelcomeSliderAct1.this,sliderListArrayList);
        viewpager_img.setAdapter(myCustomPagerAdapter);
        indicator.setViewPager(viewpager_img);
        progressbar = findViewById(R.id.progressbar);
        botumlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewpager_img.getCurrentItem()==sliderListArrayList.size()-2){
                    viewpager_img.setCurrentItem(viewpager_img.getCurrentItem()+1);
                    starttv.setText(""+getResources().getString(R.string.start1));
                } else if (viewpager_img.getCurrentItem()==sliderListArrayList.size()-1){
//                    Intent i = new Intent(WelcomeSliderAct1.this, ChooseAccountTypeAct.class);
//                    i.putExtra("type","USER");
//                    i.putExtra("comefrom","welcome");
//                    startActivity(i);
//                    finish();
                    Intent i = new Intent(WelcomeSliderAct1.this, WelcomeActivity.class);
                    startActivity(i);
                }
                else {
                    viewpager_img.setCurrentItem(viewpager_img.getCurrentItem()+1);
                    starttv.setText(""+getResources().getString(R.string.next));
                }
            }
        });

        viewpager_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if (position==0) {
                    starttv.setText(""+getResources().getString(R.string.next));
                }
                else if (position==1) {
                    starttv.setText(""+getResources().getString(R.string.next));
                }
                if (position==2) {
                    starttv.setText(""+getResources().getString(R.string.start1));
                }
            }
        });


    }


    public class CustomItemImgSlide extends PagerAdapter {
        Context context;
        LayoutInflater layoutInflater;
        ArrayList<SliderList> sliderListArrayList;

        public CustomItemImgSlide(Context context, ArrayList<SliderList> sliderListArrayList) {
            this.context = context;
            this.sliderListArrayList = sliderListArrayList;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.custom_welcome1, collection, false);
            TextView msgtv = layout.findViewById(R.id.msgtv);
            TextView msgtv1 = layout.findViewById(R.id.msgtv1);
//            RelativeLayout login_lay = layout.findViewById(R.id.login_lay);
//            RelativeLayout skipbtn = layout.findViewById(R.id.skipbtn);
            ImageView image_but = layout.findViewById(R.id.image_but);
//
//            login_lay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(WelcomeSliderAct1.this, SetLocation.class);
//                    i.putExtra("type","USER");
//                    i.putExtra("comefrom","welcome");
//                    startActivity(i);
//
//                }
//            });
//
//            skipbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent in = new Intent(WelcomeSliderAct1.this, MainActivity.class);
//                    in.putExtra("classstatus", "signup");
//                    in.putExtra("ss", "home");
//                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    WelcomeSliderAct1.this.startActivity(in);
//                    finish();
//
//                }
//            });



            if (position==0){

                image_but.setImageResource(R.drawable.slide_1);
                msgtv.setText(sliderListArrayList.get(position).getMessage());
                msgtv1.setText(sliderListArrayList.get(position).getMessage1());
            }
            else if (position==1){
                image_but.setImageResource(R.drawable.slide_2);
                msgtv.setText(sliderListArrayList.get(position).getMessage());
                msgtv1.setText(sliderListArrayList.get(position).getMessage1());
            }
            else if (position==2){
                image_but.setImageResource(R.drawable.slide_3);
                msgtv.setText(sliderListArrayList.get(position).getMessage());
                msgtv1.setText(sliderListArrayList.get(position).getMessage1());
            }
//            else if (position==3){
//                image_but.setImageResource(R.drawable.slide_4);
//            }
//            else if (position==4){
//                image_but.setImageResource(R.drawable.slide_5);
//            }
//            else {
//                image_but.setImageResource(R.drawable.slide_5);
//            }

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            //  return sliderListArrayList == null ? 0 : sliderListArrayList.size();
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

    }

    public class CustomLangAdapter extends BaseAdapter {
        private Context context; //context
        private ArrayList<String> languagelist;

        public CustomLangAdapter(Context context, ArrayList<String> languagelist) {
            this.context = context;
            this.languagelist = languagelist;
        }

        @Override
        public int getCount() {
            //return 10; //returns total of items in the list
            return languagelist == null ? 0 : languagelist.size();

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
            pname.setText(""+languagelist.get(position));

            return convertView;
        }
    }
}
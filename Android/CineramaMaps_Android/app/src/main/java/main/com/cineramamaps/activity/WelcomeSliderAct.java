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

import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.model.WelcomeBeanList;

public class WelcomeSliderAct extends AppCompatActivity {

    private ViewPager viewpager_img;
    private ProgressBar progressbar;
    private CustomItemImgSlide myCustomPagerAdapter;
    private String user_log_data = "", user_id = "";
    private RelativeLayout backbtn, botumlay;
    private String language = "";
    Spinner language_spinner;
    private LinearLayout count_view;

    private TextView starttv, skip_tv;
    private ArrayList<WelcomeBeanList> welcomeBeanLists;
    private ArrayList<String> languagelist;
    private CustomLangAdapter customLangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        setContentView(R.layout.activity_welcome_slider);
        language_spinner = findViewById(R.id.language_spinner);
        idinit();
        languagelist = new ArrayList<>();
        languagelist.add("" + getResources().getString(R.string.english));
        //  languagelist.add("French");
        languagelist.add("" + getResources().getString(R.string.arabic));
        // getWelcomeMessages();

        language_spinner.setDropDownVerticalOffset(100);
        customLangAdapter = new CustomLangAdapter(WelcomeSliderAct.this, languagelist);
        language_spinner.setAdapter(customLangAdapter);
        if (language.equalsIgnoreCase("ar")) {
            language_spinner.setSelection(1);
        } else if (language.equalsIgnoreCase("tr")) {
            language_spinner.setSelection(2);
        } else {
            language_spinner.setSelection(0);
        }


        language_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (languagelist.get(i).equalsIgnoreCase("İngilizce") || languagelist.get(i).equalsIgnoreCase("English") || languagelist.get(i).equalsIgnoreCase("الإنجليزية")) {
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
                } else if (languagelist.get(i).equalsIgnoreCase("Türkçe") || languagelist.get(i).equalsIgnoreCase("Turkish") || languagelist.get(i).equalsIgnoreCase("اللغة التركية")) {
                    myLanguageSession.insertLanguage("tr");
                    myLanguageSession.setLangRecreate("tr");
                    Log.e("LANGUAGE_EN ", " >> " + language);
                    myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
                    String oldLanguage = language;
                    language = myLanguageSession.getLanguage();
                    if (!oldLanguage.equals(language)) {
                        finish();
                        startActivity(getIntent());
                    }
                } else {
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
        clickevent();
        setData();
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
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.spinner_lay_lang, parent, false);

            }
            TextView pname = convertView.findViewById(R.id.pname);
            pname.setText("" + languagelist.get(position));

            return convertView;
        }
    }

    private void setData() {
        welcomeBeanLists = new ArrayList<>();
        WelcomeBeanList welcomeBeanList = new WelcomeBeanList();
        welcomeBeanList.setTitle(getResources().getString(R.string.firstmessage));
        welcomeBeanList.setDescription(getResources().getString(R.string.firstdescription));
        welcomeBeanList.setDrawableimage(R.drawable.slide1);
        welcomeBeanLists.add(welcomeBeanList);

        WelcomeBeanList welcomeBeanList2 = new WelcomeBeanList();
        welcomeBeanList2.setTitle(getResources().getString(R.string.secondmessage));
        welcomeBeanList2.setDescription(getResources().getString(R.string.seconddesc));
        welcomeBeanList2.setDrawableimage(R.drawable.slide2);
        welcomeBeanLists.add(welcomeBeanList2);


        WelcomeBeanList welcomeBeanList3 = new WelcomeBeanList();
        welcomeBeanList3.setTitle(getResources().getString(R.string.thirdmessage));
        welcomeBeanList3.setDescription(getResources().getString(R.string.thirddesc));
        welcomeBeanList3.setDrawableimage(R.drawable.slide3);
        welcomeBeanLists.add(welcomeBeanList3);

        myCustomPagerAdapter = new CustomItemImgSlide(WelcomeSliderAct.this, welcomeBeanLists);
        viewpager_img.setAdapter(myCustomPagerAdapter);
        if (language.equals("ar")) {
            viewpager_img.setRotation(180);
        }

        BindDot(0);
    }


    private void clickevent() {

    }


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

    private void idinit() {
        skip_tv = findViewById(R.id.skip_tv);
        skip_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WelcomeSliderAct.this, LoginAct.class);
                i.putExtra("type", "EMPLOYEE");
                i.putExtra("come_from", "");
                startActivity(i);
                finish();
            }
        });

        starttv = findViewById(R.id.starttv);
        botumlay = findViewById(R.id.botumlay);
        count_view = findViewById(R.id.count_view);

        viewpager_img = findViewById(R.id.viewpager_img);


        viewpager_img.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BindDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        progressbar = findViewById(R.id.progressbar);
        botumlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewpager_img.getCurrentItem() == welcomeBeanLists.size() - 2) {
                    viewpager_img.setCurrentItem(viewpager_img.getCurrentItem() + 1);
                    // starttv.setText(getResources().getString(R.string.start));
                    BindDot(viewpager_img.getCurrentItem());
                } else if (viewpager_img.getCurrentItem() == welcomeBeanLists.size() - 1) {
                    Intent i = new Intent(WelcomeSliderAct.this, LoginAct.class);
                    i.putExtra("type", "EMPLOYEE");
                    i.putExtra("come_from", "");
                    startActivity(i);
                    finish();
                } else {
                    viewpager_img.setCurrentItem(viewpager_img.getCurrentItem() + 1);
                    BindDot(viewpager_img.getCurrentItem());
                }
            }
        });
        viewpager_img.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BindDot(position);
                if (position == welcomeBeanLists.size() - 1) {

                    // starttv.setText(getResources().getString(R.string.start));
                } else {
                    // starttv.setText(getResources().getString(R.string.next));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    public class CustomItemImgSlide extends PagerAdapter {
        Context context;
        LayoutInflater layoutInflater;
        List<WelcomeBeanList> welcomeBeanLists;

        public CustomItemImgSlide(Context context, List<WelcomeBeanList> welcomeBeanLists) {
            this.context = context;
            this.welcomeBeanLists = welcomeBeanLists;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.custom_welcome, collection, false);
            TextView description_tv = layout.findViewById(R.id.description_tv);
            TextView msgtv = layout.findViewById(R.id.msgtv);
            ImageView image_but = layout.findViewById(R.id.image_but);
            if (language.equals("ar")) {

                layout.setRotation(180);
            }
            image_but.setImageResource(welcomeBeanLists.get(position).getDrawableimage());

           /* try {
                Picasso.get().load(welcomeBeanLists.get(position).getImage()).placeholder(R.color.lightgrey).into(image_but);
            }catch (Exception e){
                e.printStackTrace();
            }*/

            msgtv.setText(welcomeBeanLists.get(position).getTitle());
            description_tv.setText(welcomeBeanLists.get(position).getDescription());
            // description_tv.setText(welcomeBeanLists.get(position).getDescription());
            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return welcomeBeanLists == null ? 0 : welcomeBeanLists.size();
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


    void BindDot(int pos) {
        count_view.removeAllViews();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(35, 35);
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(70, 35);
        for (int i = 0; i < welcomeBeanLists.size(); i++) {
            ImageView view = new ImageView(WelcomeSliderAct.this);
            if (i == pos) {
                view.setLayoutParams(params2);
            } else {
                view.setLayoutParams(params);
            }

            view.setImageResource(i == pos ? R.drawable.button_round_dra_white1 : R.drawable.ic_dot_white);
            count_view.addView(view);
        }


    }

}
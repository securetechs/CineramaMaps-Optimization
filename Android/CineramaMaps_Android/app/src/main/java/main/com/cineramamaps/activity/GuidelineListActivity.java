package main.com.cineramamaps.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.CategoryAdapter;
import main.com.cineramamaps.constant.GPSTracker;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityGuidelineListBinding;
import main.com.cineramamaps.databinding.LayoutCustmHomeCategoryBinding;
import main.com.cineramamaps.model.City;
import main.com.cineramamaps.model.CityResponse;
import main.com.cineramamaps.model.GuidelineBean;
import main.com.cineramamaps.model.GuidelineBeanlist;
import main.com.cineramamaps.model.GuidelineCategoryModel;
import main.com.cineramamaps.model.GuidelineCategoryResponse;
import main.com.cineramamaps.model.GuidelineChildSubCategoryModel;
import main.com.cineramamaps.model.GuidelineChildSubCategoryResponse;
import main.com.cineramamaps.model.GuidelineSubCategoryModel;
import main.com.cineramamaps.model.GuidelineSubCategoryResponse;
import main.com.cineramamaps.model.GuidelinesSubCategory;
import main.com.cineramamaps.model.Guidelines_child_Response;
import main.com.cineramamaps.model.GuidelnCategory;
import main.com.cineramamaps.model.GuidlineChildCategory;
import main.com.cineramamaps.model.Guild_cat_response;
import main.com.cineramamaps.model.Guildline_subcat_response;
import main.com.cineramamaps.model.ProviderBeanList;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuidelineListActivity extends AppCompatActivity {
    ActivityGuidelineListBinding binding;
    ArrayList<GuidelineBeanlist> searchbuymessageBeanListArrayList = new ArrayList<>();
    ArrayList<GuidelineBeanlist> mapBeanLists = new ArrayList<>();
    String category_id = "";
    private SessionManager session;
    String type = "All", city_id = "";
    private LatLng latlong = null;
    String mark_img = "";
    AllRestaurnatAdapter1 adapter;
    TextView cleartxt;

    String diet_type_vegan = "", day_name = "", start_time = "", end_time = "";
    String selectedCity="All",id = "";
    Marker marker, myloc_marker;
    private GoogleApiClient googleApiClient;
    GPSTracker gpsTracker;
    ArrayList<ProviderBeanList> friendsBeanLstArrayList = new ArrayList<>();
    private GoogleMap gmap;
    public static double longitude = 0.0, latitude = 0.0;
    List<Marker> markerList = new ArrayList<Marker>();
    private MarkerOptions options = new MarkerOptions();

    Marker googlemarker_pos, my_job_location_marker;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0;
    Location location_ar;
    Location location;
    LocationManager locationManager;
    private String language = "";
    int userid;
    MyLanguageSession myLanguageSession;
    Context context;

    private String selectguidcat1 = "", selectguidsubcat1 = "", selectguidchilcat1 = "";
    private String selectguidcat1ns = "", selectguidsubcat1ns = "", selectguidchilcat1ns = "";

    RecyclerView category_recycler, sub_category_recycler, chil_category_recycler,
            category_recycler_ns, sub_category_recycler_ns, chil_category_recycler_ns;
    Spinner citySpinner;
    List<City> cityList = new ArrayList<>();
    List<String> dummyCities = Arrays.asList("Indore", "Dewas", "Bangalore", "Chennai");

    String user;
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

      //  getcityMaps();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guideline_list);
        context = GuidelineListActivity.this;
        session = SessionManager.get(this);
        Bundle bundle = getIntent().getExtras();

        //setDummyCities();


        diet_type_vegan = "";
        day_name = "";
        start_time = "";
        end_time = "";
        category_recycler = findViewById(R.id.guidline_cat_rec);
        sub_category_recycler = findViewById(R.id.guidline_sub_cat_rec);
        chil_category_recycler = findViewById(R.id.guidline_chil_cat_rec);

        category_recycler_ns = findViewById(R.id.guidline_cat_rec_ns);
        sub_category_recycler_ns = findViewById(R.id.guidline_sub_cat_rec_ns);
        chil_category_recycler_ns = findViewById(R.id.guidline_chil_cat_rec_ns);


        clickevent();
        //tabclickevent();
        //  initilizeMap();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(this, 2);
        binding.providerrecyclerview.setLayoutManager(mLayoutManager);
        binding.providerrecyclerview1.setLayoutManager(mLayoutManager1);
        type = "All";
        binding.mapbtn.setBackgroundResource(R.drawable.roundbtn1);
        binding.listbtn.setBackgroundResource(R.drawable.roundbtn2);
        binding.maptxt.setTextColor(getResources().getColor(R.color.white));
        binding.listtxt.setTextColor(getResources().getColor(R.color.black));
        // binding.usernametv.setText(getResources().getString(R.string.allmaps));

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (type.equalsIgnoreCase("All")) {
                    if (s == null) {

                    } else {


                        if (s.toString().length() > 0) {

                            if (adapter == null) {

                            } else {
                                Log.e("COME ", " >> " + s.toString());
                                adapter.filter(s.toString());
                            }

                        } else {

                            if (adapter == null) {

                            } else {
                                adapter.filter("");
                            }
                        }


                    }
                } else {


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        citySpinner = findViewById(R.id.citySpinner);
        setupDummyData();
        SharedPreferences preferences = getSharedPreferences("my_app_prefs", MODE_PRIVATE);
        String userId = preferences.getString("id", null);
        if(userId!= null){
            userid = Integer.parseInt(userId);
            Log.d("nulll","nullint"+userid);
        }else
        {
            userid = Integer.parseInt(session.getUserID());
            Log.d("nulll","nullsesson"+userid);
        }

        user = session.getUserID();
        fetchCities();

       // fetchinprogressCategories_ns();
        fetchinprogressCategories();




        getSubscribeMaps();

    }

    private void fetchCities() {
        Log.d("Useridget","userid"+userid);
        ApiCall.get().Create().getCities(userid).enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cityList = response.body().getResult();
                    Log.d("noncity","check "+response.body().getResult());

                    String All =getResources().getString(R.string.ccity);
                    List<String> cityNames = new ArrayList<>();
                    cityList.add(0, new City(All, All,All));
                    for (City city : cityList) {
                        if (language.equals("ar")){
                            cityNames.add(city.getNameAr());
                        }else {
                            cityNames.add(city.getName());
                        }
                        //cityNames.add(city.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(GuidelineListActivity.this,
                            R.layout.spinner_item, cityNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(adapter);

                    citySpinner.setOnTouchListener((v, event) -> {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            // Only "All" item means no real cities loaded
                            if (cityList == null || cityList.size() <= 1) {
                                Toast.makeText(GuidelineListActivity.this, "No cities available. Please subscribe first.", Toast.LENGTH_SHORT).show();
                                return true; // prevent dropdown from opening
                            }
                        }
                        return false;
                    });


                    citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Get selected city name

                           selectedCity= cityList.get(position).getId();

                            selectguidcat1 = "";
                            selectguidsubcat1 = "";
                            selectguidchilcat1 = "";
                            fetchinprogressCategories();
                            getSubscribeMaps();

                            Log.d("SelectedCity", "City selected: " + selectedCity);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Toast.makeText(GuidelineListActivity.this, "Failed to load cities", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    private void tabclickevent() {
//
//        binding.allcitytxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.allcity.setVisibility(View.VISIBLE);
//                binding.subscribecity.setVisibility(View.GONE);
//
//
//                binding.allcitytxt.setBackgroundResource(R.drawable.button_round_dra);
//                binding.subscribecitytxt.setBackgroundResource(R.color.grey_back_col);
//
//
//                binding.allcitytxt.setTextColor(getResources().getColor(R.color.white));
//                binding.subscribecitytxt.setTextColor(getResources().getColor(R.color.colorPrimary));
//
//                getcityMaps();
//
//                //type = "Company";
//                //getCityOrders();
//
//            }
//        });
//        binding.subscribecitytxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                binding.allcity.setVisibility(View.GONE);
//                binding.subscribecity.setVisibility(View.VISIBLE);
//
//
//                binding.allcitytxt.setBackgroundResource(R.color.grey_back_col);
//                binding.subscribecitytxt.setBackgroundResource(R.drawable.button_round_dra);
//
//
//                binding.allcitytxt.setTextColor(getResources().getColor(R.color.colorPrimary));
//                binding.subscribecitytxt.setTextColor(getResources().getColor(R.color.white));
//                // inprogressRecycler.setVisibility(View.GONE);
//                // type = "City";
//                // getCompanyOrders();
//                getSubscribeMaps();
//
//
//            }
//        });
//
//    }

    private void setupDummyData() {
        List<String> categories = Arrays.asList("");
        List<String> subCategories = Arrays.asList("");
        List<String> childCategories = Arrays.asList("");


        // Category adapter
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, (item, pos) -> {
            category_recycler.setVisibility(View.VISIBLE);
            category_recycler_ns.setVisibility(View.VISIBLE);
        });


        category_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        category_recycler.setAdapter(categoryAdapter);

        category_recycler_ns.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        category_recycler_ns.setAdapter(categoryAdapter);


        CategoryAdapter subAdapter = new CategoryAdapter(subCategories, (item, pos) -> {
            chil_category_recycler.setVisibility(View.VISIBLE);
            chil_category_recycler_ns.setVisibility(View.VISIBLE);

        });

        sub_category_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        sub_category_recycler.setAdapter(subAdapter);

        sub_category_recycler_ns.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        sub_category_recycler_ns.setAdapter(subAdapter);


        CategoryAdapter childAdapter = new CategoryAdapter(childCategories, (item, pos) -> {
            Toast.makeText(this, "Selected: " + item, Toast.LENGTH_SHORT).show();

        });

        //work for offer
        chil_category_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        chil_category_recycler.setAdapter(childAdapter);

        chil_category_recycler_ns.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        chil_category_recycler_ns.setAdapter(childAdapter);

    }


    //Work for non subscriber

    private void fetchinprogressCategories_ns() {

        ApiCall.get().Create().getguidnscat(userid).enqueue(new Callback<GuidelineCategoryResponse>() {
            @Override
            public void onResponse(Call<GuidelineCategoryResponse> call, Response<GuidelineCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("non","check "+response.body().toString());
                    List<GuidelineCategoryModel> guidlnlist = response.body().getResult();

                    GuidelineCategoryModel allCategory = new GuidelineCategoryModel("all", "All", "All", "الكل", "All");
                    allCategory.setId("all");
                    allCategory.setName_ar(getString(R.string.all));
                    guidlnlist.add(0, allCategory);
                    setupInprogressRecycler_ns(guidlnlist);
                } else {

                    Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GuidelineCategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupInprogressRecycler_ns(List<GuidelineCategoryModel> categoryList) {
        List<String> names = new ArrayList<>();
        for (GuidelineCategoryModel cat : categoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(cat.getName_ar());
            } else {
                names.add(cat.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            //childCategoryRecycler.setAdapter(null);
            String selectedguidcat = categoryList.get(pos).getId();
            Log.d("AllCall", "outside:  " + selectedguidcat);

            if ("all".equals(selectedguidcat)) {

                Log.d("AllCall", "inside:  " + selectedguidcat);

                // Show all items
                selectguidcat1ns = "";
                selectguidsubcat1ns = "";
                selectguidchilcat1ns = "";

                sub_category_recycler_ns.setVisibility(View.GONE);
                chil_category_recycler_ns.setVisibility(View.GONE);
                /// Upadte from here
                //getCityOrders();
                getcityMaps();

            } else {
                // Normal category selection
                Log.d("AllCall", "visibility:inside adapter click  ");
                sub_category_recycler_ns.setVisibility(View.VISIBLE);
                chil_category_recycler_ns.setAdapter(null);
                chil_category_recycler_ns.setVisibility(View.VISIBLE);
                selectguidcat1ns = selectedguidcat;
                selectguidsubcat1ns = "";
                selectguidchilcat1ns = "";
                fetchSubinprogressCategoriesns(selectedguidcat);

            }

        }, true);

        category_recycler_ns.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        category_recycler_ns.setAdapter(adapter);

    }

    private void fetchSubinprogressCategoriesns(String categoryId) {
        ApiCall.get().Create().getguidnssubcat(String.valueOf(userid), categoryId).enqueue(new Callback<GuidelineSubCategoryResponse>() {
            @Override
            public void onResponse(Call<GuidelineSubCategoryResponse> call, Response<GuidelineSubCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("non","check "+response.body().toString());
                    List<GuidelineSubCategoryModel> subCategoryList = response.body().getResult();
                    List<GuidelineSubCategoryModel> filteredList = new ArrayList<>();
                    for (GuidelineSubCategoryModel sub : subCategoryList) {
                        if (categoryId.equals(sub.getCat_id())) {
                            filteredList.add(sub);
                        }
                    }
                    if (filteredList.isEmpty()) {
                        /// Upadte from here
                        //getCityOrders();
                        getcityMaps();
                        sub_category_recycler_ns.setVisibility(View.GONE);
                        chil_category_recycler_ns.setVisibility(View.GONE);
                        Toast.makeText(context, "No Filters are available for this category", Toast.LENGTH_SHORT).show();
                    } else {
                        /// Upadte from here
                        //getCityOrders();
                        getcityMaps();

                        Log.d("AllCall", "onResponse: fetch Sub in progress");
                        sub_category_recycler_ns.setVisibility(View.VISIBLE);
                        setupSubInporgresRecyclerns(filteredList);
                    }
                } else {
//                    List<TourismSubCategory> Empty = new ArrayList<>();
                    sub_category_recycler_ns.setVisibility(View.GONE);
                    chil_category_recycler_ns.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed to load subcategories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GuidelineSubCategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSubInporgresRecyclerns(List<GuidelineSubCategoryModel> subCategoryList) {
        List<String> names = new ArrayList<>();

        for (GuidelineSubCategoryModel sub : subCategoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(sub.getName_ar());
            } else {
                names.add(sub.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            chil_category_recycler_ns.setVisibility(View.VISIBLE);
            chil_category_recycler_ns.setAdapter(null);

            GuidelineSubCategoryModel selectedSub = subCategoryList.get(pos);
            String selectedSubId = selectedSub.getId();
            String selectedId = selectedSub.getCat_id();

            selectguidsubcat1ns = selectedSubId;
            selectguidchilcat1ns = "";
            Toast.makeText(context, selectedId + selectedSubId, Toast.LENGTH_SHORT).show();


            getcityMaps();


            fetchChildinprogressCategoriesns(selectedSubId, selectedId);

        }, false);

        sub_category_recycler_ns.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        sub_category_recycler_ns.setAdapter(adapter);
    }


    private void fetchChildinprogressCategoriesns(String catId, String subCatId) {
        ApiCall.get().Create().getguidnschild(catId, subCatId, userid).enqueue(new Callback<GuidelineChildSubCategoryResponse>() {
            @Override
            public void onResponse(Call<GuidelineChildSubCategoryResponse> call, Response<GuidelineChildSubCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("non","check "+response.body().toString());
                    List<GuidelineChildSubCategoryModel> childList = response.body().getResult();


                    setupChildInprogressRecyclerns(childList);
                } else {
//                    List<GuidlineChildCategory> Empty = new ArrayList<>();
//                    setupChildInprogressRecycler(Empty);
                    Toast.makeText(context, "No child categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GuidelineChildSubCategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupChildInprogressRecyclerns(List<GuidelineChildSubCategoryModel> childList) {
        List<String> names = new ArrayList<>();
        for (GuidelineChildSubCategoryModel child : childList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(child.getName_ar());
            } else {
                names.add(child.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            // Handle selection of final child category
            selectguidchilcat1 = childList.get(pos).getId();

            getcityMaps();
            Toast.makeText(this, "Selected: " + item, Toast.LENGTH_SHORT).show();

            // Optionally call next API to show result based on final selected child category
        }, false);

        chil_category_recycler_ns.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        chil_category_recycler_ns.setAdapter(adapter);
    }


    //Work for subscriber
    private void fetchinprogressCategories() {

        sub_category_recycler.setVisibility(View.GONE);
        chil_category_recycler.setVisibility(View.GONE);
        Log.d("Useridget","userid"+userid+selectedCity);

        ApiCall.get().Create().getguidcat(selectedCity,userid).enqueue(new Callback<Guild_cat_response>() {
            @Override
            public void onResponse(Call<Guild_cat_response> call, Response<Guild_cat_response> response) {



                if (response.isSuccessful() && response.body() != null) {
                    List<GuidelnCategory> guidlnlist = response.body().getResult();

                    GuidelnCategory allCategory = new GuidelnCategory("all", "All", "All", "الكل", "All");
                    allCategory.setId("all");
                    allCategory.setName_ar(getString(R.string.all));
                    guidlnlist.add(0, allCategory);
                    setupInprogressRecycler(guidlnlist);
                } else {

                    Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Guild_cat_response> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupInprogressRecycler(List<GuidelnCategory> categoryList) {
        List<String> names = new ArrayList<>();
        for (GuidelnCategory cat : categoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(cat.getName_ar());
            } else {
                names.add(cat.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            //childCategoryRecycler.setAdapter(null);
            String selectedguidcat = categoryList.get(pos).getId();
            Log.d("AllCall", "outside:  " + selectedguidcat);

            if ("all".equals(selectedguidcat)) {

                Log.d("AllCall", "inside:  " + selectedguidcat);

                // Show all items
                selectguidcat1 = "";
                selectguidsubcat1 = "";
                selectguidchilcat1 = "";

                sub_category_recycler.setVisibility(View.GONE);
                chil_category_recycler.setVisibility(View.GONE);
                /// Upadte from here
                //getCityOrders();
                getSubscribeMaps();

            } else {
                // Normal category selection
                Log.d("AllCall", "visibility:inside adapter click  ");
                sub_category_recycler.setVisibility(View.VISIBLE);
                chil_category_recycler.setAdapter(null);
                chil_category_recycler.setVisibility(View.VISIBLE);
                selectguidcat1 = selectedguidcat;
                Log.d("selectguidcat1","selectguidcat1"+selectguidcat1);
                selectguidsubcat1 = "";
                selectguidchilcat1 = "";
                fetchSubinprogressCategories(selectguidcat1);

            }

        }, true);

        category_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        category_recycler.setAdapter(adapter);

    }

    private void fetchSubinprogressCategories(String categoryId) {
        Log.d("subcat", "onResponse: fetch Sub in progress " + userid + categoryId);
        ApiCall.get().Create().getguidsubcat(categoryId,userid).enqueue(new Callback<Guildline_subcat_response>() {
            @Override
            public void onResponse(Call<Guildline_subcat_response> call, Response<Guildline_subcat_response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GuidelinesSubCategory> subCategoryList = response.body().getResult();
                    List<GuidelinesSubCategory> filteredList = new ArrayList<>();
                    for (GuidelinesSubCategory sub : subCategoryList) {
                        if (categoryId.equals(sub.getCat_id())) {
                            filteredList.add(sub);
                        }
                    }

                    Log.d("subcat","insidesubcat"+response.body().toString());
                    if (filteredList.isEmpty()) {
                        /// Upadte from here
                        //getCityOrders();
                        getSubscribeMaps();
                        sub_category_recycler.setVisibility(View.GONE);
                        chil_category_recycler.setVisibility(View.GONE);
                        Toast.makeText(context, "No Filters are available for this category", Toast.LENGTH_SHORT).show();
                    } else {
                        /// Upadte from here
                        //getCityOrders();
                        getSubscribeMaps();
                        Log.d("AllCall", "onResponse: fetch Sub in progress"+categoryId);
                        sub_category_recycler.setVisibility(View.VISIBLE);
                        setupSubInporgresRecycler(filteredList);
                    }
                } else {
//                    List<TourismSubCategory> Empty = new ArrayList<>();
                    sub_category_recycler.setVisibility(View.GONE);
                    chil_category_recycler.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed to load subcategories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Guildline_subcat_response> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSubInporgresRecycler(List<GuidelinesSubCategory> subCategoryList) {
        List<String> names = new ArrayList<>();

        for (GuidelinesSubCategory sub : subCategoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(sub.getName_ar());
            } else {
                names.add(sub.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            chil_category_recycler.setVisibility(View.VISIBLE);
            chil_category_recycler.setAdapter(null);

            GuidelinesSubCategory selectedSub = subCategoryList.get(pos);
            String selectedSubId = selectedSub.getId();
            String selectedId = selectedSub.getCat_id();

            selectguidsubcat1 = selectedSubId;
            selectguidchilcat1 = "";
            Toast.makeText(context, selectedId + selectedSubId, Toast.LENGTH_SHORT).show();

            /// Upadte from here
            //getCityOrders();
            getSubscribeMaps();


            fetchChildinprogressCategories(selectedId,selectedSubId);

        }, false);

        sub_category_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        sub_category_recycler.setAdapter(adapter);
    }

    private void fetchChildinprogressCategories(String catId, String subCatId) {
        ApiCall.get().Create().getguidchild(catId,subCatId,userid).enqueue(new Callback<Guidelines_child_Response>() {
            @Override
            public void onResponse(Call<Guidelines_child_Response> call, Response<Guidelines_child_Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GuidlineChildCategory> childList = response.body().getResult();


                    setupChildInprogressRecycler(childList);
                } else {
//                    List<GuidlineChildCategory> Empty = new ArrayList<>();
//                    setupChildInprogressRecycler(Empty);
                    Toast.makeText(context, "No child categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Guidelines_child_Response> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupChildInprogressRecycler(List<GuidlineChildCategory> childList) {
        List<String> names = new ArrayList<>();
        for (GuidlineChildCategory child : childList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(child.getName_ar());
            } else {
                names.add(child.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            // Handle selection of final child category
            selectguidchilcat1 = childList.get(pos).getId();
            // Now call the main fetch method
            /// Upadte from here
            //getCityOrders();
            getSubscribeMaps();

            Toast.makeText(this, "Selected: " + item, Toast.LENGTH_SHORT).show();

            // Optionally call next API to show result based on final selected child category
        }, false);

        chil_category_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        chil_category_recycler.setAdapter(adapter);
    }


    private void getcityMaps() {

        binding.progressbar.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", String.valueOf(userid));
        if (!selectguidcat1ns.isEmpty()) {
            params.put("cat_id", selectguidcat1ns);
        }

        if (!selectguidsubcat1ns.isEmpty()) {
            params.put("sub_cat_id", selectguidsubcat1ns);
        }


        if (!selectguidchilcat1ns.isEmpty()) {
            params.put("child_cat_id", selectguidchilcat1ns);
        }

        Log.d("getcatids_ns", "selectguidcat1= " + selectguidcat1);
        Log.d("getcatids_ns", "selectguidsubcat1= " + selectguidsubcat1);
        Log.d("getcatids_ns", "selectguidchilcat1= " + selectguidchilcat1);

        mapBeanLists = new ArrayList<>();
        searchbuymessageBeanListArrayList = new ArrayList<>();
        mapBeanLists.clear();
        searchbuymessageBeanListArrayList.clear();
        //  binding.norestfound.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getGuidelinelistnew_ns(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("Restaurants  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        Log.d("getcatids_ns", "Response = " + responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            norestfound.setVisibility(View.GONE);
//                            shimmer_view_restarant.stopShimmerAnimation();
//                            shimmer_view_restarant.setVisibility(View.GONE);
                            GuidelineBean successData = new Gson().fromJson(responseData, GuidelineBean.class);
                            mapBeanLists.clear();
                            mapBeanLists.addAll(successData.getResult());
                            searchbuymessageBeanListArrayList.clear();
                            searchbuymessageBeanListArrayList.addAll(successData.getResult());
                            adapter = new AllRestaurnatAdapter1(GuidelineListActivity.this, mapBeanLists);
                            binding.providerrecyclerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            binding.providerrecyclerview.setVisibility(View.VISIBLE);
                            binding.discountfoodsrecyler.setVisibility(View.GONE);
                            binding.norestfound.setVisibility(View.GONE);

                        } else {
                            binding.providerrecyclerview.setVisibility(View.GONE);
                            binding.discountfoodsrecyler.setVisibility(View.GONE);
                            binding.norestfound.setVisibility(View.VISIBLE);
//                            shimmer_view_restarant.stopShimmerAnimation();
//                            shimmer_view_restarant.setVisibility(View.GONE);
//                            horizontalRestaurantAdapter = new HomeActivity.HorizontalRestaurantAdapter(null, HomeActivity.this, session.IsEnglish1());
//                            restaurnatlistrecyler.setAdapter(horizontalRestaurantAdapter);


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        binding.providerrecyclerview.setVisibility(View.GONE);
                        binding.norestfound.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.providerrecyclerview.setVisibility(View.GONE);
                binding.norestfound.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getSubscribeMaps() {

        binding.progressbar1.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();

        if (!selectguidcat1.isEmpty()) {
            params.put("cat_id", selectguidcat1);
        }
        if (!selectguidsubcat1.isEmpty()) {
            params.put("sub_cat_id", selectguidsubcat1);
        }
        Log.d("Useridget","userid"+userid);
        params.put("user_id", String.valueOf(userid));
        if (!selectguidchilcat1.isEmpty()) {
            params.put("child_cat_id", selectguidchilcat1);
        }
        if (!selectedCity.isEmpty() && !selectedCity.equals("Choose City") && !selectedCity.equals("اختر المدينة")){
            params.put("city_id",selectedCity);
        }


        Log.d("getcatids", "selectguidcat1= " + selectguidcat1);
        Log.d("getcatids", "selectguidsubcat1= " + selectguidsubcat1);
        Log.d("getcatids", "selectguidchilcat1= " + selectguidchilcat1);

        mapBeanLists = new ArrayList<>();
        searchbuymessageBeanListArrayList = new ArrayList<>();
        mapBeanLists.clear();
        searchbuymessageBeanListArrayList.clear();
        Log.d("subscribe","params"+params);

        ApiCall.get().Create().getGuidelinelistnew(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar1.setVisibility(View.GONE);
                Log.e("Restaurants ", " >> " + response);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.d("subscribe","data "+ object);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            GuidelineBean successData = new Gson().fromJson(responseData, GuidelineBean.class);
                            mapBeanLists.clear();
                            mapBeanLists.addAll(successData.getResult());
                            searchbuymessageBeanListArrayList.addAll(successData.getResult());
                            adapter = new AllRestaurnatAdapter1(GuidelineListActivity.this, mapBeanLists);
                            binding.providerrecyclerview1.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            binding.providerrecyclerview1.setVisibility(View.VISIBLE);

                            binding.discountfoodsrecyler1.setVisibility(View.GONE);
                            binding.norestfound1.setVisibility(View.GONE);
                        } else {
                            binding.providerrecyclerview1.setVisibility(View.GONE);
                            binding.discountfoodsrecyler1.setVisibility(View.GONE);
                            binding.norestfound1.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        binding.providerrecyclerview1.setVisibility(View.GONE);
                        binding.norestfound1.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.providerrecyclerview1.setVisibility(View.GONE);
                    binding.norestfound1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar1.setVisibility(View.GONE);
                binding.providerrecyclerview1.setVisibility(View.GONE);
                binding.norestfound1.setVisibility(View.VISIBLE);
            }
        });
    }


    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    public class AllRestaurnatAdapter1 extends RecyclerView.Adapter<AllRestaurnatAdapter1.ViewHolder> {
        Context context;
        List<GuidelineBeanlist> mapBeanLists;

        public AllRestaurnatAdapter1(Context context, List<GuidelineBeanlist> mapBeanLists) {
            this.context = context;
            this.mapBeanLists = mapBeanLists;
        }


        @NonNull
        @Override
        public AllRestaurnatAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutCustmHomeCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_home_category, parent, false);
            return new AllRestaurnatAdapter1.ViewHolder(binding);
        }

        public void filter(String charText) {
            Log.e("COMEADDD", "DDDDDDD");
            //charText = charText.toLowerCase(Locale.getDefault());
            charText = charText.toString().toLowerCase();
            mapBeanLists.clear();
            if (charText.length() == 0) {
                mapBeanLists.addAll(searchbuymessageBeanListArrayList);
            } else {
                for (GuidelineBeanlist wp : searchbuymessageBeanListArrayList) {
                    if (wp.getTitle().toLowerCase().contains(charText) || wp.getTitleAr().toLowerCase().contains(charText))//.toLowerCase(Locale.getDefault())
                    {
                        Log.e("COMEADDD", "DDD");
                        mapBeanLists.add(wp);
                    }
                }
                if (mapBeanLists == null || mapBeanLists.isEmpty()) {
                    //nodatatxt.setVisibility(View.VISIBLE);
                    // noitemtv.setText(""+getResources().getString(R.string.noitmewithsearch));
                } else {
                    // nodatatxt.setVisibility(View.GONE);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull AllRestaurnatAdapter1.ViewHolder holder, int position) {
            if (language.equalsIgnoreCase("en")) {
                holder.binding.nametv.setText("" + mapBeanLists.get(position).getTitle());
            } else {
                holder.binding.nametv.setText("" + mapBeanLists.get(position).getTitleAr());
            }
//        if(position==0){
//            holder.binding.catimage.setBackgroundResource(R.drawable.cat1);
//        }
//       else if(position==1){
//            holder.binding.catimage.setBackgroundResource(R.drawable.cat2);
//        }
//        else if(position==2){
//            holder.binding.catimage.setBackgroundResource(R.drawable.cat3);
//        }
//        else if(position==3){
//            holder.binding.catimage.setBackgroundResource(R.drawable.cat4);
//        }
//        else if(position==4){
//            holder.binding.catimage.setBackgroundResource(R.drawable.cat5);
//        }
//        else if(position==5){
//            holder.binding.catimage.setBackgroundResource(R.drawable.cat6);
//        }else{
//            holder.binding.catimage.setBackgroundResource(R.drawable.cat2);
//        }

//        holder.binding.nametv.setText(categoryBeanListArrayList.get(position).getCategoryName());
//
//
            if (mapBeanLists != null && !mapBeanLists.isEmpty()) {

                Picasso.get().load("" + mapBeanLists.get(position).getImage()).placeholder(R.color.lightgrey).into(holder.binding.catimage);

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, GuidelinesDetails.class);
                    i.putExtra("category_id", "" + mapBeanLists.get(position).getId());
                    i.putExtra("category_image", "" + mapBeanLists.get(position).getImage());
                    i.putExtra("category_name", "" + mapBeanLists.get(position).getTitle());
                    i.putExtra("category_name_ar", "" + mapBeanLists.get(position).getTitleAr());
                    i.putExtra("category_date", "" + mapBeanLists.get(position).getDateTime());
                    i.putExtra("category_details", "" + mapBeanLists.get(position).getDescription());
                    i.putExtra("category_details_ar", "" + mapBeanLists.get(position).getDescriptionAr());
                    //i.putExtra("category_id", "1");
                    context.startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            // return 6;
            return mapBeanLists == null ? 0 : mapBeanLists.size();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LayoutCustmHomeCategoryBinding binding;

            public ViewHolder(@NonNull LayoutCustmHomeCategoryBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }

        }
    }


}
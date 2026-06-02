package main.com.cineramamaps.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.CategoryAdapter;
import main.com.cineramamaps.adapter.OfferAdapter;
import main.com.cineramamaps.adapter.ServicesCityListAdapter;
import main.com.cineramamaps.adapter.ServiceCompanyListAdapter;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityAllServicesBinding;
import main.com.cineramamaps.model.Category;
import main.com.cineramamaps.model.CategoryResponse;
import main.com.cineramamaps.model.ChildCategory;
import main.com.cineramamaps.model.ChildCategoryResponse;
import main.com.cineramamaps.model.OfferBeanList_New;
import main.com.cineramamaps.model.OfferBeanNew;
import main.com.cineramamaps.model.PartnerServiceCategory;
import main.com.cineramamaps.model.PartnerServiceCategoryResponse;
import main.com.cineramamaps.model.PartnerServiceChildSubCategory;
import main.com.cineramamaps.model.PartnerServiceChildSubCategoryResponse;
import main.com.cineramamaps.model.PartnerServiceSubCategory;
import main.com.cineramamaps.model.PartnerServiceSubCategoryResponse;
import main.com.cineramamaps.model.ServiceBeanListNew;
import main.com.cineramamaps.model.ServiceBeanNew;
import main.com.cineramamaps.model.SubCategory;
import main.com.cineramamaps.model.SubCategoryResponse;
import main.com.cineramamaps.model.TourismCatResponse;
import main.com.cineramamaps.model.TourismCategory;
import main.com.cineramamaps.model.TourismChildSubCatResponse;
import main.com.cineramamaps.model.TourismChildSubCategory;
import main.com.cineramamaps.model.TourismSubCatResponse;
import main.com.cineramamaps.model.TourismSubCategory;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllServicesList extends AppCompatActivity {
    ActivityAllServicesBinding binding;
    String type = "Company";
    SessionManager session;
    Context context;

    RecyclerView categoryRecycler, subCategoryRecycler, childCategoryRecycler,
            inprogressRecycler, subinprogressRecycler, childinprogressRecycler,
            completedRecycler, subcompletedRecycler, childcompletedRecycler;
    List<OfferBeanList_New> alllistdata = new ArrayList<OfferBeanList_New>();
    List<OfferBeanList_New> filteredList = new ArrayList<>();
    private OfferAdapter offerAdapter;
    private String language = "";
    MyLanguageSession myLanguageSession;
    private String selectedCatId1 = "", selectedSubCatId1 = "", selectedChildCatId1 = "";
    private String selectedinprogeId1 = "", selectedSubinprogId1 = "", selectedChildinprogId1 = "";
    private String selectedCompleteId1 = "", selectedSubCompleteId1 = "", selectedChildCompleteId1 = "";

    List<ServiceBeanListNew> tourismoriginal = new ArrayList<>(); // keep full data
    List<ServiceBeanListNew> tourismfilteredList = new ArrayList<>();
    ServiceCompanyListAdapter Tourismadapter;

    List<ServiceBeanListNew> partnercomplete = new ArrayList<>(); // keep full data
    List<ServiceBeanListNew> partnerfilteredList = new ArrayList<>();
    ServicesCityListAdapter Partneradapter;

    int userid;

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
        //getMyOrders();
//        getCityOrders();
//        getCompanyOrders();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_services);
        context = AllServicesList.this;
        session = SessionManager.get(AllServicesList.this);
        //Work for Offer
        categoryRecycler = findViewById(R.id.categoryRecycler);
        subCategoryRecycler = findViewById(R.id.subCategoryRecycler);
        childCategoryRecycler = findViewById(R.id.childCategoryRecycler);
        //work for tourism
        inprogressRecycler = findViewById(R.id.inprogressRecycler);
        subinprogressRecycler = findViewById(R.id.subinprogressRecycler);
        childinprogressRecycler = findViewById(R.id.childinprogressRecycler);
        //Work for Partner service
        completedRecycler = findViewById(R.id.completeRecycler);
        subcompletedRecycler = findViewById(R.id.subcompleteRecycler);
        childcompletedRecycler = findViewById(R.id.childcompleteRecycler);


        setupDummyData();
        clickevent();
//        if (type.equalsIgnoreCase("Past")) {
//            binding.completedrecycler.setAdapter(new ServicesCityListAdapter(AllServicesList.this, null));
//        } else {
//            binding.inprogressrecycler.setAdapter(new ServiceCompanyListAdapter(AllServicesList.this, null));
//
//        }

        fetchCategories();
        fetchinprogressCategories();
        fetchcompleteCategories();
        setupSearch();
        setupTourismserch();
        setupPartnerserch();

        getCityOrders();
        getCompanyOrders();
        getFavfoods();
    }


    private void setupDummyData() {
        List<String> categories = Arrays.asList("");
        List<String> subCategories = Arrays.asList("");
        List<String> childCategories = Arrays.asList("");

        // Category adapter
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, (item, pos) -> {

            subCategoryRecycler.setVisibility(View.VISIBLE);
            subinprogressRecycler.setVisibility(View.VISIBLE);
            subcompletedRecycler.setVisibility(View.VISIBLE);

        });

        //work for offer
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        categoryRecycler.setAdapter(categoryAdapter);

        // work for tourism
        inprogressRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        inprogressRecycler.setAdapter(categoryAdapter);

        // work for partner service
        completedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        completedRecycler.setAdapter(categoryAdapter);

        // Subcategory adapter
        CategoryAdapter subAdapter = new CategoryAdapter(subCategories, (item, pos) -> {
            childCategoryRecycler.setVisibility(View.VISIBLE);
            childinprogressRecycler.setVisibility(View.VISIBLE);
            childcompletedRecycler.setVisibility(View.VISIBLE);

        });

        //work for offer
        subCategoryRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        subCategoryRecycler.setAdapter(subAdapter);

        // work for tourism
        subinprogressRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        subinprogressRecycler.setAdapter(subAdapter);

        // work for partner service
        subcompletedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        subcompletedRecycler.setAdapter(subAdapter);

        // Child category adapter
        CategoryAdapter childAdapter = new CategoryAdapter(childCategories, (item, pos) -> {
            Toast.makeText(this, "Selected: " + item, Toast.LENGTH_SHORT).show();

        });

        //work for offer
        childCategoryRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        childCategoryRecycler.setAdapter(childAdapter);

        // work for tourism
        childinprogressRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        childinprogressRecycler.setAdapter(childAdapter);

        // work for partner service
        childcompletedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        childcompletedRecycler.setAdapter(childAdapter);
    }

    private void fetchCategories() {

        ApiCall.get().Create().getOfferCategories(userid).enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryList = response.body().getResult();

                    Category allCategory = new Category("all", "All", "الكل", "", "", "");
                    allCategory.setId("all");
                    allCategory.setCategory_name(getString(R.string.all));
                    categoryList.add(0, allCategory);
                    setupCategoryRecycler(categoryList);
                } else {
                    List<Category> Empty = new ArrayList<>();
                    setupCategoryRecycler(Empty);
                    Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategoryRecycler(List<Category> categoryList) {
        List<String> names = new ArrayList<>();
        for (Category cat : categoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(cat.getCategory_name_ar());
            } else {
                names.add(cat.getCategory_name());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            //childCategoryRecycler.setAdapter(null);
            String selectedCategoryId = categoryList.get(pos).getId();

            if ("all".equals(selectedCategoryId)) {

                // Show all items
                selectedCatId1 = "";
                selectedSubCatId1 = "";
                selectedChildCatId1 = "";

                subCategoryRecycler.setVisibility(View.GONE);
                childCategoryRecycler.setVisibility(View.GONE);
//                fetchSubCategories("");
//                fetchChildCategories("","");
                // Call your method to show all offers/data
                getFavfoods();
            } else {
                // Normal category selection
                subCategoryRecycler.setVisibility(View.VISIBLE);
                childCategoryRecycler.setAdapter(null);
                childCategoryRecycler.setVisibility(View.VISIBLE);
                selectedCatId1 = selectedCategoryId;
                selectedSubCatId1 = ""; // Reset subcategory when changing main category
                selectedChildCatId1 = "";
                getFavfoods();
                fetchSubCategories(selectedCategoryId);

            }

        }, true);

        categoryRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        categoryRecycler.setAdapter(adapter);

    }

    private void fetchSubCategories(String categoryId) {
        ApiCall.get().Create().getOfferSubCategories(String.valueOf(userid), categoryId).enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SubCategory> subCategoryList = response.body().getResult();
                    List<SubCategory> subfilterlist = new ArrayList<>();
                    for (SubCategory sub : subCategoryList) {
                        if (categoryId.equals(sub.getCat_id())) {
                            subfilterlist.add(sub);
                        }
                    }
                    if (subfilterlist.isEmpty()) {
                        getFavfoods();
                        subCategoryRecycler.setVisibility(View.GONE);
                        childCategoryRecycler.setVisibility(View.GONE);
                        Toast.makeText(context, "No Filters are available for this category", Toast.LENGTH_SHORT).show();
                    } else {
                        getFavfoods();
                        subCategoryRecycler.setVisibility(View.VISIBLE);
                        setupSubCategoryRecycler(subfilterlist);
                    }
                } else {
                    subCategoryRecycler.setVisibility(View.GONE);
                    childCategoryRecycler.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed to load subcategories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSubCategoryRecycler(List<SubCategory> subCategoryList) {
        List<String> names = new ArrayList<>();

        for (SubCategory sub : subCategoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(sub.getName_ar());
            } else {
                names.add(sub.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            childCategoryRecycler.setVisibility(View.VISIBLE);
            childCategoryRecycler.setAdapter(null);

//            if ("all".equalsIgnoreCase(subCategoryList.get(pos).getId())) {
//                // "All" selected
////                selectedSubCatId1 = "";
////                selectedChildCatId1 = "";
//                getFavfoods();
//               // fetchChildCategories(selectedCatId1, "");
//            } else {
            // Normal subcategory selected
            SubCategory selectedSub = subCategoryList.get(pos);
            String selectedSubCatId = selectedSub.getId();
            String selectedCatId = selectedSub.getCat_id();

            selectedSubCatId1 = selectedSubCatId;
            selectedChildCatId1 = "";
            getFavfoods();
            fetchChildCategories(selectedCatId, selectedSubCatId);
            //}
        }, false);

        subCategoryRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        subCategoryRecycler.setAdapter(adapter);
    }

    private void fetchChildCategories(String catId, String subCatId) {
        ApiCall.get().Create().getOfferChildCategories(userid, catId, subCatId).enqueue(new Callback<ChildCategoryResponse>() {
            @Override
            public void onResponse(Call<ChildCategoryResponse> call, Response<ChildCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChildCategory> childList = response.body().getResult();
//                    ChildCategory allCategory = new ChildCategory("all", "All", "الكل", "", "","");
//                    allCategory.setId("all");
//                    allCategory.setName("All");
//                    childList.add(0, allCategory);
                    setupChildCategoryRecycler(childList);
                } else {
                    List<ChildCategory> Empty = new ArrayList<>();
                    setupChildCategoryRecycler(Empty);
                    Toast.makeText(context, "No child categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChildCategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupChildCategoryRecycler(List<ChildCategory> childList) {
        List<String> names = new ArrayList<>();
        for (ChildCategory child : childList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(child.getName_ar());
            } else {
                names.add(child.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            // Handle selection of final child category
            selectedChildCatId1 = childList.get(pos).getId();
            // Now call the main fetch method
            getFavfoods();

            Toast.makeText(this, "Selected: " + item, Toast.LENGTH_SHORT).show();

            // Optionally call next API to show result based on final selected child category
        }, false);

        childCategoryRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        childCategoryRecycler.setAdapter(adapter);
    }


    //Work for Tourism site
    private void fetchinprogressCategories() {

        ApiCall.get().Create().getTourismoffercat(userid).enqueue(new Callback<TourismCatResponse>() {
            @Override
            public void onResponse(Call<TourismCatResponse> call, Response<TourismCatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TourismCategory> categoryList = response.body().getResult();

                    TourismCategory allCategory = new TourismCategory("all", "All", "الكل", "");
                    allCategory.setId("all");
                    allCategory.setName_ar(getString(R.string.all));
                    categoryList.add(0, allCategory);
                    setupInprogressRecycler(categoryList);
                } else {
//                    List<TourismCategory> Empty = new ArrayList<>();
//                    setupInprogressRecycler(Empty);

                    Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TourismCatResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupInprogressRecycler(List<TourismCategory> categoryList) {
        List<String> names = new ArrayList<>();
        for (TourismCategory cat : categoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(cat.getName_ar());
            } else {
                names.add(cat.getName());
            }
        }
        Log.d("AllCall", "outside adapter click:  " + categoryList.get(0).getId());
        Log.d("AllCall", "outside adapter click sub:  " + selectedSubinprogId1);

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            //childCategoryRecycler.setAdapter(null);
            String selectedinproId = categoryList.get(pos).getId();
            Log.d("AllCall", "outside:  " + selectedinproId);

            if ("all".equals(selectedinproId)) {

                Log.d("AllCall", "inside:  " + selectedinproId);

                // Show all items
                selectedinprogeId1 = "";
                selectedSubinprogId1 = "";
                selectedChildinprogId1 = "";

                subinprogressRecycler.setVisibility(View.GONE);
                childinprogressRecycler.setVisibility(View.GONE);
                getCityOrders();

            } else {
                // Normal category selection
                Log.d("AllCall", "visibility:inside adapter click  ");
                subinprogressRecycler.setVisibility(View.VISIBLE);
                childinprogressRecycler.setAdapter(null);
                childinprogressRecycler.setVisibility(View.VISIBLE);
                selectedinprogeId1 = selectedinproId;
                selectedSubinprogId1 = "";
                selectedChildinprogId1 = "";
                fetchSubinprogressCategories(selectedinproId);

            }

        }, true);

        inprogressRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        inprogressRecycler.setAdapter(adapter);

    }

    private void fetchSubinprogressCategories(String categoryId) {
        ApiCall.get().Create().getTourismSubCategories(String.valueOf(userid), categoryId).enqueue(new Callback<TourismSubCatResponse>() {
            @Override
            public void onResponse(Call<TourismSubCatResponse> call, Response<TourismSubCatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TourismSubCategory> subCategoryList = response.body().getResult();
                    List<TourismSubCategory> filteredList = new ArrayList<>();
                    for (TourismSubCategory sub : subCategoryList) {
                        if (categoryId.equals(sub.getCatId())) {
                            filteredList.add(sub);
                        }
                    }
                    if (filteredList.isEmpty()) {
                        getCityOrders();
                        subinprogressRecycler.setVisibility(View.GONE);
                        childinprogressRecycler.setVisibility(View.GONE);
                        Toast.makeText(context, "No Filters are available for this category", Toast.LENGTH_SHORT).show();
                    } else {
                        getCityOrders();
                        Log.d("AllCall", "onResponse: fetch Sub in progress");
                        subinprogressRecycler.setVisibility(View.VISIBLE);
                        setupSubInporgresRecycler(filteredList);
                    }
                } else {
//                    List<TourismSubCategory> Empty = new ArrayList<>();
                    subinprogressRecycler.setVisibility(View.GONE);
                    childinprogressRecycler.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed to load subcategories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TourismSubCatResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSubInporgresRecycler(List<TourismSubCategory> subCategoryList) {
        List<String> names = new ArrayList<>();

        for (TourismSubCategory sub : subCategoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(sub.getNameAr());
            } else {
                names.add(sub.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            childinprogressRecycler.setVisibility(View.VISIBLE);
            childinprogressRecycler.setAdapter(null);

            TourismSubCategory selectedSub = subCategoryList.get(pos);
            String selectedSubinprogId = selectedSub.getId();
            String selectedinprogId = selectedSub.getCatId();

            selectedSubinprogId1 = selectedSubinprogId;
            selectedChildinprogId1 = "";
            Toast.makeText(context, selectedinprogId + selectedSubinprogId, Toast.LENGTH_SHORT).show();
            getCityOrders();
            fetchChildinprogressCategories(selectedinprogId,selectedSubinprogId);

        }, false);

        subinprogressRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        subinprogressRecycler.setAdapter(adapter);
    }

    private void fetchChildinprogressCategories(String catId, String subCatId) {
        ApiCall.get().Create().getTourismChildSubCategories(catId, subCatId, userid).enqueue(new Callback<TourismChildSubCatResponse>() {
            @Override
            public void onResponse(Call<TourismChildSubCatResponse> call, Response<TourismChildSubCatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TourismChildSubCategory> childList = response.body().getResult();

                    setupChildInprogressRecycler(childList);
                } else {
                    List<TourismChildSubCategory> Empty = new ArrayList<>();
                    setupChildInprogressRecycler(Empty);
                    Toast.makeText(context, "No child categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TourismChildSubCatResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupChildInprogressRecycler(List<TourismChildSubCategory> childList) {
        List<String> names = new ArrayList<>();
        for (TourismChildSubCategory child : childList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(child.getNameAr());
            } else {
                names.add(child.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            // Handle selection of final child category
            selectedChildinprogId1 = childList.get(pos).getId();
            // Now call the main fetch method
            getCityOrders();

            Toast.makeText(this, "Selected: " + item, Toast.LENGTH_SHORT).show();

            // Optionally call next API to show result based on final selected child category
        }, false);

        childinprogressRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        childinprogressRecycler.setAdapter(adapter);
    }


    //work for partner service

    private void fetchcompleteCategories() {

        ApiCall.get().Create().getPartnercat(userid).enqueue(new Callback<PartnerServiceCategoryResponse>() {
            @Override
            public void onResponse(Call<PartnerServiceCategoryResponse> call, Response<PartnerServiceCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PartnerServiceCategory> categoryList = response.body().getResult();

                    PartnerServiceCategory allCategory = new PartnerServiceCategory("all", "All", "الكل", "");
                    allCategory.setId("all");
                    allCategory.setName(getString(R.string.all));
                    categoryList.add(0, allCategory);
                    setupCompleteRecycler(categoryList);
                } else {
//                    List<PartnerServiceCategory> Empty = new ArrayList<>();
//                    setupCompleteRecycler(Empty);
                    Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PartnerServiceCategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCompleteRecycler(List<PartnerServiceCategory> categoryList) {
        List<String> names = new ArrayList<>();
        for (PartnerServiceCategory cat : categoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(cat.getNameAr());
            } else {
                names.add(cat.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            //childCategoryRecycler.setAdapter(null);
            String selectedcompleteId = categoryList.get(pos).getId();

            if ("all".equals(selectedcompleteId)) {

                // Show all items
                selectedCompleteId1 = "";
                selectedSubCompleteId1 = "";
                selectedChildCompleteId1 = "";

                subcompletedRecycler.setVisibility(View.GONE);
                childcompletedRecycler.setVisibility(View.GONE);
                getCompanyOrders();

            } else {
                // Normal category selection
                subcompletedRecycler.setVisibility(View.VISIBLE);
                childcompletedRecycler.setAdapter(null);
                childcompletedRecycler.setVisibility(View.VISIBLE);
                selectedCompleteId1 = selectedcompleteId;
                selectedSubCompleteId1 = ""; // Reset subcategory when changing main category
                selectedChildCompleteId1 = "";
                getCompanyOrders();
                fetchSubcompleteCategories(selectedCompleteId1);

            }

        }, true);

        completedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        completedRecycler.setAdapter(adapter);

    }

    private void fetchSubcompleteCategories(String categoryId) {
        ApiCall.get().Create().getPartnerSubCategories(String.valueOf(userid), categoryId).enqueue(new Callback<PartnerServiceSubCategoryResponse>() {
            @Override
            public void onResponse(Call<PartnerServiceSubCategoryResponse> call, Response<PartnerServiceSubCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PartnerServiceSubCategory> subCategoryList = response.body().getResult();
                    List<PartnerServiceSubCategory> partnerfilterlist = new ArrayList<>();
                    for (PartnerServiceSubCategory sub : subCategoryList) {
                        if (categoryId.equals(sub.getCatId())) {
                            partnerfilterlist.add(sub);
                        }
                    }
                    if (partnerfilterlist.isEmpty()) {
                        subcompletedRecycler.setVisibility(View.GONE);
                        childcompletedRecycler.setVisibility(View.GONE);
                        Toast.makeText(context, "No Filters are available for this category", Toast.LENGTH_SHORT).show();
                    } else {
                        subcompletedRecycler.setVisibility(View.VISIBLE);
                        setupSubCompleteRecycler(partnerfilterlist);
                    }

                } else {

                    subcompletedRecycler.setVisibility(View.GONE);
                    childcompletedRecycler.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed to load subcategories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PartnerServiceSubCategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSubCompleteRecycler(List<PartnerServiceSubCategory> subCategoryList) {
        List<String> names = new ArrayList<>();

        for (PartnerServiceSubCategory sub : subCategoryList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(sub.getNameAr());
            } else {
                names.add(sub.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            childcompletedRecycler.setVisibility(View.VISIBLE);
            childcompletedRecycler.setAdapter(null);


            PartnerServiceSubCategory selectedSub = subCategoryList.get(pos);
            String selectedSubCatId = selectedSub.getId();
            String selectedCatId = selectedSub.getCatId();

            selectedSubCompleteId1 = selectedSubCatId;
            selectedChildCompleteId1 = "";
//            getFavfoods();
            getCompanyOrders();
            fetchChildcompleteCategories(selectedCatId, selectedSubCatId);

        }, false);

        subcompletedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        subcompletedRecycler.setAdapter(adapter);
    }

    private void fetchChildcompleteCategories(String catId, String subCatId) {
        ApiCall.get().Create().getPartnerChildSubCategories(catId, subCatId, userid).enqueue(new Callback<PartnerServiceChildSubCategoryResponse>() {
            @Override
            public void onResponse(Call<PartnerServiceChildSubCategoryResponse> call, Response<PartnerServiceChildSubCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PartnerServiceChildSubCategory> childList = response.body().getResult();

                    setupChildCompleteRecycler(childList);
                } else {
//                    List<PartnerServiceChildSubCategory> Empty = new ArrayList<>();
//                    setupChildCompleteRecycler(Empty);
                    Toast.makeText(context, "No child categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PartnerServiceChildSubCategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupChildCompleteRecycler(List<PartnerServiceChildSubCategory> childList) {
        List<String> names = new ArrayList<>();
        for (PartnerServiceChildSubCategory child : childList) {
            if (language.equalsIgnoreCase("ar")) {
                names.add(child.getNameAr());
            } else {
                names.add(child.getName());
            }
        }

        CategoryAdapter adapter = new CategoryAdapter(names, (item, pos) -> {
            // Handle selection of final child category
            selectedChildCompleteId1 = childList.get(pos).getId();
            // Now call the main fetch method
            getCompanyOrders();

            Toast.makeText(this, "Selected: " + item, Toast.LENGTH_SHORT).show();

            // Optionally call next API to show result based on final selected child category
        }, false);

        childcompletedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        childcompletedRecycler.setAdapter(adapter);
    }


    private void setupSearch() {

        EditText searchEditText = findViewById(R.id.search_e2);
        ImageView searchIcon = findViewById(R.id.dddddd12);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void setupTourismserch() {
        EditText searchEditText = findViewById(R.id.search_et);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tourismfilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void setupPartnerserch() {
        EditText searchEditText = findViewById(R.id.search_et1);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                partnerfilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void tourismfilter(String text) {
        tourismfilteredList.clear();
        for (ServiceBeanListNew item : tourismoriginal) {
            String nameToCheck = language.equals("ar") ? item.getCompanyNameAr() : item.getCompanyName();
            if (nameToCheck != null && nameToCheck.toLowerCase().contains(text.toLowerCase())) {
                tourismfilteredList.add(item);
            }
        }
        Tourismadapter.notifyDataSetChanged();

        if (tourismfilteredList.isEmpty()) {
            binding.emptylay.setVisibility(View.VISIBLE);
        } else {
            binding.emptylay.setVisibility(View.GONE);
        }
    }

    private void partnerfilter(String text) {
        partnerfilteredList.clear();
        for (ServiceBeanListNew item : partnercomplete) {
            String nameToCheck = language.equals("ar") ? item.getCompanyNameAr() : item.getCompanyName();
            if (nameToCheck != null && nameToCheck.toLowerCase().contains(text.toLowerCase())) {
                partnerfilteredList.add(item);
            }
        }
        Partneradapter.notifyDataSetChanged();

        if (partnerfilteredList.isEmpty()) {
            binding.emptylay.setVisibility(View.VISIBLE);
        } else {
            binding.emptylay.setVisibility(View.GONE);
        }
    }

    private void filter(String text) {
        filteredList.clear();

        if (text.isEmpty()) {
            filteredList.addAll(alllistdata); // Show all when search is empty
        } else {
            String searchText = text.toLowerCase().trim();
            for (OfferBeanList_New item : alllistdata) {
                // Adjust this condition based on your data model
                if (item.getCompanyName().toLowerCase().contains(searchText) ||
                        item.getDescription().toLowerCase().contains(searchText)) {
                    filteredList.add(item);
                }
            }
        }

        // Update the adapter
        if (offerAdapter != null) {
            offerAdapter.updateList(filteredList);
        }
    }

    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.inprogresstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.inprogresslay.setVisibility(View.VISIBLE);
                binding.completedlay.setVisibility(View.GONE);
                binding.offerlay.setVisibility(View.GONE);

                binding.inprogresstv.setBackgroundResource(R.drawable.button_round_dra);
                binding.completedtv.setBackgroundResource(R.color.grey_back_col);
                binding.offertv.setBackgroundResource(R.color.grey_back_col);

                binding.inprogresstv.setTextColor(getResources().getColor(R.color.white));
                binding.completedtv.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.offertv.setTextColor(getResources().getColor(R.color.colorPrimary));
                //inprogressRecycler.setVisibility(View.VISIBLE);

                type = "Company";
            //    getCityOrders();
                if (tourismfilteredList!=null && tourismfilteredList.size()>0) {

                    binding.emptylay.setVisibility(View.GONE);
                    Tourismadapter = new ServiceCompanyListAdapter(AllServicesList.this, tourismfilteredList, language);
                    binding.inprogressrecycler.setAdapter(Tourismadapter);
                } else {
                    binding.emptylay.setVisibility(View.VISIBLE);
                    binding.inprogressrecycler.setAdapter(new ServiceCompanyListAdapter(AllServicesList.this, null, language));
                }

//                getMyOrders();
//                if (type.equalsIgnoreCase("Past")) {
//                    binding.completedrecycler.setAdapter(new ServicesCityListAdapter(AllServicesList.this, null));
//                } else {
//                    binding.inprogressrecycler.setAdapter(new ServiceCompanyListAdapter(AllServicesList.this, null));
//
//                }
            }
        });
        binding.completedtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.inprogresslay.setVisibility(View.GONE);
                binding.completedlay.setVisibility(View.VISIBLE);
                binding.offerlay.setVisibility(View.GONE);

                binding.inprogresstv.setBackgroundResource(R.color.grey_back_col);
                binding.offertv.setBackgroundResource(R.color.grey_back_col);
                binding.completedtv.setBackgroundResource(R.drawable.button_round_dra);

                binding.inprogresstv.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.offertv.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.completedtv.setTextColor(getResources().getColor(R.color.white));
                // inprogressRecycler.setVisibility(View.GONE);
                type = "City";
                // getCompanyOrders();
                if (partnerfilteredList!=null && partnerfilteredList.size()>0) {

                    binding.emptylay.setVisibility(View.GONE);
                    Partneradapter = new ServicesCityListAdapter(AllServicesList.this, partnerfilteredList, language);
                    binding.completedrecycler.setAdapter(Partneradapter);
                } else {
                    binding.emptylay.setVisibility(View.VISIBLE);
                    binding.completedrecycler.setAdapter(new ServicesCityListAdapter(AllServicesList.this, null, language));
                }

                // getMyOrders();
//                if (type.equalsIgnoreCase("Past")) {
//                    binding.completedrecycler.setAdapter(new ServicesCityListAdapter(AllServicesList.this, null));
//                } else {
//                    binding.inprogressrecycler.setAdapter(new ServiceCompanyListAdapter(AllServicesList.this, null));
//
//                }
            }
        });

        binding.offertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.inprogresslay.setVisibility(View.GONE);
                binding.completedlay.setVisibility(View.GONE);
                binding.offerlay.setVisibility(View.VISIBLE);

                binding.inprogresstv.setBackgroundResource(R.color.grey_back_col);
                binding.completedtv.setBackgroundResource(R.color.grey_back_col);
                binding.offertv.setBackgroundResource(R.drawable.button_round_dra);

                binding.inprogresstv.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.completedtv.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.offertv.setTextColor(getResources().getColor(R.color.white));
                binding.emptylay.setVisibility(View.GONE);
                //inprogressRecycler.setVisibility(View.GONE);
                type = "Offer";
              //  getFavfoods();
                if (filteredList!=null && filteredList.size()>0) {

                    if (offerAdapter == null) {
                        offerAdapter = new OfferAdapter(AllServicesList.this, filteredList,language);
                        binding.emptylay.setVisibility(View.GONE);
                        binding.offerRv.setLayoutManager(new LinearLayoutManager(AllServicesList.this));
                        binding.offerRv.setAdapter(offerAdapter);
                    } else {
                        offerAdapter.updateList(filteredList);
                    }
                } else {
                    // Handle empty list
                    binding.emptylay.setVisibility(View.VISIBLE);
                    binding.offerRv.setAdapter(new OfferAdapter(AllServicesList.this,null,language));

                }

                // getMyOrders();
//                if (type.equalsIgnoreCase("Past")) {
//                    binding.completedrecycler.setAdapter(new ServicesCityListAdapter(AllServicesList.this, null));
//                } else {
//                    binding.inprogressrecycler.setAdapter(new ServiceCompanyListAdapter(AllServicesList.this, null));
//
//                }
            }
        });
    }


    //work for tourism service
    private void getCityOrders() {
        Log.d("Comes", "getCityOrders===============.....>>>>>");
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.shimmerViewMyorder.setVisibility(View.VISIBLE);
        binding.shimmerViewMyorder.startShimmerAnimation();
        Log.d("Comes", "getCityOrders===============.....>>>>>" + session.getUserID());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", session.getUserID());
        //params.put("type", "Item");

        if (!selectedinprogeId1.isEmpty()) {
            params.put("cat_id", selectedinprogeId1);
        }

        if (!selectedSubinprogId1.isEmpty()) {
            params.put("sub_cat_id", selectedSubinprogId1);
        }

        if (!selectedChildinprogId1.isEmpty()) {
            params.put("child_cat_id", selectedChildinprogId1);
        }

        Log.d("Come from ", "Vlue new ayan" + selectedinprogeId1 + selectedSubinprogId1);


        ApiCall.get().Create().getTourismServices(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("CityOrders", " >> " + response);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("CityOrders", " >> " + responseData);
                        JSONObject object = new JSONObject(responseData);

                        binding.shimmerViewMyorder.setVisibility(View.GONE);
                        binding.shimmerViewMyorder.stopShimmerAnimation();

                        if (object.getString("status").equalsIgnoreCase("1")) {
                            ServiceBeanNew successData = new Gson().fromJson(responseData, ServiceBeanNew.class);
                            tourismoriginal = successData.getResult(); // save full list
                            tourismfilteredList = new ArrayList<>(tourismoriginal); // init
                            binding.emptylay.setVisibility(View.GONE);
                            Tourismadapter = new ServiceCompanyListAdapter(AllServicesList.this, tourismfilteredList, language);
                            binding.inprogressrecycler.setAdapter(Tourismadapter);
                        } else {
                            binding.emptylay.setVisibility(View.VISIBLE);
                            binding.inprogressrecycler.setAdapter(new ServiceCompanyListAdapter(AllServicesList.this, null, language));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }

    //work for partner service
    private void getCompanyOrders() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.shimmerViewMyorder.setVisibility(View.VISIBLE);
        binding.shimmerViewMyorder.startShimmerAnimation();

        Log.d("cometome", "id1= " + selectedCompleteId1);
        Log.d("cometome", "id2= " + selectedSubCompleteId1);
        Log.d("cometome", "id3= " + selectedChildCompleteId1);

        HashMap<String, String> params = new HashMap<>();

        if (!selectedCompleteId1.isEmpty()) {
            params.put("cat_id", selectedCompleteId1);
        }

        if (!selectedSubCompleteId1.isEmpty()) {
            params.put("sub_cat_id", selectedSubCompleteId1);
        }
        params.put("user_id", session.getUserID());
        if (!selectedChildCompleteId1.isEmpty()) {
            params.put("child_sub_cat_id", selectedChildCompleteId1);
        }


//Log.d("come in Company", "Company " + selectedCompleteId1 + selectedSubCompleteId1 + selectedChildCompleteId1);

        ApiCall.get().Create().getPartnerServices(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("cometome", " >> " + response);

                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.d("cometome", "company" + object);
                        binding.shimmerViewMyorder.setVisibility(View.GONE);
                        binding.shimmerViewMyorder.stopShimmerAnimation();

                        if (object.getString("status").equalsIgnoreCase("1")) {
                            ServiceBeanNew successData = new Gson().fromJson(responseData, ServiceBeanNew.class);
                            partnercomplete = successData.getResult();
                            partnerfilteredList = new ArrayList<>(partnercomplete);
                            binding.emptylay.setVisibility(View.GONE);
                            Partneradapter = new ServicesCityListAdapter(AllServicesList.this, partnerfilteredList, language);
                            binding.completedrecycler.setAdapter(Partneradapter);
                        } else {
                            binding.emptylay.setVisibility(View.VISIBLE);
                            binding.completedrecycler.setAdapter(new ServicesCityListAdapter(AllServicesList.this, null, language));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }


//    private void getMyOrders() {
//        binding.progressbar.setVisibility(View.VISIBLE);
//        binding.shimmerViewMyorder.setVisibility(View.VISIBLE);
//        binding.shimmerViewMyorder.startShimmerAnimation();
//        ApiCall.get().Create().getPartnerServices(getParam()).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                binding.progressbar.setVisibility(View.GONE);
//                Log.e("MyOrders  ", " >> " + response);
//                if (response.isSuccessful()) {
//                    try {
//                        String responseData = response.body().string();
//                        JSONObject object = new JSONObject(responseData);
//
//                        binding.shimmerViewMyorder.setVisibility(View.GONE);
//                        binding.shimmerViewMyorder.stopShimmerAnimation();
//                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            ServiceBeanNew successData = new Gson().fromJson(responseData, ServiceBeanNew.class);
//                            binding.emptylay.setVisibility(View.GONE);
//                            if (type.equalsIgnoreCase("City")) {
//                                binding.completedrecycler.setAdapter(new ServicesCityListAdapter(AllServicesList.this, successData.getResult()));
//                            } else {
//                                binding.inprogressrecycler.setAdapter(new ServiceCompanyListAdapter(AllServicesList.this, successData.getResult()));
//
//                            }
//                        } else {
//                            binding.emptylay.setVisibility(View.VISIBLE);
//                            if (type.equalsIgnoreCase("City")) {
//                                binding.completedrecycler.setAdapter(new ServicesCityListAdapter(AllServicesList.this, null));
//                            } else {
//                                binding.inprogressrecycler.setAdapter(new ServiceCompanyListAdapter(AllServicesList.this, null));
//
//                            }
//
//                        }
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                binding.progressbar.setVisibility(View.GONE);
//            }
//        });
//    }


    private void getFavfoods() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.emptylay.setVisibility(View.GONE);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", session.getUserID());
        params.put("type", "Item");

        if (!selectedCatId1.isEmpty()) {
            params.put("cat_id", selectedCatId1);
        }

        if (!selectedSubCatId1.isEmpty()) {
            params.put("sub_cat_id", selectedSubCatId1);
        }

        if (!selectedChildCatId1.isEmpty()) {
            params.put("child_cat_id", selectedChildCatId1);
        }

        ApiCall.get().Create().getcompanyOffernew(params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseData = response.body().string();
                        Log.d("getFavfoods", "Response: " + responseData);

                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equalsIgnoreCase("1")) {
                            OfferBeanNew successData = new Gson().fromJson(responseData, OfferBeanNew.class);

                            if (successData != null && successData.getResult() != null && !successData.getResult().isEmpty()) {
                                alllistdata = successData.getResult();
                                filteredList = new ArrayList<>(alllistdata);// Set your global list
                                if (offerAdapter == null) {
                                    offerAdapter = new OfferAdapter(AllServicesList.this, filteredList,language);
                                    binding.emptylay.setVisibility(View.GONE);
                                    binding.offerRv.setLayoutManager(new LinearLayoutManager(AllServicesList.this));
                                    binding.offerRv.setAdapter(offerAdapter);
                                } else {
                                    offerAdapter.updateList(filteredList);
                                }
                            } else {
                                // Handle empty list
                                binding.emptylay.setVisibility(View.VISIBLE);
                                binding.offerRv.setAdapter(new OfferAdapter(AllServicesList.this,null,language));

                            }
                        } else {
                            filteredList.clear();
                            alllistdata.clear();

                            binding.emptylay.setVisibility(View.VISIBLE);

                            if (offerAdapter == null) {
                                offerAdapter = new OfferAdapter(AllServicesList.this, new ArrayList<>(),language);
                                binding.offerRv.setLayoutManager(new LinearLayoutManager(AllServicesList.this));
                                binding.offerRv.setAdapter(offerAdapter);
                            } else {
                                offerAdapter.updateList(new ArrayList<>()); // clear adapter list
                            }
                        }
                    } catch (Exception e) {
                        Log.e("getFavfoods", "Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    Log.e("getFavfoods", "Response unsuccessful or empty body");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("getFavfoods", "API Failure: " + t.getMessage());
            }
        });
    }

    private HashMap<String, String> getProductParam(String type) {


        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        //  param.put("token",session.getUserDetails().getToken());
        param.put("city_id", "");
        param.put("country_id", "");
        return param;

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
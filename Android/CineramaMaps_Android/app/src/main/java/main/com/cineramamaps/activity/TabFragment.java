package main.com.cineramamaps.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.model.OfferBeanList_New;
import main.com.cineramamaps.model.OfferBeanNew;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabFragment extends Fragment {
    SessionManager session;
    List<OfferBeanList_New> alllistdata =  new ArrayList<OfferBeanList_New>();
    private String language = "";
    MyLanguageSession myLanguageSession;
    private View view;
    private RecyclerView rv_product;
    List<OfferBeanList_New> menulist = new ArrayList<>();
    String itemid="0",status="No",cart_cat_id="";
    String item_id="",item_price="",cartcount="",catid="",subcatid="",childsubcatid="",suppliesstatus="No";
    boolean btnclick = false;
    ProgressBar progress_bar;
    //TabLayout tabLayout;
    ViewPager viewPager;
    ProgressBar progressbar;
    RelativeLayout botumlay;

    Button continuebtn;
    EditText search_et;
    List<OfferBeanList_New> searchbuymessageBeanListArrayList = new ArrayList<>();
    TextView cart_item_txt,totalservicetxt ;
    String image ="";
    Context context;

    public TabFragment(List<OfferBeanList_New> menulist, List<OfferBeanList_New> searchbuymessageBeanListArrayList, String image, String language,Context context) {
        this.context = context;
        this.menulist = menulist;
        this.searchbuymessageBeanListArrayList = searchbuymessageBeanListArrayList;
        this.image = image;
        this.language = language;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab, container, false);
        myLanguageSession = new MyLanguageSession(getActivity());
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());

        session = SessionManager.get(getActivity());
//        cart_item_txt = getActivity().findViewById(R.id.cart_item_txt);
//        ImageView imagee = view.findViewById(R.id.imagee);
//        if (!image.equalsIgnoreCase("")) {
//            Picasso.with(getActivity()).load(image).placeholder(R.color.lightgrey).into(imagee);
//        }
        findId();
//

        getFavfoods();



//        adapter = new ProductLisFragment.RecyclerItemAdapter1(getActivity(), menulist, 0);
//        rv_product.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        return view;
    }
    private void findId() {
        //tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        progressbar = view.findViewById(R.id.progressbar);
    }

    private void getFavfoods() {


        ApiCall.get().Create().getcompanyOffernew(getProductParam("Item")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressbar.setVisibility(View.GONE);
                Log.e("GetProducts  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);

                       System.out.println("sssssssssssssssssssss = Tab1");
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            OfferBeanNew successData = new Gson().fromJson(responseData, OfferBeanNew.class);
                            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
                            System.out.println("sssssss22 "+alllistdata.size());
                            alllistdata = successData.getResult();
                            for(int j=0;j<4;j++) {
                                // for(int j=0;j<5;j++) {
                                if (language.equalsIgnoreCase("en")){
                                    System.out.println("sssssssssssssssssssss = Tab01");
                                    adapter.addFragment(new TabFragment1(alllistdata, alllistdata, alllistdata.get(0).getImage(), language,context), "Sub Category"+(j+1));
                                }else {
                                    adapter.addFragment(new TabFragment1(alllistdata, alllistdata, alllistdata.get(0).getImage(), language,context), "Sub Category Ar"+(j+1));
                                }
                                // adapter.addFragment(new ProductLisFragment(null,"aa"), "Restaurants and Cafes");
//                                viewPager.setAdapter(adapter);

                            }
                            viewPager.setAdapter(adapter);

                            //tabLayout.setupWithViewPager(viewPager);


                        } else {
//                                adapter1 = new AllRestaurnatAdapter(OfferActivity_New.this, null);
//                                binding.discountfoodsrecyler.setAdapter(adapter1);


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
        param.put("city_id","");
        param.put("country_id","");
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

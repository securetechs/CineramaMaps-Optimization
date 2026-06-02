package main.com.cineramamaps.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.adapter.ReveiwListAdapter;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.constant.ExpandableHeightListView;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityServiceDetailBinding;
import main.com.cineramamaps.draweractivity.BaseActivity;
import main.com.cineramamaps.model.Bannerdata;
import main.com.cineramamaps.model.ProductAdditional;
import main.com.cineramamaps.model.RatingBean;
import main.com.cineramamaps.model.RatingReview;
import main.com.cineramamaps.model.SingleProductDetail;
import main.com.cineramamaps.model.SingleServiceBean;
import main.com.cineramamaps.model.SingleServiceDetails;
import main.com.cineramamaps.restapi.ApiCall;
import main.com.cineramamaps.tabactivity.MainTabActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailActivity extends AppCompatActivity {
    ActivityServiceDetailBinding binding;
    SessionManager session;
    ExtraItemAdapter extraItemAdapter;
    int item_count_val = 0;
    ReveiwListAdapter adapter1;
    private int dotscount;
    private ImageView[] dots;
    float finalAmount;
    private ArrayList<ProductAdditional> productAdditionalArrayList;
    String id, itemprice_main = "", like_status = "", itemprice_total, cat_id = "", cat_name = "", extraitem_total_according_quantity_str = "", extra_item_name_str = "", extra_item_qty_str = "", extra_item_id = "", extra_item_price_str = "";
   int remainingitemquantity=0;
    SingleServiceBean successData;
    List<RatingReview> reviewlist = new ArrayList<>();
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
        getItemDetail();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.get().updateResources(this);
        myLanguageSession = new MyLanguageSession(this);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_detail);
        session = SessionManager.get(ServiceDetailActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.isEmpty()) {
            id = bundle.getString("id");
        }
        productAdditionalArrayList = new ArrayList<>();
        bindview();
        binding.reviewexplist.setExpanded(true);
        clickevent();



//        binding.reviewexplist.setAdapter(new ReveiwListAdapter(ServiceDetailActivity.this, null));

    }

    private void bindview() {
        binding.extraitemslist.setExpanded(true);


    }

    private void itemAddedCartSucc() {
//        final Dialog dialogSts = new Dialog(ServiceDetailActivity.this);
//        dialogSts.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogSts.setCancelable(false);
//        dialogSts.setContentView(R.layout.popup_item_added_cart);
//        dialogSts.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        TextView dismiss = dialogSts.findViewById(R.id.dismiss);
//        TextView gotocart = dialogSts.findViewById(R.id.gotocart);
//
//
//        dismiss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogSts.dismiss();
//                finish();
//
//            }
//        });
//        gotocart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(ServiceDetailActivity.this, MainTabActivity.class);
//                i.putExtra("WhichIndex", 2);
//                startActivity(i);
//                finish();
//
//
//            }
//        });
//        dialogSts.show();
    }


    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.favic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTofavCall();
            }
        });
        binding.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.isUserLogin()) {

                    item_count_val = Integer.parseInt(binding.counttv.getText().toString());
                    binding.counttv.setText("" + item_count_val);

                    finalAmount = Float.parseFloat(itemprice_main) * item_count_val;
                    itemprice_total = "" + finalAmount;
                    float extraitem_price = 0;
                    if (productAdditionalArrayList != null && !productAdditionalArrayList.isEmpty()) {

                        for (int k = 0; k < productAdditionalArrayList.size(); k++) {
                            float extra_price = 0;
                            if (productAdditionalArrayList.get(k).isSelected()) {
                                if (productAdditionalArrayList.get(k).getItemPrice() != null && !productAdditionalArrayList.get(k).getItemPrice().equalsIgnoreCase("")) {
                                    extra_price = Float.parseFloat(productAdditionalArrayList.get(k).getItemPrice());

                                    extraitem_price = extraitem_price + extra_price;
                                }
                            }
                        }
                    }

                    float tt = Float.parseFloat(itemprice_total);
                    extraitem_price = extraitem_price * item_count_val;
                    float tot_val = extraitem_price + tt;
                    itemprice_total = "" + tot_val;
                    finalAmount = tot_val;
                    String price = itemprice_total;

                    Log.e("INSIDEIF PRC tt ", " >> " + tt);
                    Log.e("INSIDEIF PRC tot_val  ", " >> " + tot_val);
                    Log.e("INSIDEIF extraite ", " >> " + extraitem_price);
                    if (price != null && !price.equalsIgnoreCase("")) {
                        double tot = Double.parseDouble(price);
                        binding.bottomFinalPriceTv.setText(getResources().getString(R.string.currency) + "" + String.format("%.2f", tot) + " for " + item_count_val + " " + getResources().getString(R.string.items));
                    }
                    binding.btompricelay.setVisibility(View.VISIBLE);







                    item_count_val = Integer.parseInt(binding.counttv.getText().toString());
                    if (item_count_val > 0) {
                      if(item_count_val>remainingitemquantity){
//                          Toast.makeText(ServiceDetailActivity.this,""+getResources().getString(R.string.sufficientitemnot),Toast.LENGTH_LONG).show();
                      }else {
                          StringBuilder extraitem_total_according_quantity = new StringBuilder();
                          StringBuilder extra_item_qty = new StringBuilder();
                          StringBuilder extra_price_value = new StringBuilder();
                          StringBuilder extra_id_value = new StringBuilder();
                          StringBuilder extra_name_value = new StringBuilder();

                          //New code
                          for (ProductAdditional ii : productAdditionalArrayList) {
                              if (ii.isSelected()) {
                                  extra_name_value.append(ii.getItemName() + ",");
                                  extra_id_value.append(ii.getId() + ",");
                                  extra_price_value.append(ii.getItemPrice() + ",");
                                  extra_item_qty.append(ii.getQty() + ",");
                                  //extra_item_qty.append(item_count_val+",");


                              }
                          }


                          Log.e("extra_item_qty >> ", " >> " + extra_item_qty);
                          if (extra_id_value != null && !extra_id_value.toString().equalsIgnoreCase("") && !productAdditionalArrayList.isEmpty() && productAdditionalArrayList != null) {
                              extra_item_id = extra_id_value.toString().substring(0, extra_id_value.length() - 1);
                              extra_item_name_str = extra_name_value.toString();
                              extra_item_price_str = extra_price_value.toString();
                              extra_item_qty_str = extra_item_qty.toString();
                              extraitem_total_according_quantity_str = extraitem_total_according_quantity.toString();

                          }
                          Log.e("extra_item_id >> ", " >> " + extra_item_id);
                          Log.e("extra_item_name_str >> ", " >> " + extra_item_name_str);
                          Log.e("extr_item_price_str >> ", " >> " + extra_item_price_str);
                          Log.e("extra_item_qty_str >> ", " >> " + extra_item_qty_str);
                          //addToCart();
                      }


                        // Toast.makeText(AddItemActivity.this,"Item added successfully",Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(ServiceDetailActivity.this, "Please add atleast one item", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ServiceDetailActivity.this, getResources().getString(R.string.needtologinfirst), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ServiceDetailActivity.this, LoginAct.class);
                    startActivity(i);
                }
            }
        });
        binding.pluslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_count_val = Integer.parseInt(binding.counttv.getText().toString());
                item_count_val = item_count_val + 1;
                binding.counttv.setText("" + item_count_val);

                finalAmount = Float.parseFloat(itemprice_main) * item_count_val;
                itemprice_total = "" + finalAmount;
                float extraitem_price = 0;
                if (productAdditionalArrayList != null && !productAdditionalArrayList.isEmpty()) {

                    for (int k = 0; k < productAdditionalArrayList.size(); k++) {
                        float extra_price = 0;
                        if (productAdditionalArrayList.get(k).isSelected()) {
                            if (productAdditionalArrayList.get(k).getItemPrice() != null && !productAdditionalArrayList.get(k).getItemPrice().equalsIgnoreCase("")) {
                                extra_price = Float.parseFloat(productAdditionalArrayList.get(k).getItemPrice());

                                extraitem_price = extraitem_price + extra_price;
                            }
                        }
                    }
                }

                float tt = Float.parseFloat(itemprice_total);
                extraitem_price = extraitem_price * item_count_val;
                float tot_val = extraitem_price + tt;
                itemprice_total = "" + tot_val;
                finalAmount = tot_val;
                String price = itemprice_total;

                Log.e("INSIDEIF PRC tt ", " >> " + tt);
                Log.e("INSIDEIF PRC tot_val  ", " >> " + tot_val);
                Log.e("INSIDEIF extraite ", " >> " + extraitem_price);
                if (price != null && !price.equalsIgnoreCase("")) {
                    double tot = Double.parseDouble(price);
                    binding.bottomFinalPriceTv.setText(getResources().getString(R.string.currency) + "" + String.format("%.2f", tot) + " for " + item_count_val + " " + getResources().getString(R.string.items));
                }
                binding.btompricelay.setVisibility(View.VISIBLE);

            }
        });


        binding.minuslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_count_val = Integer.parseInt(binding.counttv.getText().toString());
                if (item_count_val > 1) {

                    item_count_val = item_count_val - 1;
                    Log.e("TOPCOUNT ", " >> " + item_count_val);
                    binding.counttv.setText("" + item_count_val);
                    itemprice_total = "" + Float.parseFloat(itemprice_main) * item_count_val;
                    finalAmount = Float.parseFloat(itemprice_main) * item_count_val;
                    Log.e("FINALAMT TOP ", " >> " + finalAmount);
                    Log.e("ITEMPRCSTR TOP ", " >> " + itemprice_total);

                    float extraitem_price = 0;
                    if (productAdditionalArrayList != null && !productAdditionalArrayList.isEmpty()) {

                        for (int k = 0; k < productAdditionalArrayList.size(); k++) {
                            float extra_price = 0;
                            if (productAdditionalArrayList.get(k).isSelected()) {
                                if (productAdditionalArrayList.get(k).getItemPrice() != null && !productAdditionalArrayList.get(k).getItemPrice().equalsIgnoreCase("")) {
                                    extra_price = Float.parseFloat(productAdditionalArrayList.get(k).getItemPrice());
                                    extraitem_price = extraitem_price + extra_price;
                                }
                            }
                        }
                    }

                    float tt = Float.parseFloat(itemprice_total);
                    extraitem_price = extraitem_price * item_count_val;
                    float tot_val = extraitem_price + tt;
                    itemprice_total = "" + tot_val;
                    finalAmount = tot_val;
                    String price = itemprice_total;

                    Log.e("INSIDEIF PRC tt ", " >> " + tt);
                    Log.e("INSIDEIF PRC tot_val  ", " >> " + tot_val);
                    Log.e("INSIDEIF extraite ", " >> " + extraitem_price);
                    if (price != null && !price.equalsIgnoreCase("")) {
                        double tot = Double.parseDouble(price);
                        binding.bottomFinalPriceTv.setText(getResources().getString(R.string.currency) + "" + String.format("%.2f", tot) + " for " + item_count_val + " " + getResources().getString(R.string.items));
                    }
                    binding.btompricelay.setVisibility(View.VISIBLE);


                }
            }
        });

        binding.seeallreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ServiceDetailActivity.this, GiveRatingActivity.class);
                i.putExtra("provider_id",id);
                i.putExtra("order_id",id);
                i.putExtra("reviewtype","Service");
                startActivity(i);
            }
        });

    }


    private void getItemDetail() {
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getItemDetail(session.getUserID(), id, BaseActivity.lat,BaseActivity.lon).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("ItemDetails  ", " >> " + response);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            successData = new Gson().fromJson(responseData, SingleServiceBean.class);
                            reviewlist.clear();
                            reviewlist = successData.getResult().getRatingReview();
                            adapter1 = new ReveiwListAdapter(ServiceDetailActivity.this, reviewlist);
                            binding.reviewexplist.setAdapter(adapter1);

                            ArrayList<Bannerdata> bannerlist = new ArrayList<>();
                           bannerlist.clear();
                           if(!successData.getResult().getImage1().equalsIgnoreCase("") && !successData.getResult().getImage1().equalsIgnoreCase(""+ BaseUrl.image_baseurl) && !successData.getResult().getImage1().equalsIgnoreCase("https://www.ci-maps.com/CineramaMaps/uploads/images/")) {
                               Bannerdata b = new Bannerdata();
                               b.setBannername(successData.getResult().getImage1());
                               bannerlist.add(b);
                           }
                            if(!successData.getResult().getImage2().equalsIgnoreCase("") && !successData.getResult().getImage2().equalsIgnoreCase(""+ BaseUrl.image_baseurl) && !successData.getResult().getImage2().equalsIgnoreCase("https://www.ci-maps.com/CineramaMaps/uploads/images/")) {

                                Bannerdata b2 = new Bannerdata();
                                b2.setBannername(successData.getResult().getImage2());
                                bannerlist.add(b2);
                            }
                            if(!successData.getResult().getImage3().equalsIgnoreCase("") && !successData.getResult().getImage3().equalsIgnoreCase(""+ BaseUrl.image_baseurl) && !successData.getResult().getImage3().equalsIgnoreCase("https://www.ci-maps.com/CineramaMaps/uploads/images/")) {

                                Bannerdata b3 = new Bannerdata();
                                b3.setBannername(successData.getResult().getImage3());
                                bannerlist.add(b3);
                            }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(ServiceDetailActivity.this, bannerlist);
        binding.viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(ServiceDetailActivity.this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(ServiceDetailActivity.this, R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            binding.sliderDots.addView(dots[i], params);

            dots[0].setImageDrawable(ContextCompat.getDrawable(ServiceDetailActivity.this, R.drawable.active));

            binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//if(position== dots.length-1){
//    viewPager.setCurrentItem(0);
//}
                }

                @Override
                public void onPageSelected(int position) {

                    for (int i = 0; i < dotscount; i++) {
                        try {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(ServiceDetailActivity.this, R.drawable.non_active_dot));
                        } catch (Exception e) {
//                                e.printStackTrace();
                        }
                    }
                    try {
                        dots[position].setImageDrawable(ContextCompat.getDrawable(ServiceDetailActivity.this, R.drawable.active));
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

//                                Timer timer = new Timer();
//                                timer.scheduleAtFixedRate(new SliderTimer_one(), 3000, 3000);
                            //                            like_status = successData.getResult().getLikeStatus();
//                            if (like_status.equalsIgnoreCase("Yes")) {
//                                binding.favic.setImageResource(R.drawable.fav_selected);
//                            } else {
//                                binding.favic.setImageResource(R.drawable.fav);
//                            }
//                            if (successData.getResult().getType().equalsIgnoreCase("Magic Food")) {
////                                binding.pricetv.setText(getResources().getString(R.string.currency) + "" + successData.getResult().getBagSizePrice());
////                                itemprice_main = successData.getResult().getBagSizePrice();
//                                binding.pricetv.setText(getResources().getString(R.string.currency) + "" + successData.getResult().getOfferItemPrice());
//                                itemprice_main = successData.getResult().getOfferItemPrice();
//                                finalAmount = Float.valueOf(itemprice_main);
//                            } else {
//                                binding.pricetv.setText(getResources().getString(R.string.currency) + "" + successData.getResult().getOfferItemPrice());
//                                itemprice_main = successData.getResult().getOfferItemPrice();
//                                finalAmount = Float.valueOf(itemprice_main);
//                            }
                          //  getReview();

//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                binding.descriptiontv.setText(Html.fromHtml(""+successData.getResult().getDescription(), Html.FROM_HTML_MODE_COMPACT));
//                            } else {
//                                binding.descriptiontv.setText(Html.fromHtml(""+successData.getResult().getDescription()));
//                            }
                            String htmlData = "";
                            if(language.equalsIgnoreCase("en")) {

                                 htmlData = "" + successData.getResult().getDescription();
                                binding.nametv.setText(successData.getResult().getCompanyName());
                            }else{
                                htmlData = "" + successData.getResult().getDescriptionAr();
                                binding.nametv.setText(successData.getResult().getCompanyNameAr());
                            }

// WebView basic setup
                            binding.descriptiontv.getSettings().setJavaScriptEnabled(false); // enable only if needed
                            binding.descriptiontv.getSettings().setDomStorageEnabled(true);
                            binding.descriptiontv.setWebViewClient(new WebViewClient());

// Optional styling wrapper
                            String finalHtml = "<html><body style='padding:8px; font-size:16px; color:#000000;'>"
                                    + htmlData +
                                    "</body></html>";

                            binding.descriptiontv.loadDataWithBaseURL(
                                    null,
                                    finalHtml,
                                    "text/html",
                                    "UTF-8",
                                    null
                            );
                          //  binding.descriptiontv.setText(successData.getResult().getDescription());
                            binding.contactv.setText(successData.getResult().getMobile());
                            binding.locationtv.setText(successData.getResult().getAddress());
//                            binding.restlocationtv.setText(successData.getResult().getRestDetails().getProviderStreatAddress());
//                            binding.descriptiontv.setText(successData.getResult().getItemDescription());
//                            cat_id = successData.getResult().getCatId();
//                            cat_name = successData.getResult().getCatName();
//                            //holder.binding.bagtype.setText(context.getResources().getString(R.string.bagtype)+" : "+itemBeanLists.get(position).getBagSizeName());
//                            binding.quantitylefttv.setText(successData.getResult().getItemQuantity() + " " + getResources().getString(R.string.left));
//                            if(!successData.getResult().getItemQuantity().equalsIgnoreCase("")) {
//                                 remainingitemquantity = Integer.parseInt(successData.getResult().getItemQuantity());
//                            }
//                            binding.providernameTv.setText(successData.getResult().getRestDetails().getProviderName());
//                            if (successData.getResult().getProductImages() != null && !successData.getResult().getProductImages().isEmpty()) {
//                                Picasso.get().load(successData.getResult().getProductImages().get(0).getImage()).placeholder(R.color.lightgrey).into(binding.foodimage);
//                            }
//
//                            productAdditionalArrayList = new ArrayList<>();
//                            productAdditionalArrayList.addAll(successData.getResult().getProductAdditional());
//                            extraItemAdapter = new ExtraItemAdapter(ServiceDetailActivity.this, productAdditionalArrayList);
//                            binding.extraitemslist.setAdapter(extraItemAdapter);
//                            extraItemAdapter.notifyDataSetChanged();
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

    public class ExtraItemAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        private ArrayList<ProductAdditional> productAdditionalArrayList;

        public ExtraItemAdapter(Context applicationContext, ArrayList<ProductAdditional> productAdditionalArrayList) {
            this.context = applicationContext;
            this.productAdditionalArrayList = productAdditionalArrayList;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return productAdditionalArrayList == null ? 0 : productAdditionalArrayList.size();
            // return count_list1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.layout_custm_extra_item, null);
            TextView extraitemname1 = view.findViewById(R.id.extraitemname1);
            TextView extpricetv = view.findViewById(R.id.extpricetv);
            CheckBox extcheckbox = view.findViewById(R.id.extcheckbox);
            extpricetv.setText(getResources().getString(R.string.currency) + "" + productAdditionalArrayList.get(i).getItemPrice());
            extraitemname1.setText(productAdditionalArrayList.get(i).getItemName());


            extcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        productAdditionalArrayList.get(i).setSelected(true);
                        getAmount(productAdditionalArrayList.get(i), "plus");
                    } else {
                        productAdditionalArrayList.get(i).setSelected(false);
                        getAmount(productAdditionalArrayList.get(i), "minus");
                    }
                }
            });
            return view;
        }
    }


    private float getAmount(ProductAdditional additional, String minus_plus) {
        int price = 0;
        try {
            price = Integer.parseInt(additional.getItemPrice().trim());
            Log.e("GetAmountMainItemCount ", " >> " + item_count_val);
            Log.e("GetAmountMainprice ", " >> " + price);

            price = price * item_count_val;
            Log.e("ygg ", " >> " + price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (additional.isSelected()) {
            finalAmount += price;

        } else {
            finalAmount -= price;

        }
        itemprice_total = String.valueOf(finalAmount);
        String price_st = String.valueOf(finalAmount);
        if (price_st != null && !price_st.equalsIgnoreCase("")) {
            double tot = Double.parseDouble(price_st);
            binding.bottomFinalPriceTv.setText(getResources().getString(R.string.currency) + "" + String.format("%.2f", tot) + " for " + item_count_val + " " + getResources().getString(R.string.items));
            binding.btompricelay.setVisibility(View.VISIBLE);
        }

        return finalAmount;

    }


//    private HashMap<String, String> getParam() {
//
//
//        HashMap<String, String> param = new HashMap<>();
//        param.put("user_id", session.getUserID());
//        param.put("product_id", successData.getResult().getId());
//        param.put("cat_id", successData.getResult().getCatId());
//        param.put("cat_name", "" + successData.getResult().getCatName());
//        param.put("product_name", "" + successData.getResult().getItemName());
//        param.put("product_price", "" + itemprice_main);
//        param.put("provider_id", "" + successData.getResult().getProviderId());
//        param.put("total_amount", "" + finalAmount);
//        param.put("before_discount_amount", "" + finalAmount);
//        param.put("extra_item_id", "" + extra_item_id);
//        param.put("extra_item_name", "" + extra_item_name_str);
//        param.put("extra_item_price", "" + extra_item_price_str);
//        param.put("extra_item_qty", "" + extra_item_qty_str);
//        param.put("total_extra_item_price", "" + extraitem_total_according_quantity_str);
//        param.put("quantity", "" + item_count_val);
//        return param;
//
//    }

    private HashMap<String, String> getFavParam() {


        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("product_id", successData.getResult().getId());

        return param;

    }

//    private void addToCart() {
//
//
//        binding.progressbar.setVisibility(View.VISIBLE);
//        ApiCall.get().Create().addToCart(getParam()).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                binding.progressbar.setVisibility(View.GONE);
//                Log.e("AddToCart ", " >> " + response);
//
//                if (response.isSuccessful()) {
//
//                    try {
//                        String responseData = response.body().string();
//                        JSONObject object = new JSONObject(responseData);
//                        Log.e("AddToCart Response ", " >> " + responseData);
//                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            // getCartData();
//                            //Toast.makeText(ServiceDetailActivity.this, getResources().getString(R.string.itemaddedtocart), Toast.LENGTH_SHORT).show();
//                            itemAddedCartSucc();
//
//                        } else {
//                            Toast.makeText(ServiceDetailActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    Toast.makeText(ServiceDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                binding.progressbar.setVisibility(View.GONE);
//                Toast.makeText(ServiceDetailActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void addTofavCall() {


        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().addTofav(getFavParam()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("AddToCart ", " >> " + response);

                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("AddToFav Response ", " >> " + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {

                            getItemDetail();
                        } else {
                            Toast.makeText(ServiceDetailActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(ServiceDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(ServiceDetailActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCartData() {
        binding.progressbar.setVisibility(View.VISIBLE);
        ApiCall.get().Create().getMyCart(session.getUserID()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("MyCartResponse >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                         /*   CartBean successData = new Gson().fromJson(responseData, CartBean.class);
                            MainActivity.cartcount = successData.getTotal_cart();
                            MainActivity.cart_total_amt = successData.getTotalAmount();
                            if (MainActivity.cart_total_amt!=null&&!MainActivity.cart_total_amt.equalsIgnoreCase("")) {
                                double tot = Double.parseDouble(MainActivity.cart_total_amt);
                                MainActivity.cart_total_amt = String.format("%.2f", tot);
                            }
*/
                            finish();


                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                binding.progressbar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }

    private void getReview() {
        binding.progressbar.setVisibility(View.VISIBLE);

        ApiCall.get().Create().getProviderRating(successData.getResult().getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e("AllRatings  ", " >> " + response);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
//                            RatingReview successData = new Gson().fromJson(responseData, RatingReview.class);
//                            binding.reviewexplist.setExpanded(true);
//                            binding.reviewexplist.setAdapter(new ReveiwListAdapter(ServiceDetailActivity.this, successData.));
                        } else {
//                            binding.reviewexplist.setExpanded(true);
//                            binding.reviewexplist.setAdapter(new ReveiwListAdapter(ServiceDetailActivity.this, null));
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
  public   class ViewPagerAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Bannerdata> images;

        public ViewPagerAdapter(Context context, List<Bannerdata> images) {
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            // return 4;
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_slide_item, null);
            final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress);
            //progress.setVisibility(View.VISIBLE);

            Picasso.get().load(images.get(position).getBannername()).placeholder(R.color.lightgrey).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                        Intent ii = new Intent(context, ItemImageBigActivity.class);
//                        ii.putExtra("image",""+images.get(position).getBannername());
//                        String im = images.get(position).getBannername().replace(BaseUrl.image_baseurl,"");
//                        ii.putExtra("name",""+im);
//                        context.startActivity(ii);
                }
            });
//if(position == 0){
            // imageView.setImageResource(R.drawable.service1);
//}
//        if(position == 1){
//            imageView.setImageResource(R.drawable.service2);
//        }
//        if(position == 2){
//            imageView.setImageResource(R.drawable.service3);
//        }
//        if(position == 3){
//            imageView.setImageResource(R.drawable.service4);
//        }


            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }
    }
}
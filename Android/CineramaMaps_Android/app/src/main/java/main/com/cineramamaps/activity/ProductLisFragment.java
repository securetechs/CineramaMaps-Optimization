package main.com.cineramamaps.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import main.com.cineramamaps.R;
import main.com.cineramamaps.constant.BaseUrl;
import main.com.cineramamaps.model.MapBeanList;
import main.com.cineramamaps.model.Menudata;
import main.com.cineramamaps.model.OfferBeanList_New;

public class ProductLisFragment extends Fragment {

    private View view;
    private RecyclerView rv_product;
    List<OfferBeanList_New> menulist = new ArrayList<>();
    String itemid="0",status="No",cart_cat_id="";
    String item_id="",item_price="",cartcount="",catid="",subcatid="",childsubcatid="",suppliesstatus="No";
    boolean btnclick = false;
    ProgressBar progress_bar;
    RelativeLayout botumlay;
    String language="en";
    Button continuebtn;
    EditText search_et;
    List<OfferBeanList_New> searchbuymessageBeanListArrayList = new ArrayList<>();
    TextView cart_item_txt,totalservicetxt ;
    String image ="";
    RecyclerItemAdapter1 adapter;
    public ProductLisFragment(List<OfferBeanList_New> menulist,List<OfferBeanList_New> searchbuymessageBeanListArrayList, String image,String language) {
        this.menulist = menulist;
        this.searchbuymessageBeanListArrayList = searchbuymessageBeanListArrayList;
        this.image = image;
        this.language = language;

        Log.d("Parameter","Ayan"+menulist);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_lis_list, container, false);
//        cart_item_txt = getActivity().findViewById(R.id.cart_item_txt);
//        ImageView imagee = view.findViewById(R.id.imagee);
//        if (!image.equalsIgnoreCase("")) {
//            Picasso.with(getActivity()).load(image).placeholder(R.color.lightgrey).into(imagee);
//        }
        findId();
//        if (Utils.isConnected(getActivity())) {
//            Myprofilejsontask1 task1 = new Myprofilejsontask1();
//            task1.execute();
//
//        } else {
//            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
//
//        }

//        if (Utils.isConnected(getActivity())) {
//            Myprofilejsontask task = new Myprofilejsontask();
//            task.execute();
//
//        } else {
//            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
//
//        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        rv_product.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
//        ProductListAdapter adapters = new ProductListAdapter(getActivity());
//        rv_product.setAdapter(adapters);
//        search_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                if (s==null){
//
//                }
//                else {
//
//
//                    if (s.toString().length() > 0) {
//
//                        if (adapter == null) {
//
//                        } else {
//                            Log.e("COME "," >> "+s.toString());
//                            adapter.filter(s.toString());
//                        }
//
//                    } else {
//
//                        if (adapter == null) {
//
//                        } else {
//                            adapter.filter("");
//                        }
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

        System.out.println("sssssssssssssssssssss = Tab3");
        adapter = new RecyclerItemAdapter1(getActivity(), menulist, 0);
        rv_product.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }
    public class Myprofilejsontask1 extends AsyncTask<String, Void, String> {
        boolean iserror = false;
        String R_latitude = "";
        String R_longitude = "";
        String result = "";
        String msg = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //    progress_bar.setVisibility(View.VISIBLE);

            //  System.out.println("ssssssssssss " + edt_emailvalue + " " + edt_passvalue);

        }


        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();


            // Set verifier
            HttpGet post = null;
//            if(AppsConstant.usertype.equalsIgnoreCase("guest")){
//                post = new HttpGet(BaseUrl.baseurl +"cart-counter.php?user_id="+ AppsConstant.userid);
//            }else{
//                post = new HttpGet(BaseUrl.baseurl + "delete_user_cart?user_id="+ AppsConstant.userid);
//            }
//
//            System.out.println("url = = = = "+BaseUrl.baseurl + "delete_user_cart?user_id="+AppsConstant.userid);
            try {



                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                System.out.println("#####object=" + object);
                //JSONArray js = new JSONArray(object);


                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("status");
                if (result.equalsIgnoreCase("1")) {
//                    String details = jobect.getString("result");
//                    JSONObject oo = new JSONObject(details);
//                    cartcount = oo.getString("total_cart");
//                    cart_cat_id = oo.getString("cart_cat_id");
                    // AppsConstant.username = oo.getString("user_name");
                    // AppsConstant.facebookprofileimage = oo.getString("image");
//                    total_amount = oo.getString("total_amount");
//                    total_amount = total_amount.replace("AED","MYR");
//                    MainActivity.cartrestoid = oo.getString("outlet_id");



                } else {
                    //msg = jobect.getString("message");

                }
            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            // progress_bar.setVisibility(View.GONE);
            if (iserror == false) {
                //if (Utils.isConnected(getActivity())) {
                Myprofilejsontask task = new Myprofilejsontask();
                task.execute();

            } else {
                Toast.makeText(getActivity(), "Please check server connection",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void findId() {
        rv_product = view.findViewById(R.id.rv_product);
        search_et = view.findViewById(R.id.search_et);
        progress_bar = view.findViewById(R.id.progress_bar);
        //continuebtn = getActivity().findViewById(R.id.book_service_btn);
        botumlay = getActivity().findViewById(R.id.botumlay);
      //  totalservicetxt = getActivity().findViewById(R.id.totalamount1);
    }


    public class RecyclerItemAdapter1 extends RecyclerView.Adapter<RecyclerItemAdapter1.ViewHolder> {

        private List<OfferBeanList_New> friends;
        private Activity activity;
        private int pos;

        public RecyclerItemAdapter1(Activity activity, List<OfferBeanList_New> friends, int pos) {
            this.friends = friends;
            this.activity = activity;
            this.pos = pos;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            //inflate your layout and pass it to view holder
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.offerlistitemlay, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        public void filter(String charText) {
            Log.e("COMEADDD","DDDDDDD");
            //charText = charText.toLowerCase(Locale.getDefault());
            charText = charText.toString().toLowerCase();
            menulist.clear();
            if (charText.length() == 0) {
                menulist.addAll(searchbuymessageBeanListArrayList);
                System.out.println("sssssssssssssssss 11"+menulist.size());
                System.out.println("sssssssssssssssss 22"+searchbuymessageBeanListArrayList.size());
            } else {
                for (OfferBeanList_New wp : searchbuymessageBeanListArrayList) {
                    if (wp.getCompanyName().toLowerCase().contains(charText) || wp.getCompanyNameAr().toLowerCase().contains(charText)|| wp.getDiscountCode().toLowerCase().contains(charText))//.toLowerCase(Locale.getDefault())
                    {
                        Log.e("COMEADDD","DDD");
                        menulist.add(wp);
                    }
                }
                if (menulist==null||menulist.isEmpty()){
                    //nodatatxt.setVisibility(View.VISIBLE);
                    // noitemtv.setText(""+getResources().getString(R.string.noitmewithsearch));
                }
                else {
                    // nodatatxt.setVisibility(View.GONE);
                }
            }
            notifyDataSetChanged();
        }



        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

            // viewHolder.item.setText(menulist.get(position));

//            viewHolder.txt_name.setText(menulist.get(position).getMenuname());
//            viewHolder.txt_qnty.setText(HtmlCompat.fromHtml(menulist.get(position).getMenudescription(), 0));
//            //    viewHolder.txt_term.setText(getResources().getString(R.string.term)+"*: "+menulist.get(position).getTerms());
//            viewHolder.txt_term.setVisibility(View.GONE);
//            // viewHolder.txt_term.setContent("Terms: "+menulist.get(position).getTerms());
////            viewHolder.txt_term.setTextMaxLength(200);
////            viewHolder.txt_term.toggle();
////            viewHolder.txt_term.expandText(true|false);
////            viewHolder.txt_term.setSeeMoreTextColor(R.color.black) ;//default is #3F51B5
////            viewHolder.txt_term.setSeeMoreText("ShowMore","ShowLess");
//            viewHolder.pricetxt.setText(""+getResources().getString(R.string.currency)+"" + menulist.get(position).getMenuprice());
//            if(menulist.get(position).getCatid().equalsIgnoreCase("3") && menulist.get(position).getSubcatid().equalsIgnoreCase("3")) {
//                viewHolder.pricetxt1.setText(""+getResources().getString(R.string.currency)+"" + menulist.get(position).getPrice_with_supplies() + " ("+getResources().getString(R.string.withsupplier)+")");
//                viewHolder.pricetxt1.setVisibility(View.VISIBLE);
//                viewHolder.likelay1.setVisibility(View.VISIBLE);
//            }else{
//                viewHolder.pricetxt1.setVisibility(View.GONE);
//                viewHolder.likelay1.setVisibility(View.GONE);
//            }
//
//            viewHolder.salepricetxt.setPaintFlags(viewHolder.pricetxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            viewHolder.salepricetxt.setText(""+getResources().getString(R.string.currency)+"" + menulist.get(position).getSale_price());
        viewHolder.submit.setText(menulist.get(position).getDiscountCode()+" "+menulist.get(position).getDiscountPercentage()+""+""+getResources().getString(R.string.percentdiscount));
           // viewHolder.datetv.setText(""+menulist.get(position).getDescription());
           if(language.equalsIgnoreCase("en")) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                   viewHolder.datetv.setText(Html.fromHtml("" + menulist.get(position).getCompanyName(), Html.FROM_HTML_MODE_COMPACT));
               } else {
                   viewHolder.datetv.setText(Html.fromHtml("" + menulist.get(position).getCompanyName()));
               }
           }else{
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                   viewHolder.datetv.setText(Html.fromHtml(""+menulist.get(position).getCompanyNameAr(), Html.FROM_HTML_MODE_COMPACT));
               } else {
                   viewHolder.datetv.setText(Html.fromHtml(""+menulist.get(position).getCompanyNameAr()));
               }
           }
            if (!menulist.get(position).getImage().equalsIgnoreCase("")) {
                Picasso.get().load(menulist.get(position).getImage()).into(viewHolder.offeric);
            }

            viewHolder.mainlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ii = new Intent(getActivity(), OfferDetailsActivity.class);
                    ii.putExtra("code",""+menulist.get(position).getDiscountCode());
                    ii.putExtra("percent",""+menulist.get(position).getDiscountPercentage());
                    ii.putExtra("title",""+menulist.get(position).getCompanyName());
                    ii.putExtra("title_ar",""+menulist.get(position).getCompanyNameAr());
                    ii.putExtra("description",""+menulist.get(position).getDescription());
                    ii.putExtra("description_ar",""+menulist.get(position).getDescriptionAr());
                    ii.putExtra("image",""+menulist.get(position).getImage());
                    startActivity(ii);
                }
            });


        }

        @Override
        public int getItemCount() {

           return (null != menulist ? menulist.size() : 0);
           //  return  10;
        }
        public  class ViewHolder extends RecyclerView.ViewHolder {
            private TextView datetv, submit,txt_name,txt_duration,pricetxt,pricetxt1,txt_count,customizetxt,salepricetxt,txt_qnty;
            ImageView offeric;
         //   SeeMoreTextView txt_term;
            ImageView favouritests,likeimg;
            RelativeLayout ly1,minuslay,pluslay,likelay1,mainlay;
            LinearLayout addminusbtnlay;
            Button btn_add;
            CheckBox checkb;
            public ViewHolder(View view) {
                super(view);

                mainlay =  view.findViewById(R.id.mainlay);
                offeric =  view.findViewById(R.id.offeric);
                datetv =  view.findViewById(R.id.datetv);
                submit =  view.findViewById(R.id.submit);
//                txt_name = (TextView) view.findViewById(R.id.txt_name);
//                txt_qnty1 = (TextView) view.findViewById(R.id.txt_qnty1);
//                txt_term = (SeeMoreTextView) view.findViewById(R.id.txt_term);
//                txt_term.setVisibility(View.VISIBLE);
//                pricetxt = (TextView) view.findViewById(R.id.txt_price);
//                pricetxt1 = (TextView) view.findViewById(R.id.txt_price1);
//                salepricetxt = (TextView) view.findViewById(R.id.txt_mrpprice);
//                txt_qnty = (TextView) view.findViewById(R.id.txt_qnty);
//                img_service = (ImageView) view.findViewById(R.id.pro_image);
//                likeimg = (ImageView) view.findViewById(R.id.likeimg1);
//                likelay1 = (RelativeLayout) view.findViewById(R.id.likelay1);
//                checkb = (CheckBox) view.findViewById(R.id.checkb);
//
            }
        }
    }


    public class AddToCartApi extends AsyncTask<String, Void, String> {
        boolean iserror = false;
        String R_latitude = "";
        String R_longitude = "";
        String result = "";
        String msg = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
            btnclick = true;
        }


        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
//            System.out.println("sssssssssss1 = "+AppsConstant.userid);
//            System.out.println("sssssssssss2 = "+AppsConstant.userid);
//            System.out.println("sssssssssss3 = "+AppsConstant.referralcode);
//            System.out.println("sssssssssss4 = "+item_id);
//            System.out.println("sssssssssss5 = 1");
//            System.out.println("sssssssssss6 = "+item_price);
//            System.out.println("sssssssssss6 = "+"http://cobeecafe.com/webservice/add_to_cart_product?user_id="+AppsConstant.userid+"&product_id="+item_id+"&total_amount="+item_price+"&quantity=1");
            HttpPost post = new HttpPost(BaseUrl.baseurl + "add_to_cart_service?");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        10);

//                nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
//                nameValuePairs.add(new BasicNameValuePair("service_id", item_id));
//                nameValuePairs.add(new BasicNameValuePair("total_amount", ""+item_price));
//                nameValuePairs.add(new BasicNameValuePair("cat_id", ""+catid));
//                nameValuePairs.add(new BasicNameValuePair("sub_cat_id", ""+subcatid));
//                nameValuePairs.add(new BasicNameValuePair("child_cat_id", ""+childsubcatid));
//                nameValuePairs.add(new BasicNameValuePair("quantity", "1"));
//                nameValuePairs.add(new BasicNameValuePair("price_with_supplies",status ));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                System.out.println("#####object=" + object);
                //JSONArray js = new JSONArray(object);


                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("status");
                if (result.equalsIgnoreCase("1")) {

                } else {
                    msg = jobect.getString("message");

                }
            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            progress_bar.setVisibility(View.GONE);
            btnclick = false;
            if (iserror == false) {
                if (result.equalsIgnoreCase("1")) {
//                    Toast.makeText(getActivity(), ""+getResources().getString(R.string.itemadded),
//                            Toast.LENGTH_SHORT).show();
//                    if (Utils.isConnected(getActivity())) {
//                        Myprofilejsontask task = new Myprofilejsontask();
//                        task.execute();
//
//                    } else {
//                        Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
//
//                    }


                } else {

                    Toast.makeText(getActivity(), ""+msg,
                            Toast.LENGTH_SHORT).show();



                }
            } else {
                Toast.makeText(getActivity(), "Please check server connection",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

    public class Myprofilejsontask extends AsyncTask<String, Void, String> {
        boolean iserror = false;
        String R_latitude = "";
        String R_longitude = "";
        String result = "";
        String msg = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //    progress_bar.setVisibility(View.VISIBLE);

            //  System.out.println("ssssssssssss " + edt_emailvalue + " " + edt_passvalue);

        }


        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();


            // Set verifier
            HttpGet post = null;
//            if(AppsConstant.usertype.equalsIgnoreCase("guest")){
//                post = new HttpGet(BaseUrl.baseurl +"cart-counter.php?user_id="+ AppsConstant.userid);
//            }else{
//                post = new HttpGet(BaseUrl.baseurl + "get_profile?user_id="+ AppsConstant.userid);
//            }
//
//            System.out.println("url = = = = "+BaseUrl.baseurl + "get_profile?user_id="+AppsConstant.userid);
            try {



//                MultipartEntity reqEntityss = new MultipartEntity(
//                        HttpMultipartMode.BROWSER_COMPATIBLE);
//                reqEntityss.addPart("email", new StringBody(edt_emailvalue));
//                reqEntityss.addPart("password", new StringBody(edt_passvalue));



                //  reqEntityss.addPart("register_id", new StringBody(regId));

                //reqEntityss.addPart("device_id", new StringBody(usernamevalue));

//                if (bab != null) {
//                    reqEntityss.addPart("image", bab);
//
//
//                } else {
//                    reqEntityss.addPart("image", new StringBody(""));
//                }
//                JSONObject oo = new JSONObject();
//                oo.put("email",edt_emailvalue);
//                oo.put("pass",edt_passvalue);
//                StringEntity se = new StringEntity( oo.toString());
//                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//                post.setEntity(se);


                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                System.out.println("#####object=" + object);
                //JSONArray js = new JSONArray(object);


                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("status");
                if (result.equalsIgnoreCase("1")) {
                    String details = jobect.getString("result");
                    JSONObject oo = new JSONObject(details);
                    cartcount = oo.getString("total_cart");
                    cart_cat_id = oo.getString("cart_cat_id");
                    // AppsConstant.username = oo.getString("user_name");
                    // AppsConstant.facebookprofileimage = oo.getString("image");
//                    total_amount = oo.getString("total_amount");
//                    total_amount = total_amount.replace("AED","MYR");
//                    MainActivity.cartrestoid = oo.getString("outlet_id");



                } else {
                    msg = jobect.getString("message");

                }
            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            // progress_bar.setVisibility(View.GONE);
            if (iserror == false) {
                if (result.equalsIgnoreCase("1")) {
                    if(!cartcount.equalsIgnoreCase("") && cartcount !=null && !cartcount.equalsIgnoreCase("null") && !cartcount.equalsIgnoreCase("0")) {
                        cart_item_txt.setText(cartcount);
                        cart_item_txt.setVisibility(View.GONE);
                        botumlay.setVisibility(View.VISIBLE);
                        totalservicetxt.setText("Services("+cartcount+")");
//                        nametxt1.setText(AppsConstant.username);
//                        try {
//                            if(!AppsConstant.facebookprofileimage.contains("user-icon.png") && !AppsConstant.facebookprofileimage.equalsIgnoreCase("")) {
//                                Glide.with(getActivity()).load(AppsConstant.facebookprofileimage).error(R.drawable.user111).into(sliderimg);
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
                        //  blay.setVisibility(View.VISIBLE);
                        //  blay1.setVisibility(View.VISIBLE);
//
//                        newcounttxt.setText(cartcount+" item in cart");
//                        newtotalamounttxt.setText("Cart "+total_amount);
//                        newcounttxt1.setText(cartcount+" item in cart");
//                        newtotalamounttxt1.setText("Cart "+total_amount);
                    }else{
                        cart_item_txt.setText("0");
                        cart_item_txt.setVisibility(View.GONE);
                        botumlay.setVisibility(View.GONE);
//                        blay.setVisibility(View.GONE);
//                        blay1.setVisibility(View.GONE);
                    }

                } else {



                }
            } else {
                Toast.makeText(getActivity(), "Please check server connection",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }
}
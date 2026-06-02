package main.com.cineramamaps.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.ActivityGiveRatingBinding;
import main.com.cineramamaps.databinding.ActivityOfferBinding;
import main.com.cineramamaps.model.Offerdata;
import main.com.cineramamaps.restapi.ApiCall;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferActivity extends AppCompatActivity {
    ActivityOfferBinding binding;
    SessionManager session;
    private String order_id = "", provider_id = "",reviewtype="";
    private ArrayList<Offerdata> itemBeanListArrayList;
    private RecyclerItemAdapter adapter;
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
        session = SessionManager.get(OfferActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offer);



        clickevent();
        getOffersList();
    }

    private void clickevent() {
        binding.exitAppBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getOffersList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        itemBeanListArrayList = new ArrayList<>();
        itemBeanListArrayList.clear();
        Call<ResponseBody> call = ApiCall.get().Create().getCoupons1(session.getUserID());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        Log.e("OffersData >", " >" + responseData);
                        if (object.getString("status").equalsIgnoreCase("1")) {
                            String result = object.getString("result");
                            JSONArray oo = new JSONArray(result);
                            for(int j=0;j<oo.length();j++){
                                String id = oo.getJSONObject(j).getString("id");
                                String code = oo.getJSONObject(j).getString("code");
                                String percentage = oo.getJSONObject(j).getString("percentage");
                                String description = oo.getJSONObject(j).getString("description");
                                String description_ar = oo.getJSONObject(j).getString("description_ar");
                                String image = oo.getJSONObject(j).getString("image");
                                String link = "22";//oo.getJSONObject(j).getString("link");
                                String enddate = oo.getJSONObject(j).getString("end_date");
                                String date = oo.getJSONObject(j).getString("date_time");

                                Offerdata obj = new Offerdata();
                                obj.setId(id);
                                obj.setCode(code);
                                obj.setPercentage(percentage);
                                obj.setImage(image);
                                obj.setLink(link);
                                obj.setEnddate(enddate);
                                obj.setDate(date);
                                obj.setDescription(description);
                                obj.setDescription_ar(description_ar);
                                itemBeanListArrayList.add(obj);

                            }



                            LinearLayoutManager layoutManager = new LinearLayoutManager(OfferActivity.this);
                            binding.menuItemList.setLayoutManager(layoutManager);
                            adapter = new RecyclerItemAdapter(OfferActivity.this, itemBeanListArrayList);
                            binding.menuItemList.setLayoutManager(new GridLayoutManager(OfferActivity.this, 1));
                            binding.menuItemList.setAdapter(adapter);
                            // adapter.notifyDataSetChanged();

                            binding.nodatatxt.setVisibility(View.GONE);
                        }
                        else {
                            binding.nodatatxt.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                binding.progressBar.setVisibility(View.GONE);
                Log.e("TAG", t.toString());
            }
        });
    }

    class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.ViewHolder> {

        private List<Offerdata> itemBeanListArrayList;
        private Activity activity;


        public RecyclerItemAdapter(Activity activity, ArrayList<Offerdata> itemBeanListArrayList) {
            this.itemBeanListArrayList = itemBeanListArrayList;
            this.activity = activity;

        }

        @Override
        public RecyclerItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            //inflate your layout and pass it to view holder
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.offerlistitem1, viewGroup, false);
            RecyclerItemAdapter.ViewHolder viewHolder = new RecyclerItemAdapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerItemAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.code_tv.setText("Code: "+itemBeanListArrayList.get(position).getCode());
            viewHolder.code_percent.setText(itemBeanListArrayList.get(position).getPercentage()+"% Discount");
            if(language.equalsIgnoreCase("en")) {
                viewHolder.code_description.setText(itemBeanListArrayList.get(position).getDescription());
            }else {
                viewHolder.code_description.setText(itemBeanListArrayList.get(position).getDescription_ar());
            }
            Picasso.get().load(itemBeanListArrayList.get(position).getImage()).into(viewHolder.img_service);
//            viewHolder.img_service.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(itemBeanListArrayList.get(position).getLink()));
//                        startActivity(i);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                        Toast.makeText(OffersActivity.this,"Link not valid for offer.",Toast.LENGTH_LONG).show();
//
//                    }
//                }
//            });
//
//            if (position==0){
//                viewHolder.img_service.setImageResource(R.drawable.offer3);
//            }if (position==1){
//                viewHolder.img_service.setImageResource(R.drawable.offer1);
//            }
//            if (position==2){
//                viewHolder.img_service.setImageResource(R.drawable.offer4);
//            }if (position==3){
//                viewHolder.img_service.setImageResource(R.drawable.offer2);
//            }

            // viewHolder.item.setText(friends.get(position));


           /* viewHolder.item_name_tv.setText(itemBeanListArrayList.get(position).getName());
            if (itemBeanListArrayList.get(position).getProductSizePrice()!=null&&!itemBeanListArrayList.get(position).getProductSizePrice().isEmpty()){
                viewHolder.price_tv.setText(getResources().getString(R.string.nis)+" "+itemBeanListArrayList.get(position).getProductSizePrice().get(0).getPrice());
                viewHolder.item_des_tv.setText(""+itemBeanListArrayList.get(position).getProductSizePrice().get(0).getCalories());

            }
            else {
                viewHolder.price_tv.setText(getResources().getString(R.string.nis)+" "+itemBeanListArrayList.get(position).getPrice());
                viewHolder.item_des_tv.setText(itemBeanListArrayList.get(position).getDescription());
            }
            Picasso.get().load(itemBeanListArrayList.get(position).getImage1()).into(viewHolder.img_service);
            if (itemBeanListArrayList.get(position).getProduct_like().equalsIgnoreCase("No")){
                viewHolder.fav_sts_img.setImageResource(R.drawable.unlike_but);
            }
            else {
                viewHolder.fav_sts_img.setImageResource(R.drawable.like_but);
            }
            viewHolder.fav_sts_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followUnfollow(itemBeanListArrayList.get(position).getId(),position,itemBeanListArrayList.get(position).getProduct_like());
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String price ;
                    if (itemBeanListArrayList.get(position).getProductSizePrice()!=null&&!itemBeanListArrayList.get(position).getProductSizePrice().isEmpty()){
                        price = itemBeanListArrayList.get(position).getProductSizePrice().get(0).getPrice();
                        itemBeanListArrayList.get(position).getProductSizePrice().get(0).setIs_selected(true);
                    }
                    else {
                        price = itemBeanListArrayList.get(position).getPrice();
                    }
                    Intent ii = new Intent(activity, AddItemActivity.class);  //AddItemActivity
                    ii.putExtra("itemid", itemBeanListArrayList.get(position).getId());
                    ii.putExtra("itemname", itemBeanListArrayList.get(position).getName());
                    ii.putExtra("itemprice", price);
                    ii.putExtra("itemimage", itemBeanListArrayList.get(position).getImage1());
                    ii.putExtra("fav", "");
                    ii.putExtra("branchid", itemBeanListArrayList.get(position).getCompanyId());
                    ii.putExtra("variant", "");
                    ii.putExtra("discount", "");
                    ii.putExtra("itemdescription", itemBeanListArrayList.get(position).getDescription());
                    activity.startActivity(ii);
                }
            });*/
        }

        @Override
        public int getItemCount() {
            //   return 4;
            return (null != itemBeanListArrayList ? itemBeanListArrayList.size() : 0);
        }

        /**
         * View holder to display each RecylerView item
         */
        protected class ViewHolder extends RecyclerView.ViewHolder {
            private TextView item_name_tv,price_tv,item_des_tv,code_tv,code_description,code_percent;
            ImageView img_service,fav_sts_img;

            public ViewHolder(View view) {
                super(view);
                code_tv = view.findViewById(R.id.code_tv);
                code_percent = view.findViewById(R.id.code_percent);
                code_description = view.findViewById(R.id.code_description);
              //  price_tv = view.findViewById(R.id.price_tv);
//                item_des_tv = view.findViewById(R.id.item_des_tv);
//                item_name_tv = view.findViewById(R.id.item_name_tv);
//                fav_sts_img =  view.findViewById(R.id.fav_sts_img);
                img_service =  view.findViewById(R.id.img_service);

            }
        }
    }


}
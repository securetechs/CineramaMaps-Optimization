package main.com.cineramamaps.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Utils.Tools;
import main.com.cineramamaps.activity.GuidelinesDetails;
import main.com.cineramamaps.constant.MyLanguageSession;
import main.com.cineramamaps.databinding.LayoutCustmHomeCategoryBinding;
import main.com.cineramamaps.model.CategoryBeanList;
import main.com.cineramamaps.model.GuidelineBeanlist;


public class HorizontalGuidelineAdapter extends RecyclerView.Adapter<HorizontalGuidelineAdapter.ViewHolder> {
    private String language = "";
    MyLanguageSession myLanguageSession;
    private ArrayList<Bitmap> horizontalList;
    private List<GuidelineBeanlist> categoryBeanListArrayList;
    Activity context;
    boolean b;


    public HorizontalGuidelineAdapter(List<GuidelineBeanlist> categoryBeanListArrayList, Activity context, boolean b) {
        this.context = context;
        this.b = b;
        this.horizontalList = horizontalList;
        this.categoryBeanListArrayList = categoryBeanListArrayList;
    }








    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Tools.get().updateResources(context);
        myLanguageSession = new MyLanguageSession(context);
        language = myLanguageSession.getLanguage();
        myLanguageSession.setLangRecreate(myLanguageSession.getLanguage());
        LayoutCustmHomeCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_home_category, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(language.equalsIgnoreCase("en")) {
            holder.binding.nametv.setText("" + categoryBeanListArrayList.get(position).getTitle());
        }else{
            holder.binding.nametv.setText("" + categoryBeanListArrayList.get(position).getTitleAr());
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
        if (categoryBeanListArrayList!=null&&!categoryBeanListArrayList.isEmpty()){

            Picasso.get().load(""+ categoryBeanListArrayList.get(position).getImage()).placeholder(R.color.lightgrey).into(holder.binding.catimage);

        }
        holder.binding.itemlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, GuidelinesDetails.class);
                 i.putExtra("category_id",""+categoryBeanListArrayList.get(position).getId());
                i.putExtra("category_image",""+categoryBeanListArrayList.get(position).getImage());
                 i.putExtra("category_name",""+categoryBeanListArrayList.get(position).getTitle());
                 i.putExtra("category_name_ar",""+categoryBeanListArrayList.get(position).getTitleAr());
                 i.putExtra("category_date",""+categoryBeanListArrayList.get(position).getDateTime());
                 i.putExtra("category_details",""+categoryBeanListArrayList.get(position).getDescription());
                 i.putExtra("category_details_ar",""+categoryBeanListArrayList.get(position).getDescriptionAr());
                 //i.putExtra("category_id", "1");
                context.startActivity(i);
            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCustmHomeCategoryBinding binding;

        public ViewHolder(@NonNull LayoutCustmHomeCategoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }



    @Override
    public int getItemCount() {
      // return 6;
        return categoryBeanListArrayList == null ? 0 : categoryBeanListArrayList.size();

    }


}

package main.com.cineramamaps.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.databinding.LayoutCustmFilterCategoryBinding;
import main.com.cineramamaps.databinding.LayoutCustmHomeCategoryBinding;
import main.com.cineramamaps.model.CategoryBeanList;


public class FilterCategoryAdapter extends RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder> {

    private ArrayList<Bitmap> horizontalList;
    private List<CategoryBeanList> categoryBeanListArrayList;
    Activity context;
    boolean b;


    public FilterCategoryAdapter(List<CategoryBeanList> categoryBeanListArrayList, Activity context, boolean b) {
        this.context = context;
        this.b = b;
        this.horizontalList = horizontalList;
        this.categoryBeanListArrayList = categoryBeanListArrayList;
    }








    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCustmFilterCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_filter_category, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCustmFilterCategoryBinding binding;

        public ViewHolder(@NonNull LayoutCustmFilterCategoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }



    @Override
    public int getItemCount() {
        return 8;
       // return categoryBeanListArrayList == null ? 0 : categoryBeanListArrayList.size();

    }


}

package main.com.cineramamaps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.databinding.CustomSubItemBinding;
import main.com.cineramamaps.databinding.LayoutCustmReviewBinding;
import main.com.cineramamaps.model.CartDetail;


public class OrderSubAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    private List<CartDetail> cartDetails;
    public OrderSubAdapter(Context applicationContext, List<CartDetail> cartDetails) {
        this.context = applicationContext;
        this.cartDetails = cartDetails;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
       //  return 5;
        return cartDetails == null ? 0 : cartDetails.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
      //  view = inflter.inflate(R.layout.layout_yachts_item_new_sec, null);


        YachtsViewHolder holder;

        if (view == null) {
            CustomSubItemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_sub_item,viewGroup,false);

            holder = new YachtsViewHolder(binding);
            holder.view = binding.getRoot();
            holder.view.setTag(holder);
        }
        else {
            holder = (YachtsViewHolder) view.getTag();
        }


        if (cartDetails.get(position).getExtraItemName()==null||cartDetails.get(position).getExtraItemName().equalsIgnoreCase(""))
        {
            holder.binding.extraItemTv.setVisibility(View.GONE);
        }
        else {
            holder.binding.extraItemTv.setVisibility(View.VISIBLE);
        }
        holder.binding.extraItemTv.setText("with "+cartDetails.get(position).getExtraItemName().trim());

        String full_str = cartDetails.get(position).getQuantity()+" X "+cartDetails.get(position).getProduct_name();
        holder.binding.itemNameTv.setText(""+full_str);
        holder.binding.itemNameTvprice.setText(context.getResources().getString(R.string.currency)+""+cartDetails.get(position).getTotalAmount());

        return holder.view;



    }

    private static class YachtsViewHolder {
        private View view;
        private CustomSubItemBinding binding;

        YachtsViewHolder(CustomSubItemBinding binding) {
            this.view = binding.getRoot();
            this.binding = binding;
        }
    }
}



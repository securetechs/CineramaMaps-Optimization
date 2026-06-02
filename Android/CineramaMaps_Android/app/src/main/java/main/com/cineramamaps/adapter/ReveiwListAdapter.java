package main.com.cineramamaps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.squareup.picasso.Picasso;

import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.databinding.LayoutCustmReviewBinding;
import main.com.cineramamaps.model.RatingBeanList;
import main.com.cineramamaps.model.RatingReview;


public class ReveiwListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    private List<RatingReview> ratingBeanLists;
    public ReveiwListAdapter(Context applicationContext, List<RatingReview> ratingBeanLists) {
        this.context = applicationContext;
        this.ratingBeanLists = ratingBeanLists;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
       //  return 2;
        return ratingBeanLists == null ? 0 : ratingBeanLists.size();
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
            LayoutCustmReviewBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_review,viewGroup,false);

            holder = new YachtsViewHolder(binding);
            holder.view = binding.getRoot();
            holder.view.setTag(holder);
        }
        else {
            holder = (YachtsViewHolder) view.getTag();
        }

        holder.binding.commenttv.setText(ratingBeanLists.get(position).getReview());
        holder.binding.datetv.setText(ratingBeanLists.get(position).getDateTime());
        holder.binding.nametv.setText(ratingBeanLists.get(position).getUserName());
        holder.binding.ratingtv.setText(""+ratingBeanLists.get(position).getRating());
        holder.binding.ratingbar.setRating(Float.parseFloat(ratingBeanLists.get(position).getRating()));
        if (ratingBeanLists.get(position).getImage() != null && !ratingBeanLists.get(position).getImage().isEmpty()) {
            Picasso.get().load(ratingBeanLists.get(position).getImage()).placeholder(R.color.lightgrey).into(holder.binding.profileic);
        }


        return holder.view;



    }

    private static class YachtsViewHolder {
        private View view;
        private LayoutCustmReviewBinding binding;

        YachtsViewHolder(LayoutCustmReviewBinding binding) {
            this.view = binding.getRoot();
            this.binding = binding;
        }
    }
}



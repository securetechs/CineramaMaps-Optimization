package main.com.cineramamaps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.databinding.LayoutCustmReviewBinding;
import main.com.cineramamaps.model.RatingBeanList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    Context context;
    List<RatingBeanList> ratingBeanLists;

    public ReviewAdapter(Context context, List<RatingBeanList> ratingBeanLists) {
        this.context = context;
        this.ratingBeanLists = ratingBeanLists;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCustmReviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_review, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.commenttv.setText(ratingBeanLists.get(position).getReview());
        holder.binding.datetv.setText(ratingBeanLists.get(position).getDateTime());
        holder.binding.nametv.setText(ratingBeanLists.get(position).getUserName());
        holder.binding.ratingtv.setText(""+ratingBeanLists.get(position).getRating());
        holder.binding.ratingbar.setRating(Float.parseFloat(ratingBeanLists.get(position).getRating()));
        if (ratingBeanLists.get(position).getImage() != null && !ratingBeanLists.get(position).getImage().isEmpty()) {
            Picasso.get().load(ratingBeanLists.get(position).getImage()).placeholder(R.color.lightgrey).into(holder.binding.profileic);
        }

    }

    @Override
    public int getItemCount() {
       // return 20;
           return ratingBeanLists == null ? 0 : ratingBeanLists.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCustmReviewBinding binding;

        public ViewHolder(@NonNull LayoutCustmReviewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}

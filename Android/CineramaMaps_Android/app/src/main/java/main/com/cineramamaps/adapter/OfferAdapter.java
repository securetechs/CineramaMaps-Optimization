package main.com.cineramamaps.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.activity.OfferDetailsActivity;
import main.com.cineramamaps.model.OfferBeanList_New;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private Context context;
    private List<OfferBeanList_New> offerList;
    String language="en";

    public OfferAdapter(Context context, List<OfferBeanList_New> offerList,String language) {
        this.context = context;
        this.offerList = offerList;
        this.language = language;
    }

    @Override
    public OfferAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.offerlistitemlay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferAdapter.ViewHolder holder, int position) {
        OfferBeanList_New offer = offerList.get(position);

       // holder.datetv.setText("Get a " + offer.getDiscountPercentage() + "% discount at\n" + offer.getCompanyName());
      if(language.equalsIgnoreCase("en")) {
          holder.datetv.setText("" + offer.getCompanyName());
          holder.submit.setText("" + offer.getDiscountPercentage() + "%\n"+context.getResources().getString(R.string.percentdiscount1));
         // holder.submit.setText(offer.getDiscountCode() + " " + offer.getDiscountPercentage() + ""+context.getResources().getString(R.string.percentdiscount1));
      }else{
          holder.datetv.setText("" + offer.getCompanyNameAr());
          holder.submit.setText("" + offer.getDiscountPercentage() + "%\n"+context.getResources().getString(R.string.percentdiscount1));
         // holder.submit.setText(offer.getDiscountCode() + " " + offer.getDiscountPercentage() + ""+context.getResources().getString(R.string.percentdiscount1));
      }
//        Glide.with(context)
//                .load(offer.getImage())// optional placeholder
//                .into(holder.offeric);

        holder.shimmerLayout.setVisibility(View.VISIBLE);
        holder.shimmerLayout.startShimmerAnimation();

        Picasso.get()
                .load(offer.getImage())
//                .placeholder(R.drawable.favitemimg)
//                .error(R.drawable.favitemimg)
                .into(holder.offeric,
                        new com.squareup.picasso.Callback() {

                            @Override
                            public void onSuccess() {

                                holder.shimmerLayout.stopShimmerAnimation();
                                holder.shimmerLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                                holder.shimmerLayout.stopShimmerAnimation();
                                holder.shimmerLayout.setVisibility(View.GONE);
                            }
                        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(context, OfferDetailsActivity.class);
                ii.putExtra("code", offer.getDiscountCode());
                ii.putExtra("percent", offer.getDiscountPercentage());
                ii.putExtra("title", offer.getCompanyName());
                ii.putExtra("title_ar", offer.getCompanyNameAr());
                ii.putExtra("description", offer.getDescription());
                ii.putExtra("description_ar", offer.getDescriptionAr());
                ii.putExtra("image", offer.getImage());
                context.startActivity(ii);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offerList != null ? offerList.size() : 0;
    }
    public void updateList(List<OfferBeanList_New> newList) {
        offerList = newList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ShimmerFrameLayout shimmerLayout;
        ImageView offeric;
        TextView datetv, submit;

        public ViewHolder(View itemView) {
            super(itemView);

            shimmerLayout = itemView.findViewById(R.id.shimmerLayout);
            offeric = itemView.findViewById(R.id.offeric);
            datetv = itemView.findViewById(R.id.datetv);
            submit = itemView.findViewById(R.id.submit);
        }
    }
}
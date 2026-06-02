package main.com.cineramamaps.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.activity.ServiceDetailActivity;
import main.com.cineramamaps.databinding.CitylistitemBinding;
import main.com.cineramamaps.model.OrderBeanList;
import main.com.cineramamaps.model.ServiceBeanListNew;

public class ServicesCityListAdapter extends RecyclerView.Adapter<ServicesCityListAdapter.ViewHolder> {
    Context context;
    List<ServiceBeanListNew> orderBeanLists;
    private String language;

    public ServicesCityListAdapter(Context context, List<ServiceBeanListNew> orderBeanLists, String language) {
        this.context = context;
        this.orderBeanLists = orderBeanLists;
        this.language = language;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CitylistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.citylistitem, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      // Picasso.get().load(orderBeanLists.get(position).getCover_image()).placeholder(R.color.lightgrey).into(holder.binding.companyimg);
        holder.binding.shimmerLayout.setVisibility(View.VISIBLE);
        holder.binding.shimmerLayout.startShimmerAnimation();

        if (orderBeanLists.get(position).getCover_image() != null
                && !orderBeanLists.get(position).getCover_image().isEmpty()) {

            Picasso.get()
                    .load(orderBeanLists.get(position).getCover_image())
                    .placeholder(R.color.lightgrey)
                    .error(R.color.lightgrey)
                    .into(holder.binding.companyimg,
                            new com.squareup.picasso.Callback() {

                                @Override
                                public void onSuccess() {

                                    holder.binding.shimmerLayout.stopShimmerAnimation();
                                    holder.binding.shimmerLayout.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {

                                    holder.binding.shimmerLayout.stopShimmerAnimation();
                                    holder.binding.shimmerLayout.setVisibility(View.GONE);
                                }
                            });

        } else {

            holder.binding.shimmerLayout.stopShimmerAnimation();
            holder.binding.shimmerLayout.setVisibility(View.GONE);

            holder.binding.companyimg.setImageResource(R.color.lightgrey);
        }

        String rawHtml;
        if (language.equals("ar")) {
            holder.binding.nametv.setText(orderBeanLists.get(position).getCompanyNameAr());
            rawHtml = orderBeanLists.get(position).getDescriptionAr();
        } else {
            holder.binding.nametv.setText(orderBeanLists.get(position).getCompanyName());
            rawHtml = orderBeanLists.get(position).getDescription();
        }

        String cleanedHtml = rawHtml
                .replaceAll("\\\\r\\\\n", "") // removes literal \r\n
                .replaceAll("\\r\\n", "")     // removes actual carriage return + newline
                .replaceAll("(?i)<div[^>]*id=[\"']gtx-trans[\"'][^>]*>.*?</div>", ""); // removes that extra <div>

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.binding.destv.setText(Html.fromHtml(cleanedHtml, Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            holder.binding.destv.setText(Html.fromHtml(cleanedHtml));
//        }

        String htmlData = cleanedHtml;

// Important: avoid multiple reload issues in RecyclerView
        holder.binding.destv.getSettings().setJavaScriptEnabled(false);
        holder.binding.destv.getSettings().setDomStorageEnabled(true);
        holder.binding.destv.setWebViewClient(new WebViewClient());

// Wrap for better styling + responsive images
        String finalHtml = "<html><head>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0' />"
                + "<style>img{max-width:100%; height:auto;} body{font-size:14px; padding:4px; margin:0;}</style>"
                + "</head><body>"
                + htmlData
                + "</body></html>";

        holder.binding.destv.loadDataWithBaseURL(
                null,
                finalHtml,
                "text/html",
                "UTF-8",
                null
        );

        holder.binding.addresstv.setText(orderBeanLists.get(position).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(context, ServiceDetailActivity.class);
                ii.putExtra("id", "" + orderBeanLists.get(position).getId());
                context.startActivity(ii);
            }
        });


    }

    @Override
    public int getItemCount() {
        //   return 7;
        return orderBeanLists == null ? 0 : orderBeanLists.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CitylistitemBinding binding;

        public ViewHolder(@NonNull CitylistitemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}

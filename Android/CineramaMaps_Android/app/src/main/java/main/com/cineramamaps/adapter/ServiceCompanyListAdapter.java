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
import main.com.cineramamaps.databinding.ServiceCompanylistitemBinding;
import main.com.cineramamaps.model.OrderBeanList;
import main.com.cineramamaps.model.ServiceBeanListNew;

public class ServiceCompanyListAdapter extends RecyclerView.Adapter<ServiceCompanyListAdapter.ViewHolder> {
    Context context;
    List<ServiceBeanListNew> orderBeanLists;
    private String language;

    public ServiceCompanyListAdapter(Context context, List<ServiceBeanListNew> orderBeanLists,String language) {
        this.context = context;
        this.orderBeanLists = orderBeanLists;
        this.language = language ;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServiceCompanylistitemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.service_companylistitem, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if (orderBeanLists.get(position).getTotalDiscountAmount()!=null&&!orderBeanLists.get(position).getTotalDiscountAmount().equalsIgnoreCase("")) {
//            double tot = Double.parseDouble(orderBeanLists.get(position).getTotalDiscountAmount());
//            holder.binding.discountFee.setText(context.getResources().getString(R.string.currency)+""+String.format("%.2f", tot));
//        }
//
//        if (orderBeanLists.get(position).getBeforeDiscountAmount()!=null&&!orderBeanLists.get(position).getBeforeDiscountAmount().equalsIgnoreCase("")) {
//            double tot = Double.parseDouble(orderBeanLists.get(position).getBeforeDiscountAmount());
//            holder.binding.tvSubTotalAmnt.setText(context.getResources().getString(R.string.currency)+""+String.format("%.2f", tot));
//        }
//        if (orderBeanLists.get(position).getTotalAmount()!=null&&!orderBeanLists.get(position).getTotalAmount().equalsIgnoreCase("")) {
//            double tot = Double.parseDouble(orderBeanLists.get(position).getTotalAmount());
//            holder.binding.tvTotalAmnt.setText(context.getResources().getString(R.string.currency)+""+String.format("%.2f", tot));
//        }
//        if (orderBeanLists.get(position).getDeliveryFee()!=null&&!orderBeanLists.get(position).getDeliveryFee().equalsIgnoreCase("")) {
//            double tot = Double.parseDouble(orderBeanLists.get(position).getDeliveryFee());
//            holder.binding.tvDeliveryFee.setText(context.getResources().getString(R.string.currency)+""+String.format("%.2f", tot));
//        }
//
//        if (orderBeanLists.get(position).getDeliveryFee().equalsIgnoreCase("Pickup")){
//            //  track_orders.setVisibility(View.GONE);
//        }
//        else {
//            //  track_orders.setVisibility(View.VISIBLE);
//        }
//
//
       // Picasso.get().load(orderBeanLists.get(position).getImage1()).placeholder(R.color.lightgrey).into(holder.binding.companyimg);
        holder.binding.shimmerLayout.setVisibility(View.VISIBLE);
        holder.binding.shimmerLayout.startShimmerAnimation();

        if (orderBeanLists.get(position).getImage1() != null
                && !orderBeanLists.get(position).getImage1().isEmpty()) {

            Picasso.get()
                    .load(orderBeanLists.get(position).getImage1())
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



// Clean unwanted \r\n and hidden divs
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
       // holder.binding.destv.setText(orderBeanLists.get(position).getDescription());
        holder.binding.addresstv.setText(orderBeanLists.get(position).getAddress().trim());
//            /*if (currentorderBeanListArrayList.get(position).getDate()!=null&&!currentorderBeanListArrayList.get(position).getDate().equalsIgnoreCase("")){
//                order_date_tv.setText(""+currentorderBeanListArrayList.get(position).getDate()+" "+currentorderBeanListArrayList.get(position).getTime());
//            }
//            else {
//                order_date_tv.setText(currentorderBeanListArrayList.get(position).getDateTime());
//            }*/
//
//        holder.binding.txtOrderid.setText(context.getResources().getString(R.string.orderid)+" : "+orderBeanLists.get(position).getId());
//
//
//        if (orderBeanLists.get(position).getDate()!=null&&!orderBeanLists.get(position).getDate().equalsIgnoreCase("")){
//            holder.binding.orderDateTv.setText(orderBeanLists.get(position).getTime()+" "+orderBeanLists.get(position).getDate());
//
//        }
//        else {
//
//            holder.binding.orderDateTv.setText(orderBeanLists.get(position).getTime()+" "+orderBeanLists.get(position).getDateTime());
//        }
//
//        holder.binding.itemsList.setExpanded(true);
//        OrderSubAdapter orderSubAdapter = new OrderSubAdapter(context,orderBeanLists.get(position).getCartDetails());
//        holder.binding.itemsList.setAdapter(orderSubAdapter);

          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent ii = new Intent(context, ServiceDetailActivity.class);
                  ii.putExtra("id",""+orderBeanLists.get(position).getId());
                  context.startActivity(ii);
              }
          });


    }

    @Override
    public int getItemCount() {
     //  return 5;
           return orderBeanLists == null ? 0 : orderBeanLists.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ServiceCompanylistitemBinding binding;

        public ViewHolder(@NonNull ServiceCompanylistitemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}

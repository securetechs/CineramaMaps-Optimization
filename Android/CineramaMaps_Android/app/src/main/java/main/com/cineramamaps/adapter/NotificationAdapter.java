package main.com.cineramamaps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.databinding.LayoutCustomNotificationBinding;
import main.com.cineramamaps.model.NotificationBeanList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    boolean b;
    List<NotificationBeanList> notificationBeanListArrayList;
    String  language = "";

    public NotificationAdapter(Context context, ArrayList<NotificationBeanList> notificationBeanListArrayList, boolean b,String language) {
        this.context = context;
        this.notificationBeanListArrayList = notificationBeanListArrayList;
        this.b = b;
        this.language = language;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCustomNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custom_notification, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       if(language.equalsIgnoreCase("ar")) {
           holder.binding.titletv.setText("" + notificationBeanListArrayList.get(position).getTitle_ar());
           holder.binding.datetv.setText("" + notificationBeanListArrayList.get(position).getDateTime());
           holder.binding.nametv.setText("" + notificationBeanListArrayList.get(position).getMessage_ar());
       }else{
           holder.binding.titletv.setText("" + notificationBeanListArrayList.get(position).getTitle());
           holder.binding.datetv.setText("" + notificationBeanListArrayList.get(position).getDateTime());
           holder.binding.nametv.setText("" + notificationBeanListArrayList.get(position).getMessage());
       }
 /*
        if (notificationBeanListArrayList.get(position).getUserDetails() != null) {
            String image = notificationBeanListArrayList.get(position).getUserDetails().getImage();

            if (image != null && !image.equalsIgnoreCase("")) {
                Picasso.get().load(image).placeholder(R.drawable.profile_ic).into(holder.binding.userpic);
            }


        }*/
    }

    @Override
    public int getItemCount() {
         //return 4;
        return notificationBeanListArrayList == null ? 0 : notificationBeanListArrayList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCustomNotificationBinding binding;

        public ViewHolder(@NonNull LayoutCustomNotificationBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}

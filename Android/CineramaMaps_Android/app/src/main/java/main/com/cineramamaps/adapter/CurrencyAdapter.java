package main.com.cineramamaps.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Utils.Preferences;
import main.com.cineramamaps.activity.HomeActivity;
import main.com.cineramamaps.activity.MapDetailsActivity;
import main.com.cineramamaps.databinding.AdapterDialogCurrencyBinding;
import main.com.cineramamaps.databinding.LayoutCustomNotificationBinding;
import main.com.cineramamaps.model.CurrencyModel;
import main.com.cineramamaps.model.NotificationBeanList;
import main.com.cineramamaps.model.ProfileResponse;


public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    Context context;
    boolean b;
    List<CurrencyModel> currencyModels;
    String  language = "";
    Dialog dialog;

    public CurrencyAdapter(Context context, ArrayList<CurrencyModel> currencyModels, Dialog dialog) {
        this.context = context;
        this.currencyModels = currencyModels;
        this.b = b;
        this.language = language;
        this.dialog = dialog;
     }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterDialogCurrencyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_dialog_currency, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(currencyModels.get(position).getFlagUrl())
                .into(holder.binding.imgCurrency);

        holder.binding.tvCurrency.setText("" + currencyModels.get(position).getCurrencyCode());



        if(currencyModels.get(position).getCurrencyCode().equalsIgnoreCase(Preferences.get(context,Preferences.KEY_CURRENCY))){
            currencyModels.get(position).setSelected(true);
        }else{
            currencyModels.get(position).setSelected(false);
        }
        holder.binding.radioCurrency.setChecked(currencyModels.get(position).isSelected());
        holder.binding.llCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<currencyModels.size();i++){
                    currencyModels.get(i).setSelected(false);
                }

                Preferences.save(context,Preferences.KEY_CURRENCY,currencyModels.get(position).getCurrencyCode() );
                MapDetailsActivity.currency = currencyModels.get(position).getCurrencyCode();
                MapDetailsActivity.currency_ar = currencyModels.get(position).getCurrencyCode();
                currencyModels.get(position).setSelected(true);
                if (context instanceof MapDetailsActivity) {
                    ((MapDetailsActivity)context).setDynmaicData();
                }else if (context instanceof HomeActivity) {
                    ((HomeActivity)context).setDynmaicData();
                }

                notifyDataSetChanged();
                dialog.dismiss();
            }
        });



    }

    @Override
    public int getItemCount() {
         //return 4;
        return currencyModels == null ? 0 : currencyModels.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AdapterDialogCurrencyBinding binding;

        public ViewHolder(@NonNull AdapterDialogCurrencyBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}

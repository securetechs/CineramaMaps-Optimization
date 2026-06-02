package main.com.cineramamaps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.databinding.LayoutCustmTransactionBinding;
import main.com.cineramamaps.model.TransactionBeanList;

public class WalletTransactionAdapter extends RecyclerView.Adapter<WalletTransactionAdapter.ViewHolder> {
    Context context;
    List<TransactionBeanList> transactionBeanLists;

    public WalletTransactionAdapter(Context context, List<TransactionBeanList> transactionBeanLists) {
        this.context = context;
        this.transactionBeanLists = transactionBeanLists;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCustmTransactionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_custm_transaction, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.datetv.setText(transactionBeanLists.get(position).getDateTime());
        if (transactionBeanLists.get(position).getTransactionType().equalsIgnoreCase("Top Up")){
            holder.binding.transactiontypetv.setText(""+context.getResources().getString(R.string.topup));
            holder.binding.transactiontypetv.setText("+"+context.getResources().getString(R.string.currency)+transactionBeanLists.get(position).getTotalAmount()+" "+context.getResources().getString(R.string.topuptowallet));
        }


    }

    @Override
    public int getItemCount() {
       // return 10;
           return transactionBeanLists == null ? 0 : transactionBeanLists.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCustmTransactionBinding binding;

        public ViewHolder(@NonNull LayoutCustmTransactionBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}

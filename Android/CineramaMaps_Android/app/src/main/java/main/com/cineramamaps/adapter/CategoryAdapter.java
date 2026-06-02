package main.com.cineramamaps.adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import main.com.cineramamaps.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<String> items;
    private final OnItemClickListener listener;
    private int selectedPosition;
    private final boolean allowDefaultSelection;

    public interface OnItemClickListener {
        void onClick(String item, int position);
    }

    public CategoryAdapter(List<String> items, OnItemClickListener listener) {
        this(items, listener, true); // Default: allow selection at position 0
    }

    public CategoryAdapter(List<String> items, OnItemClickListener listener, boolean allowDefaultSelection) {
        this.items = items;
        this.listener = listener;
        this.allowDefaultSelection = allowDefaultSelection;
        this.selectedPosition = allowDefaultSelection ? 0 : -1;
    }

    public void setSelectedPosition(int position) {
        int prev = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(prev);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = items.get(position);
        holder.textView.setText(text);
        boolean isSelected = (position == selectedPosition);

        holder.textView.setBackgroundResource(isSelected
                ? R.drawable.rounded_body_selected
                : R.drawable.rounded_body_unselected);
        holder.textView.setTextColor(isSelected
                ? holder.textView.getContext().getResources().getColor(R.color.white) : holder.textView.getContext().getResources().getColor(R.color.colorPrimary)
        );

        holder.itemView.setOnClickListener(v -> {
            v.setEnabled(false); // disable click

            listener.onClick(text, position);
            setSelectedPosition(position);

            // Re-enable click after 1 second (1000 ms)
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                v.setEnabled(true);
            }, 2000);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.offer_name);
        }
    }
}

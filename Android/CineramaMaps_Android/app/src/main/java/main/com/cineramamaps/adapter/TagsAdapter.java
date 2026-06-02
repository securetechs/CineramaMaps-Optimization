package main.com.cineramamaps.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import main.com.cineramamaps.R;
import main.com.cineramamaps.databinding.TaglistitemBinding;
import main.com.cineramamaps.model.Tag;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private final Context context;
    private final List<Tag> tagsList;
    private final boolean isArabic;
    private final OnTagClickListener onTagClickListener;

    public interface OnTagClickListener {
        void onTagClick(String tagColor);
    }

    public TagsAdapter(Context context, List<Tag> tagsList, boolean isArabic, boolean isFavoriteList, OnTagClickListener onTagClickListener) {
        this.context = context;
        this.tagsList = tagsList;
        this.isArabic = isArabic;
        this.onTagClickListener = onTagClickListener;
    }

    @NonNull
    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TaglistitemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.taglistitem,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsAdapter.ViewHolder holder, int position) {
        Tag tag = tagsList.get(position);
        holder.binding.tagname.setText(isArabic ? tag.getTagNameAr() : tag.getTagName());
        holder.binding.tagname.setTextColor(Color.parseColor(tag.getColorCode()));
        holder.binding.tagcount.setText(String.valueOf(tag.getTotalTagPlaceCount()));
        holder.binding.tagcount.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(tag.getColorCode())));

        holder.binding.item.setOnClickListener(v -> {
            if (onTagClickListener != null) {
                onTagClickListener.onTagClick(tag.getColorCode());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagsList == null ? 0 : tagsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TaglistitemBinding binding;

        public ViewHolder(@NonNull TaglistitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
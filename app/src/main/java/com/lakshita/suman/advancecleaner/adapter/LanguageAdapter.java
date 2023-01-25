package com.lakshita.suman.advancecleaner.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.lakshita.suman.advancecleaner.databinding.ItemLanguageBinding;
import com.lakshita.suman.advancecleaner.model.ItemSelected;

import java.util.List;


public class LanguageAdapter extends BaseRecyclerAdapter2<ItemSelected> {
    private String selected = "";

    public String getSelected() {
        return selected;
    }

    public LanguageAdapter(List<ItemSelected> list, Context context, String selected) {
        super(list, context);
        this.selected = selected;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemLanguageBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            binData(list.get(position), (ViewHolder) holder);
        }
    }

    public void binData(ItemSelected item, ViewHolder viewHolder) {
        if (item == null)
            return;
        if (!TextUtils.isEmpty(context.getResources().getString(item.getEntry()))) {
            viewHolder.binding.tvTitle.setText(context.getResources().getString(item.getEntry()));
        }
        if (TextUtils.isEmpty(selected)) {
            selected = item.getValue();
        }
        viewHolder.binding.rdSelect.setChecked(item.getValue().equals(selected));

        viewHolder.binding.getRoot().setOnClickListener(v -> {
            selected = item.getValue();
            notifyDataSetChanged();
            onClickItem(item);
        });
    }

    public class ViewHolder extends BaseViewHolder<ItemLanguageBinding> {

        public ViewHolder(ItemLanguageBinding binding) {
            super(binding);
        }
    }
}


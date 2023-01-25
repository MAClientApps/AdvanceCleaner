package com.security.applock.ui.appmask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.security.applock.R;
import com.security.applock.adapter.BaseRecyclerAdapter;
import com.security.applock.databinding.ItemAppMaskBinding;
import com.security.applock.model.IconApp;

import java.util.List;

public class AppMaskAdapter extends BaseRecyclerAdapter<IconApp, AppMaskAdapter.ViewHolder> {
    private int itemSelected;

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public AppMaskAdapter(Context context, List<IconApp> list, int itemSelected) {
        super(context, list);
        this.itemSelected = itemSelected;
    }

    @Override
    public void onBindViewHolder(AppMaskAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public AppMaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAppMaskBinding binding = ItemAppMaskBinding.bind(LayoutInflater.from(mContext).inflate(R.layout.item_app_mask, parent, false));
        return new ViewHolder(binding);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemAppMaskBinding binding;

        public ViewHolder(ItemAppMaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(IconApp model) {
            Glide.with(mContext).load(model.getIconPreview()).into(binding.imvIcon);
            binding.tvName.setText(model.getNameDisplay());
            binding.rb.setChecked(model.getId() == itemSelected);

            binding.container.setOnClickListener(v -> {
                itemSelected = model.getId();
                notifyDataSetChanged();
            });
        }
    }
}


package com.example.mealsapp.ui.home.details.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.ItemCountryBinding;

public class MealIngredientsAdapter extends ListAdapter<String, MealIngredientsAdapter.ViewHolder> {
    private Context context;

    public MealIngredientsAdapter(Context context) {
        super(new DiffUtil.ItemCallback<String>() {
            @Override
            public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), context);
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCountryBinding binding;

        public ViewHolder(ItemCountryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String ingredient, Context context) {
            binding.tvName.setText(ingredient);

            Glide.with(binding.getRoot())
                    .load("https://www.themealdb.com/images/ingredients/" + ingredient + ".png")
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivImage);
        }

        public static ViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemCountryBinding binding = ItemCountryBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(binding);
        }
    }
}



package com.example.mealsapp.ui.home.search.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.ItemCountryBinding;
import com.example.mealsapp.model.pojo.ingredient.Ingredient;
import com.example.mealsapp.model.pojo.meal.Meal;

import java.util.Objects;

public class IngredientsAdapter extends ListAdapter<Ingredient, IngredientsAdapter.ViewHolder> {
    private OnIngredientClickListener clickListener;

    public IngredientsAdapter(OnIngredientClickListener clickListener) {
        super(new DiffUtil.ItemCallback<Ingredient>() {
            @Override
            public boolean areItemsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
                return Objects.equals(oldItem.getIdIngredient(), newItem.getIdIngredient());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
                return oldItem.getStrIngredient().equals(newItem.getStrIngredient());
            }
        });
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCountryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvName.setText(getItem(position).getStrIngredient());

        Glide.with(holder.binding.getRoot())
                .load("https://www.themealdb.com/images/ingredients/" + getItem(position).getStrIngredient() + ".png")
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.ivImage);

        holder.binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onIngredientClick(getItem(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(getCurrentList().size(), 30);
    }

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCountryBinding binding;

        public ViewHolder(ItemCountryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}



package com.example.mealsapp.ui.home.search.view;

import android.content.Context;
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

import java.util.Objects;

public class IngredientsAdapter extends ListAdapter<Ingredient, IngredientsAdapter.ViewHolder> {
    private Context context;
    private OnIngredientClickListener clickListener;

    public IngredientsAdapter(Context context, OnIngredientClickListener clickListener) {
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
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), clickListener, context);
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

        public void bind(Ingredient ingredient, OnIngredientClickListener clickListener, Context context) {
            binding.tvName.setText(ingredient.getStrIngredient());

            Glide.with(binding.getRoot())
                    .load("https://www.themealdb.com/images/ingredients/" + ingredient.getStrIngredient() + ".png")
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivImage);

            binding.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onIngredientClick(ingredient);
                }
            });
        }

        public static ViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemCountryBinding binding = ItemCountryBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(binding);
        }
    }
}



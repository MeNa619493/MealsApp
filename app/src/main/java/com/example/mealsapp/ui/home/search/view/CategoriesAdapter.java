package com.example.mealsapp.ui.home.search.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealsapp.R;
import com.example.mealsapp.databinding.ItemCategoryBinding;
import com.example.mealsapp.model.pojo.category.Category;
import com.example.mealsapp.model.pojo.meal.Meal;

import java.util.Objects;

public class CategoriesAdapter extends ListAdapter<Category, CategoriesAdapter.ViewHolder> {
    private Context context;
    private OnCategoryClickListener clickListener;

    public CategoriesAdapter(Context context, OnCategoryClickListener clickListener) {
        super(new DiffUtil.ItemCallback<Category>() {
            @Override
            public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                return Objects.equals(oldItem.getStrCategory(), newItem.getStrCategory());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
                return oldItem.getStrCategory().equals(newItem.getStrCategory());
            }
        });
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvName.setText(getItem(position).getStrCategory());
        holder.binding.ivImage.setImageDrawable(getItemImage(getItem(position).getStrCategory()));
        holder.binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onCategoryClick(getItem(position));
            }
        });
    }

    private Drawable getItemImage(String name) {
        Drawable drawable = null;
        switch (name) {
            case "Beef":
                drawable = context.getResources().getDrawable(R.drawable.beef);
                break;
            case "Breakfast":
                drawable = context.getResources().getDrawable(R.drawable.breakfast);
                break;
            case "Chicken":
                drawable = context.getResources().getDrawable(R.drawable.chicken);
                break;
            case "Dessert":
                drawable = context.getResources().getDrawable(R.drawable.dessert);
                break;
            case "Goat":
                drawable = context.getResources().getDrawable(R.drawable.goat);
                break;
            case "Lamb":
                drawable = context.getResources().getDrawable(R.drawable.lamb);
                break;
            case "Miscellaneous":
                drawable = context.getResources().getDrawable(R.drawable.miscellaneous);
                break;
            case "Pasta":
                drawable = context.getResources().getDrawable(R.drawable.pasta);
                break;
            case "Pork":
                drawable = context.getResources().getDrawable(R.drawable.pork);
                break;
            case "Seafood":
                drawable = context.getResources().getDrawable(R.drawable.seafood);
                break;
            case "Side":
                drawable = context.getResources().getDrawable(R.drawable.side);
                break;
            case "Starter":
                drawable = context.getResources().getDrawable(R.drawable.starter);
                break;
            case "Vegan":
                drawable = context.getResources().getDrawable(R.drawable.vegan);
                break;
            case "Vegetarian":
                drawable = context.getResources().getDrawable(R.drawable.vegetarian);
                break;
        }
        return drawable;
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryBinding binding;

        public ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

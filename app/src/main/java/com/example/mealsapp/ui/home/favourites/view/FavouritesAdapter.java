package com.example.mealsapp.ui.home.favourites.view;

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
import com.example.mealsapp.databinding.ItemMealBinding;
import com.example.mealsapp.model.pojo.meal.Meal;

public class FavouritesAdapter extends ListAdapter<Meal, FavouritesAdapter.ViewHolder> {
    private Context context;
    private OnMealClickListener clickListener;

    public FavouritesAdapter(Context context, OnMealClickListener clickListener) {
        super(new DiffUtil.ItemCallback<Meal>() {
            @Override
            public boolean areItemsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return oldItem.getIdMeal().equals(newItem.getIdMeal());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return oldItem.getIdMeal().equals(newItem.getIdMeal()) &&
                        oldItem.isFavorite() == newItem.isFavorite();
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
        return getCurrentList().size();
    }

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
        void OnFavouriteClick(Meal meal);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemMealBinding binding;

        public ViewHolder(ItemMealBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Meal meal, OnMealClickListener clickListener, Context context) {
            binding.tvMealName.setText(meal.getStrMeal());
            binding.tvCategory.setText(meal.getStrCategory());
            Glide.with(binding.getRoot())
                    .load(meal.getStrMealThumb())
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivMeal);

            binding.ivSaveMeal.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_filled));

            binding.cvMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onMealClick(meal);
                }
            });

            binding.ivSaveMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.OnFavouriteClick(meal);
                }
            });
        }

        public static ViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemMealBinding binding = ItemMealBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(binding);
        }
    }
}


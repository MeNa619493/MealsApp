package com.example.mealsapp.ui.home.home.view;

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
import java.util.Objects;

public class MealsAdapter extends ListAdapter<Meal, MealsAdapter.ViewHolder> {
    private Context context;
    private OnMealClickListener clickListener;

    public MealsAdapter(Context context ,OnMealClickListener clickListener) {
        super(new DiffUtil.ItemCallback<Meal>() {
            @Override
            public boolean areItemsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return Objects.equals(oldItem.getIdMeal(), newItem.getIdMeal());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return oldItem.getStrMeal().equals(newItem.getStrMeal())
                        && oldItem.getStrCategory().equals(newItem.getStrCategory());
            }
        });
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMealBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvMealName.setText(getItem(position).getStrMeal());
        holder.binding.tvCategory.setText(getItem(position).getStrCategory());
        Glide.with(holder.binding.getRoot())
                .load(getItem(position).getStrMealThumb())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.ivMeal);

        if (getItem(position).isFavorite()){
            holder.binding.ivSaveMeal.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_filled));
        } else {
            holder.binding.ivSaveMeal.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark));
        }

        holder.binding.cvMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onMealClick(getItem(position));
            }
        });

        holder.binding.ivSaveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.OnFavouriteClick(getItem(position));
            }
        });
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
    }
}

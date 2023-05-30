package com.example.mealsapp.ui.home.calender.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealsapp.R;
import com.example.mealsapp.databinding.ItemPlannedMealBinding;
import com.example.mealsapp.model.pojo.meal.Meal;
import com.example.mealsapp.model.pojo.meal.PlannedMeal;

public class PlannedAdapter extends ListAdapter<PlannedMeal, PlannedAdapter.ViewHolder> {
    private OnMealClickListener clickListener;

    public PlannedAdapter(OnMealClickListener clickListener) {
        super(new DiffUtil.ItemCallback<PlannedMeal>() {
            @Override
            public boolean areItemsTheSame(@NonNull PlannedMeal oldItem, @NonNull PlannedMeal newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull PlannedMeal oldItem, @NonNull PlannedMeal newItem) {
                return oldItem.getId().equals(newItem.getId());
            }
        });
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    public interface OnMealClickListener {
        void onMealClick(Meal meal);

        void OnDeleteClick(PlannedMeal plannedMeal);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPlannedMealBinding binding;

        public ViewHolder(ItemPlannedMealBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PlannedMeal plannedMeal, OnMealClickListener clickListener) {
            binding.tvMealName.setText(plannedMeal.getMeal().getStrMeal());
            binding.tvCategory.setText(plannedMeal.getMeal().getStrCategory());
            Glide.with(binding.getRoot())
                    .load(plannedMeal.getMeal().getStrMealThumb())
                    .placeholder(R.drawable.placeholder)
                    .into(binding.ivMeal);

            binding.cvMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onMealClick(plannedMeal.getMeal());
                }
            });

            binding.ivDeleteMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.OnDeleteClick(plannedMeal);
                }
            });
        }

        public static ViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemPlannedMealBinding binding = ItemPlannedMealBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(binding);
        }
    }
}


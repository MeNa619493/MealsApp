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
import com.example.mealsapp.databinding.ItemCountryBinding;
import com.example.mealsapp.model.pojo.country.Country;

import java.util.Objects;

public class CountriesAdapter extends ListAdapter<Country, CountriesAdapter.ViewHolder> {
    private Context context;
    private OnCountryClickListener clickListener;

    public CountriesAdapter(Context context, OnCountryClickListener clickListener) {
        super(new DiffUtil.ItemCallback<Country>() {
            @Override
            public boolean areItemsTheSame(@NonNull Country oldItem, @NonNull Country newItem) {
                return Objects.equals(oldItem.getStrArea(), newItem.getStrArea());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Country oldItem, @NonNull Country newItem) {
                return oldItem.getStrArea().equals(newItem.getStrArea());
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

    public interface OnCountryClickListener {
        void onCountryClick(Country country);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCountryBinding binding;

        public ViewHolder(ItemCountryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Country country, OnCountryClickListener clickListener, Context context) {
            binding.tvName.setText(country.getStrArea());
            binding.ivImage.setImageDrawable(getItemImage(country.getStrArea(), context));
            binding.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onCountryClick(country);
                }
            });
        }

        private Drawable getItemImage(String name, Context context) {
            switch (name) {
                case "American":
                    return context.getResources().getDrawable(R.drawable.american);
                case "British":
                    return context.getResources().getDrawable(R.drawable.british);
                case "Canadian":
                    return context.getResources().getDrawable(R.drawable.canadian);
                case "Chinese":
                    return context.getResources().getDrawable(R.drawable.chinese);
                case "Croatian":
                    return context.getResources().getDrawable(R.drawable.croatian);
                case "Dutch":
                    return context.getResources().getDrawable(R.drawable.dutch);
                case "Egyptian":
                    return context.getResources().getDrawable(R.drawable.egyptian);
                case "Filipino":
                    return context.getResources().getDrawable(R.drawable.filipino);
                case "French":
                    return context.getResources().getDrawable(R.drawable.french);
                case "Greek":
                    return context.getResources().getDrawable(R.drawable.greek);
                case "Indian":
                    return context.getResources().getDrawable(R.drawable.indian);
                case "Irish":
                    return context.getResources().getDrawable(R.drawable.irish);
                case "Italian":
                    return context.getResources().getDrawable(R.drawable.italian);
                case "Jamaican":
                    return context.getResources().getDrawable(R.drawable.jamaican);
                case "Japanese":
                    return context.getResources().getDrawable(R.drawable.japanese);
                case "Kenyan":
                    return context.getResources().getDrawable(R.drawable.kenyan);
                case "Malaysian":
                    return context.getResources().getDrawable(R.drawable.malaysian);
                case "Mexican":
                    return context.getResources().getDrawable(R.drawable.mexican);
                case "Moroccan":
                    return context.getResources().getDrawable(R.drawable.morocco);
                case "Polish":
                    return context.getResources().getDrawable(R.drawable.poland);
                case "Portuguese":
                    return context.getResources().getDrawable(R.drawable.portuguese);
                case "Russian":
                    return context.getResources().getDrawable(R.drawable.russian);
                case "Spanish":
                    return context.getResources().getDrawable(R.drawable.spain);
                case "Thai":
                    return context.getResources().getDrawable(R.drawable.thai);
                case "Tunisian":
                    return context.getResources().getDrawable(R.drawable.tunisia);
                case "Turkish":
                    return context.getResources().getDrawable(R.drawable.turkish);
                case "Vietnamese":
                    return context.getResources().getDrawable(R.drawable.vietnamese);
                default:
                    return context.getResources().getDrawable(R.drawable.unknown);
            }
        }

        public static ViewHolder from(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ItemCountryBinding binding = ItemCountryBinding.inflate(layoutInflater, parent, false);
            return new ViewHolder(binding);
        }
    }
}


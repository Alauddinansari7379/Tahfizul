package com.amtech.tahfizulquranonline.V2Data;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.V2Data.city.CityItem;
import com.amtech.tahfizulquranonline.V2Data.countryModel.CountyItem;
import com.amtech.tahfizulquranonline.V2Data.state.StateItem;
import com.amtech.tahfizulquranonline.v2.TalibRegisterFragment;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.viewHolder> {
    Context context;
    List<CityItem> countryModelList;
    TalibRegisterFragment talibRegisterFragment;

    public CityAdapter(Context context, List<CityItem> countryModelList, TalibRegisterFragment talibRegisterFragment) {
        this.context = context;
        this.countryModelList = countryModelList;
        this.talibRegisterFragment = talibRegisterFragment;

    }

    @NonNull
    @Override
    public CityAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityAdapter.viewHolder(LayoutInflater.from(context).inflate(R.layout.country_item_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.viewHolder holder, int position) {
        holder.country.setText(countryModelList.get(position).getCityName().toString());
        holder.itemView.setOnClickListener(v -> {
            talibRegisterFragment.citySelect(countryModelList.get(position).getId(), countryModelList.get(position).getCityName());
        });
    }

    @Override
    public int getItemCount() {
        return countryModelList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView country;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.stateItem);
        }
    }

    public void filteredList(ArrayList<CityItem> filteredPlacesArrayList) {
        countryModelList = filteredPlacesArrayList;
        notifyDataSetChanged();
    }
}

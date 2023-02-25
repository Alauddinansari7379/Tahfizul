package com.amtech.tahfizulquranonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.V2Data.StateAdapter;
import com.amtech.tahfizulquranonline.V2Data.state.StateItem;
import com.amtech.tahfizulquranonline.v2.TalibRegisterFragment;
import com.amtech.tahfizulquranonline.v2.countryCode.CountryCodeModel;

import java.util.ArrayList;
import java.util.List;

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.viewHolder> {
    Context context;
    List<CountryCodeModel> countryModelList;
    TalibRegisterFragment talibRegisterFragment;

    public CountryCodeAdapter(Context context, List<CountryCodeModel> countryModelList, TalibRegisterFragment talibRegisterFragment) {
        this.context = context;
        this.countryModelList = countryModelList;
        this.talibRegisterFragment = talibRegisterFragment;
    }

    @NonNull
    @Override
    public CountryCodeAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CountryCodeAdapter.viewHolder(LayoutInflater.from(context).inflate(R.layout.country_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CountryCodeAdapter.viewHolder holder, int position) {
//
        holder.country.setText("+" + countryModelList.get(position).getName());
        holder.name.setText("+" + countryModelList.get(position).getPhonecode());
        holder.itemView.setOnClickListener(v -> {
            talibRegisterFragment.getCountryData(countryModelList.get(position).getId(),countryModelList.get(position).getPhonecode());
        });
    }

    @Override
    public int getItemCount() {
        return countryModelList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView country, name;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.stateItem);
            name = itemView.findViewById(R.id.name);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void filteredList(List<CountryCodeModel> filteredPlacesArrayList) {
        countryModelList = filteredPlacesArrayList;
        notifyDataSetChanged();
    }
}

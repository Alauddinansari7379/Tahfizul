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
import com.amtech.tahfizulquranonline.V2Data.countryModel.CountryModel;
import com.amtech.tahfizulquranonline.V2Data.countryModel.CountyItem;
import com.amtech.tahfizulquranonline.v2.TalibRegisterFragment;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.viewHolder> {
    Context context;
    List<CountyItem> countryModelList;
    List<CountyItem> copyList = new ArrayList<>();
    TalibRegisterFragment talibRegisterFragment;

    public CountryAdapter(Context context, List<CountyItem> countryModelList,TalibRegisterFragment talibRegisterFragment) {
        this.context = context;
        this.countryModelList = countryModelList;
        this.talibRegisterFragment =talibRegisterFragment;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.country_item_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.country.setText(countryModelList.get(position).getC_name().toString());
        holder.itemView.setOnClickListener(v -> {
            talibRegisterFragment.countrySelect(countryModelList.get(position).getId(),countryModelList.get(position).getC_name());
        });    }

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


    public void filteredList(ArrayList<CountyItem> filteredPlacesArrayList) {
        countryModelList = filteredPlacesArrayList;
        notifyDataSetChanged();
    }
}

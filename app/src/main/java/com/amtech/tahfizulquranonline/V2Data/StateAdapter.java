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
import com.amtech.tahfizulquranonline.V2Data.countryModel.CountyItem;
import com.amtech.tahfizulquranonline.V2Data.state.StateItem;
import com.amtech.tahfizulquranonline.v2.TalibRegisterFragment;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.viewHolder> {
    Context context;
    List<StateItem> stateItemList;
    List<StateItem> copyList = new ArrayList<>();
    TalibRegisterFragment talibRegisterFragment;

    public StateAdapter(Context context, List<StateItem> stateItemList, TalibRegisterFragment talibRegisterFragment) {
        this.context = context;
        this.stateItemList = stateItemList;
        this.talibRegisterFragment = talibRegisterFragment;

    }

    @NonNull
    @Override
    public StateAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StateAdapter.viewHolder(LayoutInflater.from(context).inflate(R.layout.country_item_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull StateAdapter.viewHolder holder, int position) {
        holder.country.setText(stateItemList.get(position).getStateName().toString());
        holder.itemView.setOnClickListener(v -> {
            talibRegisterFragment.stateSelect(stateItemList.get(position).getId(),stateItemList.get(position).getStateName());
        });
    }

    @Override
    public int getItemCount() {
        return stateItemList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView country;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.stateItem);
        }
    }

    public void filteredList(ArrayList<StateItem> filteredPlacesArrayList) {
        stateItemList = filteredPlacesArrayList;
        notifyDataSetChanged();
    }
}

package com.amtech.tahfizulquranonline.v2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.adapter.MoulimListAdapter;
import com.amtech.tahfizulquranonline.databinding.ListOfMoulimsRcItemBinding;
import com.amtech.tahfizulquranonline.databinding.MoulimProfileItemBinding;
import com.amtech.tahfizulquranonline.v2.M.MoulimsItems;
import com.amtech.tahfizulquranonline.v2.moulimProfile.MProfileItem;

import java.util.ArrayList;
import java.util.List;

public class MoulimProfileAdapter extends RecyclerView.Adapter<MoulimProfileAdapter.viewHolder> {
    Context context;
    List<MProfileItem> list = new ArrayList<>();
    MoulimProfileActivity profileActivity;
    int currentPosition = -1;

    public MoulimProfileAdapter(Context context, List<MProfileItem> list, MoulimProfileActivity profileActivity) {
        this.context = context;
        this.list = list;
        this.profileActivity = profileActivity;
    }

    private RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        MoulimProfileItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.moulim_profile_item, group, false);
        return new MoulimProfileAdapter.viewHolder(binding);
    }

    @NonNull
    @Override
    public MoulimProfileAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (MoulimProfileAdapter.viewHolder) getViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MoulimProfileAdapter.viewHolder holder, int position) {
        MoulimProfileAdapter.viewHolder holder1 = (MoulimProfileAdapter.viewHolder) holder;
        holder.binding.timingLb1.setText(list.get(position).getName());
        holder.binding.fromTimeTxt1.setText(list.get(position).getStartTime());
        holder.binding.toTimeTxt1.setText(list.get(position).getEndTime());
        // holder.binding.profileImg.setImageResource(list.get(position).getImage());
        holder.setDataBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        MoulimProfileItemBinding binding;

        public viewHolder(@NonNull MoulimProfileItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(int position) {
            if (currentPosition == position) {
                binding.switchBtn1.setChecked(true);
            } else {
                binding.switchBtn1.setChecked(false);
            }
            binding.timingLy1.setOnClickListener(view -> {
                profileActivity.setSelected(list.get(position).getCourseId(),list.get(position).getId());
                currentPosition = position;
                notifyDataSetChanged();
            });
        }
    }


}

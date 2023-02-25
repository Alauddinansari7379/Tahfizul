package com.amtech.tahfizulquranonline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.databinding.ListOfMoulimsRcItemBinding;
import com.amtech.tahfizulquranonline.models.MoulimListModel;
import com.amtech.tahfizulquranonline.v2.M.MoulimsItems;
import com.amtech.tahfizulquranonline.v2.MoulimProfileActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MoulimListAdapter extends RecyclerView.Adapter<MoulimListAdapter.viewHolder> {
    Context context;
    List<MoulimsItems> list = new ArrayList<>();

    public MoulimListAdapter(Context context, List<MoulimsItems> list) {
        this.context = context;
        this.list = list;
    }

    private RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        ListOfMoulimsRcItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_of_moulims_rc_item, group, false);
        return new MoulimListAdapter.viewHolder(binding);
    }

    @NonNull
    @Override
    public MoulimListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (MoulimListAdapter.viewHolder) getViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MoulimListAdapter.viewHolder holder, int position) {
        MoulimListAdapter.viewHolder holder1 = (MoulimListAdapter.viewHolder) holder;
        holder.binding.moulimNamelb.setText(list.get(position).getName());
        // holder.binding.profileImg.setImageResource(list.get(position).getImage());
        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .transform(new RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                ;
        Glide.with(context)
                .load("https://www.tahfizulquranonline.com/uploads/"+list.get(position).getImage())
                .thumbnail(/*sizeMultiplier=*/ 0.25f)
                .apply(reqOpt)
                .placeholder(R.drawable.moulim).error(R.drawable.moulim)
                .into(holder.binding.profileImg);
        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, MoulimProfileActivity.class)
                    .putExtra("name", list.get(position).getName())
                    .putExtra("about", list.get(position).getAbout())
                    .putExtra("exp", list.get(position).getExperience())
                    .putExtra("id", list.get(position).getId())
                    .putExtra("profile", list.get(position).getImage()));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ListOfMoulimsRcItemBinding binding;

        public viewHolder(@NonNull ListOfMoulimsRcItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

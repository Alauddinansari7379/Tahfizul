package com.amtech.tahfizulquranonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.databinding.AllPaymentRcItemBinding;
import com.amtech.tahfizulquranonline.v2.payment.DataItem;
import com.amtech.tahfizulquranonline.v2.payment.PaymentResponse;

import java.util.List;

public class AllPaymentAdapter extends RecyclerView.Adapter<AllPaymentAdapter.viewHolder> {
    Context context;
    List<DataItem> allPaymentModelList;

    public AllPaymentAdapter(Context context, List<DataItem> allPaymentModelList) {
        this.context = context;
        this.allPaymentModelList = allPaymentModelList;
    }

    private RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        AllPaymentRcItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.all_payment_rc_item, group, false);
        return new AllPaymentAdapter.viewHolder(binding);
    }

    @NonNull
    @Override
    public AllPaymentAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (viewHolder) getViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPaymentAdapter.viewHolder holder, int position) {
        holder.binding.amountTxt.setText(allPaymentModelList.get(position).getAmount());
        holder.binding.payTypeTxt.setText(allPaymentModelList.get(position).getPayType());
        holder.binding.trandateTxt.setText(allPaymentModelList.get(position).getTrxDate());
        holder.binding.tranIdTxt.setText(allPaymentModelList.get(position).getTxId());
    }

    @Override
    public int getItemCount() {
        return allPaymentModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        AllPaymentRcItemBinding binding;

        public viewHolder(AllPaymentRcItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

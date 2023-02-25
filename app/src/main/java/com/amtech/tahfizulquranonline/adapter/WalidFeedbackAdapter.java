package com.amtech.tahfizulquranonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.models.FeedBackModel;

import java.util.List;

public class WalidFeedbackAdapter extends RecyclerView.Adapter<WalidFeedbackAdapter.ViewHolder> {

    private static final String TAG = "WalidFeedbackAdapter";
    private Context mContext;
    private List<FeedBackModel> mList;

    public WalidFeedbackAdapter(Context mContext, List<FeedBackModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feedback_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedBackModel model = mList.get(position);
        holder.maulimNameTxt.setText(model.getName());
        holder.feedbackTxt.setText(model.getFeedBack());
        holder.ratingBar.setRating(Float.parseFloat(model.getRating()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView maulimNameTxt, feedbackTxt;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            maulimNameTxt = itemView.findViewById(R.id.maulim_name_txt);
            feedbackTxt = itemView.findViewById(R.id.feedback_txt);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }
}

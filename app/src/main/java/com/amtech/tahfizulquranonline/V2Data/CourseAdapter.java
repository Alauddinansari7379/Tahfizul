package com.amtech.tahfizulquranonline.V2Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.V2Data.courseModel.CourseItem;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.viewHolder> {
    Context context;
    List<CourseItem> courseItemList;

    public CourseAdapter(Context context, List<CourseItem> courseItemList) {
        this.context = context;
        this.courseItemList = courseItemList;
    }

    @NonNull
    @Override
    public CourseAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseAdapter.viewHolder(LayoutInflater.from(context).inflate(R.layout.country_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.viewHolder holder, int position) {
        holder.course.setText(courseItemList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return courseItemList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView course;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.stateItem);
        }
    }
}

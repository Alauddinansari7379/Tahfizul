package com.amtech.tahfizulquranonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.models.StudentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shourav Paul on 31-12-2021.
 **/
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<StudentModel> studentList = new ArrayList<>();
    private Context context;
    private OnStudentItemClickListener listener;

    public StudentAdapter(List<StudentModel> studentList, Context context, OnStudentItemClickListener listener) {
        this.studentList = studentList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel model = studentList.get(position);
        holder.studentNameTxt.setText(model.getName());
        holder.stdntItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView studentNameTxt;
        private LinearLayout stdntItemLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTxt = (TextView) itemView.findViewById(R.id.talib_name_txt);
            stdntItemLayout = (LinearLayout)itemView.findViewById(R.id.student_item);
        }
    }
    public interface OnStudentItemClickListener
    {
        void onClick(int pos);
    }
}

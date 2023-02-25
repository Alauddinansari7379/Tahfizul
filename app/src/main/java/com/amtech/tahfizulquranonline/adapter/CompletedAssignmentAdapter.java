package com.amtech.tahfizulquranonline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.activity.MaulimCompleteAssignmentActivity;
import com.amtech.tahfizulquranonline.models.AssignmentModel;

import java.util.ArrayList;
import java.util.List;

import static com.amtech.tahfizulquranonline.fragment.CompletedAssignFragment.appListStatus;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;

/**
 * Created by Shourav Paul on 03-01-2022.
 **/
public class CompletedAssignmentAdapter extends RecyclerView.Adapter<CompletedAssignmentAdapter.ViewHolder> {

    private Context context;
    private List<AssignmentModel> list = new ArrayList<>();

    public CompletedAssignmentAdapter(Context context, List<AssignmentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.completed_assign_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssignmentModel model = list.get(position);
        holder.assignDateTxt.setText(model.getAssignDate());
        holder.nameTxt.setText(model.getName());
        if(model.getDesc().length() > 60)
        {
            holder.assignDescTxt.setText(model.getDesc().substring(0, 60));
        }
        else
        {
            holder.assignDescTxt.setText(model.getDesc());
        }

        holder.assignmentNameTxt.setText("ASSIGNMENT-"+(position+1));
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!maulimID.equals(""))
                {
                    if(appListStatus.equals("3"))
                    {
                        Intent intent = new Intent(context, MaulimCompleteAssignmentActivity.class);
                        intent.putExtra("id", model.getId());
                        intent.putExtra("assign_name", "ASSIGNMENT-"+(position+1));
                        intent.putExtra("name", model.getName());
                        intent.putExtra("assign_date", model.getAssignDate());
                        intent.putExtra("assign_des", model.getDesc());
                        intent.putExtra("file_link", model.getFile());
                        intent.putExtra("talib_response", model.getTalibResponse());
                        intent.putExtra("talib_file_link", model.getTalibFile());
                        intent.putExtra("submission_date", model.getSubmissionDate());
                        intent.putExtra("type", "editable");
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                    else
                    {
                        Intent intent = new Intent(context, MaulimCompleteAssignmentActivity.class);
                        intent.putExtra("id", model.getId());
                        intent.putExtra("assign_name", "ASSIGNMENT-"+(position+1));
                        intent.putExtra("name", model.getName());
                        intent.putExtra("assign_date", model.getAssignDate());
                        intent.putExtra("assign_des", model.getDesc());
                        intent.putExtra("file_link", model.getFile());
                        intent.putExtra("talib_response", model.getTalibResponse());
                        intent.putExtra("talib_file_link", model.getTalibFile());
                        intent.putExtra("submission_date", model.getSubmissionDate());
                        intent.putExtra("maulim_remark_on_comp", model.getMaulimRemarkOnComp());
                        context.startActivity(intent);
//                        ((Activity)context).finish();
                    }

                }
                else
                {
                    Intent intent = new Intent(context, MaulimCompleteAssignmentActivity.class);
                    intent.putExtra("id", model.getId());
                    intent.putExtra("assign_name", "ASSIGNMENT-"+(position+1));
                    intent.putExtra("name", model.getName());
                    intent.putExtra("assign_date", model.getAssignDate());
                    intent.putExtra("assign_des", model.getDesc());
                    intent.putExtra("file_link", model.getFile());
                    intent.putExtra("talib_response", model.getTalibResponse());
                    intent.putExtra("talib_file_link", model.getTalibFile());
                    intent.putExtra("submission_date", model.getSubmissionDate());
                    intent.putExtra("maulim_remark_on_comp", model.getMaulimRemarkOnComp());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView assignDescTxt, nameTxt, assignDateTxt, assignmentNameTxt;
        private LinearLayout itemLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assignDescTxt = (TextView)itemView.findViewById(R.id.desc_txt);
            nameTxt = (TextView)itemView.findViewById(R.id.name_txt);
            assignDateTxt = (TextView)itemView.findViewById(R.id.assign_date);
            assignmentNameTxt = (TextView)itemView.findViewById(R.id.assignment_name_txt);
            itemLayout = (LinearLayout)itemView.findViewById(R.id.assign_item);
        }
    }
}

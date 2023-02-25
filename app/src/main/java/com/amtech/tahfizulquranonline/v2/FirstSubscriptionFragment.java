package com.amtech.tahfizulquranonline.v2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.V2Data.plan.PlanItem;

public class FirstSubscriptionFragment extends Fragment {
    PlanItem planItem;

    public FirstSubscriptionFragment(PlanItem planItem) {
        this.planItem = planItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_subscription, container, false);
        TextView amount = view.findViewById(R.id.amount);
        TextView day = view.findViewById(R.id.day);
        TextView days = view.findViewById(R.id.days);
        TextView description = view.findViewById(R.id.description);
        amount.setText(planItem.getPrice() + "₹");
        days.setText("(" + planItem.getDays() + " Days Plan)");
        description.setText(planItem.getDescription());
        day.setText(planItem.getName() + " Plan");
        return view;
    }
}
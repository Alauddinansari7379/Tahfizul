package com.amtech.tahfizulquranonline.adapter;

import static com.amtech.tahfizulquranonline.utils.AppConstants.TALIB;
import static com.amtech.tahfizulquranonline.utils.AppConstants.loginUser;
import static com.amtech.tahfizulquranonline.utils.AppConstants.maulimID;
import static com.amtech.tahfizulquranonline.utils.AppConstants.talibID;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.activity.JitsiCallActivity;
import com.amtech.tahfizulquranonline.models.SlotItem;
import com.amtech.tahfizulquranonline.utils.AppConstants;
import com.amtech.tahfizulquranonline.utils.Constant;
import com.amtech.tahfizulquranonline.utils.MyTimeCalculator;
import com.amtech.tahfizulquranonline.utils.TimeDiffFinder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class TalibSlotAdapter extends RecyclerView.Adapter<TalibSlotAdapter.ViewHolder> {

    private Context context;
    private List<SlotItem> slotList;
    private static final String TAG = "MaulemSlotsAdapter";

    public TalibSlotAdapter(Context context, List<SlotItem> slotList)
    {
        this.context = context;
        this.slotList = slotList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slot_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SlotItem model = slotList.get(position);
        holder.slotNameTxt.setText("SLOT "+(position+1));
        holder.timingTxt.setText(model.getStartTime()+" TO "+model.getEndTime());
        holder.subTxt.setText(model.getDesc());


//        if(model.getStatus().equals("1"))
//        {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            {
//                holder.statusIcon.setColorFilter(context.getResources().getColor(R.color.colorGreen, context.getTheme()));
//            }
//            else
//            {
//                holder.statusIcon.setColorFilter(context.getResources().getColor(R.color.colorGreen));
//            }
//
//        }
        //Start Time
        String[] stTimeArr = model.getStartTime().split(" ");
        String[] s = stTimeArr[0].split(":");
        String hrFrag = s[0].length() == 1 ? "0"+s[0] : s[0];
        if(stTimeArr[1].equals("PM"))
        {
            if(!hrFrag.equals("12"))
            {
                int hrInt = Integer.parseInt(hrFrag) + 12;
                hrFrag = String.valueOf(hrInt);
            }
        }
        else
        {
            if(hrFrag.equals("12"))
            {
                hrFrag = "00";
            }
        }
        String newTimeFormat = hrFrag+":"+s[1];
        String stTimeStr = newTimeFormat+":"+"00";
        //End Time
        String[] edTimeArr = model.getEndTime().split(" ");
        String[] s2 = edTimeArr[0].split(":");
        String hrFrag2 = s2[0].length() == 1 ? "0"+s2[0] : s2[0];
        if(edTimeArr[1].equals("PM"))
        {
            if(!hrFrag2.equals("12"))
            {
                int hrInt = Integer.parseInt(hrFrag2) + 12;
                hrFrag2 = String.valueOf(hrInt);
            }
        }
        else
        {
            if(hrFrag2.equals("12"))
            {
                hrFrag2 = "00";
            }
        }
        String newTimeFormat2 = hrFrag2+":"+s2[1];
        String edTimeStr = newTimeFormat2+":"+"00";
        //
        String maulimId;
        String talibId;
        if(loginUser == TALIB)
        {
            maulimId = model.getMualimId();
            talibId = talibID;
        }
        else
        {
            maulimId = maulimID;
            talibId = model.getTalibIlmId();
        }
        if(checkIfActive(model.getDate(), stTimeStr, edTimeStr))
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                holder.statusIcon.setColorFilter(context.getResources().getColor(R.color.colorGreen, context.getTheme()));
            }
            else
            {
                holder.statusIcon.setColorFilter(context.getResources().getColor(R.color.colorGreen));
            }
        }
        holder.slotBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions(model.getDate(), stTimeStr, edTimeStr, slotList.get(position).getMualimId(), talibId);


            }
        });
    }
    private boolean checkIfActive(String strDate, String startTime, String endTime)
    {
        if(new MyTimeCalculator().inTimeFrame(strDate, startTime, endTime))//check appointment date n time with current date n time
        {
            long diff = new TimeDiffFinder().getTimeDiff(strDate, endTime);
            int secLeft = (int) (diff/1000.0f);
            if(secLeft <= 60)
            {
                Toast.makeText(context, "This class will end in "+secLeft+" sec", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            return false;
        }
        return true;
    }
    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView slotNameTxt, timingTxt, subTxt;
        private CardView slotBody;
        private ImageView statusIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slotNameTxt = (TextView) itemView.findViewById(R.id.slot_name);
            timingTxt = (TextView) itemView.findViewById(R.id.timing);
            subTxt = (TextView) itemView.findViewById(R.id.subject);
            slotBody = (CardView)itemView.findViewById(R.id.slot_body);
            statusIcon = (ImageView)itemView.findViewById(R.id.status_icon);
        }
    }
    private void checkPermissions(String bookDate, String startTime, String endTime, String maulimId, String talibId)
    {
        Dexter.withActivity((Activity)context)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        callAction(bookDate, startTime, endTime, maulimId, talibId);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void callAction(String strDate, String startTime, String endTime, String maulimId, String talibId)
    {
        if(new MyTimeCalculator().inTimeFrame(strDate, startTime, endTime))//check appointment date n time with current date n time
        {
            long diff = new TimeDiffFinder().getTimeDiff(strDate, endTime);
            int secLeft = (int) (diff/1000.0f);
            if(secLeft > 0)
            {
                Intent intent = new Intent(context, JitsiCallActivity.class);
                intent.putExtra("maulim_id", maulimId);
                intent.putExtra("talib_id", talibID);
                intent.putExtra("time_left", secLeft);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
            else
            {
                Toast.makeText(context, "Your session is over!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            long diff = new TimeDiffFinder().getTimeDiff(strDate, endTime);
            int secLeft = (int) (diff/1000.0f);
            Log.i(TAG, "callAction: date : "+strDate+" endTime : "+endTime);
            Log.i(TAG, "callAction: secLeft"+secLeft);
            if(secLeft > 0)
            {
                Toast.makeText(context, "Class will begin at the specified time!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "This Class is over!", Toast.LENGTH_SHORT).show();
            }
        }

        //////////////////////

    }
}

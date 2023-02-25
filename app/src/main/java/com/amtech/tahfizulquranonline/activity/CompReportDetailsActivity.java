package com.amtech.tahfizulquranonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amtech.tahfizulquranonline.R;

import java.io.IOException;

public class CompReportDetailsActivity extends AppCompatActivity {

    private String id, assignmentName, name, maulimName, assignDate, assignDesc, fileLink, talibFileLink, talibResponse, submissionDate, maulimRemarks, reportComment, assignType, assignStatus;
    private TextView assignmentNameTxt, assignNameTxt, assignDateTxt, nameTxt, maulimNameTxt, assignDescTxt, listenAudioBtn, listenAudioTalibBTn, talibResponseTxt, submissionDateTxt, maulimRemarksTxt, reportCommnetTxt, assignTypeTxt, assignStatusTxt;
    private MediaPlayer mediaPlayer, tMediaPlayer;
    private EditText assignResponseEdTxt, maulimRemarkEdTxt;
    private AppCompatButton submitBtn;
    private ProgressDialog progressDialog;
    private String assignNote;
    private static final String TAG = "CompReportDetailsActivi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_report_details);
        initView();
        assignmentName = getIntent().getStringExtra("assign_name");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        maulimName = getIntent().getStringExtra("maulim_name");
        assignDate = getIntent().getStringExtra("assign_date");
        assignDesc = getIntent().getStringExtra("assign_des");
        fileLink = getIntent().getStringExtra("file_link");
        talibFileLink = getIntent().getStringExtra("talib_file_link");
        submissionDate = getIntent().getStringExtra("submission_date");
        talibResponse = getIntent().getStringExtra("talib_response");
        maulimRemarks = getIntent().getStringExtra("remarks");
        reportComment = getIntent().getStringExtra("report_comment");
        assignType = getIntent().getStringExtra("assign_type");
        assignStatus = getIntent().getStringExtra("assign_status");
        if(assignStatus != null)
        {
            if(assignStatus.equals("PENDING"))
                maulimRemarksTxt.setVisibility(View.GONE);
        }
        if(submissionDate == null)
        {
                submissionDateTxt.setVisibility(View.GONE);
        }
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            onBackPressed();
        });
        //initMediaPlayer();
        setListener();
        setData();
    }
    private void initView()
    {
        //assignmentNameTxt = (TextView)findViewById(R.id.assignment_name_txt);
        assignNameTxt = (TextView)findViewById(R.id.student_name_txt);
        maulimNameTxt = (TextView)findViewById(R.id.maulim_name_txt);
        assignDateTxt = (TextView)findViewById(R.id.assign_date_txt);
        //nameTxt = (TextView)findViewById(R.id.name_txt);
        assignDescTxt = (TextView)findViewById(R.id.assign_desc_txt);
        submissionDateTxt = (TextView)findViewById(R.id.submitted_date_txt);
        maulimRemarksTxt = (TextView)findViewById(R.id.remark_txt);

//        listenAudioBtn = (TextView)findViewById(R.id.listen_audio_btn);
//        listenAudioTalibBTn = (TextView)findViewById(R.id.listen_talib_audio_btn);
//        submitBtn = (AppCompatButton)findViewById(R.id.submit_btn);
//        assignResponseEdTxt = (EditText)findViewById(R.id.maulim_remark_ed_txt);
        talibResponseTxt = (TextView) findViewById(R.id.submission_txt);
        reportCommnetTxt = (TextView)findViewById(R.id.report_comment_txt);
        assignTypeTxt = (TextView)findViewById(R.id.assign_type_txt);
        assignStatusTxt = (TextView)findViewById(R.id.assign_status_txt);


    }
    private void setListener()
    {
//        listenAudioBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer.start();
//            }
//        });
//        listenAudioTalibBTn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tMediaPlayer.start();
//            }
//        });
    }
    private void setData()
    {
        //assignmentNameTxt.setText("Student Name : "+assignmentName);
        if(name != null)
            assignNameTxt.setText("Student Name : "+name);
        if(maulimName != null)
            maulimNameTxt.setText("Moulim : "+maulimName);
        if(assignDate != null)
            assignDateTxt.setText("Assign Date : "+assignDate);
        if(assignDesc != null)
            assignDescTxt.setText("About Assignment : "+assignDesc);
        if(talibResponse != null)
            talibResponseTxt.setText("Talib Response : "+talibResponse);
        if(submissionDate != null)
            submissionDateTxt.setText("Submission Date : "+submissionDate);
        if(maulimRemarks != null)
            maulimRemarksTxt.setText("Maulim Remarks : "+maulimRemarks);
        if(assignStatus != null)
            assignStatusTxt.setText("Status : "+assignStatus);
        if(assignType != null)
            assignTypeTxt.setText("Assignment Type : "+assignType);
        if(reportComment != null)
            reportCommnetTxt.setText("Report Remarks : "+reportComment);
    }
    private void initMediaPlayer()
    {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(fileLink);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tMediaPlayer = new MediaPlayer();
        tMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            tMediaPlayer.setDataSource(talibFileLink);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
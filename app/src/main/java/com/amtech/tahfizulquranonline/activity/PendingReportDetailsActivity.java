package com.amtech.tahfizulquranonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.utils.MyTimeCalculator;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.amtech.tahfizulquranonline.utils.AppConstants.generateReportUrl;

public class PendingReportDetailsActivity extends AppCompatActivity {

    private String id, assignmentName, name, maulimName, assignDate, assignDesc, fileLink, talibFileLink, talibResponse, submissionDate, maulimRemarks, assignType, assignStatus;
    private TextView assignmentNameTxt, assignNameTxt, assignDateTxt, nameTxt, maulimNameTxt, assignDescTxt, listenAudioBtn, listenAudioTalibBTn, talibResponseTxt, submissionDateTxt, maulimRemarksTxt, assignTypeTxt, assignStatusTxt;
    private MediaPlayer mediaPlayer, tMediaPlayer;
    private EditText assignResponseEdTxt, maulimRemarkEdTxt, finalRemarkForRepEdTxt;
    private AppCompatButton submitBtn;
    private ProgressDialog progressDialog;
    private String assignNote;
    private static final String TAG = "CompReportDetailsActivi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_report_details);
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
        //initMediaPlayer();
        setListener();
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            onBackPressed();
        });
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
        finalRemarkForRepEdTxt = (EditText)findViewById(R.id.final_remark_for_report_edtxt);
//        listenAudioBtn = (TextView)findViewById(R.id.listen_audio_btn);
//        listenAudioTalibBTn = (TextView)findViewById(R.id.listen_talib_audio_btn);
//        submitBtn = (AppCompatButton)findViewById(R.id.submit_btn);
//        assignResponseEdTxt = (EditText)findViewById(R.id.maulim_remark_ed_txt);
        talibResponseTxt = (TextView) findViewById(R.id.submission_txt);
        submitBtn = (AppCompatButton)findViewById(R.id.generate_rep_btn);
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
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!finalRemarkForRepEdTxt.getText().toString().isEmpty())
                {
                    generateReport();
                }
                else
                {
                    Toast.makeText(PendingReportDetailsActivity.this, "Enter Remark for Report!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        if(maulimRemarks != null) {
            maulimRemarksTxt.setText("Maulim Remarks : " + maulimRemarks);
        }
        if(assignStatus != null)
            assignStatusTxt.setText("Status : "+assignStatus);
        if(assignType != null)
            assignTypeTxt.setText("Assignment Type : "+assignType);
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
            tMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void generateReport()
    {
        progressDialog = ProgressDialog.show(PendingReportDetailsActivity.this, "", "Please wait...");
        StringRequest request = new StringRequest(Request.Method.POST, generateReportUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String res = response.trim();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    boolean status = jsonObject.getBoolean("status");
                    if(status)
                    {
                        Toast.makeText(PendingReportDetailsActivity.this, "Report Generated Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PendingReportDetailsActivity.this, MainActivity.class);
                        intent.putExtra("frag", "report_fragment");
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        //no record found
                        Toast.makeText(PendingReportDetailsActivity.this, "Failed to Generate Report!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: "+error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("assignment_id", id);
                params.put("reportremarks", finalRemarkForRepEdTxt.getText().toString().trim());
                params.put("submission_date", new MyTimeCalculator().getAddedDateInStr(0));
                Log.i(TAG, "getParams: "+params.toString());
                return params;

            }
        };
        RequestQueue queue = Volley.newRequestQueue(PendingReportDetailsActivity.this);
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PendingReportDetailsActivity.this, MainActivity.class);
        intent.putExtra("frag", "report_fragment");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        if (mediaPlayer!=null)
        {
            mediaPlayer.pause();
        }

        if (tMediaPlayer!=null)
        {
            tMediaPlayer.pause();
        }
        super.onPause();
    }
}
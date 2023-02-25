package com.amtech.tahfizulquranonline;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amtech.tahfizulquranonline.V2Data.ApiClient;
import com.amtech.tahfizulquranonline.V2Data.ApiInterface;
import com.amtech.tahfizulquranonline.activity.LoginActivity;
import com.amtech.tahfizulquranonline.v2.CreateModel;
import com.amtech.tahfizulquranonline.v2.PaymentActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity2 extends AppCompatActivity implements PaymentResultListener {
    CreateModel createModel;
    LoadingDialog loadingDialog;
    String txID = "";
    String todayDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        createModel = AppDelegate.getInstance().getCreateModel();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        todayDate = df.format(c);

        loadingDialog = new LoadingDialog(this);
        String samount = createModel.getAmount();
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_asIZcxruJr3uZf");
        checkout.setImage(R.drawable.active_dot);
        JSONObject object = new JSONObject();
        try {
            object.put("name", createModel.getName());
            object.put("description", "Tahfizul Quran Online Application");
            object.put("currency", "INR");
            object.put("amount", Integer.parseInt(samount) * 100);
            object.put("prefill.contact", createModel.getMobile());
            object.put("prefill.email", createModel.getEmail());
            checkout.open(PaymentActivity2.this, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        registerAfterPay("paid", s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        registerAfterPay("failed", s);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void showDialogFail() {
        Dialog dialog = new Dialog(this, R.style.SheetDialog);
        dialog.setContentView(R.layout.fail);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.findViewById(R.id.closeBtn).setOnClickListener(v -> {
            dialog.dismiss();
            onBackPressed();
        });
        dialog.show();
    }

    public void showDialogSuccess() {

        Dialog dialog = new Dialog(this, R.style.SheetDialog);
        dialog.setContentView(R.layout.payment_status_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.findViewById(R.id.closeBtn).setOnClickListener(v -> {
            dialog.dismiss();
            SharedPreferences pref = getSharedPreferences("Tahfizul", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            dialog.dismiss();
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("isRegister", true);
            startActivity(i);
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void registerAfterPay(String status, String id) {
        loadingDialog.show();
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Map<String, String> params = new HashMap<String, String>();
//        params.put("email", createModel.email);
//        params.put("mobile", createModel.mobile);
//        params.put("madarsha_id", createModel.madarsha_id);
//        params.put("fname", createModel.fname);
//        params.put("sub_id", createModel.sub_id);
//        params.put("name", createModel.name);
//        params.put("talib_ilm_type", createModel.talib_ilm_type);
//        params.put("course_id", createModel.course_id);
//        params.put("country_id", createModel.country_id);
//        params.put("state_id", createModel.state_id);
//        params.put("city_id", createModel.city_id);
//        params.put("address", createModel.address);
//        params.put("password", createModel.password);
//        params.put("madarsa_id", createModel.madarsa_id);
//        params.put("aadhar_no", createModel.aadhar_no);
//        params.put("dob", createModel.dob);
//        params.put("tx_id", createModel.tx_id);
//        params.put("pay_status", createModel.pay_status);
//        params.put("amount", createModel.amount);
//        params.put("trx_date", createModel.trx_date);
//        params.put("pay_type", createModel.pay_type);
//        params.put("teacher_id", createModel.teacherID);
//        params.put("timing_id", createModel.timeID);
        params.put("sub_id", createModel.getSub_id());
        params.put("uid", String.valueOf(AppDelegate.instance.getRegisterSuccess().getUserId()));
        params.put("tx_id", id);
        params.put("pay_status", status);
        params.put("amount", createModel.getAmount());
        params.put("trx_date", todayDate);
//        params.put("phonecode", createModel.phoneCode);
        params.put("pay_type", "Online");
        Call<CreateNewResponse> call = service.renewAPi(params);
        call.enqueue(new Callback<CreateNewResponse>() {
            @Override
            public void onResponse(Call<CreateNewResponse> call, Response<CreateNewResponse> response) {
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        if (status.equals("paid")) {
                            showDialogSuccess();
                        } else {
                            showDialogFail();
                        }
                    } else {
                        Toast.makeText(PaymentActivity2.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showDialogFail();
                }
                loadingDialog.hide();
            }

            @Override
            public void onFailure(Call<CreateNewResponse> call, Throwable t) {
                loadingDialog.hide();
                showDialogFail();
                Toast.makeText(PaymentActivity2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
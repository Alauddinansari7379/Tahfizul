package com.amtech.tahfizulquranonline.v2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amtech.tahfizulquranonline.AppDelegate;
import com.amtech.tahfizulquranonline.CountryCodeAdapter;
import com.amtech.tahfizulquranonline.LoadingDialog;
import com.amtech.tahfizulquranonline.R;
import com.amtech.tahfizulquranonline.V2Data.ApiClient;
import com.amtech.tahfizulquranonline.V2Data.ApiInterface;
import com.amtech.tahfizulquranonline.V2Data.CityAdapter;
import com.amtech.tahfizulquranonline.V2Data.CommonResponse;
import com.amtech.tahfizulquranonline.V2Data.CountryAdapter;
import com.amtech.tahfizulquranonline.V2Data.CourseAdapter;
import com.amtech.tahfizulquranonline.V2Data.ItemClickListener;
import com.amtech.tahfizulquranonline.V2Data.RecyclerTouchListener;
import com.amtech.tahfizulquranonline.V2Data.StateAdapter;
import com.amtech.tahfizulquranonline.V2Data.city.CityItem;
import com.amtech.tahfizulquranonline.V2Data.city.CityResponse;
import com.amtech.tahfizulquranonline.V2Data.countryModel.CountryModel;
import com.amtech.tahfizulquranonline.V2Data.countryModel.CountyItem;
import com.amtech.tahfizulquranonline.V2Data.courseModel.CourseItem;
import com.amtech.tahfizulquranonline.V2Data.courseModel.CourseModel;
import com.amtech.tahfizulquranonline.V2Data.state.StateItem;
import com.amtech.tahfizulquranonline.V2Data.state.StateResponse;
import com.amtech.tahfizulquranonline.fragment.TalibLoginFragment;
import com.amtech.tahfizulquranonline.v2.countryCode.CountryCodeModel;
import com.amtech.tahfizulquranonline.v2.countryCode.GetCountryResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TalibRegisterFragment extends Fragment {

    TextView login, countryCode;
    Button register;
    boolean isAllFieldsChecked = false;
    int countryID = 0, stateID = 0, cityID = 0, courseID = 0, countryCodeID = 91;
    TextView countryTxt, stateTxt, cityTxt, courseTxt, f;
    LoadingDialog loadingDialog;
    List<CourseItem> courseItemList = new ArrayList<>();
    List<CountyItem> countyItemList = new ArrayList<>();
    CreateModel createModel;
    EditText fNameText, lNameText, emailText, fatherName, fMobileNo, aadhaarNo, email, address, passwordTxt;
    TextView dob;
    Dialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talib_register, container, false);
        login = view.findViewById(R.id.login);
        createModel = new CreateModel();
        countryCode = view.findViewById(R.id.countryCode);

        emailText = view.findViewById(R.id.emailText);
        passwordTxt = view.findViewById(R.id.passwordText);
        fNameText = view.findViewById(R.id.fNameText);
        lNameText = view.findViewById(R.id.lNametext);
        fatherName = view.findViewById(R.id.fatherNameText);
        fMobileNo = view.findViewById(R.id.fMobileText);
        aadhaarNo = view.findViewById(R.id.aadharText);
        dob = view.findViewById(R.id.dobText);
        loadingDialog = new LoadingDialog(getActivity());
        register = view.findViewById(R.id.submit_btn);
        countryTxt = view.findViewById(R.id.countryText);
        courseTxt = view.findViewById(R.id.courseText);
        stateTxt = view.findViewById(R.id.stateText);
        cityTxt = view.findViewById(R.id.cityText);
        address = view.findViewById(R.id.editText);
        login.setOnClickListener(v -> {
            TalibLoginFragment.registerV.setVisibility(View.GONE);
            TalibLoginFragment.login.setVisibility(View.VISIBLE);
        });


        dob.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int TYear = c.get(Calendar.YEAR);
            int TMonth = c.get(Calendar.MONTH);
            int TDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                    (view1, year, monthOfYear, dayOfMonth) -> dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), TYear, TMonth, TDay);
            datePickerDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            datePickerDialog.getWindow().setGravity(Gravity.CENTER);
            datePickerDialog.show();
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    login();
                }

            }
        });
        countryCode.setOnClickListener(v -> {
            getCountryCode();
        });
        countryTxt.setOnClickListener(v -> {
            getCountry();
        });
        stateTxt.setOnClickListener(v -> {
            if (countryID == 0) {
                Toast.makeText(getActivity(), "First Select Country", Toast.LENGTH_SHORT).show();
            } else {
                getState(countryID);
            }
        });


        courseTxt.setOnClickListener(v -> {
            getCourse();
        });
        cityTxt.setOnClickListener(v -> {
            if (stateID == 0) {
                Toast.makeText(getActivity(), "First Select State", Toast.LENGTH_SHORT).show();
            } else {
                getCity(stateID);
            }
        });


        return view;
    }

    private void login() {
        loadingDialog.show();
        createModel.setName(fNameText.getText().toString() + " " + lNameText.getText().toString());
        createModel.setEmail(emailText.getText().toString());
        createModel.setFname(fatherName.getText().toString());
        createModel.setMadarsa_id("1");
        createModel.setMobile(fMobileNo.getText().toString());
        createModel.setTalib_ilm_type("1");
        createModel.setCountry_id(String.valueOf(countryID));
        createModel.setCourse_id(String.valueOf(9));
        createModel.setState_id(String.valueOf(stateID));
        createModel.setCity_id(String.valueOf(cityID));
        createModel.setAddress(address.getText().toString());
        createModel.setPassword(passwordTxt.getText().toString());
        createModel.setSub_id("1");
        createModel.setAadhar_no(aadhaarNo.getText().toString());
        createModel.setDob(dob.getText().toString());
        createModel.setTx_id("0");
        createModel.setPay_status("Pending");
        createModel.setPay_type("0");
        createModel.setTrx_date("0");
        createModel.setMadarsha_id("1");
        createModel.setAmount("0");
        createModel.setPhoneCode(String.valueOf(countryCodeID));
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", createModel.email);
        params.put("mobile", createModel.mobile);
        params.put("madarsha_id", createModel.madarsha_id);
        params.put("fname", createModel.fname);
        params.put("sub_id", createModel.sub_id);
        params.put("name", createModel.name);
        params.put("talib_ilm_type", createModel.talib_ilm_type);
        params.put("course_id", createModel.course_id);
        params.put("country_id", createModel.country_id);
        params.put("state_id", createModel.state_id);
        params.put("city_id", createModel.city_id);
        params.put("address", createModel.address);
        params.put("password", createModel.password);
        params.put("madarsa_id", createModel.madarsa_id);
        params.put("aadhar_no", createModel.aadhar_no);
        params.put("dob", createModel.dob);
        params.put("sub_id", createModel.getSub_id());
//        params.put("uid", String.valueOf(AppDelegate.instance.getRegisterSuccess().getUserId()));
        params.put("phonecode", createModel.phoneCode);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<CommonResponse> call = service.userRegister(params);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        AppDelegate.getInstance().setCreateModel(createModel);
                        startActivity(new Intent(getActivity(), ListofMoulimsActivity.class));
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    loadingDialog.hide();
                } else {
                    Toast.makeText(getActivity(), "Something Wend Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                loadingDialog.hide();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean CheckAllFields() {
        if (TextUtils.isEmpty(fNameText.getText().toString())) {
            showToast("Please Enter First Name");

            return false;
        }

        if (TextUtils.isEmpty(lNameText.getText().toString())) {
            showToast("Please Enter Last Name");
            return false;
        }

        if (TextUtils.isEmpty(emailText.getText().toString())) {
            showToast("Please Enter Email");
            return false;
        }

        if (!ValidationUtils.validateEmail(emailText.getText().toString())) {
            showToast("Please Enter Valid Email!");
            return false;
        }
        if (TextUtils.isEmpty(emailText.getText().toString())) {
            showToast("Please Enter Email");
            return false;
        }
        if (TextUtils.isEmpty(fatherName.getText().toString())) {
            showToast("Please Enter Father's Name");
            return false;
        }

        if (TextUtils.isEmpty(fMobileNo.getText().toString())) {
            showToast("Please Enter Mobile Number ");
            return false;
        }
        if (fMobileNo.getText().toString().length() != 10) {
            showToast("Please Enter 10 Digit Mobile Number");
            return false;
        }

        if (TextUtils.isEmpty(dob.getText().toString())) {
            showToast("Please Enter Date Of Birth");
            return false;
        }

//
//        if (TextUtils.isEmpty(courseTxt.getText().toString())) {
//            showToast("Please Select Course");
//            return false;
//        }
//        if (TextUtils.isEmpty(passwordTxt.getText().toString())) {
//            showToast("Please Select Course");
//            return false;
//        }
        if (TextUtils.isEmpty(countryTxt.getText().toString())) {
            showToast("Please Select Country");
            return false;
        }

        if (TextUtils.isEmpty(stateTxt.getText().toString())) {
            showToast("Please Select State");
            return false;
        }

        if (TextUtils.isEmpty(cityTxt.getText().toString())) {
            showToast("Please Select City");
            return false;
        }

        if (TextUtils.isEmpty(address.getText().toString())) {
            showToast("Please Enter Address");
            return false;
        }
        return true;
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void getCountry() {
        if (countyItemList.size() == 0) {
            loadingDialog.show();
            ApiInterface service = ApiClient.createService(ApiInterface.class);
            Call<CountryModel> call = service.getCountry();
            call.enqueue(new Callback<CountryModel>() {
                @Override
                public void onResponse(Call<CountryModel> call, Response<CountryModel> response) {
                    CountryModel countryModel = response.body();
                    if (countryModel != null) {
                        countyItemList.addAll(countryModel.getCountry());
                        countryDialog();
                    } else {
                        loadingDialog.hide();
                    }
                }

                @Override
                public void onFailure(Call<CountryModel> call, Throwable t) {
                    loadingDialog.hide();
                }
            });
        } else {
            countryDialog();
        }

    }

    private void getCourse() {
        if (courseItemList.size() == 0) {
            loadingDialog.show();
            ApiInterface service = ApiClient.createService(ApiInterface.class);
            Call<CourseModel> call = service.getCourse();
            call.enqueue(new Callback<CourseModel>() {
                @Override
                public void onResponse(Call<CourseModel> call, Response<CourseModel> response) {
                    CourseModel courseModel = response.body();
                    if (courseModel != null) {
                        courseItemList.addAll(courseModel.getCourse());
                        courseDialog();
                    } else {
                        loadingDialog.hide();
                    }
                }

                @Override
                public void onFailure(Call<CourseModel> call, Throwable t) {
                    loadingDialog.hide();
                }
            });
        } else {
            courseDialog();
        }

    }

    private void getState(int id) {
        loadingDialog.show();
        ApiInterface service = ApiClient.createService(ApiInterface.class);
        Call<StateResponse> call = service.getState(id);
        call.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                StateResponse stateResponse = response.body();
                if (stateResponse != null) {
                    stateDialog(stateResponse.getData());
                } else {
                    loadingDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                loadingDialog.hide();
            }
        });
    }

    private void getCity(int id) {
        loadingDialog.show();
        ApiInterface service = ApiClient.createService(ApiInterface.class);
        Call<CityResponse> call = service.getCity(id);
        call.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                CityResponse stateResponse = response.body();
                if (stateResponse != null) {
                    cityDialog(stateResponse.getData());
                } else {
                    loadingDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                loadingDialog.hide();
            }
        });
    }

    private void getCountryCode() {
        loadingDialog.show();
        ApiInterface service = ApiClient.createService(ApiInterface.class);
        Call<GetCountryResponse> call = service.getCountryCode();
        call.enqueue(new Callback<GetCountryResponse>() {
            @Override
            public void onResponse(Call<GetCountryResponse> call, Response<GetCountryResponse> response) {
                GetCountryResponse stateResponse = response.body();
                if (stateResponse != null) {
                    countryCode(stateResponse.getData());
                } else {
                    loadingDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<GetCountryResponse> call, Throwable t) {
                loadingDialog.hide();
            }
        });
    }

    public void countryDialog() {
        Dialog dialog = new Dialog(getActivity());
        pDialog = dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        CountryAdapter stateAdapter = new CountryAdapter(getActivity(), countyItemList, this);
        RecyclerView recyclerView = dialog.findViewById(R.id.stateList);
        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText("Select Your Country");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(stateAdapter);
        EditText searchButton = dialog.findViewById(R.id.searchBar);
        searchButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<CountyItem> filteredPlaces = new ArrayList<>();
                for (CountyItem bookModel : countyItemList) {
                    if (bookModel.getC_name().toLowerCase().trim().contains(s.toString().toLowerCase().trim())) {
                        filteredPlaces.add(bookModel);
                    }
                }
                stateAdapter.filteredList(filteredPlaces);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (countyItemList.size() == 0) {
            ConstraintLayout constraintLayout = dialog.findViewById(R.id.NoDataLy);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        dialog.show();
        loadingDialog.hide();
    }


    public void countrySelect(int id, String name) {
        countryID = id;
        countryTxt.setText(name);
        pDialog.dismiss();
    }

    public void courseSelect(int id, String name) {
        courseID = id;
        courseTxt.setText(name);
        pDialog.dismiss();
    }

    public void stateSelect(int id, String name) {
        stateID = id;
        stateTxt.setText(name);
        pDialog.dismiss();
    }

    public void citySelect(int id, String name) {
        cityID = id;
        cityTxt.setText(name);
        pDialog.dismiss();
    }

    public void getCountryData(int id, int code) {
        countryCodeID = id;
        countryCode.setText("+" + code);
        pDialog.dismiss();
    }

    public void courseDialog() {
        Dialog dialog = new Dialog(getActivity());
        pDialog = dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        CourseAdapter courseAdapter = new CourseAdapter(getActivity(), courseItemList);
        RecyclerView recyclerView = dialog.findViewById(R.id.stateList);
        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText("Select Your Course");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(courseAdapter);
        if (courseItemList.size() == 0) {
            ConstraintLayout constraintLayout = dialog.findViewById(R.id.NoDataLy);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        dialog.show();
        loadingDialog.hide();
    }

    public void stateDialog(List<StateItem> list) {
        Dialog dialog = new Dialog(getActivity());
        pDialog = dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        StateAdapter stateAdapter = new StateAdapter(getActivity(), list, this);
        RecyclerView recyclerView = dialog.findViewById(R.id.stateList);
        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText("Select Your State");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(stateAdapter);

        EditText searchButton = dialog.findViewById(R.id.searchBar);
        searchButton.setHint("Search State");
        searchButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<StateItem> filteredPlaces = new ArrayList<>();
                for (StateItem bookModel : list) {
                    if (bookModel.getStateName().toLowerCase().trim().contains(s.toString().toLowerCase().trim())) {
                        filteredPlaces.add(bookModel);
                    }
                }
                stateAdapter.filteredList(filteredPlaces);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (list.size() == 0) {
            ConstraintLayout constraintLayout = dialog.findViewById(R.id.NoDataLy);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        dialog.show();
        loadingDialog.hide();
    }


    public void cityDialog(List<CityItem> list) {
        Dialog dialog = new Dialog(getActivity());
        pDialog = dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        CityAdapter stateAdapter = new CityAdapter(getActivity(), list, this);
        RecyclerView recyclerView = dialog.findViewById(R.id.stateList);
        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText("Select Your City");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(stateAdapter);

        EditText searchButton = dialog.findViewById(R.id.searchBar);
        searchButton.setHint("Search City");

        searchButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<CityItem> filteredPlaces = new ArrayList<>();
                for (CityItem bookModel : list) {
                    if (bookModel.getCityName().toLowerCase().trim().contains(s.toString().toLowerCase().trim())) {
                        filteredPlaces.add(bookModel);
                    }
                }
                stateAdapter.filteredList(filteredPlaces);

            }
        });

        if (list.size() == 0) {
            ConstraintLayout constraintLayout = dialog.findViewById(R.id.NoDataLy);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        dialog.show();
        loadingDialog.hide();
    }

    public void countryCode(List<CountryCodeModel> list) {
        Dialog dialog = new Dialog(getActivity());
        pDialog=dialog;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        CountryCodeAdapter stateAdapter = new CountryCodeAdapter(getActivity(), list, this);
        RecyclerView recyclerView = dialog.findViewById(R.id.stateList);
        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText("Select Country Code");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(stateAdapter);
        if (list.size() == 0) {
            ConstraintLayout constraintLayout = dialog.findViewById(R.id.NoDataLy);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        EditText searchButton = dialog.findViewById(R.id.searchBar);
        searchButton.setHint("Search County Code");

        searchButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<CountryCodeModel> filteredPlaces = new ArrayList<>();
                for (CountryCodeModel bookModel : list) {
                    if (bookModel.getName().toLowerCase().trim().contains(s.toString().toLowerCase().trim()) || String.valueOf(bookModel.getPhonecode()).toLowerCase().trim().contains(s.toString().toLowerCase().trim())) {
                        filteredPlaces.add(bookModel);
                    }
                }
                stateAdapter.filteredList(filteredPlaces);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
        loadingDialog.hide();
    }


}
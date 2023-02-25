package com.amtech.tahfizulquranonline.V2Data;


import com.amtech.tahfizulquranonline.CreateNewResponse;
import com.amtech.tahfizulquranonline.V2Data.city.CityResponse;
import com.amtech.tahfizulquranonline.V2Data.countryModel.CountryModel;
import com.amtech.tahfizulquranonline.V2Data.courseModel.CourseModel;
import com.amtech.tahfizulquranonline.V2Data.plan.PlanResponse;
import com.amtech.tahfizulquranonline.V2Data.state.StateResponse;
import com.amtech.tahfizulquranonline.v2.M.GetMoulimsResponse;
import com.amtech.tahfizulquranonline.v2.countryCode.GetCountryResponse;
import com.amtech.tahfizulquranonline.v2.moulimProfile.GetMoulimProfileResponse;
import com.amtech.tahfizulquranonline.v2.payment.PaymentResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("/master_management/country/list/api")
    Call<CountryModel> getCountry();

    @GET("/master_management/state/selected/{id}")
    Call<StateResponse> getState(@Path("id") int id);

    @GET("/master_management/city/selected/{id}")
    Call<CityResponse> getCity(@Path("id") int id);

    @GET("/madarsa_management/course/list/api")
    Call<CourseModel> getCourse();

    @GET("/master_management/code/list/api")
    Call<GetCountryResponse> getCountryCode();

    @GET("/subPlan/api")
    Call<PlanResponse> getPlan();


    @POST("/talibilm_management/talibilm/createwapi")
    @FormUrlEncoded
    Call<CommonResponse> userRegister(@FieldMap Map<String, String> params);


    @POST("/talibilm_management/talibilm/createnew")
    @FormUrlEncoded
    Call<CreateNewResponse> registerAfterPay(@FieldMap Map<String, String> params);

    @POST("/talibilm_management/talibilm/createwpay")
    @FormUrlEncoded
    Call<CreateNewResponse> renewAPi(@FieldMap Map<String, String> params);

    @GET("/talibilm_management/talibilm/paydetails/{id}")
    Call<PaymentResponse> getAllPayment(@Path("id") String id);

    @GET("/mualem_management/mualem/selected/{id}")
    Call<GetMoulimsResponse> getMoulims(@Path("id") String id);

    @GET("/madarsa_management/mualem_slot_type/selectoption/{id}/{idS}")
    Call<GetMoulimProfileResponse> getMoulimsProfile(@Path("id") String id, @Path("idS") String idS);
}

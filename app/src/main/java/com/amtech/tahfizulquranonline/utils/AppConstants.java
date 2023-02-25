package com.amtech.tahfizulquranonline.utils;

import com.amtech.tahfizulquranonline.models.StudentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shourav Paul on 02-12-2021.
 **/
public class AppConstants {

    public static final String mainUrl = "https://www.tahfizulquranonline.com/";
    public static String maulimloginUrl = mainUrl + "mualem_management/mualem/api/login";
    public static String talibloginUrl = mainUrl + "talibilm_management/talibilm/api/login";
    public static String walidloginUrl = mainUrl + "walidain_management/walidain/api/login";
    public static String newMaulimLoginUrl = mainUrl + "api/mualemlogin";
    public static String newTalibLoginUrl = mainUrl + "api/talibilmlogin";
    public static String newWalidLoginUrl = mainUrl + "api/walidainlogin";
    public static String maulimClassSlotsUrl = mainUrl + "api/mualemclass/";
    public static String talibClassSlotsUrl = mainUrl + "api/class/";
    public static String maulimGetMyTalibListUrl = mainUrl + "mualem_management/mualem/mytalibilm/";
    public static String walidGetMyTalibListUrl = mainUrl + "api/talib_walidain/";
    public static String talibGetMyMaulimListUrl = mainUrl + "talibilm_management/talibilm/mymualem/";
    public static String maulimCreateAssignmentUrl = mainUrl + "talibilm_mualem_management/assignment/create";
    public static String getTalibAssignmentByStatusUrl = mainUrl + "talibilm_mualem_management/talibilmassignmentlist/";
    public static String getMaulimAssignmentByStatusUrl = mainUrl + "talibilm_mualem_management/mualemassignmentlist/";
    public static String talibAssignmentResponseUrl = mainUrl + "talibilm_mualem_management/assignmentreportbytalibilm";
    public static String maulimCompleteAssignmentUrl = mainUrl + "talibilm_mualem_management/assignmentcompletereport";
    public static String getAssignmentForReportUrl = mainUrl + "talibilm_mualem_management/mualemassignmentcompletedlist/";
    public static String generateReportUrl = mainUrl + "talibilm_mualem_management/assignmentupdatereport";
    public static String getTalibReportUrl = mainUrl + "api/assignment/walidain/";
    public static String sendWalidFeedbackUrl = mainUrl + "api/walidainfeed/create";
    public static String getWalidFeedbackUrl = mainUrl + "api/walidainfeed/bywalid/";
    public static String getMaulimFeedbackUrl = mainUrl + "api/walidainfeed/bymuailim/";
    public static String getProfileUrl = mainUrl + "talibilm_management/talibilm/";
    public static String getPlaystoreLatestVersionUrl = "http://gptaxcalculator.site/calc/api/others/gettahfizulcurrentversion.php";
    public static String getMaulimProfileUrl = mainUrl + "api/mualem/";
    public static String getWalidProfileUrl = mainUrl + "api/walidain/";
    public static String maulimPhone = "";
    public static String maulimID = "";
    public static String maulimToken = "";
    public static String talibPhone = "";
    public static String talibID = "";
    public static String talibToken = "";
    public static String walidPhone = "";
    public static String walidID = "";
    public static String walidToken = "";
    public static int loginUser;
    public static final int MAULIM = 1;
    public static final int TALIB = 2;
    public static final int WALID = 3;
    public static String currentUserName = "";
    public static String mediaFileLink;
    public static List<StudentModel> myTalibList = new ArrayList<>();

}

package com.amtech.tahfizulquranonline;

import com.amtech.tahfizulquranonline.v2.CreateModel;
import com.amtech.tahfizulquranonline.v2.RegisterSuccess;

import java.util.ArrayList;

public class AppDelegate {

    public static AppDelegate instance;
    CreateModel createModel;
    RegisterSuccess registerSuccess;
    int UserID;

    public static void setInstance(AppDelegate instance) {
        AppDelegate.instance = instance;
    }

    public static AppDelegate getInstance() {
        if (instance == null) {
            instance = new AppDelegate();
        }
        return instance;
    }

    public CreateModel getCreateModel() {
        return createModel;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public void setCreateModel(CreateModel createModel) {
        this.createModel = createModel;
    }

    public RegisterSuccess getRegisterSuccess() {
        return registerSuccess;
    }

    public void setRegisterSuccess(RegisterSuccess registerSuccess) {
        this.registerSuccess = registerSuccess;
    }
}
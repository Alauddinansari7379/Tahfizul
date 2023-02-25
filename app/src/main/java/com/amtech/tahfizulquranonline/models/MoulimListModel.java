package com.amtech.tahfizulquranonline.models;

public class MoulimListModel {
    int prof_img ;
    String name ;

    public MoulimListModel(int prof_img, String name) {
        this.prof_img = prof_img;
        this.name = name;
    }

    public int getProf_img() {
        return prof_img;
    }

    public void setProf_img(int prof_img) {
        this.prof_img = prof_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

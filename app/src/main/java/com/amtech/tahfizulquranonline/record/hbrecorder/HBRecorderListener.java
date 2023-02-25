package com.amtech.tahfizulquranonline.record.hbrecorder;

public interface HBRecorderListener {
    void HBRecorderOnStart();
    void HBRecorderOnComplete();
    void HBRecorderOnError(int errorCode, String reason);
}

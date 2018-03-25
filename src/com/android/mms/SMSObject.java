package com.android.mms;

import java.io.Serializable;
import java.util.ArrayList;

public class SMSObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String addr;
    private String msg;
    private String readState;
    private String dateTime;
    private String dateSentTime;

    String getId() {
        return id;
    }

    String getAddr() {
        return addr;
    }

    String getMsg() {
        return msg;
    }

    String getReadState() {
        return readState;
    }

    String getDateTime() {
        return dateTime;
    }

    String getDateSentTime() {
        return dateSentTime;
    }

    void setId(String id){
        this.id = id;
    }

    void setAddr(String addr){
        this.addr = addr;
    }

    void setMsg(String msg){
        this.msg = msg;
    }

    void setReadState(String readState) {
        this.readState = readState;
    }

    void setDateTime(String time) {
        this.dateTime = time;
    }

    void setDateSentTime(String time) {
        this.dateSentTime = time;
    }
}


class SMSBackup implements Serializable {

	public ArrayList<SMSObject> SMSList;
    public SMSBackup() {
        SMSList = new ArrayList<SMSObject>();
    }
}
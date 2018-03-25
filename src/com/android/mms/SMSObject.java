/*
Copyright (c) 2014, The Linux Foundation. All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of The Linux Foundation nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

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

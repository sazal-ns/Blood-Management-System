/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RTsoftBD_Siddiqui on 2017-04-11.
 */

public class History {
    private String id, date, hospital, qun, userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public History(String id, String date, String hospital, String qun, String userId) {

        this.id = id;
        this.date = date;
        this.hospital = hospital;
        this.qun = qun;
        this.userId = userId;
    }

    public History(String date, String hospital, String qun) {
        this.date = date;
        this.hospital = hospital;
        this.qun = qun;
    }

    public History() {
    }

    public History(String id, String date, String hospital, String qun) {
        this.id = id;
        this.date = date;
        this.hospital = hospital;
        this.qun = qun;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getQun() {
        return qun;
    }

    public void setQun(String qun) {
        this.qun = qun;
    }
}

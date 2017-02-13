/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package models;

/**
 * Created by RTsoftBD_Siddiqui on 2017-02-06.
 */

public class Users {
    private  String sl, dname, mobile, area, thana, union, district, age, bloodg, imageLink;

    public Users() {

    }

    public Users(String dname, String mobile, String area, String thana, String union, String district, String age, String bloodg, String imageLink) {
        this.dname = dname;
        this.mobile = mobile;
        this.area = area;
        this.thana = thana;
        this.union = union;
        this.district = district;
        this.age = age;
        this.bloodg = bloodg;
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodg() {
        return bloodg;
    }

    public void setBloodg(String bloodg) {
        this.bloodg = bloodg;
    }
}

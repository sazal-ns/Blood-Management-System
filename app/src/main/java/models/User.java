/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package models;

/**
 * Created by RTsoftBD_Siddiqui on 2017-02-06.
 */

public class User {
    private static String id, dname, username, password, user_type, mobile, area, thana, union, district, age, bloodg;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        User.id = id;
    }

    public static String getDname() {
        return dname;
    }

    public static void setDname(String dname) {
        User.dname = dname;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static String getUser_type() {
        return user_type;
    }

    public static void setUser_type(String user_type) {
        User.user_type = user_type;
    }

    public static String getMobile() {
        return mobile;
    }

    public static void setMobile(String mobile) {
        User.mobile = mobile;
    }

    public static String getArea() {
        return area;
    }

    public static void setArea(String area) {
        User.area = area;
    }

    public static String getThana() {
        return thana;
    }

    public static void setThana(String thana) {
        User.thana = thana;
    }

    public static String getUnion() {
        return union;
    }

    public static void setUnion(String union) {
        User.union = union;
    }

    public static String getDistrict() {
        return district;
    }

    public static void setDistrict(String district) {
        User.district = district;
    }

    public static String getAge() {
        return age;
    }

    public static void setAge(String age) {
        User.age = age;
    }

    public static String getBloodg() {
        return bloodg;
    }

    public static void setBloodg(String bloodg) {
        User.bloodg = bloodg;
    }
}

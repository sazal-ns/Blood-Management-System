/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package models;

/**
 * Created by RTsoftBD_Siddiqui on 2017-02-13.
 */

 public  class  FBUsers {
   private static String id,age_range,email,name, imageLink, isFB="false",first_name;

    public static String getFirst_name() {
        return first_name;
    }

    public static void setFirst_name(String first_name) {
        FBUsers.first_name = first_name;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        FBUsers.id = id;
    }

    public static String getAge_range() {
        return age_range;
    }

    public static void setAge_range(String age_range) {
        FBUsers.age_range = age_range;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        FBUsers.email = email;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        FBUsers.name = name;
    }

    public static String getImageLink() {
        return imageLink;
    }

    public static void setImageLink(String imageLink) {
        FBUsers.imageLink = imageLink;
    }

    public static String getIsFB() {
        return isFB;
    }

    public static void setIsFB(String isFB) {
        FBUsers.isFB = isFB;
    }
}

/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package config;

/**
 * Created by RTsoftBD_Siddiqui on 2017-02-06.
 */

public final class Config {
    private static String BASE_URL = "http://api.thebloodbank.tk/blood/";

    public static String URL_LOGIN = BASE_URL.concat("login.php");

    public static String URL_SEARCH_BLOOD= BASE_URL.concat("search.php");

    public static String URL_REGISTRATION= BASE_URL.concat("reg.php");

    public static String URL_UPDATE_USER_INFO= BASE_URL.concat("updateUserInfo.php");

    public static String URL_LOC = BASE_URL.concat("location.php");
}

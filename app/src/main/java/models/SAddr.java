/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RTsoftBD_Siddiqui on 2017-04-01.
 */

public class SAddr {
    private static List<String> areas = new ArrayList<>();
    private static List<String> districts = new ArrayList<>();

    public static List<String> getAreas() {
        return areas;
    }

    public static void setAreas(String areas) {
        SAddr.areas.add( areas);
    }

    public static List<String> getDistricts() {
        return districts;
    }

    public static void setDistricts(String districts) {
        SAddr.districts.add(districts);
    }
}

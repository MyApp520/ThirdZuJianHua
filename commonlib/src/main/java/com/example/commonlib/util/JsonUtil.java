package com.example.commonlib.util;

import com.google.gson.Gson;

/**
 * Created by smile on 2019/4/22.
 */

public class JsonUtil {

    private static final Gson gson = new Gson();

    public static String toJson(Object object) {
        if (object == null) {
            return "";
        }

        try {
            return gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

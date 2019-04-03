package com.example.commonlib.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xh_peng on 2017/11/7.
 */
public class TimeUitls {

    /**
     * 获取当前时间
     * @param flag
     * @return
     */
    public static String getCurrentTime(int flag, Date currentDate) {
        SimpleDateFormat formatter = null;
        switch (flag) {
            case 1:
                formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                break;
            case 2:
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            case 3:
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                break;
        }
        if (formatter == null) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
        if (currentDate != null) {
            return formatter.format(currentDate);
        }
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static long getTimeMillisByString(int flag, String srcTime) {
        if (TextUtils.isEmpty(srcTime) || "".equals(srcTime.trim())) {
            return 0;
        }
        SimpleDateFormat formatter = null;
        switch (flag) {
            case 1:
                formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                break;
            case 2:
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            case 3:
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                break;
        }
        Date desDate = null;
        try {
            desDate = formatter.parse(srcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (desDate == null) {
            return 0;
        }
        return desDate.getTime();
    }

    /**
     * 计算指定时间和当前时间相差多少
     * @param flag
     * @param endTime 指定时间
     * @return
     */
    public static String getTimestampToCurrentTime(int flag, String endTime) {
        if (AndroidUtil.judgeStringIsNull(endTime)) {
            return "";
        }
        long endTimeMillis = getTimeMillisByString(flag, endTime);
        Date currentDate = new Date(System.currentTimeMillis());
        long timestamp = endTimeMillis - currentDate.getTime();
        if (timestamp < 0) {
            return endTime;
        }
        if (timestamp < 3600) {
            return "1小时内结束";
        }
        long days = timestamp / (1000 * 60 * 60 * 24);
        long hours = (timestamp - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (timestamp - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timestamp - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
        if (days <= 0) {
            return  hours + "小时后结束";
        }
        return days + "天" + hours + "小时后结束";
    }
}

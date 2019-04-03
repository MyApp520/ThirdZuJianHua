package com.example.commonlib.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xh_peng on 2017/11/17.
 */
public class SharepreSave {
    private static SharedPreferences sp;
    private static final String SZXHDZ_FACE = "szxh_face_control";

    /**
     * 创建任务时订阅的镜头
     */
    public static final String HAS_SUBSCRIBE_CAMERA = "has_subscribe_camera";

    /**
     * 用户定位到的的位置信息
     */
    public static final String MAP_LOCATION_ADDR = "map_location_addr";

    /**
     * POI搜索历史--关键字保存
     */
    public static final String BDPOI_SEARCH_HISTORY = "bdpoi_search_history";

    /**
     * 是否下载了百度离线地图
     */
    public static final String HAS_DOWNLOAD_OFFLINEMAP = "has_download_offlinemap";

    /**
     * 从地图组获取到镜头的时间
     */
    public static final String GET_CAMERA_TIME = "get_camera_time";

    /**
     * 登录用户信息
     */
    public static final String CURRENT_USER_ALL_INFO = "login_user_all_info";

    /**
     * 是否开启消息通知铃声
     */
    public static final String MSG_SOUND_SWITCH = "msg_sound_switch";

    /**
     * 是否开启消息通知震动
     */
    public static final String MSG_VIBRATOR_SWITCH = "msg_vibrator_switch";

    /**
     * 消息通知的铃声
     */
    public static final String MSG_NOTIFICATION_RING = "msg_notification_ring";

    /**
     * 消息通知的铃声的名称
     */
    public static final String MSG_NOTIFICATION_RING_NAME = "msg_notification_ring_name";

    /**
     * 常用联系人
     */
    public static final String FREQUENT_PEOPLE_LIST = "frequent_people_jingyuan";

    /**
     * 写入boolean变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值 boolean
     */
    public static void putBoolean(Context ctx, String key, boolean value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(SZXHDZ_FACE, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 读取boolean标示从sp中
     *
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(SZXHDZ_FACE, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /**
     * 写入boolean变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值string
     */
    public static void putString(Context ctx, String key, String value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(SZXHDZ_FACE, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     * 读取boolean标示从sp中
     *
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static String getString(Context ctx, String key, String defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(SZXHDZ_FACE, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    /**
     * 写入boolean变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值string
     */
    public static void putLong(Context ctx, String key, long value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(SZXHDZ_FACE, Context.MODE_PRIVATE);
        }
        sp.edit().putLong(key, value).apply();
    }

    /**
     * 读取boolean标示从sp中
     *
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static long getLong(Context ctx, String key, long defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(SZXHDZ_FACE, Context.MODE_PRIVATE);
        }
        return sp.getLong(key, defValue);
    }


    //清除搜索记录
    public static void cleanHistory(Context ctx) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(SZXHDZ_FACE, Context.MODE_PRIVATE);
        }
        sp.edit().clear().commit();
    }
}

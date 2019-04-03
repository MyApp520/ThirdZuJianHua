package com.example.commonlib.util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 手机信息 & MAC地址 & 开机时间
 *
 * @author MaTianyu
 * @date 2014-09-25
 */
public class AndroidUtil {
    private static final String TAG = AndroidUtil.class.getSimpleName();
    private static long lastClickTime;
    private static Ringtone mDefaultRingtone, mTargetRingtone;
    private static Uri defaultNotificationUri;
    private static Vibrator mTargetVibrator;

    public enum ShowType {
        NUll,      // 没有
        FIRST_SHOW, //第一次
        ERROR_SHOW,//加载错误
        NO_DATA,//没有数据
        LONG_NETWORK,//网络超时
        NO_NETWORK,//没有网络
        DATA_EXECPTION,//服务器数据异常
        NO_PERMISSIONS,//没有权限
    }

    /**
     * 获取 MAC 地址
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getMacAddress(Context context) {
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        Log.i(TAG, " MAC：" + mac);
        return mac;
    }

    /**
     * 获取 开机时间
     */
    public static String getBootTimeString() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        int h = (int) ((ut / 3600));
        int m = (int) ((ut / 60) % 60);
        Log.i(TAG, h + ":" + m);
        return h + ":" + m;
    }


    public static String printSystemInfo() {
        String phonename = Build.MODEL;
        return phonename;
    }

    public static String formatFileSizeToString(long fileLen) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileLen < 1024) {
            fileSizeString = df.format((double) fileLen) + "B";
        } else if (fileLen < 1048576) {
            fileSizeString = df.format((double) fileLen / 1024) + "K";
        } else if (fileLen < 1073741824) {
            fileSizeString = df.format((double) fileLen / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileLen / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static void showContent(Context c, ShowType type, LinearLayout layout) {
//        try {
//            //第一次 加载的布局
//            View firstChild = LayoutInflater.from(c).inflate(R.layout.first_refresh, null);
//            //加载无数据 的布局
//            View noChild = LayoutInflater.from(c).inflate(R.layout.nodata_refresh, null);
//            // 加载错误 的布局
//            View errorChild = LayoutInflater.from(c).inflate(R.layout.errordata_refresh, null);
//            // 无网络 的布局
//            View no_networkChild = LayoutInflater.from(c).inflate(R.layout.nonetwork_refresh, null);
//            // 网络超时 的布局
//            View long_networkChild = LayoutInflater.from(c).inflate(R.layout.longnetwork_refresh, null);
//            // 服务器数据异常
//            View dataExecptionChild = LayoutInflater.from(c).inflate(R.layout.server_data_execption, null);
//            // 没有权限
//            View no_permissions = LayoutInflater.from(c).inflate(R.layout.nopermissions_refresh, null);
//
//            switch (type) {
//                case FIRST_SHOW://第一次加载
//                    layout.setVisibility(View.VISIBLE);
//                    layout.removeAllViews();
//                    layout.addView(firstChild);
//                    break;
//                case NO_DATA:// 连接成功000    0无数据
//                    layout.setVisibility(View.VISIBLE);
//                    layout.removeAllViews();
//                    layout.addView(noChild);
//                    break;
//                case ERROR_SHOW:// 连接成功002  加载错误
//                    layout.setVisibility(View.VISIBLE);
//                    layout.removeAllViews();
//                    layout.addView(errorChild);
//                    break;
//                case NUll://加载成功
//                    layout.removeAllViews();
//                    layout.setVisibility(View.GONE);
//                    break;
//                case NO_NETWORK://没有网络
//                    layout.setVisibility(View.VISIBLE);
//                    layout.removeAllViews();
//                    layout.addView(no_networkChild);
//                    break;
//                case LONG_NETWORK://网络超时
//                    layout.setVisibility(View.VISIBLE);
//                    layout.removeAllViews();
//                    layout.addView(long_networkChild);
//                    break;
//                case DATA_EXECPTION://服务器数据异常
//                    layout.setVisibility(View.VISIBLE);
//                    layout.removeAllViews();
//                    layout.addView(dataExecptionChild);
//                    break;
//                case NO_PERMISSIONS://没有权限
//                    layout.setVisibility(View.VISIBLE);
//                    layout.removeAllViews();
//                    layout.addView(no_permissions);
//                    break;
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void dissContent(LinearLayout layout) {
        layout.removeAllViews();
        layout.removeView(layout);
        layout.setVisibility(View.GONE);
    }


    public static void writeToLocal(String msg, String fileName) {
        CharSequence timestamp = DateFormat.format("yyyyMMddkkmmss", System.currentTimeMillis());
        String fileDir = Environment.getExternalStorageDirectory().getPath() + "/XHstartLog/";
        File file = new File(fileDir);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            if (file.exists()) {
                File f = new File(fileDir + fileName);
                if (!f.exists()) {
                    f.createNewFile();
                }
                FileOutputStream fos;
                fos = new FileOutputStream(f, true);
                byte[] bytes = (msg + "    init time : " + timestamp + "\n").getBytes();
                fos.write(bytes);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将字符串转换成字符串数组
        char[] pswdArray = str.toCharArray();
        byte[] pswdByte = new byte[pswdArray.length];
        //将字符转换成字节
        for (int i = 0; i < pswdArray.length; i++) {
            pswdByte[i] = (byte) pswdArray[i];
        }
        byte[] digest = md5.digest(pswdByte);
        //将得到的字节数组转换成十六进制数
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            int num = ((int) digest[i]) & 0xff;
            //如果不足16，加0填充
            if (num < 16)
                buff.append("0");
            buff.append(Integer.toHexString(num));
        }
        return buff.toString().toUpperCase();
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断是否处于锁屏状态
     *
     * @param c
     * @return 返回ture为锁屏, 返回flase为未锁屏
     */
    public final static boolean isScreenLocked(Context c) {
        KeyguardManager km = (KeyguardManager) c.getSystemService(c.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }


    //唤醒屏幕部分----------------------
    private static KeyguardManager km;                                                                                         //键盘管理
    private static PowerManager pm;                                                                                            //电源管理
    private static PowerManager.WakeLock wakeLock;                                                                             //屏幕唤醒对象

    /**
     * 初始化唤醒屏幕
     *
     * @param activity
     */
    public static void initWakeScrenUnlock(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        wakeLock.acquire();
    }

    /**
     * 唤醒屏幕
     */
    public static void wakeScreen(Activity activity) {
        //屏幕解锁
        km = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        kl.disableKeyguard();

        //屏幕唤醒
        if (wakeLock == null) {
            pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        }
        wakeLock.acquire();
        wakeLock.release();
    }
    //唤醒屏幕部分----------------------

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(View view) {
        if (view == null) {
            return;
        }
        // 先隐藏键盘
        ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 手机 震动
     *
     * @param mContext
     */
    public static void setPhoneVibrate(Context mContext) {
        if (mContext == null) {
            return;
        }
        if (mTargetVibrator == null) {
            mTargetVibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        }
        if (mTargetVibrator == null || !mTargetVibrator.hasVibrator()) {
            return;
        }
        mTargetVibrator.vibrate(300);
    }

    /**
     * 系统提示音
     * @param mContext
     */
    public static void startAlarm(Context mContext) {
        if (mContext == null) {
            return;
        }
        if (defaultNotificationUri == null) {
            defaultNotificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        if (mDefaultRingtone == null && defaultNotificationUri != null) {
            mDefaultRingtone = RingtoneManager.getRingtone(mContext, defaultNotificationUri);
        }
        if (mDefaultRingtone != null && !mDefaultRingtone.isPlaying()) {
            mDefaultRingtone.play();
        }
    }

    /**
     * 播放指定提示音
     * @param mContext
     */
    public static void playTargetRingtone(Context mContext) {
        if (mContext == null) {
            return;
        }
        if (mTargetRingtone == null) {
            mTargetRingtone = getTargetRingtone(mContext);
        }
        if (mTargetRingtone != null && !mTargetRingtone.isPlaying()) {
            mTargetRingtone.play();
        }
    }

    public static void setNewTargetRingtone(Context mContext, String selfRing) {
        if (mTargetRingtone != null && mTargetRingtone.isPlaying()) {
            mTargetRingtone.stop();
        }
        mTargetRingtone = null;
        if (mContext == null) {
            return;
        }
        if (judgeStringIsNull(selfRing) || "None".equals(selfRing)) {
            return;
        }
        Uri selfRingUri = Uri.parse(selfRing);
        if (selfRingUri == null) {
            return;
        }
        mTargetRingtone = RingtoneManager.getRingtone(mContext, selfRingUri);
    }

    public static Ringtone getTargetRingtone(Context mContext) {
        String selfRing = SharepreSave.getString(mContext, SharepreSave.MSG_NOTIFICATION_RING, "");
        if (judgeStringIsNull(selfRing) || "None".equals(selfRing)) {
            startAlarm(mContext);
            return null;
        }
        Uri selfRingUri = Uri.parse(selfRing);
        if (selfRingUri == null) {
            startAlarm(mContext);
            return null;
        }
        return RingtoneManager.getRingtone(mContext, selfRingUri);
    }

    /**
     * 字符串是否为空
     *
     * @param data
     * @return
     */
    public static boolean judgeStringIsNull(String data) {
        if (TextUtils.isEmpty(data) || "".equals(data.trim()) || "null".equals(data.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 根据铃声类型 获取铃声的名称
     * @param context
     * @param type
     * @return
     */
    public static CharSequence getRingtoneName(Context context, int type) {
        if (context == null) {
            MyLog.e(TAG, "Unable to update ringtone name, no context provided");
            return "Unknown ringtone";
        }
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, type);
        CharSequence summary = "Unknown ringtone";
        // Is it a silent ringtone?
        if (ringtoneUri == null) {
            summary = "None";
        } else {
            Cursor cursor = null;
            try {
                if (MediaStore.AUTHORITY.equals(ringtoneUri.getAuthority())) {
                    // Fetch the ringtone title from the media provider
                    cursor = context.getContentResolver().query(ringtoneUri, new String[]{MediaStore.Audio.Media.TITLE}, null, null, null);
                } else if (ContentResolver.SCHEME_CONTENT.equals(ringtoneUri.getScheme())) {
                    cursor = context.getContentResolver().query(ringtoneUri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
                }
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        summary = cursor.getString(0);
                    }
                }
            } catch (SQLiteException sqle) {
                // Unknown title for the ringtone
            } catch (IllegalArgumentException iae) {
                // Some other error retrieving the column from the provider
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return summary;
    }

    /**
     * 根据某个铃声的Uri获取铃声名称
     * @param context
     * @param targetUri
     * @return
     */
    public static CharSequence getRingtoneNameByUri(Context context, Uri targetUri) {
        if (context == null) {
            Log.e(TAG, "Unable to update ringtone name, no context provided");
            return "Unknown ringtone";
        }
        CharSequence summary = "Unknown ringtone";
        // Is it a silent ringtone?
        if (targetUri == null) {
            summary = "None";
        } else {
            Cursor cursor = null;
            try {
                if (MediaStore.AUTHORITY.equals(targetUri.getAuthority())) {
                    // Fetch the ringtone title from the media provider
                    cursor = context.getContentResolver().query(targetUri, new String[]{MediaStore.Audio.Media.TITLE}, null, null, null);
                } else if (ContentResolver.SCHEME_CONTENT.equals(targetUri.getScheme())) {
                    cursor = context.getContentResolver().query(targetUri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
                }
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        summary = cursor.getString(0);
                    }
                }
            } catch (SQLiteException | IllegalArgumentException sqle) {
                // Unknown title for the ringtone
                sqle.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return summary;
    }

    /**
     * 图片链接是否合规
     *
     * @param imageUrl
     * @return
     */
    public static boolean isTheImageUrlCompliant(String imageUrl) {
        if (judgeStringIsNull(imageUrl)) {
            return false;
        }
        if ((imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) && (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".png"))) {
            return true;
        }
        return false;
    }

    public static boolean isFastClick(int delayTime) {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= delayTime) {
            lastClickTime = currentClickTime;
            return false;
        }
        return true;
    }
}

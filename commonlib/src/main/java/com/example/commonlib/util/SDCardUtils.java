package com.example.commonlib.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by yinwei on 15-7-6.
 */

/**
 * SD卡相关的辅助类
 */
public class SDCardUtils {
    private static String TAG = "SDCardUtils==";

    private SDCardUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /***
     * 创建文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /***
     * 创建文件目录
     *
     * @param filePath
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getStrFromStream(InputStream inputStream) throws IOException {
        BufferedReader bFreader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = bFreader.readLine()) != null) {
            jsonString.append(line);
        }
        return jsonString.toString();
    }


    /**
     * Create a video thumbnail for a video. May return null if the video is
     * corrupt or the format is not supported.
     *
     * @param filePath the path of video file
     * @param kind     could be MINI_KIND or MICRO_KIND
     */
    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (bitmap == null) return null;

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512) {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
//            bitmap = extractThumbnail(bitmap,
//                    TARGET_SIZE_MICRO_THUMBNAIL,
//                    TARGET_SIZE_MICRO_THUMBNAIL,
//                    OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    // 遍历读取某个文件夹下所有视频
    public static List<File> getVideoFile(File fiePath, String suffix) {
        List<File> fileList = new ArrayList<>();
        FileFilter mfileFilter = new FileFilterBySuffix(suffix);
        return getFileList(fiePath, fileList, mfileFilter);
    }

    /**
     *  递归中的每一级都是用同一个“容器”和过滤器，故“容器”和过滤器不能在递归函数中定义，要作为递归函数参数
     *      * 
     *      * @param dir 被指定的文件夹
     *      * @param list 存储满足条件的File对象的“容器”
     *      * @param filter 过滤器（指定类型）
     *      
     */
    public static List<File> getFileList(File dir, List<File> list, FileFilter filter) {
        /** 通过listFiles()，获取dir的所有文件和文件夹对象 */
        File[] files = dir.listFiles();
        /** 获取指定文件夹中每一个文件和文件夹  */
        for (File file : files) {  /** 是不是文件夹？是，递归。不是，用过滤器获取指定文件。*/
            if (file.isDirectory()) {
                getFileList(file, list, filter);
            } else {
                if (filter.accept(file)) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    public static Boolean isContaincameraThumb(String mVideoThumbPath, String name) {
        Boolean iscontain = false;
        File file = new File(mVideoThumbPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles();
        // MyLog.e(TAg,"files---大小--"+files.length);

        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            //   MyLog.e(TAg,"filename---"+filename+"=====--------name---"+name);
            if (name.equals(filename)) {
                // MyLog.e(TAg,"包含此缩略图---");
                iscontain = true;
                return true;
            }
        }
        return iscontain;
    }

    public static class FileFilterBySuffix implements FileFilter {
        private String suffix;

        public FileFilterBySuffix(String suffix) {
            super();
            this.suffix = suffix;
        }

        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(suffix);
        }

    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }
    /**
     * 获取文件夹大小
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file){

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);
                }else{
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    public static boolean initMapStatu(String offLineMap) {
        File file = new File(offLineMap);
        if (!file.exists()) {
            MyLog.e(TAG, "amap文件 ---不存在----");
            return false;
        } else {
            if (file.isDirectory() && file.list().length > 0)
            {
                long mapFileSize = SDCardUtils.getFolderSize(file);
                MyLog.e(TAG, "amap文件 ---存在----" + mapFileSize);
                if (mapFileSize < 70L) {  // 如果存在 判断大小 大小不对
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }

        }
    }

    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private static final String CRASH_LOG_FILE_DIR = "faceControlAppLog";
    private static final String FILE_NAME_SUFFIX = ".txt";
    public static void saveExceptionToSDCard(String fileName, String requestUrl, Exception errorEx, String response) {
        try {
            //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡中
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.d(TAG, "dumpExceptionToSDCard: SD卡不存在");
                return;
            }
            File rootDir = new File(SDCARD_PATH);
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }

            File errorLogFileDir = new File(SDCARD_PATH + CRASH_LOG_FILE_DIR);
            if (!errorLogFileDir.exists()) {
                errorLogFileDir.mkdirs();
            }

            PrintWriter pw = null;
            try {
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(new Date(System.currentTimeMillis()));
                fileName = fileName + "-" + time;
                pw = new PrintWriter(new BufferedWriter(new FileWriter(errorLogFileDir + File.separator + fileName + FILE_NAME_SUFFIX)));
                pw.println(time);
                pw.println();
                if (TextUtils.isEmpty(requestUrl)) {
                    requestUrl = "未知";
                }
                pw.println("请求地址requestUrl==" + requestUrl);
                pw.println();
                if (response != null) {
                    pw.println("请求结果response==" + response);
                    pw.println();
                }
                if (errorEx != null) {
                    errorEx.printStackTrace(pw);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


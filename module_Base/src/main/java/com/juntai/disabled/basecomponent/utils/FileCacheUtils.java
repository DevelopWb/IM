package com.juntai.disabled.basecomponent.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.juntai.disabled.basecomponent.app.BaseApplication;
import com.juntai.disabled.basecomponent.mvp.IView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 文件
 *
 * @aouther Ma
 * @date 2019/3/21
 */
public class FileCacheUtils {

    public static long fileMaxLength = 500 * 1024 * 1024;//文件最大长度


    public static boolean isFileExists(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();

    }

    /**
     * 查看文件夹下是否有视频文件
     *
     * @param dirPath
     * @return
     */
    public static String isVideoFileExistsInDir(String dirPath,boolean isCatch) {

        dirPath = getAppVideoPath(isCatch) + dirPath;
        File file = new File(dirPath);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        File videoFile = files[0];
        if (videoFile.getName().contains(".download")) {
            return null;
        } else {
            return videoFile.getAbsolutePath();
        }

    }

    /**
     * 获取文件夹下的视频文件
     *
     * @param dirPath
     * @return
     */
    public static String getVideoFileInDir(String dirPath,boolean isCatch) {

        dirPath = getAppVideoPath(isCatch) + dirPath;
        File file = new File(dirPath);
        File[] files = file.listFiles();
        if (files.length > 0) {
            return file.listFiles()[0].getAbsolutePath();
        }
        return null;

    }


    /**
     * 获取app文件地址
     * isCatch  是否是缓存
     *
     * @return
     */
    public static String getAppPath(boolean isCatch) {
        File destDir = null;
        if (isCatch) {
            destDir = new File(BaseApplication.app.getExternalCacheDir().getPath() +
                    File.separator + BaseAppUtils.getAppName());
        } else {
            destDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + BaseAppUtils.getAppName());
        }

        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getAbsolutePath() + "/";
    }


    /**
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean
     */

    public static void copyFile(IView iView,String oldPath, String newPath,boolean isCatch) {
        RxScheduler.doTask(iView, new RxTask<String>() {
            @Override
            public String doOnIoThread() {
                String  str = null;
                try {

                    int bytesum = 0;

                    int byteread = 0;

                    File oldfile = new File(oldPath);

                    if (oldfile.exists()) { //文件存在时

                        InputStream inStream = new FileInputStream(oldPath); //读入原文件

                        FileOutputStream fs = new FileOutputStream(newPath);

                        byte[] buffer = new byte[1024];

                        int length;

                        while ((byteread = inStream.read(buffer)) != -1) {

                            bytesum += byteread; //字节数 文件大小

                            System.out.println(bytesum);

                            fs.write(buffer, 0, byteread);

                        }

                        inStream.close();

                    }
                    str = "已保存到本地";

                } catch (Exception e) {
                    str = "复制单个文件操作出错";

                    e.printStackTrace();

                }
                return str;
            }

            @Override
            public void doOnUIThread(String s) {
                if (!isCatch) {
                    ToastUtils.success(BaseApplication.app, s);
                    sendBroadcastToAlbum(BaseApplication.app,newPath);
                }

            }
        });


    }

    /**
     * 压缩图片存放目录
     *
     * @return
     */
    public static String getAppImagePath(boolean isCatch) {
        File destDir = new File(getAppPath(isCatch) + "image/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getAbsolutePath() + "/";
    }

    /**
     * 文件存放目录
     *
     * @return
     */
    public static String getAppFilePath(boolean isCatch) {
        File destDir = new File(getAppPath(isCatch) + "file");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getAbsolutePath() + "/";
    }

    /**
     * 压缩图片存放目录
     *
     * @return
     */
    public static String getMapShotImagePath() {
        return getAppImagePath(true) + "mapShot.png";
    }

    /**
     * 压缩图片存放目录
     *
     * @return
     */
    public static String getAppImagePath(String dirName,boolean isCatch) {
        File destDir = new File(getAppPath(isCatch) + "image/" + dirName + File.separator);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getAbsolutePath() + File.separator;
    }

    /**
     * 文件大小在设定范围内
     *
     * @return
     */
    public static boolean isOutInMaxLength(long fileSize) {
        return fileSize > fileMaxLength;
    }

    /**
     * 获取video缓存目录
     *
     * @return
     */
    public static String getAppVideoPath(boolean isCatch) {
        File destDir = new File(getAppPath(isCatch) + "video/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getAbsolutePath() + "/";
    }


    /**
     * 缓存bmp
     *
     * @param bmp
     * @return
     */
    public static String saveBitmap(Bitmap bmp) {
        FileOutputStream out;
        Calendar calendar = Calendar.getInstance();
        String bitmapName = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH)) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + 1)
                + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + String.valueOf(calendar.get(Calendar.MINUTE)) + String.valueOf(calendar.get(Calendar.SECOND)) + ".jpg";
        File file;
        String path = null;
        try {
            // 获取SDCard指定目录下
            String sdCardDir = getAppImagePath(false);
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            file = new File(sdCardDir, bitmapName);
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            path = sdCardDir + bitmapName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 缓存bmp
     *
     * @param bmp
     * @return
     */
    public static String saveBitmap(Bitmap bmp, String picName) {
        FileOutputStream out;
        File file;
        String path = null;
        try {
            // 获取SDCard指定目录下
            String sdCardDir = getAppImagePath(false);
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            file = new File(sdCardDir, picName);
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            path = sdCardDir + picName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }


    /**
     * 创建文件夹
     *
     * @param fileName
     */
    public static void creatFile(String fileName) {

        File file = new File(fileName);

        if (!file.exists()) {
            file.mkdirs();
        }
    }



    /**
     * 清除所有缓存
     */
    public static boolean clearAll(Context context) {
        try {
            deleteFile(new File(getAppPath(true)));
            deleteFile(new File(getAppImagePath(true)));
            deleteFile(new File(getAppVideoPath(true)));
            clearImageAllCache(context);
            return true;
        } catch (Exception e) {
            LogUtil.e("all-删除缓存文件失败=" + e.toString());
        }
        return false;
    }

    /**
     * 清除glide图片磁盘缓存
     */
    private static void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                        // BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除glide图片内存缓存
     */
    private static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除glide图片所有缓存
     */
    private static void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    private static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param file
     */
    private static void deleteFile(File file) {
        //判断是否为目录
        if (file.isDirectory()) {
            for (File ff : file.listFiles()) {
                deleteFile(ff);
            }
        } else {
            file.delete();
        }

    }


    /**
     * 将字符串写入到文本文件中
     *
     * @param strcontent 要保存的字符串
     * @param filePath   保存的路径
     * @param fileName   文件的名称
     */
    public static void writeToTxtFile(String strcontent, String filePath, String fileName) {


        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.a/";
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        String writedContent = getFileContent(fileName);
        if (!TextUtils.isEmpty(writedContent)) {
            return;
        }

        // 每次写入时，都换行写
        String strContent = MD5.md5(strcontent);
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    /**
     * 生成文件
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

    //生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    //读取指定目录下的所有TXT文件的文件内容
    public static String getFileContent(String fileName) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.a/";
        makeFilePath(path, fileName);
        File file = new File(path + fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String content = "";
        if (!file.isDirectory()) { //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为""文件
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line = "";
                        //分行读取
                        while ((line = buffreader.readLine()) != null) {
                            content += line;
//                            content += line + "\n";
                        }
                        instream.close();//关闭输入流
                    }
                } catch (java.io.FileNotFoundException e) {
                    Log.d("TestFile", "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.d("TestFile", e.getMessage());
                }
            }
        }
        return content;
    }

    /**
     * 获取bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取文件类型
     *
     * @param content
     * @return 1是图片 2是视频 3是发送的音频  4是音乐
     */
    public static int getFileType(String content) {
        if (content.contains(".jpg") || content.contains(".jpeg") || content.contains(".png")) {
            return 1;
        } else if (content.contains(".mp4") || content.contains(".mkv") || content.contains(".avi")) {
            return 2;
        } else if (content.contains(".voice")) {
            return 3;
        } else if (content.contains(".mp3") || content.contains(".wav")) {
            return 4;
        } else {
            return 100;
        }
    }


    /**
     * 获取指定目录下的所有文件(夹)
     *
     * @param filePath
     */
    public static List<File> getFiles(String filePath) {
        File f = new File(filePath);
        File[] files = f.listFiles();
        // 先排序
        List<File> resultList = null;
        if (files != null) {
            resultList = new ArrayList<File>();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!file.getName().startsWith(".")) {
                    resultList.add(file);
                }
            }

            //
            Collections.sort(resultList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    String name1 = o1.getName().toUpperCase();
                    char c1 = name1.toCharArray()[0];
                    String name11 = "";

                    if (c1 >= 0x4E00 && c1 <= 0x9FA5) {
                        name1 = "." + name11;
                    } else if (c1 < 48) {
                        name1 = "{" + name11;
                    } else if (c1 > 57 && c1 < 65) {
                        name1 = "{" + name11;
                    }

                    String name2 = o2.getName().toUpperCase();

                    char c2 = name2.toCharArray()[0];

                    String name22 = "";

                    if (c2 >= 0x4E00 && c2 <= 0x9FA5) {
                        name2 = "." + name22;
                    } else if (c2 < 48) {
                        name2 = "{" + name22;
                    } else if (c2 > 57 && c2 < 65) {
                        name2 = "{" + name22;
                    }
                    return name1.compareTo(name2);
                }
            });
        } else {
            Log.i("hnyer", filePath + "无子文件");
        }
        return resultList;
    }

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileDirSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileDirSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String formetFileSize(long fileS) {
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
     * 通知系统相册更新图库
     *
     * @param context
     * @param imagePath
     */
    public static void sendBroadcastToAlbum(Context context, String imagePath) {
        if (context != null && imagePath != null && imagePath.length() > 0) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(imageFile);
                if (uri != null && context != null) {
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                }
            }
        }
    }

}

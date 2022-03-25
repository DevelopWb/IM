package com.juntai.disabled.basecomponent.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.bean.BaseMenuBean;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.net.FileRetrofit;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.basecomponent.widght.BaseBottomDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/4/16 16:38
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/16 16:38
 */
public abstract class BaseDownLoadActivity<P extends BasePresenter> extends BaseMvpActivity<P> {
    private BaseBottomDialog baseBottomDialog;
    private String notice = "图片";
    private BaseBottomDialog.OnItemClick onItemClick;

    private OnFileDownloaded fileDownLoadCallBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String rightName = getDownloadTitleRightName();
        if (!TextUtils.isEmpty(rightName)) {
            if (rightName.contains("视频")) {
                getTitleRightTv().setText(getDownloadTitleRightName());
                getTitleRightTv().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initBottomDialog(getBaseBottomDialogMenus(getDownloadTitleRightName()), getDownLoadPath());
                    }
                });
            }
        }
    }

    /**
     * 设置回调
     *
     * @param fileDownLoadCallBack
     */
    public void setFileDownLoadCallBack(OnFileDownloaded fileDownLoadCallBack) {

        this.fileDownLoadCallBack = fileDownLoadCallBack;
    }

    /**
     * 获取标题栏右侧的内容  图片的时候可以传空
     *
     * @return
     */
    protected abstract String getDownloadTitleRightName();

    /**
     * 获取下载路径
     *
     * @return
     */
    protected abstract String getDownLoadPath();

    /**
     * 获取保存路径
     *
     * @return
     */
    private String getSavePath(String downloadPath) {
        String path = null;
        if (!TextUtils.isEmpty(downloadPath)) {
            if (2 == FileCacheUtils.getFileType(downloadPath)) {
                notice = "视频";
                path = FileCacheUtils.getAppVideoPath(false) + downloadPath.substring(downloadPath.lastIndexOf("/") + 1,
                        downloadPath.length());
            } else {
                notice = "图片";
                path = FileCacheUtils.getAppImagePath(false) + downloadPath.substring(downloadPath.lastIndexOf("/") + 1, downloadPath.length());
            }
        }
        return path;
    }

    /**
     * 初始化dialog
     */
    public void initBottomDialog(List<BaseMenuBean> arrays, final String downLoadPath) {

        if (baseBottomDialog == null) {
            baseBottomDialog = new BaseBottomDialog();
        }
        onItemClick = new BaseBottomDialog.OnItemClick() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                downloadFileContent(downLoadPath);
                releaseBottomSheetDialog();
            }
        };
        baseBottomDialog.initBottomDg(arrays, null, null, onItemClick);
        baseBottomDialog.show(getSupportFragmentManager(), "arrays");

    }

    /**
     * 初始化dialog
     */
    public void initBottomDialog(List<BaseMenuBean> arrays, BaseQuickAdapter adapter, LinearLayoutManager manager, BaseBottomDialog.OnItemClick onItemClick) {

        if (baseBottomDialog == null) {
            baseBottomDialog = new BaseBottomDialog();
        }
        baseBottomDialog.initBottomDg(arrays, adapter, manager, onItemClick);
        baseBottomDialog.show(getSupportFragmentManager(), "arrays");

    }

    /**
     * 下载文件
     */
    public void downloadFileContent(String downloadPath) {
        String savePath = getSavePath(downloadPath);
        final File file = new File(savePath);
        if (file.exists()) {
            String msg = String.format("%s%s%s", notice, "已保存至", savePath);
            ToastUtils.toast(mContext, msg);
            return;
        }
        downFileLogic(downloadPath, file);
    }

    /**
     * 下载图片
     * 有保存目录
     * 截图
     */
    public String downloadImageFile(String downloadPath,boolean isCatch) {
        if (TextUtils.isEmpty(downloadPath)) {
            return null;
        }
        String fileName = downloadPath.substring(downloadPath.lastIndexOf("/")+1,downloadPath.length());
        String savePath = FileCacheUtils.getAppImagePath(isCatch)+fileName;
        final File file = new File(savePath);
        downFileLogic(downloadPath, file);
        return savePath;
    }


    /**
     * 获取保存路径
     *
     * @param dirPath
     * @param downloadPath
     * @return
     */
    private String getSavePath(String dirPath, String downloadPath) {
        String savePath;
        String dir = FileCacheUtils.getAppImagePath(dirPath,false);
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        if (1 == FileCacheUtils.getFileType(downloadPath)) {
            savePath = dir + File.separator + downloadPath.substring(downloadPath.lastIndexOf(
                    "/") + 1, downloadPath.lastIndexOf("."));
        } else {
            //巡检图片  直接从crm读取的"xunjiantubiao" +
            savePath = dir + File.separator + downloadPath.substring(downloadPath.lastIndexOf("/") + 1,
                    downloadPath.length());

        }
        return savePath;
    }

    @SuppressLint("CheckResult")
    private void downFileLogic(String downloadPath, File file) {
        FileRetrofit.getInstance().getFileService()
                .getFile_GET(downloadPath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        try {
                            //responseBody里的数据只可以读取一次
                            //Log.e("ffffffff", "" + responseBody.bytes().length);
                            saveFileToLocal(file, responseBody.byteStream());

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable.toString());
                    }
                });
    }

    /**
     * 缓存文件到本地
     *
     * @param ins
     */
    public void saveFileToLocal(File file, InputStream ins) {
        try {
            LogUtil.d("----->in");
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
//            if (!file.getAbsolutePath().contains(FileCacheUtils.STREAM_THUMBNAIL)) {
//                String msg = String.format("%s%s%s", notice, "已下载至", file.getAbsolutePath());
//                showAlertDialogOfKnown(msg);
//            }
            if (fileDownLoadCallBack != null) {
                fileDownLoadCallBack.onFileDownloaded(file.getAbsolutePath());
            }
            LogUtil.d("----->ok");
            os.close();
            ins.close();
            sendBroadcastToAlbum(mContext,file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("----->error-" + e.toString());
        }
    }
    /**
     * 通知系统相册更新图库
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseBottomSheetDialog();
    }

    /**
     * 释放dialog
     */
    public void releaseBottomSheetDialog() {
        if (baseBottomDialog != null) {
            if (baseBottomDialog.isAdded()) {
                onItemClick = null;
                if (baseBottomDialog.getDialog().isShowing()) {
                    baseBottomDialog.dismiss();
                }
            }
        }
    }

    /**
     * 文件下载成功的回调
     */
    public interface OnFileDownloaded {

        void onFileDownloaded(String fileName);

    }

}

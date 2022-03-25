package com.juntai.wisdom.im.base.selectPics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.BaseAppUtils;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.GlideEngine4;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.bdmap.BaseRequestLocationActivity;
import com.juntai.wisdom.im.utils.CalendarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.functions.Consumer;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @Author: tobato
 * @Description: 作用描述  选择图片的基类
 * @CreateDate: 2020/5/21 15:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/21 15:04
 */
public abstract class BaseSelectPicsActivity<P extends BasePresenter> extends BaseRequestLocationActivity<P> {


    private int SELECT_PIC_RESULT = 1000;
    private int TAKE_PICTURE = 1001;
    public String cameraPath;
    private int compressedSize = 0;//被压缩的图片个数
    private List<String> icons = new ArrayList<>();


    protected abstract void selectedPicsAndEmpressed(List<String> icons);

    /**
     * 图片选择
     *
     * @param type          选择类型 0 可选可拍 1 拍照
     * @param activity
     * @param maxSelectable 最大图片选择数
     */
    @SuppressLint("CheckResult")
    public void choseImage(int type, Activity activity, int maxSelectable) {
        icons.clear();
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).compose(this.bindToLife()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    if (type == 0) {
                        Matisse.from(activity).choose(MimeType.ofImage()).showSingleMediaType(true)
                                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                                .countable(true).maxSelectable(maxSelectable).capture(true).captureStrategy(new CaptureStrategy(true, BaseAppUtils.getFileprovider()))
                                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED).thumbnailScale(0.85f).imageEngine(new GlideEngine4()).forResult(SELECT_PIC_RESULT);
                        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                    } else {
                        //打开照相机
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = getOutputMediaFileUri(mContext.getApplicationContext());
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //Android7.0添加临时权限标记，此步千万别忘了
                        openCameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                    }
                } else {
                    Toasty.info(mContext, "请给与相应权限").show();
                }
            }
        });
    }

    /**
     * 图片选择
     *
     * @param type          选择类型 0 可选可拍 1 拍照
     * @param activity
     * @param maxSelectable 最大图片选择数
     */
    @SuppressLint("CheckResult")
    public void choseImageAndVideo(int type, Activity activity, int maxSelectable) {
        icons.clear();
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).compose(this.bindToLife()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    if (type == 0) {
                        Matisse.from(activity).choose(MimeType.ofAll()).showSingleMediaType(true)
                                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                                .countable(true).maxSelectable(maxSelectable).capture(true).captureStrategy(new CaptureStrategy(true, BaseAppUtils.getFileprovider()))
                                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED).thumbnailScale(0.85f).imageEngine(new GlideEngine4()).forResult(SELECT_PIC_RESULT);
                        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                    } else {
                        //打开照相机
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = getOutputMediaFileUri(mContext.getApplicationContext());
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //Android7.0添加临时权限标记，此步千万别忘了
                        openCameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                    }
                } else {
                    Toasty.info(mContext, "请给与相应权限").show();
                }
            }
        });
    }

    /**
     * 图片选择
     *
     * @param type          选择类型 0 可选可拍 1 拍照
     * @param fragment
     * @param maxSelectable 最大图片选择数
     */
    @SuppressLint("CheckResult")
    public void choseImage(int type, Fragment fragment, int maxSelectable) {
        icons.clear();
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).compose(this.bindToLife()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    if (type == 0) {
                        Matisse.from(fragment).choose(MimeType.ofImage()).showSingleMediaType(true)
                                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                                .countable(true).maxSelectable(maxSelectable).capture(true).captureStrategy(new CaptureStrategy(true, BaseAppUtils.getFileprovider()))
                                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED).thumbnailScale(0.85f).imageEngine(new GlideEngine4()).forResult(SELECT_PIC_RESULT);
                        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                    } else {
                        //打开照相机
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = getOutputMediaFileUri(mContext.getApplicationContext());
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //Android7.0添加临时权限标记，此步千万别忘了
                        openCameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                    }
                } else {
                    Toasty.info(mContext, "请给与相应权限").show();
                }
            }
        });
    }

    /**
     * 获取拍照存储URI
     *
     * @param context
     * @return
     */
    private Uri getOutputMediaFileUri(Context context) {
        File mediaFile = null;
        // 文件名
        String filename = CalendarUtil.getCurrentTime("yyyyMMdd_HHmmss") + ".png";
        // file对象，注意路径要和resource xml里配置的一样
        mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator, filename);
        cameraPath = mediaFile.getAbsolutePath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// sdk >= 24  android7.0以上
            Uri contentUri = FileProvider.getUriForFile(context, BaseAppUtils.getFileprovider(),//与清单文件中android
                    // :authorities的值保持一致
                    mediaFile);//FileProvider方式或者ContentProvider。也可使用VmPolicy方式
            return contentUri;
        } else {
            return Uri.fromFile(mediaFile);//或者 Uri.isPaise("file://"+file.toString()

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == SELECT_PIC_RESULT && resultCode == RESULT_OK) {
//            imageCompress(Matisse.obtainPathResult(data));
            imageCompressContainVideo(Matisse.obtainPathResult(data));
        } else if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            imageCompress(cameraPath);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 图片压缩
     */
    private void imageCompress(List<String> paths) {
        compressedSize = 0;
        showLoadingDialog(mContext, false);
        Luban.with(mContext).load(paths).ignoreBy(100).setTargetDir(FileCacheUtils.getAppImagePath(true)).filter(new CompressionPredicate() {
            @Override
            public boolean apply(String path) {
                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
            }
        }).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {
                //  压缩开始前调用，可以在方法内启动 loading UI

            }

            @Override
            public void onSuccess(File file) {
                compressedSize++;
                //  压缩成功后调用，返回压缩后的图片文件
                icons.clear();
                icons.add(file.getPath());
                selectedPicsAndEmpressed(icons);
                if (compressedSize == paths.size()) {
                    stopLoadingDialog();
                }

            }

            @Override
            public void onError(Throwable e) {
                //  当压缩过程出现问题时调用
                compressedSize++;
                LogUtil.e("push-图片压缩失败");
                if (compressedSize == paths.size()) {
                    stopLoadingDialog();
                }

            }
        }).launch();
    }

    /**
     * 图片压缩
     */
    private void imageCompressContainVideo(List<String> paths) {
        compressedSize = 0;
        showLoadingDialog(mContext, false);
        List<String> pics = new ArrayList<>();
        for (String path : paths) {
            if (1 == FileCacheUtils.getFileType(path)) {
                imageCompress(path);
            } else if (2 == FileCacheUtils.getFileType(path)) {
                stopLoadingDialog();
                pics.add(path);
            } else {

            }

        }
        selectedPicsAndEmpressed(pics);
    }

    /**
     * 图片压缩
     */
    private void imageCompress(String path) {
        showLoadingDialog(mContext, false);
        Luban.with(mContext).load(path).ignoreBy(100).setTargetDir(FileCacheUtils.getAppImagePath(true)).filter(new CompressionPredicate() {
            @Override
            public boolean apply(String path) {
                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
            }
        }).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {
                //  压缩开始前调用，可以在方法内启动 loading UI

            }

            @Override
            public void onSuccess(File file) {
                //  压缩成功后调用，返回压缩后的图片文件
                List<String> pics = new ArrayList<>();
                pics.add(file.getPath());
                selectedPicsAndEmpressed(pics);
                stopLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("push-图片压缩失败");
                stopLoadingDialog();
            }
        }).launch();
    }

}

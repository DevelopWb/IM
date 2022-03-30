package com.juntai.disabled.basecomponent.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.juntai.disabled.basecomponent.R;
import com.juntai.disabled.basecomponent.bean.FileBaseInfoBean;
import com.juntai.disabled.basecomponent.mvp.IView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;


/**
 * 图片加载工具
 *
 * @aouther Ma
 * @date 2019/3/5
 */
public class ImageLoadUtil {

    /**
     * 加载本地图片
     *
     * @param context
     * @param recouse
     * @param view
     */
    public static void loadImage(Context context, int recouse, ImageView view) {
        Glide.with(context).load(recouse).into(view);
    }

    /**
     * 加载图片
     */
    public static void loadImage(Context context, Bitmap bitmap, ImageView view) {
        Glide.with(context).load(bitmap).into(view);
    }

    /**
     * @param context
     * @param url
     * @param view
     */
    public static void loadImageWithCache(Context context, String url, int replacePic, ImageView view) {
        Glide.with(context).load(url).skipMemoryCache(false)
                .apply(new RequestOptions().error(replacePic).placeholder(replacePic))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(view);
    }

    /**
     * @param url 缓存到本地
     */
    public static void setGlideDownloadFileToLocal(IView iView, Context context, String url, boolean isCatch) {
        if (TextUtils.isEmpty(url) || !url.contains("/")) {
            return;
        }
        String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        RxScheduler.doTask(iView, new RxTask<File>() {
            @Override
            public File doOnIoThread() {
                try {
                    return Glide.with(context)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (Exception ex) {
                    return null;
                }
            }

            @Override
            public void doOnUIThread(File result) {
                if (result == null) {
                    return;
                }
                //这里得到的就是我们要的文件了，接下来是保存文件。
                //filepath是目标保存文件的路径，根据自己的项目需要去配置
                //最后一步就是复制文件咯
                FileCacheUtils.copyFile(iView, result.getAbsolutePath(), FileCacheUtils.getAppImagePath(isCatch) + fileName, isCatch);
            }
        });
    }

    /**
     * @param context
     * @param url     内存缓存和硬盘缓存
     * @param view
     */
    public static void loadImageCache(Context context, String url, ImageView view) {
        try {
            int urlInt = Integer.parseInt(url);
            Glide.with(context).load(urlInt).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.drawable.nopicture).into(view);
        } catch (NumberFormatException ex) {
            Glide.with(context).load(url).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.drawable.nopicture).into(view);
        }
    }


    /**
     * @param context
     * @param url     内存缓存和硬盘缓存
     * @param view
     */
    public static void loadImageCache(Context context, int url, ImageView view) {
        Glide.with(context).load(url).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.drawable.nopicture).into(view);
    }

    /**
     * @param context
     * @param url     加载网络视频的时候 不能使用硬盘缓存
     * @param view
     */
    public static void loadImageNoCache(Context context, String url, ImageView view) {
        Glide.with(context).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(view);
    }


    /**
     * @param context
     * @param url
     * @param view
     */
    public static void loadImageNoCrash(Context context, String url, ImageView view) {
        Glide.with(context).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(view);

    }

    /**
     * @param context
     * @param url
     * @param view
     */
    public static void loadImageNoCrash(Context context, String url, ImageView view, int loading, int error) {
        Glide.with(context).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).placeholder(loading).dontAnimate().error(error).into(view);
    }


    /**
     * @param context
     * @param url
     * @param error
     * @param view
     */
    public static void loadImage(Context context, String url, int error, ImageView view) {
        Glide.with(context).load(url).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE).apply(new RequestOptions().error(error).placeholder(error)).into(view);
    }

    /**
     * @param context
     * @param url
     * @param view
     */
    public static void loadImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).skipMemoryCache(false)
                .apply(new RequestOptions().error(R.drawable.nopicture).placeholder(R.drawable.nopicture))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(view);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param error
     * @param placeholder
     * @param view
     */
    public static void loadCircularImage(Context context, String url, int error, int placeholder, ImageView view) {
        Glide.with(context).load(url).apply(new RequestOptions().error(error).placeholder(placeholder).circleCrop()).into(view);
    }

    public static void loadCentercropImage(Context context, int url, ImageView view) {
        Glide.with(context).load(url).apply(new RequestOptions().optionalCenterCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(view);
    }

    public static void loadCentercropImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).apply(new RequestOptions().optionalCenterCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(view);
    }


    /**
     * 加载圆角方形图片
     *
     * @param context
     * @param url
     * @param view
     */
    public static void loadSquareImage(Context context, String url, ImageView view) {
        loadHeadPic(context,url,view,false);
    }
    /**
     * 加载头像
     * isCircle 是否是圆形头像
     */
    public static void loadHeadPic(Context mContext, String headUrl,ImageView imageView, boolean isCircle) {
        String content = null;
        if (TextUtils.isEmpty(headUrl)) {
            return;
        }
        if (headUrl.contains("/")) {
            content = headUrl.substring(headUrl.lastIndexOf("/") + 1, headUrl.length());
        }
        if (!FileCacheUtils.isFileExists(FileCacheUtils.getAppImagePath(true) + content)) {
            //本地没有缓存
            if (isCircle) {
                ImageLoadUtil.loadCirImgWithCrash(mContext, headUrl, imageView, R.mipmap.default_user_head_icon);
            } else {
                Glide.with(mContext).load(headUrl).apply(new RequestOptions()
                        .error(R.drawable.nopicture).placeholder(R.drawable.nopicture)
                        .transform(new RoundedCorners(15)).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(imageView);
            }

            ImageLoadUtil.setGlideDownloadFileToLocal(null, mContext, headUrl, true);

        } else {
            if (isCircle) {
                ImageLoadUtil.loadCirImgWithCrash(mContext, FileCacheUtils.getAppImagePath(true) + content, imageView, R.mipmap.default_user_head_icon);
            } else {
                Glide.with(mContext).load(FileCacheUtils.getAppImagePath(true) + content).apply(new RequestOptions()
                        .error(R.drawable.nopicture).placeholder(R.drawable.nopicture)
                        .transform(new RoundedCorners(15)).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(imageView);
            }

        }
    }
    /**
     * 加载圆角方形图片
     *
     * @param context
     * @param res
     * @param view
     */
    public static void loadSquareImage(Context context, int res, ImageView view) {
        Glide.with(context).load(res).apply(new RequestOptions()

                .error(R.drawable.nopicture).placeholder(R.drawable.nopicture)
                .transform(new RoundedCorners(15)).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(view);

    }

    /**
     * 加载圆角方形图片
     *
     * @param context
     * @param url
     * @param view
     */
    public static void loadSquareImage(Context context, String url, int defaultPic, ImageView view) {
        Glide.with(context).load(url).apply(new RequestOptions().error(defaultPic).placeholder(defaultPic)
                .transform(new RoundedCorners(15)).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(view);

    }

    /**
     * 加载圆形图片,无缓存
     *
     * @param context
     * @param url
     * @param view
     * @param placeholder
     * @param error
     */
    public static void loadCirImgNoCrash(Context context, String url, ImageView view, int placeholder, int error) {
        Glide.with(context).load(url).apply(new RequestOptions().error(error).placeholder(placeholder).circleCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(view);
    }

    /**
     * 加载圆形图
     */
    public static void loadCirImgWithCrash(Context context, String url, ImageView view,int defauleDrawble) {
        Glide.with(context).load(url).apply(new RequestOptions().error(defauleDrawble).placeholder(defauleDrawble).circleCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE).skipMemoryCache(false)).into(view);
    }


    /**
     * 加载圆形图片,无缓存
     *
     * @param context
     * @param view
     */
    public static void loadCirImgNoCrash(Context context, int resId, ImageView view) {
        Glide.with(context).load(resId).apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(view);
    }

    public interface BitmapCallBack {
        void getBitmap(Bitmap bitmap);
    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param path
     * @param error
     * @param callback
     */
    public static void getBitmap(Context context, String path, int error, BitmapCallBack callback) {
        Glide.with(context).asBitmap().error(error).load(path).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                LogUtil.e("onResourceReady");
                callback.getBitmap(resource);
            }
        });
    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param path
     * @param error
     * @param width    宽
     * @param height   高
     * @param callback
     */
    public static void getBitmap(Context context, String path, int error, int width, int height, BitmapCallBack callback) {
        Glide.with(context).asBitmap().error(error).load(path).into(new SimpleTarget<Bitmap>(width, height) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                LogUtil.e("onResourceReady");
                callback.getBitmap(resource);
            }
        });
    }

    /**
     * 加载圆角图片
     * 加载网络视频的时候 不能使用硬盘缓存
     *
     * @param context
     * @param url
     * @param view
     * @param placeholder
     * @param type        1视频，2图片
     */
    public static void loadRoundCornerImg(Context context, String url, ImageView view, int placeholder, int type) {
        int corners = 10;
        if (type == 1) {
            corners = 7;
        } else {
            corners = 15;
        }
        RoundedCorners roundedCorners = new RoundedCorners(corners);//数字为圆角度数
        RequestOptions coverRequestOptions;
        if (type == 1) {
            coverRequestOptions = new RequestOptions()
                    .error(R.drawable.nopicture_video)
                    .placeholder(placeholder)
                    .override(350, 200)
                    .centerCrop()
                    .transform(new CenterCrop(), roundedCorners)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .skipMemoryCache(true);//不做内存缓存
        } else {
            coverRequestOptions = new RequestOptions()
                    .error(R.drawable.nopicture)
                    .placeholder(placeholder)
                    .override(300, 200)
                    .centerCrop()
                    .transform(new CenterCrop(), roundedCorners)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .skipMemoryCache(false);//不做内存缓存
        }
        Glide.with(context)
                .load(url)
                .apply(coverRequestOptions)
//                .thumbnail(0.1f)//缩略图
                .into(view);
    }

    /**
     * 加载列表图片(包括视频),磁盘缓存
     *
     * @param context
     * @param url         图片地址
     * @param view        填充view
     * @param type        类型，1视频，2图片
     * @param width       裁剪宽 120
     * @param height      裁剪高 160
     * @param corners     圆角度数 10
     * @param error       错误占位图
     * @param placeholder 加载占位图
     */
    public static void loadImageForList(Context context, String url, ImageView view, int type, int width, int height, int corners, int error, int placeholder) {
        RoundedCorners roundedCorners = new RoundedCorners(corners);//数字为圆角度数
        RequestOptions coverRequestOptions = new RequestOptions()
                .error(error)//R.drawable.nopicture
                .placeholder(placeholder)
                .override(width, height)
                .centerCrop()
                .transform(new CenterCrop(), roundedCorners)
                .diskCacheStrategy(type == 1 ? DiskCacheStrategy.AUTOMATIC : DiskCacheStrategy.RESOURCE)
                .skipMemoryCache(false);//内存缓存

        Glide.with(context)
                .load(url)
                .apply(coverRequestOptions)
//                .thumbnail(0.1f)//缩略图
                .into(view);
    }

    public static void loadImageForList(Context context, String url, ImageView view, int error, int placeholder) {
        loadImageForList(context, url, view, 2, 160, 120, 1, error, placeholder);
    }

    public static void loadImageForList(Context context, String url, ImageView view, int type, int width, int height, int corners) {
        loadImageForList(context, url, view, type, width, height, corners, R.drawable.nopicture, R.drawable.nopicture);
    }

    public static void loadImageForList(Context context, String url, ImageView view, int type, int width, int height) {
        loadImageForList(context, url, view, type, width, height, 1);
    }

    public static void loadImageForList(Context context, String url, ImageView view, int type) {
        loadImageForList(context, url, view, type, 160, 120);
    }

    public static void loadCornerImageForList(Context context, String url, ImageView view, int corners) {
        loadImageForList(context, url, view, 2, 160, 120, corners);
    }

    public static void loadImageForList(Context context, String url, ImageView view) {
        loadImageForList(context, url, view, 2);
    }

    /**
     * 将本地视频的封面图转成base64
     * @param videoPath
     */
    public static String getVideoThumbnailOfBase64(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);// videoPath 本地视频的路径
        Bitmap bitmap = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        /**
         *bitmap转为base64
         */
//先将bitmap转为byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,baos);
        byte[] bytes = baos.toByteArray();

//将byte[]转为base64
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }


    /**
     *   context 上下文
     *   uri 视频地址
     *   imageView 设置image
     *   frameTimeMicros 获取某一时间帧
     */
    public static void loadVideoScreenshot(final Context context, String uri, ImageView imageView, OnImageLoadSuccess onImageLoadSuccess) {
        RequestOptions requestOptions = RequestOptions.frameOf(1*1000*1000)
                .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.nopicture)//R.drawable.nopicture
                .skipMemoryCache(true);//内存缓存
        Glide.with(context).load(uri).apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //  图片记载完成
                        if (onImageLoadSuccess != null) {
                            onImageLoadSuccess.loadSuccess(0, 0);
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void loadVideoScreenshotSquareImage(final Context context, String uri, ImageView imageView, long frameTimeMicros, OnImageLoadSuccess onImageLoadSuccess) {
        RoundedCorners roundedCorners = new RoundedCorners(15);//数字为圆角度数
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros)
                .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.nopicture)//R.drawable.nopicture
                .skipMemoryCache(true);//内存缓存
        Glide.with(context).load(uri)
                .transform(new CenterCrop(), roundedCorners)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //  图片记载完成
                        if (onImageLoadSuccess != null) {
                            onImageLoadSuccess.loadSuccess(0, 0);
                        }
                        return false;
                    }
                })
                .into(imageView);
    }


    /**
     * 获取视频文件的基本信息
     *
     * @param filePath
     */
    public static FileBaseInfoBean getVideoFileBaseInfo(String filePath) {
        String rotation = null;
        String videoDuration = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(filePath);
        rotation = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        videoDuration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        String width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        mediaMetadataRetriever.release();
        if ("0".equals(rotation)) {
            if (Integer.parseInt(width)*2<Integer.parseInt(height)) {
                rotation = "90";
            }
        }
        return new FileBaseInfoBean(rotation, videoDuration);

    }


    public static void getExifOrientation(Context mContext, String filepath, OnImageLoadSuccess onImageLoadSuccess) {
        //获取图片真正的宽高
        Glide.with(mContext)
                .asBitmap()//强制Glide返回一个Bitmap对象
                .load(filepath)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        if (onImageLoadSuccess != null) {
                            onImageLoadSuccess.loadSuccess(width, height);
                        }
                    }
                });
    }

    public interface OnImageLoadSuccess {

        void loadSuccess(int width, int height);
    }

}

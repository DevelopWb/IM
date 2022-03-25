package com.juntai.wisdom.im.utils;

import android.content.Context;
import android.content.Intent;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 分享
 * Created by Ma
 * on 2019/7/4
 */
public class ShareTool {
    /**
     * 分享文字
     * @param context
     * @param string
     */
    public static void shareText(Context context,String string){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,string);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent,"分享到"));
    }
    public static void shareImage(Context context,String path){
        //由文件得到uri
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, path);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent,"分享到"));
    }

    /**
     * 第三方分享  规则
     * @param context
     */
    public static void shareForMob(Context context,String title,String url,String content,String imagepath){
//        Log.e("ffff",title+" 1 " + url + " ");
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(title);
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(imagepath);
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 第三方分享
     * @param context
     */
    public static void shareForMob(Context context, String title, String url, String content, String imagepath, PlatformActionListener callback){
//        Log.e("ffff",title+" 1 " + url + " ");
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(callback);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(title);
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(imagepath);
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(context.getApplicationContext());
    }
}

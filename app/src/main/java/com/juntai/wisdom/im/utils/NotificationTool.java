package com.juntai.wisdom.im.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.juntai.disabled.federation.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Ma
 * on 2019/5/8
 */
public class NotificationTool {

    public static int CHAT_NOTICY_ID = 1000;
    //是否展示notification
    public static boolean SHOW_NOTIFICATION = false;

    public static String MSG_CHANNEL_ID = "chaoshiju_msg_channel";//普通消息通知id
    public static String MSG_CHANNEL_NAME = "在线聊天消息";//普通消息通知


    /**
     * @param context
     * @param id:通知显示id
     * @param content:通知显示内容
     * @param imageRes:图标
     * @param ongo:是否        设置为一个正在进行的通知，此时用户无法清除通知
     * @param intent
     */
    public static Notification sendNotifMessage( int msgType, Context context, int id, String title, String content, int imageRes, boolean ongo, Intent intent, String otherNickName) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        switch (msgType) {
            case 0:
                break;
            case 1:
                content = "[图片]";
                break;
            case 2:
                content = "[视频]";
                break;
            case 3:
                content = "[语音]";
                break;
            case 4:
                content = "[视频通话]";
                break;
            case 5:
                content = "[语音通话]";
                break;
            case 6:
                content = "[位置]";
                break;
            case 7:
                // : 2021-12-08 这个地方还需要优化下
                content = title + "向你推荐了" + otherNickName;
                break;
            case 8:
                content = "[文件]";
                break;
            case 9:
                content = "[聊天记录]";
                break;
            default:
                break;
        }
        // 创建PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //手机设置的默认提示音
        Uri   uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.msg);
        Notification notification = new NotificationCompat.Builder(context, MSG_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(uri)
                .setVibrate(new long[]{0, 500, 1000})
                .setWhen(System.currentTimeMillis())
                .setOngoing(ongo)//设置为一个正在进行的通知，此时用户无法清除通知
                .setSmallIcon(imageRes)
                .setContentIntent(notifyPendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), imageRes))
                .setAutoCancel(true)
                .build();
        manager.notify(id, notification);
        return notification;
    }

    public static void cancelAllNotice(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
    }


    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context, String channelId, String channelName, int importance) {
        Uri   uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.msg);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setShowBadge(true);
        channel.enableLights(true);//是否在桌面icon右上角展示小圆点
        channel.setLightColor(Color.RED); //小圆点颜色
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        //8.0之后  自定义铃声
        channel.setSound(uri, audioAttributes);
        channel.setVibrationPattern(new long[]{0, 500, 1000});
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

}

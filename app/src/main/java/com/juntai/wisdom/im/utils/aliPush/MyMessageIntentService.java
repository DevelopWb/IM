package com.juntai.wisdom.im.utils.aliPush;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.AliyunMessageIntentService;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.List;
import java.util.Map;


/**
 * Created by liyazhou on 17/8/22.
 * 为避免推送广播被系统拦截的小概率事件,我们推荐用户通过IntentService处理消息互调,接入步骤:
 * 1. 创建IntentService并继承AliyunMessageIntentService
 * 2. 覆写相关方法,并在Manifest的注册该Service
 * 3. 调用接口CloudPushService.setPushIntentService
 * 详细用户可参考:https://help.aliyun.com/document_detail/30066.html#h2-2-messagereceiver-aliyunmessageintentservice
 */

public class MyMessageIntentService extends AliyunMessageIntentService {

    private static final String TAG = "MyMessageIntentService";


    /**
     * 推送通知的回调方法
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        Log.i(TAG, "收到一条推送通知 ： " + title + ", summary:" + summary);
        //来电亮屏
//        ScreensUtils.isScree(context);
//        //通知fragment 重新请求网络数据
//        EventManager.sendStringMsg(EventBusProperty.SELECTED_ROOM);

    }

    /**
     * 推送消息的回调方法
     *
     * @param context
     * @param cPushMessage
     */
    @Override
    protected void onMessage(Context context, CPushMessage cPushMessage) {
        Log.i(TAG, "收到一条推送消息 ： " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());


    }

    /**
     * 从通知栏打开通知的扩展处理
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.i(TAG, "onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
        Log.i(TAG, "summary" + summary + extraMap + "extramap");

//        Gson gson = new Gson();
//        PushBean pushBean = gson.fromJson(extraMap, PushBean.class);
//
//        if (pushBean != null) {
//            String noticeType = pushBean.getNoticeType();
//            if (isAppRunning(context, context.getPackageName())) {
//
//                if (noticeType.equals(com.ghs.ghshome.models.push.AliPushManager.PUSH_HOUSE_KEEPER)) {
//                    //使用eventbus是为了解决三星手机点击notification不跳转的问题
//                    EventManager.sendStringMsg(EventBusProperty.INTENT_TO_HOUSE_KEEPER_NOTICE);
//                } else if (noticeType.equals(com.ghs.ghshome.models.push.AliPushManager.PUSH_OFFICAL_MSG)) {
//                    EventManager.sendStringMsg(EventBusProperty.INTENT_TO_OFFICAL_NOTICE);
//                } else if (noticeType.equals(com.ghs.ghshome.models.push.AliPushManager.PUSH_USER_AUTH)) {
//                    String  state = pushBean.getUserState();
//                    if (StrUtils.isStringValueOk(state)) {
//                        if (!"-1".equals(state)) {
//                            EventManager.sendStringMsg(EventBusProperty.INTENT_TO_HOUSE_KEEPER_NOTICE);
//                        }
//                    }
//                }
//
//            } else {
//                Intent intent = new Intent(context, SplashActivity.class);
//                intent.putExtra("noticeType", noticeType);
//                startActivity(intent);
//            }

//        }

    }

    /**
     * 无动作通知点击回调。当在后台或阿里云控制台指定的通知动作为无逻辑跳转时,通知点击回调为onNotificationClickedWithNoAction而不是onNotificationOpened
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.i(TAG, "onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }

    /**
     * 通知删除回调
     *
     * @param context
     * @param messageId
     */
    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.i(TAG, "onNotificationRemoved ： " + messageId);
    }

    /**
     * 应用处于前台时通知到达回调。注意:该方法仅对自定义样式通知有效,相关详情请参考https://help.aliyun.com/document_detail/30066.html#h3-3-4-basiccustompushnotification-api
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     * @param openType
     * @param openActivity
     * @param openUrl
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.i(TAG, "onNotificationReceivedInApp ： " + " : " + title + " : " + summary + "  " + extraMap + " : " + openType + " : " + openActivity + " : " + openUrl);
    }

    /**
     * 判读app是否是活跃状态
     */
    public boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }


}

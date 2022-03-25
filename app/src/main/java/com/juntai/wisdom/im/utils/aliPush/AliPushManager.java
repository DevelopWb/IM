package com.juntai.wisdom.im.utils.aliPush;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.juntai.disabled.federation.R;

public class AliPushManager {

    private static volatile AliPushManager instance = null;
    private CloudPushService pushService;
    private String TAG = "AliPushManager";

    private AliPushManager() {

    }

    public static AliPushManager getInstance() {
        if (instance == null) {
            synchronized (AliPushManager.class) {
                if (instance == null) {
                    instance = new AliPushManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化推送对象
     */
    public void initPush(Context context) {
        PushServiceFactory.init(context);
        pushService = PushServiceFactory.getCloudPushService();
        pushService.setPushIntentService(MyMessageIntentService.class);
        pushRegister(context);
        initConfig(context);

    }

    /**
     *
     */
    private void initConfig(Context context) {
        if (pushService != null) {
            //设置通知栏图标
            pushService.setNotificationLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon));
//            //设置状态栏图标  图标不能太大  图片背景透明 要不不显示
            pushService.setNotificationSmallIcon(R.mipmap.app_icon);
        }
    }

    /**
     * 注册
     */
    public void pushRegister(Context context) {
        if (pushService != null) {
            pushService.register(context, new CommonCallback() {
                @Override
                public void onSuccess(String s) {

                    Log.i(TAG, s);

                }

                @Override
                public void onFailed(String s, String s1) {
                    Log.i(TAG, s);
                }
            });

        }
    }

    /**
     * 绑定账号
     */
    public void bindUUID(String uuid) {

        if (pushService != null) {
            pushService.bindAccount(uuid, new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.i(TAG, s);
                }

                @Override
                public void onFailed(String errorCode, String errorMsg) {
                    Log.i(TAG, errorMsg);
                }
            });

        }
    }

    /**
     * 解绑标签
     */

    public void unbindAccount(String tags) {
        if (pushService != null) {
            pushService.unbindAccount(new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.i(TAG, s);
                }

                @Override
                public void onFailed(String errorCode, String errorMsg) {
                    Log.i(TAG, errorMsg);
                }
            });
        }


    }











}

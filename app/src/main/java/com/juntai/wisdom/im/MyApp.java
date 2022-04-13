package com.juntai.wisdom.im;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.juntai.disabled.basecomponent.app.BaseApplication;
import com.juntai.disabled.basecomponent.utils.EventManager;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.basecomponent.utils.RomUtil;
import com.juntai.disabled.video.ModuleVideo_Init;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.socket.MyWebSocket;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.NotificationTool;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.mob.MobSDK;
import com.orhanobut.hawk.Hawk;
import com.siyee.oscvpush.PushConstants;
import com.siyee.oscvpush.base.PushAdapter;
import com.siyee.oscvpush.huawei.HWPushRegister;
import com.siyee.oscvpush.mi.MiPushRegister;
import com.siyee.oscvpush.model.Message;
import com.siyee.oscvpush.model.Token;
import com.siyee.oscvpush.oppo.OppoPushRegister;
import com.siyee.oscvpush.util.LogUtils;
import com.siyee.oscvpush.vivo.VivoPushRegister;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;

import java.util.HashMap;


/**
 * @aouther Ma
 * @date 2019/3/12
 */
public class MyApp extends BaseApplication {
    public static MyApp app;
    public boolean isFinish = false;
    public static long lastClickTime;//上次点击按钮时间
    public static int timeLimit = 1000;






    /**
     * 推送注册回调
     */
    private PushAdapter adapter = new PushAdapter() {

        @Override
        public void onRegister(int resCode, Token regId) {
            if (resCode == PushConstants.SUCCESS_CODE && regId != null) {
                String pushRegId = regId.getRegId();
                Hawk.put(HawkProperty.DEV_REGID,pushRegId);
            }
        }
        /**
         *  收到系统推送的消息
         * @param msg
         */
        @Override
        public void onMessage(Message msg) {
            String data = msg.getJsonData();
            if (!TextUtils.isEmpty(data)) {
                MessageBodyBean messageBody = GsonTools.changeGsonToBean(data, MessageBodyBean.class);
                //系统推送的时候  视频通话 音频通话都掉不起来 所有这个地方的逻辑去掉了
                if (4==messageBody.getMsgType()||5==messageBody.getMsgType()) {
                    return;
                }
                //未读
                messageBody.setRead(false);
                HawkProperty.setRedPoint(getApplicationContext(), 1);
                EventManager.getEventBus().post(messageBody);
                MyWebSocket.showNotification(app,messageBody);
            }
        }

        @Override
        public void onMessageClicked(Message msg) {
            //小米能监听到这个回调   华为没有
            LogUtils.e("收到离线消息");
//            Map<String, String> map = msg.getExtra();
//            String data = map.get("data");
//            if (!TextUtils.isEmpty(data)) {
//                LogUtils.e("收到离线消息");
//                Intent intent = new Intent();
//                MessageBodyBean messageBody = GsonTools.changeGsonToBean(data, MessageBodyBean.class);
//                ContactBean contactBean = new ContactBean();
//                contactBean.setMessageBodyBean(messageBody);
//                contactBean.setId(messageBody.getFromUserId());
//                contactBean.setNickname(messageBody.getFromNickname());
//                contactBean.setRemarksNickname(messageBody.getFromNickname());
//                contactBean.setAccountNumber(messageBody.getFromAccount());
//                contactBean.setHeadPortrait(messageBody.getFromHead());
//
//                switch (messageBody.getChatType()) {
//                    case 1:
//                        if (4==messageBody.getMsgType()||5==messageBody.getMsgType()) {
//                            intent.setClass(getApplicationContext(), VideoRequestActivity.class);
//                            intent.putExtra(VideoRequestActivity.IS_SENDER, true)
//                                    .putExtra(BaseActivity.BASE_PARCELABLE, messageBody);
//                        }else {
//                            intent.setClass(getApplicationContext(), PrivateChatActivity.class);
//                            intent.putExtra(BaseActivity.BASE_PARCELABLE, contactBean);
//                        }
//
//                        break;
//                    case 2:
//                        intent.setClass(getApplicationContext(), GroupChatActivity.class);
//                        // : 2022-01-22 群聊消息的跳转  这个地方的逻辑需要优化  请求群消息详情
//                        intent.putExtra(BaseActivity.BASE_ID, messageBody.getGroupId());
//                        break;
//                    case 4:
//                        intent.setClass(getApplicationContext(), NewFriendsApplyActivity.class);
//                        break;
//                    default:
//                        break;
//                }
//                intent.putExtra(BaseActivity.BASE_PARCELABLE, contactBean);
//                startActivity(intent);
//            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Hawk.init(this).build();
        MobSDK.init(this);
        //Video模块初始化
        ModuleVideo_Init.init();
        //百度地图初始化
        SDKInitializer.initialize(this);
        //        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        ObjectBox.init(this);
        initPushRegist();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationTool.createNotificationChannel(getApplicationContext(), NotificationTool.MSG_CHANNEL_ID, NotificationTool.MSG_CHANNEL_NAME, importance);
        }

        initDownload();
        initTPS();
    }

    private void initTPS() {
        //腾讯tbs
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //  Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                //  Auto-generated method stub
            }
        };
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        QbSdk.setDownloadWithoutWifi(true);

    }

    private void initDownload() {
        // 1、创建Builder
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);

// 2.配置Builder
// 配置下载文件保存的文件夹
        builder.configFileDownloadDir(FileCacheUtils.getAppFilePath(false));
// 配置同时下载任务数量，如果不配置默认为2
        builder.configDownloadTaskSize(5);
// 配置失败时尝试重试的次数，如果不配置默认为0不尝试
        builder.configRetryDownloadTimes(5);
// 开启调试模式，方便查看日志等调试相关，如果不配置默认不开启
        builder.configDebugMode(true);
// 配置连接网络超时时间，如果不配置默认为15秒
        builder.configConnectTimeout(25000);// 25秒

// 3、使用配置文件初始化FileDownloader
        FileDownloadConfiguration configuration = builder.build();
        FileDownloader.init(configuration);
    }

    public void initPushRegist() {
            if (RomUtil.isEmui()) {
                //华为
                HWPushRegister.getInstance(this).register();
            } else if (RomUtil.isVivo()) {
                VivoPushRegister.getInstance(this).register();
            } else if (RomUtil.isOppo()) {
                OppoPushRegister.getInstance(this).register("a8aaa44a557b420f921aa4079ec1774b", "34eecd930b2849edbc5162305fee687e");
            } else {
                //小米
                MiPushRegister.getInstance(this).register("2882303761520089591", "5432008920591");
            }
            if (RomUtil.isEmui()) {
                //华为
                HWPushRegister.getInstance(this).setPushCallback(adapter);
            } else if (RomUtil.isVivo()) {
                VivoPushRegister.getInstance(this).setPushCallback(adapter);
            } else if (RomUtil.isOppo()) {
                OppoPushRegister.getInstance(this).setPushCallback(adapter);
            } else {
                //小米
                MiPushRegister.getInstance(this).setPushCallback(adapter);
            }

    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)


    @Override
    public void appBackground(boolean isBackground, Activity activity) {
        if (isBackground && !isFinish) {
            //            NitoficationTool.sendNotif(activity,
            //                    11,
            //                    "挂起",
            //                    BaseAppUtils.getAppName() + "服务正在运行",
            //                    R.mipmap-xxhdpi.logo,
            //                    true,
            //                    new Intent(activity, MainActivity.class));
        } else {
            //变为前台
            //            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //            manager.cancelAll();
        }
    }


    /**
     * 防止暴力点击
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < timeLimit) {
            return true;
        }
        lastClickTime = time;
        return false;
    }




}

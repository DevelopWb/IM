package com.juntai.wisdom.im.webSocket;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-10-31 10:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-10-31 10:34
 */

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.EventManager;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.AddContractOrGroupMsgBean;
import com.juntai.wisdom.im.bean.BaseWsMessageBean;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.UnReadMsgsBean;
import com.juntai.wisdom.im.bean.VideoActivityMsgBean;
import com.juntai.wisdom.im.chat_module.chat.PrivateChatActivity;
import com.juntai.wisdom.im.chat_module.chat.videocall.VideoRequestActivity;
import com.juntai.wisdom.im.chat_module.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.contact.newfriends.NewFriendsApplyActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.NotificationTool;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.orhanobut.hawk.Hawk;
import com.rabtman.wsmanager.WsManager;
import com.rabtman.wsmanager.listener.WsStatusListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okio.ByteString;

/**
 * MyWsManager管理类
 */
public class MyWsManager {

    private final String TAG = this.getClass().getSimpleName();

    private static MyWsManager wsManager;
    private static WsManager myWsManager;
    private WsManager.Builder builder;

    private Context mContext;

    public MyWsManager setWsUrl(String wsUrl) {
        if (builder != null) {
            builder.wsUrl(wsUrl);
        }
        return this;
    }

    //单例
    public static MyWsManager getInstance() {
        if (wsManager == null) {
            synchronized (MyWsManager.class) {
                if (wsManager == null) {
                    wsManager = new MyWsManager();
                }
            }
        }
        return wsManager;
    }

    public MyWsManager init(Context context) {
        this.mContext = context;
        builder = new WsManager.Builder(context)
                .client(new OkHttpClient().newBuilder()
                        .pingInterval(5, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true).build())
                //.needReconnect(true)                  //是否需要重连
                //.setHeaders(null)                     //设置请求头
                //.setReconnnectIMaxTime(30*1000)       //设置重连最大时长
                .needReconnect(true);
        return this;
    }

    public void startConnect() {
        try {
            if (myWsManager == null) {
                myWsManager = builder.build();
                myWsManager.setWsStatusListener(wsStatusListener);
            }
            if (!myWsManager.isWsConnected()) {
                myWsManager.startConnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "myWsManager-----Exception");
        }
    }

    private WsStatusListener wsStatusListener = new WsStatusListener() {
        @Override
        public void onOpen(Response response) {
            Log.d(TAG, "myWsManager-----onOpen");
            Log.e("onOpen", "服务器连接成功");
        }

        @Override
        public void onMessage(String text) {
            Log.e("onMessage", text + "\n\n");
            //在这里接收和处理收到的ws数据吧
            if (!TextUtils.isEmpty(text)) {
                BaseWsMessageBean baseWsMessageBean = GsonTools.changeGsonToBean(text, BaseWsMessageBean.class);
                int jsonType = baseWsMessageBean.getTypeJson();
                if (1 == jsonType) {
                    Log.d(TAG, "MyWsManager-----onMessage---未读消息");
                    UnReadMsgsBean unReadMsgsBean = GsonTools.changeGsonToBean(text, UnReadMsgsBean.class);
                    EventManager.getEventBus().post(unReadMsgsBean);
                } else {
                    MessageBodyBean messageBody = GsonTools.changeGsonToBean(text, MessageBodyBean.class);
                    //视频通话相关
                    if (!TextUtils.isEmpty(messageBody.getEvent())) {
                        if (VideoRequestActivity.EVENT_CAMERA_REQUEST.equals(messageBody.getEvent())) {
                            Log.d(TAG, "MyWsManager-----onMessage---视频通话消息");
                            Intent intent =
                                    new Intent(mContext, VideoRequestActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .putExtra(VideoRequestActivity.IS_SENDER, false)
                                            .putExtra(BaseActivity.BASE_PARCELABLE,
                                                    messageBody);
                            mContext.startActivity(intent);
                        } else {
                            EventManager.getEventBus().post(new VideoActivityMsgBean(messageBody));
                        }
                        return;
                    }
                    //未读
                    messageBody.setRead(false);
                    HawkProperty.setRedPoint(mContext, 1);
                    switch (messageBody.getChatType()) {
                        case 1:
                            if (checkLocalContact(messageBody)) {
                                EventManager.getEventBus().post(messageBody);
                            } else {
                                EventManager.getEventBus().post(new AddContractOrGroupMsgBean(messageBody));
                            }
                            break;
                        case 2:
                            if (ObjectBox.checkGroupIsExist(messageBody.getGroupId())) {
                                EventManager.getEventBus().post(messageBody);
                            } else {
                                EventManager.getEventBus().post(new AddContractOrGroupMsgBean(messageBody));
                            }
                            break;
                        default:
                            break;
                    }
                    if (NotificationTool.SHOW_NOTIFICATION) {
                        showNotification(messageBody);
                    }
                }
            }
        }


        /**
         * 检测本地群组
         * @param messageBodyBean
         * @return
         */
        private boolean checkLocalContact(MessageBodyBean messageBodyBean) {
            List<ContactBean> contactBeans = Hawk.get(HawkProperty.getContactListKey());
            List<Integer> contactIds = new ArrayList<>();
            for (ContactBean contactBean : contactBeans) {
                contactIds.add(contactBean.getId());
            }
            contactIds.add(UserInfoManager.getUserId());
            return contactIds.contains(messageBodyBean.getFromUserId());
        }

        @Override
        public void onMessage(ByteString bytes) {
            Log.d(TAG, "MyWsManager-----onMessage");
        }

        @Override
        public void onReconnect() {
            Log.d(TAG, "MyWsManager-----onReconnect");
            Log.e("onReconnect", "服务器重连接中...");
        }

        @Override
        public void onClosing(int code, String reason) {
            Log.d(TAG, "MyWsManager-----onClosing");
            Log.e("onClosing", "服务器连接关闭中...");

            //这一步我个人认为是比较骚的操作,上面提及了设备会出现断开后无法连接的情况,那这种无法连接的情
            //况我发现有可能会卡在这个关闭过程中,因为如果是确实断开后会确实的启动重连机制,至于还有别的坑
            //我后面会补充;这里主要的目的就死让他跳出这个关闭中的状态,确实的关闭了ws先
            if (myWsManager != null) {
                myWsManager.stopConnect();
                myWsManager.startConnect();
            }

        }

        @Override
        public void onClosed(int code, String reason) {
            Log.d(TAG, "MyWsManager-----onClosed");
            Log.e("onClosed", "服务器连接已关闭...");
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            Log.d(TAG, "MyWsManager-----onFailure");
            Log.e("onFailure", "服务器连接失败...");
        }
    };

    public void showNotification(MessageBodyBean messageBody) {
        Log.d(TAG, "MyWsManager-----onMessage---发送通知");

        ContactBean contactBean = new ContactBean();
        String content = messageBody.getContent();
        Intent intent = new Intent();
        contactBean.setMessageBodyBean(messageBody);
        contactBean.setId(messageBody.getFromUserId());
        contactBean.setNickname(messageBody.getFromNickname());
        contactBean.setRemarksNickname(messageBody.getFromNickname());
        contactBean.setAccountNumber(messageBody.getFromAccount());
        contactBean.setHeadPortrait(messageBody.getFromHead());
        String title = null;
        switch (messageBody.getChatType()) {
            case 1:
                title = UserInfoManager.getContactRemarkName(messageBody);
                if (4 == messageBody.getMsgType() || 5 == messageBody.getMsgType()) {
                    intent.setClass(mContext, VideoRequestActivity.class);
                    intent.putExtra(VideoRequestActivity.IS_SENDER, true)
                            .putExtra(BaseActivity.BASE_PARCELABLE, messageBody);
                } else {
                    intent.setClass(mContext, PrivateChatActivity.class);
                    intent.putExtra(BaseActivity.BASE_PARCELABLE, contactBean);
                }

                break;
            case 2:
                title = messageBody.getGroupName();
                intent.setClass(mContext, GroupChatActivity.class);
                // : 2022-01-22 群聊消息的跳转  这个地方的逻辑需要优化  请求群消息详情
                intent.putExtra(BaseActivity.BASE_ID, messageBody.getGroupId());
                break;
            case 4:
                intent.setClass(mContext, NewFriendsApplyActivity.class);
                title = "好友申请";
                content = "您有新的好友申请";
                break;
            default:
                break;
        }

        // : 2021-12-08   这个地方需要获取到发送方在本地的备注名
        NotificationTool.sendNotifMessage(messageBody.getChatType(),messageBody.getGroupName(),messageBody.getMsgType(), mContext, messageBody.getFromUserId(), title, content, R.mipmap.app_icon, false, intent, messageBody.getOtherNickname());
    }

    //发送ws数据
    public void sendDataD(String content) {

        if (myWsManager != null && myWsManager.isWsConnected()) {
            boolean isSend = myWsManager.sendMessage(content);
            if (isSend) {
                Log.e("onOpen sendMessage", "发送成功");
            } else {
                Log.e("onOpen sendMessage", "发送失败");
            }
        } else {
            if (myWsManager != null) {
                myWsManager.stopConnect();
                myWsManager.startConnect();
            }
        }

    }

    //断开ws
    public void disconnect() {
        if (myWsManager != null)
            myWsManager.stopConnect();
        myWsManager = null;
    }


    public interface OnMessageStatus {
        void onMessage(MessageBodyBean messageBodyBean);

        void onMessage(UnReadMsgsBean unReadMsgsBean);
    }

}
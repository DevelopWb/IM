package com.juntai.wisdom.im.socket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.EventManager;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.BaseWsMessageBean;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.UnReadMsgsBean;
import com.juntai.wisdom.im.chat_module.chat.PrivateChatActivity;
import com.juntai.wisdom.im.chat_module.chat.videocall.VideoRequestActivity;
import com.juntai.wisdom.im.chat_module.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.contact.newfriends.NewFriendsApplyActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.NotificationTool;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.orhanobut.hawk.Hawk;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.X509TrustManager;

/**
 * Created by dds on 2019/7/26.
 * android_shuai@163.com
 */
public class MyWebSocket extends WebSocketClient {
    private final static String TAG = "dds_WebSocket";
    private final IEvent iEvent;
    private boolean connectFlag = false;
    public final static String EVENT_CAMERA_OFFER = "offer";
    public final static String EVENT_CAMERA_ANSWER = "answer";
    public final static String EVENT_CAMERA_CANDIDATE = "candidate";
    /**
     * 被邀请
     */
    public final static String EVENT_CAMERA_REQUEST = "request";
    /**
     * join  加入两人的房间
     */
    public final static String EVENT_CAMERA_ACCESS = "access";
    /**
     * 取消拨出
     */
    public final static String EVENT_CAMERA_FINISH_SENDER = "sender_finish";
    /**
     * 拒绝邀请
     */
    public final static String EVENT_CAMERA_FINISH_RECEIVER = "receiver_finish";

    public MyWebSocket(URI serverUri, IEvent event) {
        super(serverUri);
        this.iEvent = event;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("dds_error", "onClose:" + reason + "remote:" + remote);
        if (connectFlag) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.iEvent.reConnect();
        }

    }

    @Override
    public void onError(Exception ex) {
        Log.e("dds_error", "onError:" + ex.toString());
        connectFlag = false;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("dds_info", "onOpen");
        this.iEvent.onOpen();
        connectFlag = true;
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, message);
        handleMessage(message);
    }


    public void setConnectFlag(boolean flag) {
        connectFlag = flag;
    }

    // ---------------------------------------处理接收消息-------------------------------------

    private void handleMessage(String message) {
//        Map map = JSON.parseObject(message, Map.class);
//        EventBean eventBean = GsonTools.changeGsonToBean(message,EventBean.class);
//        String eventName =eventBean.getEventName();
//        if (eventName == null) return;

        //在这里接收和处理收到的ws数据吧
        if (!TextUtils.isEmpty(message)) {
            BaseWsMessageBean baseWsMessageBean = GsonTools.changeGsonToBean(message, BaseWsMessageBean.class);
            int jsonType = baseWsMessageBean.getTypeJson();
            if (1 == jsonType) {
                Log.d(TAG, "MyWsManager-----onMessage---未读消息");
                UnReadMsgsBean unReadMsgsBean = GsonTools.changeGsonToBean(message, UnReadMsgsBean.class);
                EventManager.getEventBus().post(unReadMsgsBean);
            } else {
                MessageBodyBean messageBody = GsonTools.changeGsonToBean(message, MessageBodyBean.class);
                if (4==messageBody.getMsgType()||5==messageBody.getMsgType()) {
                    //音视频通话
                    //发起视频
                    String event = messageBody.getEvent();
                    switch (event) {
//                        case "__create":
//                            //接收方
//                            // 创建房间  收到对方创建房间后 直接给回复 __peers
//                            handleCreatRoom(messageBody);
//                            break;
//                        case "__peers":
//                            // 发送方
//                            //收到接收方发送的消息
//                            handlePeers(messageBody);
//                            //处理完之后会发送邀请
//                            break;
//
//                        case "__invite":
//                            // 接收方  被邀请
//                            //私聊的时候 接收到邀请
//                            handleInvite(messageBody);
//                            return;
//                            break;
                        case EVENT_CAMERA_REQUEST:
                            //接收到邀请视频通话的请求
                            handleInvite(messageBody);
                            break;
                        case EVENT_CAMERA_FINISH_SENDER:
                            handleCancel(messageBody);
                            break;
                        case EVENT_CAMERA_FINISH_RECEIVER:
                            handleReject(messageBody);
                            break;
                        case EVENT_CAMERA_OFFER:
                            handleOffer(messageBody);
                            break;
                        case EVENT_CAMERA_ANSWER:
                            handleAnswer(messageBody);
                            break;
                        case EVENT_CAMERA_CANDIDATE:
                            handleIceCandidate(messageBody);
                            break;
                        case "__ring":
                            // 响铃
                            handleRing(messageBody);
                            break;

//                        case "__new_peer":
//                            // 新人入房间  别人加入自己创建的房间
//                            handleNewPeer(map);
//                            break;
                        case EVENT_CAMERA_ACCESS:
                            // 新人入房间  别人加入自己创建的房间
                            handleNewPeer(messageBody);
                            break;
                        case "__leave":
                            // 离开房间
                            handleLeave(messageBody);
                            break;
                        case "__audio":
                            // 切换到语音
                            handleTransAudio(messageBody);
                            break;
                        case "__disconnect":
                            // 意外断开
                            handleDisConnect(messageBody);
                            break;
                        default:
                            break;
                    }

                }

//
//                //视频通话相关
//                if (!TextUtils.isEmpty(messageBody.getEvent())) {
//                    if (VideoRequestActivity.EVENT_CAMERA_REQUEST.equals(messageBody.getEvent())) {
//                        Log.d(TAG, "MyWsManager-----onMessage---视频通话消息");
//                        Intent intent =
//                                new Intent(mContext, VideoRequestActivity.class)
//                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                        .putExtra(VideoRequestActivity.IS_SENDER, false)
//                                        .putExtra(BaseActivity.BASE_PARCELABLE,
//                                                messageBody);
//                        mContext.startActivity(intent);
//                    } else {
//                        EventManager.getEventBus().post(new VideoActivityMsgBean(messageBody));
//                    }
//                    return;
//                }
//                //未读
//                messageBody.setRead(false);
//                HawkProperty.setRedPoint(mContext, 1);
//                switch (messageBody.getChatType()) {
//                    case 1:
//                        if (checkLocalContact(messageBody)) {
//                            EventManager.getEventBus().post(messageBody);
//                        } else {
//                            EventManager.getEventBus().post(new AddContractOrGroupMsgBean(messageBody));
//                        }
//                        break;
//                    case 2:
//                        if (ObjectBox.checkGroupIsExist(messageBody.getGroupId())) {
//                            EventManager.getEventBus().post(messageBody);
//                        } else {
//                            EventManager.getEventBus().post(new AddContractOrGroupMsgBean(messageBody));
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                if (NotificationTool.SHOW_NOTIFICATION) {
//                    // TODO: 2022/4/13 暂时注释掉
////                    showNotification(messageBody);
//                }
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
    private void handleDisConnect(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String fromId = (String) data.get("fromID");
//            this.iEvent.onDisConnect(fromId);
//        }
    }

    private void handleTransAudio(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String fromId = (String) data.get("fromID");
//            this.iEvent.onTransAudio(fromId);
//        }
    }


    private void handleIceCandidate(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String userID = (String) data.get("fromID");
//            String id = (String) data.get("id");
//            int label = (int) data.get("label");
//            String candidate = (String) data.get("candidate");
//            this.iEvent.onIceCandidate(userID, id, label, candidate);
//        }
    }

    private void handleAnswer(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String sdp = (String) data.get("sdp");
//            String userID = (String) data.get("fromID");
//            this.iEvent.onAnswer(userID, sdp);
//        }
    }

    private void handleOffer(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String sdp = (String) data.get("sdp");
//            String userID = (String) data.get("fromID");
//            this.iEvent.onOffer(userID, sdp);
//        }
    }

    private void handleReject(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String fromID = (String) data.get("fromID");
//            int rejectType = Integer.parseInt(String.valueOf(data.get("refuseType")));
//            this.iEvent.onReject(fromID, rejectType);
//        }
    }

    private void handlePeers(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String you = (String) data.get("you");
//            String connections = (String) data.get("connections");
//            int roomSize = (int) data.get("roomSize");
//            this.iEvent.onPeers(you, connections, roomSize);
//        }
    }

    private void handleNewPeer(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String userID = (String) data.get("userID");
//            this.iEvent.onNewPeer(userID);
//        }
    }

    private void handleRing(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String fromId = (String) data.get("fromID");
//            this.iEvent.onRing(fromId);
//        }
    }

    private void handleCancel(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String inviteID = (String) data.get("inviteID");
//            String userList = (String) data.get("userList");
//            this.iEvent.onCancel(inviteID);
//        }
    }

    private void handleInvite(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String room = (String) data.get("room");
//            boolean audioOnly = (boolean) data.get("audioOnly");
//            String inviteID = (String) data.get("inviteID");
//            String userList = (String) data.get("userList");
//            this.iEvent.onInvite(room, audioOnly, inviteID, userList);
//        }
    }

    private void handleLeave(MessageBodyBean messageBodyBean) {
//        Map data = (Map) map.get("data");
//        if (data != null) {
//            String fromID = (String) data.get("fromID");
//            this.iEvent.onLeave(fromID);
//        }
    }

    /**
     * ------------------------------发送消息----------------------------------------
     */
    public void createRoom(String room, int roomSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("eventName", "__create");

        Map<String, Object> childMap = new HashMap<>();
        childMap.put("room", room);
        childMap.put("roomSize", roomSize);
        childMap.put("userID", String.valueOf(UserInfoManager.getUserId()));

        map.put("data", childMap);
        JSONObject object = new JSONObject(map);
        final String jsonString = object.toString();
        Log.d(TAG, "send-->" + jsonString);

//        MessageBodyBean messageBodyBean = SendMsgUtil.getPrivateMsg(4,)


        send(jsonString);
    }

    // 发送邀请
    public void sendInvite(String room,  List<String> users, boolean audioOnly) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("eventName", "__invite");
//
//        Map<String, Object> childMap = new HashMap<>();
//        childMap.put("room", room);
//        childMap.put("audioOnly", audioOnly);
//        childMap.put("inviteID", String.valueOf(UserInfoManager.getUserId()));
//
//        String join = StringUtil.listToString(users);
//        childMap.put("userList", join);
//
//        map.put("data", childMap);
//        JSONObject object = new JSONObject(map);
//        final String jsonString = object.toString();
//        Log.d(TAG, "send-->" + jsonString);
//        send(jsonString);
    }

    // 取消邀请
    public void sendCancel(String mRoomId, List<String> users) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("eventName", "__cancel");
//
//        Map<String, Object> childMap = new HashMap<>();
//        childMap.put("inviteID", useId);
//        childMap.put("room", mRoomId);
//
//        String join = StringUtil.listToString(users);
//        childMap.put("userList", join);
//
//
//        map.put("data", childMap);
//        JSONObject object = new JSONObject(map);
//        final String jsonString = object.toString();
//        Log.d(TAG, "send-->" + jsonString);
//        send(jsonString);
    }

    // 发送响铃通知
    public void sendRing( String toId, String room) {
        Map<String, Object> map = new HashMap<>();
        map.put("eventName", "__ring");

        Map<String, Object> childMap = new HashMap<>();
        childMap.put("fromID", String.valueOf(UserInfoManager.getUserId()));
        childMap.put("toID", toId);
        childMap.put("room", room);


        map.put("data", childMap);
        JSONObject object = new JSONObject(map);
        final String jsonString = object.toString();
        Log.d(TAG, "send-->" + jsonString);
        send(jsonString);
    }

    //加入房间
    public void sendJoin(String room) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("eventName", "__join");
//
//        Map<String, String> childMap = new HashMap<>();
//        childMap.put("room", room);
//        childMap.put("userID", String.valueOf(UserInfoManager.getUserId()));
//
//
//        map.put("data", childMap);
//        JSONObject object = new JSONObject(map);
//        final String jsonString = object.toString();
//        Log.d(TAG, "send-->" + jsonString);
//        send(jsonString);
    }

    // 拒接接听
    public void sendRefuse(String room, String inviteID,  int refuseType) {
        Map<String, Object> map = new HashMap<>();
        map.put("eventName", "__reject");

        Map<String, Object> childMap = new HashMap<>();
        childMap.put("room", room);
        childMap.put("toID", inviteID);
        childMap.put("fromID", String.valueOf(UserInfoManager.getUserId()));
        childMap.put("refuseType", String.valueOf(refuseType));

        map.put("data", childMap);
        JSONObject object = new JSONObject(map);
        final String jsonString = object.toString();
        Log.d(TAG, "send-->" + jsonString);
        send(jsonString);
    }

    // 离开房间
    public void sendLeave( String room, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("eventName", "__leave");

        Map<String, Object> childMap = new HashMap<>();
        childMap.put("room", room);
        childMap.put("fromID", String.valueOf(UserInfoManager.getUserId()));
        childMap.put("userID", userId);

        map.put("data", childMap);
        JSONObject object = new JSONObject(map);
        final String jsonString = object.toString();
        Log.d(TAG, "send-->" + jsonString);
        if (isOpen()) {
            send(jsonString);
        }
    }

    // send offer
    public void sendOffer( String userId, String sdp) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> childMap = new HashMap<>();
        childMap.put("sdp", sdp);
        childMap.put("userID", userId);
        childMap.put("fromID", String.valueOf(UserInfoManager.getUserId()));
        map.put("data", childMap);
        map.put("eventName", "__offer");
        JSONObject object = new JSONObject(map);
        final String jsonString = object.toString();
        Log.d(TAG, "send-->" + jsonString);
        send(jsonString);
    }

    // send answer
    public void sendAnswer( String userId, String sdp) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> childMap = new HashMap<>();
        childMap.put("sdp", sdp);
        childMap.put("fromID", String.valueOf(UserInfoManager.getUserId()));
        childMap.put("userID", userId);
        map.put("data", childMap);
        map.put("eventName", "__answer");
        JSONObject object = new JSONObject(map);
        final String jsonString = object.toString();
        Log.d(TAG, "send-->" + jsonString);
        send(jsonString);
    }

    // send ice-candidate
    public void sendIceCandidate( String userId, String id, int label, String candidate) {
        Map<String, Object> map = new HashMap<>();
        map.put("eventName", "__ice_candidate");

        Map<String, Object> childMap = new HashMap<>();
        childMap.put("userID", userId);
        childMap.put("fromID", String.valueOf(UserInfoManager.getUserId()));
        childMap.put("id", id);
        childMap.put("label", label);
        childMap.put("candidate", candidate);

        map.put("data", childMap);
        JSONObject object = new JSONObject(map);
        final String jsonString = object.toString();
        Log.d(TAG, "send-->" + jsonString);
        if (isOpen()) {
            send(jsonString);
        }
    }

    // 切换到语音
    public void sendTransAudio( String userId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> childMap = new HashMap<>();
        childMap.put("fromID", String.valueOf(UserInfoManager.getUserId()));
        childMap.put("userID", userId);
        map.put("data", childMap);
        map.put("eventName", "__audio");
        JSONObject object = new JSONObject(map);
        final String jsonString = object.toString();
        Log.d(TAG, "send-->" + jsonString);
        send(jsonString);
    }

    // 断开重连
    public void sendDisconnect(String room,  String userId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> childMap = new HashMap<>();
        childMap.put("fromID", String.valueOf(UserInfoManager.getUserId()));
        childMap.put("userID", userId);
        childMap.put("room", room);
        map.put("data", childMap);
        map.put("eventName", "__disconnect");
        JSONObject object = new JSONObject(map);
        final String jsonString = object.toString();
        Log.d(TAG, "send-->" + jsonString);
        send(jsonString);
    }

    // 忽略证书
    public static class TrustManagerTest implements X509TrustManager {

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }




    public static void showNotification(Context mContext, MessageBodyBean messageBody) {
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

        switch (messageBody.getChatType()) {
            case 1:
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
                intent.setClass(mContext, GroupChatActivity.class);
                // : 2022-01-22 群聊消息的跳转  这个地方的逻辑需要优化  请求群消息详情
                intent.putExtra(BaseActivity.BASE_ID, messageBody.getGroupId());
                break;
            case 4:
                intent.setClass(mContext, NewFriendsApplyActivity.class);
                content = "您有新的好友申请";
                break;
            default:
                break;
        }

        // : 2021-12-08   这个地方需要获取到发送方在本地的备注名
        NotificationTool.sendNotifMessage(messageBody.getMsgType(), mContext, messageBody.getFromUserId(), UserInfoManager.getContactRemarkName(messageBody), content, R.mipmap.app_icon, false, intent, messageBody.getOtherNickname());
    }


}

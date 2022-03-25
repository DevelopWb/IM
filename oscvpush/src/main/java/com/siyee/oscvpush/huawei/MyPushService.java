/*
 *  Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.siyee.oscvpush.huawei;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.huawei.hms.push.SendException;
import com.siyee.oscvpush.PushConstants;
import com.siyee.oscvpush.model.Message;
import com.siyee.oscvpush.model.Target;
import com.siyee.oscvpush.model.Token;
import com.siyee.oscvpush.util.LogUtils;
import com.siyee.oscvpush.util.NullUtils;

import java.util.Arrays;

public class MyPushService extends HmsMessageService {
    private static final String TAG = "PushDemoLog";
    private final static String CODELABS_ACTION = "com.huawei.codelabpush.action";

    /**
     * When an app calls the getToken method to apply for a token from the server,
     * if the server does not return the token during current method calling, the server can return the token through this method later.
     * This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
     *
     * @param token token
     */
    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "received refresh token:" + token);
        // send the token to your app server.
        if (!TextUtils.isEmpty(token)) {
            // This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
            refreshedTokenToServer(token);
        }
        if (NullUtils.checkNull(token)) {
            HWPushRegister.getPushCallback().onRegister(PushConstants.UNKNOWN_CODE, null);
            return;
        }
        LogUtils.e(token);
        if (HWPushRegister.getPushCallback() != null) {
            HWPushRegister.getPushCallback().onRegister(PushConstants.SUCCESS_CODE, Token.buildToken(Target.HUAWEI, token));

        }
    }

    private void refreshedTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }
    /**
     * This method is used to receive downstream data messages.
     * This method callback must be completed in 10 seconds. Otherwise, you need to start a new Job for callback processing.
     * 透传消息走这个方法   或者通知栏消息foreground_show = true时回调  但是这时候 状态栏没有通知
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        LogUtils.e("onMessageReceived is called");
        if (remoteMessage == null) {
            LogUtils.e("Received message entity is null!");
            return;
        }
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        // build msg
        final Message message = Message.buildMessageForTarget(Target.HUAWEI);
        message.setNotifyType(0);
        message.setMessageID(remoteMessage.getMessageId());
        message.setExtra(remoteMessage.getDataOfMap());
        message.setJsonData(remoteMessage.getData());
        if (notification != null) {
            LogUtils.e("\n getImageUrl: " + notification.getImageUrl()
                    + "\n getTitle: " + notification.getTitle()
                    + "\n getTitleLocalizationKey: " + notification.getTitleLocalizationKey()
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.getTitleLocalizationArgs())
                    + "\n getBody: " + notification.getBody()
                    + "\n getBodyLocalizationKey: " + notification.getBodyLocalizationKey()
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.getBodyLocalizationArgs())
                    + "\n getIcon: " + notification.getIcon()
                    + "\n getSound: " + notification.getSound()
                    + "\n getTag: " + notification.getTag()
                    + "\n getColor: " + notification.getColor()
                    + "\n getClickAction: " + notification.getClickAction()
                    + "\n getChannelId: " + notification.getChannelId()
                    + "\n getLink: " + notification.getLink()
                    + "\n getNotifyId: " + notification.getNotifyId());

            message.setTitle(notification.getTitle());
            message.setMessage(notification.getBody());
            if (HWPushRegister.getPushCallback() != null) {
                HWPushRegister.getPushCallback().onMessage(message);
            }

        }
    }

    private void startWorkManagerJob(RemoteMessage message) {
        Log.d(TAG, "Start new Job processing.");
    }

    private void processWithin10s(RemoteMessage message) {
        Log.d(TAG, "Processing now.");
    }

    @Override
    public void onMessageSent(String msgId) {
        Log.i(TAG, "onMessageSent called, Message id:" + msgId);
        Intent intent = new Intent();
        intent.setAction(CODELABS_ACTION);
        intent.putExtra("method", "onMessageSent");
        intent.putExtra("msg", "onMessageSent called, Message id:" + msgId);

        sendBroadcast(intent);
    }

    @Override
    public void onSendError(String msgId, Exception exception) {
        Log.i(TAG, "onSendError called, message id:" + msgId + ", ErrCode:"
                + ((SendException) exception).getErrorCode() + ", description:" + exception.getMessage());

        Intent intent = new Intent();
        intent.setAction(CODELABS_ACTION);
        intent.putExtra("method", "onSendError");
        intent.putExtra("msg", "onSendError called, message id:" + msgId + ", ErrCode:"
                + ((SendException) exception).getErrorCode() + ", description:" + exception.getMessage());

        sendBroadcast(intent);
    }

    @Override
    public void onTokenError(Exception e) {
        super.onTokenError(e);
    }
}

package com.juntai.wisdom.im.chat_module.voip.singleCall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.dds.skywebrtc.CallSession;
import com.dds.skywebrtc.EnumType;
import com.dds.skywebrtc.SkyEngineKit;
import com.dds.skywebrtc.except.NotInitializedException;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.SettingsCompat;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.chat_module.voip.VoipEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * Created by dds on 2018/7/26.
 * 单人通话界面
 */
public class SingleCallActivity extends BaseAppActivity implements CallSession.CallSessionCallback {

    public static final String EXTRA_TARGET = "targetId";
    public static final String EXTRA_MO = "isOutGoing";
    public static final String EXTRA_AUDIO_ONLY = "audioOnly";
    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_FROM_FLOATING_VIEW = "fromFloatingView";
    private static final String TAG = "SingleCallActivity";

    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isOutgoing;
    private String targetId;
    boolean isAudioOnly;
    private boolean isFromFloatingView;

    private SkyEngineKit gEngineKit;

    private SingleCallFragment currentFragment;
    private String room;

    public static Intent getCallIntent(Context context, String targetId, boolean isOutgoing, String inviteUserName,
                                       boolean isAudioOnly, boolean isClearTop) {
        Intent voip = new Intent(context, SingleCallActivity.class);
        voip.putExtra(SingleCallActivity.EXTRA_MO, isOutgoing);
        voip.putExtra(SingleCallActivity.EXTRA_TARGET, targetId);
        voip.putExtra(SingleCallActivity.EXTRA_USER_NAME, inviteUserName);
        voip.putExtra(SingleCallActivity.EXTRA_AUDIO_ONLY, isAudioOnly);
        voip.putExtra(SingleCallActivity.EXTRA_FROM_FLOATING_VIEW, false);
        if (isClearTop) {
            voip.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        return voip;
    }


    public static void openActivity(Context context, String targetId, boolean isOutgoing, String inviteUserName,
                                    boolean isAudioOnly, boolean isClearTop) {
        Intent intent = getCallIntent(context, targetId, isOutgoing, inviteUserName, isAudioOnly, isClearTop);
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_single_call;
    }

    @SuppressLint("CheckResult")
    @Override
    public void initView() {
        try {
            gEngineKit = SkyEngineKit.Instance();
        } catch (NotInitializedException e) {
            SkyEngineKit.init(new VoipEvent()); //重新初始化
            try {
                gEngineKit = SkyEngineKit.Instance();
            } catch (NotInitializedException ex) {
                finish();
            }
        }
        final Intent intent = getIntent();
        targetId = intent.getStringExtra(EXTRA_TARGET);
        isFromFloatingView = intent.getBooleanExtra(EXTRA_FROM_FLOATING_VIEW, false);
        isOutgoing = intent.getBooleanExtra(EXTRA_MO, false);
        isAudioOnly = intent.getBooleanExtra(EXTRA_AUDIO_ONLY, false);

        if (isFromFloatingView) {
            Intent serviceIntent = new Intent(this, FloatingVoipService.class);
            stopService(serviceIntent);
            init(targetId, false, isAudioOnly, false);
        } else {
            // 权限检测
            String[] per;
            if (isAudioOnly) {
                per = new String[]{Manifest.permission.RECORD_AUDIO};
            } else {
                per = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
            }

            new RxPermissions(this)
                    .request(per)
                    .delay(1, TimeUnit.SECONDS)
                    .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                //所有权限通过
                                // 权限同意
                                init(targetId, isOutgoing, isAudioOnly, false);
                            } else {
                                //有一个权限没通过
                                Toast.makeText(mContext, "权限被拒绝", Toast.LENGTH_SHORT).show();
                                // 权限拒绝
                                finish();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    });

        }

    }

    @Override
    public void initData() {

    }


    @Override
    public void onBackPressed() {
        //通话时不能按返回键，跟微信同现象，只能挂断结束或者接听
//        super.onBackPressed();
//        if (currentFragment != null) {
//            if (currentFragment instanceof FragmentAudio) {
//                ((FragmentAudio) currentFragment).onBackPressed();
//            } else if (currentFragment instanceof FragmentVideo) {
//                ((FragmentVideo) currentFragment).onBackPressed();
//            }
//        }

    }

    private void init(String targetId, boolean outgoing, boolean audioOnly, boolean isReplace) {
        SingleCallFragment fragment;
        if (audioOnly) {
            fragment = new FragmentAudio();
        } else {
            fragment = new FragmentVideo();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = fragment;
        if (isReplace) {
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit();
        }
        if (outgoing && !isReplace) {
            // 创建会话
            room = UUID.randomUUID().toString() + System.currentTimeMillis();
            boolean b = gEngineKit.startOutCall(getApplicationContext(), room, targetId, audioOnly);
            if (!b) {
                finish();
                return;
            }
            // TODO: 2022/4/12 原代码 先注释掉
//            MyApp.app.setRoomId(room);
//            MyApp.app.setOtherUserId(targetId);
            CallSession session = gEngineKit.getCurrentSession();
            if (session == null) {
                finish();
            } else {
                session.setSessionCallback(this);
            }
        } else {
            CallSession session = gEngineKit.getCurrentSession();
            if (session == null) {
                finish();
            } else {
                if (session.isAudioOnly() && !audioOnly) { //这种情况是，对方切换成音频的时候，activity还没启动，这里启动后需要切换一下
                    isAudioOnly = session.isAudioOnly();
                    fragment.didChangeMode(true);
                }
                session.setSessionCallback(this);
            }
        }

    }

    public SkyEngineKit getEngineKit() {
        return gEngineKit;
    }

    public boolean isOutgoing() {
        return isOutgoing;
    }


    public boolean isFromFloatingView() {
        return isFromFloatingView;
    }

    // 显示小窗
    public void showFloatingView() {
        if (!checkOverlayPermission()) {
            return;
        }
        Intent intent = new Intent(this, FloatingVoipService.class);
        intent.putExtra(EXTRA_TARGET, targetId);
        intent.putExtra(EXTRA_AUDIO_ONLY, isAudioOnly);
        intent.putExtra(EXTRA_MO, isOutgoing);
        startService(intent);
        finish();
    }

    // 切换到语音通话
    public void switchAudio() {
        init(targetId, isOutgoing, true, true);
    }

    public String getRoomId() {
        return room;
    }

    // ======================================界面回调================================
    @Override
    public void didCallEndWithReason(EnumType.CallEndReason reason) {
        // TODO: 2022/4/12 原代码 先注释掉
//        MyApp.app.setOtherUserId("0");
        //交给fragment去finish
//        finish();
        handler.post(() -> currentFragment.didCallEndWithReason(reason));
    }

    @Override
    public void didChangeState(EnumType.CallState callState) {
        if (callState == EnumType.CallState.Connected) {
            isOutgoing = false;
        }
        handler.post(() -> currentFragment.didChangeState(callState));
    }

    @Override
    public void didChangeMode(boolean var1) {
        handler.post(() -> currentFragment.didChangeMode(var1));
    }

    @Override
    public void didCreateLocalVideoTrack() {
        handler.post(() -> currentFragment.didCreateLocalVideoTrack());
    }

    @Override
    public void didReceiveRemoteVideoTrack(String userId) {
        handler.post(() -> currentFragment.didReceiveRemoteVideoTrack(userId));
    }

    @Override
    public void didUserLeave(String userId) {
        handler.post(() -> currentFragment.didUserLeave(userId));
    }

    @Override
    public void didError(String var1) {
        handler.post(() -> currentFragment.didError(var1));
//        finish();
    }

    @Override
    public void didDisconnected(String userId) {
        handler.post(() -> currentFragment.didDisconnected(userId));
    }


    // ========================================================================================

    private boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SettingsCompat.setDrawOverlays(this, true);
            if (!SettingsCompat.canDrawOverlays(this)) {
                Toast.makeText(this, "需要悬浮窗权限", Toast.LENGTH_LONG).show();
                SettingsCompat.manageDrawOverlays(this);
                return false;
            }
        }
        return true;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }
}

package com.juntai.wisdom.im.chat_module.voip.singleCall;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.BarUtils;
import com.dds.skywebrtc.CallSession;
import com.dds.skywebrtc.EnumType;
import com.dds.skywebrtc.SkyEngineKit;
import com.juntai.disabled.basecomponent.utils.RomUtil;
import com.juntai.disabled.federation.R;

/**
 * Created by dds on 2018/7/26.
 * android_shuai@163.com
 * 语音通话控制界面
 */
public class FragmentAudio extends SingleCallFragment implements View.OnClickListener {
    private static final String TAG = "FragmentAudio";
    private ImageView muteImageView;
    private ImageView speakerImageView;
    private boolean micEnabled = false; // 静音
    private boolean isSpeakerOn = false; // 扬声器

    @Override
    int getLayout() {
        return R.layout.fragment_audio;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        muteImageView = view.findViewById(R.id.muteImageView);
        speakerImageView = view.findViewById(R.id.speakerImageView);
        minimizeImageView.setVisibility(View.GONE);
        outgoingHangupImageView.setOnClickListener(this);
        incomingHangupImageView.setOnClickListener(this);
        minimizeImageView.setOnClickListener(this);
        muteImageView.setOnClickListener(this);
        acceptImageView.setOnClickListener(this);
        speakerImageView.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || RomUtil.isMiui() || RomUtil.isFlyme()) {
            lytParent.post(() -> {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) minimizeImageView.getLayoutParams();
                params.topMargin = BarUtils.getStatusBarHeight();
                minimizeImageView.setLayoutParams(params);
            });
        }
    }

    @Override
    public void init() {
        super.init();
        CallSession currentSession = gEngineKit.getCurrentSession();
        currentState = currentSession.getState();
        // 如果已经接通
        if (currentSession != null && currentState == EnumType.CallState.Connected) {
            descTextView.setVisibility(View.GONE); // 提示语
            outgoingActionContainer.setVisibility(View.VISIBLE);
            durationTextView.setVisibility(View.VISIBLE);
            minimizeImageView.setVisibility(View.VISIBLE);
            startRefreshTime();
        } else {
            // 如果未接通
            if (isOutgoing) {
                descTextView.setText(R.string.av_waiting);
                outgoingActionContainer.setVisibility(View.VISIBLE);
                incomingActionContainer.setVisibility(View.GONE);
            } else {
                descTextView.setText(R.string.av_audio_invite);
                outgoingActionContainer.setVisibility(View.GONE);
                incomingActionContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void didChangeState(EnumType.CallState state) {

        currentState = state;
        runOnUiThread(() -> {
            if (state == EnumType.CallState.Connected) {
                handler.removeMessages(WHAT_DELAY_END_CALL);
                incomingActionContainer.setVisibility(View.GONE);
                outgoingActionContainer.setVisibility(View.VISIBLE);
                minimizeImageView.setVisibility(View.VISIBLE);
                descTextView.setVisibility(View.GONE);

                startRefreshTime();
            } else {
                // do nothing now
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        // 接听
        if (id == R.id.acceptImageView) {
            CallSession session = gEngineKit.getCurrentSession();
            if (session != null)
                Log.d(TAG, "session = " + session + "; session.getState() = " + session.getState());
            if (session != null && session.getState() == EnumType.CallState.Incoming) {
                session.joinHome(session.getRoomId());
            } else if (session != null) {
                session.sendRefuse();
            }
        }
        // 挂断电话
        if (id == R.id.incomingHangupImageView || id == R.id.outgoingHangupImageView) {
            // TODO: 2022/4/12 原代码 先注释掉
//            MyApp.app.setOtherUserId("0");
            CallSession session = gEngineKit.getCurrentSession();
            if (session != null) {
                SkyEngineKit.Instance().endCall();
            }
            //            activity.finish();
            //再onEvent中结束，防止ChatActivity结束了，消息发送不了
        }
        // 静音
        if (id == R.id.muteImageView) {
            CallSession session = gEngineKit.getCurrentSession();
            if (session != null && session.getState() != EnumType.CallState.Idle) {
                if (session.toggleMuteAudio(!micEnabled)) {
                    micEnabled = !micEnabled;
                }
                muteImageView.setSelected(micEnabled);
            }
        }
        // 扬声器
        if (id == R.id.speakerImageView) {
            CallSession session = gEngineKit.getCurrentSession();
            if (session != null && session.getState() != EnumType.CallState.Idle) {
                if (session.toggleSpeaker(!isSpeakerOn)) {
                    isSpeakerOn = !isSpeakerOn;
                }
                speakerImageView.setSelected(isSpeakerOn);
            }
        }
        // 小窗
        if (id == R.id.minimizeImageView) {
            if (callSingleActivity != null) {
                callSingleActivity.showFloatingView();
            }
        }
    }
}
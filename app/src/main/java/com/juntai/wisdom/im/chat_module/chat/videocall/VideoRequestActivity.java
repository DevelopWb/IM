package com.juntai.wisdom.im.chat_module.chat.videocall;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.utils.EventManager;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.bean.FinishVideoActivityMsgBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.VideoActivityMsgBean;
import com.juntai.wisdom.im.chat_module.chat.ChatPresent;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.OperateMsgUtil;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.juntai.wisdom.im.webSocket.MyWsManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.Logging;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RendererCommon;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * @aouther tobato
 * @description ??????  ?????????????????????
 * @date 2020/12/3 15:11
 */
public class VideoRequestActivity extends SoundManagerActivity<ChatPresent> implements View.OnClickListener, MainContract.IBaseView {

    private ImageView mHandDownIv;
    /**
     * ??????
     */
    private TextView mHandDownTv;
    private ImageView mHandUpIv;
    /**
     * ??????
     */
    private TextView mHandUpTv;
    private Group mHandDownGp;
    private Group mHandUpGp;
    private ConstraintLayout mVedioCallCl;
    /**
     * ??????????????????????????????
     */
    private TextView mNameTv;
    private MessageBodyBean mMessageBodyBean;
    private MessageBodyBean mSenderMessageBodyBean;
    private MessageBodyBean mReceiverMessageBodyBean;
    private Group mCallOnGp;


    //????????? surface view
    private SurfaceViewRenderer mBigSurfaceView;
    private SurfaceViewRenderer mSmallSurfaceView;
    //OpenGL ES
    private EglBase mRootEglBase;

    private PeerConnectionFactory mPeerConnectionFactory;
    //????????????
    private SurfaceTextureHelper mSurfaceTextureHelper;
    private VideoTrack mLocalVideoTrack;
    private AudioTrack mAudioTrack;
    private VideoCapturer mVideoCapturer;
    private VideoSource videoSource;
    private PeerConnection mPeerConnection;
    private VideoTrack remoteVideoTrack;
    //????????????
    public final static String EVENT_CAMERA_OFFER = "offer";
    public final static String EVENT_CAMERA_ANSWER = "answer";
    public final static String EVENT_CAMERA_CANDIDATE = "candidate";
    public final static String EVENT_CAMERA_REQUEST = "request";
    public final static String EVENT_CAMERA_ACCESS = "access";
    public final static String EVENT_CAMERA_FINISH_SENDER = "sender_finish";
    public final static String EVENT_CAMERA_FINISH_RECEIVER = "receiver_finish";


    public static final String VIDEO_TRACK_ID = "1";//"ARDAMSv0";
    public static final String AUDIO_TRACK_ID = "2";//"ARDAMSa0";

    private boolean isFront = true;//????????????
    private static final int VIDEO_RESOLUTION_WIDTH = 1280;
    private static final int VIDEO_RESOLUTION_HEIGHT = 720;
    private static final int VIDEO_FPS = 30;
    private ImageView mHeadIv;

    private boolean smallWindowIsRemoteStream = true;//???????????????????????????

    private boolean isCallOn = false;//??????????????????
    //??????????????????
    public final static String IS_SENDER = "issender";
    private boolean isSender;
    private TextView mDurationTv;
    public static long mRecordingBegin;
    private ImageView mSwitchCameraIv;
    /**
     * ????????????  4 ???????????? 5 ????????????
     */
    private int callType = 4;
    private TextView mWaitDesTv;
    private AudioManager audioManager;

    @Override
    public int getLayoutView() {
        return R.layout.request_video_call_layout;
    }

    @Override
    public void initView() {
        super.initView();
        initToolbarAndStatusBar(false);
        mHandDownIv = (ImageView) findViewById(R.id.hand_down_iv);
        mHeadIv = (ImageView) findViewById(R.id.call_head_iv);
        mHandDownIv.setOnClickListener(this);
        mDurationTv = (TextView) findViewById(R.id.call_duration_tv);
        mDurationTv.setVisibility(View.GONE);
        mWaitDesTv = (TextView) findViewById(R.id.call_wait_des_tv);
        mHandDownTv = (TextView) findViewById(R.id.hand_down_tv);
        mSwitchCameraIv = (ImageView) findViewById(R.id.switch_camera_iv);
        mSwitchCameraIv.setOnClickListener(this);
        mHandUpIv = (ImageView) findViewById(R.id.hand_up_iv);
        mHandUpIv.setOnClickListener(this);
        mHandUpTv = (TextView) findViewById(R.id.hand_up_tv);
        mHandDownGp = (Group) findViewById(R.id.sender_call_gp);
        mHandDownGp.setOnClickListener(this);
        mHandUpGp = (Group) findViewById(R.id.receiver_call_wait_gp);
        mHandUpGp.setOnClickListener(this);
        mCallOnGp = (Group) findViewById(R.id.call_on_gp);
        mCallOnGp.setOnClickListener(this);
        mVedioCallCl = (ConstraintLayout) findViewById(R.id.vedio_call_cl);
        mNameTv = (TextView) findViewById(R.id.name_tv);
        mCallOnGp.setVisibility(View.GONE);
        mRootEglBase = EglBase.create();
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.FX_KEY_CLICK);
            audioManager.setSpeakerphoneOn(true);
        }
    }

    private void initSurfaceView() {

        mBigSurfaceView = findViewById(R.id.bigSurfaceView);
        mSmallSurfaceView = findViewById(R.id.smallSurfaceView);
        mBigSurfaceView.init(mRootEglBase.getEglBaseContext(), null);
        mBigSurfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        mBigSurfaceView.setMirror(true);
        mBigSurfaceView.setEnableHardwareScaler(false /* enabled */);
        mSmallSurfaceView.init(mRootEglBase.getEglBaseContext(), null);
        mSmallSurfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        mSmallSurfaceView.setMirror(true);
        mSmallSurfaceView.setZOrderMediaOverlay(true);
        mSmallSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remoteVideoTrack != null && mLocalVideoTrack != null) {
                    if (smallWindowIsRemoteStream) {
                        mLocalVideoTrack.removeSink(mBigSurfaceView);
                        remoteVideoTrack.removeSink(mSmallSurfaceView);
                        mLocalVideoTrack.addSink(mSmallSurfaceView);
                        remoteVideoTrack.addSink(mBigSurfaceView);
                    } else {
                        mLocalVideoTrack.removeSink(mSmallSurfaceView);
                        remoteVideoTrack.removeSink(mBigSurfaceView);
                        mLocalVideoTrack.addSink(mBigSurfaceView);
                        remoteVideoTrack.addSink(mSmallSurfaceView);
                    }
                    smallWindowIsRemoteStream = !smallWindowIsRemoteStream;
                }
            }
        });

    }

    private void initTrack() {
        //?????? factory??? pc??????factory????????????
        mPeerConnectionFactory = mPresenter.createPeerConnectionFactory(mRootEglBase, getApplicationContext());
        // NOTE: this _must_ happen while PeerConnectionFactory is alive!
        Logging.enableLogToDebugOutput(Logging.Severity.LS_VERBOSE);

        if (4 == callType) {
            mVideoCapturer = mPresenter.createVideoCapturer(getApplicationContext(), isFront);
            mSurfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", mRootEglBase.getEglBaseContext());
            videoSource = mPeerConnectionFactory.createVideoSource(false);
            mVideoCapturer.initialize(mSurfaceTextureHelper, getApplicationContext(), videoSource.getCapturerObserver());
            mLocalVideoTrack = mPeerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
            mLocalVideoTrack.setEnabled(true);
            mLocalVideoTrack.addSink(mBigSurfaceView);
        }
        MediaConstraints audioConstraints = new MediaConstraints();
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googEchoCancellation", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAutoGainControl", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googHighpassFilter", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googNoiseSuppression", "true"));
        AudioSource audioSource = mPeerConnectionFactory.createAudioSource(audioConstraints);
        mAudioTrack = mPeerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, audioSource);
        mAudioTrack.setEnabled(true);
    }


    public void doStartCall() {
        if (mPeerConnection == null) {
            mPeerConnection = createPeerConnection();
        }
        MediaConstraints mediaConstraints = new MediaConstraints();
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        mediaConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        mPeerConnection.createOffer(new SimpleSdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                Log.i(TAG, "Create local offer success: \n" + sessionDescription.description);
                mPeerConnection.setLocalDescription(new SimpleSdpObserver(), sessionDescription);
                /**
                 *   ?????? ????????????????????????  ??????  EVENT_CAMERA_OFFER   ???sdp???????????????
                 */

                mMessageBodyBean.setSdp(sessionDescription.description);
                mMessageBodyBean.setEvent(EVENT_CAMERA_OFFER);
                mMessageBodyBean.setFaceTimeType(2);
                mMessageBodyBean.setContent("??????");
                mPresenter.sendPrivateMessage(OperateMsgUtil.getMsgBuilder(mMessageBodyBean).build(), AppHttpPath.SEND_MSG);
            }
        }, mediaConstraints);
    }


    public PeerConnection createPeerConnection() {
        Log.i(TAG, "Create PeerConnection ...");

        LinkedList<PeerConnection.IceServer> iceServers = new LinkedList<PeerConnection.IceServer>();

        PeerConnection.IceServer ice_server =
                PeerConnection.IceServer.builder(AppHttpPath.CHAT_VIDEO_URL)
                        .setPassword("jtkj2020")
                        .setUsername("juntai")
                        .createIceServer();

        iceServers.add(ice_server);

        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
        // TCP candidates are only useful when connecting to a server that supports
        // ICE-TCP.
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        //rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        //rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        // Use ECDSA encryption.
        //rtcConfig.keyType = PeerConnection.KeyType.ECDSA;
        // Enable DTLS for normal calls and disable for loopback calls.
        rtcConfig.enableDtlsSrtp = true;
        //rtcConfig.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN;
        PeerConnection connection =
                mPeerConnectionFactory.createPeerConnection(rtcConfig,
                        mPeerConnectionObserver);
        if (connection == null) {
            Log.e(TAG, "Failed to createPeerConnection !");
            return null;
        }

        List<String> mediaStreamLabels = Collections.singletonList("ARDAMS");
        if (4 == callType) {
            connection.addTrack(mLocalVideoTrack, mediaStreamLabels);
        }
        connection.addTrack(mAudioTrack, mediaStreamLabels);

        return connection;
    }


    private PeerConnection.Observer mPeerConnectionObserver = new PeerConnection.Observer() {


        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.i(TAG, "onSignalingChange: " + signalingState);
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            Log.i(TAG, "onIceConnectionChange: " + iceConnectionState);
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            Log.i(TAG, "onIceConnectionChange: " + b);
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.i(TAG, "onIceGatheringChange: " + iceGatheringState);
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            Log.i(TAG, "onIceCandidate: " + iceCandidate);
            /**
             * ?????????????????????  ???????????????????????????????????????
             */
            mMessageBodyBean.setEvent(EVENT_CAMERA_CANDIDATE);
            mMessageBodyBean.setSdpMLineIndex(iceCandidate.sdpMLineIndex);
            mMessageBodyBean.setSdpMid(iceCandidate.sdpMid);
            mMessageBodyBean.setSdp(iceCandidate.sdp);
            mMessageBodyBean.setFaceTimeType(2);
            mMessageBodyBean.setContent("??????");
            mPresenter.sendPrivateMessage(OperateMsgUtil.getMsgBuilder(mMessageBodyBean).build(), AppHttpPath.SEND_MSG);
            pause();
        }

        @Override
        public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
            for (int i = 0; i < iceCandidates.length; i++) {
                Log.i(TAG, "onIceCandidatesRemoved: " + iceCandidates[i]);
            }
            mPeerConnection.removeIceCandidates(iceCandidates);
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {
            Log.i(TAG, "onAddStream: " + mediaStream.videoTracks.size());
        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {
            Log.i(TAG, "onRemoveStream");
        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {
            Log.i(TAG, "onDataChannel");
        }

        @Override
        public void onRenegotiationNeeded() {
            Log.i(TAG, "onRenegotiationNeeded");
        }

        @Override
        public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
            MediaStreamTrack track = rtpReceiver.track();
            if (track instanceof VideoTrack) {
                Log.i(TAG, "onAddVideoTrack");
                remoteVideoTrack = (VideoTrack) track;
                remoteVideoTrack.setEnabled(true);
                remoteVideoTrack.addSink(mSmallSurfaceView);
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initMsgData(intent);
    }

    @Override
    public void initData() {
        if (getIntent() != null) {

//            if (null != getIntent()) {
//                // ??????2???intent?????????????????????
//                String name2 = getIntent().getStringExtra("name");
//                ToastUtils.toast(mContext, name2);
//                LogUtil.e(name2);
//                return;
//            }
            initMsgData(getIntent());
            if (isSender) {
                //???????????????
                mHandUpGp.setVisibility(View.GONE);
                mHandDownGp.setVisibility(View.VISIBLE);
                mHandDownTv.setText("??????");
                mNameTv.setText(mMessageBodyBean.getToNickname());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(mMessageBodyBean.getToHead()), mHeadIv);
                /**
                 * ????????? ?????? ??????????????????  ??????event EVENT_CAMERA_REQUEST
                 */
                mMessageBodyBean.setFaceTimeType(1);
                mMessageBodyBean.setEvent(EVENT_CAMERA_REQUEST);
                mPresenter.requestVideoCall(OperateMsgUtil.getMsgBuilder(mMessageBodyBean).build(), AppHttpPath.REQUEST_VIDEO_CALL);
            } else {
                /**
                 * ????????? ?????? ??????????????????  ??????EVENT_CAMERA_REQUEST   ??????????????????
                 */
                mHandUpGp.setVisibility(View.VISIBLE);
                mHandDownGp.setVisibility(View.GONE);
                mHandDownTv.setText("??????");
                mNameTv.setText(mMessageBodyBean.getFromNickname());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(mMessageBodyBean.getFromHead()), mHeadIv);
            }
            // TODO: 2022-01-29 ????????????????????????
            play();
        }


        if (4 == callType) {
            initSurfaceView();
            mWaitDesTv.setText("?????????????????????");
        } else {
            mWaitDesTv.setText("?????????????????????");
        }
        initTrack();

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //???ui????????????
    public void receiveFinishMessage(FinishVideoActivityMsgBean finishVideoActivityMsgBean) {
        finish();
    }

    /**
     * @param videoActivityMsgBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //???ui????????????
    public void receiveMessage(VideoActivityMsgBean videoActivityMsgBean) {
        MessageBodyBean messageBody = videoActivityMsgBean.getMessageBodyBean();
        if (messageBody != null) {
            switch (messageBody.getMsgType()) {
                //????????????
                case 4:
                    //????????????
                case 5:
                    String eventMsg = messageBody.getEvent();
                    if (!TextUtils.isEmpty(eventMsg)) {
                        switch (eventMsg) {
                            case EVENT_CAMERA_ACCESS:
                                //???????????????
                                /**
                                 * ?????? ??????
                                 * ????????? ?????????????????????????????????
                                 */
                                callOnSuccess();
                                // ?????????????????????  ????????????
                                /**
                                 * ????????? ????????????call
                                 */
                                doStartCall();
                                break;
                            case EVENT_CAMERA_FINISH_SENDER:
                                //????????????  ??????????????????
                                if (isCallOn) {
                                    //??????????????? ???????????????
                                    messageBody.setDuration(getTextViewValue(mDurationTv));
                                } else {
                                    messageBody.setDuration(null);
                                }
                                messageBody.setFaceTimeType(4);
                                finishActivity(messageBody);
                                break;
                            case EVENT_CAMERA_FINISH_RECEIVER:
                                //??????
                                //?????????????????????  ??????????????????
                                //??????????????? ???????????????
                                if (isCallOn) {
                                    //??????????????? ???????????????
                                    mSenderMessageBodyBean.setDuration(getTextViewValue(mDurationTv));
                                } else {
                                    mSenderMessageBodyBean.setDuration(null);

                                }
                                mSenderMessageBodyBean.setFaceTimeType(4);
                                finishActivity(mSenderMessageBodyBean);
                                break;
                            case EVENT_CAMERA_OFFER:
                                /**
                                 * ???????????????
                                 * ????????? ??????  ??????offer
                                 */
                                if (mPeerConnection == null) {
                                    mPeerConnection = createPeerConnection();
                                }
                                mPeerConnection.setRemoteDescription(
                                        new SimpleSdpObserver(),
                                        new SessionDescription(
                                                SessionDescription.Type.OFFER,
                                                messageBody.getSdp()));
                                /**
                                 * ????????? ??????  ??????call
                                 */
                                doAnswerCall();
                                break;
                            case EVENT_CAMERA_ANSWER:
                                /**
                                 * ????????? ??????  ???????????????????????????
                                 */
                                isCallOn = true;
                                mPeerConnection.setRemoteDescription(
                                        new SimpleSdpObserver(),
                                        new SessionDescription(
                                                SessionDescription.Type.ANSWER,
                                                messageBody.getSdp()));
                                updateCallState(false);


                                break;
                            case EVENT_CAMERA_CANDIDATE:
                                /**
                                 * ?????????
                                 */
                                IceCandidate remoteIceCandidate =
                                        new IceCandidate(messageBody.getSdpMid(),
                                                messageBody.getSdpMLineIndex(),
                                                messageBody.getSdp());
                                mPeerConnection.addIceCandidate(remoteIceCandidate);
                                break;
                            default:
                                break;
                        }
                        return;
                    }

                    break;
                default:
                    break;
            }

        }

    }

    private void initMsgData(Intent intent) {
        String msgStr = intent.getStringExtra(BASE_STRING);
        isSender = getIntent().getBooleanExtra(IS_SENDER, true);
        if (!TextUtils.isEmpty(msgStr)) {
            //????????????????????????
            mMessageBodyBean = GsonTools.changeGsonToBean(msgStr,MessageBodyBean.class);
            MyWsManager.getInstance()
                    .init(mContext.getApplicationContext())
                    .setWsUrl(AppHttpPath.BASE_SOCKET + UserInfoManager.getUserUUID() + "/" + UserInfoManager.getUserId())
                    .startConnect();
            LogUtil.e("????????????????????????"+isSender);
        }else {
            mMessageBodyBean = intent.getParcelableExtra(BASE_PARCELABLE);
        }
        mSenderMessageBodyBean = mMessageBodyBean;
        mReceiverMessageBodyBean = mMessageBodyBean;

        callType = mMessageBodyBean.getMsgType();
    }


    /**
     * ????????????activity
     */
    private void finishActivity(MessageBodyBean messageBody) {
        EventManager.getEventBus().post(new FinishVideoActivityMsgBean(messageBody));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoCapturer != null) {
            mVideoCapturer.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, VIDEO_FPS);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoCapturer != null) {
            try {
                mVideoCapturer.stopCapture();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        doLeave();

        mRootEglBase.releaseSurface();
        mRootEglBase.release();
        if (mPeerConnection != null) {
            mPeerConnection.close();
            mPeerConnection = null;
        }
        updateCallState(true);
        if (mBigSurfaceView != null) {
            mBigSurfaceView.release();
            mBigSurfaceView = null;
        }
        if (mSmallSurfaceView != null) {
            mSmallSurfaceView.release();
            mSmallSurfaceView = null;
        }
        if (mVideoCapturer != null) {
            mVideoCapturer.dispose();
            mVideoCapturer = null;
        }
        if (mSurfaceTextureHelper != null) {
            mSurfaceTextureHelper.dispose();
            mSurfaceTextureHelper = null;

        }
        PeerConnectionFactory.stopInternalTracingCapture();
        PeerConnectionFactory.shutdownInternalTracer();
        if (mPeerConnectionFactory != null) {
            mPeerConnectionFactory.dispose();
            mPeerConnectionFactory = null;
        }
        mDurationTv.removeCallbacks(callingRunnable);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_camera_iv:
                if (mVideoCapturer != null) {
                    mVideoCapturer.dispose();
                    mVideoCapturer = null;
                }
                isFront = !isFront;
                mVideoCapturer = mPresenter.createVideoCapturer(getApplicationContext(), isFront);
                mVideoCapturer.initialize(mSurfaceTextureHelper, getApplicationContext(), videoSource.getCapturerObserver());
                mVideoCapturer.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, VIDEO_FPS);


                break;
            default:
                break;
            case R.id.hand_down_iv:
                //??????????????????
                LogUtil.e("????????????  ??????????????????"+isSender);
                if (isSender) {
                    //?????????????????????
                    //??????
                    if (isCallOn) {
                        //??????????????? ???????????????
                        mSenderMessageBodyBean.setFaceTimeType(4);
                        mSenderMessageBodyBean.setDuration(getTextViewValue(mDurationTv));
                    } else {
                        mSenderMessageBodyBean.setFaceTimeType(2);
                        mSenderMessageBodyBean.setDuration(null);
                    }
                    mSenderMessageBodyBean.setEvent(EVENT_CAMERA_FINISH_SENDER);
                    mPresenter.rejectVideoCall(OperateMsgUtil.getMsgBuilder(mSenderMessageBodyBean).build(), EVENT_CAMERA_FINISH_SENDER);

                } else {
                    //??????
                    //??????????????????
                    // : 2021-12-01 ?????????????????? ?????????????????????????????????
                    //???????????????????????????  ????????????????????????  ???????????? ???????????????
                    if (isCallOn) {
                        //??????????????? ???????????????
                        mReceiverMessageBodyBean.setFaceTimeType(4);
                        mReceiverMessageBodyBean.setDuration(getTextViewValue(mDurationTv));
                    } else {
                        mReceiverMessageBodyBean.setFaceTimeType(2);
                        mReceiverMessageBodyBean.setDuration(null);

                    }
                    MessageBodyBean bodyBean = OperateMsgUtil.getPrivateMsg(callType, mReceiverMessageBodyBean.getFromUserId(), mReceiverMessageBodyBean.getFromAccount(), mReceiverMessageBodyBean.getFromNickname(), mReceiverMessageBodyBean.getFromHead(), "");
                    if (isCallOn) {
                        //??????????????? ???????????????
                        bodyBean.setFaceTimeType(4);
                    } else {
                        bodyBean.setFaceTimeType(2);
                    }
                    bodyBean.setEvent(EVENT_CAMERA_FINISH_RECEIVER);
                    mPresenter.rejectVideoCall(OperateMsgUtil.getMsgBuilder(bodyBean).build(), EVENT_CAMERA_FINISH_RECEIVER);


                }


                break;
            case R.id.hand_up_iv:
                isCallOn = true;
                /**
                 * ????????? ??????  ??????  ??????EVENT_CAMERA_ACCESS
                 */
                mMessageBodyBean = OperateMsgUtil.getPrivateMsg(callType, mMessageBodyBean.getFromUserId(), mMessageBodyBean.getFromAccount(), mMessageBodyBean.getFromNickname(), mMessageBodyBean.getFromHead(), "");
                mMessageBodyBean.setFaceTimeType(1);
                mMessageBodyBean.setEvent(EVENT_CAMERA_ACCESS);
                callOnSuccess();
                mPresenter.accessVideoCall(OperateMsgUtil.getMsgBuilder(mMessageBodyBean).build(), AppHttpPath.ACCESS_VIDEO_CALL);
                if (mPeerConnection == null) {
                    mPeerConnection = createPeerConnection();
                }
                break;


        }
    }


    /**
     * ??????????????????
     */
    private void callOnSuccess() {
        if (4 == callType) {
            mCallOnGp.setVisibility(View.VISIBLE);
            mSwitchCameraIv.setVisibility(View.VISIBLE);
        } else {
            mCallOnGp.setVisibility(View.GONE);
            mSwitchCameraIv.setVisibility(View.GONE);
        }

        mHandDownGp.setVisibility(View.GONE);
        mHandUpGp.setVisibility(View.GONE);
        mHandDownTv.setText("??????");

    }

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case EVENT_CAMERA_FINISH_SENDER:
                // : 2021-12-01 ???????????? ??????????????????????????????
                finishActivity(mSenderMessageBodyBean);

                break;
            case EVENT_CAMERA_FINISH_RECEIVER:
                finishActivity(mReceiverMessageBodyBean);
                break;
            default:
                break;
        }
    }


    public static class SimpleSdpObserver implements SdpObserver {
        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
        }

        @Override
        public void onSetSuccess() {
        }

        @Override
        public void onCreateFailure(String msg) {
        }

        @Override
        public void onSetFailure(String msg) {

        }
    }

    private void updateCallState(final boolean idle) {
        if (idle) {
//            mSmallSurfaceView.setVisibility(View.GONE);
        } else {
            mRecordingBegin = System.currentTimeMillis();
//            mSmallSurfaceView.setVisibility(View.VISIBLE);
            mDurationTv.setVisibility(View.VISIBLE);
            //   ???????????????????????????????????????  ????????????
            mDurationTv.removeCallbacks(callingRunnable);
            mDurationTv.post(callingRunnable);

        }
    }

    // ????????????
    private Runnable callingRunnable = new Runnable() {
        @Override
        public void run() {
            long duration = System.currentTimeMillis() - mRecordingBegin;
            duration /= 1000;

            mDurationTv.setText(String.format("%02d:%02d", duration / 60, (duration) % 60));

//            if (duration % 2 == 0) {
//                mDurationTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.recording_marker_shape, 0, 0, 0);
//            } else {
//                mDurationTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.recording_marker_interval_shape, 0,
//                        0, 0);
//            }

            mDurationTv.removeCallbacks(this);
            mDurationTv.postDelayed(this, 1000);
        }
    };

    /**
     * ??????  ????????????
     */
    public void doAnswerCall() {
        if (mPeerConnection == null) {
            mPeerConnection = createPeerConnection();
        }
        MediaConstraints sdpMediaConstraints = new MediaConstraints();
        Log.i(TAG, "Create answer ...");
        mPeerConnection.createAnswer(new SimpleSdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                Log.i(TAG, "Create answer success !");
                /**
                 * ???????????????  ?????? EVENT_CAMERA_ANSWER  ???sdp???????????????
                 */
                mPeerConnection.setLocalDescription(new SimpleSdpObserver(),
                        sessionDescription);
                mMessageBodyBean.setSdp(sessionDescription.description);
                mMessageBodyBean.setEvent(EVENT_CAMERA_ANSWER);
                mMessageBodyBean.setFaceTimeType(2);
                mMessageBodyBean.setContent("??????");
                mPresenter.sendPrivateMessage(OperateMsgUtil.getMsgBuilder(mMessageBodyBean).build(), AppHttpPath.SEND_MSG);
            }

            @Override
            public void onCreateFailure(String msg) {
                super.onCreateFailure(msg);
            }

            @Override
            public void onSetFailure(String msg) {
                super.onSetFailure(msg);
            }
        }, sdpMediaConstraints);
        updateCallState(false);
    }
}

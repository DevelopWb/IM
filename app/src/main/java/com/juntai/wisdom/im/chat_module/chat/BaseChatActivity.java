package com.juntai.wisdom.im.chat_module.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.base.BaseWebViewActivity;
import com.juntai.disabled.basecomponent.bean.FileBaseInfoBean;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.basecomponent.utils.RxScheduler;
import com.juntai.disabled.basecomponent.utils.RxTask;
import com.juntai.disabled.basecomponent.utils.ScreenUtils;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.basecomponent.widght.BaseBottomDialog;
import com.juntai.disabled.bdmap.act.LocateSelectionActivity;
import com.juntai.disabled.bdmap.act.LocateShowActivity;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.base.displayPicVideo.PicVideoDisplayActivity;
import com.juntai.wisdom.im.base.uploadFile.UploadFileBean;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.FileSelectBean;
import com.juntai.wisdom.im.bean.GroupDetailBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.HomePageMenuBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MessageListBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.MyMenuBean;
import com.juntai.wisdom.im.chat_module.chat.chatInfo.ChatInfoActivity;
import com.juntai.wisdom.im.chat_module.chat.chatRecord.ChatRecordDetailActivity;
import com.juntai.wisdom.im.chat_module.chat.displayFile.FileDetailActivity;
import com.juntai.wisdom.im.chat_module.chat.forwardMsg.ForwardMsgActivity;
import com.juntai.wisdom.im.chat_module.chat.selectFile.SelectFileActivity;
import com.juntai.wisdom.im.chat_module.chat.videocall.VideoRequestActivity;
import com.juntai.wisdom.im.chat_module.groupchat.GroupInfoActivity;
import com.juntai.wisdom.im.contact.ContactorInfoActivity;
import com.juntai.wisdom.im.contact.SelectContactActivity;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.CalendarUtil;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.NotificationTool;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.OperateMsgUtil;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.negier.emojifragment.bean.Emoji;
import com.negier.emojifragment.fragment.EmojiFragment;
import com.negier.emojifragment.util.EmojiUtils;
import com.orhanobut.hawk.Hawk;
import com.zyl.chathelp.audio.AudioPlayManager;
import com.zyl.chathelp.audio.AudioRecordManager;
import com.zyl.chathelp.audio.IAudioPlayListener;
import com.zyl.chathelp.audio.IAudioRecordListener;
import com.zyl.chathelp.utils.EmotionKeyboard;
import com.zyl.chathelp.video.CameraActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  1对1聊天
 * @date 2020/11/10 16:44
 */
public abstract class BaseChatActivity extends BaseAppActivity<ChatPresent> implements View.OnClickListener,
        MainContract.IBaseView, EmojiFragment.OnEmojiClickListener, BaseAppActivity.OnFileUploadStatus {
    private BaseBottomDialog baseBottomDialog;
    private BaseBottomDialog.OnItemClick onItemClick;

    private RecyclerView mRecyclerview, mBottomEditMsgRv;
    private ChatAdapter chatAdapter;
    //    private List<PrivateMsgBean> news = new ArrayList<>();//消息的集合
    private int privateContactId;
    private ContactBean privateContactBean;
    private ImageView mIvAudio;
    private EditText mContentEt;
    /**
     * 按住 说话
     */
    private Button mBtnAudio;
    private ImageView mIvEmo;
    private ImageView mIvMore;
    /**
     * 发送
     */
    private TextView mTvSend;
    private FrameLayout mChatFl;
    private LinearLayout mLlRoot;
    private Fragment mEmojiFg;
    private EmotionKeyboard mEmotionKeyboard;
    private LinearLayout mContentLl;
    private RecyclerView mMoreActionRv;
    private LinearLayout mEmojiLl;
    private int mDuration = 0;
    public final static int REQUEST_TAKE_PHOTO = 1001;

    public static boolean ACTIVITY_IS_ON = false;
    private MoreActionAdapter moreActionAdapter;
    private double lat;
    private double lng;
    private String addrName;
    private String addrDes;
    private ImageView mReceiverHeadIv;
    /**
     * dfadsfa
     */
    private TextView mReceiverNameTv;
    /**
     * [个人名片]
     */
    private TextView mCardNameTv;
    /**
     * 给朋友留言
     */
    private EditText mToReceiverMsgEt;
    /**
     * 取消
     */
    private TextView mCloseDialogTv;
    /**
     * 发送
     */
    private TextView mSendCardTv;
    private AlertDialog sendCardDialog;

    private int groupId;
    //0是私聊 1是群聊 2是密聊
    private int chatType;
    private int offset = 0;
    //每次展示20条数据
    private int limit = 20;
    private BottomSheetDialog noticePeopleDialog;
    private GroupDetailInfoBean groupBean;
    private long searchedMsgId;
    private int searchedMsgPosition;
    /**
     * 正在被操作的消息实体
     */
    private MessageBodyBean operateingMsgBean;

    /**
     * 所有的图片和视频
     */
    ArrayList<MessageBodyBean> allPicVideoPath = new ArrayList<>();
    //@的成员
    List<Integer> atUsers = new ArrayList<>();
    private TextView mQuoteContentTv;
    private ImageView mCloseQuoteIv;
    private LinearLayout mQuoteLl;


    @Override

    public int getLayoutView() {
        return R.layout.activity_chat;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        allPicVideoPath.clear();
        initAdapterData(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 获取聊天的类型  0是私聊 1是群聊 2是密聊
     *
     * @return
     */
    protected abstract int getChatType();

    private void initAdapterData(Intent intent) {
        HawkProperty.clearRedPoint(mContext.getApplicationContext());
        if (intent == null) {
            intent = getIntent();
        }
        searchedMsgPosition = 0;
        searchedMsgId = intent.getLongExtra(BASE_ID2, 0);
        chatAdapter.setNewData(null);
        switch (chatType) {
            case 0:
                String msgStr = intent.getStringExtra(BASE_STRING);
                if (!TextUtils.isEmpty(msgStr)) {
                    //离线消息跳转进来
                    MessageBodyBean mMessageBodyBean = GsonTools.changeGsonToBean(msgStr, MessageBodyBean.class);
                    privateContactBean = new ContactBean();
                    privateContactBean.setMessageBodyBean(mMessageBodyBean);
                    privateContactBean.setId(mMessageBodyBean.getFromUserId());
                    privateContactBean.setNickname(mMessageBodyBean.getFromNickname());
                    privateContactBean.setRemarksNickname(mMessageBodyBean.getFromNickname());
                    privateContactBean.setAccountNumber(mMessageBodyBean.getFromAccount());
                    privateContactBean.setHeadPortrait(mMessageBodyBean.getFromHead());
                } else {
                    privateContactBean = intent.getParcelableExtra(BASE_PARCELABLE);
                }

                privateContactId = privateContactBean.getId();
                if (Hawk.contains(HawkProperty.getDraftKey(privateContactId, true))) {
                    MessageBodyBean messageBodyBean = Hawk.get(HawkProperty.getDraftKey(privateContactId, true));
                    mContentEt.setText(messageBodyBean.getContent());
                    mContentEt.setSelection(messageBodyBean.getContent().length());
                }

                if (privateContactBean != null) {
                    setTitleName(UserInfoManager.getContactRemarkName(privateContactId, privateContactBean.getRemarksNickname()));
                    //获取历史数据
                    List<MessageBodyBean> arrays = mPresenter.findPrivateChatRecordList(privateContactId);
                    if (arrays != null && arrays.size() > 0) {
                        for (int i = 0; i < arrays.size(); i++) {
                            MessageBodyBean startBean = arrays.get(i);
                            if (searchedMsgId == startBean.getId()) {
                                searchedMsgPosition = i;
                            }
                            if (!startBean.isRead()) {
                                startBean.setRead(true);
                                //更新数据库数据
                                ObjectBox.addMessage(startBean);
                            }
                            initAdapterDataFromMsgTypes(startBean);
                            if (i < arrays.size() - 1) {
                                MessageBodyBean endBean = arrays.get(i + 1);
                                addDateTag(startBean, endBean);
                            }
                        }
                    }
                    if (searchedMsgId > 0) {
                        mRecyclerview.scrollToPosition(searchedMsgPosition);
                    }

                }
                mPresenter.getPrivateUnreadMsg(getBaseBuilder().add("toUserId", String.valueOf(privateContactId)).build(), AppHttpPath.GET_UNREAD_PRIVATE_MSG);

                break;
            case 1:
                // : 2022-01-13 群聊 获取历史数据
                String groupMsgStr = intent.getStringExtra(BASE_STRING);
                if (!TextUtils.isEmpty(groupMsgStr)) {
                    //离线消息跳转进来
                    MessageBodyBean mMessageBodyBean = GsonTools.changeGsonToBean(groupMsgStr, MessageBodyBean.class);
                    groupId = mMessageBodyBean.getGroupId();
                    if (ObjectBox.checkGroupIsExist(groupId)) {
                        //存在群组信息 加载群组聊天记录
                        groupBean = ObjectBox.getGroup(groupId);
                        getGruopRecord();
                    }
                } else {
                    groupId = intent.getIntExtra(BASE_ID, 0);
                    //加载群组聊天记录
                    groupBean = ObjectBox.getGroup(groupId);
                    getGruopRecord();
                }
                mPresenter.getGroupInfo(getBaseBuilder().add("groupId", String.valueOf(groupId)).build(),
                        AppHttpPath.GET_GROUP_INFO);

                if (Hawk.contains(HawkProperty.getDraftKey(groupId, false))) {
                    MessageBodyBean messageBodyBean = Hawk.get(HawkProperty.getDraftKey(groupId, false));
                    mContentEt.setText(messageBodyBean.getContent());
                    mContentEt.setSelection(messageBodyBean.getContent().length());
                }


                break;
            case 3:
                // TODO: 2022-01-13 密聊 获取历史数据
                break;
            default:
                break;
        }
        scrollRecyclerview();


    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        chatType = getChatType();
        ACTIVITY_IS_ON = true;
        setAutoHideKeyboard(false);
        NotificationTool.cancelAllNotice(mContext);
        setRightTvDrawable(R.mipmap.item_more);
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (chatType) {
                    case 0:
                        startActivityForResult(new Intent(mContext, ChatInfoActivity.class).putExtra(BASE_PARCELABLE,
                                privateContactBean), ChatInfoActivity.CHAT_INFO_REQUEST);
                        break;
                    case 1:
                        // : 2022-01-13 群聊 右标题点击事件

                        startActivityForResult(new Intent(mContext, GroupInfoActivity.class).putExtra(BASE_PARCELABLE
                                , groupBean), GroupInfoActivity.GROUP_INFO_REQUEST);

                        break;
                    case 2:
                        // TODO: 2022-01-13 密聊 右标题点击事件
                        break;
                    default:
                        break;
                }
            }
        });
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (MotionEvent.ACTION_MOVE == action) {
                    hideBottomAndKeyboard();
                    if (mEmotionKeyboard != null) {
                        if (mEmotionKeyboard.isSoftInputShown()) {
                            mEmotionKeyboard.hideSoftInput();
                        }
                    }
                    mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);

                }
                return false;
            }
        });
        initBottomEditMsg();
        chatAdapter = new ChatAdapter(null);
        LinearLayoutManager managere = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //数据直接定位到最底部 如果根布局是约束布局 效果失效
        managere.setStackFromEnd(true);
        mRecyclerview.setLayoutManager(managere);
        mRecyclerview.setAdapter(chatAdapter);
        mIvAudio = (ImageView) findViewById(R.id.ivAudio);
        mIvAudio.setOnClickListener(this);
        mContentEt = (EditText) findViewById(R.id.content_et);
        mContentLl = (LinearLayout) findViewById(R.id.llContent);
        mBtnAudio = (Button) findViewById(R.id.btnAudio);
        mIvEmo = (ImageView) findViewById(R.id.ivEmo);
        mIvMore = (ImageView) findViewById(R.id.ivMore);
        mTvSend = (TextView) findViewById(R.id.tvSend);
        mQuoteContentTv = (TextView) findViewById(R.id.quote_content_tv);
        mCloseQuoteIv = (ImageView) findViewById(R.id.close_quote_iv);
        mTvSend.setOnClickListener(this);
        mCloseQuoteIv.setOnClickListener(this);
        mChatFl = (FrameLayout) findViewById(R.id.chat_fl);
        mLlRoot = (LinearLayout) findViewById(R.id.llRoot);
        mEmojiLl = (LinearLayout) findViewById(R.id.emoji_ll);
        mQuoteLl = (LinearLayout) findViewById(R.id.quote_ll);
        mMoreActionRv = (RecyclerView) findViewById(R.id.more_action_rv);
        initMoreActionAdapter();
        mEmojiFg = getSupportFragmentManager().findFragmentById(R.id.emoji_fg);

        initListener();
        initEmotionKeyboard();
        initAudioRecordManager();
        initAudioListener();
        initAdapterData(getIntent());
        mContentEt.setOnTouchListener(new View.OnTouchListener() {

            //解决有些机型 先点击+  展示更多内容后点击输入控件 软键盘遮挡输入控件的问题
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    hideBottomAndKeyboard();
                    if (mEmotionKeyboard != null) {
                        mEmotionKeyboard.showSoftInput();
                    }
                    if (!mEmojiLl.isShown()) {
                        mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
                    }
                    scrollRecyclerview();
                }
                return false;
            }
        });
    }

    /**
     * 滚到列表
     */
    private void scrollRecyclerview() {
        mRecyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerview.scrollToPosition(chatAdapter.getData().size() - 1);
            }
        }, 500);
    }

    private void initBottomEditMsg() {
        mBottomEditMsgRv = (RecyclerView) findViewById(R.id.bottom_edit_msg_rv);
        BottomEditMsgAdapter bottomEditMsgAdapter = new BottomEditMsgAdapter(R.layout.edit_msg_bottom_layout);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        mBottomEditMsgRv.setLayoutManager(manager);
        mBottomEditMsgRv.setAdapter(bottomEditMsgAdapter);
        bottomEditMsgAdapter.setNewData(mPresenter.getBoottomEditMsgAdapterData());
        bottomEditMsgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyMenuBean menuBean = (MyMenuBean) adapter.getItem(position);
                switch (menuBean.getName()) {
                    case ChatPresent.EDIT_MSG_FORWARD:

                        // : 2022-02-13   逐条转发   合并转发
                        if (baseBottomDialog == null) {
                            baseBottomDialog = new BaseBottomDialog();
                        }
                        onItemClick = new BaseBottomDialog.OnItemClick() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                releaseDialog();
                                OperateMsgUtil.selectedToEditMsgItems = getAllSelectedItem(false);
                                startActivityForResult(new Intent(mContext, ForwardMsgActivity.class)
                                        .putExtra(BASE_ID, position + 1), ChatInfoActivity.CHAT_INFO_REQUEST);
                                // : 2022-02-13  因为无法将selectedItem通过intent传递过去 所以 通过保存到本地的方式传递过去


                            }
                        };
                        baseBottomDialog.initBottomDg(mPresenter.getForwardTypes(), null, null, onItemClick);
                        baseBottomDialog.show(getSupportFragmentManager(), "arrays");

                        break;
                    case ChatPresent.EDIT_MSG_DELETE:

                        // : 2022-01-22 删除选择的消息

                        showAlertDialog("确定删除?", "确定", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<MultipleItem> selectedItem = getAllSelectedItem(true);
                                chatAdapter.getData().removeAll(selectedItem);
                                initAdapterSelectStatus();
                            }
                        });
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void receiveVideoCallMessageBody(MessageBodyBean messageBodyBean) {
        //只私聊和密聊的时候存在
        addDateTag(mPresenter.findPrivateChatRecordLastMessage(messageBodyBean.getFromUserId()), messageBodyBean);
        chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_VIDEO_CALL, messageBodyBean));
        scrollRecyclerview();
        ObjectBox.addMessage(messageBodyBean);
        super.receiveVideoCallMessageBody(messageBodyBean);
    }


    /**
     * @param messageBody
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void receiveMessage(MessageBodyBean messageBody) {
        messageBody.setRead(false);
        switch (chatType) {
            case 0:
                if (1 == messageBody.getChatType()) {
                    if (messageBody.getFromUserId() == privateContactId || messageBody.getToUserId() == privateContactId) {
                        //如果是正在聊天的对象发过来的 就不需要notification
                        NotificationTool.SHOW_NOTIFICATION = false;
                        messageBody.setRead(true);
                        HawkProperty.setRedPoint(mContext, -1);
                        addDateTag(mPresenter.findPrivateChatRecordLastMessage(messageBody.getFromUserId()),
                                messageBody);
                        initAdapterDataFromMsgTypes(messageBody);
                        scrollRecyclerview();
                    } else {
                        NotificationTool.SHOW_NOTIFICATION = true;
                    }
                }

                break;
            case 1:
                // : 2022-01-13 群聊 收到消息
                if (2 == messageBody.getChatType()) {
                    if (messageBody.getGroupId() == groupId) {
                        //如果是正在聊天的对象发过来的 就不需要notification
                        NotificationTool.SHOW_NOTIFICATION = false;
                        messageBody.setRead(true);
                        HawkProperty.setRedPoint(mContext, -1);

                        addDateTag(mPresenter.findGroupChatRecordLastMessage(groupId), messageBody);
                        initAdapterDataFromMsgTypes(messageBody);
                        scrollRecyclerview();
                    } else {
                        NotificationTool.SHOW_NOTIFICATION = true;
                    }
                }
                break;
            case 2:
                // TODO: 2022-01-13 密聊 收到消息
                break;
            default:
                break;
        }

        ObjectBox.addMessage(messageBody);

    }

    private void initMoreActionAdapter() {
        moreActionAdapter = new MoreActionAdapter(R.layout.item_more_action);
        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        mMoreActionRv.setLayoutManager(manager);
        mMoreActionRv.setAdapter(moreActionAdapter);
        moreActionAdapter.setNewData(mPresenter.getMoreActionMenus(chatType));
        moreActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyMenuBean myMenuBean = (MyMenuBean) adapter.getData().get(position);
                switch (myMenuBean.getName()) {
                    case ChatPresent.MORE_ACTION_PIC:
                        //  发送图片或者视频文件
                        choseImageAndVideo(0, BaseChatActivity.this, 6);
                        break;
                    case ChatPresent.MORE_ACTION_TAKE_PHOTO:
                        // : 2021-11-23 拍照 发送小视频
                        startActivityForResult(new Intent(mContext, CameraActivity.class), REQUEST_TAKE_PHOTO);
                        break;
                    case ChatPresent.MORE_ACTION_VIDEO_CALL:
                        if (baseBottomDialog == null) {
                            baseBottomDialog = new BaseBottomDialog();
                        }
                        onItemClick = new BaseBottomDialog.OnItemClick() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                releaseDialog();
                                // : 2021-11-23 视频通话
                                MessageBodyBean videoMsg = OperateMsgUtil.getPrivateMsg(0 == position ? 4 : 5,
                                        privateContactId, privateContactBean.getUuid(),
                                        privateContactBean.getRemarksNickname(), privateContactBean.getHeadPortrait(), "");
                                //跳转到等待接听界面
                                Intent intent =
                                        new Intent(mContext, VideoRequestActivity.class)
                                                .putExtra(VideoRequestActivity.IS_SENDER, true)
                                                .putExtra(BASE_PARCELABLE,
                                                        videoMsg);

                                startActivity(intent);

                            }
                        };
                        baseBottomDialog.initBottomDg(mPresenter.getCalls(), null, null, onItemClick);
                        baseBottomDialog.show(getSupportFragmentManager(), "arrays");


                        break;
                    case ChatPresent.MORE_ACTION_LOCATION:
                        // : 2021-11-23 发送位置
                        startActivityForResult(new Intent(mContext, LocateSelectionActivity.class)
                                .putExtra(LocateSelectionActivity.RIGHT_CONTENT, "发送"), BASE_REQUEST_RESULT);


                        break;
                    case ChatPresent.MORE_ACTION_SECRET_CHAT:
                        // TODO: 2021-12-05 密聊
                        ToastUtils.toast(mContext, "暂未开放");
                        break;
                    case ChatPresent.MORE_ACTION_FILE:
                        // : 2021-12-05 发送文本文件
                        startActivityForResult(new Intent(mContext, SelectFileActivity.class)
                                        .putExtra(BASE_ID, chatType)
                                        .putExtra(BASE_ID2, chatType == 0 ? privateContactId : groupId)
                                , SelectFileActivity.REQUEST_SELECT_FILES);


                        break;
                    case ChatPresent.MORE_ACTION_CARD:
                        // : 2021-12-05 发送名片
                        startActivityForResult(new Intent(mContext, SelectContactActivity.class),
                                SelectContactActivity.REQUEST_CONTACT);


                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        setOnFileUploadStatus(this);
        chatAdapterClickListener();

    }

    /**
     * 最外层聊天适配器的点击事件
     */
    private void chatAdapterClickListener() {
        chatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                hideBottomAndKeyboard();
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                if (MultipleItem.ITEM_CHAT_DATE_MSG == multipleItem.getItemType()) {
                    return;
                }
                MessageBodyBean messageBodyBean = (MessageBodyBean) multipleItem.getObject();
                if (chatAdapter.isEdit()) {
                    if (messageBodyBean.isSelected()) {
                        messageBodyBean.setSelected(false);
                    } else {
                        messageBodyBean.setSelected(true);
                    }
                    chatAdapter.notifyItemChanged(position);
                } else {
                    hideBottomAndKeyboard();
                    mEmotionKeyboard.hideSoftInput();
                    mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
                }


            }
        });

        chatAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_CHAT_DATE_MSG:
                        break;
                    default:
                        MessageBodyBean messageBodyBean = (MessageBodyBean) multipleItem.getObject();
                        if (chatAdapter.isEdit()) {
                            if (messageBodyBean.isSelected()) {
                                messageBodyBean.setSelected(false);
                            } else {
                                messageBodyBean.setSelected(true);
                            }
                            chatAdapter.notifyItemChanged(position);
                            return;
                        }
                        switch (view.getId()) {
                            case R.id.sender_pic_iv:
                            case R.id.receiver_pic_iv:
                                startActivityForResult(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, messageBodyBean.getFromUserId()), ContactorInfoActivity.CONTACT_INFO_RESULT);
                                break;
                            case R.id.receiver_videocall_content_tv:
                            case R.id.sender_videocall_content_tv:
                                //视频通话
                                if (messageBodyBean.getFromUserId() != UserInfoManager.getUserId()) {
                                    messageBodyBean = OperateMsgUtil.getPrivateMsg(messageBodyBean.getMsgType(),
                                            messageBodyBean.getFromUserId(), messageBodyBean.getFromAccount(),
                                            messageBodyBean.getFromNickname(), messageBodyBean.getFromHead(), "");
                                }

                                Intent intent =
                                        new Intent(mContext, VideoRequestActivity.class)
                                                .putExtra(VideoRequestActivity.IS_SENDER, true)
                                                .putExtra(BASE_PARCELABLE,
                                                        messageBodyBean);
                                startActivity(intent);
                                break;
                            case R.id.sender_pic_video_iv:
                            case R.id.receiver_pic_video_iv:
                                displayPicVideo(messageBodyBean);
                                break;
                            case R.id.sender_locate_ll:
                            case R.id.receiver_locate_ll:
                                //位置信息
                                LocateShowActivity.startLocateActivity(mContext,
                                        Double.parseDouble(messageBodyBean.getLat()),
                                        Double.parseDouble(messageBodyBean.getLng()), messageBodyBean.getAddrName(),
                                        messageBodyBean.getAddrDes());

                                break;
                            case R.id.receiver_card_cl:
                            case R.id.sender_card_cl:
                                //名片
                                startActivityForResult(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, messageBodyBean.getOtherUserId()), ContactorInfoActivity.CONTACT_INFO_RESULT);

                                break;
                            case R.id.receiver_file_cl:
                            case R.id.sender_file_cl:
                                //文件
                                if (UserInfoManager.getUserId() == messageBodyBean.getFromUserId()) {
                                    //本人发的
                                    if (TextUtils.isEmpty(messageBodyBean.getLocalCatchPath())) {
                                        //本地缓存没有 这时候应该是web端发的同步消息
                                        messageBodyBean.setFromUserId(0);
                                    }
                                }
                                startActivity(new Intent(mContext, FileDetailActivity.class).putExtra(BASE_PARCELABLE
                                        , messageBodyBean));

                                break;
                            case R.id.receiver_share_cl:
                            case R.id.sender_share_cl:
                                //外部分享的链接
                                startActivity(new Intent(mContext, BaseWebViewActivity.class).putExtra("url", messageBodyBean.getShareUrl()));

                                break;
                            case R.id.receiver_chatrecord_cl:
                            case R.id.sender_chatrecord_cl:
                                //聊天记录
                                startActivity(new Intent(mContext, ChatRecordDetailActivity.class).putExtra(BASE_STRING, TextUtils.isEmpty(messageBodyBean.getQuoteMsg())?messageBodyBean.getContent():messageBodyBean.getQuoteMsg()));
                                break;
                            case R.id.receiver_quote_content_tv:
                            case R.id.sender_quote_content_tv:
                                //引用的内容
                                MessageBodyBean quoteMsgBean = GsonTools.changeGsonToBean(messageBodyBean.getQuoteMsg(),MessageBodyBean.class);
                                assert quoteMsgBean != null;
                                startToMsgDetail(mContext,quoteMsgBean);
                                break;

                            case R.id.audio_bg_rl:
                                ImageView ivAudio = (ImageView) adapter.getViewByPosition(mRecyclerview, position,
                                        R.id.ivAudio);
                                AudioPlayManager.getInstance().stopPlay();
                                String audioUri = UrlFormatUtil.getImageOriginalUrl(messageBodyBean.getContent());
                                AudioPlayManager.getInstance().startPlay(mContext, Uri.parse(audioUri),
                                        new IAudioPlayListener() {
                                            @Override
                                            public void onStart(Uri var1) {
                                                // TODO: 2022/4/10 这个地方需要优化一下 
                                                if (ivAudio != null && ivAudio.getDrawable() instanceof AnimationDrawable) {
                                                    AnimationDrawable animation = (AnimationDrawable) ivAudio.getDrawable();
                                                    animation.start();
                                                }
                                            }

                                            @Override
                                            public void onStop(Uri var1) {
                                                if (ivAudio != null && ivAudio.getDrawable() instanceof AnimationDrawable) {
                                                    AnimationDrawable animation = (AnimationDrawable) ivAudio.getDrawable();
                                                    animation.stop();
                                                    animation.selectDrawable(0);
                                                }

                                            }

                                            @Override
                                            public void onComplete(Uri var1) {
                                                if (ivAudio != null && ivAudio.getDrawable() instanceof AnimationDrawable) {
                                                    AnimationDrawable animation = (AnimationDrawable) ivAudio.getDrawable();
                                                    animation.stop();
                                                    animation.selectDrawable(0);
                                                }
                                            }
                                        });
                                break;
                            default:
                                break;
                        }

                        break;
                }
            }
        });

        chatAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter msgAdapter, View view, int msgPosition) {
                MultipleItem multipleItem = (MultipleItem) msgAdapter.getData().get(msgPosition);
                operateingMsgBean = (MessageBodyBean) multipleItem.getObject();
                if (chatAdapter.isEdit()) {
                    if (operateingMsgBean.isSelected()) {
                        operateingMsgBean.setSelected(false);
                    } else {
                        operateingMsgBean.setSelected(true);
                    }
                    chatAdapter.notifyItemChanged(msgPosition);
                    return false;
                }

                switch (view.getId()) {
                    case R.id.sender_pic_iv:
                        //长按 @功能
                    case R.id.receiver_pic_iv:
                        switch (chatType) {
                            case 1:
                                if (operateingMsgBean.getFromUserId() != UserInfoManager.getUserId()) {
                                    atUsers.add(operateingMsgBean.getFromUserId());
                                    mContentEt.append(String.format("@%s\u3000", operateingMsgBean.getFromNickname()));
                                    //光标移动到最后面
                                    mContentEt.setSelection(getTextViewValue(mContentEt).length());
                                    //弹出软键盘
                                    mEmotionKeyboard.showSoftInput();
                                    scrollRecyclerview();
                                }
                                return true;
                            default:
                                break;
                        }

                    default:
                        View currentView = null;
                        PopupWindow editPopupWindow = null;
                        View popView = LayoutInflater.from(mContext).inflate(R.layout.home_pop, null);
                        LinearLayout topLl = popView.findViewById(R.id.pop_bg_ll);
                        topLl.setBackgroundResource(R.mipmap.edit_msg_bg);

                        RecyclerView recyclerView = popView.findViewById(R.id.home_pop_rv);
                        EditChatMsgAdapter editChatMsgAdapter = new EditChatMsgAdapter(R.layout.edit_chat_msg_item);
                        List<HomePageMenuBean> arrays = mPresenter.getEditChatMsgMenus(operateingMsgBean);
                        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(editChatMsgAdapter);
                        editChatMsgAdapter.setNewData(arrays);
                        if (arrays.size() > 4) {
                            editPopupWindow = new PopupWindow(popView, DisplayUtil.dp2px(mContext, 180), DisplayUtil.dp2px(mContext, 100));
                        } else {
                            editPopupWindow = new PopupWindow(popView, DisplayUtil.dp2px(mContext, 180), DisplayUtil.dp2px(mContext, 50));

                        }
                        editPopupWindow.setOutsideTouchable(true);

                        switch (view.getId()) {
                            case R.id.sender_content_tv:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.sender_content_tv);
                                break;
                            case R.id.receiver_content_tv:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.receiver_content_tv);
                                break;
                            case R.id.sender_videocall_content_tv:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.sender_videocall_content_tv);
                                break;
                            case R.id.receiver_videocall_content_tv:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.receiver_videocall_content_tv);
                                break;
                            case R.id.sender_pic_video_iv:
                                //图片视频
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.sender_pic_video_iv);
                                break;
                            case R.id.receiver_pic_video_iv:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.receiver_pic_video_iv);
                                break;
                            case R.id.sender_locate_ll:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.sender_locate_ll);
                                break;
                            case R.id.receiver_locate_ll:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.receiver_locate_ll);
                                break;
                            case R.id.receiver_card_cl:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.receiver_card_cl);

                                break;
                            case R.id.sender_card_cl:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.sender_card_cl);

                                break;
                            case R.id.receiver_file_cl:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.receiver_file_cl);
                                break;
                            case R.id.sender_file_cl:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.sender_file_cl);
                                break;
                            case R.id.receiver_chatrecord_cl:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.receiver_chatrecord_cl);

                                break;
                            case R.id.sender_chatrecord_cl:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.sender_chatrecord_cl);

                                break;
                            case R.id.receiver_share_cl:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.receiver_share_cl);

                                break;
                            case R.id.sender_share_cl:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.sender_share_cl);

                                break;
                            case R.id.audio_bg_rl:
                                currentView = msgAdapter.getViewByPosition(mRecyclerview, msgPosition,
                                        R.id.audio_bg_rl);
                                break;
                            default:
                                currentView = view;
                                break;
                        }
                        int[] location = new int[2];
                        currentView.getLocationOnScreen(location);
                        //显示在正上方
                        editPopupWindow.showAtLocation(currentView, Gravity.NO_GRAVITY,
                                location[0] + currentView.getWidth() / 2 - editPopupWindow.getWidth() / 2,
                                location[1] - editPopupWindow.getHeight());
                        PopupWindow finalEditPopupWindow = editPopupWindow;
                        editChatMsgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                HomePageMenuBean menuBean = (HomePageMenuBean) adapter.getItem(position);
                                finalEditPopupWindow.dismiss();
                                switch (menuBean.getName()) {
                                    case MainContract.COPY_MSG:
                                        // : 2022-02-12 复制消息
                                        copy(operateingMsgBean.getContent());

                                        break;
                                    case MainContract.QUOTE:
                                        // : 2022-02-12 引用消息

                                        mQuoteLl.setVisibility(View.VISIBLE);
                                        //如果被引用的消息有引用的消息 需要置空 要不json数据太大
                                        // 注意  合并消息除外  因为合并消息也是通过这个字段存储数据的 唉 都是后台参数不支持的问题
                                        if (9!=operateingMsgBean.getMsgType()) {
                                            operateingMsgBean.setQuoteMsg(null);
                                        }
                                        mQuoteContentTv.setTag(operateingMsgBean);
                                        mQuoteContentTv.setText(OperateMsgUtil.getContent(operateingMsgBean));
                                        mContentEt.requestFocus();
                                        //弹出软键盘
                                        mEmotionKeyboard.showSoftInput();
                                        scrollRecyclerview();
                                        break;
                                    case MainContract.FORWARD_MSG:
                                        // : 2022-02-12 转发消息
                                        startActivityForResult(new Intent(mContext, ForwardMsgActivity.class).putExtra(BASE_PARCELABLE, operateingMsgBean), ChatInfoActivity.CHAT_INFO_REQUEST);
                                        break;

                                    case MainContract.COLLECTION_MSG:
                                        // : 2022-02-12 收藏消息  如果是文件 必须下载到本地之后才能收藏
                                        switch (operateingMsgBean.getMsgType()) {
                                            case 1:
                                                mPresenter.collectFile(mContext, operateingMsgBean,
                                                        FileCacheUtils.getAppImagePath(false));

                                                break;
                                            case 2:
                                                mPresenter.collectFile(mContext, operateingMsgBean,
                                                        FileCacheUtils.getAppVideoPath(false));

                                                break;
                                            case 8:
                                                mPresenter.collectFile(mContext, operateingMsgBean,
                                                        FileCacheUtils.getAppFilePath(false));

                                                break;
                                            default:
                                                mPresenter.collect(OperateMsgUtil.getMsgBuilder(operateingMsgBean).build(), AppHttpPath.COLLECT);
                                                break;
                                        }

                                        break;
                                    case MainContract.DELETE_MSG:
                                        // : 2022-01-21  删除当前消息
                                        showAlertDialog("确定删除?", "确定", "取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                msgAdapter.remove(msgPosition);
                                                ObjectBox.deleteMessage(operateingMsgBean);
                                            }
                                        });

                                        break;
                                    case MainContract.SELECT_MORE:
                                        // : 2022-01-21  多选
                                        chatAdapter.setEdit(true);
                                        operateingMsgBean.setSelected(true);
                                        chatAdapter.notifyDataSetChanged();
                                        mBottomEditMsgRv.setVisibility(View.VISIBLE);
                                        hideBottomAndKeyboard();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                        break;

                }
                return true;
            }

        });
    }


    /**
     * 获取所有选中的消息
     */
    private List<MessageBodyBean> getAllSelectedMsg() {
        List<MessageBodyBean> arrays = new ArrayList<>();
        List<MultipleItem> multipleItemList = chatAdapter.getData();
        for (MultipleItem multipleItem : multipleItemList) {
            if (MultipleItem.ITEM_CHAT_DATE_MSG != multipleItem.getItemType()) {
                MessageBodyBean messageBodyBean = (MessageBodyBean) multipleItem.getObject();
                if (messageBodyBean.isSelected()) {
                    arrays.add(messageBodyBean);
                    messageBodyBean.setSelected(false);
                }
            }
        }

        return arrays;
    }

    /**
     * 获取所有选中的条目  并取消本地保存
     */
    private List<MultipleItem> getAllSelectedItem(boolean deleteStore) {
        List<MultipleItem> arrays = new ArrayList<>();
        List<MultipleItem> multipleItemList = chatAdapter.getData();
        for (MultipleItem multipleItem : multipleItemList) {
            if (MultipleItem.ITEM_CHAT_DATE_MSG != multipleItem.getItemType()) {
                MessageBodyBean messageBodyBean = (MessageBodyBean) multipleItem.getObject();
                if (messageBodyBean.isSelected()) {
                    arrays.add(multipleItem);
                    if (deleteStore) {
                        ObjectBox.deleteMessage(messageBodyBean);
                    }
                }
            }
        }

        return arrays;
    }

    //查看图片
    private void displayPicVideo(MessageBodyBean messageBodyBean) {
        int position = 0;
        for (int i = 0; i < allPicVideoPath.size(); i++) {
            MessageBodyBean path = allPicVideoPath.get(i);
            if (messageBodyBean.getCreateTime() == path.getCreateTime()) {
                position = i;
                break;
            }
        }
        startActivity(new Intent(mContext, PicVideoDisplayActivity.class)
                .putParcelableArrayListExtra(BASE_PARCELABLE, allPicVideoPath)
                .putExtra(PicVideoDisplayActivity.IMAGEITEM, position));

    }

    /**
     * 添加时间标识
     *
     * @param startBean
     * @param endBean
     */
    private void addDateTag(MessageBodyBean startBean, MessageBodyBean endBean) {
        String startTime = null;
        if (startBean == null) {
            startTime = CalendarUtil.getCurrentTime();
        } else {
            startTime = CalendarUtil.formatSystemCurrentMillis(startBean.getCreateTime());
        }
        String endTime = CalendarUtil.formatSystemCurrentMillis(endBean.getCreateTime());
        if (CalendarUtil.getGapMinutes(startTime, endTime) > 5) {
            //两个消息间隔5分钟  就标记时间
            chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_DATE_MSG, new MessageBodyBean(endTime, 100)));
        }
    }


    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        allPicVideoPath.clear();
        ACTIVITY_IS_ON = false;
        AudioPlayManager.getInstance().stopPlay();
        AudioRecordManager.getInstance(this).setAudioRecordListener(null);
        AudioPlayManager.getInstance().release();
        releaseDialog();
        if (noticePeopleDialog != null) {
            if (noticePeopleDialog.isShowing()) {
                noticePeopleDialog.dismiss();
            }
            noticePeopleDialog = null;
        }
    }

    /**
     * 释放dialog
     */
    private void releaseDialog() {
        if (baseBottomDialog != null) {
            if (baseBottomDialog.isAdded()) {
                onItemClick = null;
                if (baseBottomDialog.getDialog().isShowing()) {
                    baseBottomDialog.dismiss();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ivAudio:
                //切换到语音
                if (mBtnAudio.isShown()) {
                    hideAudioButton();
                    mContentEt.requestFocus();
                    if (mEmotionKeyboard != null) {
                        mEmotionKeyboard.showSoftInput();
                        scrollRecyclerview();
                    }
                } else {
                    showAudioButton();
                    hideEmotionLayout();
                    hideMoreLayout();
                }
                break;
            case R.id.close_quote_iv:
                //关闭引用
                mQuoteLl.setVisibility(View.GONE);
                mQuoteContentTv.setTag(null);
                break;
            case R.id.tvSend:
                //发送普通消息的逻辑
                switch (chatType) {
                    case 0:
                        if (privateContactBean == null) {
                            return;
                        }
                        sendNormalMsg(privateContactBean, getTextViewValue(mContentEt));
                        Hawk.delete(HawkProperty.getDraftKey(privateContactId, true));
                        break;
                    case 1:
                        // : 2022-01-13 群聊 发送群消息

                        sendGroupNormalMsg(getTextViewValue(mContentEt));
                        Hawk.delete(HawkProperty.getDraftKey(groupId, false));
                        break;
                    case 2:
                        // TODO: 2022-01-13 密聊 发送私密消息
                        break;
                    default:
                        break;
                }
                mContentEt.setText("");
                mQuoteLl.setVisibility(View.GONE);
                mQuoteContentTv.setTag(null);
                break;

        }
    }

    /**
     * 发送普通信息
     */
    private void sendNormalMsg(ContactBean toContactBean, String content) {
        MessageBodyBean messageBody = OperateMsgUtil.getPrivateMsg(0, toContactBean.getId(), toContactBean.getUuid(),
                toContactBean.getRemarksNickname(), toContactBean.getHeadPortrait(), content);
        MessageBodyBean quoteMsgBean = (MessageBodyBean) mQuoteContentTv.getTag();
        if (quoteMsgBean != null) {
            messageBody.setQuoteMsg(GsonTools.createGsonString(quoteMsgBean));
        }
        mPresenter.sendPrivateMessage(OperateMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
        if (toContactBean.getId() == privateContactBean.getId()) {
            addDateTag(mPresenter.findPrivateChatRecordLastMessage(messageBody.getFromUserId()), messageBody);
            chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_TEXT_MSG, messageBody));
            scrollRecyclerview();
        }
        ObjectBox.addMessage(messageBody);
    }

    /**
     * 发送群聊普通信息
     */
    private void sendGroupNormalMsg(String content) {
        MessageBodyBean messageBody = OperateMsgUtil.getGroupMsg(0, groupId, groupBean.getGroupCreateUserId(),
                groupBean.getUserNickname(), content);
        MessageBodyBean quoteMsgBean = (MessageBodyBean) mQuoteContentTv.getTag();
        if (quoteMsgBean != null) {
            messageBody.setQuoteMsg(GsonTools.createGsonString(quoteMsgBean));
        }
        if (!atUsers.isEmpty()) {
            //有@的成员
            messageBody.setAtUserId(atUsers.toString().substring(1, atUsers.toString().length() - 1));
        } else {
            messageBody.setAtUserId(null);
        }
        mPresenter.sendGroupMessage(OperateMsgUtil.getMsgBuilder(messageBody).build(), AppHttpPath.SEND_MSG);
        addDateTag(mPresenter.findGroupChatRecordLastMessage(groupId), messageBody);
        chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_TEXT_MSG, messageBody));
        ObjectBox.addMessage(messageBody);
        scrollRecyclerview();
        atUsers.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == 101) {//拍照
                    String path = data.getStringExtra("path");
                    uploadPicFile(path);
                } else if (resultCode == 102) {//拍视频
                    String path = data.getStringExtra("path");
                    uploadVideoFile(path);
                } else if (resultCode == 103) {
                    Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show();
                }
                break;


        }
        switch (resultCode) {
            case GroupInfoActivity.GROUP_INFO_REQUEST:
            case ChatInfoActivity.CHAT_INFO_REQUEST:
                initAdapterData(data);
                initAdapterSelectStatus();
                break;
            case SelectFileActivity.REQUEST_SELECT_FILES:
                //选择文件
                if (data != null) {
                    ArrayList<String> filePaths =
                            data.getStringArrayListExtra(SelectFileActivity.REQUEST_SELECT_FILES_STR);
                    ArrayList<FileSelectBean> files =
                            data.getParcelableArrayListExtra(SelectFileActivity.REQUEST_SELECT_FILES_BEANS);
                    if (filePaths != null) {
                        for (int i = 0; i < filePaths.size(); i++) {
                            FileSelectBean fileSelectBean = files.get(i);
                            switch (chatType) {
                                case 0:
                                    MessageBodyBean messageBody = OperateMsgUtil.getPrivateMsg(8, privateContactId,
                                            privateContactBean.getUuid(), privateContactBean.getRemarksNickname(),
                                            privateContactBean.getHeadPortrait(), filePaths.get(i));
                                    messageBody.setFileSize(FileCacheUtils.formetFileSize(fileSelectBean.getFileSize()));
                                    messageBody.setFileName(fileSelectBean.getFileName());
                                    messageBody.setLocalCatchPath(fileSelectBean.getFilePath());
                                    mPresenter.sendPrivateMessage(OperateMsgUtil.getMsgBuilder(messageBody).build(),
                                            AppHttpPath.SEND_MSG);
                                    addDateTag(mPresenter.findPrivateChatRecordLastMessage(messageBody.getFromUserId()), messageBody);
                                    chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE, messageBody));
                                    ObjectBox.addMessage(messageBody);
                                    break;
                                case 1:
                                    // : 2022-01-13 群聊 发送文件信息
                                    MessageBodyBean groupMessageBody = OperateMsgUtil.getGroupMsg(8, groupId,
                                            groupBean.getUserNickname(), filePaths.get(i));
                                    groupMessageBody.setFileSize(FileCacheUtils.formetFileSize(fileSelectBean.getFileSize()));
                                    groupMessageBody.setFileName(fileSelectBean.getFileName());
                                    groupMessageBody.setLocalCatchPath(fileSelectBean.getFilePath());
                                    mPresenter.sendGroupMessage(OperateMsgUtil.getMsgBuilder(groupMessageBody).build(),
                                            AppHttpPath.SEND_MSG);
                                    addDateTag(mPresenter.findGroupChatRecordLastMessage(groupId), groupMessageBody);
                                    chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE,
                                            groupMessageBody));
                                    ObjectBox.addMessage(groupMessageBody);
                                    break;
                                case 2:
                                    // TODO: 2022-01-13 密聊 发送文件信息
                                    break;
                                default:
                                    break;
                            }
                        }
                        scrollRecyclerview();
                    }
                }


                break;

            case SelectContactActivity.REQUEST_CONTACT:
                if (data != null) {
                    ContactBean cardContactBean = data.getParcelableExtra(BASE_PARCELABLE);
                    View view = LayoutInflater.from(mContext).inflate(R.layout.share_card_layout, null);
                    sendCardDialog = new AlertDialog.Builder(mContext)
                            .setView(view)
                            .setCancelable(false)
                            .show();
                    sendCardDialog.getWindow().setBackgroundDrawableResource(R.drawable.sp_filled_white_10dp);
                    sendCardDialog.getWindow().setLayout(ScreenUtils.getInstance(mContext).getScreenWidth() - DisplayUtil.dp2px(mContext, 80), LinearLayout.LayoutParams.WRAP_CONTENT);

                    mReceiverHeadIv = (ImageView) view.findViewById(R.id.receiver_head_iv);
                    mReceiverNameTv = (TextView) view.findViewById(R.id.receiver_name_tv);

                    switch (chatType) {
                        case 0:
                            ImageLoadUtil.loadSquareImage(mContext,
                                    UrlFormatUtil.getImageThumUrl(privateContactBean.getHeadPortrait()), mReceiverHeadIv);
                            mReceiverNameTv.setText(privateContactBean.getRemarksNickname());
                            break;
                        case 1:
                            ImageLoadUtil.loadSquareImage(mContext,
                                    UrlFormatUtil.getImageThumUrl(groupBean.getGroupPicture()), mReceiverHeadIv);
                            mReceiverNameTv.setText(groupBean.getGroupName());
                            break;
                        default:
                            break;
                    }
                    mCardNameTv = (TextView) view.findViewById(R.id.card_name_tv);
                    mCardNameTv.append(cardContactBean.getNickname());
                    mToReceiverMsgEt = (EditText) view.findViewById(R.id.to_receiver_msg_et);
                    view.findViewById(R.id.close_dialog_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendCardDialog.dismiss();

                        }
                    });
                    view.findViewById(R.id.send_card_tv).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 发送名片
                            sendCardDialog.dismiss();
                            switch (chatType) {
                                case 0:
                                    // : 2021-12-07 发送名片
                                    MessageBodyBean messageBody = OperateMsgUtil.getPrivateMsg(7, privateContactId,
                                            privateContactBean.getUuid(), privateContactBean.getRemarksNickname(),
                                            privateContactBean.getHeadPortrait(), "名片");
                                    messageBody.setOtherUserId(cardContactBean.getId());
                                    messageBody.setOtherAccount(cardContactBean.getAccountNumber());
                                    messageBody.setOtherHead(cardContactBean.getHeadPortrait());
                                    messageBody.setOtherNickname(cardContactBean.getNickname());
                                    mPresenter.sendPrivateMessage(OperateMsgUtil.getMsgBuilder(messageBody).build(),
                                            AppHttpPath.SEND_MSG);
                                    addDateTag(mPresenter.findPrivateChatRecordLastMessage(messageBody.getFromUserId()), messageBody);
                                    chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_CARD, messageBody));
                                    ObjectBox.addMessage(messageBody);
                                    break;
                                case 1:
                                    // : 2022-01-13 群聊 发送名片
                                    MessageBodyBean fileMsgBody = OperateMsgUtil.getGroupMsg(7, groupId,
                                            groupBean.getUserNickname(), "名片");
                                    fileMsgBody.setOtherUserId(cardContactBean.getId());
                                    fileMsgBody.setOtherAccount(cardContactBean.getAccountNumber());
                                    fileMsgBody.setOtherHead(cardContactBean.getHeadPortrait());
                                    fileMsgBody.setOtherNickname(cardContactBean.getNickname());
                                    mPresenter.sendGroupMessage(OperateMsgUtil.getMsgBuilder(fileMsgBody).build(),
                                            AppHttpPath.SEND_MSG);
                                    addDateTag(mPresenter.findGroupChatRecordLastMessage(groupId), fileMsgBody);
                                    chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_CARD, fileMsgBody));
                                    ObjectBox.addMessage(fileMsgBody);
                                    break;
                                case 3:
                                    // TODO: 2022-01-13 密聊 发送名片
                                    break;
                                default:
                                    break;
                            }

                            scrollRecyclerview();
                            if (!TextUtils.isEmpty(getTextViewValue(mToReceiverMsgEt))) {
                                //单独发一条信息
                                switch (chatType) {
                                    case 0:
                                        sendNormalMsg(privateContactBean, getTextViewValue(mToReceiverMsgEt));
                                        break;
                                    case 1:
                                        // : 2022-01-13 群聊
                                        sendGroupNormalMsg(getTextViewValue(mToReceiverMsgEt));
                                        break;
                                    case 2:
                                        // TODO: 2022-01-13 密聊
                                        break;
                                    default:
                                        break;
                                }
                                hideBottomAndKeyboard();
                            }


                        }
                    });
                }

                break;
            case BASE_REQUEST_RESULT:
                if (data != null) {
                    lat = data.getDoubleExtra(LocateSelectionActivity.LAT, 0.0);
                    lng = data.getDoubleExtra(LocateSelectionActivity.LNG, 0.0);
                    addrName = data.getStringExtra(LocateSelectionActivity.ADDRNAME);
                    addrDes = data.getStringExtra(LocateSelectionActivity.ADDRDES);
                    switch (chatType) {
                        case 0:
                            MessageBodyBean messageBodyBean = OperateMsgUtil.getPrivateLocateMsg(privateContactId,
                                    privateContactBean.getUuid(), privateContactBean.getRemarksNickname(),
                                    privateContactBean.getHeadPortrait(),
                                    "", addrName, addrDes, String.valueOf(lat), String.valueOf(lng));
                            addDateTag(mPresenter.findPrivateChatRecordLastMessage(messageBodyBean.getFromUserId()),
                                    messageBodyBean);
                            chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_LOCATE, messageBodyBean));
                            messageBodyBean.setLocalCatchPath(FileCacheUtils.getMapShotImagePath());
                            messageBodyBean.setAdapterPosition(chatAdapter.getData().size() - 1);
                            mUploadUtil.submit(BaseChatActivity.this,
                                    new UploadFileBean(FileCacheUtils.getMapShotImagePath(), messageBodyBean));
                            ObjectBox.addMessage(messageBodyBean);

                            break;
                        case 1:
                            // : 2022-01-13 群聊 地图截图
                            MessageBodyBean mapMsgBean = OperateMsgUtil.getGroupLocateMsg(groupId, "", addrName, addrDes
                                    , String.valueOf(lat), String.valueOf(lng), groupBean.getUserNickname());
                            addDateTag(mPresenter.findGroupChatRecordLastMessage(groupId), mapMsgBean);
                            chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_LOCATE, mapMsgBean));
                            mapMsgBean.setLocalCatchPath(FileCacheUtils.getMapShotImagePath());
                            mapMsgBean.setAdapterPosition(chatAdapter.getData().size() - 1);
                            mUploadUtil.submit(BaseChatActivity.this,
                                    new UploadFileBean(FileCacheUtils.getMapShotImagePath(), mapMsgBean));
                            ObjectBox.addMessage(mapMsgBean);
                            break;
                        case 2:
                            // TODO: 2022-01-13 密聊 地图截图
                            break;
                        default:
                            break;
                    }

                    //                    mPresenter.uploadFile(MainContract.UPLOAD_MAP_SHOT, FileCacheUtils
                    //                    .getMapShotImagePath());
                    scrollRecyclerview();

                }
                break;

            case ContactorInfoActivity.CONTACT_INFO_RESULT:
                switch (chatType) {
                    case 0:
                        setTitleName(UserInfoManager.getContactRemarkName(privateContactId, privateContactBean.getRemarksNickname()));
                        break;
                    case 1:

                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void selectedPicsAndEmpressed(List<String> icons) {
        super.selectedPicsAndEmpressed(icons);
        if (icons.size() > 0) {
            if (fileSizeIsOk(icons)) {
                // TODO: 2022-03-09 发送图片或者视频文件

                for (String picPath : icons) {
                    if (1 == FileCacheUtils.getFileType(picPath)) {
                        uploadPicFile(picPath);
                    } else if (2 == FileCacheUtils.getFileType(picPath)) {
                        uploadVideoFile(picPath);
                    }
                }

            }
        }
    }

    /**
     * 上传图片文件
     *
     * @param picPath
     */
    private void uploadPicFile(String picPath) {
        hideBottomAndKeyboard();
        //发送图片文件
        ImageLoadUtil.getExifOrientation(mContext, picPath, new ImageLoadUtil.OnImageLoadSuccess() {
            @Override
            public void loadSuccess(int width, int height) {
                switch (chatType) {
                    case 0:
                        MessageBodyBean messageBodyBean = OperateMsgUtil.getPrivateMsg(1, privateContactId,
                                privateContactBean.getUuid(), privateContactBean.getRemarksNickname(),
                                privateContactBean.getHeadPortrait(), "");
                        messageBodyBean.setRotation(width > height ? "0" : "90");
                        addDateTag(mPresenter.findPrivateChatRecordLastMessage(messageBodyBean.getFromUserId()),
                                messageBodyBean);
                        chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_PIC_VIDEO, messageBodyBean));
                        scrollRecyclerview();
                        messageBodyBean.setLocalCatchPath(picPath);
                        messageBodyBean.setAdapterPosition(chatAdapter.getData().size() - 1);
                        mUploadUtil.submit(BaseChatActivity.this, new UploadFileBean(picPath, messageBodyBean));
                        ObjectBox.addMessage(messageBodyBean);
                        allPicVideoPath.add(messageBodyBean);

                        break;
                    case 1:
                        // : 2022-01-13 群聊 发送图片文件
                        MessageBodyBean groupPicMsgBean = OperateMsgUtil.getGroupMsg(1, groupId,
                                groupBean.getUserNickname(), picPath);
                        groupPicMsgBean.setRotation(width > height ? "0" : "90");
                        addDateTag(mPresenter.findGroupChatRecordLastMessage(groupId), groupPicMsgBean);
                        chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_PIC_VIDEO, groupPicMsgBean));
                        scrollRecyclerview();
                        groupPicMsgBean.setLocalCatchPath(picPath);
                        groupPicMsgBean.setAdapterPosition(chatAdapter.getData().size() - 1);
                        mUploadUtil.submit(BaseChatActivity.this, new UploadFileBean(picPath, groupPicMsgBean));
                        ObjectBox.addMessage(groupPicMsgBean);
                        allPicVideoPath.add(groupPicMsgBean);

                        break;
                    case 2:
                        // TODO: 2022-01-13 密聊 发送图片文件
                        break;
                    default:
                        break;
                }


            }
        });


    }

    /**
     * 上传视频文件
     *
     * @param picPath
     */
    private void uploadVideoFile(String picPath) {
        hideBottomAndKeyboard();

        //发送视频文件
        switch (chatType) {
            case 0:

                // TODO: 2022/4/10 先上传一张视频的缩略图
                MessageBodyBean messageBodyBean = OperateMsgUtil.getPrivateMsg(2, privateContactId,
                        privateContactBean.getUuid(), privateContactBean.getRemarksNickname(),
                        privateContactBean.getHeadPortrait(), "");
                messageBodyBean.setLocalCatchPath(picPath);
                RxScheduler.doTask(this, new RxTask<String>() {
                    @Override
                    public String doOnIoThread() {
                        Bitmap videoBitmap = ImageLoadUtil.getVideoThumbnail(picPath);
                        return FileCacheUtils.saveBitmap(videoBitmap, ImageLoadUtil.getVideoThumbnailName(messageBodyBean.getCreateTime()), true);
                    }

                    @Override
                    public void doOnUIThread(String str) {
                        //封面图保存到本地之后上传到后台
                        mUploadUtil.submit(BaseChatActivity.this, new UploadFileBean(str, messageBodyBean));

                    }
                });


                break;
            case 1:
                // : 2022-01-13 群聊 发送视频文件
                MessageBodyBean groupVideoFileBean = OperateMsgUtil.getGroupMsg(2, groupId, groupBean.getUserNickname(),
                        picPath);
                groupVideoFileBean.setLocalCatchPath(picPath);
                // TODO: 2022/4/10 先上传一张视频的缩略图
                RxScheduler.doTask(this, new RxTask<String>() {
                    @Override
                    public String doOnIoThread() {
                        Bitmap videoBitmap = ImageLoadUtil.getVideoThumbnail(picPath);
                        return FileCacheUtils.saveBitmap(videoBitmap, ImageLoadUtil.getVideoThumbnailName(groupVideoFileBean.getCreateTime()), true);
                    }

                    @Override
                    public void doOnUIThread(String str) {
                        //封面图保存到本地之后上传到后台
                        mUploadUtil.submit(BaseChatActivity.this, new UploadFileBean(str, groupVideoFileBean));

                    }
                });


                break;
            case 2:
                // TODO: 2022-01-13 密聊 发送视频文件
                break;
            default:
                break;
        }

    }

    private boolean fileSizeIsOk(List<String> icons) {
        for (String icon : icons) {
            File file = new File(icon);
            try {
                long fileSize = FileCacheUtils.getFileSize(file);
                if (FileCacheUtils.isOutInMaxLength(fileSize)) {
                    ToastUtils.toast(mContext, "不能选择大于500M的文件");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.COLLECT:
                operateingMsgBean.setCollected(true);
                ObjectBox.addMessage(operateingMsgBean);
                ToastUtils.toast(mContext, "收藏成功");
                break;
            case AppHttpPath.GET_GROUP_INFO:
                GroupDetailBean groupDetailBean = (GroupDetailBean) o;
                groupBean = groupDetailBean.getData();
                if (chatAdapter.getData().size() == 0) {
                    getGruopRecord();
                }

                break;
            case MainContract.UPLOAD_AUDIO_FILE:
                //上传音频文件

                List<String> audioPaths = (List<String>) o;
                if (audioPaths != null && !audioPaths.isEmpty()) {
                    for (String picPath : audioPaths) {
                        switch (chatType) {
                            case 0:
                                MessageBodyBean messageBodyBean = OperateMsgUtil.getPrivateMsg(3, privateContactId,
                                        privateContactBean.getUuid(), privateContactBean.getRemarksNickname(),
                                        privateContactBean.getHeadPortrait(), picPath);
                                messageBodyBean.setDuration(String.valueOf(mDuration));
                                addDateTag(mPresenter.findPrivateChatRecordLastMessage(messageBodyBean.getFromUserId()), messageBodyBean);
                                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_SEND_AUDIO, messageBodyBean));
                                ObjectBox.addMessage(messageBodyBean);
                                mPresenter.sendPrivateMessage(OperateMsgUtil.getMsgBuilder(messageBodyBean).build(),
                                        AppHttpPath.SEND_MSG);
                                break;
                            case 1:
                                // : 2022-01-13 群聊 上传音频文件
                                MessageBodyBean audioMessageBodyBean = OperateMsgUtil.getGroupMsg(3, groupId,
                                        groupBean.getUserNickname(), picPath);
                                audioMessageBodyBean.setDuration(String.valueOf(mDuration));
                                addDateTag(mPresenter.findGroupChatRecordLastMessage(groupId), audioMessageBodyBean);
                                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_SEND_AUDIO,
                                        audioMessageBodyBean));
                                ObjectBox.addMessage(audioMessageBodyBean);
                                mPresenter.sendGroupMessage(OperateMsgUtil.getMsgBuilder(audioMessageBodyBean).build(),
                                        AppHttpPath.SEND_MSG);
                                break;
                            case 2:
                                // TODO: 2022-01-13 密聊 上传音频文件
                                break;
                            default:
                                break;
                        }


                    }
                }
                scrollRecyclerview();
                break;

            case AppHttpPath.GET_UNREAD_GROUP_MSG:
            case AppHttpPath.GET_UNREAD_PRIVATE_MSG:
                MessageListBean messageListBean = (MessageListBean) o;
                if (messageListBean != null) {
                    List<MessageBodyBean> messageBodyBeans = messageListBean.getData();
                    if (messageBodyBeans != null && !messageBodyBeans.isEmpty()) {
                        for (int i = 0; i < messageBodyBeans.size(); i++) {
                            MessageBodyBean startBean = messageBodyBeans.get(i);
                            startBean.setId(0);
                            startBean.setRead(true);
                            if (i < messageBodyBeans.size() - 1) {
                                MessageBodyBean endBean = messageBodyBeans.get(i + 1);
                                addDateTag(startBean, endBean);
                            }
                            ObjectBox.addMessage(startBean);
                            initAdapterDataFromMsgTypes(startBean);
                        }
                        scrollRecyclerview();
                    }
                }

                break;

            default:
                break;
        }
    }

    /**
     * 获取群组的聊天记录
     */
    private void getGruopRecord() {
        if (groupBean == null) {
            return;
        }
        setTitleName(groupBean.getGroupName());
        //获取历史数据
        List<MessageBodyBean> arrays = mPresenter.findGroupChatRecord(groupId);
        if (arrays != null && arrays.size() > 0) {
            for (int i = 0; i < arrays.size(); i++) {
                MessageBodyBean startBean = arrays.get(i);
                if (searchedMsgId == startBean.getId()) {
                    searchedMsgPosition = i;
                }
                if (!startBean.isRead()) {
                    startBean.setRead(true);
                    //更新数据库数据
                    ObjectBox.addMessage(startBean);
                }
                initAdapterDataFromMsgTypes(startBean);
                if (i < arrays.size() - 1) {
                    MessageBodyBean endBean = arrays.get(i + 1);
                    addDateTag(startBean, endBean);
                }
                if (i == arrays.size() - 1) {
                    if (!startBean.isRead() && startBean.isCanDelete()) {
                        ObjectBox.deleteMessage(startBean);
                    }
                }


            }
        }
        if (searchedMsgId > 0) {
            scrollRecyclerview();
        }
        mPresenter.getGroupUnreadMsg(getBaseBuilder().add("groupId", String.valueOf(groupId)).build(),
                AppHttpPath.GET_UNREAD_GROUP_MSG);
    }


    private void initAdapterDataFromMsgTypes(MessageBodyBean messageBean) {
        switch (messageBean.getMsgType()) {
            case 0:
                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_TEXT_MSG, messageBean));
                break;
            case 1:
            case 2:
                allPicVideoPath.add(messageBean);
                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_PIC_VIDEO, messageBean));
                break;
            case 3:
                //发送语音
                if (UserInfoManager.getUserId() == messageBean.getFromUserId()) {
                    chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_SEND_AUDIO, messageBean));
                } else {
                    chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_RECEIVE_AUDIO, messageBean));
                }
                break;
            case 5:
            case 4:
                //视频通话
                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_VIDEO_CALL, messageBean));
                break;
            case 6:
                //位置信息
                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_LOCATE, messageBean));
                break;
            case 7:
                //名片
                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_CARD, messageBean));
                break;
            case 8:
                //文件
                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE, messageBean));
                break;
            case 9:
                //文件
                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_RECORD, messageBean));
                break;
            case 10:
                //提示
                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_NOTICE, messageBean));
                break;
            case 11:
                //外部分享链接
                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_OUTSIDE_SHARE, messageBean));
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(getTextViewValue(mContentEt))) {
            switch (chatType) {
                case 0:
                    MessageBodyBean messageBody = OperateMsgUtil.getPrivateMsg(0, privateContactId,
                            privateContactBean.getUuid(), privateContactBean.getRemarksNickname(),
                            privateContactBean.getHeadPortrait(), getTextViewValue(mContentEt));
                    messageBody.setDraft(true);
                    Hawk.put(HawkProperty.getDraftKey(privateContactId, true), messageBody);
                    break;
                case 1:
                    MessageBodyBean groupMsgBean = OperateMsgUtil.getGroupMsg(0, groupId, 0, null, getTextViewValue(mContentEt));
                    groupMsgBean.setDraft(true);
                    Hawk.put(HawkProperty.getDraftKey(groupId, false), groupMsgBean);
                    break;
                case 2:
                    // TODO: 2022/3/22 密聊
                    break;
                default:
                    break;
            }
        } else {
            switch (chatType) {
                case 0:
                    if (Hawk.contains(HawkProperty.getDraftKey(privateContactId, true))) {
                        Hawk.delete(HawkProperty.getDraftKey(privateContactId, true));
                    }
                    break;
                case 1:
                    if (Hawk.contains(HawkProperty.getDraftKey(groupId, false))) {
                        Hawk.delete(HawkProperty.getDraftKey(groupId, false));
                    }
                    break;
                case 2:
                    // TODO: 2022/3/22 密聊
                    break;
                default:
                    break;
            }
        }
        if (initAdapterSelectStatus()) {
            return;
        }
        if (mEmotionKeyboard != null) {
            if (mEmotionKeyboard.isSoftInputShown()) {
                mEmotionKeyboard.hideSoftInput();
            }
        }
        AudioPlayManager.getInstance().stopPlay();
        if (mChatFl.isShown() || mMoreActionRv.isShown()) {
            mEmotionKeyboard.interceptBackPress();
        } else {
            startActivity(new Intent(mContext, MainActivity.class));
        }
    }

    /**
     * 重置适配器选择状态
     *
     * @return
     */
    private boolean initAdapterSelectStatus() {
        if (chatAdapter.isEdit()) {
            mBottomEditMsgRv.setVisibility(View.GONE);
            chatAdapter.setEdit(false);
            //将所有消息的选中状态清零
            getAllSelectedMsg();
            chatAdapter.notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    public void initListener() {
        mContentEt.addTextChangedListener(new TextWatcher() {
            boolean isAt = false;//是否是@

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String content = s.toString();
                if (after == 0) {
                    if (isAt) {
                        return;
                    }
                    if (content.endsWith("\u3000")) {
                        //删除@的成员
                        if (!atUsers.isEmpty()) {
                            atUsers.remove(atUsers.size() - 1);
                        }
                        isAt = true;
                        content = content.substring(0, content.lastIndexOf("@"));
                        mContentEt.setText(content);
                        mContentEt.setSelection(content.length());
                    } else {
                        isAt = false;
                    }
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // : 2022-02-08 这个地方添加@对应的逻辑
                String content = s.toString().trim();
                if (TextUtils.isEmpty(content) || !content.endsWith("\u3000")) {
                    isAt = false;
                }
                content = content.substring(start, s.length());
                if ("@".equals(content)) {
                    if (noticePeopleDialog == null) {
                        View view = LayoutInflater.from(mContext).inflate(R.layout.select_notice_people, null);
                        noticePeopleDialog = new BottomSheetDialog(mContext);
                        noticePeopleDialog.setContentView(view);
                        noticePeopleDialog.setCanceledOnTouchOutside(false);
                        noticePeopleDialog.setCancelable(false);
                        view.findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                noticePeopleDialog.dismiss();
                            }
                        });
                    }
                    SelectGroupNoticePeopleFragment selectContactFragment =
                            (SelectGroupNoticePeopleFragment) getSupportFragmentManager().findFragmentById(R.id.select_contact_fg);
                    if (groupBean != null && groupBean.getUserInfoVoList() != null && !groupBean.getUserInfoVoList().isEmpty()) {
                        selectContactFragment.getGroupInfo(groupBean,
                                new SelectGroupNoticePeopleFragment.OnContactClick() {
                                    @Override
                                    public void contactClicked(ContactBean contactBean) {
                                        noticePeopleDialog.dismiss();
                                        String nickName = contactBean.getNickname();
                                        if ("所有人".equals(nickName)) {
                                            atUsers.clear();
                                            //如果是所有人  就传 -1
                                            atUsers.add(-1);
                                        }
                                        mContentEt.append(contactBean.getNickname() + "\u3000");
                                    }
                                });

                        noticePeopleDialog.show();
                    }

                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (!TextUtils.isEmpty(content)) {
                    mTvSend.setVisibility(View.VISIBLE);
                    mIvMore.setVisibility(View.GONE);
                } else {
                    mTvSend.setVisibility(View.GONE);
                    mIvMore.setVisibility(View.VISIBLE);
                }


            }
        });
    }


    //-------------------------------------------------语音--------------------------------------------------------------

    //初始化录音模块
    private void initAudioRecordManager() {
        AudioRecordManager.getInstance(this).setMaxVoiceDuration(60);
        final File audioDir = new File(Environment.getExternalStorageDirectory(), "AUDIO");
        if (!audioDir.exists()) {
            audioDir.mkdirs();
        }
        AudioRecordManager.getInstance(this).setAudioSavePath(audioDir.getAbsolutePath());
        AudioRecordManager.getInstance(this).setAudioRecordListener(new IAudioRecordListener() {

            private TextView mTimerTV;
            private TextView mStateTV;
            private ImageView mStateIV;
            private PopupWindow mRecordWindow;

            @Override
            public void initTipView() {
                View view = View.inflate(mContext, R.layout.popup_audio_wi_vo, null);
                mStateIV = (ImageView) view.findViewById(R.id.rc_audio_state_image);
                mStateTV = (TextView) view.findViewById(R.id.rc_audio_state_text);
                mTimerTV = (TextView) view.findViewById(R.id.rc_audio_timer);
                mRecordWindow = new PopupWindow(view, -1, -1);
                mRecordWindow.showAtLocation(mLlRoot, 17, 0, 0);
                mRecordWindow.setFocusable(true);
                mRecordWindow.setOutsideTouchable(false);
                mRecordWindow.setTouchable(false);
            }

            @Override
            public void setTimeoutTipView(int counter) {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.GONE);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
                    this.mTimerTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setRecordingTipView() {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void setAudioShortTipView() {
                if (this.mRecordWindow != null) {
                    mStateIV.setImageResource(R.mipmap.ic_volume_wraning);
                    mStateTV.setText(R.string.voice_short);
                }
            }

            @Override
            public void setCancelTipView() {
                if (this.mRecordWindow != null) {
                    this.mTimerTV.setVisibility(View.GONE);
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_cancel);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_cancel);
                    this.mStateTV.setBackgroundResource(R.drawable.corner_voice_style);
                }
            }

            @Override
            public void destroyTipView() {
                if (this.mRecordWindow != null) {
                    this.mRecordWindow.dismiss();
                    this.mRecordWindow = null;
                    this.mStateIV = null;
                    this.mStateTV = null;
                    this.mTimerTV = null;
                }
            }

            @Override
            public void onStartRecord() {//开始发送的状态
            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                mDuration = duration;
                //发送文件
                File file = new File(audioPath.getPath());
                if (!file.exists() || file.length() == 0L) {
                    return;
                }
                mPresenter.uploadFile(MainContract.UPLOAD_AUDIO_FILE, file.getAbsolutePath());
            }

            @Override
            public void onAudioDBChanged(int db) {
                switch (db / 5) {
                    case 0:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                        break;
                    case 1:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_2);
                        break;
                    case 2:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_3);
                        break;
                    case 3:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_4);
                        break;
                    case 4:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_5);
                        break;
                    case 5:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_6);
                        break;
                    case 6:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_7);
                        break;
                    default:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_8);
                }
            }
        });
    }

    //录音监听
    @SuppressLint("ClickableViewAccessibility")
    private void initAudioListener() {
        mBtnAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        AudioRecordManager.getInstance(mContext).startRecord();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isCancelled(view, motionEvent)) {
                            AudioRecordManager.getInstance(mContext).willCancelRecord();
                        } else {
                            AudioRecordManager.getInstance(mContext).continueRecord();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        AudioRecordManager.getInstance(mContext).stopRecord();
                        AudioRecordManager.getInstance(mContext).destroyRecord();
                        break;
                }
                return false;
            }
        });
    }

    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40) {
            return true;
        }

        return false;
    }

    private void initEmotionKeyboard() {
        mEmotionKeyboard = EmotionKeyboard.with(this);
        mEmotionKeyboard.bindToEditText(mContentEt);
        mEmotionKeyboard.bindToContent(mContentLl);
        mEmotionKeyboard.setEmotionLayout(mChatFl);
        mEmotionKeyboard.bindToEmotionButton(mIvEmo, mIvMore);
        mEmotionKeyboard.setOnEmotionButtonOnClickListener(new EmotionKeyboard.OnEmotionButtonOnClickListener() {
            @Override
            public boolean onEmotionButtonOnClickListener(View view) {
                switch (view.getId()) {
                    case R.id.ivEmo:
                        //表情按钮
                        if (!mEmojiLl.isShown()) {
                            //表情按钮没有展示
                            if (mMoreActionRv.isShown()) {
                                //正在展示更多布局
                                showEmotionLayout();
                                hideMoreLayout();
                                hideAudioButton();
                                return true;
                            }
                        } else if (mEmojiLl.isShown() && !mMoreActionRv.isShown()) {
                            mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
                            return false;
                        }
                        showEmotionLayout();
                        hideMoreLayout();
                        hideAudioButton();
                        break;
                    case R.id.ivMore:
                        if (!mMoreActionRv.isShown()) {
                            if (mEmojiLl.isShown()) {
                                showMoreLayout();
                                hideEmotionLayout();
                                hideAudioButton();
                                return true;
                            }
                        }
                        hideEmotionLayout();
                        hideAudioButton();
                        showMoreLayout();
                        break;
                }
                return false;
            }
        });
    }

    //----------------------------------------------------切换---------------------------------------------------------------------


    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showAudioButton() {
        mBtnAudio.setVisibility(View.VISIBLE);
        mContentEt.setVisibility(View.GONE);
        mIvAudio.setImageResource(R.mipmap.ic_cheat_keyboard);

        if (mChatFl.isShown()) {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.interceptBackPress();
            }
        } else {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.hideSoftInput();
            }
        }
    }

    private void hideAudioButton() {
        mBtnAudio.setVisibility(View.GONE);
        mContentEt.setVisibility(View.VISIBLE);
        mIvAudio.setImageResource(R.mipmap.ic_cheat_voice);
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        int index = mContentEt.getSelectionStart();
        Editable editableText = mContentEt.getEditableText();
        editableText.insert(index, emoji.getName());
        displayEmoji(mContentEt, index + emoji.getName().length());
    }

    @Override
    public void onEmojiDelete() {
        String content = mContentEt.getText().toString();
        int index = mContentEt.getSelectionStart();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if ("]".equals(content.substring(index - 1, index))) {
            int lastIndexOf = content.lastIndexOf("[", index - 1);
            if (lastIndexOf == -1) {
                onKeyDownDelete(mContentEt);
            } else {
                mContentEt.getText().delete(lastIndexOf, index);
                displayEmoji(mContentEt, lastIndexOf);
            }
            return;
        }
        onKeyDownDelete(mContentEt);
    }

    public void onKeyDownDelete(EditText editText) {
        int index = mContentEt.getSelectionStart();
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL, keyEvent);
        displayEmoji(mContentEt, index - 1);
    }

    public void displayEmoji(TextView textView, int indexSelection) {
        EmojiUtils.showEmojiTextView(this, textView, textView.getText().toString(), 20);
        if (textView instanceof EditText) {
            ((EditText) textView).setSelection(indexSelection);
        }
    }

    private void showEmotionLayout() {
        mEmojiLl.setVisibility(View.VISIBLE);
        mIvEmo.setImageResource(R.mipmap.ic_cheat_keyboard);
    }

    private void hideEmotionLayout() {
        mEmojiLl.setVisibility(View.GONE);
        mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
    }

    private void showMoreLayout() {
        if (!mMoreActionRv.isShown()) {
            mMoreActionRv.setVisibility(View.VISIBLE);
        }
    }

    private void hideMoreLayout() {
        mMoreActionRv.setVisibility(View.GONE);
    }

    private void hideBottomAndKeyboard() {
        mEmojiLl.setVisibility(View.GONE);
        mMoreActionRv.setVisibility(View.GONE);
        if (mEmotionKeyboard != null) {
            mEmotionKeyboard.interceptBackPress();
        }
    }


    @Override
    public void onUploadProgressChange(UploadFileBean uploadFileBean, int percent) {
        hideBottomAndKeyboard();
        MessageBodyBean messageBodyBean = uploadFileBean.getMessageBodyBean();
        if (messageBodyBean != null) {
            messageBodyBean.setUploadProgress(percent);
            chatAdapter.notifyItemChanged(messageBodyBean.getAdapterPosition(), R.id.sender_progress);
        }

    }

    @Override
    public void onUploadFinish(UploadFileBean uploadFileBean) {
        if (uploadFileBean.getCode() != 0) {
            ToastUtils.toast(mContext, "上传失败");
            return;
        }
        MessageBodyBean messageBodyBean = uploadFileBean.getMessageBodyBean();
        List<String> filePaths = uploadFileBean.getFilePaths();
        if (filePaths != null && filePaths.size() > 0) {
            // TODO: 2022/4/10 获取返回文件的文件名

            switch (messageBodyBean.getMsgType()) {
                case 2:
                    //视频文件
                    String fileName = getSavedFileName(filePaths.get(0));
                    if (fileName.startsWith(ImageLoadUtil.IMAGE_TYPE_VIDEO_THUM)) {
                        LogUtil.d("视频缩略图" + messageBodyBean.getContent());
                        //视频缩略图
                        messageBodyBean.setVideoCover(filePaths.get(0));
                        messageBodyBean.setContent(null);
                        ImageLoadUtil.getExifOrientation(mContext, FileCacheUtils.getAppImagePath(true) + fileName, new ImageLoadUtil.OnImageLoadSuccess() {
                            @Override
                            public void loadSuccess(int width, int height) {
                                FileBaseInfoBean fileBaseInfoBean = ImageLoadUtil.getVideoFileBaseInfo(messageBodyBean.getLocalCatchPath());
                                messageBodyBean.setRotation(width > height ? "0" : "90");
                                messageBodyBean.setDuration(fileBaseInfoBean.getDuration());
                                addDateTag(mPresenter.findPrivateChatRecordLastMessage(messageBodyBean.getFromUserId()),
                                        messageBodyBean);
                                chatAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_PIC_VIDEO, messageBodyBean));
                                allPicVideoPath.add(messageBodyBean);
                                messageBodyBean.setAdapterPosition(chatAdapter.getData().size() - 1);
                                ObjectBox.addMessage(messageBodyBean);
                                scrollRecyclerview();
                                //上传视频文件
                                mUploadUtil.submit(BaseChatActivity.this, new UploadFileBean(messageBodyBean.getLocalCatchPath(), messageBodyBean));
                            }
                        });
                    } else {
                        LogUtil.d("视频原图" + messageBodyBean.getContent());
                        //视频内容
                        messageBodyBean.setContent(filePaths.get(0));
                    }
                    break;
                case 6:
                    //位置
                default:
                    messageBodyBean.setContent(filePaths.get(0));
                    break;
            }
            ObjectBox.addMessage(messageBodyBean);
            if (!TextUtils.isEmpty(messageBodyBean.getContent())) {
                switch (chatType) {
                    case 0:
                        mPresenter.sendPrivateMessage(OperateMsgUtil.getMsgBuilder(messageBodyBean).build(), AppHttpPath.SEND_MSG);
                        break;
                    case 1:
                        mPresenter.sendGroupMessage(OperateMsgUtil.getMsgBuilder(messageBodyBean).build(), AppHttpPath.SEND_MSG);
                        break;
                    default:
                        break;
                }
            }

        }


    }

}

package com.juntai.wisdom.im.entrance.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.baidu.location.BDLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.base.BaseWebViewActivity;
import com.juntai.disabled.basecomponent.utils.ActionConfig;
import com.juntai.disabled.basecomponent.utils.ActivityManagerTool;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.MyApp;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.base.customview.CustomViewPager;
import com.juntai.wisdom.im.base.customview.MainPagerAdapter;
import com.juntai.wisdom.im.bean.AddContractOrGroupMsgBean;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.ContactListBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.GroupInfoByUuidBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.HomePageMenuBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.PeopleBean;
import com.juntai.wisdom.im.bean.UnReadMsgsBean;
import com.juntai.wisdom.im.bean.UserInfoByUUIDBean;
import com.juntai.wisdom.im.chat_module.ChatListFragment;
import com.juntai.wisdom.im.chat_module.OutSideShareActivity;
import com.juntai.wisdom.im.chat_module.QRScanActivity;
import com.juntai.wisdom.im.chat_module.groupchat.GroupChatActivity;
import com.juntai.wisdom.im.chat_module.groupchat.joinGroup.SelectGroupChatPeopleActivity;
import com.juntai.wisdom.im.contact.ContactFragment;
import com.juntai.wisdom.im.contact.ContactorInfoActivity;
import com.juntai.wisdom.im.contact.addFriend.AddFriendActivity;
import com.juntai.wisdom.im.contact.group.JoinGroupByUuidActivity;
import com.juntai.wisdom.im.entrance.LoginActivity;
import com.juntai.wisdom.im.mine.MyCenterFragment;
import com.juntai.wisdom.im.mine.SuggestionActivity;
import com.juntai.wisdom.im.search.SearchActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.NotificationTool;
import com.juntai.wisdom.im.utils.ObjectBox;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.juntai.wisdom.im.webSocket.MyWsManager;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseAppActivity<MainPresent> implements ViewPager.OnPageChangeListener,
        View.OnClickListener, MainContract.IBaseView {
    private MainPagerAdapter adapter;
    private LinearLayout mainLayout;
    private CustomViewPager mainViewpager;

    private TabLayout mainTablayout;
    private String[] title = new String[]{"聊天", "通讯录", "我的"};
    private int[] tabDrawables = new int[]{R.drawable.home_index, R.drawable.contact_index, R.drawable.home_msg};
    private SparseArray<Fragment> mFragments = new SparseArray<>();
    //
    CGBroadcastReceiver broadcastReceiver = new CGBroadcastReceiver();
    private PopupWindow popupWindow;

    private ContactFragment contactFragment;
    private ChatListFragment chatListFragment;

    @Override
    public int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initThirdShareLogic(intent, mContext,OutSideShareActivity.class);
        startWebSocket();
        chatListFragment.initAdapterData();
    }

    @Override
    public void initView() {
        initThirdShareLogic(getIntent(), mContext,OutSideShareActivity.class);
        //更新聊天列表
        HawkProperty.clearRedPoint(mContext.getApplicationContext());
        setTitleName("聊天");
        initLeftBackTv(false);
        setRightTvDrawable(R.mipmap.add_icon);
        getTitleRightIv().setImageResource(R.mipmap.search_icon);
        mainViewpager = findViewById(R.id.main_viewpager);
        mainTablayout = findViewById(R.id.main_tablayout);
        mainLayout = findViewById(R.id.main_layout);
        mainViewpager.setScanScroll(false);
        contactFragment = new ContactFragment();
        chatListFragment = new ChatListFragment();
        mFragments.append(0, chatListFragment);//
        mFragments.append(1, contactFragment);//
        mFragments.append(2, new MyCenterFragment());//我的
        //
        mainViewpager.setOffscreenPageLimit(4);
        initTab();
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionConfig.BROAD_LOGIN);
        intentFilter.addAction(ActionConfig.BROAD_CASE_DETAILS);
        registerReceiver(broadcastReceiver, intentFilter);
        startWebSocket();
        mPresenter.getUnreadFriendApply(getBaseBuilder().build(), AppHttpPath.UNREAD_APPLY_FRIEND);
    }


    private void startWebSocket() {
        MyWsManager.getInstance()
                .init(mContext.getApplicationContext())
                .setWsUrl(AppHttpPath.BASE_SOCKET + UserInfoManager.getUserUUID() + "/" + UserInfoManager.getUserId())
                .startConnect();
    }


    /**
     * 好友申请小红点
     */
    public void initNewFriendRedPoint(int amount) {
        Hawk.put(HawkProperty.FRIEND_APPLY, amount);
        adapter.setUnReadMsg(amount);
    }

    @Override
    public void initData() {
        update(false);
        //添加好友
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // : 2021-11-24 弹出pop窗口
                View view = LayoutInflater.from(mContext).inflate(R.layout.home_pop, null);
                popupWindow = new PopupWindow(view, DisplayUtil.dp2px(mContext, 120), ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                RecyclerView recyclerView = view.findViewById(R.id.home_pop_rv);
                HomePageMenuAdapter menuAdapter = new HomePageMenuAdapter(R.layout.item_home_menu);
                menuAdapter.setNewData(getMenuList());
                initRecyclerview(recyclerView, menuAdapter, LinearLayoutManager.VERTICAL);
                popupWindow.showAsDropDown(v, DisplayUtil.dp2px(mContext, -80), DisplayUtil.dp2px(mContext, -5));
                menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        HomePageMenuBean menuBean = (HomePageMenuBean) adapter.getData().get(position);
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        }
                        switch (menuBean.getName()) {
                            case MainContract.ADD_NEW_FRIEND:
                                startActivity(new Intent(mContext, AddFriendActivity.class));
                                break;
                            case MainContract.SCAN_IT:
                                // : 2021-12-12 扫一扫
                                startActivityForResult(new Intent(mContext, QRScanActivity.class), QRScanActivity.SCAN_RESULT);
                                break;
                            case MainContract.CREAT_GROUP_CHAT:
                                // : 2021-12-12  发起群聊
                                startActivity(new Intent(mContext, SelectGroupChatPeopleActivity.class)
                                        .putExtra(BaseActivity.BASE_ID, 0)
                                        .putParcelableArrayListExtra(BaseActivity.BASE_PARCELABLE, new ArrayList<PeopleBean>()));
                                break;
                            case MainContract.SUGGESTION_HELP:
                                // : 2021-12-12 建议和帮助
                                startActivity(new Intent(mContext, SuggestionActivity.class));


                                break;
//
                            default:
                                ToastUtils.toast(mContext, "暂未开放");
                                break;
                        }
                    }
                });


            }
        });

        getTitleRightIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // : 2021-11-24  首页搜索
                startActivity(new Intent(mContext, SearchActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (QRScanActivity.SCAN_RESULT == resultCode) {
            //扫描结果
            if (data != null) {
                String result = data.getStringExtra(BASE_STRING);
                resolveQrcode(result);

            }

        }
    }


    /**
     * 获取适配器数据
     *
     * @return
     */
    private List<HomePageMenuBean> getMenuList() {
        List<HomePageMenuBean> arrays = new ArrayList<>();
        arrays.add(new HomePageMenuBean(MainContract.ADD_NEW_FRIEND, R.mipmap.home_menu_add_friend_icon, true));
        arrays.add(new HomePageMenuBean(MainContract.SCAN_IT, R.mipmap.home_menu_scan, true));
        arrays.add(new HomePageMenuBean(MainContract.CREAT_GROUP_CHAT, R.mipmap.home_menu_create_chat, true));
        arrays.add(new HomePageMenuBean(MainContract.SUGGESTION_HELP, R.mipmap.home_menu_suggestion_help_icon, false));
        return arrays;
    }


    public void initTab() {
        adapter = new MainPagerAdapter(getSupportFragmentManager(), getApplicationContext(), title, tabDrawables,
                mFragments);
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOffscreenPageLimit(title.length);
        /*viewpager切换监听，包含滑动点击两种*/
        mainViewpager.addOnPageChangeListener(this);
        for (int i = 0; i < title.length; i++) {
            TabLayout.Tab tab = mainTablayout.newTab();
            if (tab != null) {
                if (i == 1) {
                    tab.setCustomView(adapter.getTabView(i, true));
                } else {
                    tab.setCustomView(adapter.getTabView(i, false));
                }
                mainTablayout.addTab(tab);
            }
        }

        mainTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainViewpager.setCurrentItem(tab.getPosition(), false);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

        //        mainTablayout.newTab();
        /*viewpager切换默认第一个*/
        mainViewpager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        getToolbar().setVisibility(View.VISIBLE);
        switch (i) {
            case 0:
                setTitleName("聊天");
                mImmersionBar.statusBarColor(R.color.gray_light)
                        .statusBarDarkFont(true)
                        .init();
                break;
            case 1:
                setTitleName("通讯录");
                adapter.setUnReadMsg(0);
                mImmersionBar.statusBarColor(R.color.gray_light)
                        .statusBarDarkFont(true)
                        .init();
                break;
            default:
                mImmersionBar.statusBarColor(R.color.white)
                        .statusBarDarkFont(true)
                        .init();
                getToolbar().setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.GET_CONTACT_LISTE:
                //获取群聊列表
                mPresenter.getGroupList(getBaseBuilder().build(), AppHttpPath.GET_GROUP_LIST);
                ContactListBean contactListBean = (ContactListBean) o;
                if (contactListBean != null) {
                    List<ContactBean> data = contactListBean.getData();
                    List<ContactBean> friendList = new ArrayList<>();
                    if (data != null) {
                        for (ContactBean datum : data) {
                            if (datum.getStatusType() != 3) {
                                friendList.add(datum);
                            }
                        }
                        Hawk.put(HawkProperty.getContactListKey(), friendList);
                    }
                    contactFragment.initContactData(friendList);


                }
                break;
            case AppHttpPath.GET_GROUP_LIST:

                GroupListBean groupListBean = (GroupListBean) o;
                if (groupListBean != null) {
                    List<GroupDetailInfoBean> arrays = groupListBean.getData();
                    if (arrays != null) {
                        ObjectBox.addGroup(arrays);
                    }
                }
                chatListFragment.initAdapterData();
                break;

            case AppHttpPath.ADD_FRIEND_BY_UUID:
                // : 2022-01-18 好友信息

                UserInfoByUUIDBean userInfoByUUIDBean = (UserInfoByUUIDBean) o;
                if (userInfoByUUIDBean != null && userInfoByUUIDBean.getData() != null) {
                    startActivity(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, userInfoByUUIDBean.getData().getId()));
                }

                break;
            case AppHttpPath.UNREAD_APPLY_FRIEND:
                // : 2022-01-18 好友申请

                BaseResult baseResult = (BaseResult) o;
                String message = baseResult.getMessage();
                if (!TextUtils.isEmpty(message)) {
                    initNewFriendRedPoint(Integer.parseInt(message));
                }
                break;
            case AppHttpPath.JOIN_GROUP_BY_UUID:
                GroupInfoByUuidBean groupInfoByUuidBean = (GroupInfoByUuidBean) o;
                if (groupInfoByUuidBean != null) {
                    GroupInfoByUuidBean.DataBean dataBean = groupInfoByUuidBean.getData();
                    GroupDetailInfoBean groupBean = GsonTools.modelA2B(dataBean, GroupDetailInfoBean.class);
                    groupBean.setGroupId(dataBean.getId());
                    startActivity(new Intent(mContext, GroupChatActivity.class)
                            .putExtra(BASE_ID, groupBean.getGroupId()));
                }
                break;
            default:
                break;
        }
    }


    /**
     * @param messageBodyBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void receiveMessage(MessageBodyBean messageBodyBean) {
        int chatType = messageBodyBean.getChatType();
        Log.d(TAG, "onMessage消息类型" + chatType);

        if (4 == chatType) {
            //好友申请
            String content = messageBodyBean.getContent();
            initNewFriendRedPoint(Integer.parseInt(content));
        } else {
            chatListFragment.onMessageReceived(messageBodyBean);
        }

    }

    /**
     * 当收到本地还没有存储的联系人或者群 发送消息的时候 更新联系人库 或者 群库
     *
     * @param addContractOrGroupMsgBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void receiveMessage(AddContractOrGroupMsgBean addContractOrGroupMsgBean) {
        MessageBodyBean messageBodyBean = addContractOrGroupMsgBean.getMessageBodyBean();
        //未读
        messageBodyBean.setRead(false);
        //保存消息到本地
        ObjectBox.addMessage(messageBodyBean);
        int chatType = messageBodyBean.getChatType();
        switch (chatType) {
            case 1:
            case 2:
                mPresenter.getContactList(getBaseBuilder().build(), AppHttpPath.GET_CONTACT_LISTE);
                break;
            default:
                break;
        }

    }

    /**
     * 未读消息  socket连接之后 服务端主动推送的消息
     *
     * @param unReadMsgsBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessageUnread(UnReadMsgsBean unReadMsgsBean) {
        chatListFragment.onMessageUnReadMsg(unReadMsgsBean);
    }

    /**
     * 登录被顶
     */
    public class CGBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ActionConfig.BROAD_LOGIN.equals(intent.getAction())) {
                ToastUtils.info(MyApp.app, "账号在另一设备登录");
                UserInfoManager.clearUserData();
                ActivityManagerTool.getInstance().finishApp();
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        }
    }

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }

    @Override
    public void onLocationReceived(BDLocation bdLocation) {

    }

    @Override
    public boolean requestLocation() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MyWsManager-----onResume---Mainactivity");
        mPresenter.getContactList(getBaseBuilder().build(), AppHttpPath.GET_CONTACT_LISTE);
        MyWsManager.getInstance().startConnect();
        //先获取通讯录列表 再获取群聊列表

        switch (mainViewpager.getCurrentItem()) {
            case 0:
                NotificationTool.SHOW_NOTIFICATION = false;
                break;
            case 1:
                NotificationTool.SHOW_NOTIFICATION = true;
                break;
            case 2:
                NotificationTool.SHOW_NOTIFICATION = true;
                break;
            default:
                break;
        }


    }

    @Override
    protected void onDestroy() {
        Log.e("EEEEEEEEEE", " = MainActivity  onDestroy");
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        }

        super.onDestroy();
        MyWsManager.getInstance().disconnect();
        HawkProperty.releaseGlobleMap();

    }

    @Override
    public void onBackPressed() {
        setAlertDialogHeightWidth(new AlertDialog.Builder(mContext)
                .setMessage("请选择退出方式")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApp.app.isFinish = true;
                        ActivityManagerTool.getInstance().finishApp();
                        MainActivity.super.onBackPressed();
                        ;
                    }
                })
                .setNegativeButton("挂起", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //模拟home键,发送广播
                        //sendBroadcast(new Intent().setAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                        // .putExtra("reason","homekey"));
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                }).show(), -1, 0);

    }

    @Override
    public void onPause() {
        super.onPause();
        NotificationTool.SHOW_NOTIFICATION = true;
    }

    /**
     * 解析二维码
     *
     * @param result
     */
    public void resolveQrcode(String result) {
        if (result.contains("juntaikeji")) {
            //内部二维码
            String uuid = result.substring(result.indexOf("=") + 1, result.indexOf("&"));
            String type = result.substring(result.lastIndexOf("=") + 1, result.length());
            if ("1".equals(type)) {
                //好友
                if (UserInfoManager.getUserUUID().equals(uuid)) {
                    ToastUtils.error(mContext, "不能添加自己为好友");
                    return;
                }

                mPresenter.addFriendByUuid(getBaseBuilder().add("uuid", uuid).build(), AppHttpPath.ADD_FRIEND_BY_UUID);
            } else {
                //群聊
                // : 2022-01-18 群信息
                if (!isGroupExsit(uuid)) {
                    //如果不是群成员
                    startActivity(new Intent(mContext, JoinGroupByUuidActivity.class).putExtra(BASE_STRING, uuid));
                } else {
                    //是群成员  获取群信息 然后跳转到群聊天界面
                    mPresenter.joinGroupByUuid(getBaseBuilder().add("uuid", uuid).build(), AppHttpPath.JOIN_GROUP_BY_UUID);
                }
            }
        } else {
            startActivity(new Intent(mContext, BaseWebViewActivity.class).putExtra("url", result));
        }
    }

}

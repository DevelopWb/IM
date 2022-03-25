package com.juntai.wisdom.im.contact;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.contact.group.GroupListActivity;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.base.BaseAppFragment;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.contact.newfriends.NewFriendsApplyActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  通讯录
 * @date 2021/4/18 14:59
 */
public class ContactFragment extends BaseAppFragment<MainPresent> implements MainContract.IBaseView,
        View.OnClickListener {

    private RecyclerView recyclerView;
    private IndexBar mIndexBar;
    private TextView mTvSideBarHint;
    private static final String INDEX_STRING_TOP = "↑";
    private ContactAdapter adapter;
    private LinearLayoutManager manager;

    @Override
    protected int getLayoutRes() {
        return R.layout.contact_layout;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) getView(R.id.rv);
        mIndexBar = (IndexBar) getView(R.id.indexBar);
        mTvSideBarHint = (TextView) getView(R.id.tvSideBarHint);
        adapter = new ContactAdapter(null);
        manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
//                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(manager);//设置RecyclerView的LayoutManager

    }


    @Override
    protected void initData() {
        List<ContactBean> data = Hawk.get(HawkProperty.getContactListKey());
        if (data != null) {
            initContactData(data);
        }
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_CONTACT:
                        ContactBean contactBean = (ContactBean) multipleItem.getObject();
                        String name = contactBean.getRemarksNickname();
                        switch (name) {
                            case MainContract.CONTACT_NEW_FRIEND:
                                //  新朋友申请
                                startActivityForResult(new Intent(mContext, NewFriendsApplyActivity.class), BaseActivity.BASE_REQUEST_RESULT);
                                break;
                            case MainContract.CONTACT_GROUP_CHAT:
                                // : 2021-11-05 群聊
                                startActivity(new Intent(mContext, GroupListActivity.class));
                                break;
                            default:
                                startActivity(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, contactBean.getId()));
                                break;
                        }

                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== BaseActivity.BASE_REQUEST_RESULT) {
            ((MainActivity)getActivity()).initNewFriendRedPoint(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        List<ContactBean> data = Hawk.get(HawkProperty.getContactListKey());
        if (data != null) {
            initContactData(data);
        }

    }

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }


    @Override
    public void onSuccess(String tag, Object o) {
    }

    /**
     * 初始化联系人数据
     *
     * @param data
     */
    public void initContactData(List<ContactBean> data) {
        HashMap<Integer, String> remarkMap = HawkProperty.getGlobleMap();
        List<MultipleItem> adapterList = new ArrayList<>();
        List<ContactBean> mDatas = new ArrayList<>();
        //微信的头部 也是可以右侧IndexBar导航索引的，
        // 但是它不需要被ItemDecoration设一个标题titile
        ContactBean newFriend = (ContactBean) new ContactBean(MainContract.CONTACT_NEW_FRIEND)
                .setFriendApplyAmount(Hawk.get(HawkProperty.FRIEND_APPLY, 0))
                .setHasEndLine(true).setTopInContact(true).setBaseIndexTag(INDEX_STRING_TOP);
        ContactBean groupChat = (ContactBean) new ContactBean(MainContract.CONTACT_GROUP_CHAT)
                .setFriendApplyAmount(0).setTopInContact(true).setBaseIndexTag(INDEX_STRING_TOP);
        mDatas.add(newFriend);
        mDatas.add(groupChat);
        adapterList.add(new MultipleItem(MultipleItem.ITEM_CONTACT, newFriend));
        adapterList.add(new MultipleItem(MultipleItem.ITEM_CONTACT, groupChat));
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                ContactBean datum = data.get(i);
                if (!remarkMap.containsKey(String.valueOf(datum.getId()))) {
                    remarkMap.put(datum.getId(), datum.getRemarksNickname());
                }
                datum.setRemarksNickname(datum.getRemarksNickname());//设置名称
                if (datum.getStatusType() == 1) {
                    mDatas.add(datum);
                }

            }
        }
        if (mIndexBar != null&&mDatas!=null) {
            mIndexBar.setmSourceDatas(mDatas)//设置数据
                    .invalidate();
        }
        for (int i = 0; i < mDatas.size(); i++) {
            if (i > 0) {
                ContactBean bean = mDatas.get(i);
                //为了排除新的朋友
                if (i < mDatas.size() - 1) {
                    //如果当前和下一个不一样 就添加分割线
                    ContactBean nextBena = mDatas.get(i + 1);
                    if (null != bean.getSuspensionTag() && !bean.getSuspensionTag().equals(nextBena.getSuspensionTag())) {
                        if (i != 1) {
                            adapterList.add(new MultipleItem(MultipleItem.ITEM_CONTACT, bean));
                        }
                        adapterList.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, nextBena.getSuspensionTag()));
                        bean.setHasEndLine(false);
                    } else {
                        adapterList.add(new MultipleItem(MultipleItem.ITEM_CONTACT, bean));
                        bean.setHasEndLine(true);
                    }
                } else {
                    if (!MainContract.CONTACT_GROUP_CHAT.equals(bean.getRemarksNickname())) {
                        //最后一个
                        bean.setHasEndLine(true);
                        adapterList.add(new MultipleItem(MultipleItem.ITEM_CONTACT, bean));
                    }

                }
            }
        }
        if (adapter != null) {
            adapter.setNewData(adapterList);
        }
    }


    @Override
    public void onError(String tag, Object o) {
        ToastUtils.error(mContext, String.valueOf(o));
    }


    @Override
    public void onClick(View v) {
    }

}

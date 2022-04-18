package com.juntai.wisdom.im.chat_module.chat;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.base.BaseAppFragment;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.UserInfoVoListBean;
import com.juntai.wisdom.im.contact.ContactAdapter;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  选择群成员中被提醒的人  @
 * * @date 2021/4/18 14:59
 */
public class SelectGroupNoticePeopleFragment extends BaseAppFragment<MainPresent> implements MainContract.IBaseView,
        View.OnClickListener {

    private RecyclerView recyclerView;
    private IndexBar mIndexBar;
    private TextView mTvSideBarHint;
    private static final String INDEX_STRING_TOP = "↑";
    private ContactAdapter adapter;
    private LinearLayoutManager manager;
    private  OnContactClick onContactClick;
    private GroupDetailInfoBean groupBean;

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


    /**
     * 获取群信息
     */
    public void getGroupInfo(GroupDetailInfoBean groupDetailBean, OnContactClick onContactClick) {
        this.onContactClick = onContactClick;
        this.groupBean =groupDetailBean;
        List<UserInfoVoListBean> dataBeans = groupDetailBean.getUserInfoVoList();
        List<ContactBean> peoples = new ArrayList<>();
        HashMap<Integer, String> remarkMap = HawkProperty.getGlobleMap();

        for (UserInfoVoListBean dataBean : dataBeans) {
            ContactBean contactBean = new ContactBean();
            contactBean.setId(dataBean.getId());
            contactBean.setNickname(dataBean.getNickname());
            contactBean.setAccountNumber(dataBean.getAccountNumber());
            contactBean.setHeadPortrait(dataBean.getHeadPortrait());
            if (remarkMap.containsKey(contactBean.getId())) {
                contactBean.setRemarksNickname(remarkMap.get(contactBean.getId()));
            }else {
                contactBean.setRemarksNickname(dataBean.getGroupNickname());
            }
            //不能@自己
            if (UserInfoManager.getUserId()!=contactBean.getId()) {
                peoples.add(contactBean);
            }
        }
        initContactData(peoples);
    }



    @Override
    protected void initData() {

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_CONTACT:
                        ContactBean contactBean = (ContactBean) multipleItem.getObject();
                        if (onContactClick != null) {
                            onContactClick.contactClicked(contactBean);
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
//        if (requestCode== BaseActivity.BASE_REQUEST_RESULT) {
//            mPresenter.getFriendList(getBaseAppActivity().getBaseBuilder()
//                    .add("statusType", "1").build(), AppHttpPath.GET_FRIEND_LISTE);
//        }
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


    }

    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }



    /**
     * 初始化联系人数据
     *
     * @param mDatas
     */
    public void initContactData(List<ContactBean> mDatas) {
        List<MultipleItem> adapterList = new ArrayList<>();

        if (UserInfoManager.canAtAllPeople(groupBean)) {
            ContactBean groupChat = (ContactBean) new ContactBean(MainContract.CONTACT_GROUP_ALL_PEOPLE)
                    .setFriendApplyAmount(0).setTopInContact(true).setNickname(MainContract.CONTACT_GROUP_ALL_PEOPLE);
            adapterList.add(new MultipleItem(MultipleItem.ITEM_CONTACT, groupChat));
        }
        mIndexBar.setmSourceDatas(mDatas)//设置数据
                .invalidate();

        for (int i = 0; i < mDatas.size(); i++) {
            ContactBean bean = mDatas.get(i);
            if (i < mDatas.size() - 1) {
                //如果当前和下一个不一样 就添加分割线
                ContactBean nextBena = mDatas.get(i + 1);
                if (0 == i) {
                    adapterList.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, bean.getSuspensionTag()));
                }
                if (null != bean.getSuspensionTag() && !bean.getSuspensionTag().equals(nextBena.getSuspensionTag())) {
                    adapterList.add(new MultipleItem(MultipleItem.ITEM_CONTACT, bean));
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
        adapter.setNewData(adapterList);
    }


    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onError(String tag, Object o) {
        ToastUtils.error(mContext, String.valueOf(o));
    }


    @Override
    public void onClick(View v) {
    }


    public interface   OnContactClick{
        void   contactClicked(ContactBean contactBean);
    }
}

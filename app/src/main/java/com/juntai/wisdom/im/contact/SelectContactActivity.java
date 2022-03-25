package com.juntai.wisdom.im.contact;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.ContactListBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  选择联系人
 * @date 2021-12-07 9:11
 */
public class SelectContactActivity extends BaseAppActivity<MainPresent> implements MainContract.IBaseView {
    private RecyclerView recyclerView;
    private IndexBar mIndexBar;
    private TextView mTvSideBarHint;
    private static final String INDEX_STRING_TOP = "↑";
    private ContactAdapter adapter;
    private LinearLayoutManager manager;
    public static final int REQUEST_CONTACT = 10087;//请求的回执


    @Override
    protected MainPresent createPresenter() {
        return new MainPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.contact_layout;
    }

    @Override
    public void initView() {
        setTitleName("选择联系人");
        mPresenter.getContactList(getBaseBuilder().build(), AppHttpPath.GET_CONTACT_LISTE);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
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
    public void initData() {
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
                        Intent intent = new Intent();
                        intent.putExtra(BASE_PARCELABLE, contactBean);
                        setResult(REQUEST_CONTACT, intent);
                        finish();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case AppHttpPath.GET_CONTACT_LISTE:
                ContactListBean contactListBean = (ContactListBean) o;
                if (contactListBean != null) {
                    List<ContactBean> data = contactListBean.getData();
                    if (data != null) {
                        List<ContactBean>  friendList = new ArrayList<>();
                        List<ContactBean>  friendWithoutBlockList = new ArrayList<>();
                        for (ContactBean datum : data) {
                            if (datum.getStatusType()!=3) {
                                friendList.add(datum);
                                if (datum.getStatusType()!=2) {
                                    friendWithoutBlockList.add(datum);
                                }
                            }
                        }
                        Hawk.put(HawkProperty.getContactListKey(),friendList);
                        initContactData(friendWithoutBlockList);

                    }

                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化联系人数据
     *
     * @param data
     */
    private void initContactData(List<ContactBean> data) {
        HashMap<Integer, String> remarkMap = HawkProperty.getGlobleMap();
        List<MultipleItem> adapterList = new ArrayList<>();
        List<ContactBean> mDatas = new ArrayList<>();
        //微信的头部 也是可以右侧IndexBar导航索引的，
        // 但是它不需要被ItemDecoration设一个标题titile
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                ContactBean datum = data.get(i);
                if (!remarkMap.containsKey(String.valueOf(datum.getId()))) {
                    remarkMap.put(datum.getId(), datum.getRemarksNickname());
                }
                datum.setRemarksNickname(datum.getRemarksNickname());//设置名称
                if (datum.getStatusType()==1) {
                    mDatas.add(datum);
                }


            }
        }
        mIndexBar.setmSourceDatas(mDatas)//设置数据
                .invalidate();
        for (int i = 0; i < mDatas.size(); i++) {
            ContactBean bean = mDatas.get(i);
            //为了排除新的朋友
            if (i < mDatas.size() - 1) {
                //如果当前和下一个不一样 就添加分割线
                ContactBean nextBena = mDatas.get(i + 1);
                if (0==i) {
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

}

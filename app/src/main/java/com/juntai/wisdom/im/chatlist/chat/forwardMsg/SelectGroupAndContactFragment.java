package com.juntai.wisdom.im.chatlist.chat.forwardMsg;


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
import com.juntai.wisdom.im.bean.PeopleBean;
import com.juntai.wisdom.im.contact.ContactAdapter;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.entrance.main.MainPresent;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  选择群成员
 * @date 2021/4/18 14:59
 */
public class SelectGroupAndContactFragment extends BaseAppFragment<MainPresent> implements MainContract.IBaseView,
        View.OnClickListener {

    private RecyclerView recyclerView;
    private IndexBar mIndexBar;
    private TextView mTvSideBarHint;
    private static final String INDEX_STRING_TOP = "↑";
    private ContactAdapter adapter;
    private LinearLayoutManager manager;
    private ArrayList<PeopleBean> groupPeoples;



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
        List<GroupDetailInfoBean> groups = Hawk.get(HawkProperty.GROUP_LIST);
        for (GroupDetailInfoBean group : groups) {
            adapter.addData(new MultipleItem(MultipleItem.ITEM_SELECT_GROUP,group));
        }
        List<ContactBean> data = Hawk.get(HawkProperty.getContactListKey());
        if (data != null) {
            initContactData(data);
        }

    }


    public void notifyContact(ContactBean selectingContactBean) {
        List<MultipleItem> arrays = adapter.getData();
        for (int i = 0; i < arrays.size(); i++) {
            MultipleItem array = arrays.get(i);
            switch (array.getItemType()) {
                case MultipleItem.ITEM_SELECT_CONTACT:
                    ContactBean contactBean = (ContactBean) array.getObject();
                    if (selectingContactBean.getId() == contactBean.getId()) {
                        if (1 == contactBean.getSelected()) {
                            ToastUtils.toast(mContext, "已经选过了");
                        } else {
                            contactBean.setSelected(1);
                            adapter.notifyItemChanged(i);
                        }

                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void notifyGroup(GroupDetailInfoBean selectGroupBean) {
        List<MultipleItem> arrays = adapter.getData();
        for (int i = 0; i < arrays.size(); i++) {
            MultipleItem array = arrays.get(i);
            switch (array.getItemType()) {
                case MultipleItem.ITEM_SELECT_GROUP:
                    GroupDetailInfoBean groupBean = (GroupDetailInfoBean) array.getObject();
                    if (selectGroupBean.getGroupId() == groupBean.getGroupId()) {
                        if (groupBean.isSelected()) {
                            ToastUtils.toast(mContext, "已经选过了");
                        }else {
                            groupBean.setSelected(true);
                            adapter.notifyItemChanged(i);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }



    @Override
    protected void initData() {

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_SELECT_CONTACT:
                        ContactBean contactBean = (ContactBean) multipleItem.getObject();
                        switch (contactBean.getSelected()) {
                            case 0:
                                contactBean.setSelected(1);
                                break;
                            case 1:
                                contactBean.setSelected(0);
                                break;
                            default:
                                break;
                        }
                        adapter.notifyItemChanged(position);
                        break;
                    case MultipleItem.ITEM_SELECT_GROUP:
                        GroupDetailInfoBean groupBean = (GroupDetailInfoBean) multipleItem.getObject();
                        if (groupBean.isSelected()) {
                            groupBean.setSelected(false);
                        }else {
                            groupBean.setSelected(true);
                        }
                        adapter.notifyItemChanged(position);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    /**
     * 选择选中的item
     *
     * @return
     */
    public List<MultipleItem> getAllSelectedItems() {
        List<MultipleItem> arrays = adapter.getData();
        List<MultipleItem> selectedItems = new ArrayList<>();
        for (MultipleItem array : arrays) {
            switch (array.getItemType()) {
                case MultipleItem.ITEM_SELECT_CONTACT:
                    ContactBean contactBean = (ContactBean) array.getObject();
                    if (1 == contactBean.getSelected()) {
                        selectedItems.add(array);
                    }
                    break;
                case MultipleItem.ITEM_SELECT_GROUP:
                    GroupDetailInfoBean groupBean = (GroupDetailInfoBean) array.getObject();
                    if (groupBean.isSelected()) {
                        selectedItems.add(array);
                    }
                    break;
                default:
                    break;
            }
        }
        return selectedItems;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


    @Override
    public void onSuccess(String tag, Object o) {
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

                if (datum.getStatusType() == 1) {
                    mDatas.add(datum);
                }
            }
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
                    adapterList.add(new MultipleItem(MultipleItem.ITEM_SELECT_CONTACT, bean));
                    adapterList.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, nextBena.getSuspensionTag()));
                    bean.setHasEndLine(false);
                } else {
                    adapterList.add(new MultipleItem(MultipleItem.ITEM_SELECT_CONTACT, bean));
                    bean.setHasEndLine(true);
                }
            } else {
                if (!MainContract.CONTACT_GROUP_CHAT.equals(bean.getRemarksNickname())) {
                    //最后一个
                    bean.setHasEndLine(true);
                    adapterList.add(new MultipleItem(MultipleItem.ITEM_SELECT_CONTACT, bean));
                }

            }
        }
        adapter.addData(adapterList);
    }


    @Override
    public void onError(String tag, Object o) {
        ToastUtils.error(mContext, String.valueOf(o));
    }


    @Override
    public void onClick(View v) {
    }

}

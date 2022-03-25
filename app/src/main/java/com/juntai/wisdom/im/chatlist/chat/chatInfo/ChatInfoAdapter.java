package com.juntai.wisdom.im.chatlist.chat.chatInfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.PeopleBean;
import com.juntai.wisdom.im.bean.TextKeyValueBean;
import com.juntai.wisdom.im.contact.ContactorInfoActivity;
import com.juntai.wisdom.im.chatlist.groupchat.joinGroup.SelectGroupChatPeopleActivity;
import com.juntai.wisdom.im.entrance.main.MainContract;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述
 * @date 2021-12-11 10:52
 */
public class ChatInfoAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {


    private OnChatInfoCallBack onChatInfoCallBack;
    private  int  joinGroupType = 0;

    private String groupUuid;


    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid == null ? "" : groupUuid;
    }

    public void setJoinGroupType(int joinGroupType) {
        this.joinGroupType = joinGroupType;
    }

    public void setOnChatInfoCallBack(OnChatInfoCallBack onChatInfoCallBack) {
        this.onChatInfoCallBack = onChatInfoCallBack;
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatInfoAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.ITEM_DIVIDER, R.layout.memu_divider);
        addItemType(MultipleItem.ITEM_RECYCLE, R.layout.item_recyclerview);
        addItemType(MultipleItem.ITEM_KEY_VALUE, R.layout.item_key_value2);
        addItemType(MultipleItem.ITEM_PIC_TEXT_MENUS, R.layout.pic_text_item);
        addItemType(MultipleItem.ITEM_KEY_VALUE_WITH_SWITCH, R.layout.item_key_switch);

    }


    @Override
    protected void convert(BaseViewHolder helper, MultipleItem multipleItem) {
        switch (multipleItem.getItemType()) {
            case MultipleItem.ITEM_DIVIDER:
                break;
            case MultipleItem.ITEM_RECYCLE:
                RecyclerView recyclerView = helper.getView(R.id.item_rv);
                PeopleAdapter peopleAdapter = new PeopleAdapter(R.layout.people_item);
                GridLayoutManager manager = new GridLayoutManager(mContext, 5);
                recyclerView.setAdapter(peopleAdapter);
                recyclerView.setLayoutManager(manager);
                List<PeopleBean> peopleBeans = (List<PeopleBean>) multipleItem.getObject();
                peopleAdapter.setNewData(peopleBeans);
                peopleAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        ArrayList<PeopleBean> arrays = (ArrayList<PeopleBean>) adapter.getData();
                        PeopleBean peopleBean = arrays.get(position);
                        switch (peopleBean.getType()) {
                            case 0:
                                // : 2021-12-11 跳转到用户详情
                                mContext.startActivity(new Intent(mContext, ContactorInfoActivity.class).putExtra(BaseActivity.BASE_ID, peopleBean.getUserId()));

                                break;
                            case 1:
                                // : 2021-12-11 发起群聊  跳转到选择成员的界面  添加
                                mContext.startActivity(new Intent(mContext, SelectGroupChatPeopleActivity.class)
                                        .putExtra(BaseActivity.BASE_ID,joinGroupType)
                                        .putExtra(BaseActivity.BASE_STRING,groupUuid)
                                        .putParcelableArrayListExtra(BaseActivity.BASE_PARCELABLE,arrays));
                                break;
                            case 2:
                                // : 2021-12-11 删除群成员   跳转到选择成员的界面  删除
                                mContext.startActivity(new Intent(mContext, SelectGroupChatPeopleActivity.class)
                                        .putExtra(BaseActivity.BASE_ID,2)
                                        .putExtra(BaseActivity.BASE_STRING,groupUuid)
                                        .putParcelableArrayListExtra(BaseActivity.BASE_PARCELABLE,arrays));
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            case MultipleItem.ITEM_KEY_VALUE:
                TextKeyValueBean keyValueBean = (TextKeyValueBean) multipleItem.getObject();
                helper.setText(R.id.item_key_tv, keyValueBean.getKey());
                TextView valueTv = helper.getView(R.id.item_value_tv);

                if (!TextUtils.isEmpty(keyValueBean.getValue())) {
                    helper.setText(R.id.item_value_tv, keyValueBean.getValue());
                } else {
                    if (MainContract.GROUP_QRCODE.equals(keyValueBean.getKey())) {
                        initViewLeftDrawable(valueTv, R.mipmap.qr_code_tag, 15, 15);
                    }
                    helper.setText(R.id.item_value_tv, "");
                }


                break;
            case MultipleItem.ITEM_KEY_VALUE_WITH_SWITCH:
                SwitchCompat switchCompat = helper.getView(R.id.item_switch_sc);
                TextKeyValueBean switKeyValueBean = (TextKeyValueBean) multipleItem.getObject();
                helper.setText(R.id.item_key_tv, switKeyValueBean.getKey());
                if (switKeyValueBean.isTurnOn()) {
                    switchCompat.setChecked(true);
                } else {
                    switchCompat.setChecked(false);
                }
                switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (onChatInfoCallBack != null) {
                            onChatInfoCallBack.onCheckedChanged(switKeyValueBean, buttonView, isChecked);
                        }

                    }
                });
                break;

            case MultipleItem.ITEM_PIC_TEXT_MENUS:
                helper.setGone(R.id.item_pic_iv, false);
                TextKeyValueBean textKeyValueBean = (TextKeyValueBean) multipleItem.getObject();
                helper.setText(R.id.item_name, textKeyValueBean.getKey());

                switch (textKeyValueBean.getKey()) {
                    case MultipleItem.DELETE:
                    case MultipleItem.DELETE_CHAT_RECORD:
                    case MultipleItem.DELETE_QUIT_GROUP:
                        helper.setTextColor(R.id.item_name, ContextCompat.getColor(mContext, R.color.red));
                        break;
                    default:
                        helper.setTextColor(R.id.item_name, ContextCompat.getColor(mContext, R.color.black));
                        break;
                }
                break;
            default:
                break;
        }


    }

    /**
     * 设置左边图标
     *
     * @param textView
     * @param drawableId
     */
    public void initViewLeftDrawable(TextView textView, int drawableId, int width, int height) {
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, DisplayUtil.dp2px(mContext, width), DisplayUtil.dp2px(mContext, height));//第一个 0 是距左边距离，第二个 0 是距上边距离，40 分别是长宽
        textView.setCompoundDrawables(null, null, drawable, null);//放左边
    }

    public interface OnChatInfoCallBack {
        void onCheckedChanged(TextKeyValueBean textKeyValueBean, CompoundButton buttonView, boolean isChecked);
    }

}

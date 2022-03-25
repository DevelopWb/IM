package com.juntai.wisdom.im.contact;

import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-24 16:38
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-24 16:38
 */
public class ContactAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ContactAdapter(List<MultipleItem> data) {
        super(data);
        addItemType(MultipleItem.ITEM_DIVIDER, R.layout.memu_divider);
        addItemType(MultipleItem.ITEM_CONTACT, R.layout.item_contact);
        addItemType(MultipleItem.ITEM_GROUP, R.layout.item_contact);
        addItemType(MultipleItem.ITEM_SELECT_CONTACT, R.layout.item_select_contact);
        addItemType(MultipleItem.ITEM_SELECT_GROUP, R.layout.item_select_contact);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {

        switch (item.getItemType()) {
            case MultipleItem.ITEM_CONTACT:
                ContactBean contactBean = (ContactBean) item.getObject();
                helper.setText(R.id.item_name, contactBean.getRemarksNickname());
                switch (contactBean.getRemarksNickname()) {
                    case MainContract.CONTACT_NEW_FRIEND:
                        ImageLoadUtil.loadSquareImage(mContext, R.mipmap.newfriends_icon, helper.getView(R.id.item_iv));
                        break;
                    case MainContract.CONTACT_GROUP_ALL_PEOPLE:
                    case MainContract.CONTACT_GROUP_CHAT:
                        ImageLoadUtil.loadSquareImage(mContext, R.mipmap.group_icon, helper.getView(R.id.item_iv));
                        break;
                    default:
                        ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(contactBean.getHeadPortrait()), helper.getView(R.id.item_iv));
                        break;
                }
                if (contactBean.isHasEndLine()) {
                    helper.setGone(R.id.item_divider_v, true);
                } else {
                    helper.setGone(R.id.item_divider_v, false);
                }
                int amount = contactBean.getFriendApplyAmount();
                helper.setGone(R.id.amount_tv, amount > 0);
                helper.setText(R.id.amount_tv, String.valueOf(amount));
                break;
            case MultipleItem.ITEM_GROUP:
                GroupListBean.DataBean  groupBean = (GroupListBean.DataBean) item.getObject();
                helper.setText(R.id.item_name, groupBean.getGroupName());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(groupBean.getGroupPicture()), helper.getView(R.id.item_iv));

                if (groupBean.isHasEndLine()) {
                    helper.setGone(R.id.item_divider_v, true);
                } else {
                    helper.setGone(R.id.item_divider_v, false);
                }
                helper.setGone(R.id.amount_tv, false);
                break;
            case MultipleItem.ITEM_SELECT_CONTACT:
                ContactBean selectContactBean = (ContactBean) item.getObject();
                helper.setText(R.id.item_name, selectContactBean.getRemarksNickname());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(selectContactBean.getHeadPortrait()), helper.getView(R.id.item_iv));
                if (selectContactBean.isHasEndLine()) {
                    helper.setGone(R.id.item_divider_v, true);
                } else {
                    helper.setGone(R.id.item_divider_v, false);
                }
                helper.setGone(R.id.amount_tv, false);
                if (1 == selectContactBean.getSelected()) {
                    helper.setImageResource(R.id.select_status_iv, R.mipmap.select_icon);
                } else if (0 == selectContactBean.getSelected()) {
                    helper.setImageResource(R.id.select_status_iv, R.mipmap.unselect_icon);
                } else {
                    helper.setImageResource(R.id.select_status_iv, R.mipmap.select_icon_light);

                }

                break;
            case MultipleItem.ITEM_SELECT_GROUP:
                GroupListBean.DataBean  selectGroupBean = (GroupListBean.DataBean) item.getObject();
                helper.setText(R.id.item_name, selectGroupBean.getGroupName());
                ImageLoadUtil.loadSquareImage(mContext, UrlFormatUtil.getImageThumUrl(selectGroupBean.getGroupPicture()), helper.getView(R.id.item_iv));
                if (selectGroupBean.isHasEndLine()) {
                    helper.setGone(R.id.item_divider_v, true);
                } else {
                    helper.setGone(R.id.item_divider_v, false);
                }
                helper.setGone(R.id.amount_tv, false);
                if (selectGroupBean.isSelected()) {
                    helper.setImageResource(R.id.select_status_iv, R.mipmap.select_icon);
                } else {
                    helper.setImageResource(R.id.select_status_iv, R.mipmap.unselect_icon);
                }

                break;
            case MultipleItem.ITEM_DIVIDER:
                String tag = (String) item.getObject();
                TextView textView = helper.getView(R.id.menu_divider_v);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                params.height = DisplayUtil.dp2px(mContext, 25);
                textView.setLayoutParams(params);
                helper.setText(R.id.menu_divider_v, TextUtils.isEmpty(tag) ? "" : tag);
                break;
            default:
                break;
        }


    }
}

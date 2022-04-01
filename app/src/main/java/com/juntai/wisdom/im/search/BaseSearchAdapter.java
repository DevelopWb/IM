package com.juntai.wisdom.im.search;

import android.widget.Filter;
import android.widget.Filterable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022/4/1 11:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/4/1 11:27
 */
public  abstract class BaseSearchAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> implements Filterable {
    List<T> datas ;
    private ArrayFilter mFilter;
   // 聊天类型（1：搜索私聊信息；2搜索公聊信息； 3搜索联系人  4首页搜索  搜索各种类型数据  5消息转发的时候跳转的界面  里面包含群组和通讯录  6 收藏的内容
    private int adapterType;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BaseSearchAdapter(List<T> data) {
        super(data);
        this.datas = data;
        this.adapterType = getAdapterType();
    }

    protected abstract int getAdapterType();


    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }


    /**
     * 数据过滤
     */
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {//constraint用户输入关键词
            FilterResults results = new  FilterResults();
            List<Object> newData = new ArrayList<>();
            if (constraint == null || constraint.toString().trim().length() == 0) {
            } else {
                String prefixString = constraint.toString().toLowerCase();


                switch (adapterType) {
                    case 4:

                        for (T t : datas) {
                            MultipleItem item = (MultipleItem) t;
                            switch (item.getItemType()) {
                                case MultipleItem.ITEM_CONTACT:
                                    ContactBean contactBean = (ContactBean) item.getObject();
                                    if (contactBean.getRemarksNickname().startsWith(prefixString)) {
                                        newData.add(item);
                                    }
                                    break;
                                case MultipleItem.ITEM_GROUP:
                                    GroupListBean.DataBean  groupBean = (GroupListBean.DataBean) item.getObject();
                                    if (groupBean.getGroupName().startsWith(prefixString)) {
                                        newData.add(item);
                                    }
                                    break;
                                case MultipleItem.ITEM_COLLECTION_TEXT:
                                case MultipleItem.ITEM_COLLECTION_PIC:
                                case MultipleItem.ITEM_COLLECTION_VIDEO:
                                case MultipleItem.ITEM_COLLECTION_AUDIO:
                                    if (getMessageBodyBean(item).getContent().startsWith(prefixString)) {
                                        newData.add(item);
                                    }
                                    break;
                                case MultipleItem.ITEM_COLLECTION_FILE:
                                    if (getMessageBodyBean(item).getFileName().startsWith(prefixString)) {
                                        newData.add(item);
                                    }

                                    break;
                                case MultipleItem.ITEM_COLLECTION_LOCATE:
                                    if (getMessageBodyBean(item).getAddrDes().startsWith(prefixString)) {
                                        newData.add(item);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    default:
                        break;
                }


            }
            results.values = newData;
            results.count = newData.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            datas = (ArrayList)results.values;
            if (datas == null || datas.size() ==0){
                ToastUtils.warning(mContext, "暂无相关部门！");
            }
            notifyDataSetChanged();
        }

        //给输入框返回的选择结果
        @Override
        public CharSequence convertResultToString(Object resultValue) {
//            PoliceBranchBean.DataBean testBean = (PoliceBranchBean.DataBean) resultValue;
//            return testBean.getName();
            return "";
        }
        private MessageBodyBean getMessageBodyBean(MultipleItem item) {
            MessageBodyBean messageBodyBean = (MessageBodyBean) item.getObject();
            return messageBodyBean;
        }
    }
}

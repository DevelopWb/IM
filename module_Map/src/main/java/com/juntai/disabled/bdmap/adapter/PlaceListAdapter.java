package com.juntai.disabled.bdmap.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.juntai.disabled.bdmap.R;
import com.juntai.disabled.bdmap.bean.AddressBean;

/**
 * 融云 - 位置定位选择
 * @aouther Ma
 * @date 2019/3/17
 */
public class PlaceListAdapter extends BaseQuickAdapter<AddressBean, BaseViewHolder> {


    public PlaceListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBean item) {
            helper.setText(R.id.mapaddress,item.getPoiInfo().getAddress());
            helper.setText(R.id.mapname,item.getPoiInfo().getName());

        //根据重新加载的时候第position条item是否是当前所选择的，选择加载不同的图片
        if(item.ischecked()){
            helper.setTextColor(R.id.mapaddress, ContextCompat.getColor(mContext,R.color.colorAccent));
            helper.setTextColor(R.id.mapname, ContextCompat.getColor(mContext,R.color.colorAccent));
        }
        else {
            helper.setTextColor(R.id.mapaddress,ContextCompat.getColor(mContext,R.color.text_title));
            helper.setTextColor(R.id.mapname,ContextCompat.getColor(mContext,R.color.text_title));
        }
    }
}
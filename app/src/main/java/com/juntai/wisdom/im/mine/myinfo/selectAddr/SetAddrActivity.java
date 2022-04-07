package com.juntai.wisdom.im.mine.myinfo.selectAddr;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.baidu.location.BDLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.CitysBean;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.TextKeyValueBean;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.mine.MyCenterContract;
import com.juntai.wisdom.im.mine.MyCenterPresent;
import com.juntai.wisdom.im.mine.myinfo.MyInformationActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.orhanobut.hawk.Hawk;

import java.util.List;

/**
 * @aouther tobato
 * @description 描述  设置地址
 * @date 2021-11-13 15:45
 */
public class SetAddrActivity extends BaseRecyclerviewActivity<MyCenterPresent> implements MyCenterContract.ICenterView {


    public static final String CHINA_TAG = "中国大陆";
    private String addr;


    @Override
    protected MyCenterPresent createPresenter() {
        return new MyCenterPresent();
    }

    @Override
    public void initData() {
        super.initData();
        setTitleName("选择地区");
        String presentAdCode = getIntent().getStringExtra(BASE_STRING);
        mPresenter.getAllCitys(presentAdCode, AppHttpPath.ALL_CITYS);
        baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_DIVIDER, "当前位置"));
        baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_TEXT_VALUE, new TextKeyValueBean(CHINA_TAG, "正在获取当前位置")));
        baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_DIVIDER, "全部地区"));
        mSmartrefreshlayout.setEnableRefresh(false);
        mSmartrefreshlayout.setEnableLoadMore(false);
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem item = (MultipleItem) adapter.getData().get(position);
                switch (item.getItemType()) {
                    case MultipleItem.ITEM_TEXT_VALUE:
                        TextKeyValueBean textKeyValueBean = (TextKeyValueBean) item.getObject();
                        addr = textKeyValueBean.getValue();
                        switch (textKeyValueBean.getKey()) {
                            case CHINA_TAG:
                                if ("正在获取当前位置".equals(addr)) {
                                    // : 2021-11-14 设置位置

                                }else {
                                    mPresenter.modifyUserInfo(getBaseBuilder().add("address", addr).build(), AppHttpPath.MODIFY_USER_ACCOUNT);
                                }
                                break;
                            default:
                                String adCode = textKeyValueBean.getAdcode();
                                String level = textKeyValueBean.getLevel();
                                addr = textKeyValueBean.getKey();
                                if ("city".equals(level)) {
                                    //确定位置 调用设置位置的接口
                                    mPresenter.modifyUserInfo(getBaseBuilder().add("address", textKeyValueBean.getKey()).build(), AppHttpPath.MODIFY_USER_ACCOUNT);

                                } else {
                                    startActivity(new Intent(mContext, SetAddrActivity.class).putExtra(BASE_STRING, adCode));

                                }


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
    public boolean requestLocation() {
        return true;
    }

    @Override
    public void onLocationReceived(BDLocation bdLocation) {
        if (bdLocation != null) {
            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            MultipleItem item = (MultipleItem) baseQuickAdapter.getData().get(1);
            TextKeyValueBean textKeyValueBean = (TextKeyValueBean) item.getObject();
            textKeyValueBean.setValue(province + " " + city);
            baseQuickAdapter.notifyItemChanged(1);
        }

    }


    @Override
    protected LinearLayoutManager getBaseAdapterManager() {
        return null;
    }

    @Override
    protected void getRvAdapterData() {

    }

    @Override
    protected boolean enableRefresh() {
        return false;
    }

    @Override
    protected boolean enableLoadMore() {
        return false;
    }

    @Override
    protected BaseQuickAdapter getBaseQuickAdapter() {
        return new AddrAdapter(null);
    }


    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);

        switch (tag) {
            case AppHttpPath.ALL_CITYS:
                CitysBean citysBean = (CitysBean) o;
                if (citysBean != null) {
                    CitysBean.DistrictsBean presentCity = citysBean.getDistricts().get(0);
                    List<CitysBean.DistrictsBean> citys = presentCity.getDistricts();
                    for (CitysBean.DistrictsBean city : citys) {
                        baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_TEXT_VALUE, new TextKeyValueBean(city.getName(), "", city.getAdcode(), city.getLevel())));
                    }
                }

                break;

            case AppHttpPath.MODIFY_USER_ACCOUNT:
                UserBean userBean = UserInfoManager.getUser();
                ContactBean contactBean = userBean.getData();
                contactBean.setAddress(addr);
                Hawk.put(HawkProperty.SP_KEY_USER, userBean);
                startActivity(new Intent(mContext, MyInformationActivity.class));
                break;
            default:
                break;
        }
    }

}

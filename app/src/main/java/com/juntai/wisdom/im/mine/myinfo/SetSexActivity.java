package com.juntai.wisdom.im.mine.myinfo;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.mine.MyCenterContract;
import com.juntai.wisdom.im.mine.MyCenterPresent;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.orhanobut.hawk.Hawk;

/**
 * @aouther tobato
 * @description 描述  修改性别
 * @date 2021-11-13 15:43
 */
public class SetSexActivity extends BaseAppActivity<MyCenterPresent> implements MyCenterContract.ICenterView {

    /**
     * 男
     */
    private RadioButton mSexBoyRb;
    /**
     * 女
     */
    private RadioButton mSexGirlRb;
    private RadioGroup mSexRg;

    @Override
    protected MyCenterPresent createPresenter() {
        return new MyCenterPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_modify_sex;
    }

    @Override
    public void initView() {
        setTitleName("设置性别");
        getTitleRightTv().setText("保存");
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  设置性别
                mPresenter.modifyUserInfo(getBaseBuilder().add("gender", String.valueOf(mSexBoyRb.isChecked()?1:2)).build(), AppHttpPath.MODIFY_USER_ACCOUNT);

            }
        });
        mSexBoyRb = (RadioButton) findViewById(R.id.sex_boy_rb);
        mSexGirlRb = (RadioButton) findViewById(R.id.sex_girl_rb);
        mSexRg = (RadioGroup) findViewById(R.id.sex_rg);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {
        UserBean userBean = UserInfoManager.getUser();
        ContactBean contactBean = userBean.getData();
        contactBean.setGender(mSexBoyRb.isChecked()?1:2);
        userBean.setData(contactBean);
        Hawk.put(HawkProperty.SP_KEY_USER,userBean);
        ToastUtils.toast(mContext,"修改成功");
        finish();
    }

}

package com.juntai.wisdom.im.mine.myinfo;

import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.orhanobut.hawk.Hawk;

/**
 * @aouther tobato
 * @description 描述  修改昵称
 * @date 2021-11-13 15:37
 */
public class ModifyNickNameActivity extends BaseModifyActivity {


    @Override
    protected String getWarnContent() {
        return getString(R.string.warn_mofify_name);
    }

    @Override
    protected String getTitleName() {
        return "更改名字";
    }

    @Override
    protected void commit(String textViewValue) {



// 修改昵称
        mPresenter.modifyUserInfo(getBaseBuilder().add("nickname", textViewValue).build(), AppHttpPath.MODIFY_USER_ACCOUNT);


    }

    @Override
    public void onSuccess(String tag, Object o) {
        UserBean userBean = UserInfoManager.getUser();
        ContactBean contactBean = userBean.getData();
        contactBean.setNickname(getTextViewValue(mNickNameEt));
        userBean.setData(contactBean);
        Hawk.put(HawkProperty.SP_KEY_USER,userBean);
        finish();
    }
}

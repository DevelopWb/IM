package com.juntai.wisdom.im.contact;

import android.os.Bundle;

import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.mine.myinfo.BaseModifyActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  修改备注名
 * @date 2021-12-10 9:13
 */
public class ModifyRemarkNameActivity extends BaseModifyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getWarnContent() {
        return "好备注可以更容易区分自己的朋友";
    }


    @Override
    protected String getTitleName() {
        return "修改备注";
    }

    @Override
    protected void commit(String textViewValue) {

        mPresenter.modifyFriendRemark(getBaseBuilder()
                .add("remarksNickname", textViewValue)
                .add("toUserId", String.valueOf(baseId)).build(), AppHttpPath.MODIFY_FRIEND_REMARK);
    }


    @Override
    public void onSuccess(String tag, Object o) {
        HashMap<Integer, String> remarkMap = HawkProperty.getGlobleMap();
        remarkMap.replace(baseId, getTextViewValue(mNickNameEt));
        ToastUtils.toast(mContext, "修改成功");

        List<ContactBean> data = Hawk.get(HawkProperty.getContactListKey());
        for (ContactBean datum : data) {
            if (datum.getId()==baseId) {
                datum.setRemarksNickname(getTextViewValue(mNickNameEt));
            }
        }
        Hawk.put(HawkProperty.getContactListKey(),data);
        finish();
    }
}

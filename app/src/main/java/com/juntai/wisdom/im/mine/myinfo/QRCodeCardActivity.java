package com.juntai.wisdom.im.mine.myinfo;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.mine.MyCenterContract;
import com.juntai.wisdom.im.mine.MyCenterPresent;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;

/**
 * @aouther tobato
 * @description 描述  二维码名片
 * @date 2021-11-13 15:39
 */
public class QRCodeCardActivity extends BaseAppActivity<MyCenterPresent> implements MyCenterContract.ICenterView {

    private ImageView mContactorIconIv;
    /**
     * 昵称
     */
    private TextView mContactorNameTv;
    private ImageView mSexTagIv;
    /**
     * 账号：
     */
    private TextView mContactorAddrTv;
    private ImageView mQRCodeIv;
    private String qrcodeUrl;

    @Override
    protected MyCenterPresent createPresenter() {
        return new MyCenterPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_q_r_code;
    }

    @Override
    public void initView() {
        setTitleName("二维码名片");
        mContactorIconIv = (ImageView) findViewById(R.id.contactor_icon_iv);
        mContactorNameTv = (TextView) findViewById(R.id.contactor_name_tv);
        mContactorAddrTv = (TextView) findViewById(R.id.contactor_addr_tv);
        mSexTagIv = (ImageView) findViewById(R.id.sex_tag_iv);
        mSexTagIv.setImageResource(UserInfoManager.getSexId() == 2 ? R.mipmap.sex_tag_girl : R.mipmap.sex_tag_boy);
        mQRCodeIv = (ImageView) findViewById(R.id.qrcode_iv);
        ImageLoadUtil.loadImage(mContext, UrlFormatUtil.getImageOriginalUrl(UserInfoManager.getHeadPic()), mContactorIconIv);
        mContactorNameTv.setText(UserInfoManager.getUserNickName());
        mContactorAddrTv.setText(UserInfoManager.getAddr());
        mQRCodeIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initBottomDialog(getBaseBottomDialogMenus("保存图片"),UrlFormatUtil.getImageOriginalUrl(qrcodeUrl));
                return true;
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.getQRCode(getBaseBuilder().build(), AppHttpPath.GET_QRCODE);
    }


    @Override
    public void onSuccess(String tag, Object o) {
        BaseResult baseResult = (BaseResult) o;
        qrcodeUrl = baseResult.getMessage();
        if (!TextUtils.isEmpty(qrcodeUrl)) {
            ImageLoadUtil.loadImage(mContext, UrlFormatUtil.getImageOriginalUrl(qrcodeUrl), mQRCodeIv);
        }

    }


}

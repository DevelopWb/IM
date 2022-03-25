package com.juntai.wisdom.im.mine.myinfo;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.base.selectPics.HeadCropActivity;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.TextKeyValueBean;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.mine.MyCenterContract;
import com.juntai.wisdom.im.mine.MyCenterPresent;
import com.juntai.wisdom.im.mine.myinfo.selectAddr.SetAddrActivity;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  我的信息
 * @date 2021/6/1 16:36
 */
public class MyInformationActivity extends BaseRecyclerviewActivity<MyCenterPresent> implements MyCenterContract.ICenterView {

    private final String MY_INFO_NICK_NAME = "昵称";
    private final String MY_INFO_ACCOUNT = "超视距号";
    public static final String MY_INFO_QR_CODE = "二维码名片";
    private final String MY_INFO_SEX = "性别";
    private final String MY_INFO_AREA = "地区";
    private ImageView imageView;
    private TextView nicknameTv;

    @Override
    protected MyCenterPresent createPresenter() {
        return new MyCenterPresent();
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
        return new MyInfoAdapter(R.layout.item_key_value);
    }

    /**
     * 添加头部
     */
    public View getHeadView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.include_myinfo_head, null);
        LinearLayout headLayout = view.findViewById(R.id.myinfo_headLayout);
        imageView = view.findViewById(R.id.myinfo_headimage);
        nicknameTv = view.findViewById(R.id.myinfo_nickname);
        headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseImage(0, MyInformationActivity.this, 1);
            }
        });
        return view;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initAdapterData();
    }

    @Override
    protected void selectedPicsAndEmpressed(List<String> icons) {
        super.selectedPicsAndEmpressed(icons);
        if (icons.size() > 0) {
            String picPath = icons.get(0);
            //跳转到裁剪头像的界面
            startActivityForResult(new Intent(this, HeadCropActivity.class).putExtra(HeadCropActivity.HEAD_PIC,
                    picPath), BASE_REQUEST_RESULT);

        }
    }



    @Override
    public void initData() {
        setTitleName("我的信息");
        baseQuickAdapter.setHeaderView(getHeadView());
        initAdapterData();

        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TextKeyValueBean textKeyValueBean = (TextKeyValueBean) adapter.getData().get(position);
                switch (textKeyValueBean.getKey()) {
                    case MY_INFO_NICK_NAME:
                        startActivityForResult(new Intent(mContext, ModifyNickNameActivity.class).putExtra(BaseModifyActivity.DEFAULT_HINT,UserInfoManager.getUserNickName()), BASE_REQUEST_RESULT);
                        break;
                    case MY_INFO_ACCOUNT:
                        startActivityForResult(new Intent(mContext, ModifyAccountActivity.class)
                                .putExtra(BaseModifyActivity.DEFAULT_HINT,UserInfoManager.getUserAccount()), BASE_REQUEST_RESULT);
                        break;
                    case MY_INFO_SEX:
                        startActivityForResult(new Intent(mContext, SetSexActivity.class), BASE_REQUEST_RESULT);
                        break;
                    case MY_INFO_QR_CODE:
                        startActivityForResult(new Intent(mContext, QRCodeCardActivity.class), BASE_REQUEST_RESULT);
                        break;
                    case MY_INFO_AREA:
                        startActivityForResult(new Intent(mContext, SetAddrActivity.class).putExtra(BASE_STRING,"100000"), BASE_REQUEST_RESULT);
                        break;
                    default:
                        break;
                }
            }
        });


    }

    private void initAdapterData() {
        UserBean bean = UserInfoManager.getUser();
        if (bean != null) {
            ContactBean userBean = bean.getData();
            List<TextKeyValueBean> beanList = new ArrayList<>();
            beanList.add(new TextKeyValueBean(MY_INFO_NICK_NAME, userBean.getNickname()));
            beanList.add(new TextKeyValueBean(MY_INFO_ACCOUNT, userBean.getAccountNumber()));
            beanList.add(new TextKeyValueBean(MY_INFO_QR_CODE, userBean.getAccountNumber()));
            beanList.add(new TextKeyValueBean(MY_INFO_SEX, userBean.getGender()==0?"":userBean.getGender()==1?"男":"女"));
            beanList.add(new TextKeyValueBean(MY_INFO_AREA, userBean.getAddress()));
            baseQuickAdapter.setNewData(beanList);
            ImageLoadUtil.loadCirImgNoCrash(getApplicationContext(), UrlFormatUtil.getImageThumUrl(userBean.getHeadPortrait()),
                    imageView,
                    R.mipmap.default_user_head_icon, R.mipmap.default_user_head_icon);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BASE_REQUEST_RESULT) {
            if (data != null) {
                String path = data.getStringExtra(HeadCropActivity.CROPED_HEAD_PIC);
                ImageLoadUtil.loadCirImgNoCrash(getApplicationContext(), path,
                        imageView,
                        R.mipmap.default_user_head_icon, R.mipmap.default_user_head_icon);
                //  调用上传图片的接口

               mPresenter.uploadFile(AppHttpPath.UPLOAD_FILES,path);
            }
        } else {
            initAdapterData();
        }
    }

    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);
        switch (tag) {
            case AppHttpPath.UPLOAD_FILES:
                List<String> paths = (List<String>) o;
                if (paths!=null&&!paths.isEmpty()) {
                    //调用修改头像的接口
                    mPresenter.modifyUserInfo(getBaseBuilder().add("headPortrait",paths.get(0)).build(),AppHttpPath.MODIFY_USER_INFO);
                }

                break;
            case AppHttpPath.MODIFY_USER_INFO:
                ToastUtils.toast(mContext, "更改成功");
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        setResult(BaseActivity.BASE_REQUEST_RESULT);
        finish();

    }
}

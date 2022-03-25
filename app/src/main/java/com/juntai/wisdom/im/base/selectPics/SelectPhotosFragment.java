package com.juntai.wisdom.im.base.selectPics;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.app.BaseApplication;
import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.disabled.basecomponent.utils.BaseAppUtils;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.GlideEngine4;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.bdmap.utils.DateUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.base.BaseAppFragment;
import com.juntai.wisdom.im.utils.StringTools;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.functions.Consumer;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;


/**
 * @aouther tobato
 * @description 描述  这是一个选择图片的fragment 图片的高和宽通过设置一行的个数 间距 magin等动态控制
 * @date 2020/3/17 14:41
 * <p>
 * 例子
 * FragmentManager fragmentManager = getSupportFragmentManager();
 * //开启事务
 * FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
 * beginTransaction.replace(R.id.id_card_positive_fl, SelectPhotosFragment.newInstance().setPhotoTitle("身份证正面照片")
 * .setPhotoSpace(45)
 * .setMaxCount(1));
 * beginTransaction.replace(R.id.id_card_obverse_fl, SelectPhotosFragment.newInstance().setPhotoTitle("身份证反面照片")
 * .setPhotoSpace(45)
 * .setMaxCount(1));
 * beginTransaction.replace(R.id.id_card_with_hand_fl, SelectPhotosFragment.newInstance().setPhotoTitle(getString(R
 * .string.id_card_notice))
 * .setPhotoSpace(45)
 * .setMaxCount(1));
 * //最后一步 记得commit
 * beginTransaction.commit();
 */
public class SelectPhotosFragment<T> extends BaseAppFragment implements View.OnClickListener {

    private RecyclerView mSelectPhotosRv;
    private TextView mSelectPhotosTitleTv;
    private List<String> icons = new ArrayList<>();
    private ShowSelectedPicsAdapter selectedPicsAdapter;
    private Context mContext;
    private int mSpanCount = 1;//一行的个数，默认4
    private int widthParama = 0;//
    private int mMaxCount = 9;//最大个数，默认9个
    private int horSpace = 15;//图片之间的横向间距 默认10
    private int marginLeftParents = 20;//图片距离左边父窗体的距离 dp
    private String title = null;//标题 默认为空
    private int SELECT_PIC_RESULT = 1000;
    private int TAKE_PICTURE = 1001;
    private int compressedSize = 0;//被压缩的图片个数

    private boolean deleteable = true;//可删除

    private OnPhotoItemClick onPhotoItemClick;
    private OnPicCalculateed onPicCalculateed;
    private int type;//0拍照照片，1拍照

    public String cameraPath;
    private GridLayoutManager manager;
    private OnPicLoadSuccessCallBack onPicLoadSuccessCallBack;


    private T  object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    /**
     * 在这里我们提供一个静态的方法来实例化PageFragment
     * 在这里我们传入一个参数，用来得到title，然后我们拿到这个title设置给内容
     *
     * @return
     */
    public static SelectPhotosFragment newInstance() {
        SelectPhotosFragment fragment = new SelectPhotosFragment();
        return fragment;
    }

    /**
     * @param spanCount 一行的个数，
     * @return
     */
    public SelectPhotosFragment setSpanCount(int spanCount) {
        this.mSpanCount = spanCount;
        if (manager != null) {
            manager.setSpanCount(spanCount);
        }
        return this;
    }

    /**
     * @param maxCount //最大个数，默认9个
     * @return
     */
    public SelectPhotosFragment setMaxCount(int maxCount) {
        this.mMaxCount = maxCount;
        return this;
    }
    /**
     * @param onPicLoadSuccessCallBack //选择图片  图片压缩后
     * @return
     */
    public SelectPhotosFragment setOnPicLoadSuccessCallBack(OnPicLoadSuccessCallBack onPicLoadSuccessCallBack) {
        this.onPicLoadSuccessCallBack = onPicLoadSuccessCallBack;
        return this;
    }

    /**
     * 获取最大个数
     *
     * @return
     */
    public int getmMaxCount() {
        return mMaxCount;
    }

    /**
     * @param photoSpace //图片距离左边父窗体的距离 dp  以距离左边父窗口和距离右边父窗口相同为标准
     * @return
     */
    public SelectPhotosFragment setPhotoSpace(int photoSpace) {
        this.marginLeftParents = photoSpace;
        return this;
    }

    /**
     * @title 设置标题
     */
    public SelectPhotosFragment setPhotoTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @title 设置图片删除状态
     */
    public SelectPhotosFragment setPhotoDelateable(boolean deleteable) {
        if (selectedPicsAdapter != null) {
            selectedPicsAdapter.setDelateable(deleteable);
        }
        this.deleteable = deleteable;
        return this;
    }

    public SelectPhotosFragment setType(int type) {
        this.type = type;
        return this;
    }

    /**
     * 配置图片测量完宽高后的回调
     *
     * @param onPicCalculateed
     * @return
     */
    public SelectPhotosFragment setPicCalculateCallBack(OnPicCalculateed onPicCalculateed) {
        this.onPicCalculateed = onPicCalculateed;
        return this;
    }


    /**
     * 清空数据
     */
    public void initContentAndIcons() {
        icons.clear();
        icons.add("-1");
        compressedSize = 0;
        selectedPicsAdapter.setNewData(icons);
    }

    /**
     * 填充图片
     *
     * @param arrays
     */
    public void setIcons(List<String> arrays) {
        icons.clear();
        selectedPicsAdapter.setNewData(icons);
        if (arrays != null && arrays.size() > 0) {
            //传入的图片数量大于等于最大值，截取最大值对应数
            if (arrays.size() >= mMaxCount) {
                for (int i = 0; i < mMaxCount; i++) {
                    icons.add(arrays.get(i));
                }
            } else {
                for (String array : arrays) {
                    icons.add(array);
                }
                icons.add("-1");
            }
            selectedPicsAdapter.setNewData(icons);
        }
    }

    /**
     * 获取图片路径
     *
     * @return
     */
    public List<String> getPhotosPath() {
        List<String> icons_new = new ArrayList<>();
        for (String icon : icons) {
            if (!"-1".equals(icon)) {
                icons_new.add(icon);
            }
        }
        return icons_new;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == SELECT_PIC_RESULT && resultCode == RESULT_OK) {
            imageCompress(Matisse.obtainPathResult(data));
        } else if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            imageCompress(cameraPath);
        }
    }

    /**
     * 图片压缩
     */
    private void imageCompress(List<String> paths) {
        compressedSize = 0;
        getBaseActivity().showLoadingDialog(getContext(),false);
        Luban.with(mContext).load(paths).ignoreBy(100).setTargetDir(FileCacheUtils.getAppImagePath(true)).filter(new CompressionPredicate() {
            @Override
            public boolean apply(String path) {
                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
            }
        }).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {
                //  压缩开始前调用，可以在方法内启动 loading UI

            }

            @Override
            public void onSuccess(File file) {
                compressedSize++;
                //  压缩成功后调用，返回压缩后的图片文件
                icons.add(file.getPath());
                selectedPicsAdapter.setNewData(reSortIconList());
                if (compressedSize == paths.size()) {
                    getBaseActivity().stopLoadingDialog();
                    if (onPicLoadSuccessCallBack != null) {
                        onPicLoadSuccessCallBack.loadSuccess(getSelectedPics(icons));
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                //  当压缩过程出现问题时调用
                compressedSize++;
                LogUtil.e("push-图片压缩失败");
                if (compressedSize == paths.size()) {
                    getBaseActivity().stopLoadingDialog();
                }

            }
        }).launch();
    }

    /**
     * 图片压缩
     */
    private void imageCompress(String path) {
        getBaseActivity().showLoadingDialog(getContext(),false);
        Luban.with(mContext).load(path).ignoreBy(100).setTargetDir(FileCacheUtils.getAppImagePath(true)).filter(new CompressionPredicate() {
            @Override
            public boolean apply(String path) {
                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
            }
        }).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {
                //  压缩开始前调用，可以在方法内启动 loading UI

            }

            @Override
            public void onSuccess(File file) {
                //  压缩成功后调用，返回压缩后的图片文件
                icons.add(file.getPath());
                selectedPicsAdapter.setNewData(reSortIconList());
                getBaseActivity().stopLoadingDialog();
                if (onPicLoadSuccessCallBack != null) {
                    onPicLoadSuccessCallBack.loadSuccess(getSelectedPics(icons));
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("push-图片压缩失败");
                getBaseActivity().stopLoadingDialog();
            }
        }).launch();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof SelectPhotosFragment.OnPhotoItemClick) {
            onPhotoItemClick = (SelectPhotosFragment.OnPhotoItemClick) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectPhotosFragment.OnPhotoItemClick");
        }
    }

    @Override
    protected boolean canCancelLoadingDialog() {
        return true;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.select_photo_layout;
    }

    @Override
    protected void initView() {
        mSelectPhotosRv = (RecyclerView) getView(R.id.select_photos_rv);

        mSelectPhotosTitleTv = (TextView) getView(R.id.select_photos_title_tv);
        selectedPicsAdapter = new ShowSelectedPicsAdapter(R.layout.show_selected_pic_item);
        selectedPicsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {


            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //                imageView = (ImageView) selectedPicsAdapter.getViewByPosition(mPublishNoticeRv,
                //                position, R.id.mine_sugguest_icon_iv);
                List<String> arrays = reSortIconList();
                String icon_path = arrays.get(position);
                switch (view.getId()) {
                    case R.id.select_pic_icon_iv:
                        if ("-1".equals(icon_path)) {
                            int count = mMaxCount - (icons.size() - 1);
                            choseImageFromFragment(type, SelectPhotosFragment.this, count, SELECT_PIC_RESULT);
                        } else {
                            if (icon_path.contains(".mp4")) {
                                //视频路径
                                if (onPhotoItemClick != null) {
                                    onPhotoItemClick.onVedioPicClick(adapter, position);
                                }
                            } else {
                                //图片路径
                                if (onPhotoItemClick != null) {
                                    onPhotoItemClick.onPicClick(adapter, position);
                                }
                            }
                        }
                        break;
                    case R.id.delete_pushed_news_iv:
                        arrays.remove(position);
                        icons.clear();
                        if (arrays.size() < mMaxCount) {
                            if (!arrays.contains("-1")) {
                                arrays.add("-1");
                            }
                        }
                        icons = arrays;
                        if (onPicLoadSuccessCallBack != null) {
                            onPicLoadSuccessCallBack.loadSuccess(getSelectedPics(icons));
                        }
                        adapter.setNewData(arrays);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        manager = new GridLayoutManager(mContext, mSpanCount);
        mSelectPhotosRv.setLayoutManager(manager);
        mSelectPhotosRv.setAdapter(selectedPicsAdapter);
        selectedPicsAdapter.setWidthAndHeigh(calculateImageHeight());
        selectedPicsAdapter.setDelateable(deleteable);
        if (StringTools.isStringValueOk(title)) {
            mSelectPhotosTitleTv.setVisibility(View.VISIBLE);
            mSelectPhotosTitleTv.setText(title);
        } else {
            mSelectPhotosTitleTv.setVisibility(View.GONE);
        }
        initContentAndIcons();
    }


    @Override
    protected IPresenter createPresenter() {
        return null;
    }


    /**
     * 对icons集合处理
     *
     * @return
     */
    private List<String> reSortIconList() {
        List<String> icons_new = new ArrayList<>();
        for (String icon : icons) {
            if (!"-1".equals(icon)) {
                icons_new.add(icon);
            }
        }
        if (icons.size() <= mMaxCount) {
            icons_new.add("-1");
        }
        return icons_new;
    }
    /**
     * 获取选中的照片
     *
     * @return
     */
    private List<String> getSelectedPics(List<String> pics) {
        List<String> icons_new = new ArrayList<>();
        for (String icon : pics) {
            if (!"-1".equals(icon)) {
                icons_new.add(icon);
            }
        }
        return icons_new;
    }

    /**
     * 计算图片的宽高
     */

    private int calculateImageHeight() {
        int width;
        //横向所有的间距
        int spaces = DisplayUtil.dp2px(mContext, horSpace) * (mSpanCount - 1);
        //左右间距
        int marginPresent = DisplayUtil.dp2px(mContext, marginLeftParents) * 2;
        width = DisplayUtil.px2dp(mContext, (BaseApplication.width - spaces - marginPresent) / mSpanCount);
        if (onPicCalculateed != null) {
            onPicCalculateed.picCalculateed(width);
        }
        return width;
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onError(String tag, Object o) {

    }

    /**
     * 图片选择
     *
     * @param type          选择类型 0 图片 1 短视频
     * @param fragment
     * @param maxSelectable 最大图片选择数
     * @param requestCode   请求得code
     */
    public void choseImageFromFragment(int type, Fragment fragment, int maxSelectable, int requestCode) {
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).compose(this.bindToLife()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    if (type == 0) {
                        Matisse.from(fragment).choose(MimeType.ofImage()).showSingleMediaType(true)
                                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                                .countable(true).maxSelectable(maxSelectable).capture(true).captureStrategy(new CaptureStrategy(true, BaseAppUtils.getFileprovider()))
                                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED).thumbnailScale(0.85f).imageEngine(new GlideEngine4()).forResult(requestCode);
                        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                    } else {
                        //打开照相机
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = getOutputMediaFileUri(mContext.getApplicationContext());
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //Android7.0添加临时权限标记，此步千万别忘了
                        openCameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                    }
                } else {
                    Toasty.info(mContext, "请给与相应权限").show();
                }
            }
        });
    }

    /**
     * 获取拍照存储URI
     *
     * @param context
     * @return
     */
    public Uri getOutputMediaFileUri(Context context) {
        File mediaFile = null;
        // 文件名
        String filename = DateUtil.getCurrentTime("yyyyMMdd_HHmmss") + ".png";
        // file对象，注意路径要和resource xml里配置的一样
        mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator, filename);
        cameraPath = mediaFile.getAbsolutePath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// sdk >= 24  android7.0以上
            Uri contentUri = FileProvider.getUriForFile(context, BaseAppUtils.getFileprovider(),//与清单文件中android
                    // :authorities的值保持一致
                    mediaFile);//FileProvider方式或者ContentProvider。也可使用VmPolicy方式
            return contentUri;
        } else {
            return Uri.fromFile(mediaFile);//或者 Uri.isPaise("file://"+file.toString()

        }
    }

    /**
     * 视频图片和普通图片的点击事件
     */
    public interface OnPhotoItemClick {
        void onVedioPicClick(BaseQuickAdapter adapter, int position);

        void onPicClick(BaseQuickAdapter adapter, int position);
    }

    /**
     * 测量好图片的宽高后的回调
     */
    public interface OnPicCalculateed {
        void picCalculateed(int width);
    }

    @Override
    public void onDetach() {
        onPhotoItemClick = null;
        onPicCalculateed = null;
        mContext = null;
        super.onDetach();
    }


    /**
     * 图片加载完成
     */
    public interface OnPicLoadSuccessCallBack {
        void loadSuccess(List<String> icons);

    }
}

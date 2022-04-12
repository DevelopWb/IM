package com.juntai.disabled.basecomponent.base;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.juntai.disabled.basecomponent.R;
import com.juntai.disabled.basecomponent.bean.BaseMenuBean;
import com.juntai.disabled.basecomponent.utils.ActivityManagerTool;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.basecomponent.utils.DividerItemDecoration;
import com.juntai.disabled.basecomponent.utils.EventManager;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.LoadingDialog;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.basecomponent.utils.PubUtil;
import com.juntai.disabled.basecomponent.utils.ScreenUtils;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;


public abstract class BaseActivity extends RxAppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private ImageView mRightIv;

    public abstract int getLayoutView();

    public abstract void initView();

    public abstract void initData();


    public Context mContext;
    public Toast toast;
    private Toolbar toolbar;
    protected CoordinatorLayout mBaseRootCol;
    private boolean title_menu_first = true;
    private TextView mBackTv;
    public ImmersionBar mImmersionBar;
    private TextView mTitleTv, titleRightTv;
    private boolean autoHideKeyboard = true;
    public FrameLayout frameLayout;
    public static int ActivityResult = 1001;//activity返回值
    public static final int BASE_REQUEST_RESULT = 10086;//请求的回执
    public static final int BASE_REQUEST_RESULT2 = 10087;//请求的回执
    public static String BASE_PARCELABLE = "parcelable";//请求的回执
    public static String BASE_ID = "baseId";//请求的回执
    public static String BASE_ID2 = "baseId2";//请求的回执
    public static String BASE_STRING = "baseString";//
    public   final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventManager.getEventBus().register(this);//注册
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 锁定竖屏
        mContext = this;
        mImmersionBar = ImmersionBar.with(this);
        initWidows();
        setContentView(R.layout.activity_base);
        frameLayout = findViewById(R.id.base_content);
        if (0 != getLayoutView()) {
            frameLayout.addView(View.inflate(this, getLayoutView(), null));
        }
        toolbar = findViewById(R.id.base_toolbar);
        mBaseRootCol = findViewById(R.id.base_col);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBackTv = findViewById(R.id.back_tv);
        mRightIv = findViewById(R.id.right_iv);
        mTitleTv = findViewById(R.id.title_name);
        titleRightTv = findViewById(R.id.title_rightTv);
        initToolbarAndStatusBar(true);
        initLeftBackTv(true);
        initView();
        initData();
        ActivityManagerTool.getInstance().addActivity(this);
    }

    /**
     * 警小宝 东关派出所版本 初始化toolbar和状态栏
     */
    protected void initToolbarAndStatusBar(boolean visible) {
        if (visible) {
            getToolbar().setVisibility(View.VISIBLE);
            getToolbar().setNavigationIcon(null);
            getToolbar().setBackgroundResource(R.drawable.sp_filled_gray_lighter);
            //状态栏配置
            mBaseRootCol.setFitsSystemWindows(true);
            mImmersionBar.statusBarColor(R.color.gray_light)
                    .statusBarDarkFont(true)
                    .init();
        }else{
            getToolbar().setVisibility(View.GONE);
            //状态栏配置
            mBaseRootCol.setFitsSystemWindows(false);
            mImmersionBar.reset().transparentStatusBar().init();
        }

    }

    /**
     * 初始化左侧按钮 默认不显示
     *
     * @param isShow 是否显示
     */
    protected void initLeftBackTv(boolean isShow) {
        if (isShow) {
            mBackTv.setVisibility(View.VISIBLE);
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.app_back);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mBackTv.setCompoundDrawables(drawable, null, null, null);
//            mBackTv.setText("返回");
            mBackTv.setCompoundDrawablePadding(-DisplayUtil.dp2px(this, 3));
            mBackTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            mBackTv.setVisibility(View.GONE);
        }

    }

    /**
     * 展示对话框
     * @param msg
     * @param positiveTitle
     * @param negativeTitle
     * @param positiveListener
     */
    public void showAlertDialog(String msg, String positiveTitle, String negativeTitle, DialogInterface.OnClickListener positiveListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(msg)
                .setPositiveButton(positiveTitle, positiveListener).setNegativeButton(negativeTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

        setAlertDialogHeightWidth(alertDialog,-1,0);
    }
    /**
     * 展示对话框
     * @param msg
     */
    public void showAlertDialogOfKnown(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(msg)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

        setAlertDialogHeightWidth(alertDialog,-1,0);
    }
    /**
     * 展示对话框
     * @param msg
     * @param positiveTitle
     * @param negativeTitle
     * @param positiveListener
     */
    public void showAlertDialog(String msg, String positiveTitle, String negativeTitle, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(msg)
                .setPositiveButton(positiveTitle, positiveListener).setNegativeButton(negativeTitle, negativeListener).show();

        setAlertDialogHeightWidth(alertDialog,-1,0);
    }


    /**
     * 设置alertdialog的宽高
     * 这个是为了类似锤子手机 对话框显示不全的问题
     * 需要在dialog  show()方法调用之后 调用此方法
     *
     * @param dialog
     * @param width  -1代表屏幕宽度  0 代表 wrap_content  其他就是自定义值了
     * @param height
     */
    public void setAlertDialogHeightWidth(AlertDialog dialog, int width, int height) {

//        shareMsgDialog.getWindow().setBackgroundDrawableResource(R.drawable.sp_filled_white_10dp);
//        shareMsgDialog.getWindow().setLayout(ScreenUtils.getInstance(mContext).getScreenWidth() - DisplayUtil.dp2px(mContext, 80), LinearLayout.LayoutParams.WRAP_CONTENT);
//
        // 设置dialog的宽度
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        if (-1 == width) {
            params.width = getScreenWidth();
        } else if (0 == width) {
            params.width = params.width;
        } else {
            params.width = width;
        }
        if (-1 == height) {
            params.height = getScreenHeight();
        } else if (0 == height) {
            params.height = params.height;
        } else {
            params.height = height;
        }
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 倍数
     * @param dialog
     */
    public static void setDialog(Dialog dialog,double mult) {
        Display display = dialog.getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int)(display.getWidth() * mult);
        dialog.getWindow().setAttributes(params);
    }
    /**
     * 获取屏幕宽度(px)
     *
     * @param
     * @return
     */
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        return width;
    }

    /**
     * 获取屏幕高度(px)
     *
     * @param
     * @return
     */
    public int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        return height;
    }
    public Toolbar getToolbar() {
        return toolbar;
    }

    public TextView getTitleRightTv() {
        titleRightTv.setVisibility(View.VISIBLE);
        return titleRightTv;
    }
    public ImageView getTitleRightIv() {
        mRightIv.setVisibility(View.VISIBLE);
        return mRightIv;
    }
    /**
     * 设置图标
     *
     * @param drawableId
     */
    public void setRightTvDrawable( int drawableId) {
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, DisplayUtil.dp2px(mContext, 20), DisplayUtil.dp2px(mContext, 20));//第一个 0 是距左边距离，第二个 0 是距上边距离，40 分别是长宽
        getTitleRightTv().setCompoundDrawables(drawable, null, null, null);//只放左边
    }
    /**
     * 展示加载动画
     */
    public void showLoadingDialog(Context context,boolean canCancel) {
        LoadingDialog.getInstance().showProgress(context,canCancel);
    }

    /**
     * 获取左控件
     *
     * @return
     */
    public TextView getTitleLeftTv() {
        mBackTv.setVisibility(View.VISIBLE);
        return mBackTv;
    }
    /**
     * 获取左控件
     *
     * @return
     */
    public TextView getTitleTv() {
        mTitleTv.setVisibility(View.VISIBLE);
        return mTitleTv;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 标题
     *
     * @param title
     */
    public void setTitleName(String title) {
        mTitleTv.setText(title);
        mTitleTv.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    /**
     * title右侧:图标类
     */
    private void setRightRes() {
        //扩展menu
        toolbar.inflateMenu(R.menu.toolbar_menu);
        //添加监听
        toolbar.setOnMenuItemClickListener(this);
    }

    /**
     * 显示右侧图标
     *
     * @param itemId
     */
    public void showTitleRes(int... itemId) {
        if (title_menu_first) {
            setRightRes();
            title_menu_first = false;
        }
        for (int item : itemId) {
            //显示
            toolbar.getMenu().findItem(item).setVisible(true);//通过id查找,也可以用setIcon()设置图标
            //            toolBar.getMenu().getItem(0).setVisible(true);//通过位置查找
        }
    }

    /**
     * 隐藏title图标
     *
     * @param itemId :图标对应的选项id
     */
    public void hindTitleRes(int... itemId) {
        //        if (titleBack != null)
        //            titleBack.setVisibility(View.GONE);
        for (int item : itemId) {
            //隐藏
            toolbar.getMenu().findItem(item).setVisible(false);
        }
    }

    /**
     * toolbar菜单监听---子activity直接onMenuItemClick()
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    /**
     * 清理webview
     *
     * @param webView
     */
    public void closeWebView(WebView webView) {
        if (webView != null) {
            //            ViewGroup parent = webView.getParent();
            //            if (parent != null) {
            //                parent.re(webView);
            //            }
            webView.removeAllViews();
            webView.destroy();
        }
    }
    //    @Override
    //    public void showLoadingFileDialog() {
    //        showFileDialog();
    //    }
    //
    //    @Override
    //    public void hideLoadingFileDialog() {
    //        hideFileDialog();
    //    }

    //    @Override
    //    public void onProgress(long totalSize, long downSize) {
    //        if (dialog != null) {
    //            dialog.setProgress((int) (downSize * 100 / totalSize));
    //        }
    //    }

    /**
     * 点击空白隐藏键盘
     *
     * @param event
     * @param view
     * @param activity
     */
    public static void hideKeyboard(MotionEvent event, View view,
                                    Activity activity) {
        try {
            if (view != null && view instanceof TextView) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏软键盘  view 可以是当前点击的view 没必要全是edittext
     */
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 点击空白自动隐藏键盘- - - 默认
     *
     * @param autoHideKeyboard:false - 不自动隐藏
     */
    public void setAutoHideKeyboard(boolean autoHideKeyboard) {
        this.autoHideKeyboard = autoHideKeyboard;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                if (autoHideKeyboard) {
                    hideKeyboard(ev, view, BaseActivity.this);//调用方法判断是否需要隐藏键盘
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        ToastUtils.info(mContext,"长按");
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        ActivityManagerTool.getInstance().removeActivity(this);
        EventManager.getEventBus().unregister(this);//注册
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
            mImmersionBar = null;
        }
        this.mContext = null;
        stopLoadingDialog();
    }

    /**
     * 停止加载动画
     */
    public void stopLoadingDialog() {
        LoadingDialog.getInstance().dismissProgress();
    }

    /**
     * 获取测试数据
     *
     * @return
     */
    public List<String> getTestData() {
        return Arrays.asList(new String[]{ "test2", "test3", "test4", "test5", "测试很测试很多数据的测试很多数据的多数据的XXXXXXXXXXXXX", "测试很测试很多数据的测试很多数据的多数据的"});
    }

    /**
     * 获取TextView的值
     *
     * @param textView
     * @return
     */
    public String getTextViewValue(TextView textView) {
        return textView.getText().toString().trim();
    }



    /**
     * 初始化窗口 在界面为初始化之前调用
     */
    protected void initWidows() {
        //设置屏幕适配 360为设计图尺寸px/2
        ScreenUtils screenUtils = ScreenUtils.getInstance(getApplicationContext());
        if (screenUtils.isPortrait()) {
            screenUtils.adaptScreen4VerticalSlide(this, 360);
        } else {
            screenUtils.adaptScreen4HorizontalSlide(this, 360);
        }

    }

    /**
     * 初始化recyclerview LinearLayoutManager
     */
    public void initRecyclerview(RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter, @RecyclerView.Orientation int orientation) {
        LinearLayoutManager managere = new LinearLayoutManager(this, orientation, false);
        //        baseQuickAdapter.setEmptyView(getAdapterEmptyView("一条信息也没有",0));
        recyclerView.setLayoutManager(managere);
        recyclerView.setAdapter(baseQuickAdapter);
    }
    /**
     * 初始化recyclerview LinearLayoutManager
     */
    public void initRecyclerviewNoScroll(RecyclerView recyclerView, BaseQuickAdapter baseQuickAdapter,
                                         @RecyclerView.Orientation int orientation) {
        LinearLayoutManager managere = new LinearLayoutManager(this, orientation, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        //        baseQuickAdapter.setEmptyView(getAdapterEmptyView("一条信息也没有",0));
        recyclerView.setLayoutManager(managere);
        recyclerView.setAdapter(baseQuickAdapter);
    }
    /**
     * 添加分割线
     *
     * @param recyclerView
     * @param haveTopLine
     * @param isHorizontalDivider 水平分割线
     * @param haveEndLine         最后一个item下是否划线
     */
    public void addDivider(boolean isHorizontalDivider, RecyclerView recyclerView, boolean haveTopLine, boolean haveEndLine) {
        DividerItemDecoration dividerItemDecoration;
        if (isHorizontalDivider) {
            dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, R.drawable.divider_hor_line_sp);
        } else {
            dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST);
        }
        if (haveTopLine) {
            if (haveEndLine) {
                dividerItemDecoration.setDividerMode(DividerItemDecoration.ALL);
            } else {
                dividerItemDecoration.setDividerMode(DividerItemDecoration.START);
            }
        } else {
            if (haveEndLine) {
                dividerItemDecoration.setDividerMode(DividerItemDecoration.END);
            } else {
                dividerItemDecoration.setDividerMode(DividerItemDecoration.INSIDE);

            }
        }
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    /**
     * 获取空布局
     *
     * @param text
     * @return
     */
    public View getAdapterEmptyView(String text, int imageId) {
        View view = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
        TextView noticeTv = view.findViewById(R.id.none_tv);
        noticeTv.setText(text);
        ImageView imageView = view.findViewById(R.id.none_image);
        if (-1==imageId) {
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setImageResource(imageId);
        }
        return view;
    }

    /**
     * 释放imageview内存资源
     *
     * @param imageView
     */
    public void recycleImageView(ImageView imageView) {
        Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bm.recycle();
        bm = null;
    }
    //单点登录
    public  void singleLogin(){}
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void receiveBaseStringMsg(String msg) {
        switch (msg) {
            case EventManager.SINGLE_LOGIN:
                //单点登录
                singleLogin();
                break;
            default:
                break;
        }
    }
    /**
     * 配置view的margin属性
     */
    public void setMargin(View view, int left, int top, int right, int bottom) {
        left = DisplayUtil.dp2px(this, left);
        top = DisplayUtil.dp2px(this, top);
        right = DisplayUtil.dp2px(this, right);
        bottom = DisplayUtil.dp2px(this, bottom);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(view.getLayoutParams());
        layoutParams.setMargins(left, top, right, bottom);
        view.setLayoutParams(layoutParams);
    }
    /**
     * 隐藏软键盘  view 可以是当前点击的view 没必要全是edittext
     */
    public void hideKeyboardFromView(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * view获取焦点
     */
    public  void getViewFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
    /**
     * 显示软键盘
     *
     * @param view
     */
    public void openKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

    }

    /**
     * 拨打电话
     */
    public void makeAPhoneCall(String telNum) {
        View view = getLayoutInflater().inflate(R.layout.call_layout, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();
        final TextView phone = view.findViewById(R.id.property_phone_no_tv);
        phone.setText(telNum);
        view.findViewById(R.id.call_property_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RxPermissions(BaseActivity.this)
                        .request(new String[]{
                                Manifest.permission.CALL_PHONE})
                        .delay(1, TimeUnit.SECONDS)
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {

                                } else {
                                    //有一个权限没通过
                                }
                                //所有权限通过
                                alertDialog.dismiss();
                                PubUtil.callPhone(BaseActivity.this, phone.getText().toString().trim());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                            }
                        });
            }
        });
        view.findViewById(R.id.cancel_call_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
    /**
     * 压缩图片
     * @param path  图片路径
     * @param saveDirName  保存本地图片的目录
     * @param onImageCompressedPath
     * @param saveFileName  保存文件的名称
     */
    public void  compressImage(String path, String saveDirName,
                               String saveFileName,OnImageCompressedPath onImageCompressedPath) {
        //        showLoadingDialog(mContext);
        Luban.with(mContext).load(path).ignoreBy(100)
                .setTargetDir(FileCacheUtils.getAppImagePath(saveDirName,false))
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                }).setRenameListener(new OnRenameListener() {
            @Override
            public String rename(String filePath) {
                return TextUtils.isEmpty(saveFileName)||saveFileName==null?System.currentTimeMillis()+".jpg":
                        saveFileName+".jpg";
            }
        })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        //  压缩开始前调用，可以在方法内启动 loading UI

                    }

                    @Override
                    public void onSuccess(File file) {
                        //  压缩成功后调用，返回压缩后的图片文件
                        if (onImageCompressedPath != null) {
                            onImageCompressedPath.compressedImagePath(file);
                        }
                        stopLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("push-图片压缩失败");
                        stopLoadingDialog();
                    }
                }).launch();
    }

    /**
     * 图片压缩成功回调
     */
    public interface OnImageCompressedPath {
        void  compressedImagePath(File file);
    }

    /**
     * 设置左边图标
     * @param textView
     * @param drawableId
     */
    public void initViewLeftDrawable(TextView textView, int drawableId, int width, int height) {
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, DisplayUtil.dp2px(this, width), DisplayUtil.dp2px(this, height));//第一个 0 是距左边距离，第二个 0 是距上边距离，40 分别是长宽
        textView.setCompoundDrawables(drawable, null, null, null);//放左边
    }

    /**
     * 设置顶部图标
     * @param textView
     * @param drawableId
     */
    public void initViewTopDrawable(TextView textView, int drawableId, int width, int height) {
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, DisplayUtil.dp2px(this, width), DisplayUtil.dp2px(this, height));//第一个 0 是距左边距离，第二个 0 是距上边距离，40 分别是长宽
        textView.setCompoundDrawables(null, drawable, null, null);//放顶部
    }

    /**
     * 设置右边图标
     *
     * @param textView
     * @param drawableId
     */
    public void initViewRightDrawable(TextView textView, int drawableId, int width, int height) {
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, DisplayUtil.dp2px(this, width), DisplayUtil.dp2px(this, height));//第一个 0 是距左边距离，第二个 0 是距上边距离，40 分别是长宽
        textView.setCompoundDrawables(null, null, drawable, null);//只放右边
    }


    /**
     * @return
     */
    public List<BaseMenuBean>  getBaseBottomDialogMenus(String... names) {
        List<BaseMenuBean> calls = new ArrayList<>();
        if (names.length==0) {
            return null;
        }
        for (String name : names) {
            calls.add(new BaseMenuBean(name));
        }
        return calls;
    }
    /**
     * @return
     */
    public List<BaseMenuBean>  getBaseBottomDialogMenus(BaseMenuBean... menus) {
        List<BaseMenuBean> calls = new ArrayList<>();
        if (menus.length==0) {
            return null;
        }
        for (BaseMenuBean menu : menus) {
            calls.add(menu);
        }
        return calls;
    }


    /**
     * 所有view可见or 不可见
     */
    public void  setViewVisibleOrGone(boolean visible,View... views){
        if (views != null&&views.length>0) {
            for (View view : views) {
                if (visible) {
                    view.setVisibility(View.VISIBLE);
                }else {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }



}

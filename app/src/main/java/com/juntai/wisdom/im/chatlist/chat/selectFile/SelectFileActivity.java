package com.juntai.wisdom.im.chatlist.chat.selectFile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.DisplayUtil;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseRecyclerviewActivity;
import com.juntai.wisdom.im.bean.FileResourceBean;
import com.juntai.wisdom.im.bean.FileSelectBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chatlist.chat.ChatPresent;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  选择文件
 * @date 2022-01-11 9:38
 */
public class SelectFileActivity extends BaseRecyclerviewActivity<ChatPresent> implements MainContract.IBaseView, View.OnClickListener {

    public static final int REQUEST_SELECT_FILES = 10089;//选择文件
    public static final String REQUEST_SELECT_FILES_STR = "selectFile";//选择文件
    public static final String REQUEST_SELECT_FILES_BEANS = "selectFilesize";//选择文件

    private String filePath;
    private RecyclerView recyclerView;

    private PopupWindow popupWindow;
    //0 聊天中的文件 1 收藏中的文件 2手机存储 3 手机相册
    private int fileResType = 0;
    //0 私聊 1群聊 2 密聊
    private  int chatType = 0;
    //联系人的ID 或者群组的ID
    private int  id ;



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
        return new SelectFileAdapter(null);
    }

    @Override
    protected ChatPresent createPresenter() {
        return new ChatPresent();
    }

    @Override
    protected boolean canCancelLoadingDialog() {
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            filePath = intent.getStringExtra(BASE_STRING);
            baseQuickAdapter.setNewData(null);
            initAdapterData(filePath);
        }
    }

    @Override
    public void initData() {
        super.initData();
        initFileResourcePop();
        if (getIntent() != null) {
            chatType = getIntent().getIntExtra(BASE_ID,0);
            id = getIntent().getIntExtra(BASE_ID2,0);
            initAdapterData(null);
        }
        setTitleName("聊天中的文件");
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.arrow_down_filled);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        getTitleTv().setCompoundDrawablePadding(DisplayUtil.dp2px(mContext,5));
        getTitleTv().setCompoundDrawables(null, null, drawable, null);
        getTitleRightTv().setText("发送");
        getTitleTv().setTextSize(16);
        getTitleTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null&&!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(v,0, DisplayUtil.dp2px(mContext,10));
                }else {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            }
        });
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // : 2022-01-11  选择文件之后发送
                List<String> files = getSelectedFiles();
                if (files == null) {
                    ToastUtils.toast(mContext, "不能选择大于500M的文件");

                    return;
                }
                if (files.isEmpty()) {
                    ToastUtils.toast(mContext, "请选择要发送的文件");
                    return;
                }
                mPresenter.uploadFile(files, AppHttpPath.UPLOAD_FILES);
            }
        });
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem item = (MultipleItem) adapter.getItem(position);
                FileSelectBean selectBean = (FileSelectBean) item.getObject();
                switch (item.getItemType()) {
                    case MultipleItem.ITEM_CHAT_FILE_DIR:
                        int childAmount = selectBean.getFileChilds();
                        if (0 == childAmount) {
                            return;
                        }
                        startActivity(new Intent(mContext, SelectFileActivity.class).putExtra(BASE_STRING, selectBean.getFilePath()));
                        break;

                    case MultipleItem.ITEM_CHAT_FILE_TEXT:
                        if (selectBean.isSelect()) {
                            selectBean.setSelect(false);
                        } else {
                            selectBean.setSelect(true);
                        }
                        adapter.notifyItemChanged(position);


                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 获取选择文件的路径
     *
     * @return
     */
    private List<String> getSelectedFiles() {
        List<String> filePaths = new ArrayList<>();
        List<MultipleItem> multipleItems = baseQuickAdapter.getData();
        for (MultipleItem multipleItem : multipleItems) {
            if (MultipleItem.ITEM_CHAT_FILE_TEXT == multipleItem.getItemType()) {
                FileSelectBean selectBean = (FileSelectBean) multipleItem.getObject();
                if (selectBean.isSelect()) {
                    if (FileCacheUtils.isOutInMaxLength(selectBean.getFileSize())) {
                        return null;
                    }
                    filePaths.add(selectBean.getFilePath());
                }
            }
        }
        return filePaths;

    }

    /**
     * 获取选择文件的路径
     *
     * @return
     */
    private ArrayList<FileSelectBean> getSelectedFileSizes() {
        ArrayList<FileSelectBean> fileSizes = new ArrayList<>();
        List<MultipleItem> multipleItems = baseQuickAdapter.getData();
        for (MultipleItem multipleItem : multipleItems) {
            if (MultipleItem.ITEM_CHAT_FILE_TEXT == multipleItem.getItemType()) {
                FileSelectBean selectBean = (FileSelectBean) multipleItem.getObject();
                if (selectBean.isSelect()) {
                    fileSizes.add(selectBean);
                }
            }
        }
        return fileSizes;

    }

    /**
     * 更改适配器内容
     * @param filePath
     */
    private void initAdapterData(String filePath) {
        switch (fileResType) {
            case 0:
                List<MessageBodyBean> arrays = null;
                //聊天中的文件
                if (0==chatType) {
                    //私聊中的文件
                    arrays = mPresenter.findPrivateChatFileRecordList(id);
                }else {
                    //群聊中的文件
                    arrays = mPresenter.findGroupChatFileRecord(id);
                }
                if (arrays != null&&!arrays.isEmpty()) {
                    for (MessageBodyBean messageBodyBean : arrays) {
                        if (UserInfoManager.getUserId()==messageBodyBean.getFromUserId()) {
                            //自己发送的
                            if (FileCacheUtils.isFileExists(messageBodyBean.getLocalCatchPath())) {
                                File file = new File(messageBodyBean.getLocalCatchPath());
                                try {
                                    baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE_TEXT, new FileSelectBean(file.getName(), file.getAbsolutePath(), false, FileCacheUtils.getFileSize(file), 0)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }else {
                            if (FileCacheUtils.isFileExists(FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean))) {
                                File file = new File(FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean));
                                try {
                                    baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE_TEXT, new FileSelectBean(file.getName(), file.getAbsolutePath(), false, FileCacheUtils.getFileSize(file), 0)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                    }
                }
                break;
            case 1:
                // : 2022/3/18 收藏中的文件
                List<MessageBodyBean> collects = null;
                collects = mPresenter.findCollectFileRecordList();
                if (collects != null&&!collects.isEmpty()) {
                    for (MessageBodyBean messageBodyBean : collects) {

                        if (UserInfoManager.getUserId()==messageBodyBean.getFromUserId()) {
                            //自己发送的
                            if (FileCacheUtils.isFileExists(messageBodyBean.getLocalCatchPath())) {
                                File file = new File(messageBodyBean.getLocalCatchPath());
                                try {
                                    baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE_TEXT, new FileSelectBean(file.getName(), file.getAbsolutePath(), false, FileCacheUtils.getFileSize(file), 0)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }else {
                            if (FileCacheUtils.isFileExists(FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean))) {
                                File file = new File(FileCacheUtils.getAppFilePath(false) + getSavedFileName(messageBodyBean));
                                try {
                                    baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE_TEXT, new FileSelectBean(file.getName(), file.getAbsolutePath(), false, FileCacheUtils.getFileSize(file), 0)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                    }
                }
                break;
            case 2:
                // : 2022/3/18 手机中的文件
                List<File> files = FileCacheUtils.getFiles(filePath);
                for (File file : files) {
                    if (file.isDirectory()) {
                        baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE_DIR, new FileSelectBean(file.getName(), file.getAbsolutePath(), false, 0, file.list().length)));
                    } else {
                        try {
                            baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE_TEXT, new FileSelectBean(file.getName(), file.getAbsolutePath(), false, FileCacheUtils.getFileSize(file), 0)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 3:
                // : 2022/3/18 相册中的文件
                List<File> photoFiles = FileCacheUtils.getFiles(filePath);
                for (File file : photoFiles) {
                    if (!file.isDirectory()) {
                        try {
                            baseQuickAdapter.addData(new MultipleItem(MultipleItem.ITEM_CHAT_FILE_TEXT, new FileSelectBean(file.getName(), file.getAbsolutePath(), false, FileCacheUtils.getFileSize(file), 0)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:

                break;
        }

    }

    @Override
    public void onBackPressed() {
        switch (fileResType) {
            case 0:
            case 1:
                super.onBackPressed();
                break;
            case 2:
                if (Environment.getExternalStorageDirectory().getAbsolutePath().equals(filePath)) {
                    super.onBackPressed();
                } else {
                    baseQuickAdapter.setNewData(null);
                    filePath = new File(filePath).getParentFile().getAbsolutePath();
                    initAdapterData(filePath);

                }
                break;
            case 3:
                if ((Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera").equals(filePath)) {
                    super.onBackPressed();
                } else {
                    baseQuickAdapter.setNewData(null);
                    filePath = new File(filePath).getParentFile().getAbsolutePath();
                    initAdapterData(filePath);

                }
                break;
            default:

                break;
        }

    }


    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);
        switch (tag) {
            case AppHttpPath.UPLOAD_FILES:
                ArrayList<String> picPaths = (ArrayList<String>) o;
                ArrayList<FileSelectBean> fileSelectBeans = getSelectedFileSizes();
                Intent intent = new Intent();
                intent.putStringArrayListExtra(REQUEST_SELECT_FILES_STR, picPaths);
                intent.putParcelableArrayListExtra(REQUEST_SELECT_FILES_BEANS, fileSelectBeans);
                setResult(REQUEST_SELECT_FILES, intent);
                finish();


                break;
            default:
                break;
        }

    }

    /**
     * 展示文件源的pop
     *
     * @return
     */
    private void initFileResourcePop() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.file_resource_layout, null);
        View shadowV = view.findViewById(R.id.file_resource_shadow_v);
        shadowV.setOnClickListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.work_order_status_rv);
        FileResourceAdapter adapter = new FileResourceAdapter(R.layout.file_resource_type_item);
        initRecyclerview(recyclerView, adapter, LinearLayoutManager.VERTICAL);
        adapter.setNewData(getFileResources());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                fileResType = position;
                baseQuickAdapter.setNewData(null);
                switch (fileResType) {
                    case 2:
                        filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        initAdapterData(filePath);
                        break;
                    case 3:
                        //手机相册
                        filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera";
                        initAdapterData(filePath);
                        break;
                    default:
                        initAdapterData(null);
                        break;
                }
                FileResourceBean stringSelectedBean = (FileResourceBean) adapter.getItem(position);
                getTitleTv().setText(stringSelectedBean.getFileResourceName());
                List<FileResourceBean> fileResourceBeans = adapter.getData();
                for (FileResourceBean fileResourceBean : fileResourceBeans) {
                    fileResourceBean.setSelected(false);
                }
                stringSelectedBean.setSelected(true);
                adapter.notifyDataSetChanged();
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
        }
    }

    /**
     * 获取文件路径
     *
     * @return
     */
    private List<FileResourceBean> getFileResources() {
        List<FileResourceBean> arrays = new ArrayList<>();
        arrays.add(new FileResourceBean(R.mipmap.file_resource_chat,"聊天中的文件",true));
        arrays.add(new FileResourceBean(R.mipmap.file_resource_collect,"收藏中的文件",false));
        arrays.add(new FileResourceBean(R.mipmap.file_resource_root,"手机存储",false));
        arrays.add(new FileResourceBean(R.mipmap.file_resource_photo,"手机相册",false));
        return arrays;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.file_resource_shadow_v:
                break;
            default:
                break;
        }
    }
}

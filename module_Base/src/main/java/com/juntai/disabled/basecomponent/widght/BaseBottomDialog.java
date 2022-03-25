package com.juntai.disabled.basecomponent.widght;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.R;
import com.juntai.disabled.basecomponent.bean.BaseMenuBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  仿微信底部弹框
 * @date 2020/4/16 13:59
 */
public class BaseBottomDialog extends DialogFragment implements View.OnClickListener {

    private List<BaseMenuBean> arrays = new ArrayList<>();
    private RecyclerView mBaseBottomDialogRv;
    private OnItemClick onItemClick;

    private BaseQuickAdapter adapter;
    private LinearLayoutManager manager;
    /**
     * 取消
     */
    private TextView mBaseBottomDialogCancelTv;


    public void initBottomDg(List<BaseMenuBean> arrays, BaseQuickAdapter adapter, LinearLayoutManager manager, OnItemClick onItemClick) {
        this.arrays = arrays;
        this.adapter = adapter;
        this.manager = manager;
        this.onItemClick = onItemClick;

    }


    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getShowsDialog()) {
            setShowsDialog(false);
        }
        super.onActivityCreated(savedInstanceState);
        setShowsDialog(true);

        View view = getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new IllegalStateException(
                        "DialogFragment can not be attached to a container view");
            }
            getDialog().setContentView(view);
        }
        final Activity activity = getActivity();
        if (activity != null) {
            getDialog().setOwnerActivity(activity);
        }
        if (savedInstanceState != null) {
            Bundle dialogState = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG);
            if (dialogState != null) {
                getDialog().onRestoreInstanceState(dialogState);
            }
        }
        // 设置宽度为屏宽、位置靠近屏幕底部
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.base_bottom_dialog, null);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {//利用反射获取listenersHandler
            Field field = Dialog.class.getDeclaredField("mListenersHandler");
            field.setAccessible(true);
            Handler mListenersHandler = (Handler) field.get(getDialog());
            if (mListenersHandler != null) {
                mListenersHandler.removeCallbacksAndMessages(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView(View view) {
        mBaseBottomDialogRv = (RecyclerView) view.findViewById(R.id.base_bottom_dialog_rv);
        mBaseBottomDialogCancelTv = (TextView) view.findViewById(R.id.base_bottom_dialog_cancel_tv);
        mBaseBottomDialogCancelTv.setOnClickListener(this);
        if (adapter == null) {
            adapter = new BottomDialogAdapter(R.layout.single_text_layout);
        }
        if (manager == null) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        mBaseBottomDialogRv.setAdapter(adapter);
        mBaseBottomDialogRv.setLayoutManager(manager);
        adapter.setNewData(arrays);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(adapter, view, position);
                }
            }
        });
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.base_bottom_dialog_cancel_tv) {
            dismiss();
        }
    }

    public interface OnItemClick {
        void onItemClick(BaseQuickAdapter adapter, View view, int position);
    }
}

package com.example.commonlib.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.commonlib.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by smile on 2019/5/5.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    private final String TAG = getClass().getSimpleName();

    protected Unbinder unbinder;
    protected Activity activity;

    protected abstract int bindLayout();
    protected abstract void initView();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "底部弹出框 BaseDialogFragment onAttach context = " + context);
        activity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Design_Light_BottomSheetDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(bindLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);

        Log.e(TAG, "底部弹出框 BaseDialogFragment onCreateView: ");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        Log.e(TAG, "底部弹出框 BaseDialogFragment onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "底部弹出框 BaseDialogFragment onStart: ");
        DisplayMetrics dm = null;
        WindowManager windowManager = null;
        if (activity != null) {
            windowManager = activity.getWindowManager();
        }
        if (windowManager != null) {
            dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
        }

        Window window = null;
        Dialog dialog = getDialog();
        if (dialog != null) {
            window = getDialog().getWindow();
        }
        if (window != null) {
            //默认开始的时候关闭软键盘
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            if (dm == null || dm.heightPixels < 320) {
                params.height = 320;
            } else {
                params.height = dm.heightPixels - 360;
            }
            window.setAttributes(params);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        Log.e(TAG, "BaseDialogFragment onDestroyView: 底部弹出框已销毁1");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "BaseDialogFragment onDestroy: 底部弹出框已销毁2");
    }
}

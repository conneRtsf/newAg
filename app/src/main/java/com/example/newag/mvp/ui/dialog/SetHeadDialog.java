package com.example.newag.mvp.ui.dialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.luck.picture.lib.config.PictureSelectionConfig.selectorStyle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.newag.Engine.GlideEngine;
import com.example.newag.Engine.ImageCropEngine;
import com.example.newag.R;
import com.example.newag.mvp.ui.my.ChangeMyActivity;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.basic.PictureCommonFragment;
import com.luck.picture.lib.basic.PictureSelectionCameraModel;
import com.luck.picture.lib.basic.PictureSelectionModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.engine.SandboxFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.interfaces.OnCallbackIndexListener;
import com.luck.picture.lib.utils.MediaUtils;
import com.luck.picture.lib.utils.SandboxTransformUtils;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.ButterKnife;

public class SetHeadDialog extends DialogFragment implements View.OnClickListener {
    protected FragmentActivity mActivity;
    private TextView take;
    private TextView select;
    public void setmActivity(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    //自定义样式，注：此处主要设置弹窗的宽高
    @Override
    public int getTheme() {
        return R.style.SetHeadDialog;
    }

    public void show() {
        mActivity.runOnUiThread(() -> {
            if (isActivityAlive()) {
                FragmentManager fm = mActivity.getSupportFragmentManager();
                Fragment prev = fm.findFragmentByTag(getClass().getSimpleName());
                if (prev != null) fm.beginTransaction().remove(prev);
                if (!SetHeadDialog.this.isAdded()) {
                    SetHeadDialog.super.show(fm, getClass().getSimpleName());
                }
            }
        });
    }

    @Override
    public void dismiss() {
        mActivity.runOnUiThread(() -> {
            if (isActivityAlive()) {
                SetHeadDialog.super.dismissAllowingStateLoss();
            }
        });
    }

    private boolean isActivityAlive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return mActivity != null && !mActivity.isFinishing() && !mActivity.isDestroyed();
        } else {
            return mActivity != null && !mActivity.isFinishing();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_sethead, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        take=view.findViewById(R.id.takePhoto);
        take.setOnClickListener(this);
        select=view.findViewById(R.id.selectPhoto);
        select.setOnClickListener(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog;
        dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.takePhoto:

                break;
            case R.id.selectPhoto:
                ((ChangeMyActivity) Objects.requireNonNull(getActivity())).openPhotoSelector();
        }
    }

}

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
                openPhotoTaker();
                break;
            case R.id.selectPhoto:
                openPhotoSelector();
        }
    }
    private void openPhotoSelector() {
        // 进入相册
        PictureSelectionModel selectionModel = PictureSelector.create(mActivity)
                .openGallery(SelectMimeType.TYPE_IMAGE)//0 TYPE ALL 1 Image
                .setSelectorUIStyle(selectorStyle)
                .setImageEngine(GlideEngine.createGlideEngine())//Glide Picasso
                .setCropEngine(new ImageCropEngine(selectorStyle))//是否裁剪 null
                .setCompressEngine(null)//是否压缩
                .setSandboxFileEngine(new MeSandboxFileEngine())
                .setCameraInterceptListener(null)//自定义相机 null
                .setRecordAudioInterceptListener(null) //录音回调
                .setSelectLimitTipsListener(null)//拦截自定义提示
                .setEditMediaInterceptListener(null)//自定义编辑时间 null
                .setPermissionDescriptionListener(null)//权限说明 null
                .setPreviewInterceptListener(null)//预览 null
                .setPermissionDeniedListener(null)//权限说明null
                //.setExtendLoaderEngine(getExtendLoaderEngine())
                .setInjectLayoutResourceListener(null)//注入自定义布局 null
                .setSelectionMode(SelectModeConfig.SINGLE)//多选单选
//                .setLanguage(Tools.getLanage().equals("zh") ? 0 : 1) //-2 简体0繁体1
                .setQuerySortOrder("")//降序 升序 查询
//                .setOutputCameraDir(chooseMode == SelectMimeType.ofAudio()
//                        ? getSandboxAudioOutputPath() : getSandboxCameraOutputPath())
//                .setOutputAudioDir(chooseMode == SelectMimeType.ofAudio()
//                        ? getSandboxAudioOutputPath() : getSandboxCameraOutputPath())
//                .setQuerySandboxDir(chooseMode == SelectMimeType.ofAudio()
//                        ? getSandboxAudioOutputPath() : getSandboxCameraOutputPath())
                .isDisplayTimeAxis(true)//显示资源时间轴
//                .isOnlyObtainSandboxDir(cb_only_dir.isChecked())
                .isPageStrategy(false)//false 指定目录
                .isOriginalControl(false)//false开启原图
                .isDisplayCamera(false)//显示摄像 图标
                .isOpenClickSound(false)//是否开启点击声音 false
                .setSkipCropMimeType(getNotSupportCrop())
                .isFastSlidingSelect(true)//true 滑动选择
                //.setOutputCameraImageFileName("luck.jpeg")
                //.setOutputCameraVideoFileName("luck.mp4")
                .isWithSelectVideoImage(false)//图片视频同时选择选 true
                .isPreviewFullScreenMode(false)
                .isPreviewZoomEffect(false)
                .isPreviewImage(false)
                //.setQueryOnlyMimeType(PictureMimeType.ofGIF())
//                .isMaxSelectEnabledMask(cbEnabledMask.isChecked())//达到最大可选 显示蒙层
                .isDirectReturnSingle(false) //单选模式直接返回
                .setMaxSelectNum(1)
//                .setMaxVideoSelectNum(maxSelectVideoNum)
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)
                .isGif(false);//是否显示gif false
//                .setSelectedData(mAdapter.getData());
        selectionModel.forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private void openPhotoTaker() {
        PictureSelectionCameraModel cameraModel = PictureSelector.create(mActivity)
                .openCamera(SelectMimeType.TYPE_IMAGE)//0 TYPE ALL 1 Image
                .setCropEngine(new ImageCropEngine(selectorStyle))//是否裁剪 null
                .setSandboxFileEngine(new MeSandboxFileEngine())
                .isOriginalControl(false);//false开启原图
        cameraModel.forResultActivity(PictureConfig.REQUEST_CAMERA);
    }

    /**
     * 自定义沙盒文件处理
     */
    private static class MeSandboxFileEngine implements SandboxFileEngine {

        @Override
        public void onStartSandboxFileTransform(Context context, boolean isOriginalImage,
                                                int index, LocalMedia media,
                                                OnCallbackIndexListener<LocalMedia> listener) {
            if (PictureMimeType.isContent(media.getAvailablePath())) {//沙盒文件
                String sandboxPath = SandboxTransformUtils.copyPathToSandbox(context, media.getPath(),
                        media.getMimeType());
                media.setSandboxPath(sandboxPath);
            }
            if (isOriginalImage) {
                String originalPath = SandboxTransformUtils.copyPathToSandbox(context, media.getPath(),
                        media.getMimeType());
                media.setOriginalPath(originalPath);
                media.setOriginal(!TextUtils.isEmpty(originalPath));
            }
            listener.onCall(media, index);
        }
    }

    private String[] getNotSupportCrop() {
        return new String[]{PictureMimeType.ofGIF(), PictureMimeType.ofWEBP()};
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == PictureConfig.REQUEST_CAMERA) {
                ArrayList<LocalMedia> result = PictureSelector.obtainSelectorList(data);
                analyticalSelectResults(result);
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.i(TAG, "onActivityResult PictureSelector Cancel");
        }
    }

    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(getContext(), media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(getContext(), media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
            int id=(int) media.getId();
            ImageView imageView=mActivity.findViewById(R.id.changeMy);
            imageView.setImageResource(id);
        }
    }
}

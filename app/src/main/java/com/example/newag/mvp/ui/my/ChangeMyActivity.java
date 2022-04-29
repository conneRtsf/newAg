package com.example.newag.mvp.ui.my;


import static com.luck.picture.lib.config.PictureSelectionConfig.selectorStyle;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.widget.ImageView;


import androidx.annotation.Nullable;

import com.example.newag.Engine.GlideEngine;
import com.example.newag.Engine.ImageCropEngine;
import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.mvp.ui.dialog.SetHeadDialog;
import com.luck.picture.lib.animators.AnimationType;

import com.luck.picture.lib.app.PictureAppMaster;
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
import com.luck.picture.lib.immersive.ImmersiveManager;
import com.luck.picture.lib.interfaces.OnCallbackIndexListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.utils.MediaUtils;
import com.luck.picture.lib.utils.SandboxTransformUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeMyActivity extends BaseActivity {
    private final static String TAG = "PictureSelectorTag";
    private ImageView imageView;
    @OnClick(R.id.back)
    void Click1(){
        ChangeMyActivity.this.finish();
    }

    @Override
    protected void initBaseData() {
    }

    @Override
    protected void baseConfigView() {
        this.imageView=findViewById(R.id.changeMy);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_changemy;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }

    @OnClick(R.id.changeMy)
    void onClick(){
        SetHeadDialog dialog=new SetHeadDialog();
        dialog.setmActivity(this);
        dialog.show();
    }
    public void openPhotoSelector(){
       try {
           PictureSelector.create(ChangeMyActivity.this)
                   .openGallery(SelectMimeType.ofAll())
                   .setImageEngine(GlideEngine.createGlideEngine())
                   .buildLaunch(R.id.fragment_container, new OnResultCallbackListener<LocalMedia>() {
                       @Override
                       public void onResult(ArrayList<LocalMedia> result) {
                           setTranslucentStatusBar();
                           analyticalSelectResults(result);
                       }

                       @Override
                       public void onCancel() {
                           setTranslucentStatusBar();
                           Log.i(TAG, "PictureSelector Cancel");
                       }
                   });
       }catch (Error error){
           error.printStackTrace();
       }
    }

    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
        StringBuilder builder = new StringBuilder();
        builder.append("Result").append("\n");
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(this,media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(PictureAppMaster.getInstance().getAppContext(), media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
            builder.append(media.getAvailablePath()).append("\n");
            Log.i(TAG, "文件名: " + media.getFileName());
            Log.i(TAG, "是否压缩:" + media.isCompressed());
            Log.i(TAG, "压缩:" + media.getCompressPath());
            Log.i(TAG, "原图:" + media.getPath());
            Log.i(TAG, "绝对路径:" + media.getRealPath());
            Log.i(TAG, "是否裁剪:" + media.isCut());
            Log.i(TAG, "裁剪:" + media.getCutPath());
            Log.i(TAG, "是否开启原图:" + media.isOriginal());
            Log.i(TAG, "原图路径:" + media.getOriginalPath());
            Log.i(TAG, "沙盒路径:" + media.getSandboxPath());
            Log.i(TAG, "原始宽高: " + media.getWidth() + "x" + media.getHeight());
            Log.i(TAG, "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
            Log.i(TAG, "文件大小: " + media.getSize());
        }
    }

    private void setTranslucentStatusBar() {
        ImmersiveManager.translucentStatusBar(ChangeMyActivity.this, true);
    }
}

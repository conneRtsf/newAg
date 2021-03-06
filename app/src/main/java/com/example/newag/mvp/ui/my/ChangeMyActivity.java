package com.example.newag.mvp.ui.my;


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
import com.luck.picture.lib.style.PictureSelectorStyle;
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

    private PictureSelectorStyle selectorStyle;
    public void openPhotoSelector(){
        selectorStyle = new PictureSelectorStyle();
           PictureSelectionModel selectionModel = PictureSelector.create(ChangeMyActivity.this)
                   .openGallery(SelectMimeType.TYPE_IMAGE)//0 TYPE ALL 1 Image
                   .setSelectorUIStyle(selectorStyle)
                   .setImageEngine(GlideEngine.createGlideEngine())//Glide Picasso
                   .setCropEngine(new ImageCropEngine(selectorStyle))//???????????? null
                   .setCompressEngine(null)//????????????
                   .setSandboxFileEngine(new MeSandboxFileEngine())
                   .setCameraInterceptListener(null)//??????????????? null
                   .setRecordAudioInterceptListener(null) //????????????
                   .setSelectLimitTipsListener(null)//?????????????????????
                   .setEditMediaInterceptListener(null)//????????????????????? null
                   .setPermissionDescriptionListener(null)//???????????? null
                   .setPreviewInterceptListener(null)//?????? null
                   .setPermissionDeniedListener(null)//????????????null
                   //.setExtendLoaderEngine(getExtendLoaderEngine())
                   .setInjectLayoutResourceListener(null)//????????????????????? null
                   .setSelectionMode(SelectModeConfig.SINGLE)//????????????
//                .setLanguage(Tools.getLanage().equals("zh") ? 0 : 1) //-2 ??????0??????1
                   .setQuerySortOrder("")//?????? ?????? ??????
//                .setOutputCameraDir(chooseMode == SelectMimeType.ofAudio()
//                        ? getSandboxAudioOutputPath() : getSandboxCameraOutputPath())
//                .setOutputAudioDir(chooseMode == SelectMimeType.ofAudio()
//                        ? getSandboxAudioOutputPath() : getSandboxCameraOutputPath())
//                .setQuerySandboxDir(chooseMode == SelectMimeType.ofAudio()
//                        ? getSandboxAudioOutputPath() : getSandboxCameraOutputPath())
                   .isDisplayTimeAxis(true)//?????????????????????
//                .isOnlyObtainSandboxDir(cb_only_dir.isChecked())
                   .isPageStrategy(false)//false ????????????
                   .isOriginalControl(false)//false????????????
                   .isDisplayCamera(false)//???????????? ??????
                   .isOpenClickSound(false)//???????????????????????? false
                   .setSkipCropMimeType(getNotSupportCrop())
                   .isFastSlidingSelect(true)//true ????????????
                   //.setOutputCameraImageFileName("luck.jpeg")
                   //.setOutputCameraVideoFileName("luck.mp4")
                   .isWithSelectVideoImage(false)//??????????????????????????? true
                   .isPreviewFullScreenMode(false)
                   .isPreviewZoomEffect(false)
                   .isPreviewImage(false)
                   //.setQueryOnlyMimeType(PictureMimeType.ofGIF())
//                .isMaxSelectEnabledMask(cbEnabledMask.isChecked())//?????????????????? ????????????
                   .isDirectReturnSingle(false) //????????????????????????
                   .setMaxSelectNum(1)
//                .setMaxVideoSelectNum(maxSelectVideoNum)
                   .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)
                   .isGif(false);//????????????gif false
//                .setSelectedData(mAdapter.getData());
           selectionModel.forResult(PictureConfig.CHOOSE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == PictureConfig.REQUEST_CAMERA) {
                    ArrayList<LocalMedia> result = PictureSelector.obtainSelectorList(data);
                    analyticalSelectResults(result);
                }
            }
        } catch (Exception e) {
//            dismissUploadDialog();
        }
    }

    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
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
            Log.i(TAG, "?????????: " + media.getFileName());
            Log.i(TAG, "????????????:" + media.isCompressed());
            Log.i(TAG, "??????:" + media.getCompressPath());
            Log.i(TAG, "??????:" + media.getPath());
            Log.i(TAG, "????????????:" + media.getRealPath());
            Log.i(TAG, "????????????:" + media.isCut());
            Log.i(TAG, "??????:" + media.getCutPath());
            Log.i(TAG, "??????????????????:" + media.isOriginal());
            Log.i(TAG, "????????????:" + media.getOriginalPath());
            Log.i(TAG, "????????????:" + media.getSandboxPath());
            Log.i(TAG, "????????????: " + media.getWidth() + "x" + media.getHeight());
            Log.i(TAG, "????????????: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
            Log.i(TAG, "????????????: " + media.getSize());
        }
    }

    private static class MeSandboxFileEngine implements SandboxFileEngine {

        @Override
        public void onStartSandboxFileTransform(Context context, boolean isOriginalImage,
                                                int index, LocalMedia media,
                                                OnCallbackIndexListener<LocalMedia> listener) {
            if (PictureMimeType.isContent(media.getAvailablePath())) {//????????????
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
}

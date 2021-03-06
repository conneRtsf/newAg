package com.example.newag.mvp.ui.change;

import static com.luck.picture.lib.config.PictureSelectionConfig.selectorStyle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.newag.Engine.GlideEngine;
import com.example.newag.Engine.ImageCropEngine;
import com.example.newag.R;
import com.example.newag.mvp.model.bean.AllText;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.app.PictureAppMaster;
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

public class LeaveActivity extends AppCompatActivity {
    AllText allText;
    int position;
    private final static String TAG = "PictureSelectorTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_plustemplate);
        EditText editText=findViewById(R.id.et_1);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        allText=(AllText) bundle.getSerializable("data");
        editText.setText(allText.getName());
        position=(int)bundle.getSerializable("position");
        Button button=findViewById(R.id.commit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent();
                Bundle bundle1=new Bundle();
                allText.setName(editText.getText().toString());
                bundle1.putSerializable("data",allText);
                bundle1.putSerializable("position",position);
                intent1.putExtras(bundle1);
                setResult(2,intent1);
                finish();
            }
        });
        Button button1=findViewById(R.id.make_text);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhotoSelector();
            }
        });
    }
    public void openPhotoSelector(){
        PictureSelectionModel selectionModel = PictureSelector.create(LeaveActivity.this)
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<LocalMedia> selectorResult = PictureSelector.obtainSelectorList(data);
            analyticalSelectResults(selectorResult);
        } else if (resultCode == RESULT_CANCELED) {
            Log.i(TAG, "onActivityResult PictureSelector Cancel");
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
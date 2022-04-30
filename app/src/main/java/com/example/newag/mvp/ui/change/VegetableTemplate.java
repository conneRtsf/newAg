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

public class VegetableTemplate extends AppCompatActivity {
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
        PictureSelectionModel selectionModel = PictureSelector.create(VegetableTemplate.this)
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
}
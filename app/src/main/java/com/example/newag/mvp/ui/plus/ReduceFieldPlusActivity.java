package com.example.newag.mvp.ui.plus;

import static com.luck.picture.lib.config.PictureSelectionConfig.selectorStyle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newag.Engine.GlideEngine;
import com.example.newag.Engine.ImageCropEngine;
import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.example.newag.mvp.ui.main.test;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReduceFieldPlusActivity extends BaseActivity {
    private String TAG="test";
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    @BindView(R.id.plus2)
    ImageButton lb1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.name)
    EditText name;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.length1)
    EditText length1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.length2)
    EditText length2;
    @BindView(R.id.width1)
    EditText width1;
    @BindView(R.id.width2)
    EditText width2;
    @BindView(R.id.orientation)
    EditText orientation;
    @BindView(R.id.position)
    EditText position;
    @BindView(R.id.production)
    EditText production;
    @BindView(R.id.commit)
    Button commit;
    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        lb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopueWindow();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSync();
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_plusreducefield;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    public void postSync() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", String.valueOf(name.getText()));
        paramsMap.put("width", String.valueOf(width1.getText()));
        paramsMap.put("widthUsed", String.valueOf(width2.getText()));
        paramsMap.put("length", String.valueOf(length1.getText()));
        paramsMap.put("lengthUsed", String.valueOf(length2.getText()));
        paramsMap.put("orientation", String.valueOf(orientation.getText()));
        paramsMap.put("location", String.valueOf(position.getText()));
        paramsMap.put("product", String.valueOf(production.getText()));
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, Objects.requireNonNull(paramsMap.get(key)));
        }
        RequestBody formBody = builder.build();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url("http://124.222.111.61:9000/daily/field/add")
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String ResponseData = response.body().string();
                Log.e("onResponse: ", ResponseData);
                new Thread(() -> {
                    try {
                        Looper.prepare();
                        JSONObject jsonObject1 = new JSONObject(ResponseData);
                        Toast.makeText(ReduceFieldPlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if(jsonObject1.getString("msg").equals("????????????")){
                            ReduceFieldPlusActivity.this.finish();
                        }
                        Looper.loop();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });
    }
    private void showPopueWindow(){
        View popView = View.inflate(this,R.layout.popupwindow_camera_need,null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
//        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //??????????????????
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        popupWindow.setFocusable(true);
        //????????????popueWindow??????
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoSelector();
                popupWindow.dismiss();

            }
        });
//        bt_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//
//            }
//        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow???????????????????????????
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow???????????????????????????
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);

    }
    public void openPhotoSelector(){
        PictureSelectionModel selectionModel = PictureSelector.create(ReduceFieldPlusActivity.this)
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
            String fileName = media.getCutPath();
            Bitmap bm = BitmapFactory.decodeFile(fileName);
            lb1.setImageBitmap(bm);
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

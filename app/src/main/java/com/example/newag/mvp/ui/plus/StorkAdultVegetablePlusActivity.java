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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newag.Engine.GlideEngine;
import com.example.newag.Engine.ImageCropEngine;
import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.intercept.LoginIntercept;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StorkAdultVegetablePlusActivity extends BaseActivity {
    @BindView(R.id.plus2)
    ImageButton lb1;
    private String TAG="test";
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.inventory)
    EditText inventory;
    @BindView(R.id.inventoryUnit)
    EditText inventoryUnit;
    @BindView(R.id.factory)
    EditText factory;
    @BindView(R.id.note)
    EditText note;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.stock)
    Spinner stock;
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    public int[] midIn;

    public String[] getmFactory() {
        return mFactory;
    }

    public void setmFactory(String[] mFactory) {
        this.mFactory = mFactory;
    }

    public String[] db;
    public String[] mFactory;

    public String[] getDb() {
        return db;
    }

    public void setDb(String[] db) {
        this.db = db;
    }

    public int[] getMidIn() {
        return midIn;
    }

    public void setMidIn(int[] midIn) {
        this.midIn = midIn;
    }

    private ArrayAdapter<CharSequence> adapter;
    public String[] unit;

    public String[] getUnit() {
        return unit;
    }

    public void setUnit(String[] unit) {
        this.unit = unit;
    }
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
        OkHttpClient httpClient=new OkHttpClient.Builder()
                .addInterceptor(new LoginIntercept())
                .build();
        Request requestField = new Request.Builder()
                .get()
                .url("http://124.222.111.61:9000/daily/input/queryAll")
                .build();
        new Thread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                try {
                    Call call = httpClient.newCall(requestField);
                    Response response=call.execute();
                    assert response.body() != null;
                    String responseData=response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    String json=jsonObject.getJSONObject("data").toString();
                    Map<String, JsonArray> map = new Gson()
                            .fromJson(json, new TypeToken<Map<String, JsonArray>>() {}
                                    .getType());
                    String[] newone;
                    int t=0;
                    int[] midI=new int[1000];
                    String[] midS=new String[1];
                    String[] midw=new String[1000];
                    String[] midf=new String[1000];
                    String[] midMap=new String[1000];
                    midS[0]="??????????????????";
                    String type2 = null;
                    for (Map.Entry<String, JsonArray> entry : map.entrySet()) {
                        JsonArray mapValue = entry.getValue();
                        JSONArray pond = new JSONArray(String.valueOf(mapValue));
                        Log.e("pond1: ", String.valueOf(pond));
                        newone=new String[pond.length()];
                        for (int i = 0; i < pond.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) pond.get(i);
                            int id = jsonObject1.getInt("id");
                            String type = jsonObject1.getString("type");
                            String unit=jsonObject1.getString("inventoryUnit");
                            String factory=jsonObject1.getString("factory");
                            String information=jsonObject1.getString("information");
                            type2=type;
                            if (type.equals("adultvegetable")){
                                midMap[t]=information;
                                midI[t] = id;
                                midw[t]=unit;
                                midf[t]=factory;
                                t++;
                                String name = jsonObject1.getString("name");
                                newone[i] = name;
                            }
                        }
                        if (type2.equals("adultvegetable")) {
                            midS = insert(midS, newone);
                        }
                    }
                    setMidM(midMap);
                    setmFactory(midf);
                    setMidIn(midI);
                    setDb(midS);
                    setUnit(midw);
                    String[] finalMidS = midS;
                    StorkAdultVegetablePlusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<CharSequence>(StorkAdultVegetablePlusActivity.this,android.R.layout.simple_spinner_item, finalMidS);
                            stock.setAdapter(adapter);
                            stock.setOnItemSelectedListener(new StorkAdultVegetablePlusActivity.OnItemSelectedListenerImpl());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private class OnItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String city = (String) parent.getItemAtPosition(position);
            Log.e( "onItemSelected: ", String.valueOf(id));
            String[] BS=getDb();
            int[] BC=getMidIn();
            String[] Unit=getUnit();
            String[] mFactory=getmFactory();
            String[] map=getMidM();
            if (city.equals("??????????????????")) {
                factory.setText("");
                inventoryUnit.setText("");
                lb1.setImageDrawable(getDrawable(R.drawable.plus3));
                lb1.setEnabled(true);
                name.setText("");
                inventoryUnit.setEnabled(true);
                name.setEnabled(true);
                factory.setEnabled(true);
                note.setVisibility(View.VISIBLE);
            }else{
                for (int i = 1; i < BS.length; i++) {
                    if(city.equals(BS[i])){
                        factory.setText(mFactory[i-1]);
                        inventoryUnit.setText(Unit[i-1]);
                        name.setText(city);
                        inventoryUnit.setEnabled(false);
                        lb1.setImageBitmap(returnBitMap(map[i-1]));
                        lb1.setEnabled(false);
                        name.setEnabled(false);
                        factory.setEnabled(false);
                        note.setVisibility(View.GONE);
                        break;
                    }
                }
            }
            commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (city.equals("??????????????????")) {
                        if (getPaths()==null){
                            Toast.makeText(StorkAdultVegetablePlusActivity.this, "???????????????",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        RequestBody requestFile =
                                RequestBody.create(MediaType.parse("multipart/form-data"), getPng());
                        MultipartBody.Builder multiBuilder=new MultipartBody.Builder();
                        multiBuilder.addFormDataPart("information", getPng().getName(), requestFile);
                        multiBuilder.setType(MultipartBody.FORM);
                        multiBuilder.addFormDataPart("information", getPng().getName(), requestFile);

                        HashMap<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("name", String.valueOf(name.getText()));
                        paramsMap.put("inventory", String.valueOf(inventory.getText()));
                        paramsMap.put("inventoryUnit", String.valueOf(inventoryUnit.getText()));
                        paramsMap.put("factory", String.valueOf(factory.getText()));
                        paramsMap.put("note", String.valueOf(note.getText()));
                        paramsMap.put("type", "adultvegetable");
                        if (!paramsMap.isEmpty()) {
                            for (String key : paramsMap.keySet()) {
                                multiBuilder.addPart(
                                        Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                                        RequestBody.create(null, Objects.requireNonNull(paramsMap.get(key))));
                            }
                        }
                        RequestBody multiBody=multiBuilder.build();
                        OkHttpClient httpClient = new OkHttpClient.Builder()
                                .addInterceptor(new LoginIntercept())
                                .build();
                        Request request = new Request.Builder()
                                .post(multiBody)
                                .url("http://124.222.111.61:9000/daily/input/add")
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
                                        Toast.makeText(StorkAdultVegetablePlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                        if (jsonObject1.getString("msg").equals("????????????")) {
                                            StorkAdultVegetablePlusActivity.this.finish();
                                        }
                                        Looper.loop();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                        });
                    }else{
                        for (int i = 1; i < BS.length; i++) {
                            if(city.equals(BS[i])){
                                HashMap<String, String> paramsMap = new HashMap<>();
                                paramsMap.put("inventory", String.valueOf(inventory.getText()));
                                paramsMap.put("id", String.valueOf(BC[i-1]));
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
                                        .url("http://124.222.111.61:9000/daily/input/add")
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
                                                Toast.makeText(StorkAdultVegetablePlusActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                                if (jsonObject1.getString("msg").equals("????????????")) {
                                                    StorkAdultVegetablePlusActivity.this.finish();
                                                }
                                                Looper.loop();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }).start();
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }

    }
    @Override
    protected int layoutId() {
        return R.layout.activity_plusstork;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    private static String[] insert(String[] arr, String... str) {
        int size = arr.length; // ?????????????????????
        int newSize = size + str.length; // ????????????????????????????????????????????????

        // ???????????????????????????
        String[] tmp = new String[newSize];
        // ????????????????????????????????????????????????????????????????????????
        for (int i = 0; i < size; i++) {
            tmp[i] = arr[i];
        }
        // ???????????????????????????????????????
        for (int i = size; i < newSize; i++) {
            tmp[i] = str[i - size];
        }
        return tmp; // ????????????????????????????????????
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
        PictureSelectionModel selectionModel = PictureSelector.create(StorkAdultVegetablePlusActivity.this)
                .openGallery(SelectMimeType.TYPE_IMAGE)//0 TYPE ALL 1 Image
                .setSelectorUIStyle(selectorStyle)
                .setImageEngine(GlideEngine.createGlideEngine())//Glide Picasso
                .setCropEngine(new ImageCropEngine(selectorStyle))//???????????? null
                .setCompressEngine(null)//????????????
                .setSandboxFileEngine(new StorkAdultVegetablePlusActivity.MeSandboxFileEngine())
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
            String path;
            if (media.isCut() && !media.isCompressed()) {
                // ?????????
                path = media.getCutPath();
            } else if (media.isCut() || media.isCompressed()) {
                // ?????????,???????????????????????????,??????????????????????????????
                path = media.getCompressPath();
            } else {
                // ??????
                path = media.getPath();
            }
            setPaths(path);

            builder.append(media.getAvailablePath()).append("\n");
            Bitmap bm = BitmapFactory.decodeFile(path);
            lb1.setImageBitmap(bm);
            File file = new File(path);
            setPng(file);
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


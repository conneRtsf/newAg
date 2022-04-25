package com.example.newag.mvp.ui.my;

import static android.content.ContentValues.TAG;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.mvp.ui.dialog.SetHeadDialog;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.utils.MediaUtils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeMyActivity extends BaseActivity {
    @OnClick(R.id.back)
    void Click1(){
        ChangeMyActivity.this.finish();
    }

    @Override
    protected void initBaseData() {
    }

    @Override
    protected void baseConfigView() {

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_changemy;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.changeMy)
    void onClick(){
        SetHeadDialog dialog=new SetHeadDialog();
        dialog.setmActivity(this);
        dialog.show();
    }

}

package com.example.newag.mvp.ui.monitor;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.mvp.ui.main.MyFragment;
import com.example.newag.mvp.ui.main.NourishFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class BucketManagementActivity extends BaseActivity {
    @OnClick(R.id.tb2)
    void  onClick2(){
        finish();
    }
    @BindView(R.id.RG)
    RadioGroup radioGroup;
    @BindView(R.id.sow1)
    RadioButton sow1;
    @BindView(R.id.sow2)
    RadioButton sow2;
    private Fragment bucketFragment;
    private Fragment batchFragment;
    @Override
    protected void initBaseData() {

    }

    @Override
    protected void baseConfigView() {
        setFragment(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.sow1:
                        setFragment(0);
                        break;
                    case R.id.sow2:
                        setFragment(1);
                        break;
                }
            }
        });
    }
    private void setFragment(int index){
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        hideFragments(mTransaction);
        switch (index){
            case 0:
                if(bucketFragment == null){
                    bucketFragment = new BucketFragment();
                    mTransaction.add(R.id.container, bucketFragment,
                            "bucket_fragment");
                }else {
                    mTransaction.show(bucketFragment);
                }
                break;
            case 1:
                if(batchFragment== null){
                    batchFragment= new BatchFragment();
                    mTransaction.add(R.id.container, batchFragment,
                            "batch_fragment");
                }else {
                    mTransaction.show(batchFragment);
                }
                break;
        }
        mTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(bucketFragment != null){
            transaction.hide(bucketFragment);
        }
        if(batchFragment!= null){
            transaction.hide(batchFragment);
        }
    }
    @Override
    protected int layoutId() {
        return R.layout.activity_bucket_management;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }
}
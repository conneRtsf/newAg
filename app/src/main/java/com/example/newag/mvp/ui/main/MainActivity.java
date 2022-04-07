package com.example.newag.mvp.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.newag.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView nourish;
    private TextView share;
    private TextView my;

    private Fragment nourishFragment;
    private Fragment shareFragment;
    private Fragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setFragment(0);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
            case R.id.nourish:
                setFragment(0);
                break;
            case R.id.share:
                setFragment(1);
                break;
            case R.id.my:
                setFragment(2);
                break;
        }
    }

    private void init(){
        nourish = (TextView)findViewById(R.id.nourish);
        share = (TextView)findViewById(R.id.share);
        my = (TextView)findViewById(R.id.my);

        nourish.setOnClickListener(this);
        share.setOnClickListener(this);
        my.setOnClickListener(this);
    }

    private void setFragment(int index){
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        hideFragments(mTransaction);
        switch (index){
            case 0:
                nourish.setTextColor(getResources()
                        .getColor(R.color.press));
                nourish.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.farm_press,0,0);
                if(nourishFragment == null){
                    nourishFragment = new nourishFragment();
                    mTransaction.add(R.id.container, nourishFragment,
                            "clothes_fragment");
                }else {
                    mTransaction.show(nourishFragment);
                }
                break;
            case 1:
                share.setTextColor(getResources()
                        .getColor(R.color.press));
                share.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.share_press,0,0);
                if(shareFragment == null){
                    shareFragment = new shareFragment();
                    mTransaction.add(R.id.container, shareFragment,
                            "food_fragment");
                }else {
                    mTransaction.show(shareFragment);
                }
                break;
            case 2:
                my.setTextColor(getResources()
                        .getColor(R.color.press));
                my.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.my_press,0,0);
                if(myFragment== null){
                    myFragment= new MyFragment();
                    mTransaction.add(R.id.container, myFragment,
                            "hotel_fragment");
                }else {
                    mTransaction.show(myFragment);
                }
                break;
        }
        mTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(nourishFragment != null){
            transaction.hide(nourishFragment);
            nourish.setTextColor(getResources()
                    .getColor(R.color.grey));
            nourish.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.farm,0,0);
        }
        if(shareFragment != null){
            transaction.hide(shareFragment);
            share.setTextColor(getResources()
                    .getColor(R.color.grey));
            share.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.share,0,0);
        }
        if(myFragment!= null){
            transaction.hide(myFragment);
            my.setTextColor(getResources()
                    .getColor(R.color.grey));
            my.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.my,0,0);
        }
    }
}
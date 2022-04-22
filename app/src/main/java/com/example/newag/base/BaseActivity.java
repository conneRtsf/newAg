package com.example.newag.base;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import butterknife.ButterKnife;
import com.example.newag.MyApplication;
import com.example.newag.R;
import com.example.newag.di.component.AppComponent;
import com.example.newag.utils.AppUtils;
import com.example.newag.utils.ScreenUtils;

public abstract class BaseActivity<T1 extends BaseContract.BasePresenter> extends AppCompatActivity {
    @Inject
    protected T1 mPresenter;
    private View contentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.base_view, null, false);
            contentView = getLayoutInflater().inflate(layoutId(), null, false);
            sparseArray.put(layoutId(), contentView);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contentView.setLayoutParams(layoutParams);
            FrameLayout frameLayout = view.findViewById(R.id.base_contentView);
            frameLayout.addView(contentView);
            if (ImmersionBar.isSupportStatusBarDarkFont()) {
                ImmersionBar.with(this).keyboardEnable(true).statusBarDarkFont(true).navigationBarEnable(false).init();
            } else {
                ImmersionBar.with(this).statusBarColor(R.color.gray).keyboardEnable(true).statusBarDarkFont(true).navigationBarEnable(false).init();
            }
            setContentView(view);
//            setActivityComponent(MyApplication.getMyApplication().getAppComponent());
            if (mPresenter != null) mPresenter.attachView(this);
            ButterKnife.bind(this);
            baseConfigView();
            initBaseData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    SparseArray<View> sparseArray = new SparseArray<>();

    public void showLoadView(int type) {
        showView(R.id.base_loading_viewstub, type);
    }

    public void showloadErrorView(String s) {
        showView(R.id.base_loading_error_viewstub, s);
    }

    public void showloadNoNetView() {
        showView(R.id.base_nonet_viewstub);
    }

    public void showNetErrorView() {
        showView(R.id.base_network_error_viewstub);
    }

    public void showContextView() {
        showView(layoutId());
    }

    public void showView(int viewstubId) {
        AppUtils.runOnUI(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < sparseArray.size(); i++) {
                    int key = sparseArray.keyAt(i);
                    View view = sparseArray.get(key);
                    view.setVisibility(View.GONE);
                }
                View view = sparseArray.get(viewstubId);
                if (null == view) {
                    ViewStub viewStub = getView(viewstubId);
                    view = viewStub.inflate();
                    sparseArray.put(viewstubId, view);
                    if (viewstubId == R.id.base_loading_error_viewstub || viewstubId == R.id.base_network_error_viewstub) {
                        View btn = view.findViewById(R.id.error_page);
                        if (btn != null)
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initBaseData();
                                }
                            });
                    }
                    if (viewstubId == R.id.base_loading_viewstub) {
                        ImageView imageView = view.findViewById(R.id.loading);
//                        ImageLoaderUtils.getInstance().loadImage(R.drawable.xing_loading, imageView);
                        Animation animation = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.rotate);
                        imageView.startAnimation(animation);
                    }
                }
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void showView(int viewstubId, String s) {
        AppUtils.runOnUI(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < sparseArray.size(); i++) {
                    int key = sparseArray.keyAt(i);
                    View view = sparseArray.get(key);
                    view.setVisibility(View.GONE);
                }
                View view = sparseArray.get(viewstubId);
                if (null == view) {
                    ViewStub viewStub = getView(viewstubId);
                    view = viewStub.inflate();
                    sparseArray.put(viewstubId, view);
                    if (viewstubId == R.id.base_loading_error_viewstub || viewstubId == R.id.base_network_error_viewstub) {
                        View btn = view.findViewById(R.id.error_page);
                        TextView textView = view.findViewById(R.id.errror_describe);
                        textView.setText(s);
                        if (btn != null)
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initBaseData();
                                }
                            });
                    }
                    if (viewstubId == R.id.base_loading_viewstub) {
                        ImageView imageView = view.findViewById(R.id.loading);
//                        ImageLoaderUtils.getInstance().loadImage(R.drawable.xing_loading, imageView);
                        Animation animation = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.rotate);
                        imageView.startAnimation(animation);
                    }
                }
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showView(int viewstubId, int type) {
        AppUtils.runOnUI(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < sparseArray.size(); i++) {
                    int key = sparseArray.keyAt(i);
                    View view = sparseArray.get(key);
                    view.setVisibility(View.GONE);
                }
                View view = sparseArray.get(viewstubId);
                if (null == view) {
                    ViewStub viewStub = getView(viewstubId);
                    view = viewStub.inflate();
                    sparseArray.put(viewstubId, view);
                    if (viewstubId == R.id.base_loading_error_viewstub || viewstubId == R.id.base_network_error_viewstub) {
                        View btn = view.findViewById(R.id.loading_again_btn);
                        if (btn != null)
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initBaseData();
                                }
                            });
                    }
                    if (viewstubId == R.id.base_loading_viewstub) {
                        ImageView imageView = view.findViewById(R.id.loading);
                        Animation animation = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.rotate);
                        imageView.startAnimation(animation);
//                        ImageLoaderUtils.getInstance().loadImage(R.drawable.xing_loading, imageView);
                    }
                }
                if (viewstubId == R.id.base_loading_viewstub) {
                    Log.e("当前显示的是加油", "当前现实加油");
                    sparseArray.get(layoutId()).setVisibility(View.VISIBLE);
                    LinearLayout linearLayout = view.findViewById(R.id.loadingview);
                    if (type == 0) {
                        linearLayout.setGravity(Gravity.CENTER);
                    } else if (type == 1) {
                        ImageView imageView = view.findViewById(R.id.loading);
                        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                        layoutParams.setMargins(0, ScreenUtils.dip2px(MyApplication.getMyApplication(), 50), 0, 0);
                        imageView.setLayoutParams(layoutParams);
                    }
                }
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    protected abstract void initBaseData();

    protected final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

//dependency inject, no usage
//    protected abstract T1 initPresenter();

    protected abstract void baseConfigView();

    protected abstract int layoutId();

    protected abstract void setActivityComponent(AppComponent appComponent);

    protected void VISIBLE(View... view) {
        for (View v : view) {
            v.setVisibility(View.VISIBLE);
        }
    }

    protected void GONE(View... view) {
        for (View v : view) {
            v.setVisibility(View.GONE);
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mPresenter != null) {
//            mPresenter.detachView();
//        }
//        // 必须调用该方法，防止内存泄漏
//        ImmersionBar.with(this).destroy();
//    }

    protected int getScreenW(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    protected int getScreenH(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 如果你的 app 可以横竖屏切换，并且适配 4.4 或者 emui3 手机请务必在 onConfigurationChanged 方法里添加这句话
        ImmersionBar.with(this).init();
    }

    @Override
    public void finish() {
        super.finish();
    }


    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }
}

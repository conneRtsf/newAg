package com.example.newag.mvp.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.example.newag.MyApplication;
import com.example.newag.R;
import com.example.newag.base.BaseFragment;
import com.example.newag.mvp.ui.accounting.SalesAccountingActivity;
import com.example.newag.mvp.ui.costs.PeopleCostActivity;
import com.example.newag.mvp.ui.dialog.WelcomeDialog;
import com.example.newag.mvp.ui.inputperiod.FishPeriodActivity;
import com.example.newag.mvp.ui.inspection.InspectionActivity;
import com.example.newag.mvp.ui.program.ProgramAddActivity;
import com.example.newag.mvp.ui.reduce.ReduceAddActivity;
import com.example.newag.mvp.ui.template.DrugTemplateActivity;
import com.example.newag.utils.SdkDelayInitUtil;
import com.example.newag.utils.SharedPreferencesUtil;
import com.example.newag.utils.UMInitUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NourishFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nourish;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        showWelcomedialog();
    }

    private void showWelcomedialog() {
        boolean welcomedialog = SharedPreferencesUtil.getInstance().getBoolean("welcomedialog", false);
        if (!welcomedialog && !MyApplication.getMyApplication().welcomedialogshowed) {
            try {
                WelcomeDialog dialog = new WelcomeDialog(getContext(), R.style.AppraiseDialog, new WelcomeDialog.agreeListener() {
                    @Override
                    public void agree() {
                        SharedPreferencesUtil.getInstance().putBoolean("welcomedialog", true).commit();
                        UMInitUtil.init();
                        SdkDelayInitUtil.getInstance().init();
                    }

                    @Override
                    public void disagreeAndFinish() {

                    }
                });
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                MyApplication.getMyApplication().welcomedialogshowed = true;
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                DisplayMetrics outMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
                int widthPixels = outMetrics.widthPixels;
                lp.width = (int) (widthPixels * 0.8); //设置宽度
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
//                        showGuide(mRootView);
                    }
                });
            }catch (Exception E){

            }

        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @OnClick(R.id.bt7)
    void onClick7(View view) {
        Intent intent = new Intent(getActivity(), PeopleCostActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt3)
    void onClick3(View view) {
        Intent intent = new Intent(getActivity(), SalesAccountingActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt4)
    void onClick4(View view) {
        Intent intent = new Intent(getActivity(), DrugTemplateActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt5)
    void onClick5(View view) {
        Intent intent = new Intent(getActivity(), FishPeriodActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt6)
    void onClick6(View view) {
        Intent intent = new Intent(getActivity(), ProgramAddActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt9)
    void onClick9(View view) {
        Intent intent = new Intent(getActivity(), InspectionActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt1)
    void onClick(View view) {
        Intent intent = new Intent(getActivity(), ReduceAddActivity.class);
        startActivity(intent);
    }
}
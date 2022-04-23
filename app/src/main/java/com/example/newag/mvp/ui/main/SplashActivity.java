package com.example.newag.mvp.ui.main;

import static com.umeng.commonsdk.utils.UMUtils.getTargetSdkVersion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.newag.MyApplication;
import com.example.newag.R;
import com.example.newag.base.BaseActivity;
import com.example.newag.di.component.AppComponent;
import com.example.newag.mvp.ui.dialog.PermissionDialog;
import com.example.newag.mvp.ui.dialog.WelcomeDialog;
import com.example.newag.mvp.ui.login.LoginActivity;
import com.example.newag.utils.SdkDelayInitUtil;
import com.example.newag.utils.SharedPreferencesUtil;
import com.example.newag.utils.UMInitUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {

    private static String FIRST_OPEN_FILE = "hsfirstopen";
    private static String FIRST_IMPORT_FILE = "firstimport";
    private static String FIRST_OPEN = "FIRST";
    private SharedPreferences firstOpen;
    private SharedPreferences firstImport;

    @Override
    protected void initBaseData() {
        firstOpen = getSharedPreferences(FIRST_OPEN_FILE, 0);
        firstImport = getSharedPreferences(FIRST_IMPORT_FILE, 0);
        showWelcomedialog();
    }

    @Override
    protected void baseConfigView() {

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setActivityComponent(AppComponent appComponent) {

    }

    private static final int REQUEST_PERMISSION = 0x1401;
    public static boolean selfPermissionGranted(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (getTargetSdkVersion(context) >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    private void showWelcomedialog(){
        boolean welcomedialog = SharedPreferencesUtil.getInstance().getBoolean("welcomedialog", false);
        if (!welcomedialog && !MyApplication.getMyApplication().welcomedialogshowed){
            WelcomeDialog dialog = new WelcomeDialog(SplashActivity.this, R.style.AppraiseDialog, true, new WelcomeDialog.agreeListener() {
                @Override
                public void agree() {
                    SharedPreferencesUtil.getInstance().putBoolean("welcomedialog",true).commit();
                    requestPermission();
                    UMInitUtil.init();
                    SdkDelayInitUtil.getInstance().init();
                }

                @Override
                public void disagreeAndFinish() {
                    MyApplication.getMyApplication().welcomedialogshowed=false;
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
            MyApplication.getMyApplication().welcomedialogshowed=true;
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            DisplayMetrics outMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            int widthPixels = outMetrics.widthPixels;
            lp.width = (int) (widthPixels * 0.8); //设置宽度
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
//                    showGuide(mRootView);
                }
            });
        } else {
            requestPermission();
        }
    }
    private void requestPermission() {
        boolean first = firstOpen.getBoolean(FIRST_OPEN, true);

        if (first && Build.VERSION.SDK_INT<Build.VERSION_CODES.Q) {
            boolean permission_write = selfPermissionGranted(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            boolean permission_read = selfPermissionGranted(this, android.Manifest.permission.READ_PHONE_STATE);
            if (!permission_write && !permission_read) {//都无权限 无读权限 无电话权限
                requestPermission(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE
                });
            } else if (!permission_write && permission_read) {//有电话 无存储
                requestPermission(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                });
            } else if (permission_write && !permission_read) {//有存储 无电话
                requestPermission(new String[]{
                        Manifest.permission.READ_PHONE_STATE
                });
            }  else {
                SdkDelayInitUtil.getInstance().ClientId(MyApplication.getMyApplication());
                start();
            }
        } else {
            SdkDelayInitUtil.getInstance().ClientId(MyApplication.getMyApplication());
            start();

        }
    }

    private void start() {
        boolean first = firstOpen.getBoolean(FIRST_OPEN,true);
        if (first){
            firstOpen.edit().putBoolean(FIRST_OPEN, false).apply();
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this,MainActivity.class));
        }
        this.finish();
    }

    private PermissionDialog detailDialog;

    private void requestPermission(final String[] strings) {
        if (detailDialog == null) {
            detailDialog = new PermissionDialog(SplashActivity.this, R.style.AppraiseDialog, new PermissionDialog.Click() {
                @Override
                public void click() {
                    detailDialog.dismiss();
//                    getAppDetailSettingIntent(SplashActivity.this);
                    ActivityCompat.requestPermissions((Activity) SplashActivity.this, strings, REQUEST_PERMISSION);
                }
            });
        }
        detailDialog.show();

//                new AlertDialog.Builder(this)
//				.setTitle(getString(R.string.remind))
//				.setMessage(getString(R.string.request_permission))
//				.setCancelable(false)
//				.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialogInterface, int i) {
//						ActivityCompat.requestPermissions((Activity) mContext, strings, REQUEST_PERMISSION);
//					}
//				})
//				.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_PERMISSION) {
            return;
        }
        if (grantResults.length > 0) {
            List<String> deniedPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permissions[i]);
                }
            }

            if (deniedPermissionList.isEmpty()) {
                System.out.println("已经全部授权!");
                SdkDelayInitUtil.getInstance().ClientId(MyApplication.getMyApplication());

                //已经全部授权
                start();

            } else {

                //勾选了对话框中”Don’t ask again”的选项, 返回false
                for (String deniedPermission : deniedPermissionList) {
                    boolean flag = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        flag = shouldShowRequestPermissionRationale(deniedPermission);
                    }
                    if (!flag) {
                        //拒绝授权
//                        permissionShouldShowRationale(deniedPermissionList);
//                        return;
                    }
                }
                //拒绝授权
                Toast.makeText(getApplicationContext(), getString(R.string.refuse_permission), Toast.LENGTH_SHORT).show();
//                permissionHasDenied(deniedPermissionList);
                start();

            }
        }

    }


}
package com.example.newag.mvp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.newag.R;


public class PermissionDialog extends Dialog {
    private Context context;
    private Click click;
    public PermissionDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }
    public PermissionDialog(@NonNull Context context, int themeResId, Click click){
        super(context,themeResId);
        this.context = context;
        this.click = click;
        initDialog();
    }

    private void initDialog(){
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_permission, null);
        root.findViewById(R.id.permission_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.click();
            }
        });
        this.setContentView(root);
        this.setCancelable(false);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        //<item name="android:windowFrame">@null</item>
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
//        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
    }

    public interface Click{
        void click();
    }
}

package com.example.newag.mvp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newag.R;
import com.example.newag.base.Constant;
import com.example.newag.utils.SharedPreferencesUtil;


public class WelcomeDialog extends Dialog {
    Context context;
    boolean isAdView;
    boolean showTip2;

    public WelcomeDialog(@NonNull Context context, agreeListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    protected WelcomeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public WelcomeDialog(@NonNull Context context, int themeResId, agreeListener listener) {
        super(context, themeResId);
        this.context = context;
        this.listener = listener;
    }
    
    public WelcomeDialog(@NonNull Context context, int themeResId, boolean isAdView, agreeListener listener) {
        super(context, themeResId);
        this.context = context;
        this.isAdView = isAdView;
        this.listener = listener;
    }

    private void setUpWindow() {
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        p.width = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(p);

    }


    Button button;

    ImageView closeBtn;
    TextView tv_agree,tv_disagree;

    TextView tv_intro;
    agreeListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpWindow();
        View view = View.inflate(getContext(), R.layout.dialog_welcome, null);
        setContentView(view);
        tv_intro = view.findViewById(R.id.tv_intro);
        SpannableStringBuilder spannableStringBuilder = tipStringStyle();
        if (spannableStringBuilder!=null){
            showTip2 = false;
            tv_intro.setText(spannableStringBuilder);
        }

        tv_agree = view.findViewById(R.id.tv_agree);
        tv_disagree = view.findViewById(R.id.tv_disagree);
        closeBtn = view.findViewById(R.id.iv_cancel);
        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.agree();
                dismiss();
            }
        });
        tv_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAdView) {
                    dismiss();
                    SharedPreferencesUtil.getInstance().putBoolean("welcomedialog",false).commit();
                } else if(!showTip2) {
                    setTip2Spannable();
                } else {//是adView但是图2
                    dismiss();
                    listener.disagreeAndFinish();
                }
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAdView) {
                    dismiss();
                    SharedPreferencesUtil.getInstance().putBoolean("welcomedialog",false).commit();
                } else if(!showTip2) {
                    setTip2Spannable();
                } else {
                    dismiss();
                    listener.disagreeAndFinish();
                }
            }
        });
    }

    private void setTip2Spannable() {
        SpannableStringBuilder spannableStringBuilder = tip2Styleable();
        if (tv_intro != null && spannableStringBuilder!=null){
            showTip2 = true;
            tv_intro.setText(spannableStringBuilder);
        }
        if(tv_disagree != null) {
            tv_disagree.setText("不同意，退出app");
        }
    }

    private SpannableStringBuilder tipStringStyle(){
        String tips = "感谢您信任并使用菜鱼共生!\n" +
                "我们非常重视您的隐私保护和个人信息保护。在您使用菜鱼共生提供的服务前，请务必认真的阅读《用户协议》和《隐私协议》条款。";

        final SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(tips);
        ClickableSpan egodleSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String privacy = Constant.PRIVACY_URL.replace("{qid}","72");
//                openBrowser
            }

            @Override
            public void updateDrawState(TextPaint ds) {
               // super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan kefuSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                String privacy = Constant.PRIVACY_URL.replace("{qid}","59");
//                openBrowser
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                // super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int start = tips.indexOf("《");
        int end = tips.indexOf("议")+2;
        int kefuStart = tips.indexOf("和", tips.indexOf("和") + 1)+1;//获取第一个he字之后的he
        int kefuend = tips.indexOf("议", tips.indexOf("议") + 1)+2;//获取第一个yi字之后的yi
        if (start!=-1 && end!=-1 && kefuStart!=-1 && kefuend!=-1){
            style.setSpan(egodleSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(kefuSpan,kefuStart,kefuend,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#66AFFB"));
            style.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan kefuForeground = new ForegroundColorSpan(Color.parseColor("#66AFFB"));
            style.setSpan(kefuForeground,kefuStart,kefuend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_intro.setMovementMethod(LinkMovementMethod.getInstance());
            tv_intro.setHighlightColor(Color.parseColor("#00000000"));
            return style;
        }
        return null;

    }
    private SpannableStringBuilder tip2Styleable() {
        String tips = "您的信息仅用于提供注册认证、内容分享等服务，未经您同意，我们不会从第三方获取或向第三方提供、共享您的信息。\n" +
                "您可以阅读并了解完整版《用户协议》和《隐私协议》条款。";

        final SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(tips);
        ClickableSpan egodleSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String privacy = Constant.PRIVACY_URL.replace("{qid}","72");
//                openBrowser
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                // super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan kefuSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                String privacy = Constant.PRIVACY_URL.replace("{qid}","59");
//                openBrowser
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                // super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int start = tips.indexOf("《");
        int end = tips.indexOf("议")+2;
        int kefuStart = tips.indexOf("和")+1;
        int kefuend = tips.indexOf("议", tips.indexOf("议") + 1)+2;
        if (start!=-1 && end!=-1 && kefuStart!=-1 && kefuend!=-1){
            style.setSpan(egodleSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(kefuSpan,kefuStart,kefuend,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#66AFFB"));
            style.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan kefuForeground = new ForegroundColorSpan(Color.parseColor("#66AFFB"));
            style.setSpan(kefuForeground,kefuStart,kefuend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv_intro.setMovementMethod(LinkMovementMethod.getInstance());
            tv_intro.setHighlightColor(Color.parseColor("#00000000"));
            return style;
        }
        return null;

    }
    public interface agreeListener{
        void agree();
        void disagreeAndFinish();
    }
}

package me.ufreedom.yummytextswitcher;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;


/**
 * Author SunMeng
 * Date : 2016 一月 20
 * 
 * 带有回弹动画的对话框，
 * 
 * 注意事项：
 * 
 * 1.设置当用户触发返回键时，是否销毁对话框，请使用{@link #setCancelable(boolean)}
 * 
 * 2.设置当用户触发对话框窗口边界外区域时，是否销毁对话框。请使用{@link #setCanceledOnTouchOutside(boolean)}}
 * 
 */
public abstract class ReboundAnimDialogFragment extends DialogFragment  {

    protected View dialogView;
    private View rootView;
    private Dialog mDialog;
    private boolean mCanceledOnTouchOutside = true;
    private boolean mAnimEnable = true;
    private Handler mHandler;
    private float mTranslateY;
    private SpringSystem mSpringSystem;
    private Spring mScaleAnimation;
    private Spring mTranslateAnimationSpring;
    private ValueAnimator mAlphaAnimation;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(R.style.DialogTheme, android.support.v4.app.DialogFragment.STYLE_NO_TITLE);
        mHandler = new Handler();
        configReboundAnim();
        
    }

    protected void configReboundAnim(){
        mSpringSystem = SpringSystem.create();
        mScaleAnimation = mSpringSystem.createSpring()
                .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(12, 12))
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        setScaleProgress((float) spring.getCurrentValue());
                    }
                });

        mTranslateAnimationSpring = mSpringSystem.createSpring()
                .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(12, 16))
                .addListener(new SimpleSpringListener() {
                    
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        setTranslateYProgress((float) spring.getCurrentValue());
                    }
                     }
                );
        
        mAlphaAnimation = ObjectAnimator.ofFloat(0.0f,1.0f);
        mAlphaAnimation.setDuration(100);
        mAlphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dialogView.setAlpha((Float) animation.getAnimatedValue());
                rootView.setAlpha((Float) animation.getAnimatedValue());
            }
        });
    }
    

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rebounce_dialog, container, false);
        view.setBackgroundColor(getWindowBackgroundColor());
        rootView = view.findViewById(R.id.rootView);
        dialogView = LayoutInflater.from(getActivity()).inflate(getDialogLayoutResId(), (ViewGroup) view,false);
        
        ((ViewGroup) view).addView(dialogView);
        dialogView.setAlpha(0f);
        rootView.setAlpha(0f);
        onFindView(dialogView);
        dialogView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dialogView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mTranslateY = (rootView.getMeasuredHeight() - dialogView.getMeasuredHeight()) / 2 + dialogView.getMeasuredHeight(); 
            }
        });
        

        onConfigDialog(getDialog());
        if (mCanceledOnTouchOutside){
            rootView.setOnTouchListener(new RootViewTouchListener());
            dialogView.setOnTouchListener(new DialogTouchListener());
        }
        return view;
    }
    

    

    /**
     * 
     * 设置当用户触发对话框窗口边界外区域时，是否销毁对话框。请勿在 {@link #onConfigDialog(Dialog) }中使用其参数设置此选项，否则
     * 会出现问题
     * 
     * Sets whether this dialog is canceled when touched outside the window's
     * bounds. If setting to true, the dialog is set to be cancelable if not
     * already set.
     *
     * @param cancel Whether the dialog should be canceled when touched outside
     *            the window.
     */
    public void setCanceledOnTouchOutside(boolean cancel) {
        mCanceledOnTouchOutside = cancel;
        mDialog.setCanceledOnTouchOutside(cancel);
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        mDialog.getWindow().setWindowAnimations(R.style.FadeAnimation);
        return mDialog;
    }


    /**
     * 可以配置Dialog
     * @param mDialog
     */
    protected void onConfigDialog(Dialog mDialog) {
        
    }

    
    public void  setAnimEnable(boolean enable){
        mAnimEnable = enable;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAnimEnable){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAlphaAnimation.start();
                    mTranslateAnimationSpring.setEndValue(1f);
                    mScaleAnimation.setEndValue(1f);
                }
            },100);
         
        }
    }
    
    

    private void setScaleProgress(float progress) {
        float transition = transition(progress, 0f, 1f);
        dialogView.setScaleX(transition);
        dialogView.setScaleY(transition);
    }

    private void setTranslateYProgress(float progress) {
        float transition = transition(progress, -mTranslateY,0);
        dialogView.setTranslationY(transition);
    }

    private float transition(float progress, float startValue, float endValue) {
        return (float) SpringUtil.mapValueFromRangeToRange(progress, 0, 1, startValue, endValue);
    }
    


    class RootViewTouchListener implements View.OnTouchListener{


        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    dismiss();
                    break;
            }
            return false;
        }
    }


    class DialogTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }
    
    

    public int getWindowBackgroundColor() {
        return Color.parseColor("#CC000000");
    }
    
    
    protected abstract void onFindView(View dialog);

    public abstract int getDialogLayoutResId();
    
    
}

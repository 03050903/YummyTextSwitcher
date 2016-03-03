package me.ufreedom.yummytextswitcher;

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

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
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
public abstract class ReboundAnimDialogFragment extends DialogFragment implements SpringListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final int mStartY = 300;
    public static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig
            .fromOrigamiTensionAndFriction(70, 6.5f);
    

    private Spring animSpring =  SpringSystem.create().createSpring()
            .setSpringConfig(ORIGAMI_SPRING_CONFIG)
            .addListener(this);

    protected View dialogView;
    private View rootView;
    private Dialog mDialog;
    private boolean mCanceledOnTouchOutside = true;
    private boolean mAnimEnable = true;
    private Handler mHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(R.style.DialogTheme, android.support.v4.app.DialogFragment.STYLE_NO_TITLE);
        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rebounce_dialog, container, false);
        view.setBackgroundColor(getWindowBackgroundColor());
        rootView = view.findViewById(R.id.rootView);
        dialogView = LayoutInflater.from(getActivity()).inflate(getDialogLayoutResId(), (ViewGroup) view,false);

        ViewTreeObserver vto2 = dialogView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(this);

        ((ViewGroup) view).addView(dialogView);
        onFindView(dialogView);

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
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();

        float selectedTitleScale = (float) SpringUtil.mapValueFromRangeToRange(
                value, 0, 1, 0, 1);
      /*  float titleTranslateY = (float) SpringUtil.mapValueFromRangeToRange(
                value, 0, 1, mStartDP, 0);*/
        dialogView.setScaleX(selectedTitleScale);
        dialogView.setScaleY(selectedTitleScale);
    }



    @Override
    public void onStart() {
        super.onStart();
        if (mAnimEnable){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (animSpring.getEndValue() == 1){
                        animSpring.setAtRest();
                    }else {
                        animSpring.setVelocity(8);
                        animSpring.setEndValue(1);   
                    }
                }
            },100);
           
        }
        
        

    }

    @Override
    public void onGlobalLayout() {

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
    
    

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }


    public int getWindowBackgroundColor() {
        return Color.parseColor("#CC000000");
    }
    
    
    protected abstract void onFindView(View dialog);

    public abstract int getDialogLayoutResId();
    
    
}

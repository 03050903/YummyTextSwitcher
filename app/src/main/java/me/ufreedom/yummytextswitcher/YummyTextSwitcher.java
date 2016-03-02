package me.ufreedom.yummytextswitcher;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Author SunMeng
 * Date : 2016 三月 01
 */
public class YummyTextSwitcher extends View {

    public static final int FRAME_NUMBER_MIDDLE = 30;
    public static final int FRAME_NUMBER_START = 3;
    public static final int FRAME_NUMBER_END = 3;


    private static final String TAG = "YummyTextSwitcher";
    private float mTextSize;
    private Paint mTextPaint;
    private FrameInterpolator mFrameInterpolator;

    private int mViewHeight;
    private int mViewWidth;
    private boolean isInit = false;

    private BlurMaskFilter mMaskFilterFirst;
    private BlurMaskFilter mMaskFilterSecond;
    private BlurMaskFilter mMaskFilterMiddle;
    private float scrollY = 0.0f;

    private Paint testPaint;

    private Paint mNormalPaint;
    private Paint mMiddleFramePaint;
    private Paint mFirstFramePaint;
    private Paint mSecondFramePaint;
    private Rect drawRect = new Rect();
    
    private float mFrameOffset;

    public YummyTextSwitcher(Context context) {
        super(context);
        init();
    }

    public YummyTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private void init() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //View从API Level 11才加入setLayerType方法
            //设置myView以软件渲染模式绘图
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFrameInterpolator = new DefaultFrameInterpolator();
        mTextSize = 80;
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalPaint.setTextSize(mTextSize);
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setTextSize(mTextSize);
        mNormalPaint.setColor(Color.BLACK);
        mNormalPaint.setStyle(Paint.Style.FILL);
        mNormalPaint.setTextAlign(Paint.Align.CENTER);


        mFirstFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstFramePaint.setTextSize(mTextSize);
        mFirstFramePaint.setAntiAlias(true);
        mFirstFramePaint.setTextSize(mTextSize);
        mFirstFramePaint.setColor(Color.BLACK);
        mFirstFramePaint.setStyle(Paint.Style.FILL);
        mFirstFramePaint.setTextAlign(Paint.Align.CENTER);

        mSecondFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondFramePaint.setTextSize(mTextSize);
        mSecondFramePaint.setAntiAlias(true);
        mSecondFramePaint.setTextSize(mTextSize);
        mSecondFramePaint.setColor(Color.BLACK);
        mSecondFramePaint.setStyle(Paint.Style.FILL);
        mSecondFramePaint.setTextAlign(Paint.Align.CENTER);

        mMiddleFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleFramePaint.setTextSize(mTextSize);
        mMiddleFramePaint.setAntiAlias(true);
        mMiddleFramePaint.setTextSize(mTextSize);
        mMiddleFramePaint.setColor(Color.BLACK);
        mMiddleFramePaint.setStyle(Paint.Style.FILL);
        mMiddleFramePaint.setTextAlign(Paint.Align.CENTER);


        mMaskFilterFirst = new BlurMaskFilter(3, BlurMaskFilter.Blur.NORMAL);
        mMaskFilterSecond = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        mMaskFilterMiddle = new BlurMaskFilter(25, BlurMaskFilter.Blur.NORMAL);


        mFirstFramePaint.setMaskFilter(mMaskFilterFirst);
        mSecondFramePaint.setMaskFilter(mMaskFilterSecond);
        mMiddleFramePaint.setMaskFilter(mMaskFilterMiddle);


        testPaint = new Paint();
        testPaint.setStyle(Paint.Style.FILL);
        testPaint.setColor(Color.WHITE);

        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();

        mFrameOffset = fmi.bottom - fmi.top;

    }
    
    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {

            if (heightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(staticLayout.getWidth(), staticLayout.getHeight());
            } else if (heightMode == MeasureSpec.EXACTLY) {
                setMeasuredDimension(staticLayout.getWidth(), Math.max(heightSize, staticLayout.getHeight()));
            }

        } else if (widthMode == MeasureSpec.EXACTLY) {
            if (heightMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(Math.max(widthSize, staticLayout.getWidth()), staticLayout.getHeight());
            } else if (heightMode == MeasureSpec.EXACTLY) {
                setMeasuredDimension(Math.max(widthSize, staticLayout.getWidth()), Math.max(heightSize, staticLayout.getHeight()));
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        mTextSize = Math.min(mViewWidth, mViewHeight) / 4.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawInit(canvas);
    }

    private void drawInit(Canvas canvas) {


        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0);
        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));


        mTextPaint.setMaskFilter(null);

        drawRect.left = 0;
        drawRect.top = (getHeight() - getWidth()) / 2;
        drawRect.right = getWidth();
        drawRect.bottom = drawRect.top + getWidth();
        canvas.drawRect(drawRect, testPaint);
        
        
        canvas.drawText(mFrameInterpolator.getStartFrame(0), x, baseline + scrollY, mTextPaint);
        canvas.drawText(mFrameInterpolator.getStartFrame(1), x, baseline + mFrameOffset + scrollY, mFirstFramePaint);
        canvas.drawText(mFrameInterpolator.getStartFrame(2), x, baseline + mFrameOffset * 2 + scrollY, mSecondFramePaint);


        for (int i = 0; i < FRAME_NUMBER_MIDDLE; i++) {
            canvas.drawText(mFrameInterpolator.getMiddleFrame(i * 1.0f / FRAME_NUMBER_MIDDLE), x, baseline + mFrameOffset * (3 + i) + scrollY, mMiddleFramePaint);
        }

        canvas.drawText(mFrameInterpolator.getEndFrame(0), x, baseline + mFrameOffset * (FRAME_NUMBER_MIDDLE + 3) + scrollY, mSecondFramePaint);
        canvas.drawText(mFrameInterpolator.getEndFrame(1), x, baseline + mFrameOffset * (FRAME_NUMBER_MIDDLE + 4) + scrollY, mFirstFramePaint);
        canvas.drawText(mFrameInterpolator.getEndFrame(2), x, baseline + mFrameOffset * (FRAME_NUMBER_MIDDLE + 5) + scrollY, mTextPaint);

    }
    
    public void startAnim() {
        
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(this, "scrollY", 0, - mFrameOffset * 8);
        anim2.setInterpolator(new AccelerateInterpolator(5));
        anim2.setDuration(500);

        ObjectAnimator anim3 = ObjectAnimator.ofFloat(this, "scrollY", - mFrameOffset * 8, -mFrameOffset * (FRAME_NUMBER_MIDDLE + FRAME_NUMBER_START + FRAME_NUMBER_END - 1));
        anim3.setInterpolator(new OvershootInterpolator(0.45f));
        anim3.setDuration(1000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(anim2, anim3);
        animatorSet.start();

    }


    public void setScrollY(float scrollY) {
        this.scrollY = scrollY;
        invalidate();
    }
    

    private void printLog(String log) {
        Log.e(TAG, String.format("---->  %s", log));
    }

    public interface FrameInterpolator {

        String getStartFrame(int input);

        String getMiddleFrame(float input);

        String getEndFrame(int input);
    }
    

    public class DefaultFrameInterpolator implements FrameInterpolator {


        public DefaultFrameInterpolator() {

        }

        @Override
        public String getStartFrame(int input) {
            if (input == 0) {
                return "1";
            } else if (input == 1) {
                return "2";
            } else {
                return "3";
            }
        }

        @Override
        public String getMiddleFrame(float input) {

            if (input <= 0.4f) {
                return "88";
            } else if (input <= 0.6f) {
                return "888";
            } else {
                return "8888";
            }
        }

        @Override
        public String getEndFrame(int input) {
            if (input == 0) {
                return "2334";
            } else if (input == 1) {
                return "2335";
            } else {
                return "2336";
            }
        }
    }


}

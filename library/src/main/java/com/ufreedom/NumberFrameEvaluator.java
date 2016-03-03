package com.ufreedom;

/**
 * Author SunMeng
 * Date : 2016 三月 02
 */
public class NumberFrameEvaluator implements FrameEvaluator {

    private int mStart;
    private int mEnd;
    private String[] middles;

    public NumberFrameEvaluator(int start, int end) {
        this.mStart = start;
        this.mEnd = end;
        middles = new String[2];
        
    }
    
    @Override
    public String getStartFrame(int input) {
        if (input == 0) {
            return "" + mStart;
        } else if (input == 1) {
            return "" + (mStart + 1);
        } else {
            return "" + (mStart + 2);
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
            return "" + (mEnd - 2);
        } else if (input == 1) {
            return "" + (mEnd - 1);
        } else {
            return "" + mEnd;
        }
    }
}

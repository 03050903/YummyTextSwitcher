package com.ufreedom;

/**
 * Author SunMeng
 * Date : 2016 三月 02
 */
public class NumberFrameInterpolator implements FrameInterpolator {


    public NumberFrameInterpolator() {

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

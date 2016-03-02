package com.ufreedom;

/**
 * Author SunMeng
 * Date : 2016 三月 02
 */
public interface FrameInterpolator {

    String getStartFrame(int input);

    String getMiddleFrame(float input);

    String getEndFrame(int input);
}

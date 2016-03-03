package me.ufreedom.yummytextswitcher;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.view.View;

import com.ufreedom.NumberFrameEvaluator;
import com.ufreedom.YummyTextSwitcher;

/**
 * Author SunMeng
 * Date : 2016 三月 02
 */
public class NumberAnimDialog extends ReboundAnimDialogFragment {

    YummyTextSwitcher yummyTextSwitcher;

    
    public static void showDialog(Activity activity){
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("NumberAnimDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        NumberAnimDialog dialogFragment =  new NumberAnimDialog();
        dialogFragment.show(ft, "NumberAnimDialog");
    }

    @Override
    protected void onFindView(View dialog) {
        yummyTextSwitcher = (YummyTextSwitcher) dialog.findViewById(R.id.yummyTextSwitcher);
        yummyTextSwitcher.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeueLTPro.otf"));
        yummyTextSwitcher.setFrameInterpolator(new NumberFrameEvaluator(1,2345));
        yummyTextSwitcher.startAnim();
    }

    
    @Override
    public int getDialogLayoutResId() {
        return R.layout.fragment_number_anim;
    }
}

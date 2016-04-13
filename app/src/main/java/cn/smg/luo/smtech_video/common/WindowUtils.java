package cn.smg.luo.smtech_video.common;

import android.content.Context;
import android.content.res.Resources;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * @author jl_luo
 * @name: cn.smg.luo.smtech_video.common
 * @description:
 * @date 2016/3/31 17:31
 */

public class WindowUtils {
//    public static int getStatusBarHeight(Context context) {
//        Class<?> c = null;
//         Object obj = null;
//         Field field = null;
//        int x = 0, statusBarHeight = 0;
//        try {
//               c = Class.forName("com.android.internal.R$dimen");
//               obj = c.newInstance();
//               field = c.getField("status_bar_height");
//                x = Integer.parseInt(field.get(obj).toString());
//                statusBarHeight = context.getResources().getDimensionPixelSize(x);
//          } catch (Exception e1) {
//               e1.printStackTrace();
//            }
//       return statusBarHeight;
//   }

    /**
     *  A method to find height of the status bar
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * This snippet hides the system bars.
     * @param mDecorView
     */
    public static void hideSystemUI(View mDecorView) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * This snippet shows the system bars. It does this by removing all the flags
     *except for the ones that make the content appear under the system bars.
     * @param mDecorView
     */
    public static void showSystemUI(View mDecorView) {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * 判断是否有navigationBar
     * @param activity 上下文对象
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context activity) {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}

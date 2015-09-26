package bingyan.net.localphoto;

import android.app.Application;
import android.util.DisplayMetrics;

/**
 * Created by Demon on 2015/7/17.
 *
 */
public class MyApplication  extends Application{

    private static DisplayMetrics dm = new DisplayMetrics();

    public static DisplayMetrics getDisplayMetrics(){
        return dm;
    }
}

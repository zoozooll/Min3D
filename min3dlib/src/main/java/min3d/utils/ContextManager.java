package min3d.utils;

import android.content.Context;

/**
 * Created by sw on 2016/11/8.
 */

public class ContextManager {

    private static Context sContext;

    public static void setContext(Context c) {
        sContext = c;
    }

    public static Context getContext() {
        return sContext;
    }

    public static void removeContext () {
        sContext = null;
    }
}

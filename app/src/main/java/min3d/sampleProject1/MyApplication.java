package min3d.sampleProject1;

import android.app.Application;

import min3d.utils.ContextManager;

/**
 * Created by sw on 2016/11/8.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextManager.setContext(this);
    }
}

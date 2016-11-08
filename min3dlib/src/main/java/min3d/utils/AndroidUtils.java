package min3d.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sw on 2016/10/9.
 */

public class AndroidUtils {


    public static String readRawTextFile(Context context, int resId) {
        InputStream is = context.getResources().openRawResource(resId);
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader buf = new BufferedReader(reader);
        StringBuilder text = new StringBuilder();
        try {
            String line;
            while ((line = buf.readLine()) != null) {
                text.append(line).append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

    public static Bitmap loadImageAssets(Context context, String ImgName) {
        Bitmap bmp = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(context.getAssets().open(
                    ImgName));
            bmp = BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bmp;
    }

    public static Bitmap loadImageRes(Resources res, int rid) {
        return BitmapFactory.decodeResource(res, rid);
    }
}

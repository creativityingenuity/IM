package com.gxs.im.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public class ToastUtils {
    private static Toast toast;
    public static void showToast(Context context,String msg){
        if (toast==null){
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        //如果这个Toast已经在显示了，那么这里会立即修改文本
        toast.setText(msg);
        toast.show();
    }
}

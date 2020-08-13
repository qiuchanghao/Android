package com.example.libmail.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionsUtils {
    public final static int CODE = 200;

    public static void requestPermission(Activity activity, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(permissions, CODE);
                }
            }
        }
    }
}

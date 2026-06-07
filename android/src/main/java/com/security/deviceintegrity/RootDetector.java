package com.security.deviceintegrity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.util.List;

public final class RootDetector {

    private static final String TAG = "DeviceIntegrity";

    private static final int SCORE_SU_BINARY = 5;
    private static final int SCORE_TEST_KEYS = 2;
    private static final int SCORE_ROOT_APP = 5;

    private RootDetector() {
    }

    public static int detect(
            Context context,
            List<String> reasons
    ) {

        int score = 0;

        String[] suPaths = {
                "/system/bin/su",
                "/system/xbin/su",
                "/sbin/su",
                "/vendor/bin/su",
                "/system_ext/bin/su",
                "/data/local/tmp/su"
        };

        for (String path : suPaths) {

            if (new File(path).exists()) {

                reasons.add("SU_BINARY:" + path);

                score += SCORE_SU_BINARY;
            }
        }

        if (Build.TAGS != null
                && Build.TAGS.contains("test-keys")) {

            reasons.add("TEST_KEYS");

            score += SCORE_TEST_KEYS;
        }

        String[] rootApps = {
                "com.topjohnwu.magisk",
                "eu.chainfire.supersu",
                "com.koushikdutta.superuser",
                "com.thirdparty.superuser"
        };

        PackageManager pm =
                context.getPackageManager();

        for (String app : rootApps) {

            try {

                pm.getPackageInfo(app, 0);

                reasons.add("ROOT_APP:" + app);

                score += SCORE_ROOT_APP;

            } catch (
                    PackageManager.NameNotFoundException ex
            ) {

                Log.d(
                        TAG,
                        "Package not installed: " + app
                );
            }
        }

        return score;
    }
}
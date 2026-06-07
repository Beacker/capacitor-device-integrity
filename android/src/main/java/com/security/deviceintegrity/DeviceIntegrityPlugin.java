package com.security.deviceintegrity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.provider.Settings;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONArray;

import java.io.File;
import java.util.List;

@CapacitorPlugin(name = "DeviceIntegrity")
public class DeviceIntegrityPlugin extends Plugin {

    private static final int ROOT_THRESHOLD = 5;
    private static final int EMULATOR_THRESHOLD = 5;
    private static final int FRIDA_THRESHOLD = 10;

    private static final int SCORE_LOW = 1;
    private static final int SCORE_MEDIUM = 5;

    @PluginMethod
    public void checkIntegrity(
            PluginCall call
    ) {

        IntegrityResult result =
                new IntegrityResult();

        result.setRootScore(
                RootDetector.detect(
                        getContext(),
                        result.getRootReasons()
                )
        );

        result.setFridaScore(
                FridaDetector.detect(
                        result.getFridaReasons()
                )
        );

        result.setEmulatorScore(
                detectEmulator(
                        result.getEmulatorReasons()
                )
        );

        result.setRooted(
                result.getRootScore()
                        >= ROOT_THRESHOLD
        );

        result.setFridaDetected(
                result.getFridaScore()
                        >= FRIDA_THRESHOLD
        );

        result.setEmulator(
                result.getEmulatorScore()
                        >= EMULATOR_THRESHOLD
        );

        JSObject response =
                new JSObject();

        response.put(
                "isRooted",
                result.isRooted()
        );

        response.put(
                "isEmulator",
                result.isEmulator()
        );

        response.put(
                "isFridaDetected",
                result.isFridaDetected()
        );

        response.put(
                "rootScore",
                result.getRootScore()
        );

        response.put(
                "emulatorScore",
                result.getEmulatorScore()
        );

        response.put(
                "fridaScore",
                result.getFridaScore()
        );

        response.put(
                "rootReasons",
                new JSONArray(
                        result.getRootReasons()
                )
        );

        response.put(
                "emulatorReasons",
                new JSONArray(
                        result.getEmulatorReasons()
                )
        );

        response.put(
                "fridaReasons",
                new JSONArray(
                        result.getFridaReasons()
                )
        );

        call.resolve(response);
    }

    private int detectEmulator(
            List<String> reasons
    ) {

        int score = 0;

        if (
                (Build.FINGERPRINT != null
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.contains("emulator")))
                        || (Build.MODEL != null
                        && (Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for")))
                        || (Build.MANUFACTURER != null
                        && Build.MANUFACTURER.contains("Genymotion"))
                        || (Build.BRAND != null
                        && Build.BRAND.startsWith("generic"))
                        || (Build.DEVICE != null
                        && Build.DEVICE.startsWith("generic"))
                        || "google_sdk".equals(Build.PRODUCT)
        ) {

            reasons.add(
                    "BUILD_PROPERTIES"
            );

            score += SCORE_MEDIUM;
        }

        if (Build.HARDWARE != null
                && Build.HARDWARE.contains(
                "goldfish"
        )) {

            reasons.add("GOLDFISH");

            score += SCORE_MEDIUM;
        }

        if (Build.HARDWARE != null
                && Build.HARDWARE.contains(
                "ranchu"
        )) {

            reasons.add("RANCHU");

            score += SCORE_MEDIUM;
        }

        String[] emulatorFiles = {
                "/dev/socket/qemud",
                "/dev/qemu_pipe",
                "/system/bin/qemu-props"
        };

        for (String file :
                emulatorFiles) {

            if (
                    new File(file).exists()
            ) {

                reasons.add(
                        "QEMU_FILE:" + file
                );

                score += SCORE_MEDIUM;
            }
        }

        if (
                Build.SUPPORTED_ABIS.length > 0
        ) {

            String abi =
                    Build.SUPPORTED_ABIS[0];

            if (
                    abi.contains("x86")
            ) {

                reasons.add("X86_ABI");

                score += SCORE_LOW;
            }
        }

        SensorManager sm =
                (SensorManager)
                        getContext()
                                .getSystemService(
                                        Context.SENSOR_SERVICE
                                );

        if (sm != null) {

            Sensor gyro =
                    sm.getDefaultSensor(
                            Sensor.TYPE_GYROSCOPE
                    );

            if (gyro == null) {

                reasons.add(
                        "NO_GYROSCOPE"
                );

                score += SCORE_LOW;
            }
        }

        boolean adbEnabled =
                Settings.Global.getInt(
                        getContext()
                                .getContentResolver(),
                        Settings.Global.ADB_ENABLED,
                        0
                ) == 1;

        if (adbEnabled) {

            reasons.add(
                    "ADB_ENABLED"
            );
        }

        return score;
    }
}
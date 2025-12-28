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
import java.util.ArrayList;
import java.util.List;

@CapacitorPlugin(name = "DeviceIntegrity")
public class DeviceIntegrityPlugin extends Plugin {

    @PluginMethod
    public void detectEmulator(PluginCall call) {
        List<String> reasons = new ArrayList<>();

        // 1️⃣ Build properties (muy fuerte)
        if (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.contains("emulator")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic")
                || Build.DEVICE.startsWith("generic")
                || "google_sdk".equals(Build.PRODUCT)) {
            reasons.add("BUILD_PROPERTIES");
        }

        // 2️⃣ Archivos QEMU
        String[] emulatorFiles = {
                "/dev/socket/qemud",
                "/dev/qemu_pipe",
                "/system/lib/libc_malloc_debug_qemu.so",
                "/sys/qemu_trace",
                "/system/bin/qemu-props"
        };

        for (String file : emulatorFiles) {
            if (new File(file).exists()) {
                reasons.add("QEMU_FILE:" + file);
            }
        }

        // 3️⃣ ABI x86
        if (Build.SUPPORTED_ABIS.length > 0) {
            String abi = Build.SUPPORTED_ABIS[0];
            if (abi.contains("x86")) {
                reasons.add("X86_ABI");
            }
        }

        // 4️⃣ Sensores (emuladores suelen tener pocos)
        SensorManager sm = (SensorManager)
                getContext().getSystemService(Context.SENSOR_SERVICE);

        if (sm != null) {
            List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);
            if (sensors.size() < 10) {
                reasons.add("LOW_SENSOR_COUNT");
            }
        }

        // 5️⃣ ADB activo
        boolean adbEnabled =
                Settings.Global.getInt(
                        getContext().getContentResolver(),
                        Settings.Global.ADB_ENABLED,
                        0
                ) == 1;

        if (adbEnabled) {
            reasons.add("ADB_ENABLED");
        }

        // Resultado
        JSObject result = new JSObject();
        result.put("isEmulator", !reasons.isEmpty());
        result.put("reasons", new JSONArray(reasons));

        call.resolve(result);
    }
}

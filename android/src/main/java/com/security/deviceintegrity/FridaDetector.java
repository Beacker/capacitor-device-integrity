package com.security.deviceintegrity;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public final class FridaDetector {

    private static final String TAG =
            "DeviceIntegrity";

    private static final int SCORE_PORT = 10;
    private static final int SCORE_MAPS = 15;
    private static final int SCORE_DEBUGGER = 5;

    private FridaDetector() {
    }

    public static int detect(
            List<String> reasons
    ) {

        int score = 0;

        if (isPortOpen(27042)) {

            reasons.add("FRIDA_PORT_27042");

            score += SCORE_PORT;
        }

        if (isPortOpen(27043)) {

            reasons.add("FRIDA_PORT_27043");

            score += SCORE_PORT;
        }

        if (detectMaps(reasons)) {

            score += SCORE_MAPS;
        }

        if (detectTracerPid(reasons)) {

            score += SCORE_DEBUGGER;
        }

        return score;
    }

    private static boolean isPortOpen(
            int port
    ) {

        try (Socket socket =
                     new Socket()) {

            socket.connect(
                    new InetSocketAddress(
                            "127.0.0.1",
                            port
                    ),
                    200
            );

            return true;

        } catch (IOException ex) {

            Log.d(
                    TAG,
                    "Port closed: " + port
            );

            return false;
        }
    }

    private static boolean detectMaps(
            List<String> reasons
    ) {

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(
                                        "/proc/self/maps"
                                )
                        )
        ) {

            String line;

            while (
                    (line = reader.readLine())
                            != null
            ) {

                String lower =
                        line.toLowerCase();

                if (
                        lower.contains("frida")
                                || lower.contains("gum-js-loop")
                                || lower.contains("gmain")
                                || lower.contains("linjector")
                ) {

                    reasons.add(
                            "FRIDA_MAPS"
                    );

                    return true;
                }
            }

        } catch (IOException ex) {

            Log.e(
                    TAG,
                    "Unable to read /proc/self/maps",
                    ex
            );
        }

        return false;
    }

    private static boolean detectTracerPid(
            List<String> reasons
    ) {

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(
                                        "/proc/self/status"
                                )
                        )
        ) {

            String line;

            while (
                    (line = reader.readLine())
                            != null
            ) {

                if (
                        line.startsWith(
                                "TracerPid:"
                        )
                ) {

                    String value =
                            line.replace(
                                    "TracerPid:",
                                    ""
                            ).trim();

                    int pid =
                            Integer.parseInt(
                                    value
                            );

                    if (pid > 0) {

                        reasons.add(
                                "DEBUGGER_ATTACHED"
                        );

                        return true;
                    }
                }
            }

        } catch (Exception ex) {

            Log.e(
                    TAG,
                    "Unable to read TracerPid",
                    ex
            );
        }

        return false;
    }
}
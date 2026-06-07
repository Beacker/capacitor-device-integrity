package com.security.deviceintegrity;

import java.util.ArrayList;
import java.util.List;

public class IntegrityResult {

    private boolean rooted;
    private boolean emulator;
    private boolean fridaDetected;

    private int rootScore;
    private int emulatorScore;
    private int fridaScore;

    private final List<String> rootReasons = new ArrayList<>();
    private final List<String> emulatorReasons = new ArrayList<>();
    private final List<String> fridaReasons = new ArrayList<>();

    public boolean isRooted() {
        return rooted;
    }

    public void setRooted(boolean rooted) {
        this.rooted = rooted;
    }

    public boolean isEmulator() {
        return emulator;
    }

    public void setEmulator(boolean emulator) {
        this.emulator = emulator;
    }

    public boolean isFridaDetected() {
        return fridaDetected;
    }

    public void setFridaDetected(boolean fridaDetected) {
        this.fridaDetected = fridaDetected;
    }

    public int getRootScore() {
        return rootScore;
    }

    public void setRootScore(int rootScore) {
        this.rootScore = rootScore;
    }

    public int getEmulatorScore() {
        return emulatorScore;
    }

    public void setEmulatorScore(int emulatorScore) {
        this.emulatorScore = emulatorScore;
    }

    public int getFridaScore() {
        return fridaScore;
    }

    public void setFridaScore(int fridaScore) {
        this.fridaScore = fridaScore;
    }

    public List<String> getRootReasons() {
        return rootReasons;
    }

    public List<String> getEmulatorReasons() {
        return emulatorReasons;
    }

    public List<String> getFridaReasons() {
        return fridaReasons;
    }
}
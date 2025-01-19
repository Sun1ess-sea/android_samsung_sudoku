package com.example.sudokuapp;

import android.util.Log;

public class Session {
    private static final String TAG = "Session";

    private static Session instance;
    private String username;
    private String level;
    private String time;

    private Session() { }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void logCurrentState() {
        Log.d(TAG, "Session State");
        Log.d(TAG, "Username: " + (username != null ? username : "NOT SET"));
        Log.d(TAG, "Level: " + (level != null ? level : "NOT SET"));
        Log.d(TAG, "Time: " + time);
        System.out.println("Session State: Username = " + username + ", Level = " + level + ", Time = " + time);
    }
}

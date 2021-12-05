package com.example.practicafinal;

import android.app.Application;

public class GlobalClass extends Application {
    String algo="ON";

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }
}

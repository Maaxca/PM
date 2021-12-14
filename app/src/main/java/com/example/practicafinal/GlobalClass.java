package com.example.practicafinal;

import android.app.Application;

public class GlobalClass extends Application {
    String algo="ON";
    String algo2="OFF";

    public String getAlgo2() {
        return algo2;
    }

    public void setAlgo2(String algo2) {
        this.algo2 = algo2;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }
}

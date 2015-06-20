package com.vinay.deviceid;

public class Band {
    private int frequency;
    private int band;
    private String technology;

    public Band(int frequency, int band, String technology) {
        this.frequency = frequency;
        this.band = band;
        this.technology = technology;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}

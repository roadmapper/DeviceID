package com.vinay.deviceid;

public class Band {
    private String frequency;
    private int band;
    private String technology;

    // For GSM
    public Band(String frequency, String technology) {
        this.frequency = frequency;
        this.band = 0;
        this.technology = technology;
    }

    // For UMTS/LTE
    public Band(String frequency, int band, String technology) {
        this.frequency = frequency;
        this.band = band;
        this.technology = technology;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Band)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Band b = (Band) obj;
        if (b.getBand() == this.getBand() && b.getFrequency().equals(this.getFrequency()) && b.getTechnology().equals(this.getTechnology()))
            return true;
        return false;
    }
}

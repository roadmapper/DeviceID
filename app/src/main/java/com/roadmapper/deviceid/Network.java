package com.roadmapper.deviceid;

public class Network {
    private String operator;
    private String country;
    private Band band;

    public Network(String operator, String country, Band band) {
        this.operator = operator;
        this.country = country;
        this.band = band;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public String toString() {
        return this.getOperator() + ", " + this.getCountry() + ", " + this.getBand();
    }

}

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Network)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Network n = (Network) obj;
        if (n.getBand().equals(this.getBand()) && n.getCountry().equals(this.getCountry()) && n.getOperator().equals(this.getOperator()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return 47 * (this.band.hashCode() + this.country.hashCode() + this.operator.hashCode());
    }

    public String toString() {
        return this.getOperator() + ", " + this.getCountry() + ", " + this.getBand();
    }

}

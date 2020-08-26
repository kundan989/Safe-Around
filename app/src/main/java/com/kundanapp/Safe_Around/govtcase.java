package com.kundanapp.Safe_Around;

public class govtcase {
    private String Name;
    private Double Lattitude;
    private Double Longitude;

    public govtcase() {
    }

    public govtcase(String name, Double lattitude, Double longitude) {
        Name = name;
        Lattitude = lattitude;
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getLattitude() {
        return Lattitude;
    }

    public void setLattitude(Double lattitude) {
        Lattitude = lattitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
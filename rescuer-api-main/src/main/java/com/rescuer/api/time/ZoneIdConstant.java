package com.rescuer.api.time;

public enum ZoneIdConstant {
    ASIA_KOLKATA("Asia/Kolkata"),
    CENTRAL_EUROPE("Europe/Paris"),
    UTC("UTC");

    private String name;

    ZoneIdConstant(String zoneName) {
        this.name = zoneName;
    }

    public static ZoneIdConstant toZoneIdConstant(final String zoneId) {
        if(zoneId == ZoneIdConstant.ASIA_KOLKATA.value()) {
            return ZoneIdConstant.ASIA_KOLKATA;
        } else if(zoneId == ZoneIdConstant.CENTRAL_EUROPE.value()) {
            return ZoneIdConstant.CENTRAL_EUROPE;
        }
        return ZoneIdConstant.UTC; // handle null pointer exception
    }

    public String value() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
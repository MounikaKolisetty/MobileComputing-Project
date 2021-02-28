package com.rescuer.api.time;

public class SystemZoneDateTime {

    private static ZoneIdHolder zoneIdHolder = new ZoneIdHolder();

    public static ZoneIdHolder getZoneIdHolder() {
        return zoneIdHolder;
    }

}

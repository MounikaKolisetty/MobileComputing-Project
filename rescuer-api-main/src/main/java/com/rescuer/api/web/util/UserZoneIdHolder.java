package com.rescuer.api.web.util;

import com.rescuer.api.time.ZoneIdConstant;
import com.rescuer.api.time.ZoneIdHolder;

public class UserZoneIdHolder {

    private static final ThreadLocal<ZoneIdHolder> USER_ZONE_ID_HOLDER = new ThreadLocal<>();

    public static ZoneIdHolder getZoneIdHolder() {
        return USER_ZONE_ID_HOLDER.get();
    }

    public static void setUserZoneIdHolder(ZoneIdConstant zoneIdConstant) {
        USER_ZONE_ID_HOLDER.set(new ZoneIdHolder(zoneIdConstant));
    }
}

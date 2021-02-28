package com.rescuer.api.util;

public interface ChangeLogDetails {

    String getOnSaveChangeLogMessage();

    String getOnUpdateChangeLogMessage();

    String getOnDeleteChangeLogMessage();
}

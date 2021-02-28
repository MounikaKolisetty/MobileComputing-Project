package com.rescuer.api.eventhandler;

import com.rescuer.api.util.ChangeLogDetails;
import com.rescuer.api.util.EntityUniqueIdentifier;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class AuditEventHandler {

    @PostPersist
    public void postPersist(Object entity) {
        if(entity instanceof ChangeLogDetails && entity instanceof EntityUniqueIdentifier) {

        }
    }

    @PostUpdate
    public void postUpdate(Object entity) {
        if(entity instanceof  ChangeLogDetails && entity instanceof EntityUniqueIdentifier) {

        }
    }

    @PostRemove
    public void postDelete(Object entity) {
        if(entity instanceof  ChangeLogDetails && entity instanceof EntityUniqueIdentifier) {

        }
    }
}

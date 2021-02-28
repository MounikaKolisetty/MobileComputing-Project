package com.rescuer.api.dtos;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TicketId {

    private String ticketId;

    public TicketId(String ticketId) {
        this.ticketId = ticketId.toString();
    }

    public TicketId(UUID ticketId) {
        this(ticketId.toString());
    }

}

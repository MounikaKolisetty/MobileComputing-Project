package com.rescuer.api.service;

import com.rescuer.api.util.LogIntoTicketHolder;
import com.rescuer.api.util.TicketHolder;
import org.springframework.stereotype.Component;

@Component
public class TicketHelper {

    @LogIntoTicketHolder
    public TicketHolder allocateTicketToRescuer(String ticketId) {
        return TicketHolder
                .builder()
                .ticketId(ticketId).build();
    }
}

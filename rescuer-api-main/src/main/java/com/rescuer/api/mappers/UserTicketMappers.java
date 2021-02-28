package com.rescuer.api.mappers;

import com.rescuer.api.entity.Ticket;
import com.rescuer.api.entity.UserTicketStats;
import com.rescuer.api.web.dto.GetTicketDetailsDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

@Slf4j
public class UserTicketMappers {

    public static Optional<GetTicketDetailsDTO> mapToGetTicketDetailsDTO(UserTicketStats userTicketStats) {
        log.debug("Request received for mapToGetTicketDetailsDTO.");
        Set<Ticket> associatedTickets = userTicketStats.getTicketsAssociated();
        if(associatedTickets.size() > 0) {
            Ticket ticket = associatedTickets.iterator().next();
            GetTicketDetailsDTO getTicketDetailsDTO = TicketMappers.mapToGetTicketDetails.apply(ticket);
            return Optional.ofNullable(getTicketDetailsDTO);
        }
        log.info("Associated tickets are empty");
        return Optional.empty();
    }
}

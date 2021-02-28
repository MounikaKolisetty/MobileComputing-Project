package com.rescuer.api.mappers;

import com.rescuer.api.entity.*;
import com.rescuer.api.time.LocalDateTimeParser;
import com.rescuer.api.web.dto.*;
import com.rescuer.api.web.util.UserZoneIdHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.function.Function;

@Slf4j
public class TicketMappers {

    public static final Function<CreateTicketDTO, Ticket> mapToTicket = createTicketDTO -> {
        log.debug("In mapping CreateTicketDTO to Ticket");
        CreateVictimDTO createVictimDetailsDTO = createTicketDTO.getVictimDetails();
        CreateEmergencyContactDTO createEmergencyContactDTO = createTicketDTO.getEmergencyDetails();

        Victim victimDetails = Victim
                .builder()
                .victimAge(createVictimDetailsDTO.getVictimAge())
                .victimName(createVictimDetailsDTO.getVictimName())
                .attachments(Collections.emptySet())
                .reportedLocation(
                        String.format("%s:%s",createVictimDetailsDTO.getLatitude(), createVictimDetailsDTO.getLongitude()))
                .requiredItems(createVictimDetailsDTO.getRequiredItems())
                .build();
        log.debug("Setting Victim details {}", victimDetails);

        EmergencyContact emergencyContactDetails = EmergencyContact
                .builder()
                .emergencyContactAge(createEmergencyContactDTO.getEmergencyContactAge())
                .emergencyContactName(createEmergencyContactDTO.getEmergencyContactName())
                .emergencyAddress(createEmergencyContactDTO.getEmergencyAddress())
                .emergencyPhoneNumber(createEmergencyContactDTO.getEmergencyPhoneNumber())
                .build();
        log.debug("Setting EmergencyContact details {}", emergencyContactDetails);

        TicketDetails ticketDetails = TicketDetails
                .builder()
                .victim(victimDetails)
                .emergencyContact(emergencyContactDetails)
                .additionalInformation(createTicketDTO.getAdditionalInformation())
                .victimsAroundCount(createTicketDTO.getVictimsAround())
                .build();
        log.debug("Setting ticketDetails {}", ticketDetails);

        log.debug("Building Ticket object and returning");
        return Ticket
                .builder()
                .ticketStatus(TicketStatus.OPEN)
                .allocatedTo(null)
                .ticketDetails(ticketDetails)
                .build();
    };

    public static final Function<Ticket, GetAllTicketsDTO> mapToGetAllTickets = ticket -> {
        log.debug("In mapping ticket to GetAllTicketsDTO");
        Instant createdAtInstant = ticket.getCreatedAt();
        LocalDateTime createdAtLocalDateTime = UserZoneIdHolder.getZoneIdHolder().getLocalDateTime(createdAtInstant);
        log.debug("Got createdAt user's zone specific LocalDateTime");
        return GetAllTicketsDTO.builder()
                .allocatedTo(ObjectUtils.isEmpty(ticket.getAllocatedTo()) ? null : ticket.getAllocatedTo().getUsername())
        .requiredItems(ticket.getTicketDetails().getVictim().getRequiredItems())
        .ticketStatus(ticket.getTicketStatus().name())
        .createdAt(LocalDateTimeParser.toUserFriendlyLocalDateTime(createdAtLocalDateTime))
        .ticketId(ticket.getUniqueIdentifier().toString()).build();
    };

    public static final Function<Ticket, GetTicketDetailsDTO> mapToGetTicketDetails = ticket -> {
        log.debug("In mapping ticket to GetTicketDetailsDTO");
        Instant createdAtInstant = ticket.getCreatedAt();
        LocalDateTime createdAtLocalDateTime = UserZoneIdHolder.getZoneIdHolder().getLocalDateTime(createdAtInstant);

        TicketDetails ticketDetails = ticket.getTicketDetails();
        EmergencyContact emergencyContactDetails = ticket.getTicketDetails().getEmergencyContact();
        Victim victimDetails = ticketDetails.getVictim();

        log.debug("Building GetEmergencyContactDetails");
        GetEmergencyContactDetails getEmergencyContactDetails = GetEmergencyContactDetails.builder()
                .emergencyContactAge(emergencyContactDetails.getEmergencyContactAge())
                .emergencyContactName(emergencyContactDetails.getEmergencyContactName())
                .emergencyAddress(emergencyContactDetails.getEmergencyAddress())
                .emergencyPhoneNumber(emergencyContactDetails.getEmergencyPhoneNumber()).build();

        log.debug("Building GetVictimDetailsDTO");
        GetVictimDetailsDTO getVictimDetailsDTO = GetVictimDetailsDTO.builder()
                .victimAge(victimDetails.getVictimAge())
                .victimName(victimDetails.getVictimName())
                .build();

        // latitude and longitude is represented with ':' separator in reported location.
        String reportedLocation = victimDetails.getReportedLocation();
        String longitude = "";
        String latitude = "";

        if(!reportedLocation.isBlank()) {
            latitude = victimDetails.getReportedLocation().split(":")[0];
            if(reportedLocation.split(":").length > 0) {
                longitude = victimDetails.getReportedLocation().split(":")[1];
            }

        }

        log.info("Mapping GetTicketDetailsDTO...");
        return GetTicketDetailsDTO
                .builder()
                .additionalInformation(ticketDetails.getAdditionalInformation())
                .allocatedTo(ObjectUtils.isEmpty(ticket.getAllocatedTo()) ? null : ticket.getAllocatedTo().getUsername())
                .createdAt(LocalDateTimeParser.toUserFriendlyLocalDateTime(createdAtLocalDateTime))
                .emergencyContactDetails(getEmergencyContactDetails)
                .latitude(latitude)
                .longitude(longitude)
                .requiredItems(victimDetails.getRequiredItems())
                .ticketId(ticket.getUniqueIdentifier().toString())
                .ticketStatus(ticket.getTicketStatus().name())
                .victimDetails(getVictimDetailsDTO)
                .victimsAround(ticketDetails.getVictimsAroundCount()).build();

    };
}

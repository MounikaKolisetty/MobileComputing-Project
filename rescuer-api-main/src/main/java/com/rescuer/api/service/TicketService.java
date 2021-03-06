package com.rescuer.api.service;

import com.rescuer.api.dtos.TicketId;
import com.rescuer.api.entity.Attachment;
import com.rescuer.api.entity.Ticket;
import com.rescuer.api.entity.TicketStatus;
import com.rescuer.api.entity.UserTicketStats;
import com.rescuer.api.mappers.TicketMappers;
import com.rescuer.api.mappers.UserTicketMappers;
import com.rescuer.api.repository.rescuer.TicketRepository;
import com.rescuer.api.repository.rescuer.UserTicketStatsRepository;
import com.rescuer.api.web.dto.CreateTicketDTO;
import com.rescuer.api.web.dto.CreateTicketDTOResponse;
import com.rescuer.api.web.dto.GetAllTicketsDTO;
import com.rescuer.api.web.dto.GetTicketDetailsDTO;
import com.rescuer.api.web.error.ResourceNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserTicketStatsRepository userTicketStatsRepository;
    private final AllocationService allocationService;
    private final TicketHelper ticketHelper;

    @Autowired
    public TicketService(final TicketRepository ticketRepository, final UserTicketStatsRepository userTicketStatsRepository,
                         AllocationService allocationService, final TicketHelper ticketHelper) {
        this.ticketRepository = ticketRepository;
        this.userTicketStatsRepository = userTicketStatsRepository;
        this.allocationService = allocationService;
        this.ticketHelper = ticketHelper;
    }

    public CreateTicketDTOResponse createTicket(CreateTicketDTO createTicketDTO, List<MultipartFile> files) {
        if (ObjectUtils.isEmpty(files)) {
            files = Collections.emptyList();
        }
        log.info("Received request for creating ticket");
        log.debug("Mapping CreateTicketDTO to Ticket");
        Ticket ticket = TicketMappers.mapToTicket.apply(createTicketDTO);
        log.debug("Uploading attachments of size {}", files.size());
        Set<Attachment> attachments = FileService.getInstance().uploadFiles(files);
        log.debug("Finished Uploading attachments of size {}", attachments.size());
        ticket.getTicketDetails().getVictim().setAttachments(attachments);

        Ticket createdTicket = this.ticketRepository.save(ticket);
        log.info("Created ticket with id {}", createdTicket.getUniqueIdentifier().toString());

        // Allocation service is a job that runs after a specific time, log this ticket id.
        ticketHelper.allocateTicketToRescuer(createdTicket.getUniqueIdentifier().toString());

        return CreateTicketDTOResponse
                .builder()
                .id(createdTicket.getUniqueIdentifier().toString())
                .build();
    }

    /**
     * Get all tickets in database - ADMIN
     *
     * @return
     */
    public Set<GetAllTicketsDTO> getAllTickets() {
        log.info("Received request for getting all tickets");
        List<Ticket> ticketsInDatabase = this.ticketRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        log.info("Found tickets of size {}", ticketsInDatabase.size());
        log.debug("Mapping tickets to GetAllTicketsDTO");
        return ticketsInDatabase.stream().map(TicketMappers.mapToGetAllTickets).collect(Collectors.toSet());
    }

    /**
     * Get all tickets by status in database - ADMIN
     *
     * @return
     */
    public Set<GetAllTicketsDTO> getAllTickets(TicketStatus ticketStatus) {
        log.info("Received request for getting all tickets by status");
        List<Ticket> ticketsInDatabase = this.ticketRepository
                .findAllByTicketStatus(ticketStatus, Sort.by(Sort.Direction.DESC, "createdAt"));
        log.info("Found tickets of size {}", ticketsInDatabase.size());
        log.debug("Mapping tickets to GetAllTicketsDTO");
        return ticketsInDatabase.stream().map(TicketMappers.mapToGetAllTickets).collect(Collectors.toSet());
    }

    /**
     * Get all closed tickets for a user
     *
     * @param userName
     * @return
     */
    public Set<GetAllTicketsDTO> getAllClosedTickets(String userName) {
        log.info("Received request for getting all tickets for user {}", userName);
        Set<Ticket> closedTickets = this.ticketRepository
                .findAllByTicketStatusAndAllocatedTo_userName(TicketStatus.CLOSE, userName);
        if (!CollectionUtils.isEmpty(closedTickets)) {
            return closedTickets.stream().map(TicketMappers.mapToGetAllTickets).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Get All Tickets for a user
     *
     * @param userName
     * @return
     */
    public Set<GetAllTicketsDTO> getAllTickets(String userName) {
        log.info("Received request for getting all tickets for user {}", userName);
        Optional<UserTicketStats> userTicketStatsOptional = Optional.ofNullable(this.userTicketStatsRepository.findByUserName(
                userName, Sort.by(Sort.Direction.DESC, "createdAt")));
        return this.mapToGetAllTicketsDTO(userTicketStatsOptional);
    }

    /**
     * Get all tickets associated to user
     *
     * @param userName
     * @return
     */
    public Set<GetAllTicketsDTO> getAllInProgressTickets(String userName) {
        log.info("Received request for getting all tickets for user {}", userName);
        Set<Ticket> inprogressTickets = this.ticketRepository
                .findAllByTicketStatusAndAllocatedTo_userName(TicketStatus.IN_PROGRESS, userName);
        if (!CollectionUtils.isEmpty(inprogressTickets)) {
            return inprogressTickets.stream().map(TicketMappers.mapToGetAllTickets).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private Set<GetAllTicketsDTO> mapToGetAllTicketsDTO(Optional<UserTicketStats> userTicketStatsOptional) {
        if (userTicketStatsOptional.isPresent()) {
            UserTicketStats userTicketStats = userTicketStatsOptional.get();
            log.info("Getting associated tickets...");
            Set<Ticket> associatedTickets = userTicketStats.getTicketsAssociated();
            log.info("Associated tickets are of size {}", associatedTickets.size());
            log.debug("Mapping tickets to GetAllTicketsDTO");
            return associatedTickets.stream().map(TicketMappers.mapToGetAllTickets).collect(Collectors.toSet());
        }
        log.debug("No entry found with this user name");
        return Collections.emptySet();
    }

    public GetTicketDetailsDTO getTicketDetails(UUID ticketId) {
        log.info("Request received for getting ticket: {} details", ticketId.toString());
        Optional<Ticket> optionalTicket =
                this.ticketRepository.findById(ticketId);
        if (optionalTicket.isPresent()) {
            Ticket foundTicket = optionalTicket.get();
            return TicketMappers.mapToGetTicketDetails.apply(foundTicket);
        }
        log.error("Could not find ticket details for id %s ans status :", ticketId.toString(), TicketStatus.OPEN);
        throw new ResourceNotFound(String.format("Could not find ticket details for id %s", ticketId.toString()));
    }

    public GetTicketDetailsDTO getTicketDetails(UUID ticketId, String userName) {
        log.info("Request received for getTicketDetails for ticket id {} and user name {}", ticketId.toString(), userName);
        Optional<UserTicketStats> optionalFoundUserTicketStats = Optional.ofNullable(this.userTicketStatsRepository
                .findByUserNameAndTicketsAssociated_UniqueIdentifierIn(userName, Arrays.asList(ticketId)));
        Optional<GetTicketDetailsDTO> optionalGetTicketDetailsDTO = Optional.ofNullable(null);
        if (optionalFoundUserTicketStats.isPresent()) {
            optionalGetTicketDetailsDTO = UserTicketMappers
                    .mapToGetTicketDetailsDTO(optionalFoundUserTicketStats.get());
        }
        if (optionalGetTicketDetailsDTO.isPresent()) {
            log.info("Found details with ticket id {} and user name {}", ticketId.toString(), userName);
            return optionalGetTicketDetailsDTO.get();
        }
        log.error("Could not able to get details with ticket id {} and user name {}", ticketId.toString(), userName);
        throw new ResourceNotFound(String.format("Could not find ticket details for id %s and userName %s",
                ticketId.toString(), userName));
    }

    @Transactional
    public Boolean resolveTicket(UUID ticketId, String userName) {
        log.info("Received request for closing the ticket id: {}, userName: {}", ticketId, userName);
        Optional<Ticket> optionalTicket = Optional.ofNullable(this.ticketRepository
                .findByUniqueIdentifierAndAllocatedTo_userName(ticketId, userName));
        if (optionalTicket.isPresent()) {
            // Closing a ticket should also minus open tickets count by one.
            Ticket ticketToUpdate = optionalTicket.get();
            ticketToUpdate.setTicketStatus(TicketStatus.CLOSE);
            boolean isTicketInReOpenState = ticketToUpdate.getTicketStatus().equals(TicketStatus.RE_OPEN);
            this.ticketRepository.save(ticketToUpdate);

            Optional<UserTicketStats> userTicketStatsToUpdateStatsCountOptional = Optional.ofNullable(
                    this.userTicketStatsRepository.findByUserName(userName));
            if (userTicketStatsToUpdateStatsCountOptional.isPresent()) {
                UserTicketStats userTicketStatsToUpdateStatsCount = userTicketStatsToUpdateStatsCountOptional.get();
                if (isTicketInReOpenState) {
                    userTicketStatsToUpdateStatsCount.setReopenedTickets(userTicketStatsToUpdateStatsCount.getReopenedTickets() - 1);
                } else {
                    userTicketStatsToUpdateStatsCount.setOpenedTickets(userTicketStatsToUpdateStatsCount.getOpenedTickets() - 1);
                }
                Integer closedTickets = ObjectUtils.isEmpty(userTicketStatsToUpdateStatsCount.getClosedTickets()) ? 0 : userTicketStatsToUpdateStatsCount.getClosedTickets();
                userTicketStatsToUpdateStatsCount.setClosedTickets(closedTickets + 1);
                this.userTicketStatsRepository.save(userTicketStatsToUpdateStatsCount);
            }
            log.info("Successfully closed the ticket {} assigned to {}", ticketId, userName);
            return true;
        }
        log.error("Could not find ticket details for id {} and userName {} to resolve", ticketId, userName);
        throw new ResourceNotFound(String.format("Could not find ticket details for id %s and userName %s to resolve",
                ticketId.toString(), userName));
    }

    public boolean reOpenTicket(UUID ticketId, String userName) {
        log.info("Received request for re-opening the ticket id: {}, userName: {}", ticketId, userName);
        Optional<Ticket> optionalTicket = Optional.ofNullable(this.ticketRepository
                .findByUniqueIdentifierAndAllocatedTo_userNameAndTicketStatus(ticketId, userName, TicketStatus.CLOSE));
        if (optionalTicket.isPresent()) {
            // Re-opening a ticket should increase re-open tickets count by one and decrease closed count by one.
            Ticket ticketToUpdate = optionalTicket.get();
            ticketToUpdate.setTicketStatus(TicketStatus.IN_PROGRESS);

            Optional<UserTicketStats> userTicketStatsToUpdateStatsCountOptional = Optional.ofNullable(
                    this.userTicketStatsRepository.findByUserName(userName));
            if (userTicketStatsToUpdateStatsCountOptional.isPresent()) {
                UserTicketStats userTicketStatsToUpdateStatsCount = userTicketStatsToUpdateStatsCountOptional.get();
                int reOpenCount = ObjectUtils.isEmpty(userTicketStatsToUpdateStatsCount.getReopenedTickets()) ? 0 :
                        userTicketStatsToUpdateStatsCount.getReopenedTickets();
                userTicketStatsToUpdateStatsCount.setReopenedTickets(reOpenCount + 1);
                int openTicketsCount = ObjectUtils.isEmpty(userTicketStatsToUpdateStatsCount.getOpenedTickets()) ? 0 :
                        userTicketStatsToUpdateStatsCount.getOpenedTickets();
                userTicketStatsToUpdateStatsCount.setClosedTickets(openTicketsCount - 1);
                this.userTicketStatsRepository.save(userTicketStatsToUpdateStatsCount);
            } else {
                ticketToUpdate.setTicketStatus(TicketStatus.OPEN);
            }
            this.ticketRepository.save(ticketToUpdate);
            if (!userTicketStatsToUpdateStatsCountOptional.isPresent()) {
                TicketId ticketIdToAllocate = new TicketId(ticketToUpdate.getUniqueIdentifier());
                this.allocationService.allocateTickets(Arrays.asList(ticketIdToAllocate));
            }
            log.info("Successfully re-opened the ticket {} assigned to {}", ticketId, userName);
            return true;
        }
        log.error("Could not find ticket details for id {} and userName {} to reopen with status", ticketId, userName, TicketStatus.CLOSE);
        throw new ResourceNotFound(String.format("Could not find ticket details for id %s and userName %s to reopen",
                ticketId.toString(), userName));
    }

    public boolean assignTicket(UUID ticketId) {
        log.info("Request received for assign rescuer for ticket id {}", ticketId.toString());
        Ticket ticket = this.ticketRepository.findByUniqueIdentifierAndAllocatedToIsNull(ticketId);
        if (ObjectUtils.isEmpty(ticket.getAllocatedTo())) {
            return this.allocationService.allocateTickets(Arrays.asList(new TicketId(ticketId)));
        }
        log.info("Ticket is not found or already assigned {}", ticketId.toString());
        return false;
    }


}

package com.rescuer.api.service;

import com.rescuer.api.dtos.TicketId;
import com.rescuer.api.entity.*;
import com.rescuer.api.repository.rescuer.TicketRepository;
import com.rescuer.api.repository.rescuer.UserRepository;
import com.rescuer.api.repository.rescuer.UserTicketStatsRepository;
import com.rescuer.api.util.ServiceUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AllocationService {

    private static AllocationService instance;
    private final UserTicketStatsRepository userTicketStatsRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketsHolder ticketsHolder;

    @Autowired
    public AllocationService(final UserTicketStatsRepository userTicketStatsRepository, final TicketsHolder ticketsHolder,
                             final TicketRepository ticketRepository, final UserRepository userRepository) {
        this.userTicketStatsRepository = userTicketStatsRepository;
        this.ticketsHolder = ticketsHolder;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        instance = this;
    }

    public static AllocationService getInstance() {
        return instance;
    }

    @Scheduled(initialDelayString = "#{'${app-settings.allocation-service-initial-delay}'}",
            fixedDelayString = "#{'${app-settings.allocation-service-fixed-delay}'}")
    protected void assignTicketsToServiceUsers() {
        StopWatch timer = new StopWatch();
        timer.start();
        if (ticketsHolder.getTicketIds().size() > 0) {
            LinkedHashSet<String> ticketsIdsString = ticketsHolder.getTicketIds();
            Set<TicketId> ticketIds = ticketsIdsString
                    .stream()
                    .map(id -> new TicketId(id)).collect(Collectors.toSet());
            boolean isSuccess = this.allocateTickets(ticketIds);
            if (isSuccess) {
                ticketsHolder.removeTickets(ticketsIdsString);
            }
        }
        timer.stop();
        log.info("Finished allocation service, took {} milliseconds", timer.getTotalTimeMillis());
    }

    @Transactional
    public boolean allocateTickets(Collection<TicketId> ticketIds) {
        try {
            StopWatch timer = new StopWatch();
            timer.start();
            log.info("Allocating tickets got started");
            if (!CollectionUtils.isEmpty(ticketIds)) {
                Set<ServiceUser> serviceUsers = this.userTicketStatsRepository.getAllUsersByUserType(UserType.RESCUER);
                Set<ServiceUser> serviceUserStats = this.userTicketStatsRepository.getAllUserStatsByUserType(UserType.RESCUER);
                serviceUsers.addAll(serviceUserStats);
                TreeSet<ServiceUser> priorityServiceUsers = new TreeSet<>(serviceUsers);
                if (!CollectionUtils.isEmpty(priorityServiceUsers)) {
                    List<UUID> ticketUUIDs = ticketIds
                            .stream()
                            .map(ticketId -> UUID.fromString(ticketId.getTicketId())).collect(Collectors.toList());
                    Set<Ticket> tickets = this.ticketRepository.findAllByUniqueIdentifierIn(ticketUUIDs);
                    List<Ticket> ticketsToUpdate = new ArrayList<>(tickets.size());
                    for (Ticket ticket : tickets) {
                        try {
                            ServiceUser priorityUser = priorityServiceUsers.pollFirst();
                            Optional<UserTicketStats> foundUserUserTicketStatsOptional = this.userTicketStatsRepository
                                    .findById(UUID.fromString(priorityUser.getUserId()));
                            if (foundUserUserTicketStatsOptional.isPresent()) {
                                log.info("User has an entry already in UserStats, adding one more ticket!");
                                log.info("User {} doesn't have stats, creating an entry in user stats",
                                        foundUserUserTicketStatsOptional.get().getUsername());
                                UserTicketStats userTicketStats = foundUserUserTicketStatsOptional.get();
                                userTicketStats.incrementTicketsCount();
                                userTicketStats.getTicketsAssociated().add(ticket);
                                ticket.setAllocatedTo(userTicketStats);
                            } else {
                                log.info("User {} doesn't have stats, creating an entry in user stats",
                                        foundUserUserTicketStatsOptional.get().getUsername());
                                User foundUser = this.userRepository.findById(UUID.fromString(priorityUser.getUserId())).get();
                                UserTicketStats newUserTicketsStats = new UserTicketStats();
                                newUserTicketsStats.setUserDetails(foundUser);
                                newUserTicketsStats.initializeTicketsCount();
                                newUserTicketsStats.incrementTicketsCount();
                                this.userTicketStatsRepository.save(newUserTicketsStats);
                                newUserTicketsStats.getTicketsAssociated().add(ticket);
                                ticket.setAllocatedTo(newUserTicketsStats);
                            }
                            ticket.setTicketStatus(TicketStatus.IN_PROGRESS);
                            ticketsToUpdate.add(ticket);
                        } catch (Exception e) {
                            log.error("Exception while performing allocation", e);
                        }
                    }
                    log.info("Tickets size to be updated {}", ticketsToUpdate.size());
                    if (ticketsToUpdate.size() > 0) {
                        this.ticketRepository.saveAll(ticketsToUpdate);
                    }
                } else {
                    log.warn("No users found in database of type {}", UserType.RESCUER);
                }
            }
            timer.stop();
            log.info("Finished allocation service, took {} milliseconds", timer.getTotalTimeMillis());
        } catch (Exception e) {
            log.error("Error while allocating tickets", e);
            return false;
        }
        return true;
    }

    public boolean allocateAllTickets() {
        log.info("Manually running job for allocating all tickets with Status {}", TicketStatus.OPEN);
        Set<TicketId> ticketIds = this.ticketRepository.getAllOpenTicketIds();
        if (ticketIds.size() > 0) {
            this.allocateTickets(ticketIds);
            return true;
        }
        log.info("No tickets found with status {}", TicketStatus.OPEN);
        return true;
    }

}

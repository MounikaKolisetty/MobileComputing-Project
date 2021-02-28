package com.rescuer.api.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rescuer.api.entity.TicketStatus;
import com.rescuer.api.entity.User;
import com.rescuer.api.entity.UserType;
import com.rescuer.api.service.TicketService;
import com.rescuer.api.web.dto.CreateTicketDTO;
import com.rescuer.api.web.dto.CreateTicketDTOResponse;
import com.rescuer.api.web.dto.GetAllTicketsDTO;
import com.rescuer.api.web.dto.GetTicketDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService, ObjectMapper objectMapper) {
        this.ticketService = ticketService;
    }

    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(@RequestParam("ticketData") CreateTicketDTO createTicketDTO,
                                          @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        try {
            CreateTicketDTOResponse createTicketDTOResponse = ticketService.createTicket(createTicketDTO, files);
            return ResponseEntity.status(HttpStatus.CREATED).body(createTicketDTO);
        } catch (Exception ex) {
            log.error("filed to create ticket with exception {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/all-tickets")
    public ResponseEntity<?> getAllTickets() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();
            Set<GetAllTicketsDTO> getAllTicketsDTOS = new HashSet<>(0);
            if (UserType.ADMIN.toString().equalsIgnoreCase(user.getUserType().toString())) {
                getAllTicketsDTOS = ticketService.getAllTickets();
            } else {
                getAllTicketsDTOS = ticketService.getAllTickets(user.getUsername());
            }
            return ResponseEntity.status(HttpStatus.OK).body(getAllTicketsDTOS);
        } catch (Exception ex) {
            log.error("filed to get all tickets with exception {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/closed-tickets")
    public ResponseEntity<?> getAllClosedTickets() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();
            Set<GetAllTicketsDTO> getClosedTickets = new HashSet<>(0);
            if (UserType.ADMIN.toString().equalsIgnoreCase(user.getUserType().toString())) {
                getClosedTickets = ticketService.getAllTickets(TicketStatus.CLOSE);
            } else {
                getClosedTickets = ticketService.getAllClosedTickets(user.getUsername());
            }
            return ResponseEntity.status(HttpStatus.OK).body(getClosedTickets);
        } catch (Exception ex) {
            log.error("filed to get closed tickets  with exception {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/open-tickets")
    public ResponseEntity<?> getAllOpenTickets() {
        try {
            Set<GetAllTicketsDTO> getOpenTickets = ticketService.getAllTickets(TicketStatus.OPEN);
            return ResponseEntity.status(HttpStatus.OK).body(getOpenTickets);
        } catch (Exception ex) {
            log.error("filed to get open tickets with exception {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/inProgress-tickets")
    public ResponseEntity<?> getAllinProgressTickets() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();
            Set<GetAllTicketsDTO> getInProgressTickets = new HashSet<>(0);
            if (UserType.ADMIN.toString().equalsIgnoreCase(user.getUserType().toString())) {
                getInProgressTickets = ticketService.getAllTickets(TicketStatus.IN_PROGRESS);
            } else {
                getInProgressTickets = ticketService.getAllInProgressTickets(user.getUsername());
            }
            return ResponseEntity.status(HttpStatus.OK).body(getInProgressTickets);
        } catch (Exception ex) {
            log.error("filed to get open tickets with exception {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<?> getTicketById(@PathVariable(value = "ticketId") String ticketId) {
        try {
            GetTicketDetailsDTO ticketDto = ticketService.getTicketDetails(UUID.fromString(ticketId));
            return ResponseEntity.status(HttpStatus.OK).body(ticketDto);
        } catch (Exception ex) {
            log.error("filed to get ticket with ticketId: {}, exception {}", ticketId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('RESCUER') or hasAuthority('ADMIN')")
    @PostMapping("/resolve/{ticketId}")
    public ResponseEntity<?> resolveTicket(@PathVariable(value = "ticketId") String ticketId,
                                           @RequestParam(value = "userName") String username) {
        try {
            Boolean isResolved = ticketService
                    .resolveTicket(UUID.fromString(ticketId), username);
            return ResponseEntity.status(HttpStatus.OK).body(isResolved);
        } catch (Exception ex) {
            log.error("filed to resolve ticket with ticketId: {}, exception {}", ticketId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/reopen/{ticketId}")
    public ResponseEntity<?> reOpenTicket(@PathVariable(value = "ticketId") String ticketId,
                                          @RequestParam(value = "userName") String username) {
        try {
            Boolean isReOpened = ticketService
                    .reOpenTicket(UUID.fromString(ticketId), username);
            return ResponseEntity.status(HttpStatus.OK).body(isReOpened);
        } catch (Exception ex) {
            log.error("filed to reopen ticket with ticketId: {}, exception {}", ticketId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/assign/{ticketId}")
    public ResponseEntity<?> assignTicket(@PathVariable(value = "ticketId") String ticketId) {
        try {
            Boolean isTicketAssigned = ticketService
                    .assignTicket(UUID.fromString(ticketId));
            return ResponseEntity.status(HttpStatus.OK).body(isTicketAssigned);
        } catch (Exception ex) {
            log.error("filed to assign ticket with ticketId: {}, exception {}", ticketId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}

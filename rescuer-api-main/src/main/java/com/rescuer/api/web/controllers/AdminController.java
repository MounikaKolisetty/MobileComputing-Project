package com.rescuer.api.web.controllers;

import com.rescuer.api.dtos.requestDtos.UserInputDto;
import com.rescuer.api.entity.User;
import com.rescuer.api.service.AdminService;
import com.rescuer.api.service.TicketService;
import com.rescuer.api.web.error.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final TicketService ticketService;

    @Autowired
    public AdminController(AdminService adminService, TicketService ticketService) {
        this.adminService = adminService;
        this.ticketService = ticketService;
    }


    @PostMapping("/add-admin")
    public ResponseEntity<?> addAdmin(@RequestBody UserInputDto user) {
        try {
            User userResponse = adminService.saveAdmin(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (BadRequest b) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(b.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add-rescuer")
    public ResponseEntity<?> addRescuer(@RequestBody UserInputDto user) {
        try {
            User userResponse = adminService.saveRescuer(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (BadRequest b) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(b.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/assign-ticket/{ticketId}")
    public ResponseEntity<?> assignTicket(@PathVariable(value = "ticketId") String ticketId) {
        boolean isAssigned = ticketService.assignTicket(UUID.fromString(ticketId));
        return ResponseEntity.status(HttpStatus.OK).body(isAssigned);
    }
}

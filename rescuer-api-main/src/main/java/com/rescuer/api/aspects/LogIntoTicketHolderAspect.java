package com.rescuer.api.aspects;

import com.rescuer.api.dtos.TicketId;
import com.rescuer.api.service.AllocationService;
import com.rescuer.api.service.TicketsHolder;
import com.rescuer.api.util.LogIntoTicketHolder;
import com.rescuer.api.util.TicketHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Aspect
@Component
@Slf4j
public class LogIntoTicketHolderAspect {

    private final AllocationService allocationService;
    private final TicketsHolder ticketsHolder;

    @Autowired
    public LogIntoTicketHolderAspect(final AllocationService allocationService, final TicketsHolder ticketsHolder) {
        this.allocationService = allocationService;
        this.ticketsHolder = ticketsHolder;
    }

    @Pointcut("@annotation(logIntoTicketHolder)")
    public void perform(LogIntoTicketHolder logIntoTicketHolder) {

    }

    @AfterReturning(pointcut = "perform(logIntoTicketHolder)", returning = "result")
    public void addTicketToTicketHolder(JoinPoint joinPoint, Object result, LogIntoTicketHolder logIntoTicketHolder) {
        log.info("Adding ticket id to TicketsHolder");
        if (ObjectUtils.isEmpty(result)) {
            log.warn("result cannot be empty");
            return;
        }
        if (result instanceof TicketHolder) {
            CompletableFuture.runAsync(() -> {
                TicketHolder ticketHolderDTO = (TicketHolder) result;
                try {
                    ticketsHolder.addTicket(ticketHolderDTO.getTicketId());
                } catch(Exception e) {
                    log.error("Exception while adding ticket to TicketsHolder, sending it manually", e);
                    String ticketId = ticketHolderDTO.getTicketId();
                    allocationService.allocateTickets(Arrays.asList(new TicketId(ticketId)));
                }
            });
            return;
        }
        throw new RuntimeException("Expected instance type TicketHolder");
    }
}

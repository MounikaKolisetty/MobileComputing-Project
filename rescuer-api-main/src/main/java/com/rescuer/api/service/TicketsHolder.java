package com.rescuer.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.*;
import java.util.Collection;
import java.util.LinkedHashSet;

@Component
@Slf4j
public class TicketsHolder implements Serializable {

    private volatile LinkedHashSet<String> ticketIds;

    public TicketsHolder() throws IOException {
        File f = new File("ticket_holder.ser");
        if(f.exists()) {
            log.info("Found file with ticket_holder.ser");
            try(
                    FileInputStream fis = new FileInputStream(f);
                    ObjectInputStream dis = new ObjectInputStream(fis);
                    ) {
                TicketsHolder ticketHolder = (TicketsHolder)dis.readObject();
                this.ticketIds = ticketHolder.getTicketIds();
                log.info("Setting ticket ids {} to ticketHolder object", this.ticketIds);
                return;
            } catch (ClassNotFoundException e) {
                log.error("Exception while reading object", e);
            }
        }
        this.ticketIds = new LinkedHashSet<>(0);
    }

    public synchronized void addTicket(String ticketId) {
        this.ticketIds.add(ticketId);
    }

    public synchronized void removeTickets(Collection<String> ticketIds) {
        this.ticketIds.removeAll(ticketIds);
    }

    public LinkedHashSet<String> getTicketIds() {
        return this.ticketIds;
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        log.info("Saving ticket holder to file");
        try(
                FileOutputStream f = new FileOutputStream(new File("ticket_holder.ser"));
                ObjectOutputStream o = new ObjectOutputStream(f);
        ){
            o.writeObject(this);
        } catch (Exception ex) {
            log.error("exception while saving object to file", ex);
        }

    }

}
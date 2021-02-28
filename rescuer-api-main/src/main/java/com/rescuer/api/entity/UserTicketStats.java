package com.rescuer.api.entity;

import lombok.*;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"ticketsAssociated"})
@EqualsAndHashCode(exclude = {"ticketsAssociated"})

@Entity
@PrimaryKeyJoinColumn(name = "user_ticket_details_id")
public class UserTicketStats extends User {

    @OneToMany(mappedBy = "allocatedTo", fetch = FetchType.EAGER, orphanRemoval = true, cascade = {CascadeType.PERSIST})
    private Set<Ticket> ticketsAssociated;
    private Integer openedTickets;
    private Integer reopenedTickets;
    private Integer closedTickets;

    public void incrementTicketsCount() {
        this.openedTickets = ObjectUtils.isEmpty(this.openedTickets) ? 0 : this.openedTickets;
        this.openedTickets += 1;
    }

    public void initializeTicketsCount() {
        this.openedTickets = 0;
        this.reopenedTickets = 0;
        this.closedTickets = 0;
        this.ticketsAssociated = new HashSet<>();
    }

    public void setUserDetails(User user) {
        this.setUniqueIdentifier(user.getUniqueIdentifier());
        this.setZoneId(user.getZoneId());
        this.setUserName(user.getUsername());
        this.setPassword(user.getPassword());
        this.setIsUserActive(user.getIsUserActive());
        this.setUserType(user.getUserType());
    }
}

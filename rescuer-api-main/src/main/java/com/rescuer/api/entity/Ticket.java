package com.rescuer.api.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"allocatedTo"})

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Ticket extends BaseEntity {

    @Embedded
    private TicketDetails ticketDetails;
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_allocated_id")
    private UserTicketStats allocatedTo;

}

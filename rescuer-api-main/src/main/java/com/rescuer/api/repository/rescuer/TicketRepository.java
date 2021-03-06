package com.rescuer.api.repository.rescuer;

import com.rescuer.api.dtos.TicketId;
import com.rescuer.api.entity.Ticket;
import com.rescuer.api.entity.TicketStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Ticket findByUniqueIdentifierAndTicketStatus(UUID ticketId, TicketStatus ticketStatus);

    Ticket findByUniqueIdentifierAndAllocatedTo_userName(UUID ticketId, String userName);

    Ticket findByUniqueIdentifierAndAllocatedTo_userNameAndTicketStatus(UUID ticketId, String userName, TicketStatus ticketStatus);

    @Query(value = "Select new com.rescuer.api.dtos.TicketId(t.uniqueIdentifier) from Ticket t where t.ticketStatus='OPEN'")
    Set<TicketId> getAllOpenTicketIds();

    Set<Ticket> findAllByUniqueIdentifierIn(List<UUID> ticketIds);

    List<Ticket> findAllByTicketStatus(TicketStatus ticketStatus, Sort sort);

    Ticket findByUniqueIdentifierAndAllocatedToIsNull(UUID ticketId);

    Set<Ticket> findAllByTicketStatusAndAllocatedTo_userName(TicketStatus status, String userName);
    
}

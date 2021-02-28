package com.rescuer.api.repository.rescuer;

import com.rescuer.api.entity.TicketStatus;
import com.rescuer.api.entity.UserTicketStats;
import com.rescuer.api.entity.UserType;
import com.rescuer.api.util.ServiceUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserTicketStatsRepository extends JpaRepository<UserTicketStats, UUID> {

    UserTicketStats findByUserName(String userName, Sort sort);

    UserTicketStats findByUserName(String userName);

    UserTicketStats findByUserNameAndTicketsAssociated_TicketStatus(String userName, TicketStatus ticketStatus, Sort sort);

    UserTicketStats findByUserNameAndTicketsAssociated_UniqueIdentifierIn(String userName, List<UUID> ticketId);

    @Query(value = "Select new com.rescuer.api.util.ServiceUser(u.uniqueIdentifier, u.openedTickets, u.reopenedTickets) " +
            "from UserTicketStats u WHERE u.userType=:userType")
    Set<ServiceUser> getAllUserStatsByUserType(@Param("userType") UserType userType);

    @Query(value = "Select new com.rescuer.api.util.ServiceUser(u.uniqueIdentifier, 0, 0) " +
            "from User u WHERE u.userType=:userType")
    Set<ServiceUser> getAllUsersByUserType(@Param("userType") UserType userType);
}

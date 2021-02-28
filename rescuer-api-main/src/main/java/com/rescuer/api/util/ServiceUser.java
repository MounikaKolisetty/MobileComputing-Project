package com.rescuer.api.util;

import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"openTicketsCount"})
public class ServiceUser implements Comparable<ServiceUser> {

    private String userId;
    private Integer openTicketsCount;

    public ServiceUser(UUID userId, Integer openedTickets, Integer reopenedTickets) {
        this(userId.toString(), openedTickets, reopenedTickets);
    }

    private ServiceUser(String userId, Integer openedTickets, Integer reopenedTickets) {
        this.userId = userId.toString();
        openedTickets = ObjectUtils.isEmpty(openedTickets) ? 0 : openedTickets;
        reopenedTickets = ObjectUtils.isEmpty(reopenedTickets) ? 0 : reopenedTickets;
        this.openTicketsCount = openedTickets + reopenedTickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceUser that = (ServiceUser) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public int compareTo(ServiceUser serviceUser) {
        return this.openTicketsCount > serviceUser.getOpenTicketsCount() ? 1 : -1;
    }
}

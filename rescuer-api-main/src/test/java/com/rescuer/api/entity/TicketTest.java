package com.rescuer.api.entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketTest {

    private EmergencyContact getTestEmergencyContact() {
        return EmergencyContact.builder()
                .emergencyContactAge(30)
                .emergencyContactName("testEmergencyUser")
                .emergencyAddress("test address")
                .emergencyPhoneNumber("999999999")
                .build();
    }

    private Victim getTestVictimDetails() {
        return Victim.builder()
                .victimAge(20)
                .victimName("test victim")
                .attachments(new HashSet<>(0))
                .reportedLocation("test location")
                .requiredItems(new HashSet<>(Arrays.asList("choco bar")))
                .build();
    }

    private TicketDetails getTestTicketDetails(EmergencyContact emergencyContact, Victim victim) {
        return TicketDetails.builder()
                .additionalInformation("test additional information")
                .emergencyContact(emergencyContact)
                .victim(victim)
                .build();
    }

    private UserTicketStats getTestUserTicketStats() {
        UserTicketStats userTicketStats = new UserTicketStats(
                new HashSet<>(Arrays.asList(new Ticket())), 5, 1, 20);

        User testUser = User.builder()
                .userName("testUserName")
                .userType(UserType.RESCUER)
                .isUserActive(true)
                .password("dhshdsd")
                .build();

        userTicketStats.setUserDetails(testUser);
        return userTicketStats;
    }

    @Test
    public void shouldAbleToStoreTicketDetails() {
        // Prepare
        EmergencyContact emergencyContact = this.getTestEmergencyContact();
        Victim victim = this.getTestVictimDetails();
        TicketDetails ticketDetails = this.getTestTicketDetails(emergencyContact, victim);

        // Act
        Ticket ticket = Ticket.builder()
                .ticketDetails(ticketDetails)
                .allocatedTo(this.getTestUserTicketStats())
                .ticketStatus(TicketStatus.OPEN)
                .build();

        // Assert
        assertThat(ticket.getTicketDetails()).isNotNull();
        assertThat(ticket.getTicketDetails().getAdditionalInformation()).isNotNull();

        // Emergency contact
        assertThat(ticket.getTicketDetails().getEmergencyContact()).isNotNull();
        assertThat(ticket.getTicketDetails().getEmergencyContact().getEmergencyAddress()).isNotNull();
        assertThat(ticket.getTicketDetails().getEmergencyContact().getEmergencyContactAge()).isNotNull();
        assertThat(ticket.getTicketDetails().getEmergencyContact().getEmergencyContactName()).isNotNull();
        assertThat(ticket.getTicketDetails().getEmergencyContact().getEmergencyPhoneNumber()).isNotNull();

        // Victim
        assertThat(ticket.getTicketDetails().getVictim()).isNotNull();
        assertThat(ticket.getTicketDetails().getVictim().getReportedLocation()).isNotNull();
        assertThat(ticket.getTicketDetails().getVictim().getAttachments()).isNotNull();
        assertThat(ticket.getTicketDetails().getVictim().getRequiredItems().size()).isGreaterThanOrEqualTo(1);
        assertThat(ticket.getTicketDetails().getVictim().getVictimAge()).isNotNull();
        assertThat(ticket.getTicketDetails().getVictim().getVictimName()).isNotNull();

        // ticket status
        assertThat(ticket.getTicketStatus()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    public void shouldNotOutPutAllocatedToWhenToStringPerformed() {
        // Prepare
        EmergencyContact emergencyContact = this.getTestEmergencyContact();
        Victim victim = this.getTestVictimDetails();
        TicketDetails ticketDetails = this.getTestTicketDetails(emergencyContact, victim);

        // Act
        Ticket ticket = Ticket.builder()
                .ticketDetails(ticketDetails)
                .allocatedTo(this.getTestUserTicketStats())
                .ticketStatus(TicketStatus.OPEN)
                .build();

        // Assert
        assertThat(ticket.toString().contains("allocatedTo")).isFalse();
    }

}
package com.rescuer.api.service;

import com.rescuer.api.config.RescueMongoContainer;
import com.rescuer.api.config.RescuePostgresContainer;
import com.rescuer.api.dtos.requestDtos.UserInputDto;
import com.rescuer.api.entity.TicketStatus;
import com.rescuer.api.entity.User;
import com.rescuer.api.repository.rescuer.TicketRepository;
import com.rescuer.api.repository.rescuer.UserRepository;
import com.rescuer.api.time.ZoneIdConstant;
import com.rescuer.api.web.dto.*;
import com.rescuer.api.web.error.ResourceNotFound;
import com.rescuer.api.web.util.UserZoneIdHolder;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TicketServiceTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = RescuePostgresContainer.getInstance();
    @ClassRule
    public static RescueMongoContainer mongoContainer = RescueMongoContainer.getInstance();

    @Autowired
    private TicketService ticketService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeClass
    public static void beforeAll() {
        UserZoneIdHolder.setUserZoneIdHolder(ZoneIdConstant.ASIA_KOLKATA);
    }

    @Before
    public void beforeEach() {
        UserInputDto userInputDTO = UserInputDto.builder()
                .isUserActive(true)
                .password("s")
                .userName("testUser").build();
        User savedUser = this.adminService.saveRescuer(userInputDTO);
        Arrays.asList("1", "2", "3").forEach(appendStringForEachVar -> {
            CreateEmergencyContactDTO createEmergencyContactDTO = CreateEmergencyContactDTO
                    .builder()
                    .emergencyContactAge(25)
                    .emergencyContactName("Saini" + appendStringForEachVar)
                    .emergencyAddress("Emergency address" + appendStringForEachVar)
                    .emergencyPhoneNumber("9999998978" + appendStringForEachVar)
                    .build();
            CreateVictimDTO createVictimDTO = CreateVictimDTO.builder()
                    .victimAge(20)
                    .victimName("John" + appendStringForEachVar)
                    .latitude("11024.25" + appendStringForEachVar)
                    .longitude("100.255" + appendStringForEachVar)
                    .requiredItems(new HashSet<>(Arrays.asList("water" + appendStringForEachVar, "First Aid" + appendStringForEachVar)))
                    .build();
            CreateTicketDTO createTicketDTO = CreateTicketDTO.builder()
                    .additionalInformation("We are in bad situation" + appendStringForEachVar)
                    .emergencyDetails(createEmergencyContactDTO)
                    .victimDetails(createVictimDTO)
                    .victimsAround(10)
                    .build();

            CreateTicketDTOResponse createTicketDTOResponse = ticketService.createTicket(createTicketDTO, Collections.emptyList());
        });
    }

    @After
    public void afterEach() {
        this.ticketRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void createTicket() {
        CreateEmergencyContactDTO createEmergencyContactDTO = CreateEmergencyContactDTO
                .builder()
                .emergencyContactAge(25)
                .emergencyContactName("Saini")
                .emergencyAddress("Emergency address")
                .emergencyPhoneNumber("9999998978")
                .build();
        CreateVictimDTO createVictimDTO = CreateVictimDTO.builder()
                .victimAge(20)
                .victimName("John")
                .latitude("11024.25")
                .longitude("100.255")
                .requiredItems(new HashSet<>(Arrays.asList("water", "First Aid")))
                .build();
        CreateTicketDTO createTicketDTO = CreateTicketDTO.builder()
                .additionalInformation("We are in bad situation")
                .emergencyDetails(createEmergencyContactDTO)
                .victimDetails(createVictimDTO)
                .victimsAround(10)
                .build();

        CreateTicketDTOResponse createTicketDTOResponse = ticketService.createTicket(createTicketDTO, Collections.emptyList());
        assertThat(createTicketDTOResponse.getId()).isNotNull();
        String createdId = createTicketDTOResponse.getId();
        GetTicketDetailsDTO ticketDetailsDTO = ticketService.getTicketDetails(UUID.fromString(createdId));
        assertThat(ticketDetailsDTO).isNotNull();
        assertThat(ticketDetailsDTO.getAdditionalInformation()).isEqualTo("We are in bad situation");
        assertThat(ticketDetailsDTO.getRequiredItems().size()).isGreaterThanOrEqualTo(2);
        assertThat(ticketDetailsDTO.getRequiredItems().contains("water")).isTrue();
        assertThat(ticketDetailsDTO.getRequiredItems().contains("water1")).isFalse();
        assertThat(ticketDetailsDTO.getEmergencyContactDetails().getEmergencyContactName()).isEqualTo("Saini");
        assertThat(ticketDetailsDTO.getEmergencyContactDetails().getEmergencyAddress()).isEqualTo("Emergency address");
        assertThat(ticketDetailsDTO.getEmergencyContactDetails().getEmergencyPhoneNumber()).isEqualTo("9999998978");
        assertThat(ticketDetailsDTO.getVictimDetails().getVictimAge()).isEqualTo(20);
        assertThat(ticketDetailsDTO.getVictimDetails().getVictimName()).isEqualTo("John");
        assertThat(ticketDetailsDTO.getLatitude()).isEqualTo("11024.25");
        assertThat(ticketDetailsDTO.getLongitude()).isEqualTo("100.255");
        assertThat(ticketDetailsDTO.getVictimsAround()).isEqualTo(10);
        assertThat(ticketDetailsDTO.getTicketStatus()).isEqualTo(TicketStatus.OPEN.name());
    }

    @Test
    public void getAllTickets() {
        Collection<GetAllTicketsDTO> ticketsInDB = this.ticketService.getAllTickets();
        assertThat(ticketsInDB.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void getAllClosedTickets() {
        Collection<GetAllTicketsDTO> ticketsInDB = this.ticketService.getAllTickets();
        GetAllTicketsDTO getAllTicketsDTO = ticketsInDB.iterator().next();
        String ticketId = getAllTicketsDTO.getTicketId();

        // assign ticket
        this.ticketService.assignTicket(UUID.fromString(ticketId));
        GetTicketDetailsDTO ticketDetailsDTO = ticketService.getTicketDetails(UUID.fromString(ticketId));
        assertThat(ticketDetailsDTO.getAllocatedTo()).isNotNull();
        assertThat(ticketDetailsDTO.getTicketStatus()).isEqualTo(TicketStatus.IN_PROGRESS.name());

        // resolve ticket
        Collection<GetAllTicketsDTO> closedTicketsBeforeResolveInDB = this.ticketService.getAllClosedTickets("testUser");
        this.ticketService.resolveTicket(UUID.fromString(ticketId), "testUser");
        GetTicketDetailsDTO resolvedTicketDetailsDTO = ticketService.getTicketDetails(UUID.fromString(ticketId));
        assertThat(resolvedTicketDetailsDTO.getTicketStatus()).isEqualTo(TicketStatus.CLOSE.name());

        // get all closed tickets
        Collection<GetAllTicketsDTO> closedTicketsInDB = this.ticketService.getAllClosedTickets("testUser");
        assertThat(closedTicketsInDB.size()).isGreaterThanOrEqualTo(closedTicketsBeforeResolveInDB.size());
    }

    @Test
    public void testGetAllTicketsForAUser() {
        Collection<GetAllTicketsDTO> ticketsInDB = this.ticketService.getAllTickets();
        GetAllTicketsDTO getAllTicketsDTO = ticketsInDB.iterator().next();
        String ticketId = getAllTicketsDTO.getTicketId();

        // assign ticket
        this.ticketService.assignTicket(UUID.fromString(ticketId));
        GetTicketDetailsDTO ticketDetailsDTO = ticketService.getTicketDetails(UUID.fromString(ticketId));
        assertThat(ticketDetailsDTO.getAllocatedTo()).isNotNull();
        assertThat(ticketDetailsDTO.getTicketStatus()).isEqualTo(TicketStatus.IN_PROGRESS.name());

        // get all tickets for a user
        Collection<GetAllTicketsDTO> userTickets = this.ticketService.getAllTickets("testUser");
        assertThat(userTickets.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void getAllOpenTicketsForUser() {
        Collection<GetAllTicketsDTO> ticketsInDB = this.ticketService.getAllTickets();
        GetAllTicketsDTO getAllTicketsDTO = ticketsInDB.iterator().next();
        String ticketId = getAllTicketsDTO.getTicketId();

        // assign ticket
        this.ticketService.assignTicket(UUID.fromString(ticketId));
        GetTicketDetailsDTO ticketDetailsDTO = ticketService.getTicketDetails(UUID.fromString(ticketId));
        assertThat(ticketDetailsDTO.getAllocatedTo()).isNotNull();
        assertThat(ticketDetailsDTO.getTicketStatus()).isEqualTo(TicketStatus.IN_PROGRESS.name());

        // get all tickets for a user
        Collection<GetAllTicketsDTO> userTickets = this.ticketService.getAllInProgressTickets("testUser");
        assertThat(userTickets.size()).isGreaterThanOrEqualTo(1);

        // resolve ticket
        this.ticketService.resolveTicket(UUID.fromString(ticketId), "testUser");
        GetTicketDetailsDTO resolvedTicketDetailsDTO = ticketService.getTicketDetails(UUID.fromString(ticketId));
        assertThat(resolvedTicketDetailsDTO.getTicketStatus()).isEqualTo(TicketStatus.CLOSE.name());

        userTickets = this.ticketService.getAllInProgressTickets("testUser");

        Collection<GetAllTicketsDTO> UpdatedUserTickets = this.ticketService.getAllTickets("testUser");
        assertThat(UpdatedUserTickets.size()).isGreaterThan(userTickets.size());
    }

    @Test
    public void getTicketDetailsForAUser() {
        Collection<GetAllTicketsDTO> ticketsInDB = this.ticketService.getAllTickets();
        GetAllTicketsDTO getAllTicketsDTO = ticketsInDB.iterator().next();
        String ticketId = getAllTicketsDTO.getTicketId();
        try {
            GetTicketDetailsDTO getTicketDetails = this.ticketService.getTicketDetails(UUID.fromString(ticketId), "testUser");
            assertThat(getTicketDetails).isNull();
        } catch (ResourceNotFound e) {
        }

        // assign ticket
        this.ticketService.assignTicket(UUID.fromString(ticketId));
        GetTicketDetailsDTO updatedGetTicketDetails = this.ticketService.getTicketDetails(UUID.fromString(ticketId), "testUser");
        assertThat(updatedGetTicketDetails).isNotNull();
    }

    @Test
    public void reOpenTicket() {
        Collection<GetAllTicketsDTO> ticketsInDB = this.ticketService.getAllTickets();
        GetAllTicketsDTO getAllTicketsDTO = ticketsInDB.iterator().next();
        String ticketId = getAllTicketsDTO.getTicketId();

        // assign ticket
        this.ticketService.assignTicket(UUID.fromString(ticketId));
        GetTicketDetailsDTO ticketDetailsDTO = ticketService.getTicketDetails(UUID.fromString(ticketId));
        assertThat(ticketDetailsDTO.getAllocatedTo()).isNotNull();
        assertThat(ticketDetailsDTO.getTicketStatus()).isEqualTo(TicketStatus.IN_PROGRESS.name());

        // get all tickets for a user
        Collection<GetAllTicketsDTO> userTickets = this.ticketService.getAllTickets("testUser");
        assertThat(userTickets.size()).isGreaterThanOrEqualTo(1);

        // resolve ticket
        this.ticketService.resolveTicket(UUID.fromString(ticketId), "testUser");
        GetTicketDetailsDTO resolvedTicketDetailsDTO = ticketService.getTicketDetails(UUID.fromString(ticketId));
        assertThat(resolvedTicketDetailsDTO.getTicketStatus()).isEqualTo(TicketStatus.CLOSE.name());

        // reopen ticket
        Collection<GetAllTicketsDTO> getReOpenedTickets = this.ticketService.getAllTickets("testUser");
        long getReOpenedTicketsCount = getReOpenedTickets.stream().filter(reOpenTicket -> reOpenTicket.getTicketStatus().equals(TicketStatus.RE_OPEN.name())).count();
        this.ticketService.resolveTicket(UUID.fromString(ticketId), "testUser");
        ticketService.reOpenTicket(UUID.fromString(ticketId), "testUser");
        Collection<GetAllTicketsDTO> reOpenedTickets = this.ticketService.getAllTickets("testUser");
        long reOpenedTicketsCount = reOpenedTickets.stream().filter(reOpenTicket -> reOpenTicket.getTicketStatus().equals(TicketStatus.RE_OPEN.name())).count();
        assertThat(getReOpenedTicketsCount).isLessThan(reOpenedTicketsCount);
    }
}
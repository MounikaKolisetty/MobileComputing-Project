package com.rescuer.api.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rescuer.api.dtos.requestDtos.UserInputDto;
import com.rescuer.api.entity.User;
import com.rescuer.api.entity.UserType;
import com.rescuer.api.service.AdminService;
import com.rescuer.api.service.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    private AdminController adminController;

    @Mock
    private AdminService adminService;
    @Mock
    private TicketService ticketService;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.adminController = new AdminController(adminService, ticketService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(adminController)
                .build();
    }

    @Test
    public void addAdmin() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String responseString = objectMapper.writeValueAsString(User
                .builder()
                .userName("admin")
                .password("password")
                .userType(UserType.ADMIN)
                .isUserActive(true).build());
        when(adminService.saveAdmin(any(UserInputDto.class))).thenReturn(User.builder()
                .userName("user").password("pass").isUserActive(true).userType(UserType.ADMIN).build());
        // Act
        MvcResult mockMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/add-admin")
                        .contentType(MediaType.APPLICATION_JSON).content(responseString)).andReturn();

        // Assert
        assertThat(mockMvcResult.getResponse().getStatus()).isEqualTo(201);
    }

    @Test
    public void addRescuer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String responseString = objectMapper.writeValueAsString(User
                .builder()
                .userName("admin")
                .password("password")
                .userType(UserType.ADMIN)
                .isUserActive(true).build());
        when(adminService.saveRescuer(any(UserInputDto.class))).thenReturn(User.builder()
                .userName("user").password("pass").isUserActive(true).userType(UserType.ADMIN).build());
        // Act
        MvcResult mockMvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/add-rescuer")
                        .contentType(MediaType.APPLICATION_JSON).content(responseString)).andReturn();

        // Assert
        assertThat(mockMvcResult.getResponse().getStatus()).isEqualTo(201);
    }
}
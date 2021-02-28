package com.rescuer.api.entity;

import com.rescuer.api.time.ZoneIdConstant;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void shouldAbleToStoreActiveUserDetails() {
        User user = User.builder()
                .userName("testUsername")
                .userType(UserType.ADMIN)
                .isUserActive(true)
                .password("testUserName")
                .zoneId(ZoneIdConstant.CENTRAL_EUROPE)
                .build();

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isNotNull();
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getUserType()).isEqualTo(UserType.ADMIN);
        assertThat(user.getIsUserActive()).isTrue();
        assertThat(user.getZoneId()).isEqualTo(ZoneIdConstant.CENTRAL_EUROPE);
    }

    @Test
    public void shouldAbleToStoreNonActiveUserDetails() {
        User user = User.builder()
                .userName("testUsername")
                .userType(UserType.ADMIN)
                .isUserActive(false)
                .password("testUserName")
                .build();

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isNotNull();
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getUserType()).isEqualTo(UserType.ADMIN);
        assertThat(user.getIsUserActive()).isFalse();
    }

    @Test
    public void shouldAbleToStoreUserIdForActiveUser() {
        User user = User.builder()
                .userName("testUsername")
                .userType(UserType.ADMIN)
                .isUserActive(true)
                .password("testUserName")
                .build();

        user.setUniqueIdentifier(UUID.randomUUID());

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isNotNull();
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getUserType()).isEqualTo(UserType.ADMIN);
        assertThat(user.getIsUserActive()).isTrue();
        assertThat(user.getUniqueIdentifier()).isNotNull();
    }

    @Test
    public void shouldAbleToStoreAuditDetailsForUser() {
        User user = User.builder()
                .userName("testUsername")
                .userType(UserType.ADMIN)
                .isUserActive(false)
                .password("testUserName")
                .build();

        user.setCreatedAt(Instant.now());
        user.setModifiedAt(Instant.now());
        user.setModifiedBy("test user");

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isNotNull();
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getUserType()).isEqualTo(UserType.ADMIN);
        assertThat(user.getIsUserActive()).isFalse();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getModifiedAt()).isNotNull();
        assertThat(user.getModifiedBy()).isEqualTo("test user");
    }

}
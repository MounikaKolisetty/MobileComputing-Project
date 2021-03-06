package com.rescuer.api.entity;

import com.rescuer.api.dtos.requestDtos.UserInputDto;
import com.rescuer.api.time.ZoneIdConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="RescuerUser")
@Inheritance(strategy = InheritanceType.JOINED)
@Where(clause = "isUserActive='true'")
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true, length = 100, nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private Boolean isUserActive;
    @Enumerated(EnumType.STRING)
    private ZoneIdConstant zoneId;

    public static User getUserFromUser(UserInputDto user, BCryptPasswordEncoder bCryptPasswordEncoder) {
        User convertedUser = UserTicketStats.builder().userName(user.getUserName())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .isUserActive(user.getIsUserActive())
                .zoneId(ZoneIdConstant.ASIA_KOLKATA)
                .build();
        UserTicketStats userTicketStats = new UserTicketStats();
        userTicketStats.setUserDetails(convertedUser);
        return userTicketStats;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(this.userType.toString()));
    }

    @Override
    public String getPassword() {
        return new String(this.password);
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

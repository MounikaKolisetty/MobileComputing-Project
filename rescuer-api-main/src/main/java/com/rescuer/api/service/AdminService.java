package com.rescuer.api.service;

import com.rescuer.api.dtos.requestDtos.UserInputDto;
import com.rescuer.api.entity.User;
import com.rescuer.api.entity.UserTicketStats;
import com.rescuer.api.entity.UserType;
import com.rescuer.api.repository.rescuer.UserRepository;
import com.rescuer.api.repository.rescuer.UserTicketStatsRepository;
import com.rescuer.api.web.error.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final UserTicketStatsRepository userTicketStatsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AdminService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                        UserTicketStatsRepository userTicketStatsRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userTicketStatsRepository = userTicketStatsRepository;
    }

    public User saveAdmin(UserInputDto userInputDto) throws IllegalArgumentException, BadRequest {
        User savedUser = new User();
        try {
            log.info("request received to save a user with username: {}", userInputDto.getUserName());
            Optional<User> optionalUser = userRepository.findByUserName(userInputDto.getUserName());
            if (optionalUser.isPresent()) {
                log.info("adding duplicated username username: {}", userInputDto.getUserName());
                throw new BadRequest("A user already exists with the username {}" + userInputDto.getUserName());
            }
            User saveUser = User.getUserFromUser(userInputDto, bCryptPasswordEncoder);
            saveUser.setUserType(UserType.ADMIN);
            return userRepository.save(saveUser);
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    public User saveRescuer(UserInputDto userInputDto) throws IllegalArgumentException, BadRequest {
        User savedUser = new User();
        try {
            log.info("request received to save a user with username: {}", userInputDto.getUserName());
            Optional<User> optionalUser = userRepository.findByUserName(userInputDto.getUserName());
            if (optionalUser.isPresent()) {
                log.info("adding duplicated username username: {}", userInputDto.getUserName());
                throw new BadRequest("A user already exists with the username {}" + userInputDto.getUserName());
            }
            UserTicketStats saveUser = (UserTicketStats) User.getUserFromUser(userInputDto, bCryptPasswordEncoder);
            saveUser.setUserType(UserType.RESCUER);
            return this.userTicketStatsRepository.save(saveUser);
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

}

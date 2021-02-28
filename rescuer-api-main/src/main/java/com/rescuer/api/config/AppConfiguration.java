package com.rescuer.api.config;

import com.rescuer.api.time.SystemZoneDateTime;
import com.rescuer.api.time.ZoneIdHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAspectJAutoProxy
public class AppConfiguration {

    @Bean("systemZoneIdHolder")
    public ZoneIdHolder systemZoneIdHolder() {
        return SystemZoneDateTime.getZoneIdHolder();
    }
    
}

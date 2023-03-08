package de.mscho.toftws.configuration.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class LogConfiguration {

    @Bean
    @Scope("singleton")
    public Logger getLogger() {
        return LoggerFactory.getLogger("toft");
    }
}

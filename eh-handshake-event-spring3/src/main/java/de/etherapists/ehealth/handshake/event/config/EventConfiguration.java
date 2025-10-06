package de.etherapists.ehealth.handshake.event.config;

import de.etherapists.ehealth.handshake.event.activation.ActivationCodeEventMessagePublisher;
import de.etherapists.ehealth.handshake.event.company.CompanyEventMessagePublisher;
import de.etherapists.ehealth.handshake.event.messaging.EventMessagePublisher;
import de.etherapists.ehealth.handshake.event.messaging.IdGenerator;
import de.etherapists.ehealth.handshake.event.user.UserEventMessagePublisher;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {

    @Bean
    public ActivationCodeEventMessagePublisher activationCodeEventMessagePublisher(
            EventMessagePublisher eventMessagePublisher, Clock utcClock, IdGenerator idGenerator) {
        return new ActivationCodeEventMessagePublisher(eventMessagePublisher, utcClock, idGenerator);
    }

    @Bean
    public CompanyEventMessagePublisher companyEventMessagePublisher(
            EventMessagePublisher eventMessagePublisher, Clock utcClock, IdGenerator idGenerator) {
        return new CompanyEventMessagePublisher(eventMessagePublisher, utcClock, idGenerator);
    }

    @Bean
    public UserEventMessagePublisher userEventMessagePublisher(
            EventMessagePublisher eventMessagePublisher, Clock utcClock, IdGenerator idGenerator) {
        return new UserEventMessagePublisher(eventMessagePublisher, utcClock, idGenerator);
    }
}

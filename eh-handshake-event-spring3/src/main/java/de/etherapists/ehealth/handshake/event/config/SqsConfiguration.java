package de.etherapists.ehealth.handshake.event.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.etherapists.ehealth.handshake.event.messaging.EventMessageJsonConverter;
import de.etherapists.ehealth.handshake.event.messaging.EventMessagePublisher;
import de.etherapists.ehealth.handshake.event.messaging.SqsEventMessagePublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfiguration {

  @Value("${aws.region}")
  private String region;

  @Value("${ehealth.handshake.sqs.handshake-queue-url}")
  private String handshakeQueueUrl;

  @Bean
  public SqsClient sqsClient() {
    return SqsClient.builder().region(Region.of(region)).build();
  }

  @Bean
  public EventMessageJsonConverter eventMessageJsonConverter(ObjectMapper objectMapper) {
    return new EventMessageJsonConverter(objectMapper);
  }

  @Bean
  public EventMessagePublisher eventMessagePublisher(
      SqsClient sqsClient, EventMessageJsonConverter serDe) {
    return new SqsEventMessagePublisher(serDe, sqsClient, handshakeQueueUrl);
  }
}

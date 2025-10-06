package de.etherapists.ehealth.handshake.event.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class LocalStackSqsConfiguration {
  @Value("${aws.region}")
  private String region;

  @Value("${ehealth.handshake.sqs.handshake-queue-url}")
  private String handshakeQueueUrl;

  @Value("${aws.accessKeyId}")
  private String accessKeyId;

  @Value("${aws.secretKey}")
  private String secretKey;

  @Value("${override.aws.sqs.endpoint}")
  private String endpoint;

  @Bean
  public SqsClient sqsClient() {
    return SqsClient.builder()
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretKey)))
        .region(Region.of(region))
        .build();
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

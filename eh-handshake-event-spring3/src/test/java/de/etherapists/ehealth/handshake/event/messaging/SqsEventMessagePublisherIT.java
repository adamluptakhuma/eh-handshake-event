package de.etherapists.ehealth.handshake.event.messaging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

import de.etherapists.ehealth.handshake.event.activation.ActivationCodeAssignedPayload;
import de.etherapists.ehealth.handshake.event.config.ClockConfiguration;
import de.etherapists.ehealth.handshake.event.config.EventConfiguration;
import de.etherapists.ehealth.handshake.event.config.IdGeneratorConfiguration;
import de.etherapists.ehealth.handshake.event.config.ObjectMapperConfiguration;
import de.etherapists.ehealth.handshake.event.config.RegionConfiguration;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Testcontainers
@SpringBootTest(
    classes = {
      ClockConfiguration.class,
      EventConfiguration.class,
      RegionConfiguration.class,
      LocalStackSqsConfiguration.class,
      IdGeneratorConfiguration.class,
      ObjectMapperConfiguration.class
    })
class SqsEventMessagePublisherIT {
  @Autowired private EventMessagePublisher publisher;
  @Autowired private SqsClient sqsClient;
  @Autowired private EventMessageJsonConverter eventMessageJsonConverter;

  private static String queueUrl;

  @BeforeAll
  static void setupQueue() {
    var sqsClient =
        SqsClient.builder()
            .endpointOverride(localstack.getEndpointOverride(SQS))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        localstack.getAccessKey(), localstack.getSecretKey())))
            .region(Region.of(localstack.getRegion()))
            .build();

    queueUrl =
        sqsClient
            .createQueue(
                CreateQueueRequest.builder()
                    .queueName("test-queue.fifo")
                    .attributes(Map.of(QueueAttributeName.FIFO_QUEUE, String.valueOf(true)))
                    .build())
            .queueUrl();
  }

  @Container
  static LocalStackContainer localstack =
      new LocalStackContainer("latest")
          .withServices(SQS)
          .withReuse(true); // keeps container alive across tests

  @DynamicPropertySource
  static void registerAwsProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.region", localstack::getRegion);
    registry.add("aws.accessKeyId", localstack::getAccessKey);
    registry.add("aws.secretKey", localstack::getSecretKey);
    registry.add("override.aws.sqs.endpoint", () -> localstack.getEndpointOverride(SQS).toString());
    registry.add("ehealth.handshake.sqs.handshake-queue-url", () -> queueUrl);
  }

  @Test
  void shouldSendMessageToQueueSuccessfully() {
    // given
    var payload =
        ActivationCodeAssignedPayload.builder()
            .userId(1)
            .awsRegion(Region.EU_CENTRAL_1.id())
            .build();

    var message =
        EventMessage.<ActivationCodeAssignedPayload>builder()
            .eventType(EventType.ACTIVATION_CODE_CREATED)
            .entityType(EntityType.ACTIVATION_CODE)
            .entityId("123")
            .messageId(java.util.UUID.randomUUID().toString())
            .payload(payload)
            .build();

    // when
    var result = publisher.send(message);

    // then
    assertThat(result.isSuccess()).isTrue();

    ReceiveMessageResponse response =
        sqsClient.receiveMessage(
            ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(1).build());

    assertThat(response.messages()).hasSize(1);
    String body = response.messages().get(0).body();
    assertThat(eventMessageJsonConverter.deserialize(body, ActivationCodeAssignedPayload.class))
        .isEqualTo(message);
  }
}

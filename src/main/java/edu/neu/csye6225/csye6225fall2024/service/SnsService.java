package edu.neu.csye6225.csye6225fall2024.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.time.LocalDateTime;

@Service
public class SnsService {

    private final SnsClient snsClient;
    private final TokenGenerateService tokenService;

    @Value("${aws.sns.topic.arn}") // read SNS Topic ARN
    private String topicArn;

    public SnsService(SnsClient snsClient, TokenGenerateService tokenService) {
        this.snsClient = snsClient;
        this.tokenService = tokenService;
    }
    /**
     * release SNS message
     * @return SNS message ID
     */
    public String publishEvent(String email ,String message) {
        // pub request
        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .build();

        PublishResponse response = snsClient.publish(request);

        String messageId = response.messageId();
        if (messageId == null || messageId.isEmpty()) {
            throw new IllegalStateException("SNS publish failed: messageId is null or empty");
        }

        LocalDateTime sentAt = LocalDateTime.now(); // add 2 minutes in updateTokenExpiry function
        tokenService.updateTokenExpiry(email, sentAt);

        System.out.println("SNS message published successfully. Message ID: " + messageId);

        return response.messageId();
    }
}

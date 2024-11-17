package edu.neu.csye6225.csye6225fall2024.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SnsService {

    private final SnsClient snsClient;

    @Value("${aws.sns.topic.arn}") // read SNS Topic ARN
    private String topicArn;

    public SnsService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    /**
     * release SNS message
     * @param userEmail email
     * @return message ID
     */
    public String publishEvent(String userEmail) {
        // set message
        String message = String.format("{\"userEmail\": \"%s\", \"event\": \"UserRegistered\"}", userEmail);

        // pub request
        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .build();

        PublishResponse response = snsClient.publish(request);

        return response.messageId();
    }
}

package edu.neu.csye6225.csye6225fall2024.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SnsService {

    private final AmazonSNS snsClient;
    private final String topicArn;

    public SnsService(@Value("${aws.sns.topic.arn}") String topicArn) {
        this.snsClient = AmazonSNSClientBuilder.defaultClient();
        this.topicArn = topicArn;
    }

    public void publishUserCreatedMessage(String email) {
        String message = String.format("{\"email\": \"%s\"}", email);
        PublishRequest publishRequest = new PublishRequest(topicArn, message);
        snsClient.publish(publishRequest);
        System.out.println("Message published to SNS: " + message);
    }
}

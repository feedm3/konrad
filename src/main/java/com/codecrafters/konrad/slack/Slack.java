package com.codecrafters.konrad.slack;

import com.codecrafters.konrad.KonradProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This class is used to send messages to slack.
 *
 * @author Fabian Dietenberger
 */
@Component
public class Slack {

    private final RestTemplate restTemplate;
    private final KonradProperties properties;

    @Autowired
    public Slack(final RestTemplate restTemplate, final KonradProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public void send(final SlackMessage message) {
        restTemplate.postForEntity(properties.getWebhookurl(), message, null);
    }

    public void send(final String text) {
        final SlackMessage message = SlackMessage.builder().text(text).build();
        send(message);
    }
}

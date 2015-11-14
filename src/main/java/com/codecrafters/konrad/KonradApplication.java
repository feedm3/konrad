package com.codecrafters.konrad;

import com.codecrafters.konrad.slack.SlackMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class KonradApplication {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(final String[] args) {
        SpringApplication.run(KonradApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(final KonradProperties properties, final RestTemplate restTemplate) {
        return args -> {
            final SlackMessage message = SlackMessage.builder().text("konrad is up and running").build();
            try {
                restTemplate.postForEntity(properties.getWebhookurl(), message, null);
            } catch (Exception ignored) {
                logger.error("Slack Webhook URL not working. Plase check your webhook URL and restart konrad");
                System.exit(0);
            }
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

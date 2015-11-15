package com.codecrafters.konrad;

import com.codecrafters.konrad.slack.Slack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import static com.google.common.base.Preconditions.checkArgument;

@SpringBootApplication
@EnableScheduling
public class KonradApplication {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(final String[] args) {
        SpringApplication.run(KonradApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(final KonradProperties properties, final Slack slack) {
        return args -> {
            checkArgument(properties.getWebhookurl() != null, "Slack webhook url must not be null");
            checkArgument(properties.getUrls() != null, "URLs to check must not be null");

            try {
                slack.send("konrad is up and running");
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

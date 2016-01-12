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

import java.util.concurrent.TimeUnit;

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
                slack.send(createStartingMessage(properties));
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

    /**
     * Create a starting message which will be send to slack when the application has started.
     *
     * @param konradProperties the properties of the application to determine the message
     * @return the message
     */
    private String createStartingMessage(final KonradProperties konradProperties) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("konrad is up and running. ");
        stringBuilder.append("konrad will check ");
        stringBuilder.append(konradProperties.getUrls().size());
        stringBuilder.append(" urls every ");
        stringBuilder.append(millisToString(konradProperties.getInterval()));
        if (konradProperties.isReportOnlyWhenBrokenUrls()) {
            stringBuilder.append(" but report to you only when a url is down.");
        } else {
            stringBuilder.append(" and message you the result. ");
            stringBuilder.append("If you get too many messages you can set ");
            stringBuilder.append("a property to only get messages when there are broken urls.");
        }
        stringBuilder.append(" konrad will also send you a report of all URLs every midnight. ");
        stringBuilder.append("If you dont get a message at midnight konrad may be down! ");
        stringBuilder.append("Feel free to join this project at github.com/feedm3/konrad");
        return stringBuilder.toString();
    }

    /**
     * Format milli seconds to a string.
     *
     * @param milliSeconds the milli seconds to format
     * @return the formatted string. examples: 20 secs; 05:00 min; 1:30 min
     */
    private String millisToString(final long milliSeconds) {
        long seconds = milliSeconds / 1000;
        if (seconds > 60) {
            return String.format("%02d:%02d min",
                    TimeUnit.MILLISECONDS.toMinutes(milliSeconds),
                    TimeUnit.MILLISECONDS.toSeconds(milliSeconds) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds))
            );
        }
        return seconds + " secs";
    }
}

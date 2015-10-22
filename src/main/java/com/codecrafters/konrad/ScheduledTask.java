package com.codecrafters.konrad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This class is used to request url's and check if they are up or down.
 *
 *  @author Fabian Dietenberger
 */
@Component
public class ScheduledTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UrlChecker urlChecker;

    @Autowired
    KonradProperties properties;

    @Scheduled(fixedRate = 10000)
    public void reportUrlUpStatus() {
        logger.info("Start checking Urls...");

        final StringBuilder slackMessage = new StringBuilder();
        for (final String urlToCheck : properties.getUrls()) {
            boolean urlOk = urlChecker.isUrlOk(urlToCheck);
            if (urlOk) {
                slackMessage.append(":white_check_mark: ").append(urlToCheck).append("\n");
            } else {
                slackMessage.append(":no_entry: ").append(urlToCheck).append("\n");
            }
        }

        final SlackMessage message = new SlackMessage();
        message.setUsername("konrad");
        message.setText(slackMessage.toString());

        // TODO muss auch in try catch, falls slack url falsch ist
        restTemplate.postForEntity(properties.getWebhookurl(), message, null);
    }

}



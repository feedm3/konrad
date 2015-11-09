package com.codecrafters.konrad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

    @Scheduled(fixedRateString = "${konrad.interval}")
    public void reportUrlUpStatus() {
        logger.info("Start checking Urls...");

        final Map<String, Boolean> urlOkResults = new HashMap<>();
        for (final String urlToCheck : properties.getUrls()) {
            boolean urlOk = urlChecker.isUrlOk(urlToCheck);
            urlOkResults.put(urlToCheck, urlOk);
        }

        final SlackMessage message = SlackMessage.build(urlOkResults);
        restTemplate.postForEntity(properties.getWebhookurl(), message, null);
    }

}



package com.codecrafters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * This class is used to request url's and check if they are up or down.
 *
 *  @author Fabian Dietenberger
 */
@Component
public class ScheduledUrlCheckTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UptimeCheckerProperties properties;

    @Scheduled(fixedRate = 10000)
    public void reportUrlUpStatus() {
        logger.info("Start checking Urls...");

        for (final String urlToCheck : properties.getUrls()) {
            ResponseEntity responseEntity = null;
            try {
                responseEntity = restTemplate.getForEntity(URI.create(urlToCheck), null);
            } catch (Exception ignored) {
                // RestTemplate will throw an error if the request didn't work.
                // We don't need the information from this error, all information
                // is in the ResponseEntity object.
            } finally {
                if (isResponseOk(responseEntity)) {
                    logger.info(urlToCheck + " is up");
                } else {
                    logger.error(urlToCheck + " is down");
                }
            }
        }
    }

    private boolean isResponseOk(final ResponseEntity responseEntity) {
        if (responseEntity == null) {
            return false;
        }
        final HttpStatus.Series series = responseEntity.getStatusCode().series();
        return !HttpStatus.Series.CLIENT_ERROR.equals(series)
                && !HttpStatus.Series.SERVER_ERROR.equals(series);
    }
}



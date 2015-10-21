package com.codecrafters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is used to request url's and check if they are up or down.
 *
 *  @author Fabian Dietenberger
 */
@Component
public class ScheduledUrlCheckTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 10000)
    public void reportUrlUpStatus() {
        logger.info("Checking Url " + dateFormat.format(new Date()));

        ResponseEntity responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(URI.create("https://google.com"), null);
        } catch (Exception ignored) {
            // RestTemplate will throw an error if the request didn't work.
            // We don't need the information from this error, all information
            // is in the ResponseEntity object.
        } finally {
            if (isResponseOk(responseEntity)) {
                logger.info("Url is up");
            } else {
                logger.error("Url is down");
            }
        }
    }

    private boolean isResponseOk(final ResponseEntity responseEntity) {
        return responseEntity != null
                && responseEntity.getStatusCode() == HttpStatus.OK;
    }
}



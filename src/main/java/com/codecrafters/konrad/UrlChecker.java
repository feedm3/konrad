package com.codecrafters.konrad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * This class is used the check if a URL is up or down.
 *
 * @author Fabian Dietenberger
 */
@Component
public class UrlChecker {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate;

    @Autowired
    public UrlChecker(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isUrlOk(final String url) {
        boolean urlOk = false;
        if (!StringUtils.isEmpty(url)) {
            ResponseEntity responseEntity = null;
            try {
                responseEntity = restTemplate.getForEntity(URI.create(url), null);
            } catch (Exception ignored) {
                // RestTemplate will throw an error if the request didn't work.
                // We don't need the information from this error, all information
                // is in the ResponseEntity object.
            } finally {
                if (isResponseOk(responseEntity)) {
                    urlOk = true;
                }
            }
        }
        return urlOk;
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

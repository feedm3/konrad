package com.codecrafters.konrad;

import com.codecrafters.konrad.slack.Slack;
import com.codecrafters.konrad.slack.SlackMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This class is used to request url's and check if they are up or down.
 *
 *  @author Fabian Dietenberger
 */
@Component
public class ScheduledTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Slack slack;

    private final UrlChecker urlChecker;

    @Autowired
    public ScheduledTask(final Slack slack, final UrlChecker urlChecker) {
        this.slack = slack;
        this.urlChecker = urlChecker;
    }

    @Scheduled(fixedRateString = "${konrad.interval}")
    public void reportUrlUpStatus() {
        logger.info("Start checking Urls...");

        final Map<String, Boolean> urlOkResults = urlChecker.checkUrlsFromProperties();

        final SlackMessage message = SlackMessage
                                        .builder()
                                        .urlStatusesAsText(urlOkResults)
                                        .displayOnlyBadUrls()
                                        .build();
        slack.send(message);
    }

    // sec min hour
    @Scheduled(cron = "0 44 19 * * *")
    public void reportDailyStatus() {
        logger.info("Daily Check: Report all URL statuses to slack");

        final Map<String, Boolean> urlOkResults = urlChecker.checkUrlsFromProperties();

        final SlackMessage message = SlackMessage
                                        .builder()
                                        .urlStatusesAsText(urlOkResults)
                                        .build();
        slack.send(message);
    }
}



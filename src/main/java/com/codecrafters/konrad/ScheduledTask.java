package com.codecrafters.konrad;

import com.codecrafters.konrad.slack.Slack;
import com.codecrafters.konrad.slack.SlackMessage;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class is used to schedule the {@link UrlChecker}.
 * <p>
 * The scheduler has 2 schedules. One gets executed at a given interval (from the properties)
 * and the other one gets executed every day at midnight.
 * </p>
 *
 * @author Fabian Dietenberger
 */
@Component
public class ScheduledTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Slack slack;
    private final UrlChecker urlChecker;
    private final KonradProperties properties;

    @Autowired
    public ScheduledTask(final Slack slack, final UrlChecker urlChecker, final KonradProperties properties) {
        this.slack = slack;
        this.urlChecker = urlChecker;
        this.properties = properties;
    }

    @Scheduled(fixedRateString = "${konrad.interval}")
    public void reportIntervalStatus() {
        logger.info("Checking URLs and creating interval report");

        final Multimap<Boolean, String> urlStatusResults = urlChecker.checkUrlsFromProperties();
        if (!properties.isReportOnlyWhenBrokenUrls() ||
                (properties.isReportOnlyWhenBrokenUrls() && !urlStatusResults.get(false).isEmpty())) {
            // report if we do not need to check if there are broken urls or
            // we need to check and there are broken urls
            createMessageAndSendToSlack(urlStatusResults);
        }
    }

    // cron: sec min hour
    @Scheduled(cron = "0 0 0 * * *")
    public void reportDailyStatus() {
        logger.info("Checking URLs and creating daily report");

        final Multimap<Boolean, String> urlStatusResults = urlChecker.checkUrlsFromProperties();
        createMessageAndSendToSlack(urlStatusResults);
    }

    private void createMessageAndSendToSlack(final Multimap<Boolean, String> urlStatusResults) {
        final SlackMessage message = SlackMessage
                .builder()
                .urlStatusesAsText(urlStatusResults)
                .build();
        slack.send(message);
    }
}



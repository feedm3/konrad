package com.codecrafters.konrad.slack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fabian Dietenberger
 */
public class SlackMessageBuilder {

    private final String username = "konrad";
    private String text;
    private Map<String, Boolean> urlStatuses;

    private boolean displayGoodUrls;
    private KonradMessageType messageType;

    public enum KonradMessageType {
        INTERVAL,
        DAILY
    }

    public SlackMessageBuilder() {
        displayGoodUrls = false;
        messageType = KonradMessageType.INTERVAL;
        urlStatuses = new HashMap<>();
    }

    public SlackMessageBuilder text(final String text) {
        this.text = text;
        return this;
    }

    public SlackMessageBuilder messageType(final KonradMessageType konradMessageType) {
        this.messageType = konradMessageType;
        return this;
    }

    public SlackMessageBuilder urlStatusesAsText(final Map<String, Boolean> urlStatuses) {
        if (urlStatuses == null) throw new IllegalStateException("URL Statueses Map must not be null");
        this.urlStatuses = urlStatuses;
        return this;
    }

    public SlackMessageBuilder onlyBadUrls() {
        this.displayGoodUrls = false;
        return this;
    }


    public SlackMessage build() {
        final SlackMessage message = new SlackMessage();
        message.setText(text);
        message.setUsername(username);

        if (!urlStatuses.isEmpty()) {
            final StringBuilder urlResultListMessage = new StringBuilder();
            urlStatuses.forEach((url, isOk) -> urlResultListMessage.append(getMessageToUrlStatus(url, isOk)));
            message.setText(urlResultListMessage.toString());
        }
        return message;
    }

    private String getMessageToUrlStatus(final String url, final boolean isOk) {
        if (isOk) {
            return ":white_check_mark: " + url + "\n";
        }
        if (displayGoodUrls) {
            return "";
        }
        return ":no_entry: " + url + "\n";
    }

}

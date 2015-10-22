package com.codecrafters.konrad;

import java.util.Map;

/**
 * This class is used to represent a message for the slack webhook api.
 *
 * https://api.slack.com/incoming-webhooks
 *
 * @author Fabian Dietenberger
 */
public class SlackMessage {

    private String text;
    private String username;

    private SlackMessage() {
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Get an empty message object.
     *
     * @return a new message object
     */
    public static SlackMessage build(final String text) {
        final SlackMessage message = new SlackMessage();
        message.setUsername("konrad");
        message.setText(text);
        return message;
    }

    /**
     * Get a pre-build message object.
     *
     * @param urlStatuses to status mapping to each url
     * @return a new message object
     */
    public static SlackMessage build(final Map<String, Boolean> urlStatuses) {
        final SlackMessage message = new SlackMessage();
        message.setUsername("konrad");

        final StringBuilder urlResultListMessage = new StringBuilder();
        urlStatuses.forEach((url, isOk) -> urlResultListMessage.append(getMessageToUrlStatus(url, isOk)));
        message.setText(urlResultListMessage.toString());

        return message;
    }

    private static String getMessageToUrlStatus(final String url, final boolean isOk) {
        if (isOk) {
            return ":white_check_mark: " + url + "\n";
        }
        return ":no_entry: " + url + "\n";
    }
}

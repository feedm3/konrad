package com.codecrafters.konrad;

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

    public SlackMessage() {
    }

    public SlackMessage(final String text) {
        this.text = text;
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
}

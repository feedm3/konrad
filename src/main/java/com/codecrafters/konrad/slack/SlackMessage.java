package com.codecrafters.konrad.slack;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to represent a <a href="https://api.slack.com/incoming-webhooks">message</a> for the slack webhook api.
 *
 * To create a new instance you have to use the static {@link #builder()} method.
 *
 * @author Fabian Dietenberger
 */
public class SlackMessage {

    private String text;
    private String username;
    private List<Attachment> attachments;

    /* package */ SlackMessage() {
        attachments = new ArrayList<>();
    }

    /**
     * Get a new {@link SlackMessageBuilder} to create
     * a new instace of a {@link SlackMessage}
     *
     * @return a new {@link SlackMessageBuilder}
     */
    public static SlackMessageBuilder builder() {
        return new SlackMessageBuilder();
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * An <a href="https://api.slack.com/docs/attachments">attachment</a> is display under the message text.
     */
    public static class Attachment {

        private String text;
        private String color;

        public String getText() {
            return text;
        }

        public void setText(final String text) {
            this.text = text;
        }

        public String getColor() {
            return color;
        }

        public void setColor(final String color) {
            this.color = color;
        }
    }
}

package com.codecrafters.konrad.slack;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to represent a <a href="https://api.slack.com/incoming-webhooks">message</a> for the slack webhook api.
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


    /**
     * An <a href="https://api.slack.com/docs/attachments">attachment</a> is display under the message text.
     */
    class Attachment {

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

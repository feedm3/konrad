package com.codecrafters.konrad.slack;

import com.google.common.base.CharMatcher;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class is used to create new instances of a {@link SlackMessage}.
 *
 * @author Fabian Dietenberger
 */
public class SlackMessageBuilder {

    private final String username = "konrad";
    private String text;
    private Map<String, Boolean> urlStatuses;

    private boolean displayOnlyGoodUrls;
    private boolean containsOnlyGoodUrls;
    private KonradMessageType messageType;

    public enum KonradMessageType {
        INTERVAL,
        DAILY
    }

    public SlackMessageBuilder() {
        displayOnlyGoodUrls = false;
        containsOnlyGoodUrls = true;
        messageType = KonradMessageType.INTERVAL;
        urlStatuses = new HashMap<>();
    }

    public SlackMessageBuilder text(final String text) {
        checkArgument(text != null, "Text must not be null");
        this.text = text;
        return this;
    }

    public SlackMessageBuilder messageType(final KonradMessageType konradMessageType) {
        this.messageType = konradMessageType;
        return this;
    }

    public SlackMessageBuilder urlStatusesAsText(final Map<String, Boolean> urlStatuses) {
        checkArgument(urlStatuses != null, "URL statuses map must not be null");
        this.urlStatuses = urlStatuses;
        return this;
    }

    public SlackMessageBuilder displayOnlyBadUrls() {
        this.displayOnlyGoodUrls = false;
        return this;
    }


    public SlackMessage build() {
        final SlackMessage message = new SlackMessage();
        message.setText(text);
        message.setUsername(username);

        if (!urlStatuses.isEmpty()) {
            final StringBuilder goodUrlsText = new StringBuilder();
            final StringBuilder badUrlsText = new StringBuilder();

            // TODO use a Joiner
            urlStatuses.forEach((url, isOk) -> {
                if (isOk) {
                    goodUrlsText
                            .append(url)
                            .append("\n");
                } else {
                    badUrlsText
                            .append(url)
                            .append("\n");
                    containsOnlyGoodUrls = false;
                }
            });

            final SlackMessage.Attachment goodAttachment = new SlackMessage.Attachment();
            goodAttachment.setColor("good");
            String goodUrlsTrimmedText = CharMatcher.anyOf(" \n").trimAndCollapseFrom(goodUrlsText.toString(), ' ');
            goodAttachment.setText(goodUrlsTrimmedText);

            final SlackMessage.Attachment badAttachment = new SlackMessage.Attachment();
            badAttachment.setColor("danger");
            String badUrlsTrimmedText = CharMatcher.anyOf(" \n").trimAndCollapseFrom(badUrlsText.toString(), ' ');
            badAttachment.setText(badUrlsTrimmedText);

            if (containsOnlyGoodUrls) {
                message.setText("*Results*\nAll URLs are ok");
            } else {
                message.setText("*Results*\nSome URLs are not reachable");
            }

            message.getAttachments().add(badAttachment);
            message.getAttachments().add(goodAttachment);
        }
        return message;
    }
}

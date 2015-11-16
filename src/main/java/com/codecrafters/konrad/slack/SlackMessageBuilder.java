package com.codecrafters.konrad.slack;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class is used to create new instances of a {@link SlackMessage}.
 *
 * @author Fabian Dietenberger
 */
public class SlackMessageBuilder {

    private final String username = "konrad";
    private String text;
    private Multimap<Boolean, String> urlStatuses;

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
        urlStatuses = HashMultimap.create();
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

    public SlackMessageBuilder urlStatusesAsText(final Multimap<Boolean, String> urlStatuses) {
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
            final List<String> goodUrls = ImmutableList.copyOf(urlStatuses.get(true));
            final List<String> badUrls = ImmutableList.copyOf(urlStatuses.get(false));

            final String goodUrlsText = Joiner.on("\n").join(goodUrls);
            final String badUrlsText = Joiner.on("\n").join(badUrls);

            containsOnlyGoodUrls = badUrls.isEmpty();

            final SlackMessage.Attachment goodAttachment = new SlackMessage.Attachment();
            goodAttachment.setColor("good");
            goodAttachment.setText(goodUrlsText);

            final SlackMessage.Attachment badAttachment = new SlackMessage.Attachment();
            badAttachment.setColor("danger");
            badAttachment.setText(badUrlsText);

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

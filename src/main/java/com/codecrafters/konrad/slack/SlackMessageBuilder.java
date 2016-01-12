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

    private final String USERNAME = "konrad";

    private String text;
    private Multimap<Boolean, String> urlStatuses;

    private boolean displayOnlyBrokenUrls;

    public SlackMessageBuilder() {
        displayOnlyBrokenUrls = false;
        urlStatuses = HashMultimap.create();
    }

    public SlackMessageBuilder text(final String text) {
        checkArgument(text != null, "Text must not be null");
        this.text = text;
        return this;
    }

    /**
     * Put the statuses of the urls into the {@link SlackMessage}s text field.
     * This will override the text that can be set in the {@link #text} method.
     *
     * @param urlStatuses the status (true = ok, false = down) and the corresponding urls
     */
    public SlackMessageBuilder urlStatusesAsText(final Multimap<Boolean, String> urlStatuses) {
        checkArgument(urlStatuses != null, "URL statuses map must not be null");
        this.urlStatuses = urlStatuses;
        return this;
    }

    public SlackMessageBuilder displayOnlyBrokenUrls() {
        this.displayOnlyBrokenUrls = true;
        return this;
    }

    public SlackMessage build() {
        final SlackMessage message = new SlackMessage();
        message.setText(text);
        message.setUsername(USERNAME);

        if (!urlStatuses.isEmpty()) {
            final List<String> goodUrls = ImmutableList.copyOf(urlStatuses.get(true));
            final List<String> brokenUrls = ImmutableList.copyOf(urlStatuses.get(false));

            final String goodUrlsText = Joiner.on("\n").join(goodUrls);
            final String brokenUrlsText = Joiner.on("\n").join(brokenUrls);


            final SlackMessage.Attachment goodAttachment = new SlackMessage.Attachment();
            goodAttachment.setColor("good");
            goodAttachment.setText(goodUrlsText);

            final SlackMessage.Attachment badAttachment = new SlackMessage.Attachment();
            badAttachment.setColor("danger");
            badAttachment.setText(brokenUrlsText);

            final boolean containsGoodUrls = !goodUrls.isEmpty();
            final boolean containsBrokenUrls = !brokenUrls.isEmpty();

            if (containsGoodUrls && !containsBrokenUrls) {
                message.setText("*All URLs are ok*");
                message.getAttachments().add(goodAttachment);
            } else if (!containsGoodUrls && containsBrokenUrls) {
                message.setText("*All URLs are down*");
                message.getAttachments().add(badAttachment);
            } else {
                message.setText("*Some URLs are not reachable*");
                message.getAttachments().add(goodAttachment);
                message.getAttachments().add(badAttachment);
            }
        }
        return message;
    }
}

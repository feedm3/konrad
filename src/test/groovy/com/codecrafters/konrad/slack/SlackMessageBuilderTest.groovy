package com.codecrafters.konrad.slack

import spock.lang.Shared
import spock.lang.Specification

import static com.codecrafters.konrad.slack.SlackMessageBuilder.KonradMessageType.*

/**
 * @author Fabian Dietenberger
 */
class SlackMessageBuilderTest extends Specification {

    @Shared
    SlackMessageBuilder builder = new SlackMessageBuilder()

    def "create a message with text only"() {
        given:
        def message = builder.text("This is text").build()

        expect:
        message.text == "This is text"
    }

    def "create a interval message"() {
        given:
        Map<String, Boolean> urlStatueses = new HashMap<>()
        urlStatueses.put("www.google.de", true)
        urlStatueses.put("www.dadwwdaiodjawdj.grdg", false)

        when:
        def message = builder
                .urlStatusesAsText(urlStatueses)
                .messageType(INTERVAL)
                .build()

        then:
        message.text == "*Results*\nSome URLs are not reachable"
        message.getAttachments().get(0).text == "www.dadwwdaiodjawdj.grdg"
        message.getAttachments().get(0).color == "danger"
        message.getAttachments().get(1).text == "www.google.de"
        message.getAttachments().get(1).color == "good"


    }

    def "create a daily message"() {

    }

    def "throw errors on illegal arguments"() {
        when:
        builder.text(null).build()

        then:
        thrown(IllegalArgumentException)

        when:
        builder.urlStatusesAsText(null)

        then:
        thrown(IllegalArgumentException)
    }
}

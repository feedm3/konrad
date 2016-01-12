package com.codecrafters.konrad.slack
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import spock.lang.Specification
/**
 * @author Fabian Dietenberger
 */
class SlackMessageBuilderTest extends Specification {

    def "create a message with text only"() {
        given:
        def message = SlackMessage.builder()
                                    .text("This is text")
                                    .build()

        expect:
        message.text == "This is text"
    }

    def "create a interval message with only reachable URLs"() {
        given:
        Multimap<Boolean, String> urlStatueses = HashMultimap.create()
        urlStatueses.put(Boolean.TRUE, "www.google.com")
        urlStatueses.put(Boolean.TRUE, "www.facebook.com")

        when:
        def message = SlackMessage.builder()
                                    .urlStatusesAsText(urlStatueses)
                                    .build()

        then:
        message.text == "*All URLs are ok*"
        message.getAttachments().size() == 1
        message.getAttachments().get(0).text == "www.google.com\nwww.facebook.com"
        message.getAttachments().get(0).color == "good"
    }

    def "create a interval message with only broken URLs"() {
        given:
        Multimap<Boolean, String> urlStatueses = HashMultimap.create()
        urlStatueses.put(Boolean.FALSE, "fes")
        urlStatueses.put(Boolean.FALSE, "w")

        when:
        def message = SlackMessage.builder()
                                    .urlStatusesAsText(urlStatueses)
                                    .build()

        then:
        message.text == "*All URLs are down*"
        message.getAttachments().size() == 1
        message.getAttachments().get(0).text == "fes\nw"
        message.getAttachments().get(0).color == "danger"
    }

    def "create a interval message with reachable and broken URLs"() {
        given:
        Multimap<Boolean, String> urlStatueses = HashMultimap.create()
        urlStatueses.put(Boolean.TRUE, "www.google.com")
        urlStatueses.put(Boolean.FALSE, "www.wda134")

        when:
        def message = SlackMessage.builder()
                                    .urlStatusesAsText(urlStatueses)
                                    .build()

        then:
        message.text == "*Some URLs are not reachable*"
        message.getAttachments().get(0).text == "www.google.com"
        message.getAttachments().get(0).color == "good"
        message.getAttachments().get(1).text == "www.wda134"
        message.getAttachments().get(1).color == "danger"
    }

    def "create a daily message"() {

    }

    def "throw errors on illegal arguments"() {
        when:
        SlackMessage.builder().text(null).build()

        then:
        thrown(IllegalArgumentException)

        when:
        SlackMessage.builder().urlStatusesAsText(null)

        then:
        thrown(IllegalArgumentException)
    }
}

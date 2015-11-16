package com.codecrafters.konrad

import com.google.common.collect.Multimap
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
/**
 * This class is used as test for the UrlChecker
 *
 * @author Fabian Dietenberger
 */
class UrlCheckerTest extends Specification {

    def "single urls must be validated correctly"() {
        given:
        def restTemplate = Stub(RestTemplate)
        def urlChecker = new UrlChecker(restTemplate, null)

        and:
        restTemplate.getForEntity(_, _) >> { return new ResponseEntity<>(httpStatus) }

        expect:
        urlChecker.isUrlOk(url) == isOk

        where:
        url                               | httpStatus                   | isOk
        "http://google.com"               | HttpStatus.OK                | true
        "http://bit.ly/DefEditionSpotify" | HttpStatus.MOVED_PERMANENTLY | true
        null                              | _                            | false
        ""                                | _                            | false
        "dawdwad"                         | HttpStatus.NOT_FOUND         | false
    }

    def "urls must be loaded from the properties and then checked correctly"() {
        given:
        def properties = new KonradProperties()
        def restTemplate = Stub(RestTemplate)
        def urlChecker = new UrlChecker(restTemplate, properties)

        and:
        restTemplate.getForEntity(_, _) >> { return new ResponseEntity<>(httpStatus) }
        properties.urls = [url]

        when:
        Multimap<Boolean, String> urlOkStatuses = urlChecker.checkUrlsFromProperties()

        then:
        urlOkStatuses.get(isOk) != null
        urlOkStatuses.get(isOk).getAt(0) == url

        where:
        url                               | httpStatus                   | isOk
        "http://google.com"               | HttpStatus.OK                | true
        "http://bit.ly/DefEditionSpotify" | HttpStatus.MOVED_PERMANENTLY | true
        null                              | _                            | false
        ""                                | _                            | false
        "dawdwad"                         | HttpStatus.NOT_FOUND         | false
    }

    def "restTemplate should be called"() {
        given:
        def restTemplate = Mock(RestTemplate)
        def urlChecker = new UrlChecker(restTemplate, null)

        when:
        urlChecker.isUrlOk("http://google.com")

        then:
        1 * restTemplate.getForEntity(_, _)
    }
}

package com.codecrafters.konrad

import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification
/**
 * This class is used as test for the UrlChecker
 *
 * @author Fabian Dietenberger
 */
class UrlCheckerTest extends Specification {

    @Shared
    UrlChecker urlChecker

    void setup() {
        urlChecker = new UrlChecker(new RestTemplate())
    }

    def "working urls must be validated as ok"() {
        expect:
        urlChecker.isUrlOk(url) == isOk

        where:
        isOk  | url
        true  | "http://google.com"
        true  | "http://facebook.com"
        true  | "https://www.youtube.com/watch?v=a-xWhG4UU_Y"
        false | null
        false | ""
        false | "nxyaeww"
        false | "15238csdu"
        false | "http:/google.com"
    }
}

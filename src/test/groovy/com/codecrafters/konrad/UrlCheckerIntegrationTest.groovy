package com.codecrafters.konrad

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification


/**
 * @author Fabian Dietenberger
 */
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = KonradApplication.class)
@ActiveProfiles("test")
@IntegrationTest
class UrlCheckerIntegrationTest extends Specification {

    @Autowired
    UrlChecker urlChecker

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
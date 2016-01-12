package com.codecrafters.konrad;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This class is used to access the properties.
 *
 * @author Fabian Dietenberger
 */
@Configuration
@ConfigurationProperties(prefix="konrad")
public class KonradProperties {

    private List<String> urls;
    private String webhookurl;
    private boolean reportOnlyWhenBrokenUrls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(final List<String> urls) {
        this.urls = urls;
    }

    public String getWebhookurl() {
        return webhookurl;
    }

    public void setWebhookurl(final String webhookurl) {
        this.webhookurl = webhookurl;
    }

    public boolean isReportOnlyWhenBrokenUrls() {
        return reportOnlyWhenBrokenUrls;
    }

    public void setReportOnlyWhenBrokenUrls(final boolean reportOnlyWhenBrokenUrls) {
        this.reportOnlyWhenBrokenUrls = reportOnlyWhenBrokenUrls;
    }
}

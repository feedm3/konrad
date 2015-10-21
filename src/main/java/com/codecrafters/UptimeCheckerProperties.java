package com.codecrafters;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Fabian Dietenberger
 */
@Configuration
@ConfigurationProperties(prefix="uptime")
public class UptimeCheckerProperties {

    private List<String> urls;
    private String webhookurl;

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
}

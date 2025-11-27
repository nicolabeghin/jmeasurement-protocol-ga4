package com.google.ga4.mp.builders;

import com.google.ga4.mp.GA4Analytics;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for page_view events in GA4
 */
public class PageViewBuilder {
    private final GA4Analytics analytics;
    private String documentTitle;
    private String documentPath;
    private String documentLocation;

    public PageViewBuilder(GA4Analytics analytics) {
        this.analytics = analytics;
    }

    public PageViewBuilder documentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
        return this;
    }

    public PageViewBuilder documentPath(String documentPath) {
        this.documentPath = documentPath;
        return this;
    }

    public PageViewBuilder documentLocation(String documentLocation) {
        this.documentLocation = documentLocation;
        return this;
    }

    public void send() {
        sendInternal(false);
    }

    public void sendAsync() {
        sendInternal(true);
    }

    private void sendInternal(boolean async) {
        Map<String, Object> params = new HashMap<>();

        if (documentTitle != null) {
            params.put("page_title", documentTitle);
        }
        if (documentPath != null) {
            params.put("page_location", documentPath);
        }
        if (documentLocation != null) {
            params.put("page_location", documentLocation);
        }

        analytics.sendEvent("page_view", params, async);
    }
}

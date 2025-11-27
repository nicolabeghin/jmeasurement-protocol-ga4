package com.google.ga4.mp.builders;

import com.google.ga4.mp.GA4Analytics;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for custom events in GA4
 */
public class EventBuilder {
    private final GA4Analytics analytics;
    private String category;
    private String action;
    private String label;
    private Integer value;
    private final Map<String, Object> customParams;

    public EventBuilder(GA4Analytics analytics) {
        this.analytics = analytics;
        this.customParams = new HashMap<>();
    }

    public EventBuilder eventCategory(String category) {
        this.category = category;
        return this;
    }

    public EventBuilder eventAction(String action) {
        this.action = action;
        return this;
    }

    public EventBuilder eventLabel(String label) {
        this.label = label;
        return this;
    }

    public EventBuilder eventValue(Integer value) {
        this.value = value;
        return this;
    }

    public EventBuilder customParameter(String key, Object value) {
        this.customParams.put(key, value);
        return this;
    }

    public void send() {
        sendInternal(false);
    }

    public void sendAsync() {
        sendInternal(true);
    }

    private void sendInternal(boolean async) {
        Map<String, Object> params = new HashMap<>(customParams);

        if (category != null) {
            params.put("event_category", category);
        }
        if (action != null) {
            params.put("event_action", action);
        }
        if (label != null) {
            params.put("event_label", label);
        }
        if (value != null) {
            params.put("value", value);
        }

        // Use the action as event name if available, otherwise use a generic name
        String eventName = action != null ? action : "custom_event";

        analytics.sendEvent(eventName, params, async);
    }
}

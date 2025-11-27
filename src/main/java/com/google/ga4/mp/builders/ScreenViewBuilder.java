package com.google.ga4.mp.builders;

import com.google.ga4.mp.GA4Analytics;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for screen_view events in GA4
 */
public class ScreenViewBuilder {
    private final GA4Analytics analytics;
    private String screenName;
    private String sessionControl;

    public ScreenViewBuilder(GA4Analytics analytics) {
        this.analytics = analytics;
    }

    public ScreenViewBuilder screenName(String screenName) {
        this.screenName = screenName;
        return this;
    }

    public ScreenViewBuilder sessionControl(String sessionControl) {
        this.sessionControl = sessionControl;
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

        if (screenName != null) {
            params.put("screen_name", screenName);
        }
        if (sessionControl != null) {
            params.put("session_control", sessionControl);
        }

        // For session start/end, use session_start event name
        String eventName = "start".equals(sessionControl) ? "session_start" :
                          "end".equals(sessionControl) ? "session_end" : "screen_view";

        analytics.sendEvent(eventName, params, async);
    }
}

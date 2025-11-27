package com.google.ga4.mp.builders;

import com.google.ga4.mp.GA4Analytics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventBuilderTest {

    private GA4Analytics analytics;

    @BeforeEach
    void setUp() {
        analytics = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .withDebugMode(true)
                .build();
    }

    @AfterEach
    void tearDown() {
        if (analytics != null) {
            analytics.shutdown();
        }
    }

    @Test
    void testEventBuilderCreation() {
        EventBuilder builder = analytics.event();
        assertNotNull(builder);
    }

    @Test
    void testEventBuilderFluentInterface() {
        EventBuilder builder = analytics.event()
                .eventCategory("engagement")
                .eventAction("button_click")
                .eventLabel("signup")
                .eventValue(1);

        assertNotNull(builder);
    }

    @Test
    void testEventBuilderWithCustomParameters() {
        EventBuilder builder = analytics.event()
                .eventCategory("ecommerce")
                .eventAction("purchase")
                .customParameter("transaction_id", "TXN-123")
                .customParameter("value", 99.99)
                .customParameter("currency", "USD");

        assertNotNull(builder);
    }

    @Test
    void testEventBuilderMinimalConfiguration() {
        EventBuilder builder = analytics.event()
                .eventAction("simple_event");

        assertNotNull(builder);
    }

    @Test
    void testEventBuilderSendSync() {
        assertDoesNotThrow(() -> {
            analytics.event()
                    .eventCategory("test")
                    .eventAction("test_action")
                    .send();
        });
    }

    @Test
    void testEventBuilderSendAsync() {
        assertDoesNotThrow(() -> {
            analytics.event()
                    .eventCategory("test")
                    .eventAction("test_action")
                    .sendAsync();
        });
    }

    @Test
    void testEventBuilderWithOnlyCategory() {
        assertDoesNotThrow(() -> {
            analytics.event()
                    .eventCategory("category_only")
                    .send();
        });
    }

    @Test
    void testEventBuilderWithMultipleCustomParameters() {
        EventBuilder builder = analytics.event()
                .eventAction("multi_param_event")
                .customParameter("param1", "value1")
                .customParameter("param2", 123)
                .customParameter("param3", true)
                .customParameter("param4", 45.67);

        assertNotNull(builder);
        assertDoesNotThrow(builder::send);
    }
}

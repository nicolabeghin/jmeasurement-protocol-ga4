package com.google.ga4.mp.builders;

import com.google.ga4.mp.GA4Analytics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageViewBuilderTest {

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
    void testPageViewBuilderCreation() {
        PageViewBuilder builder = analytics.pageView();
        assertNotNull(builder);
    }

    @Test
    void testPageViewBuilderFluentInterface() {
        PageViewBuilder builder = analytics.pageView()
                .documentTitle("Home Page")
                .documentPath("/home")
                .documentLocation("https://example.com/home");

        assertNotNull(builder);
    }

    @Test
    void testPageViewBuilderWithTitle() {
        PageViewBuilder builder = analytics.pageView()
                .documentTitle("Product Details");

        assertNotNull(builder);
    }

    @Test
    void testPageViewBuilderWithPath() {
        PageViewBuilder builder = analytics.pageView()
                .documentPath("/products/123");

        assertNotNull(builder);
    }

    @Test
    void testPageViewBuilderWithLocation() {
        PageViewBuilder builder = analytics.pageView()
                .documentLocation("https://example.com/products/123");

        assertNotNull(builder);
    }

    @Test
    void testPageViewBuilderSendSync() {
        assertDoesNotThrow(() -> {
            analytics.pageView()
                    .documentTitle("Test Page")
                    .documentPath("/test")
                    .send();
        });
    }

    @Test
    void testPageViewBuilderSendAsync() {
        assertDoesNotThrow(() -> {
            analytics.pageView()
                    .documentTitle("Test Page")
                    .documentPath("/test")
                    .sendAsync();
        });
    }

    @Test
    void testPageViewBuilderMinimal() {
        assertDoesNotThrow(() -> {
            analytics.pageView().send();
        });
    }

    @Test
    void testPageViewBuilderWithAllFields() {
        assertDoesNotThrow(() -> {
            analytics.pageView()
                    .documentTitle("Complete Page")
                    .documentPath("/complete")
                    .documentLocation("https://example.com/complete")
                    .send();
        });
    }
}

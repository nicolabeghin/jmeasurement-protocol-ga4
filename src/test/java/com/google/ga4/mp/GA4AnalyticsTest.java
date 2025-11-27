package com.google.ga4.mp;

import com.google.ga4.mp.providers.SystemInfoProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GA4AnalyticsTest {

    private GA4Analytics analytics;

    @AfterEach
    void tearDown() {
        if (analytics != null) {
            analytics.shutdown();
        }
    }

    @Test
    void testBuilderWithRequiredFields() {
        analytics = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .build();

        assertNotNull(analytics);
    }

    @Test
    void testBuilderWithAllFields() {
        analytics = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .withClientId("custom-client-id")
                .withAppName("Test App")
                .withAppVersion("1.0.0")
                .withUserAgent("Custom User Agent")
                .withDebugMode(true)
                .build();

        assertNotNull(analytics);
    }

    @Test
    void testBuilderMissingMeasurementId() {
        assertThrows(IllegalStateException.class, () -> {
            GA4Analytics.builder()
                    .withApiSecret("test-secret")
                    .build();
        });
    }

    @Test
    void testBuilderMissingApiSecret() {
        assertThrows(IllegalStateException.class, () -> {
            GA4Analytics.builder()
                    .withMeasurementId("G-TEST123")
                    .build();
        });
    }

    @Test
    void testBuilderWithCustomSystemInfoProvider() {
        SystemInfoProvider customProvider = new SystemInfoProvider() {
            @Override
            public String getOsName() {
                return "CustomOS";
            }

            @Override
            public String getOsVersion() {
                return "1.0";
            }
        };

        analytics = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .withSystemInfoProvider(customProvider)
                .build();

        assertNotNull(analytics);
    }

    @Test
    void testDefaultUserAgentGeneration() {
        String userAgent = GA4Analytics.builder().buildDefaultUserAgent();
        assertNotNull(userAgent);
        assertTrue(userAgent.contains("Java/"));
    }

    @Test
    void testEventBuilderCreation() {
        analytics = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .build();

        assertNotNull(analytics.event());
    }

    @Test
    void testPageViewBuilderCreation() {
        analytics = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .build();

        assertNotNull(analytics.pageView());
    }

    @Test
    void testScreenViewBuilderCreation() {
        analytics = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .build();

        assertNotNull(analytics.screenView());
    }

    @Test
    void testBuilderFluentInterface() {
        analytics = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .withAppName("Test App")
                .withAppVersion("1.0.0")
                .withClientId("client-123")
                .withUserAgent("Test Agent")
                .withDebugMode(false)
                .build();

        assertNotNull(analytics);
    }

    @Test
    void testShutdown() {
        analytics = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .build();

        assertDoesNotThrow(() -> analytics.shutdown());
    }
}

package com.google.ga4.mp.builders;

import com.google.ga4.mp.GA4Analytics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScreenViewBuilderTest {

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
    void testScreenViewBuilderCreation() {
        ScreenViewBuilder builder = analytics.screenView();
        assertNotNull(builder);
    }

    @Test
    void testScreenViewBuilderFluentInterface() {
        ScreenViewBuilder builder = analytics.screenView()
                .screenName("Dashboard")
                .sessionControl("start");

        assertNotNull(builder);
    }

    @Test
    void testScreenViewBuilderWithScreenName() {
        ScreenViewBuilder builder = analytics.screenView()
                .screenName("Settings Screen");

        assertNotNull(builder);
    }

    @Test
    void testScreenViewBuilderWithSessionStart() {
        ScreenViewBuilder builder = analytics.screenView()
                .screenName("Home")
                .sessionControl("start");

        assertNotNull(builder);
    }

    @Test
    void testScreenViewBuilderWithSessionEnd() {
        ScreenViewBuilder builder = analytics.screenView()
                .screenName("Logout")
                .sessionControl("end");

        assertNotNull(builder);
    }

    @Test
    void testScreenViewBuilderSendSync() {
        assertDoesNotThrow(() -> {
            analytics.screenView()
                    .screenName("Test Screen")
                    .send();
        });
    }

    @Test
    void testScreenViewBuilderSendAsync() {
        assertDoesNotThrow(() -> {
            analytics.screenView()
                    .screenName("Test Screen")
                    .sendAsync();
        });
    }

    @Test
    void testScreenViewBuilderMinimal() {
        assertDoesNotThrow(() -> {
            analytics.screenView().send();
        });
    }

    @Test
    void testScreenViewBuilderWithAllFields() {
        assertDoesNotThrow(() -> {
            analytics.screenView()
                    .screenName("Main Dashboard")
                    .sessionControl("start")
                    .send();
        });
    }

    @Test
    void testScreenViewBuilderSessionControlVariations() {
        assertDoesNotThrow(() -> {
            analytics.screenView()
                    .sessionControl("start")
                    .send();

            analytics.screenView()
                    .sessionControl("end")
                    .send();

            analytics.screenView()
                    .sessionControl("continue")
                    .send();
        });
    }
}

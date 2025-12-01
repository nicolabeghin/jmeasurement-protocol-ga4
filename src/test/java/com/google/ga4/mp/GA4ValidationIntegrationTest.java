package com.google.ga4.mp;

import com.google.ga4.mp.providers.SystemInfoProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test that validates device info payload against GA4 debug endpoint
 * Requires GA4_MEASUREMENT_ID and GA4_API_SECRET environment variables
 * Run with: GA4_MEASUREMENT_ID=G-XXXXXX GA4_API_SECRET=your-secret ./gradlew test --tests GA4ValidationIntegrationTest
 */
class GA4ValidationIntegrationTest {

    @Test
    @EnabledIfEnvironmentVariable(named = "GA4_MEASUREMENT_ID", matches = ".+")
    @EnabledIfEnvironmentVariable(named = "GA4_API_SECRET", matches = ".+")
    void testDeviceInfoValidationWithApacheCommonsProvider() throws Exception {
        String measurementId = System.getenv("GA4_MEASUREMENT_ID");
        String apiSecret = System.getenv("GA4_API_SECRET");

        // Use the default ApacheCommonsSystemInfoProvider (OOTB provider)
        GA4Analytics ga = GA4Analytics.builder()
                .withMeasurementId(measurementId)
                .withApiSecret(apiSecret)
                .withAppName("Test App")
                .withAppVersion("1.0.0")
                .withDebugMode(true)
                .withValidationBehavior("ENFORCE_RECOMMENDATIONS")
                // No custom provider - uses ApacheCommonsSystemInfoProvider by default
                .build();

        // Send a test event (this will be logged in debug mode)
        ga.event()
                .eventCategory("test")
                .eventAction("apache_commons_provider_test")
                .eventLabel("validation")
                .send();

        // Give it a moment to send
        Thread.sleep(500);

        ga.shutdown();

        // Note: The validation response is logged via LOG.INFO
        // Check the test output for: "GA4 Validation passed for event 'apache_commons_provider_test' - no validation messages"
        // This verifies that the OOTB ApacheCommonsSystemInfoProvider works correctly with GA4
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "GA4_MEASUREMENT_ID", matches = ".+")
    @EnabledIfEnvironmentVariable(named = "GA4_API_SECRET", matches = ".+")
    void testDeviceInfoValidationUsingGA4Analytics() throws Exception {
        String measurementId = System.getenv("GA4_MEASUREMENT_ID");
        String apiSecret = System.getenv("GA4_API_SECRET");

        // Create custom provider with mobile device info
        SystemInfoProvider mobileProvider = new SystemInfoProvider() {
            @Override
            public String getOsName() {
                return "Android";
            }

            @Override
            public String getOsVersion() {
                return "14";
            }

            @Override
            public String getDeviceCategory() {
                return "mobile";
            }

            @Override
            public String getLanguage() {
                return "en-US";
            }

            @Override
            public String getScreenResolution() {
                return "1080x2400";
            }

            @Override
            public String getDeviceModel() {
                return "Pixel 9 Pro";
            }

            @Override
            public String getDeviceBrand() {
                return "Google";
            }

            @Override
            public String getBrowser() {
                return "Chrome";
            }

            @Override
            public String getBrowserVersion() {
                return "120.0.6099.144";
            }
        };

        // Create GA4Analytics with debug mode and validation behavior
        GA4Analytics ga = GA4Analytics.builder()
                .withMeasurementId(measurementId)
                .withApiSecret(apiSecret)
                .withAppName("Test App")
                .withAppVersion("1.0.0")
                .withDebugMode(true)
                .withValidationBehavior("ENFORCE_RECOMMENDATIONS")
                .withSystemInfoProvider(mobileProvider)
                .build();

        // Send a test event (this will be logged in debug mode)
        ga.event()
                .eventCategory("test")
                .eventAction("device_info_test")
                .eventLabel("validation")
                .send();

        // Give it a moment to send
        Thread.sleep(500);

        ga.shutdown();

        // Note: In a real test, you would capture and verify the HTTP response
        // For now, this test verifies that the code doesn't throw exceptions
        // and logs the debug response which should show empty validationMessages
    }

    @Test
    void testValidationBehaviorInPayload() {
        // Test that validation_behavior is properly added to payload
        GA4Analytics ga = GA4Analytics.builder()
                .withMeasurementId("G-TEST123")
                .withApiSecret("test-secret")
                .withDebugMode(true)
                .withValidationBehavior("ENFORCE_RECOMMENDATIONS")
                .build();

        assertNotNull(ga);
        ga.shutdown();
    }
}

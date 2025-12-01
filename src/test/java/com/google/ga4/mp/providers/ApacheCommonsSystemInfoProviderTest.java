package com.google.ga4.mp.providers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApacheCommonsSystemInfoProviderTest {

    @Test
    void testGetOsName() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();
        String osName = provider.getOsName();

        assertNotNull(osName);
        assertFalse(osName.isEmpty());
    }

    @Test
    void testGetOsVersion() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();
        String osVersion = provider.getOsVersion();

        assertNotNull(osVersion);
        assertFalse(osVersion.isEmpty());
    }

    @Test
    void testOsNameIsRecognized() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();
        String osName = provider.getOsName();

        // Should be one of the recognized OS names or fallback to system property
        assertTrue(
            osName.equals("Windows NT") ||
            osName.equals("macOS") ||
            osName.equals("Linux") ||
            osName.equals(System.getProperty("os.name", "Unknown"))
        );
    }

    @Test
    void testSystemInfoProviderInterface() {
        SystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();

        assertNotNull(provider.getOsName());
        assertNotNull(provider.getOsVersion());
    }

    @Test
    void testConsistentResults() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();

        String osName1 = provider.getOsName();
        String osName2 = provider.getOsName();
        String osVersion1 = provider.getOsVersion();
        String osVersion2 = provider.getOsVersion();

        assertEquals(osName1, osName2);
        assertEquals(osVersion1, osVersion2);
    }

    @Test
    void testGetDeviceCategory() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();
        String deviceCategory = provider.getDeviceCategory();

        assertNotNull(deviceCategory);
        assertEquals("desktop", deviceCategory);
    }

    @Test
    void testGetLanguage() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();
        String language = provider.getLanguage();

        assertNotNull(language);
        assertFalse(language.isEmpty());
        // Language should be in ISO 639-1 format, either "en" or "en-US" style
        assertTrue(language.matches("^[a-z]{2}(-[A-Z]{2})?$"));
    }

    @Test
    void testGetScreenResolution() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();
        String screenResolution = provider.getScreenResolution();

        // Screen resolution may be null in headless environments (CI/CD)
        // If not null, should be in WIDTHxHEIGHT format
        if (screenResolution != null) {
            assertTrue(screenResolution.matches("^\\d+x\\d+$"),
                    "Screen resolution should be in WIDTHxHEIGHT format, got: " + screenResolution);
        }
    }

    @Test
    void testGetDeviceModel() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();
        String deviceModel = provider.getDeviceModel();

        assertNotNull(deviceModel);
        assertFalse(deviceModel.isEmpty());
        // Device model should be same as OS name for desktop apps
        assertEquals(provider.getOsName(), deviceModel);
    }

    @Test
    void testGetBrowser() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();
        String browser = provider.getBrowser();

        assertNotNull(browser);
        assertEquals("Java", browser);
    }

    @Test
    void testGetBrowserVersion() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();
        String browserVersion = provider.getBrowserVersion();

        assertNotNull(browserVersion);
        assertFalse(browserVersion.isEmpty());
        // Should match Java version
        assertEquals(System.getProperty("java.version"), browserVersion);
    }

    @Test
    void testDeviceInfoDefaultMethods() {
        ApacheCommonsSystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();

        // Only device brand should be null (not applicable for desktop Java apps)
        assertNull(provider.getDeviceBrand());
    }

    @Test
    void testAllDeviceInfoMethods() {
        SystemInfoProvider provider = new ApacheCommonsSystemInfoProvider();

        // OS info
        assertNotNull(provider.getOsName());
        assertNotNull(provider.getOsVersion());

        // Device info
        assertNotNull(provider.getDeviceCategory());
        assertNotNull(provider.getLanguage());

        // Optional device info (may be null)
        provider.getScreenResolution();
        provider.getDeviceModel();
        provider.getDeviceBrand();
        provider.getBrowser();
        provider.getBrowserVersion();
    }
}

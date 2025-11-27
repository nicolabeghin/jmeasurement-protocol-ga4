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
}

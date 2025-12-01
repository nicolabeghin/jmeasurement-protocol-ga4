package com.google.ga4.mp.providers;

/**
 * Interface for providing system information to GA4Analytics
 * This allows applications to customize how OS information is reported
 */
public interface SystemInfoProvider {
    /**
     * @return The operating system name (e.g., "Windows", "macOS", "Linux")
     */
    String getOsName();

    /**
     * @return The operating system version
     */
    String getOsVersion();

    /**
     * @return The device category (e.g., "desktop", "tablet", "mobile", "smart TV")
     */
    default String getDeviceCategory() {
        return null;
    }

    /**
     * @return The language in ISO 639-1 format (e.g., "en", "en-US")
     */
    default String getLanguage() {
        return null;
    }

    /**
     * @return The screen resolution formatted as WIDTHxHEIGHT (e.g., "1280x2856")
     */
    default String getScreenResolution() {
        return null;
    }

    /**
     * @return The device model (e.g., "Pixel 9 Pro", "Samsung Galaxy S24")
     */
    default String getDeviceModel() {
        return null;
    }

    /**
     * @return The device brand (e.g., "Google", "Samsung")
     */
    default String getDeviceBrand() {
        return null;
    }

    /**
     * @return The browser brand or type (e.g., "Chrome", "Firefox")
     */
    default String getBrowser() {
        return null;
    }

    /**
     * @return The browser version (e.g., "136.0.7103.60", "5.0")
     */
    default String getBrowserVersion() {
        return null;
    }
}

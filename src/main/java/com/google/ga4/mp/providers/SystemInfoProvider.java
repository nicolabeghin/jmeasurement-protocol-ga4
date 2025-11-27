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
}

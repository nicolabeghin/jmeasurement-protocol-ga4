package com.google.ga4.mp.providers;

import org.apache.commons.lang3.SystemUtils;

import java.util.Locale;

/**
 * SystemInfoProvider implementation using Apache Commons Lang3 SystemUtils
 * This is the default provider used by GA4Analytics when no custom provider is specified.
 */
public class ApacheCommonsSystemInfoProvider implements SystemInfoProvider {

    @Override
    public String getOsName() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "Windows NT";
        } else if (SystemUtils.IS_OS_MAC) {
            return "macOS";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "Linux";
        }
        return System.getProperty("os.name", "Unknown");
    }

    @Override
    public String getOsVersion() {
        return SystemUtils.OS_VERSION;
    }

    @Override
    public String getDeviceCategory() {
        return "desktop";
    }

    @Override
    public String getLanguage() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (country != null && !country.isEmpty()) {
            return language + "-" + country;
        }
        return language;
    }
}

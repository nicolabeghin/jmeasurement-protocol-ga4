package com.google.ga4.mp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.ga4.mp.builders.EventBuilder;
import com.google.ga4.mp.builders.PageViewBuilder;
import com.google.ga4.mp.builders.ScreenViewBuilder;
import com.google.ga4.mp.providers.ApacheCommonsSystemInfoProvider;
import com.google.ga4.mp.providers.SystemInfoProvider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom implementation of Google Analytics 4 Measurement Protocol
 * https://developers.google.com/analytics/devguides/collection/protocol/ga4
 */
public class GA4Analytics {
    private static final String GA4_ENDPOINT = "https://www.google-analytics.com/mp/collect";
    private static final String GA4_DEBUG_ENDPOINT = "https://www.google-analytics.com/debug/mp/collect";
    private static final Logger LOG = Logger.getLogger(GA4Analytics.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String measurementId;
    private final String apiSecret;
    private final String clientId;
    private final String appName;
    private final String appVersion;
    private final String userAgent;
    private final boolean debugMode;
    private final ExecutorService executor;
    private final HttpClient httpClient;
    private final Map<String, Object> sessionParams;
    private final SystemInfoProvider systemInfoProvider;

    private GA4Analytics(Builder builder) {
        this.measurementId = builder.measurementId;
        this.apiSecret = builder.apiSecret;
        this.clientId = builder.clientId != null ? builder.clientId : UUID.randomUUID().toString();
        this.appName = builder.appName;
        this.appVersion = builder.appVersion;
        this.userAgent = builder.userAgent;
        this.debugMode = builder.debugMode;
        this.systemInfoProvider = builder.systemInfoProvider != null ? builder.systemInfoProvider : new ApacheCommonsSystemInfoProvider();
        this.executor = Executors.newFixedThreadPool(2);
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.sessionParams = new HashMap<>();

        // Add default session parameters
        sessionParams.put("session_id", System.currentTimeMillis() / 1000);
        sessionParams.put("engagement_time_msec", "100");

        if (debugMode) {
            LOG.info("GA4Analytics initialized in DEBUG mode - events will be validated but not recorded");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public EventBuilder event() {
        return new EventBuilder(this);
    }

    public ScreenViewBuilder screenView() {
        return new ScreenViewBuilder(this);
    }

    public PageViewBuilder pageView() {
        return new PageViewBuilder(this);
    }

    public void sendEvent(String eventName, Map<String, Object> params, boolean async) {
        if (async) {
            executor.submit(() -> doSendEvent(eventName, params));
        } else {
            doSendEvent(eventName, params);
        }
    }

    private void doSendEvent(String eventName, Map<String, Object> eventParams) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("client_id", clientId);

            // Add user properties
            Map<String, Object> userProperties = new HashMap<>();
            userProperties.put("app_name", createValueMap(appName));
            userProperties.put("app_version", createValueMap(appVersion));
            userProperties.put("os_name", createValueMap(systemInfoProvider.getOsName()));
            userProperties.put("os_version", createValueMap(systemInfoProvider.getOsVersion()));
            payload.put("user_properties", userProperties);

            // Add device info
            Map<String, Object> deviceInfo = new HashMap<>();
            addIfNotNull(deviceInfo, "category", systemInfoProvider.getDeviceCategory());
            addIfNotNull(deviceInfo, "language", systemInfoProvider.getLanguage());
            addIfNotNull(deviceInfo, "screen_resolution", systemInfoProvider.getScreenResolution());
            addIfNotNull(deviceInfo, "operating_system", systemInfoProvider.getOsName());
            addIfNotNull(deviceInfo, "operating_system_version", systemInfoProvider.getOsVersion());
            addIfNotNull(deviceInfo, "model", systemInfoProvider.getDeviceModel());
            addIfNotNull(deviceInfo, "brand", systemInfoProvider.getDeviceBrand());
            addIfNotNull(deviceInfo, "browser", systemInfoProvider.getBrowser());
            addIfNotNull(deviceInfo, "browser_version", systemInfoProvider.getBrowserVersion());

            if (!deviceInfo.isEmpty()) {
                payload.put("device", deviceInfo);
            }

            // Create event
            Map<String, Object> event = new HashMap<>();
            event.put("name", eventName);

            // Merge session params and event params
            Map<String, Object> params = new HashMap<>(sessionParams);
            if (eventParams != null) {
                params.putAll(eventParams);
            }
            event.put("params", params);

            payload.put("events", new Object[]{event});

            // Prepare JSON payload
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Build URI with query parameters (use debug endpoint if in debug mode)
            String endpoint = debugMode ? GA4_DEBUG_ENDPOINT : GA4_ENDPOINT;
            String uri = endpoint + "?measurement_id=" + measurementId + "&api_secret=" + apiSecret;

            // Build and send HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .header("User-Agent", userAgent)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Handle response
            if (debugMode) {
                // In debug mode, log the validation response
                LOG.log(Level.INFO, "GA4 Debug Response (event: {0}): {1}",
                        new Object[]{eventName, response.body()});
            }

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                LOG.log(Level.WARNING, "GA4 request failed with code: " + response.statusCode() +
                        (debugMode ? ", body: " + response.body() : ""));
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Failed to send GA4 event: " + eventName, e);
        }
    }

    private Map<String, String> createValueMap(String value) {
        Map<String, String> map = new HashMap<>();
        map.put("value", value);
        return map;
    }

    private void addIfNotNull(Map<String, Object> map, String key, String value) {
        if (value != null && !value.isEmpty()) {
            map.put(key, value);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }

    public static class Builder {
        private String measurementId;
        private String apiSecret;
        private String clientId;
        private String appName;
        private String appVersion;
        private String userAgent;
        private boolean debugMode = false;
        private SystemInfoProvider systemInfoProvider;

        public Builder withMeasurementId(String measurementId) {
            this.measurementId = measurementId;
            return this;
        }

        public Builder withApiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }

        public Builder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder withAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder withAppVersion(String appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        public Builder withUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder withDebugMode(boolean debugMode) {
            this.debugMode = debugMode;
            return this;
        }

        public Builder withSystemInfoProvider(SystemInfoProvider systemInfoProvider) {
            this.systemInfoProvider = systemInfoProvider;
            return this;
        }

        public GA4Analytics build() {
            if (measurementId == null || apiSecret == null) {
                throw new IllegalStateException("measurementId and apiSecret are required");
            }
            if (userAgent == null) {
                userAgent = buildDefaultUserAgent();
            }
            return new GA4Analytics(this);
        }

        public String buildDefaultUserAgent() {
            SystemInfoProvider provider = systemInfoProvider != null ? systemInfoProvider : new ApacheCommonsSystemInfoProvider();
            return String.format("Java/%s (%s %s; %s)",
                System.getProperty("java.version"),
                provider.getOsName(),
                provider.getOsVersion(),
                System.getProperty("os.arch"));
        }
    }
}

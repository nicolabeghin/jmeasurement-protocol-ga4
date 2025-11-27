# jmeasurement-protocol-ga4

A lightweight Java library for sending analytics events through [Google Analytics 4 Measurement Protocol](https://developers.google.com/analytics/devguides/collection/protocol/ga4).

## Features

- 🚀 Simple, fluent API for GA4 event tracking
- 🔍 Debug mode for validating events without recording
- ⚡ Async and sync event sending
- 🎯 Built-in support for common event types (page views, screen views, custom events)
- 🔧 Customizable system information reporting
- ☕ Java 21+ compatible

## Installation

### Using JitPack

[![](https://jitpack.io/v/nicolabeghin/jmeasurement-protocol-ga4.svg)](https://jitpack.io/#nicolabeghin/jmeasurement-protocol-ga4)

#### Gradle

Add JitPack repository and dependency to your `build.gradle`:

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.nicolabeghin:jmeasurement-protocol-ga4:1.0.0'
}
```

#### Maven

Add JitPack repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.nicolabeghin</groupId>
        <artifactId>jmeasurement-protocol-ga4</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

See all available versions at [JitPack](https://jitpack.io/#nicolabeghin/jmeasurement-protocol-ga4)

## Quick Start

```java
import com.google.ga4.mp.GA4Analytics;

// Initialize GA4Analytics
GA4Analytics ga = GA4Analytics.builder()
    .withMeasurementId("G-XXXXXXXXXX")
    .withApiSecret("your-api-secret")
    .withAppName("My App")
    .withAppVersion("1.0.0")
    .build();

// Send a page view event
ga.pageView()
    .documentTitle("Home")
    .documentPath("/home")
    .sendAsync();

// Send a custom event
ga.event()
    .eventCategory("engagement")
    .eventAction("button_click")
    .eventLabel("signup")
    .sendAsync();

// Send a screen view
ga.screenView()
    .screenName("Dashboard")
    .sendAsync();

// Shutdown when done
ga.shutdown();
```

## Debug Mode

Enable debug mode to validate events without recording them:

```java
GA4Analytics ga = GA4Analytics.builder()
    .withMeasurementId("G-XXXXXXXXXX")
    .withApiSecret("your-api-secret")
    .withDebugMode(true)  // Enable debug mode
    .build();
```

In debug mode:
- Events are sent to the debug endpoint
- Validation responses are logged
- Events are NOT recorded in your GA4 property

## System Information Providers

### ApacheCommonsSystemInfoProvider (Default)
The library uses Apache Commons Lang3 `SystemUtils` for OS detection by default. This provider is automatically used if no custom provider is specified.

```java
GA4Analytics ga = GA4Analytics.builder()
    .withMeasurementId("G-XXXXXXXXXX")
    .withApiSecret("your-api-secret")
    // ApacheCommonsSystemInfoProvider is used automatically
    .build();
```

You can also specify it explicitly:

```java
import com.google.ga4.mp.providers.ApacheCommonsSystemInfoProvider;

GA4Analytics ga = GA4Analytics.builder()
    .withMeasurementId("G-XXXXXXXXXX")
    .withApiSecret("your-api-secret")
    .withSystemInfoProvider(new ApacheCommonsSystemInfoProvider())
    .build();
```

### Custom SystemInfoProvider
Implement your own for custom OS detection:

```java
import com.google.ga4.mp.providers.SystemInfoProvider;

SystemInfoProvider customProvider = new SystemInfoProvider() {
    @Override
    public String getOsName() {
        return "Custom OS";
    }

    @Override
    public String getOsVersion() {
        return "1.0";
    }
};

GA4Analytics ga = GA4Analytics.builder()
    .withMeasurementId("G-XXXXXXXXXX")
    .withApiSecret("your-api-secret")
    .withSystemInfoProvider(customProvider)
    .build();
```

## API Reference

### GA4Analytics.Builder

- `withMeasurementId(String)` - GA4 measurement ID (required)
- `withApiSecret(String)` - Measurement Protocol API secret (required)
- `withAppName(String)` - Application name
- `withAppVersion(String)` - Application version
- `withClientId(String)` - Custom client ID (auto-generated if not provided)
- `withUserAgent(String)` - Custom user agent string
- `withDebugMode(boolean)` - Enable debug/validation mode
- `withSystemInfoProvider(SystemInfoProvider)` - Custom system info provider

### Event Builders

**EventBuilder** - Custom events
- `eventCategory(String)` - Event category
- `eventAction(String)` - Event action (used as event name)
- `eventLabel(String)` - Event label
- `eventValue(Integer)` - Event value
- `customParameter(String, Object)` - Add custom parameter

**PageViewBuilder** - Page view events
- `documentTitle(String)` - Page title
- `documentPath(String)` - Page path
- `documentLocation(String)` - Page location URL

**ScreenViewBuilder** - Screen view events
- `screenName(String)` - Screen name
- `sessionControl(String)` - Session control ("start" or "end")

All builders support:
- `send()` - Send synchronously
- `sendAsync()` - Send asynchronously

## Getting GA4 Credentials

1. Go to your GA4 property in Google Analytics
2. Navigate to Admin → Data Streams
3. Select your data stream
4. Copy the **Measurement ID** (starts with "G-")
5. Scroll down to **Measurement Protocol API secrets**
6. Click **Create** to generate an API secret

## Requirements

- Java 21 or higher
- Jackson library (included as dependency)

## License

MIT License

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

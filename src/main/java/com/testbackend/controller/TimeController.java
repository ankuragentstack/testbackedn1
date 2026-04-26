package com.testbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/config/v1")
public class TimeController {

    private static final Logger log = LoggerFactory.getLogger(TimeController.class);
    private static final String PREFIX = "/config/v1/testTime/";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    // Captures full path so slashes inside the timezone (e.g. America/New_York) are preserved.
    @GetMapping("/testTime/**")
    public ResponseEntity<?> getTime(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("Received request: {} {}", request.getMethod(), path);

        if (!path.startsWith(PREFIX) || path.length() <= PREFIX.length()) {
            log.warn("Missing timezone in request path: {}", path);
            return ResponseEntity.badRequest().body(
                Map.of("error", "Missing timezone",
                       "hint", "Usage: /config/v1/testTime/<IANA-timezone>  e.g. America/New_York")
            );
        }

        String timezone = path.substring(PREFIX.length()).replace(" ", "+");
        log.info("Resolved timezone: {}", timezone);

        if (!ZoneId.getAvailableZoneIds().contains(timezone)) {
            log.warn("Unknown timezone requested: {}", timezone);
            return ResponseEntity.badRequest().body(
                Map.of("error", "Unknown timezone: " + timezone,
                       "hint", "Use IANA zone IDs such as America/New_York or Asia/Kolkata")
            );
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timezone));
        log.info("Returning time for timezone={} currentTime={} utcOffset={}", timezone, now.format(FORMATTER), now.getOffset().getId());
        return ResponseEntity.ok(Map.of(
            "timezone", timezone,
            "currentTime", now.format(FORMATTER),
            "utcOffset", now.getOffset().getId()
        ));
    }
}

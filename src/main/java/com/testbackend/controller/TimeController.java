package com.testbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
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

    private static final String PREFIX = "/config/v1/testTime/";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    // Captures full path so slashes inside the timezone (e.g. America/New_York) are preserved.
    @GetMapping("/testTime/**")
    public ResponseEntity<?> getTime(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (!path.startsWith(PREFIX) || path.length() <= PREFIX.length()) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Missing timezone",
                       "hint", "Usage: /config/v1/testTime/<IANA-timezone>  e.g. America/New_York")
            );
        }

        String timezone = path.substring(PREFIX.length()).replace(" ", "+");

        if (!ZoneId.getAvailableZoneIds().contains(timezone)) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Unknown timezone: " + timezone,
                       "hint", "Use IANA zone IDs such as America/New_York or Asia/Kolkata")
            );
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timezone));
        return ResponseEntity.ok(Map.of(
            "timezone", timezone,
            "currentTime", now.format(FORMATTER),
            "utcOffset", now.getOffset().getId()
        ));
    }
}

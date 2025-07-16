package de.thm.mcptest;

import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

public class DateTimeTools {

    String getCurrentDateTime(String format) {
        System.out.println("getCurrentDateTime called with format: " + format);
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}

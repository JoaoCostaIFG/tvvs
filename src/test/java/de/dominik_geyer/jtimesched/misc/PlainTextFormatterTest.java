package de.dominik_geyer.jtimesched.misc;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlainTextFormatterTest {
    @Test
    public void formatTest() {
        // Given
        Level severity = Level.SEVERE;
        String msg = "test log";
        long millis = 1000;

        LogRecord record = new LogRecord(severity, msg);
        record.setMillis(millis);

        // When
        String result = new PlainTextFormatter().format(record);

        // Then
        String wanted = String.format("%s [%s]: %s%n", new SimpleDateFormat("yyyy-MM-dd (E) HH:mm:ss").format(millis), severity, msg);
        assertEquals(wanted, result);
    }
}

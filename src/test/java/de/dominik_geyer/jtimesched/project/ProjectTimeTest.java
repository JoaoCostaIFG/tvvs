package de.dominik_geyer.jtimesched.project;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectTimeTest {
    private int timeComponentsToSecs(String hours, String minutes, String seconds) {
        return Integer.parseInt(hours) * 3600
                + Integer.parseInt(minutes) * 60
                + Integer.parseInt(seconds);
    }

    private String timeComponentsToTimeStr(String hours, String minutes, String seconds) {
        return String.format("%s:%s:%s", hours, minutes, seconds);
    }

    @ParameterizedTest(name = "Hours: {0} | Minutes: {1} | Seconds: {2}")
    @MethodSource("parseSecondsValidInputs")
    public void parseSecondsValidTest(String hours, String minutes, String seconds) throws ParseException {
        // given
        int expectedSeconds = this.timeComponentsToSecs(hours, minutes, seconds);
        String timeStr = this.timeComponentsToTimeStr(hours, minutes, seconds);

        // when
        int result = ProjectTime.parseSeconds(timeStr);

        // then
        assertEquals(expectedSeconds, result);
    }

    private static Stream<Arguments> parseSecondsValidInputs() {
        return Stream.of(
                Arguments.arguments("0", "00", "00"),
                Arguments.arguments("00", "1", "00"),
                Arguments.arguments("00", "00", "1"),
                Arguments.arguments("00", "59", "00"),
                Arguments.arguments("00", "00", "59")
        );
    }

    @ParameterizedTest(name = "Hours: {0} | Minutes: {1} | Seconds: {2}")
    @MethodSource("parseSecondsInvalidInputs")
    public void parseSecondsInvalidTest(String hours, String minutes, String seconds) {
        // given
        String timeStr = this.timeComponentsToTimeStr(hours, minutes, seconds);

        // when + then
        assertThrows(ParseException.class, () -> ProjectTime.parseSeconds(timeStr));
    }

    private static Stream<Arguments> parseSecondsInvalidInputs() {
        return Stream.of(
                Arguments.arguments("-1", "00", "00"),
                Arguments.arguments("00", "-1", "00"),
                Arguments.arguments("00", "00", "-1"),
                Arguments.arguments("00", "60", "00"),
                Arguments.arguments("0", "00", "60"),
                Arguments.arguments("a", "b", "c")
        );
    }

    @Test(expected = ParseException.class)
    public void parseSecondsMissingComponentTest() throws ParseException {
        // given
        String timeStr = "00:00";

        // when
        ProjectTime.parseSeconds(timeStr);
    }
}

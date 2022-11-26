package de.dominik_geyer.jtimesched.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTimeTest {
    private static int timeComponentsToSecs(String hours, String minutes, String seconds) {
        return Integer.parseInt(hours) * 3600
                + Integer.parseInt(minutes) * 60
                + Integer.parseInt(seconds);
    }

    private static String timeComponentsToTimeStr(String hours, String minutes, String seconds) {
        return String.format("%s:%s:%s", hours, minutes, seconds);
    }

    @ParameterizedTest(name = "Hours: {0} | Minutes: {1} | Seconds: {2}")
    @MethodSource("parseSecondsValidInputs")
    public void parseSecondsValidTest(String hours, String minutes, String seconds) throws ParseException {
        // given
        int expectedSeconds = timeComponentsToSecs(hours, minutes, seconds);
        String timeStr = timeComponentsToTimeStr(hours, minutes, seconds);

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
                Arguments.arguments("00", "00", "59"),

                // Boundary-value analysis
                Arguments.arguments("00", "00", "00"),    // E1 - off-point 2
                Arguments.arguments("00", "00", "59"),    // E7 - on-point
                Arguments.arguments("00", "59", "00"),    // E10 - on-point
                Arguments.arguments("00", "59", "59"),    // E32 - on-point 2

                // Dataflow testing
                Arguments.arguments("00", "00", "59")
        );
    }

    @ParameterizedTest(name = "Hours: {0} | Minutes: {1} | Seconds: {2}")
    @MethodSource("parseSecondsInvalidInputs")
    public void parseSecondsInvalidTest(String timeStr) {
        // when + then
        assertThrows(ParseException.class, () -> ProjectTime.parseSeconds(timeStr));
    }

    private static Stream<Arguments> parseSecondsInvalidInputs() {
        return Stream.of(
                Arguments.arguments(timeComponentsToTimeStr("-1", "00", "00")),
                Arguments.arguments(timeComponentsToTimeStr("00", "-1", "00")),
                Arguments.arguments(timeComponentsToTimeStr("00", "00", "-1")),
                Arguments.arguments(timeComponentsToTimeStr("00", "60", "00")),
                Arguments.arguments(timeComponentsToTimeStr("0", "00", "60")),
                Arguments.arguments(timeComponentsToTimeStr("a", "b", "c")),
                Arguments.arguments("00:00"), // missing-component

                // Boundary-value analysis
                Arguments.arguments(""),    // E1 - off-point 1
                Arguments.arguments(timeComponentsToTimeStr("0a", "00", "00")),    // E4 - on-point
                Arguments.arguments(timeComponentsToTimeStr("00", "00", "-1")),    // E6 - off-point
                Arguments.arguments(timeComponentsToTimeStr("00", "00", "60")),    // E7 - off-point
                Arguments.arguments(timeComponentsToTimeStr("00", "-1", "00")),    // E9 - on-point
                Arguments.arguments(timeComponentsToTimeStr("00", "60", "00")),    // E10 - off-point
                Arguments.arguments(timeComponentsToTimeStr("-1", "00", "00")),    // E12 - off-point
                Arguments.arguments(timeComponentsToTimeStr("00", "-1", "-1")),    // E13 - off-point
                Arguments.arguments(timeComponentsToTimeStr("00", "60", "-1")),    // E14 - off-point
                Arguments.arguments(timeComponentsToTimeStr("-1", "60", "-1")),    // E16 - off-point
                Arguments.arguments(timeComponentsToTimeStr("-1", "-1", "-1")),    // E17 - off-point
                Arguments.arguments(timeComponentsToTimeStr("00", "-1", "60")),    // E20 - off-point
                Arguments.arguments(timeComponentsToTimeStr("00", "60", "60")),    // E21 - off-point
                Arguments.arguments(timeComponentsToTimeStr("-1", "00", "60")),    // E23 - off-point
                Arguments.arguments(timeComponentsToTimeStr("-1", "-1", "60")),    // E24 - off-point
                Arguments.arguments(timeComponentsToTimeStr("-1", "60", "60")),    // E25 - off-point
                Arguments.arguments(timeComponentsToTimeStr("-1", "00", "-1")),    // E30 - off-point 1
                Arguments.arguments(":00:00"),    // E35 - off-point
                Arguments.arguments("00::00"),    // E36 - off-point
                Arguments.arguments("00:00:"),    // E37 - off-point
                Arguments.arguments("00::"),    // E38 - off-point
                Arguments.arguments(":00:"),    // E39 - off-point
                Arguments.arguments("::00"),    // E40 - off-point
                Arguments.arguments("::"),    // E41 - off-point
                Arguments.arguments("000000"),    // E42 - off-point
                Arguments.arguments("00:0000"),    // E43 - off-point
                Arguments.arguments("0000:00"),    // E44 - off-point

                // Dataflow testing
                Arguments.arguments("00")    // E44 - off-point
        );
    }

    @Test
    public void parseSecondsNullTest() {
        // given (E1 - on-point)
        String timeStr = null;

        // when + then
        assertThrows(NullPointerException.class, () -> ProjectTime.parseSeconds(timeStr));
    }

    @Test
    public void parseDateTest() {
        // given
        Date expected = new Date();
        String input = ProjectTime.formatDate(expected);

        // when
        Date result;
        try {
            result = ProjectTime.parseDate(input);
        } catch (ParseException e) {
            fail();
            return;
        }

        // then
        assertEquals(expected.getDay(), result.getDay());
        assertEquals(expected.getMonth(), result.getMonth());
        assertEquals(expected.getYear(), result.getYear());
    }

    @Test
    public void parseDateNullTest() {
        // given
        String input = null;

        // when + then
        try {
            ProjectTime.parseDate(input);
            fail();
        } catch (ParseException e) {
            fail();
        } catch (NullPointerException e) {
            // good
        }
    }
}

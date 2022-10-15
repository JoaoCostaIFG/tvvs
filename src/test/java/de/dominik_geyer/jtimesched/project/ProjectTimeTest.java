package de.dominik_geyer.jtimesched.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
				Arguments.arguments("0", "00", "00"),	// E1 - off-point 2
				Arguments.arguments("00", "00", "59"), 	// E7 - on-point
				Arguments.arguments("00", "59", "00")	// E10 - on-point
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
				Arguments.arguments(""),	// E1 - off-point 1
				Arguments.arguments(timeComponentsToTimeStr("0a", "00", "00")),	// E4 - on-point
				Arguments.arguments(timeComponentsToTimeStr("00", "00", "-1")),	// E6 - off-point
				Arguments.arguments(timeComponentsToTimeStr("00", "00", "60")),	// E7 - off-point
				Arguments.arguments(timeComponentsToTimeStr("00", "-1", "00")),	// E9 - on-point
				Arguments.arguments(timeComponentsToTimeStr("00", "60", "00"))	// E10 - off-point
        );
    }

	@Test
	public void parseSecondsNullTest() {
		// given (E1 - on-point)
		String timeStr = null;

		// when + then
		assertThrows(NullPointerException.class, () -> ProjectTime.parseSeconds(timeStr));
	}
}

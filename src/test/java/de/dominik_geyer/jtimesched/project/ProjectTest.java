package de.dominik_geyer.jtimesched.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectTest {
    private static final int secondsToday = 10;
    private static final int secondsOverall = 30;
    private Project project;

    @BeforeEach
    public void setUp() {
        this.project = new Project();
        this.project.setSecondsToday(secondsToday);
        this.project.setSecondsOverall(secondsOverall);
    }

    @ParameterizedTest()
    @MethodSource("adjustSecondsValidInputs")
    public void adjustSecondsValidTest(int secondsToday) {
        // given
        int expectedSecondsToday = secondsToday;
        int expectedSecondsOverall = ProjectTest.secondsOverall - ProjectTest.secondsToday + secondsToday;

        // when
        this.project.adjustSecondsToday(secondsToday);

        // then
        assertEquals(expectedSecondsToday, this.project.getSecondsToday());
        assertEquals(expectedSecondsOverall, this.project.getSecondsOverall());
    }

    public static Stream<Arguments> adjustSecondsValidInputs() {
        return Stream.of(
                Arguments.arguments(0),
                Arguments.arguments(ProjectTest.secondsToday - 1),
                Arguments.arguments(ProjectTest.secondsToday),
                Arguments.arguments(ProjectTest.secondsToday + 1)
        );
    }

    @ParameterizedTest()
    @MethodSource("adjustSecondsInvalidInputs")
    public void adjustSecondsInvalidTest(int secondsToday) {
        // given
        int expectedSecondsToday = 0;
        int expectedSecondsOverall = ProjectTest.secondsOverall - ProjectTest.secondsToday;

        // when
        this.project.adjustSecondsToday(secondsToday);

        // then
        assertEquals(expectedSecondsToday, this.project.getSecondsToday());
        assertEquals(expectedSecondsOverall, this.project.getSecondsOverall());
    }

    public static Stream<Arguments> adjustSecondsInvalidInputs() {
        return Stream.of(
                Arguments.arguments(-1),
                Arguments.arguments(-(ProjectTest.secondsToday - 1)),
                Arguments.arguments(-ProjectTest.secondsToday),
                Arguments.arguments(-(ProjectTest.secondsToday + 1))
        );
    }

    @Test
    public void startTest() throws ProjectException {
        // given
        Date beforeTime = new Date();

        // when
        this.project.start();
        Date afterTime = new Date();

        // then
        assertTrue(this.project.isRunning());
        // beforeTime <= getTimeStart <= afterTime
        assertTrue(beforeTime.compareTo(this.project.getTimeStart()) <= 0); // before or equal
        assertTrue(afterTime.compareTo(this.project.getTimeStart()) >= 0); // after or equal
    }
}

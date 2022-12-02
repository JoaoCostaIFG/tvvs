package de.dominik_geyer.jtimesched.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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
        secondsToday = Math.max(secondsToday,0);
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
                Arguments.arguments(ProjectTest.secondsToday + 1),

                // boundary-value analysis
                Arguments.arguments(0),  // E1 on-point

                // dataflow testing
                Arguments.arguments(-1),
                Arguments.arguments(1)
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
                Arguments.arguments(-(ProjectTest.secondsToday + 1)),

                // boundary-value analysis
                Arguments.arguments(-1)  // E1 off-point
        );
    }

    @ParameterizedTest
    @MethodSource("setSecondsOverallInputs")
    public void setSecondsOverallTest(int secondsOverall) {
        // when
        this.project.setSecondsOverall(secondsOverall);

        // then
        assertEquals(Math.max(secondsOverall, 0), this.project.getSecondsOverall());
    }

    public static Stream<Arguments> setSecondsOverallInputs() {
        return Stream.of(
                // category partition
                Arguments.arguments(-1),
                Arguments.arguments(0),
                Arguments.arguments(1),

                // boundary-value analysis
                Arguments.arguments(-1),    // off-point
                Arguments.arguments(0)      // on-point
        );
    }

    @ParameterizedTest
    @MethodSource("setSecondsTodayInputs")
    public void setSecondsTodayTest(int secondsToday) {
        // when
        this.project.setSecondsToday(secondsToday);

        // then
        assertEquals(Math.max(secondsToday, 0), this.project.getSecondsToday());
    }

    public static Stream<Arguments> setSecondsTodayInputs() {
        return Stream.of(
                Arguments.arguments(-1),
                Arguments.arguments(0),
                Arguments.arguments(1)
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

    @Test
    public void getElapsedSecondsNotRunningTest() {
        // given
        this.project.setRunning(false);

        // when + then
        assertThrows(ProjectException.class, () -> this.project.getElapsedSeconds());
    }

    @Test
    public void startRunningTest() {
        // given
        this.project.setRunning(true);

        // when + then
        assertThrows(ProjectException.class, () -> this.project.start());
    }

    @Test
    public void pauseNotRunningTest() {
        // given
        this.project.setRunning(false);

        // when + then
        assertThrows(ProjectException.class, () -> this.project.pause());
    }

    @Test
    public void toggleNotRunningTest() {
        // given
        this.project.setRunning(false);

        // when
        this.project.toggle();

        // then
        assertTrue(this.project.isRunning());
    }

    @Test
    public void toggleRunningTest() {
        // given
        this.project.setRunning(true);

        // when
        this.project.toggle();

        // then
        assertFalse(this.project.isRunning());
    }

    @Test
    public void resetTodayTest() {
        // when
        this.project.resetToday();

        // then
        assertEquals(0, this.project.getSecondsToday());
        assertEquals(0, this.project.getQuotaToday());
        assertTrue(this.project.getTimeStart().compareTo(new Date()) <= 0);
    }

    @ParameterizedTest
    @MethodSource("getSecondsTodayInputs")
    public void getSecondsTodayTest(Boolean running) {
        // when
        this.project.setRunning(running);

        //then
        assertEquals(this.project.getSecondsToday(), ProjectTest.secondsToday);
    }

    public static Stream<Arguments> getSecondsTodayInputs() {
        return Stream.of(
                Arguments.arguments(false),
                Arguments.arguments(true)
        );
    }

    @Test
    public void toStringTest() {
        // Given
        Project p = new Project();

        // When
        String pStr = p.toString();

        // Then
        assertEquals("Project [title=project, running=no, secondsOverall=0, secondsToday=0, checked=no]", pStr);
    }

    @Test
    public void notesTest() {
        // Given
        String testNote = "A test note for a test project.";
        Project p = new Project();
        p.setNotes(testNote);

        // When
        String gottenNote = p.getNotes();

        // Then
        assertEquals(testNote, gottenNote);
    }

    @Test
    public void elapsedSecondsTest() throws InterruptedException, ProjectException {
        // Given
        int sleepDuration = 1000;
        Project p = new Project();
        p.setRunning(true);

        // When
        Thread.sleep(sleepDuration);
        int elapsedSecs = p.getElapsedSeconds();

       // Then
        assertTrue(sleepDuration / 1000 <= elapsedSecs);
    }
}

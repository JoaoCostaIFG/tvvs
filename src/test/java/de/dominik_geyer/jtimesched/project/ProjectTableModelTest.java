package de.dominik_geyer.jtimesched.project;

import de.dominik_geyer.jtimesched.gui.table.ProjectTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectTableModelTest {
    private static Project proj1, proj2;

    @BeforeAll
    public static void setUp() {
        proj1 = new Project("1");
        proj2 = new Project("2");
    }

    @ParameterizedTest
    @MethodSource("getValueAtValidInputs")
    public void getValueAtValidTest(ArrayList<Project> projects, int row, int column, Object expected) {
        // given
        ProjectTableModel projectTableModel = new ProjectTableModel(projects);

        // when
        Object result = projectTableModel.getValueAt(row, column);

        // then
        assertEquals(expected, result);
    }

    public static Stream<Arguments> getValueAtValidInputs() {
        //Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, ProjectTableModel.COLUMN_ACTION_DELETE, proj1.d()),
        return Stream.of(
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, ProjectTableModel.COLUMN_TITLE, proj1.getTitle()),
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, ProjectTableModel.COLUMN_TIMEOVERALL, proj1.getSecondsOverall()),
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, ProjectTableModel.COLUMN_CREATED, proj1.getTimeCreated()),
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, ProjectTableModel.COLUMN_CHECK, proj1.isChecked()),
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, ProjectTableModel.COLUMN_TIMETODAY, proj1.getSecondsToday()),
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, ProjectTableModel.COLUMN_ACTION_STARTPAUSE, proj1.isRunning()),
                Arguments.arguments(new ArrayList<>(Arrays.asList(proj1, proj2)), 1, ProjectTableModel.COLUMN_TITLE, proj2.getTitle())
        );
    }

    @ParameterizedTest
    @MethodSource("getValueAtOutOfBoundsInputs")
    public void getValueAtOutOfBoundsTest(ArrayList<Project> projects, int row, int column) {
        // given
        ProjectTableModel projectTableModel = new ProjectTableModel(projects);

        // when + then
        assertThrows(IndexOutOfBoundsException.class, () -> projectTableModel.getValueAt(row, column));
    }

    public static Stream<Arguments> getValueAtOutOfBoundsInputs() {
        return Stream.of(
                Arguments.arguments(new ArrayList<>(), 0, ProjectTableModel.COLUMN_TITLE),
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), -1, ProjectTableModel.COLUMN_TITLE),
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 1, ProjectTableModel.COLUMN_TITLE)
        );
    }

    @ParameterizedTest
    @MethodSource("getValueAtInvalidInputs")
    public void getValueAtInvalidTest(ArrayList<Project> projects, int row, int column) {
        // given
        ProjectTableModel projectTableModel = new ProjectTableModel(projects);

        // when
        Object result = projectTableModel.getValueAt(row, column);

        // then
        assertEquals("wtf?", result);
    }

    public static Stream<Arguments> getValueAtInvalidInputs() {
        return Stream.of(
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, -100),
                Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, 100)
                // Arguments.arguments(new ArrayList<>(Collections.singletonList(proj1)), 0, ProjectTableModel.COLUMN_ACTION_DELETE)
        );
    }
}

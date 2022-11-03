package de.dominik_geyer.jtimesched.project;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProjectSerializerTest {

    private File tmpFile;
    private ProjectSerializer projectSerializer;

    @BeforeEach
    public void setup() {
        try {
            tmpFile = File.createTempFile("project_test_file", ".tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        projectSerializer = new ProjectSerializer(tmpFile.getAbsolutePath());
    }

    @ParameterizedTest
    @MethodSource("readWriteXmlInputs")
    public void readWriteXmlTest(Project p) throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException {
        // Given
        List<Project> givenProjects = Collections.singletonList(p);

        // When
        projectSerializer.writeXml(givenProjects);
        List<Project> gottenProjects = projectSerializer.readXml();

        // Then
        assertEquals(givenProjects.size(), gottenProjects.size());
        assertEquals(p, gottenProjects.get(0));
    }

    public static Stream<Arguments> readWriteXmlInputs() {
        Project checkedProject = new Project();
        checkedProject.setChecked(true);

        Project coloredProject = new Project();
        checkedProject.setColor(new Color(255, 0, 255));

        Project notedProject = new Project();
        notedProject.setNotes("the test note");

        Project nullTitleProject = new Project();
        nullTitleProject.setTitle("");

        return Stream.of(
                Arguments.arguments(new Project()),
                Arguments.arguments(checkedProject),
                Arguments.arguments(coloredProject),
                Arguments.arguments(notedProject),
                Arguments.arguments(nullTitleProject)
        );
    }

    @Test
    public void readWriteXmlWriteRunningProjectTest() throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException {
        // Given
        Project runningProject = new Project();
        runningProject.setRunning(true);
        List<Project> givenProjects = Collections.singletonList(runningProject);

        // When
        projectSerializer.writeXml(givenProjects);
        List<Project> gottenProjects = projectSerializer.readXml();

        // Then
        assertEquals(givenProjects.size(), gottenProjects.size());
        // it sets running projects as not running when saving
        runningProject.setRunning(false);
        assertEquals(runningProject, gottenProjects.get(0));
    }

    @Test
    public void readWriteXmlReadRunningProjectTest() throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException {
        // Given
        Project runningProject = new Project();
        runningProject.setRunning(true);
        List<Project> givenProjects = Collections.singletonList(runningProject);

        // When
        projectSerializer.writeXml(givenProjects);
        // set project running (on written file)
        List<String> xml = Files.readAllLines(this.tmpFile.toPath());
        xml.set(6, xml.get(6).replace("no", "yes"));
        Files.write(this.tmpFile.toPath(), xml);

        List<Project> gottenProjects = projectSerializer.readXml();

        // Then
        assertEquals(givenProjects.size(), gottenProjects.size());
        assertEquals(runningProject, gottenProjects.get(0));
    }

    @Test
    public void readWriteXmlNoQuotaTest() throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException {
        // Given
        Project p = new Project();
        List<Project> givenProjects = Collections.singletonList(p);

        // When
        projectSerializer.writeXml(givenProjects);
        // set project running (on written file)
        List<String> xml = Files.readAllLines(this.tmpFile.toPath());
        xml.remove(9);
        Files.write(this.tmpFile.toPath(), xml);

        List<Project> gottenProjects = projectSerializer.readXml();

        // Then
        assertEquals(givenProjects.size(), gottenProjects.size());
        assertEquals(p, gottenProjects.get(0));
    }

    @Test
    public void readWriteXmlNoNotesTest() throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException {
        // Given
        Project p = new Project();
        List<Project> givenProjects = Collections.singletonList(p);

        // When
        projectSerializer.writeXml(givenProjects);
        // set project running (on written file)
        List<String> xml = Files.readAllLines(this.tmpFile.toPath());
        xml.remove(3);
        Files.write(this.tmpFile.toPath(), xml);

        List<Project> gottenProjects = projectSerializer.readXml();

        // Then
        assertEquals(givenProjects.size(), gottenProjects.size());
        assertEquals(p, gottenProjects.get(0));
    }

    @AfterEach
    public void teardown() {
        tmpFile.delete();
    }
}

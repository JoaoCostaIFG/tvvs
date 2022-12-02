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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ProjectSerializerTest {

    private File tmpFile;
    private ProjectSerializer projectSerializer;

    public static boolean projectsEqual(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null || o1.getClass() != o2.getClass()) return false;
        Project p1 = (Project) o1;
        Project p2 = (Project) o2;
        return p1.isChecked() == p2.isChecked() && p1.getSecondsOverall() == p2.getSecondsOverall() && p1.getSecondsToday() == p2.getSecondsToday() && p1.getQuotaOverall() == p2.getQuotaOverall() && p1.getQuotaToday() == p2.getQuotaToday() && p1.isRunning() == p2.isRunning() && p1.getTitle().equals(p2.getTitle()) && p1.getNotes().equals(p2.getNotes()) && p1.getTimeCreated().equals(p2.getTimeCreated()) && Objects.equals(p1.getColor(), p2.getColor()) && p1.getTimeStart().equals(p2.getTimeStart());
    }

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
        assertTrue(projectsEqual(p, gottenProjects.get(0)));
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

        Project timedProject = new Project();
        timedProject.setTimeStart(new Date(333));
        timedProject.setTimeCreated(new Date(666));
        timedProject.setSecondsToday(333);
        timedProject.setSecondsOverall(666);

        Project quotaProject = new Project();
        quotaProject.setQuotaToday(666);
        quotaProject.setQuotaOverall(999);

        return Stream.of(
                Arguments.arguments(new Project()),
                Arguments.arguments(checkedProject),
                Arguments.arguments(coloredProject),
                Arguments.arguments(notedProject),
                Arguments.arguments(nullTitleProject),
                Arguments.arguments(timedProject),
                Arguments.arguments(quotaProject)
        );
    }

    @Test
    public void xmlContentTest() throws TransformerConfigurationException, IOException, SAXException {
        // Given
        Project p = new Project();
        p.setTimeCreated(new Date(0));
        p.setTimeStart(new Date(0));
        p.setColor(new Color(255, 0, 255));
        List<Project> givenProjects = Collections.singletonList(p);

        // When
        projectSerializer.writeXml(givenProjects);

        // Then
        final String testStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><projects version=\"unknown\">\n" +
                "    <project>\n" +
                "        <title>project</title>\n" +
                "        <notes/>\n" +
                "        <created>0</created>\n" +
                "        <started>0</started>\n" +
                "        <running>no</running>\n" +
                "        <checked>no</checked>\n" +
                "        <time overall=\"0\" today=\"0\"/>\n" +
                "        <quota overall=\"0\" today=\"0\"/>\n" +
                "        <color red=\"255\" green=\"0\" blue=\"255\" alpha=\"255\"/>\n" +
                "    </project>\n" +
                "</projects>";

        List<String> xml = Files.readAllLines(this.tmpFile.toPath());
        assertEquals(testStr, String.join("\n", xml));
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
        assertTrue(projectsEqual(runningProject, gottenProjects.get(0)));
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
        assertTrue(projectsEqual(runningProject, gottenProjects.get(0)));
    }

    @Test
    public void readWriteXmlNoQuotaTest() throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException {
        // Given
        Project p = new Project();
        List<Project> givenProjects = Collections.singletonList(p);

        // When
        projectSerializer.writeXml(givenProjects);
        // remove quota line
        List<String> xml = Files.readAllLines(this.tmpFile.toPath());
        xml.remove(9);
        Files.write(this.tmpFile.toPath(), xml);

        List<Project> gottenProjects = projectSerializer.readXml();

        // Then
        assertEquals(givenProjects.size(), gottenProjects.size());
        assertTrue(projectsEqual(p, gottenProjects.get(0)));
    }

    @Test
    public void readWriteXmlNoNotesTest() throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException {
        // Given
        Project p = new Project();
        List<Project> givenProjects = Collections.singletonList(p);

        // When
        projectSerializer.writeXml(givenProjects);
        // remove notes line
        List<String> xml = Files.readAllLines(this.tmpFile.toPath());
        xml.remove(3);
        Files.write(this.tmpFile.toPath(), xml);

        List<Project> gottenProjects = projectSerializer.readXml();

        // Then
        assertEquals(givenProjects.size(), gottenProjects.size());
        assertTrue(projectsEqual(p, gottenProjects.get(0)));
    }

    @AfterEach
    public void teardown() {
        tmpFile.delete();
    }
}

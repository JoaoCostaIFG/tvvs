Project.java
- public void setSecondsToday(int secondsToday)

ProjectSrializzaer.java
- protected static void addXmlElement(TransformerHandler hd, String element, AttributesImpl atts, Object data) throws SAXException {

ProjectTableModel.java
- public Object getValueAt(int row, int column)
- public Class<?> getColumnClass(int column) {
- public void removeProject(int row) {

ProjectTime.java
- public static String formatSeconds(int s) {
# Assignment 2 - G03P02

## Group information

- Ana Inês Oliveira de Barros - `up201806593@fe.up.pt`;
- João de Jesus Costa - `up201806560@fe.up.pt`

## Function Selection Process

The aim of this assignment is to perform black-box testing. This is problematic because none of 
methods in the code are documented (e.g. javadoc). In order to find the purpose of each method, 
we need to read the source code, which defeats the purpose of black-box testing. 

Since we aren't using mocks, we tried to test methods that didn't depend on other objects of 
the project. We discarded functions belonging to the `gui` package due to its
dependence on *swing*. The `misc` package was also discarded since it only contains one function (not enough for the completion of the assignment). 
Function related to the elapsed time were also ignored.  

The selected package for testing was the `de.dominik_geyer.jtimesched.project` package. 
## Category-Partition - Function 1

**Function**: `public static int parseSeconds(String strTime)` in `ProjectTime.java` line 36.

**Reason for selection:** Parsing of time string might be error prone.

**Function's purpose:** This function receives a string representing time, in `hh:mm:ss` format, and returns the total number of seconds it represents.

### Steps

1. Identify the parameters:
    - String representing time 
2. Characteristics of the parameters
    - The string should represent a valid time in this format `hh:mm:ss` (where *hh* represents the hours, *mm* represents the minutes and *ss* represents the seconds).
3. Add constraints
    - Negative time is not allowed
    - Seconds lie within the interval [0, 59]
    - Minutes lie within the interval [0, 59]
4. Generate combinations
    | Partition             | Input    | Expected output  |
    |-----------------------|----------|------------------|
    | Negative hour         | -1:00:00 | Thrown exception |
    | Negative minute       | 00:-1:00 | Thrown exception |
    | Negative seconds      | 00:00:-1 | Thrown exception |
    | Minute out of bounds  | 00:60:00 | Thrown exception |
    | Seconds out of bounds | 00:00:60 | Thrown exception |
    | Not a number          | a:b:c    | Thrown exception |
    | Missing component     | 00:00    | Thrown exception |
    | One digit minute      | 00:1:00  | 60               |
    | One digit second      | 00:00:01 | 1                |
    | Two digit minutes     | 00:59:00 | 999x3600         |
    | Two digit seconds     | 00:00:59 | 999x3600         |

## Unit Test - Function 1

We created three tests: one for valid inputs, one for missing components, and another one for invalid inputs. 
Each test receives the inputs from a stream of arguments from an auxiliar method.
The inputs tested are the same as the ones present on the previous table. 

**Valid test:**
```java 
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
```

**Missing component test:**
```java 
@Test(expected = ParseException.class)
public void parseSecondsMissingComponentTest() throws ParseException {
    // given
    String timeStr = "00:00";

    // when
    ProjectTime.parseSeconds(timeStr);
}
```

**Invalid inputs test:**
```java 
@ParameterizedTest(name = "Hours: {0} | Minutes: {1} | Seconds: {2}")
@MethodSource("parseSecondsInvalidInputs")
public void parseSecondsInvalidTest(String hours, String minutes, String seconds) {
    // given
    String timeStr = this.timeComponentsToTimeStr(hours, minutes, seconds);

    // when + then
    assertThrows(ParseException.class, () -> ProjectTime.parseSeconds(timeStr));
}
```

All the tests pass successfully.

## Category-Partition - Function 2

**Function**: `public void adjustSecondsToday(int secondsToday)` in `Project.java` line 192.

**Reason for selection:** Adjusting the time of a project is an important feature for the application that takes user input to update the time. This function is a crucial part of this feature.

**Function's purpose:** This function receives a integer representing the number of seconds that took complete a task. Then, the function updates the number of seconds spent on the task today and overall.

### Steps

1. Identify the parameters:
    - Int representing the number of seconds
2. Characteristics of the parameters
    - The integer should represent a positive number between 0 and infinite.
3. Add constraints
    - Negative time is not allowed
4. Generate combinations
    | Partition                               | Input                | Expected output  |
    |-----------------------------------------|----------------------|------------------|
    | Negative seconds                        | -1                   | 0                |
    | Immediately below time today complement | -(secondsToday - 1)  | 0                |
    | Negative time today                     | - secondsToday       | 0                |
    | Immediately above time today            | - (secondsToday + 1) | 0                |
    | Zero                                    | 0                    | 0                |
    | Immediately below time today            | secondsToday - 1     | secondsToday - 1 |
    | Time today                              | secondsToday         | secondsToday     |
    | Immediately above time today            | secondsToday + 1     | secondsToday + 1 |


## Unit Test - Function 2

We created three tests: one for valid inputs and another one for invalid inputs. 
Each test receives the inputs from a stream of arguments from an auxiliar method.
The inputs tested are the same as the ones present on the previous table. 

**Valid test:**
```java 
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
```

**Invalid inputs test:**
```java 
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
```

All the tests pass successfully.

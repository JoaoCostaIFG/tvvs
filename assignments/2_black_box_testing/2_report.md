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
Functions related to elapsed time were also ignored.  

The selected package for testing was the `de.dominik_geyer.jtimesched.project` package. 
## Category-Partition - Function 1

**Function**: `public static int parseSeconds(String strTime)` in `ProjectTime.java` line 36.

**Reason for selection:** Parsing of time string might be error prone.

**Function's purpose:** This function receives a string representing time, in `hh:mm:ss` format, and returns the total number of seconds it represents.

### Steps

1. Identify the parameters:
    - String representing time.
2. Characteristics of the parameters
    - The string should represent a valid time in this format `hh:mm:ss` (where *hh* represents the hours, *mm* represents the minutes and *ss* represents the seconds).
3. Add constraints
    - Negative time is not allowed.
    - Seconds lie within the interval [0, 59].
    - Minutes lie within the interval [0, 59].
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
    | Two digit minutes     | 00:59:00 | 59x60            |
    | Two digit seconds     | 00:00:59 | 59               |

## Unit Test - Function 1

We created three tests: one for valid inputs, one for missing components, and another one for invalid inputs. 
Each test receives the inputs from a stream of arguments from an auxiliar method.
The inputs tested are the same as the ones present on the previous table. 

**Valid inputs test**:
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

**Missing component test**:
```java 
@Test(expected = ParseException.class)
public void parseSecondsMissingComponentTest() throws ParseException {
    // given
    String timeStr = "00:00";

    // when
    ProjectTime.parseSeconds(timeStr);
}
```

**Invalid inputs test**:
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
**Test Results**: 
All the tests pass successfully.

## Category-Partition - Function 2

**Function**: `public void adjustSecondsToday(int secondsToday)` in `Project.java` line 192.

**Reason for selection**: Throughout the usage of the application, the user will frequently set the time a task has
taken to accomplish. This is an important feature that this functions is part of.

**Function's purpose:** This function receives an integer representing the number of seconds that it took to complete a task. Then, the function updates the number of seconds spent today on the task and overall.

### Steps

1. Identify the parameters:
    - Integer representing the number of seconds.
2. Characteristics of the parameters
    - The integer should represent a positive number between 0 and infinite.
3. Add constraints
    - Negative time is not allowed.
4. Generate combinations
    | Partition                               | Input                | Expected output  |
    |-----------------------------------------|----------------------|------------------|
    | Negative seconds                        | -1                   | 0                |
    | Immediately below time today complement | -(secondsToday - 1)  | 0                |
    | Negative time today                     | - secondsToday       | 0                |
    | Immediately above time today complement | - (secondsToday + 1) | 0                |
    | Zero                                    | 0                    | 0                |
    | Immediately below time today            | secondsToday - 1     | secondsToday - 1 |
    | Time today                              | secondsToday         | secondsToday     |
    | Immediately above time today            | secondsToday + 1     | secondsToday + 1 |


## Unit Test - Function 2

We created two tests: one for valid inputs and another one for invalid inputs. 
Each test receives the inputs from a stream of arguments from an auxiliar method.
The inputs tested are the same as the ones present on the previous table. 

**Valid inputs test:**
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

**Test Results**: 
All the tests pass successfully.

## Category-Partition - Function 3

**Function**: `public Object getValueAt(int row, int column)` in `ProjectTableModel.java` line 65.

**Reason for selection:** This function takes care of the selection of values present on the table. As the user interacts with the application, it is important that the selected value is the correct one. It is required for the application to work as intended.

**Function's purpose:** Given two integers row and column, this function returns the value at the given column for the project at the given row.

### Steps

1. Identify the parameters:
    - int representing the row
    - int representing the column
    - list of projects present on the table model
2. Characteristics of the parameters
    - The row integer should represent a positive number between 0 and the total number of projects.
    - The column integer should represent a positive number between 0 and the total number of categories.
3. Add constraints
    - Negative rows are not allowed.
    - Negative columns are not allowed.
    - No single row is valid when there are no projects.
    - No single column is valid when there are no projects.
    - Rows must point to a project.
    - Columns must point to a category.
    - Lists of projects mustn't be empty.
4. Generate combinations
    | Partition                   | Input (List, line, column)               | Expected output        |
    |-----------------------------|------------------------------------------|------------------------|
    | Title singleton list        | One project, Line 0, Title column        | Project's title        |
    | Time overall singleton list | One project, Line 0, Time overall column | Project's time overall |
    | Time created singleton list | One project, Line 0, Time created column | Project's time crated  |
    | Checked singleton list      | One project, Line 0, Checked column      | Project's checked      |
    | Time today singleton list   | One project, Line 0, Time today column   | Project's time today   |
    | Start/Pause singleton list  | One project, Line 0, Start/pause column  | Project's start/pause  |
    | Title two projects list     | Two projects, Line 1, Title column       | Second project's title |
    | Title empty list            | No projects, Line 0, Title column        | Thrown exception       |
    | Out of lower bound project  | One project, Line -1, Title column       | Thrown exception       |
    | Out of upper bound project  | One project, Line 1, Title column        | Thrown exception       |
    | Out of lower bound column   | One project, Line 0, -100                | Default case           |
    | Out of upper bound column   | One project, Line 0, 100                 | Default case           |
    | Out of upper bound column   | One project, Line 0, Delete column       | Default case           |

## Unit Test - Function 3

We created three tests: one for valid inputs, one for out of bounds inputs, and another one for invalid inputs. 
Each test receives the inputs from a stream of arguments from an auxiliar method.
The inputs tested are the same as the ones present on the previous table. 

**Valid inputs test:**
```java 
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
```


**Out of bounds inputs test:**
```java 
@ParameterizedTest
@MethodSource("getValueAtOutOfBoundsInputs")
public void getValueAtOutOfBoundsTest(ArrayList<Project> projects, int row, int column) {
    // given
    ProjectTableModel projectTableModel = new ProjectTableModel(projects);

    // when + then
    assertThrows(IndexOutOfBoundsException.class, () -> projectTableModel.getValueAt(row, column));
}
```

**Invalid inputs test:**
```java 
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
```

**Test Results**: 
Only the invalid inputs test failed for the inputs: one project list, line 0, delete column. 
This specific test fails because of the "delete column" argument. It is expected that the value 
of the delete column of a project would be the default case. However, it returns whether 
the project is currently running or not. This does not makes sense in the context of the project
since it is expected that the user deletes projects whether they are running or not.
For this reason, we consider this case to be a fault. 

## Category-Partition - Function 4

**Function**: `public void setSecondsOverall(int secondsOverall)` in `Project.java` line 178.

**Reason for selection**: It is important that this function works as expected since
other methods depend on it.

**Function's purpose**: This function sets the seconds overall of a project as the
value it receives as an argument (if valid). 

### Steps

1. Identify the parameters:
    - Integer representing the number of seconds overall.
2. Characteristics of the parameters
    - The integer should represent a positive number between 0 and infinite.
3. Add constraints
    - Negative time is not allowed.
4. Generate combinations
    | Partition          | Input | Expected output |
    |--------------------|-------|-----------------|
    | Negative seconds   | -100  | 0               |
    | Negative seconds 2 | -1    | 0               |
    | Zero seconds       | 0     | 0               |
    | Positive seconds   | 1     | 1               |
    | Positive seconds 2 | 100   | 100             |

## Unit Test - Function 4

We created one test for all inputs.
The test receives the inputs from a stream of arguments from an auxiliar method.
The inputs tested are the same as the ones present on the previous table. 

**Test:**
```java 
@ParameterizedTest
@MethodSource("setSecondsOverallInputs")
public void setSecondsOverallTest(int secondsOverall) {
    // when
    this.project.setSecondsOverall(secondsOverall);

    // then
    assertEquals(Math.max(secondsOverall, 0), this.project.getSecondsOverall());
}
```
`
**Test Results**: 
All the tests pass successfully.

## Category-Partition - Function 5

**Function**: `public void setSecondsToday(int secondsToday)` in `Project.java` line 185.

**Reason for selection**: It is important that this function works as expected since
other methods depend on it.

**Function's purpose**: This function sets the seconds today of a project as the
value it receives as an argument (if valid). 

### Steps

1. Identify the parameters:
    - Integer representing the number of seconds today.
2. Characteristics of the parameters
    - The integer should represent a positive number between 0 and infinite.
3. Add constraints
    - Negative time is not allowed.
4. Generate combinations
    | Partition          | Input | Expected output |
    |--------------------|-------|-----------------|
    | Negative seconds   | -100  | 0               |
    | Negative seconds 2 | -1    | 0               |
    | Zero seconds       | 0     | 0               |
    | Positive seconds   | 1     | 1               |
    | Positive seconds 2 | 100   | 100             |

## Unit Test - Function 5

We created one test for all inputs.
The test receives the inputs from a stream of arguments from an auxiliar method.
The inputs tested are the same as the ones present on the previous table. 

**Test:**
```java 
@ParameterizedTest
@MethodSource("setSecondsTodayInputs")
public void setSecondsTodayTest(int secondsToday) {
    // when
    this.project.setSecondsToday(secondsToday);

    // then
    assertEquals(Math.max(secondsToday, 0), this.project.getSecondsToday());
}
```

**Test Results**: 
All the tests pass successfully.

**Note about function 4 and 5**: 
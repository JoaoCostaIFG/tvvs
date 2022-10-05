# Assignment 2 - G03P02

## Group information

- Ana Inês Oliveira de Barros - `up201806593@fe.up.pt`;
- João de Jesus Costa - `up201806560@fe.up.pt`

## Function Selection Process

The aim of this assignment is to perform black-box testing. This is problematic
because none of the methods in the code are documented (e.g. JavaDoc). In order to
find the purpose of each method, we need to read the source code, which somewhat
defeats the purpose of black-box testing.

Since we aren't using mocks, we tried to test methods that didn't depend on
other objects of the project. We discarded functions belonging to the `gui`
package due to its dependence on _swing_. The `misc` package was also discarded
since it only contains one function (not enough for the completion of the
assignment). Functions related to elapsed time were also ignored.

The selected package for testing was the `de.dominik_geyer.jtimesched.project`
package.

## Method 1

**Method**: `public static int parseSeconds(String strTime)` in
`ProjectTime.java` line 36.

**Purpose**: This function receives a string representing time, in `hh:mm:ss`
format, and returns the total number of seconds it represents.

**Reason for selection**: This method deals with the parsing of user input, which
needs to be robust.

### Steps

1. Identify the parameters:
    - String representing time.
2. Characteristics of the parameters
    - The string should represent a valid time in the format: `hh:mm:ss`
    - _hh_ represents the hours
    - _mm_ represents the minutes
    - _ss_ represents the seconds
    - It should be possible to pass single digits for each component
3. Add constraints
    - Negative time is not allowed.
    - Seconds lie within the interval [0, 59].
    - Minutes lie within the interval [0, 59].
4. Generate combinations

| Partition             | Input      | Expected outcome |
| --------------------- | ---------- |------------------|
| Negative hour         | `-1:00:00` | Thrown exception |
| Negative minute       | `00:-1:00` | Thrown exception |
| Negative seconds      | `00:00:-1` | Thrown exception |
| Minute out of bounds  | `00:60:00` | Thrown exception |
| Seconds out of bounds | `00:00:60` | Thrown exception |
| Not a number          | `a:b:c`    | Thrown exception |
| Missing component     | `00:00`    | Thrown exception |
| One digit minute      | `00:1:00`  | 60               |
| One digit second      | `00:00:01` | 1                |
| Two digit minutes     | `00:59:00` | 59x60            |
| Two digit seconds     | `00:00:59` | 59               |

### Unit Test

We created three tests: one for valid inputs, one for missing components, and
another one for invalid inputs. Each test receives the inputs from a stream of
arguments from an auxiliary method. The inputs tested are the same as the ones
presented in the previous table:

- Valid inputs test:
    - We pass a string that should be considered valid and result in an output;
    - E.g. "00:59:01".
- Missing components test:
    - We pass a string that is missing one of that components;
    - We noticed that the parsing fails, so we consider it the expected behavior;
    - The method could be adapted to accept this kind of input as valid;
    - E.g. "00:00".
- Invalid inputs test:
    - Test inputs that are known to be invalid: invalid characters (e.g. letters),
      negative time, minutes/seconds out-of-range, etc...
    - The parsing of this should always result in a failure;
    - E.g. "00:60:00".

All the tests pass successfully.

## Method 2

**Method**: `public void adjustSecondsToday(int secondsToday)` in `Project.java`
line 192.

**Purpose:** This function receives an integer representing the number of
seconds that it took to complete a task. Then, the function updates the number
of seconds spent on the task today, as well as the overall time spent on it.

**Reason for selection**: Throughout the usage of the application, the user will
frequently set the time a task has taken to accomplish. This is a method that deals
with user input, so it needs to be reliable.

### Steps

1. Identify the parameters:
    - Integer representing the number of seconds.
2. Characteristics of the parameters
    - The integer should represent a positive number between 0 and infinite.
3. Add constraints
    - Negative time is not allowed.
4. Generate combinations

| Partition                               | Input                | Expected outcome |
| --------------------------------------- |----------------------|------------------|
| Negative seconds                        | -1                   | 0                |
| Immediately below time today complement | - (secondsToday - 1) | 0                |
| Negative time today                     | - secondsToday       | 0                |
| Immediately above time today complement | - (secondsToday + 1) | 0                |
| Zero                                    | 0                    | 0                |
| Immediately below time today            | secondsToday - 1     | secondsToday - 1 |
| Time today                              | secondsToday         | secondsToday     |
| Immediately above time today            | secondsToday + 1     | secondsToday + 1 |

### Unit Test

We created two tests: one for valid inputs and another one for invalid inputs. Each test
receives the inputs from a stream of arguments from an auxiliary method. The inputs
tested are the same as the ones present in the previous table.

- Valid inputs test:
    - We try setting the value to 0 and to values around the current number of _
      seconds today_;
    - This should be valid and result in the number of seconds today being adjusted to
      the given number of seconds;
    - Furthermore, the overall time should be adjusted accordingly;
    - E.g. `10`.
- Invalid inputs test:
    - We try adjusting the time to negative values and cause the time delta to be
      negative;
    - This tests whether the method can handle negative values, and negative time
      deltas;
    - This should result in the _seconds today_ being 0 and the overall time being
      adjusted accordingly (consider the input as 0);
    - E.g. `-1`.

**Test Results**: All the tests pass successfully.

## Method 3

**Method**: `public Object getValueAt(int row, int column)` in `ProjectTableModel.java`
line 65.

**Purpose:** Given two integers row and column, this function returns the value in the
given column for the project at the given row.

**Reason for selection:** This function takes care of the selection of values
present on the table. As the user interacts with the application, it is
important that the selected value is the correct one. It is required for the
application to work as intended.

### Steps

1. Identify the parameters:
    - int representing the row
    - int representing the column
    - list of projects present on the table model
2. Characteristics of the parameters
    - The row integer should represent a positive number between 0 and the total
      number of projects.
    - The column integer should represent a positive number between 0 and the
      total number of categories.
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
| Time created singleton list | One project, Line 0, Time created column | Project's time created  |
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

### Unit Test

We created three tests: one for valid inputs, one for out-of-bounds inputs, and another
one for invalid inputs. Each test receives the inputs from a stream of arguments from an
auxiliary method. The inputs tested are the same as the ones present in the previous
table.

- Valid inputs test:
    - In this test we pass valid rows (where there is a project) and valid columns;
    - We expect to get the value of the project corresponding to the column chosen;
    - E.g. "One project, Line 0, Title column".
- Out-of-bounds inputs test:
    - In this test we play with the list of projects;
    - Trying to get a project from an empty list;
    - Passing a negative row value;
    - Choosing a row where there isn't a project (non-empty project list);
    - E.g. "One project, Line -1, Title column".
- Invalid inputs test:
    - In this test we try getting invalid column numbers from a project;
    - This includes negative column numbers and column numbers that don't correspond to
      any
      column;
    - In our opinion, the _delete column_ should be considered since it doesn't show any
      value;
    - The expected output of this test is a default output. In this case, the output
      is "wtf?". We believe the test uses a default value instead of an exception,
      because Swing doesn't handle that.

**Test Results**: Only the invalid inputs test failed for the inputs: one project list,
line 0, delete column. This specific test fails because of the"delete column" argument.
It is expected that the value of the delete column of a project would be the default
case. However, it returns whether the project is currently running or not. This does not
make sense in the context of the project since it is expected that the user deletes
projects whether they are running or not. For this reason, we consider this case to be a
fault.

## Method 4

**Method**: `public void setSecondsOverall(int secondsOverall)` in
`Project.java` line 178.

**Function's purpose**: This function sets the _seconds overall_ of a project as the
value it receives as an argument (if valid).

**Reason for selection**: It is important that this function works as expected since
other methods depend on it.

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

### Unit Test

We created one test for all inputs. The test receives the inputs from a stream of
arguments from an auxiliary method. The inputs tested are the same as the ones present
in the previous table.

- We try setting the value of secondsOverall to 0 and to values around;
- For positive numbers (including 0), this should be valid and result in the number of
  seconds overall being adjusted to the given number of seconds;
- For invalid inputs (negative times), the value of seconds overall should be set to 0.

**Test Results**: All the tests pass successfully.

## Method 5

**Method**: `public void setSecondsToday(int secondsToday)` in `Project.java`
line 185.

**Purpose**: This function sets the seconds today of a project as the value it receives
as an argument (if valid).

**Reason for selection**: It is important that this function works as expected
since other methods depend on it.

### Steps

1. Identify the parameters:
    - Integer representing the number of seconds today.
2. Characteristics of the parameters
    - The integer should represent a positive number between 0 and infinite.
3. Add constraints
    - Negative time is not allowed.
4. Generate combinations

| Partition          | Input | Expected output |
| ------------------ | ----- | --------------- |
| Negative seconds   | -100  | 0               |
| Negative seconds 2 | -1    | 0               |
| Zero seconds       | 0     | 0               |
| Positive seconds   | 1     | 1               |
| Positive seconds 2 | 100   | 100             |

### Unit Test

We created one test for all inputs. The test receives the inputs from a stream
of arguments from an auxiliary method. The inputs tested are the same as the ones
present in the previous table.

- We try setting the value of secondsToday to 0 and to the values around;
- For positive numbers, this should be valid and result in the number of seconds today
  being adjusted to the given number of seconds;
- For invalid inputs (negative times), the value of seconds today should be set to 0.

**Test Results**: All the tests pass successfully.

## Note about methods 4 and 5

We noticed that the function `setSecondsToday(int secondsToday)` does not update the
value of _seconds overall_ of a project. If the author of the project is not careful
with keeping both values updated accordingly, it might lead to problems.
For example, a task could have 10 seconds today and 0 seconds overall, which
does not make sense. For this reason, we think that this function shouldn't be
publicly exposed and that this update should have been implemented differently.

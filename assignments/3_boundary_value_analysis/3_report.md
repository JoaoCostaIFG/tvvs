# Assignment 3 - G03P02

## Group information

- Ana Inês Oliveira de Barros - `up201806593@fe.up.pt`;
- João de Jesus Costa - `up201806560@fe.up.pt`

## Function Selection Process

From the previous assinment's report:

> The aim of this assignment is to perform black-box testing. This is
> problematic because none of methods in the code are documented (e.g. javadoc).
> In order to find the purpose of each method, we needed to follow our intuition
> about the names of the methods, arguments, and classes. Having more extensive
> documentation would allow for better black-box testing.
>
> Since we aren't using mocks, we tried to test methods that didn't depend on
> other objects of the project. We discarded functions belonging to the `gui`
> package due to its dependence on _swing_. The `misc` package was also
> discarded since it only contains one function (not enough for the completion
> of the assignment). Functions related to elapsed time were also ignored.
>
> The selected package for testing was the `de.dominik_geyer.jtimesched.project`
> package.

In this assignment, we selected 3 methods used in the previous one.

## Method 1

**Method**: `public void setSecondsOverall(int secondsOverall)` in
`Project.java` line 178.

**Function's purpose**: This function sets the _seconds overall_ of a project as
the value it receives as an argument (if valid).

**Reason for selection**: It is important that this function works as expected
since other methods depend on it.

### Steps

1. Identify the parameters:
   - `secondsOverall` - integer (`int`) representing the number of seconds
     overall.
2. Characteristics of the parameters
   - The integer should represent a positive number between 0 and infinity.
3. Add constraints
   - Negative time is not allowed - `secondsOverall >= 0`
4. Partitions
   - E1 -- negative number
   - E2 -- positive number (including 0)
5. Boundaries
   - `secondsOverall`:
     - On-point - `0`;
     - Off-point - `-1`.
6. Generate tests

| Partition                  | Input | Expected output |
| -------------------------- | ----- | --------------- |
| secondsOverall - On-point  | 0     | 0               |
| secondsOverall - Off-point | -1    | 0               |

### Unit Tests

We created one test with the inputs of each line on the table.

**Results**: all the tests pass successfully.

## Method 2

**Method**: `public void adjustSecondsToday(int secondsToday)` in `Project.java`
line 192.

**Purpose:** This function receives an integer representing the number of
seconds that it took to complete a task. Then, the function updates the number
of seconds spent on the task today, as well as the overall time spent on it.

**Reason for selection**: Throughout the usage of the application, the user is
able to set the time spent on a task. As such, this is a method that deals with
user input, so it needs to be reliable.

### Steps

1. Identify the parameters:
   - `secondsToday` - integer representing the number of seconds.
2. Characteristics of the parameters
   - The integer should represent a positive number between 0 and infinity.
3. Add constraints
   - Negative time is not allowed - `secondsToday >= 0`
4. Partitions
   - E1 -- negative number
   - E2 -- positive number (including 0)
5. Boundaries
   - `secondsToday`:
     - On-point - `0`;
     - Off-point - `-1`.
6. Generate tests

| Partition                | Input | Expected outcome |
| ------------------------ | ----- | ---------------- |
| secondsToday - On-point  | 0     | 0                |
| secondsToday - Off-point | -1    | 0                |

### Unit Tests

We created one test with the inputs of each line on the table.

We created two tests: one for valid inputs and another one for invalid inputs.

- Valid inputs test:
  - We try setting the value to 0 and to the values around the current number of
    _seconds spent today_ on the task;
  - This should be valid and result on the number of seconds today being
    adjusted to the given number of seconds;
  - Furthermore, the overall time should be adjusted accordingly;
  - E.g. `10`.
- Invalid inputs test:
  - We try adjusting the time to negative values, and cause the time delta to be
    negative;
  - This tests whether the method can handle negative values, and negative time
    deltas;
  - This should result in the _seconds today_ being 0 and the overall time being
    adjusted accordingly (consider the input as 0);
  - E.g. `-1`.

**Results**: All the test-cases pass successfully.

## Method 3

**Method**: `public static int parseSeconds(String strTime)` in
`ProjectTime.java` line 36.

**Purpose**: This function receives a string representing time, in `hh:mm:ss`
format, and returns the total number of seconds it represents.

**Reason for selection**: This method deals with parsing of user input, which
needs to be robust.

### Steps

1. Identify the parameters:
   - `strTime` - string representing time in 3 components:
     - hours (hh);
     - minutes (mm);
     - seconds (ss).
2. Characteristics of the parameters
   - The string should represent a valid time in the format: `hh:mm:ss`
   - _hh_ represents the hours
   - _mm_ represents the minutes
   - _ss_ represents the seconds
   - It should be possible to pass single digits for each component
3. Add constraints
   - Input can't be `null`
   - Components can't be omitted
   - Components can only contain numbers
   - Negative time is not allowed
   - Seconds lie within the interval [0, 59]
   - Minutes lie within the interval [0, 59]
4. Boundaries
   - `hh != ""`
     - On-point -- any non-empty string
     - Off-point -- `""`
   - `mm != ""`
     - On-point -- any non-empty string
     - Off-point -- `""`
   - `ss != ""`
     - On-point -- any non-empty string
     - Off-point -- `""`
   - `0 <= hh`
     - On-point -- `0`
     - Off-point -- `-1`
   - `0 <= mm <= 59`
     - On-point -- `0` and `59`
     - Off-point -- `-1` and `60`
   - `0 <= ss <= 59`
     - On-point -- `0` and `59`
     - Off-point -- `-1` and `60`
5. Generate combinations

| Partition  | Input      | Expected outcome |
| ---------- | ---------- | ---------------- |
| Boundary 1 | `-1:00:00` | Thrown exception |
| Boundary 1 | `00:-1:00` | Thrown exception |
| Boundary 1 | `00:00:-1` | Thrown exception |
| Boundary 1 | `00:60:00` | Thrown exception |
| Boundary 1 | `00:00:60` | Thrown exception |
| Boundary 1 | `a:b:c`    | Thrown exception |
| Boundary 1 | `00:00`    | Thrown exception |
| Boundary 1 | `00:1:00`  | 60               |
| Boundary 1 | `00:00:01` | 1                |
| Boundary 1 | `00:59:00` | 3540             |
| Boundary 1 | `00:00:59` | 59               |

### Unit Tests

We created three tests: one for valid inputs, one for missing components, and
one for invalid inputs.

- Valid inputs test:
  - We pass a string that should be considered valid and result in an output;
  - E.g. "00:59:01".
- Missing components test:
  - We pass a string that is missing one of that components;
  - We noticed that the parsing fails, so we consider it the expected behaviour;
  - The method could be adapted to accept this king of input as valid;
  - E.g. "00:00".
- Invalid inputs test:
  - Test inputs that are known to be invalid: invalid characters (e.g. letters),
    negative time, minutes/seconds out-of-range, etc...
  - The parsing of this should always result on a failure;
  - E.g. "00:60:00".

**Results**: All the test-cases pass successfully.

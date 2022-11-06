# Assignment 5 - G03P02

## Group information

- Ana Inês Oliveira de Barros - `up201806593@fe.up.pt`;
- João de Jesus Costa - `up201806560@fe.up.pt`

## Structural Testing

Structural testing is white-box testing technique that provides a systematic way to devise tests. The tests are based 
on different criteria. For this assignment, the criteria we will be focusing on
line and decision coverage.  

We used [JaCoCo Code Coverage Library for Java](https://www.jacoco.org/jacoco/) to collect
code coverage. Unit tests were written using the [JUnit framework](https://junit.org/junit5/).
We reached a line and branch coverage of at least 90% for all classes except for *GUI* related classes. 
## Previous assignments coverage 

For both assignment #2 and #3, we wrote a few unit tests using a black-box testing technique.
The two images below show how much coverage these unit tests generated: 21% for the _de.dominik.\_geyer.jtimesched.project_ package and 0% for the rest of the packages. 

![coverage_at_start](img/coverage_at_start.png)

![coverage_project_package](img/coverage_project_package.png)

As a result of this analysis, since there are no classes with both line and brach coverage at 90% or more, it was required to write unit tests for every class (except _GUI_ related ones).

## Unit tests

This section provides a brief description and the result of each unit test we developed in order to increase project's code coverage. 

### de.dominik_geyer.jtimesched.misc tests

#### PlainTextFormatter.java

1. **Format test**
    - **Description**: Given a log record (a message, severity and time), this test checks whether the output is formatted as intended. 
    - **Result**: Success.

### de.dominik_geyer.jtimesched.project tests

#### Project.java
#### ProjectException.java
#### ProjectSerializer.java

*Note*: For the implementation of the following tests, we added two new methods to the *Project* class: *equals(Object o)* and *hashCode()*. These two methods supported the comparison of projects necessary for tests regarding the project serializer. 

1. **Read/Write XML test**
    - **Description**: This parameterized test receives as input different projects: one default project, a checked project, a project with a set color, a project with notes and another one with a null title. Then, the tests are saved in an *XML* file, and recovered from the same file. The test succeeds if the projects are equal (read and write operations were correct). 
    - **Result**: Success.

2. **Write XML running project test**
    - **Description**: Creates a new project, sets it as running, and saves it to the XML file.
    Then, the project is read from the same XML file. The test succeeds if the project is the same and not running (running projects are saved as not running). 
    - **Result**: Success.

3. **Read XML running project test**
    - **Description**: Creates a new project, sets it as running, and saves it to the XML file. Then, the XML file is edited to set the project as running and we proceed to read the project from the same XML file. The test succeeds if the project is the same and running.
    - **Result**: Success.

4. **Read/Write XML project no quota test**
    - **Description**: Creates a new project, saves it to the XML file and edits the file in order to remove the line corresponding to the project's quota. Then, the project is read from the same XML file. The test succeeds if the project is the same.
    - **Result**: Success.

5. **Read/Write XML project no notes test**
    - **Description**: Creates a new project, saves it to the XML file and edits the file in order to remove the line corresponding to the project's notes. Then, the project is read from the same XML file. The test succeeds if the project is the same.
    - **Result**: Success.
    
#### ProjectTableModel.java
#### ProjectTime.java
### Code coverage achieved

The following image contains the final results of our work. We were able to successfully achieve ??% of line coverage and ??% of branch coverage. 

o irao gosta (muito) da nach

![coverage_without_gui](img/coverage_without_gui.png)

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
## Coverage at start

![coverage_at_start](img/coverage_at_start.png)

![coverage_project_package](img/coverage_project_package.png)

### Excluding GUI package

![coverage_without_gui](img/coverage_without_gui.png)

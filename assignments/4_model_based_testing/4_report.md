# Assignment 4 - G03P02

## Group information

- Ana Inês Oliveira de Barros - `up201806593@fe.up.pt`;
- João de Jesus Costa - `up201806560@fe.up.pt`

## Use-cases

We selected the following use-cases, because they were both common use-cases for
the application and the most interesting to test/draw a model for:

- Add a new project;
- Play/pause a project;
- Edit a project's color;

### Use-case 1 -- Add a new project

Adding a new project is fundamental for using the application.

#### State diagram

The most important aspects of this use-case are:

- When a user **adds** a project while **adding another one** i.e., still
  editing its name, the name change is confirmed and the user is prompted for
  the name of the new project;
- When a user minimizes the application to the tray area while setting a new
  project's name, the process is resumed upon maximizing the application window.

We decided to represent the application's end-state as the moment the
application is closed. This state and the transitions to it are not part of the
tests.

![Use-case 1's state machine](./state_machines/state_machine_1.png)

#### transition tree

![Use-case 1's transition tree](./transition_trees/transition_tree_1.png)

In contrast with the other branches of the tree, the first branch tests
something that doesn't appear related to the use-case: open the application,
minimize it, and then maximize it again. This indicates that the state
**_MinimizedToTray_** probably shouldn't exist as it is not related to the
use-case of _adding a new project_.

With 5 leaf nodes in the tree, we need to create 5 tests to cover all states and
transitions of the system.

#### Transition table

|                          | Add          | CancelNameChange | ConfirmNameChange | Maximize     | Minimize             |
| ------------------------ | ------------ | ---------------- | ----------------- | ------------ | -------------------- |
| **_AddProject_**         | _AddProject_ | _Dashboard_      | _Dashboard_       |              | _MinimizedToTrayAdd_ |
| **_Dashboard_**          | _AddProject_ |                  |                   |              | _MinimizedToTray_    |
| **_MinimizedToTray_**    |              |                  |                   | _Dashboard_  |                      |
| **_MinimizedToTrayAdd_** |              |                  |                   | _AddProject_ |                      |

According to the table, there are 12 sneak paths.

#### Tests

1. Start app ⇒ minimize app ⇒ maximize app
2. Start app ⇒ add project ⇒ change name and confirm it
3. Start app ⇒ add project ⇒ cancel name change
4. Start app ⇒ add project ⇒ add another project
5. Start app ⇒ add project ⇒ minimize app ⇒ maximize app

All tests pass successfully.

Note: As stated previously, the first test probably should be part of this
use-case.

### Use-case 2 - Play/pause a project

This is the main function of the application.

The **state diagram**:

![Use-case 2's state machine](./state_machines/state_machine_2.png)

The **transition tree**:

![Use-case 2's transtion tree](./transition_trees/transition_tree_2.png)

The **transition table**:

|                 | StartProject<br />[nº projects > 0] | Minimize        | Maximize<br />[current project is not playing] | Maximize<br />[current project is playing] | ToggleCurrentProject<br />[selected a current project] | PauseProject<br />[selected project == current project] | DeleteProject<br />[deleted project == current project] | StartProject<br />[selected project != current project] | DeleteProject<br />[deleted project == current project] |
| :-------------- | :---------------------------------- | :-------------- | ---------------------------------------------- | ------------------------------------------ | ------------------------------------------------------ | ------------------------------------------------------- | ------------------------------------------------------- | ------------------------------------------------------- | ------------------------------------------------------- |
| Dashboard       | PlayingProject                      | MinimizedToTray |                                                |                                            |                                                        |                                                         |                                                         |                                                         |                                                         |
| MinimizedToTray |                                     |                 | Dashboard                                      | PlayingProject                             | MinimizedToTray                                        |                                                         |                                                         |                                                         |                                                         |
| PlayingProject  |                                     | MinimizedToTray |                                                |                                            |                                                        | Dashboard                                               | Dashboard                                               | Playing Project                                         | Playing Project                                         |

### Use-case 3 - Edit a project's color

Color coding tasks is a common organization method for time
scheduling/organization methods. A time scheduling application should have this
feature.

![Use-case 3's state machine](./state_machines/state_machine_3.png)

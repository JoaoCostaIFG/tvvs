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

### Use-case 1 - add a new project

Adding a new project is fundamental for using the application.

The **state diagram**:

![Use-case 1's state machine](./state_machines/state_machine_1.png)

The **transition tree**:

```plantuml
@startuml
skinparam linetype polyline

[Dashboard 0] -d-> [MinimizedToTray 0] : Minimize
[Dashboard 0] -d-> [AddProject 0] : Add
[Dashboard 0] -d-> [EndState 0] : Close

[MinimizedToTray 0] -d-> [Dashboard 1] : Maximize
[MinimizedToTray 0] -d-> [EndState 1] : Close

[AddProject 0] -d-> [Dashboard 2] : ConfirmNameChange
[AddProject 0] -d-> [Dashboard 3] : CancelNameChange
[AddProject 0] -d-> [AddProject 1] : Add
[AddProject 0] -d-> [MinimizedToTrayAdd 0] : Minimize
[AddProject 0] -d-> [EndState 2] : Close

[MinimizedToTrayAdd 0] -d-> [AddProject 2] : Maximize
[MinimizedToTrayAdd 0] -d-> [EndState 3] : Close

@enduml
```

The **transition table**:

|                        | Add        | CancelNameChange | Close | ConfirmNameChange | Maximize | Minimize |
|------------------------|------------|------------------|-------|-------------------|----------|----------|
| **AddProject**         | AddProject | Dashboard        |       |                   |          |          |
| **Dashboard**          |            |                  |       |                   |          |          |
| **MinimizedToTray**    |            |                  |       |                   |          |          |
| **MinimizedToTrayAdd** |            |                  |       |                   |          |          |


### Use-case 2 - play/pause a project

This is the main function of the application.

![Use-case 2's state machine](./state_machines/state_machine_2.png)

### Use-case 3 - edit a project's color

Color coding tasks is a common organization method for time
scheduling/organization methods. A time scheduling application should have this
feature.

![Use-case 3's state machine](./state_machines/state_machine_3.png)

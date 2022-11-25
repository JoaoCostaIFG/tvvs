# Assignment 8 - G03P02

## Group information

- Ana Inês Oliveira de Barros - `up201806593@fe.up.pt`;
- João de Jesus Costa - `up201806560@fe.up.pt`

## Dataflow Testing


## Dataflow Test #1

**Function**: `public int getSecondsToday()` in `Project.Java`

This function was selected because...

This function purpose is..

### Dataflow Graph

![dataflow_graph_1](dataflow_graphs/dataflow_graph_1.png)

### Unit Test

Brief description of the test...
Brief description of the outcome and whether it fails...

## Dataflow Test #2

**Function**: `public int adjustSecondsToday()` in `Project.Java`

### Dataflow Graph

![dataflow_graph_2](dataflow_graphs/dataflow_graph_2.png)

### Def-use pairs

#### Variable secondsToday

| **Pair ID** | **Def** | **Use** |           **Path**          |
|:-----------:|:-------:|:-------:|:---------------------------:|
|      1      |  start  |   194   |         <start,194>         |
|      2      |  start  |   195   |       <start,194,195>       |
|      3      |  start  |   197   |     <start,194,195,197>     |
|      4      |  start  |   197   |       <start,194,197>       |
|      5      |  start  |   200   | <start,194,195,197,199,200> |
|      6      |  start  |   200   |   <start,194,197,199,200>   |

#### Variable this.secondsToday

| **Pair ID** | **Def** | **Use** |   **Path**  |
|:-----------:|:-------:|:-------:|:-----------:|
|      1      |  start  |   197   | <start,197> |

#### Variable secondsDelta

| **Pair ID** | **Def** | **Use** |   **Path**  |
|:-----------:|:-------:|:-------:|:-----------:|
|      1      |   197   |   199   | <start,199> |

### Unit Test

## Dataflow Test #3

**Function**: `public static int parseSeconds(String strTime)` in `ProjectTime.Java`

### Dataflow Graph

![dataflow_graph_3](dataflow_graphs/dataflow_graph_3.png)

### Def-use pairs

### Unit Test
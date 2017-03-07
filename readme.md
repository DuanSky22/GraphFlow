[TOC]



# Graph Streaming Computing on Flink



## Flink Gelly Internals

In this section, we will focus on how the Graph Algorithm of Gelly runs well. As we know, Gelly is build on DataSet API, the core of Gelly is the BSP Model, so first of all, we should know how Gelly realize the BSP Model. Here we list four key chapters:

[chapter1-1: DataSet Iteration]: /doc/flink-gelly-internals/DataSet_Iterations.md
[chapter1-2: DataSet Iteration Implements]: /doc/flink-gelly-internals/DataSet_Iteration_Implements.md
[chapter1-3: BSP Model]: /doc/flink-gelly-internals/BSP_Model.md
[chapter1-4: Algorithms]: /doc/flink-gelly-internals/Algorithms.md

## Streaming Internals

In this section, we will focus on the key points of Flink Streaming which can realize the streaming graph computing. 

[chapter2-1: State on Streaming]: /doc/flink-streaming-internals/State.md
[chapter2-2: Iterative Streaming]: /doc/flink-streaming-internals/IterativeStreaming.md

## Streaming Graph Computing Model

In this section, we will explain our streaming graph computing model based on Flink.

[chapter3-1: Model Design]: /doc/flink-graph-streaming/Model_Design.md
[chapter3-2: Model Implements]: /doc/flink-graph-streaming/Model_Implement.md


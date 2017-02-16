# HzGraphFlow
Streaming graph processing based on Hazelcast(https://hazelcast.org/).

As we know, Flink is also a Map-Reduce Compute Framework, its Graph Processing namely Gelly is based on batch processing, while doesn't meet the streaming processing on Graph Computing. We have build a project GraphFlow which designed and implemented several Graph Algorithms: DD(Degree Distribution), TC(Triangle Count), CC(Connected Components) on Flink Streaming API. Forther more, we designed ourselves model--Dynamic Graph Computing Model Based on State Updating. Unfortunately, the more we study and try, the more we have to admit that more complex algorithm like SSSP(Single Source Shortest Path), PR(PageRank) is hart to be implemented without distributed data structure. So I want to try implement this model on Hazelcast first, and watch how it works, then I will rebuild this work on Flink, maybe combine the flink with the Hazelcast is also a good idea.

By now, I have finished this.
1. design and implement the Dynamic Graph Computing Model Based on State Updating on Hazelcast.
2. design and implement the DD algorithm.
3. design and implement the TC algorithm.
4. design but not implement SSSP algorithm.(This algorithm is very interesting, I will explain the details latter)

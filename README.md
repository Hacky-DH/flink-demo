# Flink Demo

## Flink Project

A Flink application project using Scala and SBT.

### Job with Clickhouse JDBC Driver

### Run the job

You can generate a fat jar with `sbt clean assembly`, then submit it with
```
flink run target\scala-2.11\flink-project-assembly-0.1-SNAPSHOT.jar
```

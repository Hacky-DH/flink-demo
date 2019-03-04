# Flink Demo

## Flink Project

A Flink application project using Scala and SBT.

### Job with Clickhouse JDBC Driver

### Run the job

You can generate a fat jar with `sbt clean assembly`, then submit it with
```
flink run target\scala-2.11\flink-project-assembly-0.1-SNAPSHOT.jar [clean]
```
clean arg means cleaning up the all databases after job.

To run this job requires clickhouse-server listen on localhost:8123

You can start a clickhouse-server with
```
docker pull yandex/clickhouse-server
docker run -d --name someName --ulimit nofile=262144:262144 -p 9000:9000 -p 8123:8123 yandex/clickhouse-server
```

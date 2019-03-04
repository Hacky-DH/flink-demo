package org.dhacky

/**
  * MIT License
  *
  * Copyright (c) 2019 DHacky
  */

import org.apache.flink.api.java.io.jdbc.{JDBCInputFormat, JDBCOutputFormat}
import org.apache.flink.api.scala._
import org.apache.flink.types.Row

/**
  * Skeleton for a Flink Job.
  *
  * For a full example of a Flink Job, see the WordCountJob.scala file in the
  * same package/directory or have a look at the website.
  *
  * You can also generate a .jar file that you can submit on your Flink
  * cluster. Just type
  * {{{
  *   sbt clean assembly
  * }}}
  * in the projects root directory. You will find the jar in target/scala-2.11/
  *
  */
object Job {
  def main(args: Array[String]): Unit = {
    var cleanData = args.length > 0 && args(0).equalsIgnoreCase("clean")
    // set up the execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment
    JdbcUtil.prepareDatabase()
    val input = JDBCInputFormat.buildJDBCInputFormat
      .setDrivername(JdbcUtil.DRIVER_CLASS)
      .setDBUrl(JdbcUtil.DB_URL)
      .setUsername(JdbcUtil.DB_USER)
      .setPassword(JdbcUtil.DB_PASSWORD)
      .setQuery(JdbcUtil.getSelectAll(JdbcUtil.INPUT_TABLE))
      .setRowTypeInfo(JdbcUtil.ROW_TYPE_INFO)
      .finish
    val source = env.createInput(input).map((value: Row) => {
      value.setField(0, Integer.valueOf(value.getField(0).toString) * 2)
      value
    })
    source.output(JDBCOutputFormat.buildJDBCOutputFormat
      .setDrivername(JdbcUtil.DRIVER_CLASS)
      .setDBUrl(JdbcUtil.DB_URL)
      .setUsername(JdbcUtil.DB_USER)
      .setPassword(JdbcUtil.DB_PASSWORD)
      .setQuery(JdbcUtil.getInsertTemplate)
      .finish)

    // execute program
    env.execute("Flink Batch Scala API Access ClickHouse")
    JdbcUtil.checkOutputDB()
    if (cleanData) JdbcUtil.cleanUpDatabase()
  }
}

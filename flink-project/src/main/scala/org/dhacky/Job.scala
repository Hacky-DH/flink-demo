package org.dhacky

/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

import org.apache.flink.api.common.functions.MapFunction
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
    JdbcUtil.cleanUpDatabase()
    JdbcUtil.prepareDatabase()
    val input = JDBCInputFormat.buildJDBCInputFormat.setDrivername(JdbcUtil.DRIVER_CLASS)
      .setDBUrl(JdbcUtil.DB_URL).setQuery(JdbcUtil.getSelectAll(JdbcUtil.INPUT_TABLE))
      .setRowTypeInfo(JdbcUtil.ROW_TYPE_INFO).finish
    val source = env.createInput(input).map((value: Row) => {
      value.setField(0, Integer.valueOf(value.getField(0).toString) * 2)
      value
    })
    source.output(JDBCOutputFormat.buildJDBCOutputFormat.setDrivername(JdbcUtil.DRIVER_CLASS)
      .setDBUrl(JdbcUtil.DB_URL).setQuery(JdbcUtil.getInsertTemplate).finish)

    // execute program
    env.execute("Flink Batch Scala API Access ClickHouse")
    JdbcUtil.checkOutputDB()
    if (cleanData) JdbcUtil.cleanUpDatabase()
  }
}

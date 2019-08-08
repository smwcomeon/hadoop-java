package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

//sparksql的程序编写
object SparkSql extends App {
  val sparkconf = new SparkConf().setAppName("sparksql").setMaster("local[*]")

  val spark= SparkSession.builder().config(sparkconf)
//    .enableHiveSupport()
    .getOrCreate()

  val sc=spark.sparkContext

  //文件读取
  val data = spark.read.json("")

  import spark.implicits._  //需导入隐世转换

  data.filter($"salary">3000).show()

  data.createOrReplaceTempView("employee")
  spark.sql("select * from employee").show()

  spark.close()
}

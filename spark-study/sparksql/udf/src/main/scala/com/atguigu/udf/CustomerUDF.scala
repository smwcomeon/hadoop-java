package com.atguigu.udf

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object CustomerUDF extends  App {
  val sparkConf= new SparkConf().setAppName("udf").setMaster("local[*]")

  val spark= SparkSession.builder().config(sparkConf).getOrCreate()

  val data=spark.read.json("D:\\idea-hadoopworkspace\\spark-study\\sparksql\\doc\\employee.json")

  spark.udf.register("change",(x:String)=> "Name:"+x)

  data.createOrReplaceTempView("emp")

  spark.sql("select change(name) from emp").show()

  spark.close()
}

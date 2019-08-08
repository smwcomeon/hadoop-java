package com.atguigu.wordcount

import org.apache.spark.{SparkConf, SparkContext}

object Application extends App {

  //声明一个spark conf对象，用于配置Spark连接
  val sparkConf = new SparkConf().setAppName("wordcount").setMaster("spark://master01:7077")
    .setJars(List("C:\\Users\\Administrator\\Desktop\\spark\\sparkcore\\wordcount\\target\\wordcount-jar-with-dependencies.jar"))
    .setIfMissing("spark.driver.host", "192.168.56.1")

  //创建一个SparkContext用于连接Spark集群
  val sparkContext = new SparkContext(sparkConf)

  //加载需要处理的数据文件
  val textfile = sparkContext.textFile("hdfs://master01:9000/README.txt")

  val result = textfile.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

  //输出数据文件的结果
  result.saveAsTextFile("hdfs://master01:9000/abc")

  //关闭Spark集群的连接
  sparkContext.stop()

}

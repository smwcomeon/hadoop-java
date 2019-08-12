package com.atguigu.wordcount

import org.apache.spark.{SparkConf, SparkContext}

object Application extends  App {

  //第一种运行方式 声明一个spark.conf 对象 ，用于配置Spark连接
  //setMaster("local[*]") 本地模式
  val sparkConf=new SparkConf().setAppName("wordcount").setMaster("local[*]")
  //第二种运行方式 ：打包方式运行
//  val sparkConf=new SparkConf().setAppName("wordcount")

  //第三种运行方式 本地远程调试 直接操作集群
//  val sparkConf=new SparkConf().setMaster("spark://hadoop110:7077").setAppName("wordcount")
//    .setJars(List("D:\\idea-hadoopworkspace\\spark\\sparkcore\\wordcount\\target\\wordcount-jar-with-dependencies.jar"))
//    .setIfMissing("spark.driver.host","172.18.1.110")

  //2.创建一个SparkContext用于连接Spark集群
  val sparkcontext=new SparkContext(sparkConf)

  //3.加载需要处理的数据文件
  val textFile=sparkcontext.textFile("hdfs://hadoop110:8020/README.txt")

  //4.输出数据文件的结果
  val result=textFile.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
  result.saveAsTextFile("hdfs://hadoop110:8020/wordcount/result1")

  //5.关闭Spark集群的连接
  sparkcontext.stop()

}

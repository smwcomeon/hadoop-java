package com.atguigu.stream

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Streaming extends App {

  val sparkConf= new SparkConf().setAppName("Streaming").setMaster("local[*]")
//      .set("spark.streaming.stopGracefullyOnShutdown","true")

  //创建Streaming流
  val ssc = new StreamingContext(sparkConf,Seconds(2))

  //我们可以创建一个DStream来表示来自TCP源的流数据，指定为主机名（例如localhost）和端口（例如9999）
  val line= ssc.socketTextStream("hadoop110",9999)

  // Split each line into words
  val words = line.flatMap(_.split(" "))

  val wordtuple = words.map((_,1))

  val result= wordtuple.reduceByKey(_+_)

  result.print()

  //开始
  ssc.start()

  //等待结束
  ssc.awaitTermination()

}

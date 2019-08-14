package com.atguigu

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamKafka extends  App {

  val sparkConf = new SparkConf().setAppName("StreamKafka").setMaster("local[*]")

  val ssc = new StreamingContext(sparkConf,Seconds(2))

  //连接kafka
  val topic="log"
  val kafkaParams=Map(
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG ->"172.18.1.110:9092, 172.18.1.111:9092, 172.18.1.112:9092",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG ->classOf[StringDeserializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG ->classOf[StringDeserializer],
    ConsumerConfig.GROUP_ID_CONFIG->"kafka",
    //smallest ,表示从最小的offest消费
    // latest 表示从当前topic最大消费的offset
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG->"latest",
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG->(true:java.lang.Boolean)
  )

  //创建DStream，返回接收到的输入数据
  // ConsumerStrategies.Subscribe有一个重载方法，能够让你传入offsets: collection.Map[TopicPartition, Long] 来设置偏移量，该偏移量应该是从ZK中获取
  val lines = KafkaUtils.createDirectStream[String, String](ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](Iterable(topic), kafkaParams))

  //执行逻辑
  lines.map(line=>"**********:"+line.value()).foreachRDD(rdd=>{
    // spark core操作
    rdd.foreachPartition(items => {

      //获取kafka连接池
      val pool = KafkaProxyPool("172.18.1.110:9092, 172.18.1.111:9092, 172.18.1.112:9092","log2")
      //获取了kafkaProxy对象
      val kafkaProxy = pool.borrowObject()
      //写入消息
      for (item <- items){
        println(item)
        kafkaProxy.send("kafka",item)
      }
      
      //归还对象
      pool.returnObject(kafkaProxy)

    })
  })

  //启动程序
  ssc.start()

  ssc.awaitTermination()
}


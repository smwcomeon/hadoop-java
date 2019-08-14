package com.atguigu.test

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingKafkaTest  extends  App {
  //sparkConf
  val sprkConf=new SparkConf().setAppName("kafkaTest").setMaster("local[*]")
  //创建StreamingContext
  val ssc = new StreamingContext(sprkConf,Seconds(2))

  val topic="log"
  val kafkaParams=Map(
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG->"hadoop110:9092,hadoop111:9092,hadoop112:9082",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG->classOf[StringDeserializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG->classOf[StringDeserializer],
    ConsumerConfig.GROUP_ID_CONFIG->"kafka",

    //smallest、latest, 比如已经有CG在消费TOPIC，如果有新的CG加入，
    // 那么 smallest表示从最小的offset开始消费，
    // latest是从当前TOPIC最大消费的Offset来消费
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG->"latest",
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG->(true:java.lang.Boolean)
  )

  // ConsumerStrategies.Subscribe有一个重载方法，
  // 能够让你传入offsets: collection.Map[TopicPartition, Long] 来设置偏移量，
  // 该偏移量应该是从ZK中获取
  val lines =KafkaUtils.createDirectStream[String,String](ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String,String](Iterable(topic),kafkaParams))

  lines.map(line=>"*****:"+line.value()).foreachRDD(rdd=>{
    rdd.foreachPartition(items=>{
      val pool=KafkaProxyPoolTest("hadoop110:9092,hadoop111:9092,hadoop112:9082","log2")
      val kafkaProxy= pool.borrowObject()
      //写入消息
      for (item<-items){
        println(item)
        kafkaProxy.send("kafka",item)
      }

      pool.returnObject(kafkaProxy)

    })

    // 保存Kafka消费的偏移量 Offset
  })

  // 启动程序
  ssc.start()
  ssc.awaitTermination()
}

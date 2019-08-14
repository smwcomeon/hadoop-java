package com.atguigu.test

import java.util.Properties

import org.apache.commons.pool2.impl.{DefaultPooledObject, GenericObjectPool}
import org.apache.commons.pool2.{BasePooledObjectFactory, PooledObject}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

//创建一个kafkaProducer的代理类
class KafkaProxy(brokenList:String,topic:String){

  private val producer= {
    //创建一个配置属性
    val property = new Properties()
    property.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokenList)
    property.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    property.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    //创建一个kafka连接，
    new KafkaProducer[String, String](property)
  }

  //封装一个send消息的方法
  def  send(key:String,value:String):Unit={
    producer.send(new ProducerRecord[String,String](topic,key,value))
  }
}

//创建一个kafkaPool池工厂
class KafkaProxyFactory(brokenList:String,topic:String) extends BasePooledObjectFactory[KafkaProxy]{
  override def create(): KafkaProxy = new KafkaProxy(brokenList,topic)

  override def wrap(t: KafkaProxy): PooledObject[KafkaProxy] = new DefaultPooledObject[KafkaProxy](t)
}


object KafkaProxyPoolTest {
  //导pool2的包
  private var pool:GenericObjectPool[KafkaProxy]=null

  def apply(brokenList:String,topic:String):GenericObjectPool[KafkaProxy]={
    KafkaProxyPoolTest.synchronized{
      if (null == pool){
        val poolFactory=new KafkaProxyFactory(brokenList,topic)
        pool = new GenericObjectPool[KafkaProxy](poolFactory)
      }
    }

    pool
  }

}

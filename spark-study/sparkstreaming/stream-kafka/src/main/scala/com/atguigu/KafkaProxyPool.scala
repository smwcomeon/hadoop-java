package com.atguigu

import java.util.Properties

import org.apache.commons.pool2.impl.{DefaultPooledObject, GenericObjectPool}
import org.apache.commons.pool2.{BasePooledObjectFactory, PooledObject}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}


//创建一个开发 producer客户端
//用于通过池话得方式获取kafka的对象，然后利用对象将数据写入到kafka中
class  KafkaProxy(brokenList:String,topic:String){

  private  val producer ={

    //创建一个配置属性
    val property=new Properties()
    property.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,brokenList)
    property.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")
    property.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")

    //创建一个kafka的连接，第一个是String是key的类型，第二个是vlaue
    new KafkaProducer[String,String](property)
  }

  def send(key:String,value:String):Unit={
    producer.send(new ProducerRecord[String,String](topic,key,value))
  }
}

//这个类是创建一个池话对象的工厂类
class  KafkaProxyFactory(brokenList:String,topic:String) extends BasePooledObjectFactory[KafkaProxy]{
  //创建实体
  override def create(): KafkaProxy = new KafkaProxy(brokenList,topic)

  //包装实体
  override def wrap(t: KafkaProxy): PooledObject[KafkaProxy] = new DefaultPooledObject[KafkaProxy](t)
}

/**
  * 如果需要获取KafkaProxy通过pool.borrowObject()来获取
  *     中间为业务逻辑
  * 如果使用完，归还使用pool.returnObject()
  *     pool.borrowObject()
  *     pool.returnObject()
  */
object KafkaProxyPool {
  //具体的池对象
  private var pool: GenericObjectPool[KafkaProxy] = null

  def apply(brokenList:String,topic: String):GenericObjectPool[KafkaProxy]={
    KafkaProxyPool.synchronized{
      if (null== pool){
        val poolFactory=new KafkaProxyFactory(brokenList,topic)
        pool=new GenericObjectPool[KafkaProxy](poolFactory)
      }
    }
    pool

  }
}

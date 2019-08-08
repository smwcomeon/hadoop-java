package com.atguigu.udaf

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}
import org.apache.spark.sql.expressions.Aggregator


case class Employee(name:String, salary:Long)
case  class Average(var sum: Long,var count:Long)

object NewUDAF extends Aggregator[Employee,Average,Double]{

  //初始化
  override def zero: Average = Average(0L,0L)

  //是update函数，每一个分区的聚合 聚合相同executor分片中的结果
  override def reduce(b: Average, a: Employee): Average = {
    b.sum=b.sum+a.salary
    b.count+=1
    b //给一个返回值
  }

  //聚合不同的execute的结果
  override def merge(b1: Average, b2: Average): Average = {
    b1.sum+=b2.sum
    b1.count+=b2.count
    b1
  }

  //计算输出
  override def finish(reduction: Average): Double = {
    reduction.sum.toDouble / reduction.count
  }

  //设置中间值类型的编码器 要转换成case类
  override def bufferEncoder: Encoder[Average] =Encoders.product

  //设定最终输出值的编码器
  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble

  def main(args: Array[String]): Unit = {
    val sparkConf= new SparkConf().setAppName("udf").setMaster("local[*]")

    val spark= SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._
    val data=spark.read.json("D:\\idea-hadoopworkspace\\spark-study\\sparksql\\doc\\employee.json").as[Employee]

    val avg=NewUDAF.toColumn.name("avg")

    data.select(avg).show()

    spark.close()
  }
}

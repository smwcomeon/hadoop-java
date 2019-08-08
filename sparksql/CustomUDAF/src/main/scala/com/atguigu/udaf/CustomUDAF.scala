package com.atguigu.udaf

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructField, StructType}
// 用户自定义聚合函数
object CustomUDAF extends UserDefinedAggregateFunction{

  def main(args: Array[String]): Unit = {
    val sparkConf= new SparkConf().setAppName("udf").setMaster("local[*]")

    val spark= SparkSession.builder().config(sparkConf).getOrCreate()

    val data=spark.read.json("D:\\idea-hadoopworkspace\\spark-study\\sparksql\\doc\\employee.json")

    spark.udf.register("avgs",CustomUDAF)

    data.createOrReplaceTempView("emp")

    spark.sql("select avgs(salary) from emp").show()

    spark.close()
  }


  //输入数据类型
  override def inputSchema: StructType = StructType(StructField("salary",LongType) :: Nil)

  //缓冲区的类型  总工资 个数
  override def bufferSchema: StructType = StructType(StructField("sum",LongType) ::StructField("count",LongType):: Nil)

  //最终的返回的数据类型
  override def dataType: DataType = DoubleType

  //需要声明是否根据输入数据类型有相同的输出数据类型
  override def deterministic: Boolean = true

  //缓冲区初始化操作  和传入的 //缓冲区的类型  总工资 个数相对应
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0)=0L
    buffer(1)=0L
  }

  //在每一个分区中更新buffer
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0)=buffer.getLong(0)+input.getLong(0) //工资总数
    buffer(1)=buffer.getLong(1)+1  //人数

  }

  //合并不同分区的结果
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0)=buffer1.getLong(0)+buffer2.getLong(0)
    buffer1(1)=buffer1.getLong(1)+buffer2.getLong(1)
  }


  //计算最终结果
  override def evaluate(buffer: Row): Any = buffer.getLong(0).toDouble/buffer.getLong(1)
}

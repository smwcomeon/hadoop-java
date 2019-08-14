package com.atguigu.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

case class tbStock(ordernumber:String,locationid:String,dateid:String)
  extends Serializable
case class tbStockDetail(ordernumber:String, rownum:Int, itemid:String,
                         number:Int, price:Double, amount:Double)
  extends Serializable
case class tbDate(dateid:String, years:Int, theyear:Int, month:Int,
                  day:Int, weekday:Int, week:Int, quarter:Int, period:Int,
                  halfmonth:Int) extends Serializable

object Practice {
  //定义一个把数据插入到Hive
  private def inserttoHive(spark:SparkSession,table:String,data:DataFrame):Unit={
    spark.sql("drop table if exists "+table)
    data.write.saveAsTable(table)
  }

  private  def insertMysql(table:String,data:DataFrame)={
    data.write
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/spark")
      .option("dbtable", table)
      .option("user", "root")
      .option("password", "123456")
      .save()

  }

  def main(args: Array[String]): Unit = {

    val sparkConf=new SparkConf().setAppName("Practice").setMaster("local[*]")
      .set("spark.sql.warehouse.dir","hdfs://hadoop100:8020/spark_wraehouse")

    val spark =SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._

    //加载TBStock数据
    val tbStockRDD=spark.sparkContext.textFile("D:\\idea-hadoopworkspace\\spark-study\\sparksql\\practice\\src\\main\\resources\\tbStock.txt")
    //将rdd转成DataSet
    val tbStockDS = tbStockRDD.map(_.split(",")).map(attr=>tbStock(attr(0),attr(1),attr(2))).toDS

    inserttoHive(spark,"tbStock",tbStockDS.toDF)

   //加载tbStockDetail数据
    val tbStockDetailRdd = spark.sparkContext.textFile("D:\\idea-hadoopworkspace\\spark-study\\sparksql\\practice\\src\\main\\resources\\tbStockDetail.txt")
    val tbStockDetailDS = tbStockDetailRdd.map(_.split(",")).
      map(attr=> tbStockDetail(attr(0),attr(1).trim().toInt,attr(2),attr(3).trim().toInt,attr(4).trim().toDouble, attr(5).trim().toDouble)).toDS

    inserttoHive(spark,"tbStockDetail",tbStockDetailDS.toDF)

    //加载tbDate数据
    val tbDateRdd  = spark.sparkContext.textFile("D:\\idea-hadoopworkspace\\spark-study\\sparksql\\practice\\src\\main\\resources\\tbDate.txt")
    val tbDateDS = tbDateRdd.map(_.split(",")).
      map(attr=> tbDate(attr(0),attr(1).trim().toInt, attr(2).trim().toInt,attr(3).trim().toInt, attr(4).trim().toInt, attr(5).trim().toInt, attr(6).trim().toInt, attr(7).trim().toInt, attr(8).trim().toInt, attr(9).trim().toInt)).toDS

    inserttoHive(spark,"tbDate",tbDateDS.toDF)

    //需求一  插入到hive 统计所有订单中每年的销售单数、销售总额
    val result1=spark.sql("SELECT c.theyear, COUNT(DISTINCT a.ordernumber), SUM(b.amount) FROM tbStock a JOIN tbStockDetail b ON a.ordernumber = b.ordernumber JOIN tbDate c ON a.dateid = c.dateid GROUP BY c.theyear ORDER BY c.theyear")
    insertMysql("xq1",result1)


    //需求二  1、	统计每年，每个订单一共有多少销售额
    val result2=spark.sql("SELECT a.dateid, a.ordernumber, SUM(b.amount) AS SumOfAmount FROM tbStock a JOIN tbStockDetail b ON a.ordernumber = b.ordernumber GROUP BY a.dateid, a.ordernumber")
    insertMysql("xq2",result2)

    //需求三
    val result3=spark.sql("SELECT DISTINCT e.theyear, e.itemid, f.maxofamount FROM (SELECT c.theyear, b.itemid, SUM(b.amount) AS sumofamount FROM tbStock a JOIN tbStockDetail b ON a.ordernumber = b.ordernumber JOIN tbDate c ON a.dateid = c.dateid GROUP BY c.theyear, b.itemid ) e JOIN (SELECT d.theyear, MAX(d.sumofamount) AS maxofamount FROM (SELECT c.theyear, b.itemid, SUM(b.amount) AS sumofamount FROM tbStock a JOIN tbStockDetail b ON a.ordernumber = b.ordernumber JOIN tbDate c ON a.dateid = c.dateid GROUP BY c.theyear, b.itemid ) d GROUP BY d.theyear ) f ON e.theyear = f.theyear AND e.sumofamount = f.maxofamount ORDER BY e.theyear")
    insertMysql("xq3",result3)


    spark.close()

  }
}

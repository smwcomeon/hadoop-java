package com.atguigu

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

import scala.util.matching.Regex

object CdnStatic {
  val  logger=LoggerFactory.getLogger(CdnStatic.getClass)

  //还原ip地址
  val  IPPattern="((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))".r

  //匹配视频文件名
  val  videoPattern="([0-9]+).mp4".r

  //[15/Feb/2017:11:17:13 +0800]  匹配 2017:11 按每小时播放量统计
  val  timePattern=".*(2017):([0-9]{2}):[0-9]{2}:[0-9]{2}.*".r

  //匹配 http 响应码和请求数据大小
  val httpSizePattern=".*\\s(200|206|304)\\s([0-9]+)\\s.*".r


  def main(args: Array[String]): Unit = {

    val sparkConf=new SparkConf().setAppName("wordcount").setMaster("local[*]")

    val sc=new SparkContext(sparkConf)

    val input=sc.textFile("D:\\idea-hadoopworkspace\\spark-study\\sparkcore\\sparkcore-cdn\\src\\main\\resources\\cdn.txt")

    //统计独立访问IP访问量前10位
    ipStatics(input)

    //统计每个权重独立iP数
    videoInputStatics(input)

    //统计一天中每个小时的数量
    flowOfHour(input)

    sc.stop()

  }
  //统计一天中每个小时的流量
  def flowOfHour(value: RDD[String])={
      def isMatch(pattern: Regex,str:String)={
        str match {
          case pattern(_*) => true
          case _ =>false
        }
      }

    //获取日志中小时和http请求体大小
    def getTimeSize(line:String) ={
      var res=("",0L)
      try {
        val httpSizePattern(code, size) = line
        val timePattern(year, hour) = line
        res = (hour, size.toLong)
      } catch {
        case ex:Exception  =>ex.printStackTrace()
      }
      res
    }

    //3.统计一天中每个小时的流量
    value.filter(x=>isMatch(httpSizePattern,x)).filter(x=>isMatch(timePattern,x))
        .map(x=>getTimeSize(x)).groupByKey().map(x=>(x._1,x._2.sum)).sortByKey()
        .foreach(x=>println(x._1+"时CDN流量"+x._2/(1024*1024*1024)+"G"))
  }

  //一 统计独立ip访问量前10位
  def ipStatics(value: RDD[String])={
    val ipNums=value.map(x => (IPPattern.findFirstIn(x) get,1)).reduceByKey(_+_).sortBy(_._2,false)

    //输出IP访问前10位
    ipNums.take(10).foreach(println)
    ipNums.saveAsTextFile("ipNums.txt")
    println("独立IP数"+ipNums.count())
  }

  //二 统计每个视频独立iP数
  def videoInputStatics(value: RDD[String])={
     def getFileNameAndIP(line:String)={
       (videoPattern.findFirstIn(line).mkString,IPPattern.findFirstIn(line).mkString)
     }

    //2.统计每个视频独立IP数
    value.filter(x=>x.matches(".*([0-9]+)\\.mp4.*")).
        map(x=>getFileNameAndIP(x)).groupByKey().map(x=>(x._1,x._2.toList.distinct)).
        sortBy(_._2.size,false).take(10)
        .foreach(x=>println("视频:"+x._1+"独立IP数"+x._2.size))
  }
}

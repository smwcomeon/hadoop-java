package day01.bundle

object CollectStudy {
  def main(args: Array[String]): Unit = {

    val map1=Map("Alice"->20,"Bob"->30)
    //age统一加一
    //偏函数
    map1.map{case (_,age)=> println(age+1)}
    map1.collect{case (_,age)=> println(age+1)}
    println(map1)

    //collect 可以配置只符合case的元素
    List(1,2,3,"hh",4).collect{case i:Int =>println(i+1)}

  }
}

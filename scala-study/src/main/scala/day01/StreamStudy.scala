package day01

object StreamStudy {

  def main(args:Array[String]):Unit={

    //Stream
    def numsForm(n:Int):Stream[Int]={
      n #:: numsForm(n+1)
    }

    val stream01=numsForm(10)
    println(stream01)
    println(stream01.tail)
    println(stream01)

    val stream02=stream01.tail.tail
    println(numsForm(5).map(x=>x*x).tail)

    val view1=(1 to 100000).view.map(x=>x).filter(y=>y.toString == y.toString.reverse)
    println(view1)
    println(view1.mkString(" "))


    (1 to 5).foreach(println(_))
    println()
    (1 to 5).par.foreach(println(_))

    val result1 = (0 to 10000).map{case _ => Thread.currentThread.getName}.distinct
    val result2 = (0 to 10000).par.map{case _ => Thread.currentThread.getName}.distinct
    println(result1)
    println(result2)

    val `val`= 1


  }


}

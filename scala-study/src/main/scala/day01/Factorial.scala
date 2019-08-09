package day01

object Factorial {
  def main(args: Array[String]): Unit = {


    def factorial01(a:Int):Int={
      if (a==0) 1
      else a *factorial01(a-1)
    }

    println(factorial01(4))

    for (x <- 1 to 5;y <- 1 to 5){
      print("*")
      if (y==5){
        println()
      }
    }

    def divider(x:Double,y:Double) ={
      if(y==0){
        throw  new Exception("0 不能作为除数")
      }
      x/y
    }

    try {
      println(divider(10, 5))
    } catch {
      case e1:Exception  => println("0 作为除数的异常成功捕获"+e1.printStackTrace())
    }



  }
}

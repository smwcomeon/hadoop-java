package day01

object HighFunctionStudy {
  def main(args: Array[String]): Unit = {
    //高阶函数 把函数当做参数传入另外一个函数中做为参数
    def plus(x:Int):Int={
      x+3
    }
    val  result= Array(1,2,3,4).map(plus(_))
    println(result.mkString(" "))

    //匿名函数
    val triple=(x:Double)=>x*3


    def highFunction(x:Int,y:Int,f:(Int,Double)=>Double)=f(x,y)

    def subF(a:Int,b:Double):Double={
      a*b
    }

    println(highFunction(10,20,subF))


    def highOrderFunction1(f: Double => Double) = f(10)
    def minus7(x: Double) = x - 7
    val result2 = highOrderFunction1(minus7)
    println(result2)


    def minusxy1(x: Int) = (y: Int) => x - y
    val result3 = minusxy1(3)(5)
    println(result3)



    //闭包
    def minusxy(x: Int) = (y: Int) => x - y
    val f1 = minusxy(10)
    val f2 = minusxy(10)
    println(f1(3) + f2(3))

    val a = Array("Hello", "World")
    val b = Array("hello", "world")
    println(a.corresponds(b)(_.equalsIgnoreCase(_)))

    def runInThread(f1: () => Unit): Unit = {
      new Thread {
        override def run(): Unit = {
          f1()
        }
      }.start()
    }

    runInThread {
      () => println("干活咯！")
//        Thread.sleep(5000)
        println("干完咯！")
    }

    println("=======until========")

    def until(condition: => Boolean)(block: => Unit) {
      if (!condition) {
        block
        until(condition)(block)
      }
    }

    var x = 10
    until(x == 0) {
      x -= 1
      println(x)
    }


  }

}

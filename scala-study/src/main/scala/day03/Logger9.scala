package day03

//自身类型特质
trait Logger9{
  this: Exception =>
  def log(): Unit ={
    println(getMessage)
  }
}

class Outer{
  outer =>
  val a =10
  class Inner{
    inner=>
    val a=20
    def myprint()={
      println(outer.a)
    }
  }

}


object  aa{
  def main(args: Array[String]): Unit = {
    def foo[T](x: List[T])(implicit m: Manifest[T]) = {
      println(m)
      if (m <:< manifest[String])
        println("Hey, this list is full of strings")
      else
        println("Non-stringy list")
    }

    foo(List("one", "two"))
    foo(List(1, 2))
    foo(List("one", 2))
  }

}

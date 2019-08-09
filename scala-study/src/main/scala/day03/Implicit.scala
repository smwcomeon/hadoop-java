package day03

object ImplicitSyllabus {
  def main(args: Array[String]): Unit = {
    implicit def a(d:Double) =d.toInt

    val ila:Int=3.5
    println(ila)
  }
}

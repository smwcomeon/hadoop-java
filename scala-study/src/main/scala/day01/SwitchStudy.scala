package day01

object SwitchStudy {

  def main(args: Array[String]): Unit = {

    //模式匹配
    var result = -10
    val op="_"
    op match {
      case "=" =>result = 1;
      case "_" =>result = -1;
      case _ =>result = 0

    }
    println(result)

    println("===========")

    //模式匹配 守卫
    var sign = 0
    for (ch <- "+-3!") {

      ch match {
        case '+' => sign = 1
        case '-' => sign = -1
        case cs if ch.toString.equals("3") => sign = 33
          println("cs="+cs) //模式匹配成功之后ch的值赋值给cs
        case _ => sign = 0
      }
      println(sign)
    }

    //模式匹配 类型匹配
    val a = 5
    val obj = if(a == 1) 1
    else if(a == 2) "2"
    else if(a == 3) BigInt(3)
    else if(a == 4) Map("aa" -> 1)
    else if(a == 5) Map(1 -> "aa")
    else if(a == 6) Array(1, 2, 3)
    else if(a == 7) Array("aa", 1)
    else if(a == 8) Array("aa")
    val r1 = obj match {
      case x: Int => x
      case s: String => s.toInt
      case BigInt => -1 //不能这么匹配
      case _: BigInt => Int.MaxValue
      case m: Map[String, Int] => "Map[String, Int]类型的Map集合"
      case m: Map[_, _] => "Map集合"
      case a: Array[Int] => "It's an Array[Int]"
      case a: Array[String] => "It's an Array[String]"
      case a: Array[_] => "It's an array of something other than Int"
      case _ => 0
    }
    println(r1 + ", " + r1.getClass.getName)


    for (arr <- Array(Array(0), Array(1, 0), Array(0, 1, 0), Array(1, 1, 0), Array(1, 1, 0, 1))) {
      val result = arr match {
        case Array(0) => "0"
        case Array(x, y) => x + " " + y
        case Array(x, y, z) => x + " " + y + " " + z
        case Array(0, _*) => "0..."
        case _ => "something else"
      }
      println(result)
    }

    println()
    //模式匹配使用
    println("模式匹配使用")

    val  number:Double=36.0
    number match {
      case Square(n)=>println(n)
      case Square(_) =>println("匹配失败")
    }

    //模式匹配unapplySeq
    val namesString = "Alice,Bob,Thomas"
    namesString match {
      case Names(first, second, third) => {
        println("the string contains three people's names")
        println(s"$first $second $third") //Alice Bob Thomas
      }
      case _ => println("nothing matched")
    }

    println(BigInt(10) /% 3)
    val (q, r) = BigInt(10) /% 3

    println()
    println("中置表达式----------------")
    List(1, 7, 2, 9) match {
      case first :: second :: rest => println(first + second + rest.length)
      case _ => 0
    }

    List(1, 7, 2, 9) match {
      case first  :: rest => println(first  + rest.length)
      case _ => 0
    }

  }
}

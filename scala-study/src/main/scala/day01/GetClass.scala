package day01

import scala.util.control.Breaks

object GetClass {
  def main(args: Array[String]): Unit = {

    val xl=10
    val result= if (xl >1) "dayue" else "xiaoyu"
    println(result.getClass.getTypeName)

    //可以用括号代替 点 函数
    println((result getClass) getTypeName)

    //2
    var x2=1
    // While语句本身没有值，即整个While语句的结果是Unit类型的()。
    val while1=while (x2<=10){
      x2 +=1
    }

    println(x2)
    println(while1)


    //while 表达式的中断
    val loop=new Breaks
    loop.breakable{
      while (x2 <= 20) {
        x2 +=1
        if (x2 == 15) {
          loop.break
        }else{

        }
      }
    }
    println(x2)

    var falg= true
    while (falg){
      x2 += 1
      if(x2 >20){
        falg= false
      }
    }
    println(x2)

    //for 循环

    val arrayT= Array(1,2,"3","4",true)
    for (a <-arrayT){
      println(a.getClass.getTypeName+":"+a)
    }

    //to 前闭后闭
    for (a <- 1 to 10){
      println(a)
    }

    println()

    for (a <- 1 until(10)){
      println(a)
    }

    println("yield primaryWord")
    //yield 关键字
    val array3 = Array(1,2,3,4,5)
    val result3 = for (x <- array3) yield x +1
    println(result3.mkString(" ")+result3.getClass.getTypeName)

//    for(i <- 1 to 9 ; j <- 1 to 9){
//      print(i*j +" ")
//      println()
//    }


    //守卫
    for (a <- 1 to 10 if a!=6){
      print(a+" ")
    }
    println()

    //for引入变量
    for(a <- 1 to 10 ; y =a -1){
      print(y +" ")
    }

    //{} 代替（） 可以省略；号
    for{
      a <- 1 to 10
      y = a-1
    }print(y +" ")

  }
}

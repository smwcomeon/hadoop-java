package day01

import scala.math.{BigInt, sqrt}
import scala.util.Random

object ScalaHello {

  def main(args: Array[String]) :Unit={

    println("hello scala")

    //调用函数 方根
//    val sqrt=scala.math.sqrt(10);
    val sqrt1 =sqrt(100);
    println(sqrt1);

//    BigInt.probablePrime() 产生素数的方法
   var probable= BigInt.probablePrime(11,Random);
    println(probable)


    //函数的调用
    println("Hello scala".distinct)

    //apply "hello"相当于一个char数组
    print("hello"(1))
    println("hello".apply(1))

    //update  mkString 输出array
    val array = Array(1,2,3,4)
    array(1)=8
    array.update(0,9)
    println(array.mkString(","))

    //map
    val map1 =Map("nick" ->"19" , "tom"->24)
    println(map1.get("nick"))
    println(map1("nick"))
    println(map1.get("nicknick"))
    //当map集合中没有对应的key 如通不使用map.get方法 会抛异常
//    println(map1("nicknick"))


  }



}

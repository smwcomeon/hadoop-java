package day01

import scala.collection.mutable.ArrayBuffer

object CollectionSyllabus {
  def main(args: Array[String]): Unit = {
      //定长数组
    val array1= Array(1,2,3,4)
    val array1_1 =array1.toBuffer
    //定长数组转变为可变数组
    array1_1.append(6)
    println(array1+"------"+array1.getClass.getTypeName)
    println(array1_1+array1_1.getClass.getTypeName)

    val array01=Array(10)
    array01.update(0,8)
    println(array01.mkString(" "))

    //变长数组 [Int]:表示指定数组中的类型
    val array2=ArrayBuffer[Int]()
    array2.toArray //变长数组变为定长数组

    //使用::向数组中追加数据
    println(1 :: 2 :: 3 :: Nil)

    //多维数组
    val array3= Array.ofDim[Int](10,20)
    array3(1)(1) =10

    //元组
    val touple1 =Tuple3(1,"2",true)
    println(touple1._1)
    println(touple1._2)
    println(touple1._3)

    println("iterator------------")
    val iterator = touple1.productIterator
    println(iterator.next())
    println(iterator.next())
    println(iterator.next())

    println("foreach-----")
    //_表示当前元素 只能使用一次 在_之后就不能使用了
    touple1.productIterator.foreach(println(_))

    val list1=List(1,2)
    val list2 = 3::4::list1
    val list3 = 3::4::list1::Nil
    println(list1)
    println(list2)
    println(list3)

  }
}

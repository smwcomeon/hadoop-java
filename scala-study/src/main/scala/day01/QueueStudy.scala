package day01

import scala.collection.mutable

object QueueStudy {
  def main(args: Array[String]): Unit = {
    val array1= Array(1,2)
    import scala.collection.mutable.Queue

    val q1=new mutable.Queue[Int]
    q1 +=1
    println(q1)

    //追加array 或者list集合用++=
    q1 ++=array1
    println(q1)

    //返回队列的第一个元素
    println(q1.head)

    //返回队列的最后一个元素
    println(q1.last)

    //返回队列的除一个元素外的所有元素  以队列的形式返回
    println(q1.tail)

    //map 映射
    //map新增删除修改数据之前必须把map修改为可变的map  默认是不可变的
    val map1= scala.collection.mutable.Map("Alisa" ->10,"Bob"->20,"Motic"->30)

    //map更新值的方式
    map1("Alisa")=30
    println(map1)

    //当map1中有Alisa时 修改value的值 没有则新增
    map1+=("Alisa"->25,"Ami"->33)
    println(map1)

    //map的删除
    map1-=("Bob","Motic")
    println(map1)

    //map的遍历  遍历kv
    for((k,v)<-map1){
      println(k,v)
    }

    //map的遍历
    for((x)<-map1){
      println(x)
    }

    println("map1  =="+map1)

    //相当于for((x)<-map1) 默认不可变
    map1.iterator.foreach(println(_))

    println("set ----------------")
    val set1 = Set(1,2,3)
    println(set1)

    //可变set
    var set2 = collection.mutable.Set(1,2,3)
    println(set2)
    set2.add(4)
    set2 +=5
    set2.remove(1)
    println(set2)

    set2 -= 5
    println(set2)


    val names = List("Alice", "Bob", "Nick")
    println(names.map(_.toUpperCase))
    println(names.map(x => x.toUpperCase()))  //匿名函数

    println("flatMap---拆分成每个字母")
    println(names.flatMap(_.toUpperCase()))


    val list6 = List(1,2,3,4)
    println(list6.reduceLeft(_-_))  //1-2-3
    println(list6.reduceLeft((x,y)=>x-y))

    println(list6.reduceRight(_-_)) //1-(2-(3-4))

    println(list6.fold(0)((sum,y)=> sum+y)) //(((0+1)+2)+3)+4)
    println(list6.foldLeft(0)((sum,y)=> sum+y)) //
    println(list6.foldRight(0)((sum,y)=> sum+y)) //
    println(list6.foldLeft(0)((sum,y)=> sum-y)) //
    println(list6.foldRight(0)((sum,y)=> sum-y)) //

    //简化版
    println("/: 简化版---------")
    val list4 = List(1, 9, 2, 8)
    val i6 = (0 /: list4)(_ - _)
    println(i6)

    val sentence = "一首现代诗《笑里藏刀》:哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈刀哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"
    //m +  (“一” -> 1, “首” -> 1, “哈” -> 1)
    val i7 = (Map[Char, Int]() /: sentence)((m, c) => m + (c -> (m.getOrElse(c, 0) + 1)))
    println(i7)

    val result =(Map[Char,Int]()/:sentence)((map,char) =>(map+(char->(map.getOrElse(char,0)+1))))

    val i8 = (1 to 10).scanLeft(0)(_ + _)
    println(i8)

    val list1 = List("15837312345", "13737312345", "13811332299")
    val list2 = List(17, 87)
//    println(i1)

    val list3 = List(1, 9, 2, 8)
    val i5 = list3.foldRight(100)(_ - _)
    println(i5)

//    val i6 = list3.foldLeft(100)(_ - _)
    println(list3.foldLeft(100)(_ - _))


  }
}

package day01

object ZipperStudy {
  def main(args: Array[String]): Unit = {

    //拉链操作
    //TODO 三个list的zip未完成

    val list1 = List("15837312345", "13737312345", "13811332299")
    val list2 = List("张三", "李四","王五")
    val list3 = List("男","女","男")
    val list4 =list2 zip list1
    println(list4)


    val list5=list4 zip list3
    for (i<-list5){
      println(i)
    }

    for(x<-list4;y<-list3 ;if list4.indexOf(x)==list3.indexOf(y)){
        y.toArray
      println(y.getClass.getTypeName)
        println("x+y"+(x+y))
    }

    val iterator = List(1, 2, 3, 4, 5).iterator
    while (iterator.hasNext) {
      println(iterator.next())
    }













  }
}

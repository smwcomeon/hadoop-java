package day03

class Creater {
  val range:Int =10
  val env:Array[Int] =  new Array[Int](range)
}

class Ant extends Creater{
  override val range: Int = 2


}

class Ant2 extends {
  override val range = 3
} with Creater
